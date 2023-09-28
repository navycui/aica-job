package aicluster.common.api.popup.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.popup.service.PopupService;
import aicluster.common.common.entity.CmmtPopupNotice;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import lombok.RequiredArgsConstructor;

//@Slf4j
@RestController
@RequestMapping("api/popups")
@RequiredArgsConstructor
public class AttachmentPopupController {

	@Autowired
	private PopupService popupService;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig envConfig;

	/**
	 * 이미지 삭제
	 *
	 * @param popupId
	 */
	@DeleteMapping("/{popupId}/image")
	public void remove(@PathVariable String popupId) {
		popupService.removeImage(popupId);
	}

	/**
	 * 이미지 다운로드
	 *
	 * @param response
	 * @param popupId
	 */
	@GetMapping("/{popupId}/image")
	public void download(HttpServletResponse response, @PathVariable String popupId) {
		CmmtPopupNotice cmmtPopup = popupService.select(popupId);
		attachmentService.downloadFile_contentType(response, envConfig.getDir().getUpload(), cmmtPopup.getImageFileId());
	}
}
