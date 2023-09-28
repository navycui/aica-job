package aicluster.common.api.qna.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.common.api.qna.dto.QnaQuestAddParam;
import aicluster.common.api.qna.dto.QnaQuestListParam;
import aicluster.common.api.qna.dto.QnaQuestModifyParam;
import aicluster.common.common.dao.CmmtEmpInfoDao;
import aicluster.common.common.dao.CmmtMberInfoDao;
import aicluster.common.common.dao.CmmtQnaDao;
import aicluster.common.common.dao.CmmtQnaQuestDao;
import aicluster.common.common.dto.CreatrInfDto;
import aicluster.common.common.dto.QnaQuestListItem;
import aicluster.common.common.entity.CmmtEmpInfo;
import aicluster.common.common.entity.CmmtMberInfo;
import aicluster.common.common.entity.CmmtQna;
import aicluster.common.common.entity.CmmtQnaQuest;
import aicluster.common.common.util.CodeExt;
import aicluster.common.common.util.CodeExt.validateMessageExt;
import aicluster.common.common.util.QnaUtils;
import aicluster.framework.common.dao.FwCodeDao;
import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.entity.CodeDto;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.webutils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class QnaQuestService {

	@Autowired
	private CmmtQnaDao cmmtQnaDao;

	@Autowired
	private FwCodeDao codeDao;

	@Autowired
	private CmmtQnaQuestDao cmmtQnaQuestDao;

	@Autowired
	private CmmtMberInfoDao cmmtMemberDao;

	@Autowired
	private CmmtEmpInfoDao cmmtInsiderDao;

	@Autowired
	private FwUserDao userDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private QnaUtils qnaUtils;

	@Autowired
	private EnvConfig config;

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

	/**
	 * 질의응답 게시글 목록 조회
	 *
	 * @param qnaId : 질의게시판ID
	 * @param param : 검색조건 (questStatus:질의상태코드, categoryCd:카테고리코드, title:질의제목, memberNm:질의자명)
	 * @param pageParam : 페이징
	 * @return CorePagination<CmmtQnaQuest> : 질의게시글 목록
	 */
	public CorePagination<QnaQuestListItem> getList(String qnaId, QnaQuestListParam param, CorePaginationParam pageParam) {
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 권한검사. 답변자이면 모든 글 조회. 그렇지 않은 경우에는 자신의 글만 조회해야 한다.
		String questionerId = null;
		boolean canAnswer = qnaUtils.canAnswer(qnaId, worker.getMemberId());
		if (!canAnswer) {
			questionerId = worker.getMemberId();
			param.setMemberNm(null);
		}

		CmmtQna cmmtQna = cmmtQnaDao.select(qnaId);
		if (cmmtQna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 카테고리를 사용하지 않는 게시판인 경우, 카테고리 검색어를 삭제한다.
		if (!cmmtQna.getCategory()) {
			param.setCategoryCd(null);
		}

		// 접수일 기간 검증
		if (string.isNotBlank(param.getQuestBeginDay()) && string.isNotBlank(param.getQuestEndDay())) {
			if (date.truncatedCompareTo(string.toDate(param.getQuestBeginDay()), string.toDate(param.getQuestEndDay()), Calendar.DATE) > 0) {
				throw new InvalidationException(String.format(validateMessageExt.일시_큰비교오류, "접수일 시작일", "접수일 종료일", "날짜"));
			}
		}
		
		if(string.isNotBlank(param.getQuestStatus())) {
			List<String> questStatusList = new ArrayList<String>();
			String[] questStatusArr = param.getQuestStatus().split(",");
			for(int i = 0; i < questStatusArr.length; i++) {
				questStatusList.add(questStatusArr[i].trim());
			}
			param.setQuestStatusList(questStatusList);
		}

		long totalItems = cmmtQnaQuestDao.selectList_count(qnaId, param, questionerId);
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		List<QnaQuestListItem> list = cmmtQnaQuestDao.selectList(qnaId, param, questionerId, info.getBeginRowNum(), info.getItemsPerPage(), totalItems);

		CorePagination<QnaQuestListItem> dto = new CorePagination<>(info, list);
		return dto;
	}

	public CmmtQnaQuest add(QnaQuestAddParam param, List<MultipartFile> files) {
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}

		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		CmmtQna cmmtQna = cmmtQnaDao.select(param.getQnaId());
		if (cmmtQna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}
		
		long count = cmmtQnaQuestDao.selectMinuteCount(param.getQnaId(), worker.getMemberId());
		if(count > 0) {
			throw new InvalidationException("1분이내 등록한 글이 존재하여 등록할 수 없습니다.");
		}

		/*
		 * 입력검사
		 */
		InvalidationsException exs = new InvalidationsException();
		if (cmmtQna.getCategory()) {
			if (string.isBlank(param.getCategoryCd())) {
				exs.add("categoryCd", "카테고리를 입력하세요.");
			}
			else {
				CodeDto cmmtCode = codeDao.select(cmmtQna.getCategoryCodeGroup(), param.getCategoryCd());
				if (cmmtCode == null) {
					exs.add("categoryCd", "카테고리코드가 존재하지 않습니다.");
				}
			}
		}
		else if (string.isNotBlank(param.getCategoryCd())) {
			exs.add("categoryCd", "카테고리를 사용하지 않는 게시판입니다.");
		}

		if (string.isBlank(param.getTitle())) {
			exs.add("title", "제목을 입력하세요.");
		}

		if (string.isBlank(param.getQuestion())) {
			exs.add("question", "질문내용을 입력하세요.");
		}

		if (exs.size() > 0) {
			throw exs;
		}

		/*
		 * 파일 저장
		 */
		String attachmentGroupId = null;
		if (cmmtQna.getAttachable()) {
			if (files != null && files.size() > 0) {
				String[] exts = cmmtQna.getAttachmentExt().split("/");
				long size = cmmtQna.getAttachmentSize() * 1024 * 1024;
				CmmtAtchmnflGroup attGroup = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), files, exts, size);
				attachmentGroupId = attGroup.getAttachmentGroupId();
			}
		}

		/*
		 * 입력
		 */
		CmmtQnaQuest cmmtQnaQuest = CmmtQnaQuest.builder()
				.questId(string.getNewId("quest-"))
				.qnaId(param.getQnaId())
				.categoryCd(param.getCategoryCd())
				.questSt(CodeExt.questSt.요청)
				.questStDt(now)
				.title(param.getTitle())
				.question(param.getQuestion())
				.questAttachmentGroupId(attachmentGroupId)
				.questionerId(worker.getMemberId())
				.questCreatedDt(now)
				.questUpdatedDt(now)
				.build();
		cmmtQnaQuestDao.insert(cmmtQnaQuest);

		/*
		 * 출력
		 */
		cmmtQnaQuest = cmmtQnaQuestDao.select(cmmtQnaQuest.getQuestId());
		List<CmmtAtchmnfl> fileList = attachmentService.getFileInfos_group(cmmtQnaQuest.getQuestAttachmentGroupId());
		cmmtQnaQuest.setQuestAttachmentList(fileList);
		return cmmtQnaQuest;
	}

	public CmmtQnaQuest get(String qnaId, String questId) {
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}

		CmmtQna cmmtQna = cmmtQnaDao.select(qnaId);
		if (cmmtQna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		CmmtQnaQuest cmmtQnaQuest = cmmtQnaQuestDao.select(questId);
		if (cmmtQnaQuest == null) {
			throw new InvalidationException("글이 존재하지 않습니다.");
		}

		// 권한검사. 자신이 작성한 글이거나, 답변자이어야 한다.
		boolean canRead = qnaUtils.canRead(qnaId, cmmtQnaQuest, worker.getMemberId());
		if (!canRead) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 요청상태이고, 답변자이면, 상태를 접수로 변경한다.
		boolean canAnswer = qnaUtils.canAnswer(qnaId, worker.getMemberId());
		if (string.equals(cmmtQnaQuest.getQuestSt(), CodeExt.questSt.요청) && canAnswer) {
			cmmtQnaQuest.setQuestSt(CodeExt.questSt.접수);
			cmmtQnaQuest.setQuestStDt(now);
			cmmtQnaQuest.setAnswerCreatedDt(now);
			cmmtQnaQuest.setAnswerCreatorId(worker.getMemberId());
			cmmtQnaQuest.setAnswerUpdatedDt(now);
			cmmtQnaQuest.setAnswerUpdaterId(worker.getMemberId());
			cmmtQnaQuestDao.update(cmmtQnaQuest);
		}

		List<CmmtAtchmnfl> questAttachmentList = attachmentService.getFileInfos_group(cmmtQnaQuest.getQuestAttachmentGroupId());
		List<CmmtAtchmnfl> answerAttachmentList = attachmentService.getFileInfos_group(cmmtQnaQuest.getAnswerAttachmentGroupId());
		cmmtQnaQuest.setQuestAttachmentList(questAttachmentList);
		cmmtQnaQuest.setAnswerAttachmentList(answerAttachmentList);
		CmmtMberInfo questioner = cmmtMemberDao.select(cmmtQnaQuest.getQuestionerId());
		CmmtEmpInfo answerer = cmmtInsiderDao.select(cmmtQnaQuest.getAnswerUpdaterId());
		cmmtQnaQuest.setQuestioner(questioner);
		cmmtQnaQuest.setAnswerer(answerer);
		return cmmtQnaQuest;
	}

	public CmmtQnaQuest modify(QnaQuestModifyParam param, List<MultipartFile> files) {
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}

		CmmtQna cmmtQna = cmmtQnaDao.select(param.getQnaId());
		if (cmmtQna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		CmmtQnaQuest cmmtQnaQuest = cmmtQnaQuestDao.select(param.getQuestId());
		if (cmmtQnaQuest == null) {
			throw new InvalidationException("글이 존재하지 않습니다.");
		}

		if (!string.equals(param.getQnaId(), cmmtQnaQuest.getQnaId())) {
			throw new InvalidationException("글이 해당 게시판의 글이 아닙니다.");
		}

		// 권한검사: 자신이 등록한 질문이어야 한다.
		boolean canModify = qnaUtils.canModify(cmmtQnaQuest, worker.getMemberId());
		if (!canModify) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 상태검사: 요청상태인 경우만 수정할 수 있다.
		if (!string.equals(cmmtQnaQuest.getQuestSt(), CodeExt.questSt.요청)) {
			throw new InvalidationException("요청 상태인 경우만 수정할 수 있습니다.");
		}

		/*
		 * 입력검사
		 */
		InvalidationsException exs = new InvalidationsException();
		if (cmmtQna.getCategory()) {
			if (string.isBlank(param.getCategoryCd())) {
				exs.add("categoryCd", "카테고리를 입력하세요.");
			}
			else {
				CodeDto cmmtCode = codeDao.select(cmmtQna.getCategoryCodeGroup(), param.getCategoryCd());
				if (cmmtCode == null) {
					exs.add("categoryCd", "카테고리코드가 존재하지 않습니다.");
				}
			}
		}
		else if (string.isNotBlank(param.getCategoryCd())) {
			exs.add("categoryCd", "카테고리를 사용하지 않는 게시판입니다.");
		}

		if (string.isBlank(param.getTitle())) {
			exs.add("title", "제목을 입력하세요.");
		}

		if (string.isBlank(param.getQuestion())) {
			exs.add("question", "질문내용을 입력하세요.");
		}

		if (exs.size() > 0) {
			throw exs;
		}

		/*
		 * 파일 저장
		 */
		String attachmentGroupId = cmmtQnaQuest.getQuestAttachmentGroupId();
		if (cmmtQna.getAttachable()) {
			if (files != null && files.size() > 0) {
				String[] exts = cmmtQna.getAttachmentExt().split("/");
				long size = cmmtQna.getAttachmentSize() * 1024 * 1024;
				if (string.isBlank(attachmentGroupId)) {
					CmmtAtchmnflGroup attGroup = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), files, exts, size);
					attachmentGroupId = attGroup.getAttachmentGroupId();
				} else {
					attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), attachmentGroupId, files, exts, size);
				}
			}
		}

		/*
		 * 수정
		 */
		cmmtQnaQuest.setCategoryCd(param.getCategoryCd());
		cmmtQnaQuest.setTitle(param.getTitle());
		cmmtQnaQuest.setQuestion(param.getQuestion());
		cmmtQnaQuest.setQuestAttachmentGroupId(attachmentGroupId);
		cmmtQnaQuest.setQuestUpdatedDt(now);
		cmmtQnaQuestDao.update(cmmtQnaQuest);

		/*
		 *  출력
		 */
		cmmtQnaQuest = cmmtQnaQuestDao.select(cmmtQnaQuest.getQuestId());

		List<CmmtAtchmnfl> questAttachmentList = attachmentService.getFileInfos_group(cmmtQnaQuest.getQuestAttachmentGroupId());
		List<CmmtAtchmnfl> answerAttachmentList = attachmentService.getFileInfos_group(cmmtQnaQuest.getAnswerAttachmentGroupId());
		cmmtQnaQuest.setQuestAttachmentList(questAttachmentList);
		cmmtQnaQuest.setAnswerAttachmentList(answerAttachmentList);
		CmmtMberInfo questioner = cmmtMemberDao.select(cmmtQnaQuest.getQuestionerId());
		CmmtEmpInfo answerer = cmmtInsiderDao.select(cmmtQnaQuest.getAnswerUpdaterId());
		cmmtQnaQuest.setQuestioner(questioner);
		cmmtQnaQuest.setAnswerer(answerer);
		return cmmtQnaQuest;
	}

	public void remove(String qnaId, String questId) {
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}

		CmmtQna cmmtQna = cmmtQnaDao.select(qnaId);
		if (cmmtQna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		CmmtQnaQuest cmmtQnaQuest = cmmtQnaQuestDao.select(questId);
		if (cmmtQnaQuest == null) {
			throw new InvalidationException("글이 존재하지 않습니다.");
		}

		if (!string.equals(qnaId, cmmtQnaQuest.getQnaId())) {
			throw new InvalidationException("글이 해당 게시판의 글이 아닙니다.");
		}

		// 권한 검사: 요청 상태인 경우만 삭제할 수 있다.
		if (!string.equals(cmmtQnaQuest.getQuestSt(), CodeExt.questSt.요청)) {
			throw new InvalidationException("요청 상태인 경우만 삭제할 수 있습니다.");
		}

		// 권한 검사: 자신의 글만 삭제가능
		boolean canModify = qnaUtils.canModify(cmmtQnaQuest, worker.getMemberId());
		if (!canModify) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 글 삭제
		cmmtQnaQuestDao.delete(questId);

		// 질문용 첨부파일 삭제
		if (string.isNotBlank(cmmtQnaQuest.getQuestAttachmentGroupId())) {
			attachmentService.removeFiles_group(config.getDir().getUpload(), cmmtQnaQuest.getQuestAttachmentGroupId());
		}

		// 답변용 첨부파일 삭제(없겠지만)
		if (string.isNotBlank(cmmtQnaQuest.getAnswerAttachmentGroupId())) {
			attachmentService.removeFiles_group(config.getDir().getUpload(), cmmtQnaQuest.getAnswerAttachmentGroupId());
		}
	}

	public CmmtQnaQuest answer(String qnaId, String questId, String answer) {
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 게시판 조회
		CmmtQna cmmtQna = cmmtQnaDao.select(qnaId);
		if (cmmtQna == null || !cmmtQna.getEnabled()) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		// 게시글 조회
		CmmtQnaQuest cmmtQnaQuest = cmmtQnaQuestDao.select(questId);
		if (cmmtQnaQuest == null) {
			throw new InvalidationException("글이 존재하지 않습니다.");
		}

		// 권한검사: 답변권한
		boolean canAnswer = qnaUtils.canAnswer(qnaId, worker.getMemberId());
		if (!canAnswer) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 요청/접수 상태인 경우에만 답변을 달 수 있다.
		String[] allowedQuestSts = {CodeExt.questSt.요청, CodeExt.questSt.접수};
		if (!array.contains(allowedQuestSts, cmmtQnaQuest.getQuestSt())) {
			throw new InvalidationException("답변을 달 수 있는 상태가 아닙니다.");
		}

		/*
		 * 입력검사
		 */
		if (string.isBlank(answer)) {
			throw new InvalidationException("답변을 입력하세요.");
		}

		/*
		 * 답변 달기
		 */
		cmmtQnaQuest.setQuestSt(CodeExt.questSt.답변);
		cmmtQnaQuest.setQuestStDt(now);
		cmmtQnaQuest.setAnswer(answer);
		cmmtQnaQuest.setAnswerCreatedDt(now);
		cmmtQnaQuest.setAnswerCreatorId(worker.getMemberId());
		cmmtQnaQuest.setAnswerUpdatedDt(now);
		cmmtQnaQuest.setAnswerUpdaterId(worker.getMemberId());

		cmmtQnaQuestDao.update(cmmtQnaQuest);

		/*
		 * 결과 출력
		 */
		List<CmmtAtchmnfl> questAttachmentList = attachmentService.getFileInfos_group(cmmtQnaQuest.getQuestAttachmentGroupId());
		List<CmmtAtchmnfl> answerAttachmentList = attachmentService.getFileInfos_group(cmmtQnaQuest.getAnswerAttachmentGroupId());
		cmmtQnaQuest.setQuestAttachmentList(questAttachmentList);
		cmmtQnaQuest.setAnswerAttachmentList(answerAttachmentList);
		CmmtMberInfo questioner = cmmtMemberDao.select(cmmtQnaQuest.getQuestionerId());
		CmmtEmpInfo answerer = cmmtInsiderDao.select(cmmtQnaQuest.getAnswerUpdaterId());
		cmmtQnaQuest.setQuestioner(questioner);
		cmmtQnaQuest.setAnswerer(answerer);
		return cmmtQnaQuest;
	}

	/**
	 * 게시글의 작성자 정보를 조회한다.
	 * (관리자페이지에서만 사용되는 API이므로 내부사용자 로그인 검증을 수행한다.)

	 * @param qnaId	질의게시판ID
	 * @param questId	질의글ID
	 * @return CreatrInfDto	작성자정보
	 */
	public CreatrInfDto getQnaQuestrInfo(String qnaId, String questId)
	{
		//1.내부사용자만 조회할 수 있다.
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		//2.질의게시판 정보를 조회한다.
		CmmtQna cmmtQna = cmmtQnaDao.select(qnaId);
		if (cmmtQna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		//3.게시글 정보를 조회한다.
		CmmtQnaQuest cmmtQnaQuest = cmmtQnaQuestDao.select(questId);
		if (cmmtQnaQuest == null) {
			throw new InvalidationException("글이 존재하지 않습니다.");
		}

		//4.권한검사. 자신이 작성한 글이거나, 답변자이어야 한다.
		boolean canRead = qnaUtils.canRead(qnaId, cmmtQnaQuest, worker.getMemberId());
		if (!canRead) {
			throw new InvalidationException("권한이 없습니다.");
		}

		//5.게시글 작성자 정보를 조회한다.
		UserDto userDto = userDao.select(cmmtQnaQuest.getQuestionerId());
		if (userDto == null) {
			throw new InvalidationException("해당 게시글의 작성자 정보가 존재하지 않습니다.");
		}

		CreatrInfDto writerInfDto = new CreatrInfDto();
		property.copyProperties(writerInfDto, userDto);

		//5-1.회원유형명 세팅
		CodeDto code = codeDao.select("MEMBER_TYPE", writerInfDto.getMemberType());
		writerInfDto.setMemberTypeNm(code.getCodeNm());

		//5-2.기업회원인 경우 담당자명 세팅
		if (CodeExt.memberTypeExt.isBzmn(userDto.getMemberType())) {
			CmmtMberInfo member = cmmtMemberDao.select(writerInfDto.getMemberId());
			writerInfDto.setChargerNm(member.getChargerNm());
		}

		//6.개인정보조회 log를 생성한다.
		StringBuilder workCnForm = new StringBuilder();
		workCnForm.append("[%s] 문의게시판의 [%s] 질의글 질문자 정보 조회").append(System.lineSeparator()).append(" - (질의ID : [%s])");
		if (!string.equals(worker.getMemberId(), writerInfDto.getMemberId())) {
			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
					.memberId(worker.getMemberId())
					.memberIp(webutils.getRemoteIp(request))
					.workTypeNm("조회")
					.workCn(String.format(workCnForm.toString(), cmmtQna.getQnaNm(), cmmtQnaQuest.getTitle(), cmmtQnaQuest.getQuestId()))
					.trgterId(writerInfDto.getMemberId())
					.email(writerInfDto.getEmail())
					.birthday(null)
					.mobileNo(writerInfDto.getMobileNo())
					.build();
			indvdlInfSrchUtils.insert(logParam);
		}

		return writerInfDto;
	}
}
