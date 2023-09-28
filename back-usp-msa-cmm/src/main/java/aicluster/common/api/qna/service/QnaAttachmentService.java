package aicluster.common.api.qna.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.common.dao.CmmtQnaDao;
import aicluster.common.common.dao.CmmtQnaQuestDao;
import aicluster.common.common.entity.CmmtQna;
import aicluster.common.common.entity.CmmtQnaQuest;
import aicluster.common.common.util.QnaUtils;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@Service
public class QnaAttachmentService {

	@Autowired
	private CmmtQnaDao cmmtQnaDao;

	@Autowired
	private CmmtQnaQuestDao cmmtQnaQuestDao;

	@Autowired
	private QnaUtils qnaUtils;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig envConfig;

	public JsonList<CmmtAtchmnfl> getList(String qnaId, String questId) {
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}

		CmmtQna qna = cmmtQnaDao.select(qnaId);
		if (qna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		CmmtQnaQuest quest = cmmtQnaQuestDao.select(questId);
		if (quest == null || !string.equals(qnaId, quest.getQnaId())) {
			throw new InvalidationException("글이 존재하지 않습니다.");
		}

		/*
		 * 권한검사
		 */
		boolean canRead = qnaUtils.canRead(qnaId, quest, worker.getMemberId());
		if (!canRead) {
			throw new InvalidationException("권한이 없습니다.");
		}

		List<CmmtAtchmnfl> list = new ArrayList<>();
		if (string.isNotBlank(quest.getQuestAttachmentGroupId())) {
			list = attachmentService.getFileInfos_group(quest.getQuestAttachmentGroupId());
		}
		return new JsonList<>(list);
	}

	public CmmtAtchmnfl get(String qnaId, String questId, String attachmentId) {
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}

		CmmtQna qna = cmmtQnaDao.select(qnaId);
		if (qna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		CmmtQnaQuest quest = cmmtQnaQuestDao.select(questId);
		if (quest == null || !string.equals(qnaId, quest.getQnaId())) {
			throw new InvalidationException("글이 존재하지 않습니다.");
		}

		/*
		 * 권한검사
		 */
		boolean canRead = qnaUtils.canRead(qnaId, quest, worker.getMemberId());
		if (!canRead) {
			throw new InvalidationException("권한이 없습니다.");
		}

		CmmtAtchmnfl att = attachmentService.getFileInfo(attachmentId);
		if (!string.equals(quest.getQuestAttachmentGroupId(), att.getAttachmentGroupId())) {
			throw new InvalidationException("올바르지 않은 접근입니다.");
		}

		return att;
	}

	public void remove(String qnaId, String questId, String attachmentId) {
		BnMember worker = SecurityUtils.getCurrentMember();
		if (worker == null) {
			throw new InvalidationException("권한이 없습니다.");
		}

		CmmtQna qna = cmmtQnaDao.select(qnaId);
		if (qna == null) {
			throw new InvalidationException("게시판이 존재하지 않습니다.");
		}

		CmmtQnaQuest quest = cmmtQnaQuestDao.select(questId);
		if (quest == null || !string.equals(qnaId, quest.getQnaId())) {
			throw new InvalidationException("글이 존재하지 않습니다.");
		}

		/*
		 * 권한
		 */
		boolean canModify = qnaUtils.canModify(quest, worker.getMemberId());
		if (!canModify) {
			throw new InvalidationException("권한이 없습니다.");
		}

		// 파일이 전부 삭제된 경우 파일그룹ID는 삭제되고 리턴값은 false가 반환된다.
		boolean exists = attachmentService.removeFile(envConfig.getDir().getUpload(), attachmentId);

		// 파일그룹ID NULL 처리
		if (!exists) {
			Date now = new Date();
			quest.setQuestAttachmentGroupId(null);
			quest.setQuestUpdatedDt(now);

			cmmtQnaQuestDao.update(quest);
		}
	}

}
