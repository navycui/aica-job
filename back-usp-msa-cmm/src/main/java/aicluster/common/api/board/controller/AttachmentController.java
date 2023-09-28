package aicluster.common.api.board.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.board.service.BoardArticleService;
import aicluster.common.api.board.service.BoardAttachmentService;
import aicluster.common.common.entity.CmmtBbsctt;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/boards/{boardId}/articles/{articleId}")
public class AttachmentController {

	@Autowired
	private BoardArticleService articleService;

	@Autowired
	private BoardAttachmentService boardAttachmentService;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig config;

	/**
	 * 첨부파일 목록 조회
	 * @param boardId
	 * @param articleId
	 * @return
	 */
	@GetMapping("/attachments")
	public JsonList<CmmtAtchmnfl> getList(@PathVariable String boardId, @PathVariable String articleId) {
		return boardAttachmentService.getList(boardId, articleId);
	}

	/**
	 * 첨부파일 삭제
	 * @param boardId
	 * @param articleId
	 * @param attachmentId
	 */
	@DeleteMapping("/attachments/{attachmentId}")
	public void remove(@PathVariable String boardId, @PathVariable String articleId, @PathVariable String attachmentId) {
		boardAttachmentService.remove(boardId, articleId, attachmentId);
	}

	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param boardId
	 * @param articleId
	 * @param attachmentId
	 */
	@GetMapping("/attachments/{attachmentId}")
	public void download(HttpServletResponse response, @PathVariable String boardId, @PathVariable String articleId, @PathVariable String attachmentId) {
		CmmtAtchmnfl att = boardAttachmentService.get(boardId, articleId, attachmentId);
		attachmentService.downloadFile(response, config.getDir().getUpload(), att.getAttachmentId());
	}

	/**
	 * 첨부파일 일괄 다운로드
	 * @param response
	 * @param boardId
	 * @param articleId
	 */
	@GetMapping("/attachments-zip")
	public void downloadZip(HttpServletResponse response, @PathVariable String boardId, @PathVariable String articleId) {
		CmmtBbsctt bbsctt = articleService.getDownloadZip(boardId, articleId);
		String zipFilename = bbsctt.getTitle();
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), bbsctt.getAttachmentGroupId(), zipFilename);
	}
}
