package aicluster.common.api.popup.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.common.api.popup.dto.PopupGetListParam;
import aicluster.common.common.dao.CmmtPopupNoticeDao;
import aicluster.common.common.dto.PopupListItem;
import aicluster.common.common.entity.CmmtPopupNotice;
import aicluster.common.common.util.CodeExt;
import aicluster.common.common.util.CodeExt.validateMessageExt;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class PopupService {

	@Autowired
	private CmmtPopupNoticeDao cmmtPopupDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig config;

	/**
	 * 팝업 정보 조회
	 *
	 * @param popupId	팝업ID
	 * @return 팝업정보
	 */
	public CmmtPopupNotice select(String popupId)
	{
		CmmtPopupNotice popup = cmmtPopupDao.select(popupId);
		if (popup == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "팝업정보"));
		}

		return popup;
	}

	public CorePagination<PopupListItem> getlist(PopupGetListParam param, CorePaginationParam pageParam)
	{
		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(param.getSystemId())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "포털구분"));
		}

		// 전체 건수 확인
		Long totalItems = cmmtPopupDao.selectList_count(param);

		// 페이지 구간 정보 확인
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);

		// 페이지 목록 조회
		List<PopupListItem> list = cmmtPopupDao.selectList(param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems);

		return new CorePagination<>(info, list);
	}

	public CmmtPopupNotice addPopup(CmmtPopupNotice popup, MultipartFile image) {

		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();
		InvalidationsException errs = new InvalidationsException();

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(popup.getSystemId())) {
			errs.add("systemId", String.format(validateMessageExt.입력없음, "포털구분"));
		}

		if (string.isBlank(popup.getTitle())) {
			errs.add("title", String.format(validateMessageExt.입력없음, "제목"));
		}

		if (popup.getBeginDt() == null) {
			errs.add("beginDt", String.format(validateMessageExt.날짜없음, "시작일시"));
		}

		if (popup.getEndDt() == null) {
			errs.add("endDt", String.format(validateMessageExt.날짜없음, "종료일시"));
		}

		if (popup.getBeginDt() != null && popup.getEndDt() != null) {
			if (popup.getBeginDt().compareTo(popup.getEndDt()) > 0) {
				errs.add("endDt", String.format("종료일이 시작일보다 이전 일입니다."));
			}
		}

		if (popup.getPopupWidth() == null) {
			errs.add("popupWidth", String.format(validateMessageExt.입력없음, "창 너비"));
		}

		if (popup.getPopupHeight() == null) {
			errs.add("popupHeight", String.format(validateMessageExt.입력없음, "창 높이"));
		}

		if (popup.getNewWindow() == null) {
			popup.setNewWindow(false);
		}

		if (errs.size() > 0) {
			throw errs;
		}

		// 첨부파일
		String attachmentId = null;
		if (image != null && image.getSize() > 0) {
			CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), image, CodeExt.uploadExt.imageExt, 0);
			attachmentId = att.getAttachmentId();
		}

		// 입력값 세팅
		CmmtPopupNotice cmmtPopup = CmmtPopupNotice.builder()
				.popupId(string.getNewId("popup-"))
				.systemId(popup.getSystemId())
				.title(popup.getTitle())
				.posting(true)
				.beginDt(popup.getBeginDt())
				.endDt(popup.getEndDt())
				.popupWidth(popup.getPopupWidth())
				.popupHeight(popup.getPopupHeight())
				.imageFileId(attachmentId)
				.linkUrl(popup.getLinkUrl())
				.newWindow(popup.getNewWindow())
				.creatorId(worker.getMemberId())
				.createdDt(now)
				.updaterId(worker.getMemberId())
				.updatedDt(now)
				.build();

		// 정보 입력
		cmmtPopupDao.insert(cmmtPopup);

		cmmtPopup = cmmtPopupDao.select(cmmtPopup.getPopupId());

		// 정보 조회
		return cmmtPopup;
	}

	public CmmtPopupNotice getPopup(String popupId) {

		BnMember worker = SecurityUtils.getCurrentMember();

		// 입력값 검증
		if (string.isBlank(popupId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "팝업 ID"));
		}

		CmmtPopupNotice popup = select(popupId);

		if (!string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자) && popup.getPosting() == false) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자료"));
		}

		return popup;
	}

	public CmmtPopupNotice modifyPopup(CmmtPopupNotice popup, MultipartFile image) {

		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();
		InvalidationsException errs = new InvalidationsException();

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(popup.getPopupId())) {
			errs.add("popupId", String.format(validateMessageExt.입력없음, "팝업 ID"));
		}

		if (string.isBlank(popup.getSystemId())) {
			errs.add("systemId", String.format(validateMessageExt.입력없음, "포털구분"));
		}

		if (string.isBlank(popup.getTitle())) {
			errs.add("title", String.format(validateMessageExt.입력없음, "제목"));
		}

		if (popup.getBeginDt() == null) {
			errs.add("beginDt", String.format(validateMessageExt.날짜없음, "시작날짜"));
		}

		if (popup.getEndDt() == null) {
			errs.add("endDt", String.format(validateMessageExt.날짜없음, "종료날짜"));
		}

		if (popup.getBeginDt() != null && popup.getEndDt() != null) {
			if (popup.getBeginDt().compareTo(popup.getEndDt()) > 0) {
				errs.add("endDt", String.format("종료일이 시작일보다 이전 일입니다."));
			}
		}

		if (popup.getPopupWidth() == null) {
			errs.add("popupWidth", String.format(validateMessageExt.입력없음, "창 너비"));
		}

		if (popup.getPopupHeight() == null) {
			errs.add("popupHeight", String.format(validateMessageExt.입력없음, "창 높이"));
		}

		if (popup.getNewWindow() == null) {
			popup.setNewWindow(false);
		}

		if (errs.size() > 0) {
			throw errs;
		}

		CmmtPopupNotice cmmtPopup = cmmtPopupDao.select(popup.getPopupId());
		if (cmmtPopup == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "팝업정보"));
		}

		// 첨부파일
		String attachmentId = popup.getImageFileId();
		String orgAttachmentId = attachmentId;
		boolean attachementChaged = false;
		if (image != null && image.getSize() > 0) {
			attachementChaged = true;
			CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), image, CodeExt.uploadExt.imageExt, 0);
			attachmentId = att.getAttachmentId();
		}

		// 입력값 세팅
		cmmtPopup.setSystemId(popup.getSystemId());
		cmmtPopup.setTitle(popup.getTitle());
		cmmtPopup.setBeginDt(popup.getBeginDt());
		cmmtPopup.setEndDt(popup.getEndDt());
		cmmtPopup.setPopupWidth(popup.getPopupWidth());
		cmmtPopup.setPopupHeight(popup.getPopupHeight());
		cmmtPopup.setImageFileId(attachmentId);
		cmmtPopup.setLinkUrl(popup.getLinkUrl());
		cmmtPopup.setNewWindow(popup.getNewWindow());
		cmmtPopup.setUpdaterId(worker.getMemberId());
		cmmtPopup.setUpdatedDt(now);

		cmmtPopupDao.update(cmmtPopup);

		cmmtPopup = cmmtPopupDao.select(popup.getPopupId());

		/*
		 * 기존 파일 삭제
		 */
		if (string.isNotBlank(orgAttachmentId) && attachementChaged) {
			attachmentService.removeFile(config.getDir().getUpload(), orgAttachmentId);
		}

		return cmmtPopup;
	}

	public void removePopup(String popupId) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(popupId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "팝업 ID"));
		}

		CmmtPopupNotice cmmtPopup = cmmtPopupDao.select(popupId);
		if (cmmtPopup == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "팝업정보"));
		}

		cmmtPopupDao.delete(popupId);

		// 첨부파일 확인
		String attachmentId = cmmtPopup.getImageFileId();
		if (string.isNotBlank(attachmentId)) {
			attachmentService.removeFile(config.getDir().getUpload(), attachmentId);
		}
	}

	public JsonList<CmmtPopupNotice> getTodayList(String systemId) {

		Date now = new Date();

		// 입력값 검증
		if (string.isBlank(systemId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "시스템 ID"));
		}

		List<CmmtPopupNotice> list = cmmtPopupDao.selectList_time(systemId, now);

		return new JsonList<>(list);
	}

	public CmmtPopupNotice modifyStatus(String popupId, Boolean posting) {

		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(popupId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "팝업 ID"));
		}

		if (posting == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "전시여부"));
		}

		CmmtPopupNotice cmmtPopup = cmmtPopupDao.select(popupId);
		if (cmmtPopup == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "팝업정보"));
		}

		// 입력값 세팅
		cmmtPopup.setPosting(posting);
		cmmtPopup.setUpdaterId(worker.getMemberId());
		cmmtPopup.setUpdatedDt(now);

		cmmtPopupDao.update(cmmtPopup);

		cmmtPopup = cmmtPopupDao.select(popupId);

		return cmmtPopup;
	}

	public void removeImage(String popupId) {

		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(popupId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "팝업 ID"));
		}

		CmmtPopupNotice cmmtPopup = cmmtPopupDao.select(popupId);
		if (cmmtPopup == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "팝업정보"));
		}

		// 이미지 삭제
		String attachmentId = cmmtPopup.getImageFileId();
		String uploadDir = config.getDir().getUpload();

		if (string.isNotBlank(attachmentId)) {
			attachmentService.removeFile(uploadDir, attachmentId);
		}

		cmmtPopup.setImageFileId(null);
		cmmtPopup.setUpdaterId(worker.getMemberId());
		cmmtPopup.setUpdatedDt(now);

		cmmtPopupDao.update(cmmtPopup);
	}

}
