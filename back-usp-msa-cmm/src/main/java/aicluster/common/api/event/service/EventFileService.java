package aicluster.common.api.event.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.common.dao.CmmtEventDao;
import aicluster.common.common.entity.CmmtEvent;
import aicluster.common.common.util.CodeExt;
import aicluster.common.common.util.CodeExt.validateMessageExt;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@Service
public class EventFileService {

	@Autowired
	private CmmtEventDao cmmtEventDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig envConfig;

	public JsonList<CmmtAtchmnfl> getList(String eventId) {

		BnMember worker = SecurityUtils.getCurrentMember();

		// 입력값 검증
		if (string.isBlank(eventId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "이벤트ID"));
		}

		CmmtEvent cmmtEvent = cmmtEventDao.select(eventId);
		if (cmmtEvent == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
		}
		else if (cmmtEvent.getPosting() == false) {
			if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
				throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
			}
		}

		List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(cmmtEvent.getAttachmentGroupId());

		return new JsonList<>(list);
	}

	public void downloadAttFile(HttpServletResponse response, String eventId, String attachmentId) {

		BnMember worker = SecurityUtils.getCurrentMember();

		// 입력값 검증
		if (string.isBlank(eventId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "이벤트ID"));
		}

		if (string.isBlank(attachmentId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "첨부파일ID"));
		}

		CmmtEvent cmmtEvent = cmmtEventDao.select(eventId);
		if (cmmtEvent == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
		}
		else if (cmmtEvent.getPosting() == false) {
			if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
				throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
			}
		}

		CmmtAtchmnfl attachment = attachmentService.getFileInfo(attachmentId);
		if (attachment == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "첨부파일"));
		}

		/*
		 * 파일이 해당 event 소속 파일인지 검사
		 */
		if (!string.equals(cmmtEvent.getAttachmentGroupId(), attachment.getAttachmentGroupId())) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "첨부파일"));
		}

		attachmentService.downloadFile(response, envConfig.getDir().getUpload(), attachmentId);
	}

	public void removeAttFile(String eventId, String attachmentId) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(eventId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "이벤트ID"));
		}

		if (string.isBlank(attachmentId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "첨부파일ID"));
		}

		CmmtEvent cmmtEvent = cmmtEventDao.select(eventId);
		if (cmmtEvent == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
		}

		CmmtAtchmnfl attachment = attachmentService.getFileInfo(attachmentId);
		if (attachment == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "첨부파일"));
		}

		boolean groupFileExists = attachmentService.removeFile(envConfig.getDir().getUpload(), attachmentId);
		if (!groupFileExists) {
			Date now = new Date();
			cmmtEvent.setAttachmentGroupId(null);
			cmmtEvent.setUpdatedDt(now);
			cmmtEvent.setUpdaterId(worker.getMemberId());
			cmmtEventDao.update(cmmtEvent);
		}
	}

	public void removeImgFile(String eventId, String imageFileId) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(eventId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "이벤트ID"));
		}

		if (string.isBlank(imageFileId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "이미지ID"));
		}

		CmmtEvent cmmtEvent = cmmtEventDao.select(eventId);
		if (cmmtEvent == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
		}

		if (string.equals(imageFileId, cmmtEvent.getPcThumbnailFileId())) {
			attachmentService.removeFile(envConfig.getDir().getUpload(), cmmtEvent.getPcThumbnailFileId());
			cmmtEvent.setPcThumbnailFileId(null);
		}
		else if (string.equals(imageFileId, cmmtEvent.getMobileThumbnailFileId())) {
			attachmentService.removeFile(envConfig.getDir().getUpload(), cmmtEvent.getMobileThumbnailFileId());
			cmmtEvent.setMobileThumbnailFileId(null);
		}
		else if (string.equals(imageFileId, cmmtEvent.getPcImageFileId())) {
			attachmentService.removeFile(envConfig.getDir().getUpload(), cmmtEvent.getPcImageFileId());
			cmmtEvent.setPcImageFileId(null);
		}
		else if (string.equals(imageFileId, cmmtEvent.getMobileImageFileId())) {
			attachmentService.removeFile(envConfig.getDir().getUpload(), cmmtEvent.getMobileImageFileId());
			cmmtEvent.setMobileImageFileId(null);
		}
		else {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이미지"));
		}

		Date now = new Date();
		cmmtEvent.setUpdatedDt(now);
		cmmtEvent.setUpdaterId(worker.getMemberId());
		cmmtEventDao.update(cmmtEvent);

	}

}
