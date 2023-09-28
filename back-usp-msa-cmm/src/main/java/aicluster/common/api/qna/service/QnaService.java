package aicluster.common.api.qna.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.api.qna.dto.QnaExtsnResponseParam;
import aicluster.common.api.qna.dto.QnaParam;
import aicluster.common.common.dao.CmmtEmpInfoDao;
import aicluster.common.common.dao.CmmtQnaDao;
import aicluster.common.common.dao.CmmtQnaQuestDao;
import aicluster.common.common.dao.CmmtQnaRespondDao;
import aicluster.common.common.dto.QnaQuestStatusCountItem;
import aicluster.common.common.entity.CmmtEmpInfo;
import aicluster.common.common.entity.CmmtQna;
import aicluster.common.common.entity.CmmtQnaRespond;
import aicluster.common.common.util.QnaUtils;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@Service
public class QnaService {

	@Autowired
	private CmmtQnaDao cmmtQnaDao;

	@Autowired
	private CmmtEmpInfoDao cmmtInsiderDao;

	@Autowired
	private CmmtQnaRespondDao cmmtQnaAnswererDao;

	@Autowired
	private CmmtQnaQuestDao cmmtQnaQuestDao;

	@Autowired
	private QnaUtils qnaUtils;

	public JsonList<CmmtQna> getList(String systemId, Boolean enabled, String qnaId, String qnaNm) {
		List<CmmtQna> list = cmmtQnaDao.selectList(systemId, qnaId, qnaNm, enabled);

		return new JsonList<>(list);
	}

	public CmmtQna add(QnaParam param) {
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();

		CmmtQna cmmtQna = param.getQna();
		if (cmmtQna == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		/*
		 * 입력검사
		 */
		InvalidationsException exs = new InvalidationsException();

		CmmtQna dbQna = cmmtQnaDao.select(cmmtQna.getQnaId());
		if (dbQna != null) {
			exs.add("qnaId", "게시판ID가 이미 사용중입니다.");
		}
		else if (!qnaUtils.isValidQnaId(cmmtQna.getQnaId())) {
			exs.add("qnaId", "게시판ID는 '영문/숫자/-'로만 입력해야 합니다.");
		}

		qnaUtils.checkCmmtQna(cmmtQna, exs);

		if (exs.size() > 0) {
			throw exs;
		}

		/*
		 * 답변자 검사
		 */

		if (param.getAnswererList() == null || param.getAnswererList().size() == 0) {
			throw new InvalidationException("답변자를 입력하세요.");
		}

		List<String> paramAnswerList = new ArrayList<String>();
		// 답변자 중복 제거
		for (int i = 0; i < param.getAnswererList().size(); i++) {
			if (!paramAnswerList.contains(param.getAnswererList().get(i))) {
				paramAnswerList.add(param.getAnswererList().get(i));
     		}
		}
		List<CmmtQnaRespond> answererList = new ArrayList<>();
		for (String memberId : paramAnswerList) {
			if (string.isBlank(memberId)) {
				continue;
			}
			CmmtEmpInfo cmmtInsider = cmmtInsiderDao.select(memberId);
			if (cmmtInsider == null) {
				throw new InvalidationException("답변자가 올바르지 않습니다.");
			}

			CmmtQnaRespond cmmtQnaAnswerer = CmmtQnaRespond.builder()
					.qnaId(cmmtQna.getQnaId())
					.memberId(memberId)
					.createdDt(now)
					.creatorId(worker.getMemberId())
					.build();
			answererList.add(cmmtQnaAnswerer);
		}

		if (answererList.size() == 0) {
			throw new InvalidationException("답변자를 입력하세요.");
		}

		/*
		 * 입력
		 */

		cmmtQna.setArticleCnt(0L);
		cmmtQna.setCreatedDt(now);
		cmmtQna.setCreatorId(worker.getMemberId());
		cmmtQna.setUpdatedDt(now);
		cmmtQna.setUpdaterId(worker.getMemberId());

		cmmtQnaDao.insert(cmmtQna);

		cmmtQnaAnswererDao.insertList(answererList);

		cmmtQna = cmmtQnaDao.select(cmmtQna.getQnaId());
		answererList = cmmtQnaAnswererDao.selectList(cmmtQna.getQnaId(), null, null);
		cmmtQna.setAnswererList(answererList);

		return cmmtQna;
	}

	public CmmtQna get(String qnaId) {
		CmmtQna cmmtQna = cmmtQnaDao.select(qnaId);
		if (cmmtQna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		List<CmmtQnaRespond> answererList = cmmtQnaAnswererDao.selectList(qnaId, null, null);
		cmmtQna.setAnswererList(answererList);

		return cmmtQna;
	}

	public CmmtQna modify(QnaParam param) {
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();

		CmmtQna cmmtQna = param.getQna();
		if (cmmtQna == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		/*
		 * 입력검사
		 */

		CmmtQna dbQna = cmmtQnaDao.select(cmmtQna.getQnaId());
		if (dbQna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		InvalidationsException exs = new InvalidationsException();
		qnaUtils.checkCmmtQna(cmmtQna, exs);
		if (exs.size() > 0) {
			throw exs;
		}

		/*
		 * 답변자 검사
		 */

		if (param.getAnswererList() == null || param.getAnswererList().size() == 0) {
			throw new InvalidationException("답변자를 입력하세요.");
		}

		List<String> paramAnswerList = new ArrayList<String>();
		// 답변자 중복 제거
		for (int i = 0; i < param.getAnswererList().size(); i++) {
			if (!paramAnswerList.contains(param.getAnswererList().get(i))) {
				paramAnswerList.add(param.getAnswererList().get(i));
     		}
		}
		List<CmmtQnaRespond> answererList = new ArrayList<>();
		for (String memberId : paramAnswerList) {
			CmmtEmpInfo cmmtInsider = cmmtInsiderDao.select(memberId);
			if (cmmtInsider == null) {
				throw new InvalidationException("답변자가 올바르지 않습니다.");
			}

			CmmtQnaRespond cmmtQnaAnswerer = CmmtQnaRespond.builder()
					.qnaId(cmmtQna.getQnaId())
					.memberId(memberId)
					.createdDt(now)
					.creatorId(worker.getMemberId())
					.build();
			answererList.add(cmmtQnaAnswerer);
		}

		if (answererList.size() == 0) {
			throw new InvalidationException("답변자를 입력하세요.");
		}

		/*
		 * 입력
		 */

		// 게시판 수정
		cmmtQna.setArticleCnt(dbQna.getArticleCnt());
		cmmtQna.setCreatedDt(dbQna.getCreatedDt());
		cmmtQna.setCreatorId(dbQna.getCreatorId());
		cmmtQna.setUpdatedDt(now);
		cmmtQna.setUpdaterId(worker.getMemberId());
		cmmtQnaDao.update(cmmtQna);

		// 답변자 수정(삭제 > 입력)
		cmmtQnaAnswererDao.deleteList(cmmtQna.getQnaId());
		cmmtQnaAnswererDao.insertList(answererList);

		// 결과 출력
		cmmtQna = cmmtQnaDao.select(cmmtQna.getQnaId());
		answererList = cmmtQnaAnswererDao.selectList(cmmtQna.getQnaId(), null, null);
		cmmtQna.setAnswererList(answererList);
		return cmmtQna;
	}

	public void remove(String qnaId) {
		// 게시판 존재 검사
		CmmtQna cmmtQna = cmmtQnaDao.select(qnaId);
		if (cmmtQna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 게시글 존재 검사
		boolean exists = cmmtQnaQuestDao.existsQnaQuests(qnaId);
		if (exists) {
			throw new InvalidationException("게시글이 존재하여 삭제할 수 없습니다.");
		}

		// 답변자 목록 삭제
		cmmtQnaAnswererDao.deleteList(qnaId);

		// 게시판 삭제
		cmmtQnaDao.delete(qnaId);
	}

	public JsonList<QnaQuestStatusCountItem> getQnaQuestCount()
	{
		//내부사용자 로그인 검증
		SecurityUtils.checkWorkerIsInsider();

		//질의게시판별 질의상태 건수 조회
		List<QnaQuestStatusCountItem> list = cmmtQnaDao.selectQuestSt_count();

		//JsonList 타입으로 리턴
		return new JsonList<>(list);
	}

	public QnaExtsnResponseParam getQnaExtsn(String qnaId) {
		QnaExtsnResponseParam result = cmmtQnaDao.getQnaExtsn(qnaId);
		if(result == null) {
			throw new InvalidationException("등록된 첨부파일 확장자 정보가 없습니다.");
		}
		String atchmnflExtsnSet = result.getAtchmnflExtsnSet();
		if(string.isBlank(atchmnflExtsnSet)) {
			throw new InvalidationException("등록된 첨부파일 확장자 정보가 없습니다.");
		}
		return result;
	}

}
