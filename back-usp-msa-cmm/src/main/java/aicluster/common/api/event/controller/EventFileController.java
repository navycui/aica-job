package aicluster.common.api.event.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.event.service.EventFileService;
import aicluster.common.api.event.service.EventService;
import aicluster.common.common.entity.CmmtEvent;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/events")
public class EventFileController {

	@Autowired
	private EventFileService eventFileService;

	@Autowired
	private EventService eventService;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig envConfig;

	/**
	 * 첨부파일 목록 조회
	 * @param eventId
	 * @return
	 */
	@GetMapping("/{eventId}/attachments")
	public JsonList<CmmtAtchmnfl> getList(@PathVariable String eventId) {
		return eventFileService.getList(eventId);
	}

	/**
	 * 첨부파일 다운로드
	 *
	 * @param response
	 * @param eventId
	 * @param attachmentId
	 */
	@GetMapping("/{eventId}/attachments/{attachmentId}")
	public void downloadFile(HttpServletResponse response, @PathVariable String eventId, @PathVariable String attachmentId) {
		eventFileService.downloadAttFile(response, eventId, attachmentId);
	}

	/**
	 * 첨부파일 삭제
	 *
	 * @param eventId
	 * @param attachmentId
	 */
	@DeleteMapping("/{eventId}/attachments/{attachmentId}")
	public void removeFile(@PathVariable String eventId, @PathVariable String attachmentId) {
		eventFileService.removeAttFile(eventId, attachmentId);
	}

	/**
	 * 이미지 다운로드
	 *
	 * @param response
	 * @param eventId
	 * @param imageFileId
	 * @param platformType
	 */
	@GetMapping("/{eventId}/image/{platformType}")
	public void downloadImage(HttpServletResponse response, @PathVariable String eventId, @PathVariable String platformType) {
		CmmtEvent cmmtEvent = eventService.getEventDownloadImage(eventId);
		if (string.equals(platformType, "PC")) {
			attachmentService.downloadFile_contentType(response, envConfig.getDir().getUpload(), cmmtEvent.getPcImageFileId());
		}else if (string.equals(platformType, "MOBILE")) {
			attachmentService.downloadFile_contentType(response, envConfig.getDir().getUpload(), cmmtEvent.getMobileImageFileId());
		}
	}

	/**
	 * 썸네일 다운로드
	 *
	 * @param response
	 * @param eventId
	 * @param thumbnailFileId
	 * @param platformType
	 */
	@GetMapping("/{eventId}/thumbnail/{platformType}")
	public void downloadThumbnail(HttpServletResponse response, @PathVariable String eventId, @PathVariable String platformType) {
		CmmtEvent cmmtEvent = eventService.getEventDownloadImage(eventId);
		if (string.equals(platformType, "PC")) {
			attachmentService.downloadFile_contentType(response, envConfig.getDir().getUpload(), cmmtEvent.getPcThumbnailFileId());
		}else if (string.equals(platformType, "MOBILE")) {
			attachmentService.downloadFile_contentType(response, envConfig.getDir().getUpload(), cmmtEvent.getMobileThumbnailFileId());
		}
	}

	/**
	 * 이미지 삭제
	 *
	 * @param eventId
	 * @param imageId
	 */
	@DeleteMapping("/{eventId}/images/{imageFileId}")
	public void removeImage(@PathVariable String eventId, @PathVariable String imageFileId) {
		eventFileService.removeImgFile(eventId, imageFileId);
	}

}
