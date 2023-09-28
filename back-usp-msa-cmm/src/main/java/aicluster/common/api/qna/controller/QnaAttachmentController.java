package aicluster.common.api.qna.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.qna.service.QnaAttachmentService;
import aicluster.common.api.qna.service.QnaQuestService;
import aicluster.common.common.entity.CmmtQnaQuest;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/qna/{qnaId}/quests/{questId}")
public class QnaAttachmentController {

	@Autowired
	private QnaQuestService questService;

	@Autowired
	private QnaAttachmentService service;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig config;

	/**
	 * 질문파일 목록 조회
	 *
	 * @param qnaId
	 * @param questId
	 * @return
	 */
	@GetMapping("/attachments")
	public JsonList<CmmtAtchmnfl> getList(@PathVariable String qnaId, @PathVariable String questId) {
		return service.getList(qnaId, questId);
	}

	/**
	 * 첨부파일 다운로드
	 *
	 * @param response
	 * @param qnaId
	 * @param questId
	 * @param attachmentId
	 */
	@GetMapping("/attachments/{attachmentId}")
	public void download(HttpServletResponse response, @PathVariable String qnaId, @PathVariable String questId, @PathVariable String attachmentId) {
		CmmtAtchmnfl att = service.get(qnaId, questId, attachmentId);
		attachmentService.downloadFile(response, config.getDir().getUpload(), att.getAttachmentId());
	}

	/**
	 * 첨부파일 삭제
	 *
	 * @param qnaId
	 * @param questId
	 * @param attachmentId
	 */
	@DeleteMapping("/attachments/{attachmentId}")
	public void remove(@PathVariable String qnaId, @PathVariable String questId, @PathVariable String attachmentId) {
		service.remove(qnaId, questId, attachmentId);
	}

	/**
	 * 질의응답 질문 파일 일괄 다운로드
	 * @param response
	 * @param qnaId
	 * @param questId
	 */
	@GetMapping("/attachments-zip")
	public void downloadZip(HttpServletResponse response, @PathVariable String qnaId, @PathVariable String questId) {
		CmmtQnaQuest quest = questService.get(qnaId, questId);
		String zipFilename = quest.getTitle();
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), quest.getQuestAttachmentGroupId(), zipFilename);
	}
}
