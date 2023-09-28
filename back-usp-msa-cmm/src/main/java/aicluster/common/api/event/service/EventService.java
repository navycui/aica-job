package aicluster.common.api.event.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.common.api.event.dto.EventGetListParam;
import aicluster.common.common.dao.CmmtEventCnDao;
import aicluster.common.common.dao.CmmtEventDao;
import aicluster.common.common.dto.EventListItem;
import aicluster.common.common.entity.CmmtEvent;
import aicluster.common.common.entity.CmmtEventCn;
import aicluster.common.common.util.CodeExt;
import aicluster.common.common.util.CodeExt.validateMessageExt;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class EventService {

	@Autowired
	private CmmtEventDao cmmtEventDao;

	@Autowired
	private CmmtEventCnDao cmmtEventCnDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig config;

	public CorePagination<EventListItem> getList(EventGetListParam param, CorePaginationParam pageParam)
	{
		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 검색조건 입력값 검증
		if ( string.isNotBlank(param.getBeginDay()) && string.isNotBlank(param.getEndDay()) ) {
			if ( date.getDiffDays(string.toDate(param.getBeginDay()), string.toDate(param.getEndDay())) < 0) {
				throw new InvalidationException(String.format(validateMessageExt.일시_작은비교오류, "기간 종료일", "기간 시작일", "날짜"));
			}
		}

		// 전체 건수 확인
		Long totalItems = cmmtEventDao.selectList_count(param);

		// 페이지 구간 정보 확인
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);

		// 페이지 목록 조회
		List<EventListItem> list = cmmtEventDao.selectList(param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems);

		return new CorePagination<>(info, list);
	}

	public CmmtEvent addEvent(CmmtEvent event, List<CmmtEventCn> eventCnList, MultipartFile pcThumbnailFile,
			MultipartFile mobileThumbnailFile, MultipartFile pcImageFile, MultipartFile mobileImageFile,
			List<MultipartFile> attachment) {

		Date now = new Date();
		InvalidationsException errs = new InvalidationsException();

		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(event.getEventNm())) {
			errs.add("eventId", String.format(validateMessageExt.입력없음, "이벤트명"));
		}

		if (string.isBlank(event.getBeginDay())) {
			errs.add("beginDay", String.format(validateMessageExt.입력없음, "시작일"));
		} else {
			Date day = string.toDate(event.getBeginDay());
			if (day == null) {
				errs.add("beginDay", String.format(validateMessageExt.입력오류, "시작일"));
			}
			else {
				event.setBeginDay(date.format(day, "yyyyMMdd"));
			}
		}

		if (string.isBlank(event.getEndDay())) {
			errs.add("endDay", String.format(validateMessageExt.입력없음, "종료일"));
		} else {
			Date day = string.toDate(event.getEndDay());
			if (day == null) {
				errs.add("endDay", String.format(validateMessageExt.입력오류, "종료일"));
			}
			else {
				event.setEndDay(date.format(day, "yyyyMMdd"));
			}
		}

		Date beginDay = string.toDate(event.getBeginDay());
		Date endDay = string.toDate(event.getEndDay());
		if (beginDay.compareTo(endDay) > 0) {
			errs.add("endDay", String.format("종료일이 시작일보다 이전 일입니다."));
		}

		if (pcThumbnailFile == null) {
			errs.add("pcThumbnailFile", "pc용 썸네일 이미지가 없습니다.");
		}

		if (mobileThumbnailFile == null) {
			errs.add("mobileThumbnailFile", "mobile용 썸네일 이미지가 없습니다.");
		}

		if (pcImageFile == null) {
			errs.add("pcImageFile", "pc용 이미지가 없습니다.");
		}

		if (mobileImageFile == null) {
			errs.add("mobileImageFile", "mobile용 이미지가 없습니다.");
		}

		if (pcThumbnailFile != null || mobileThumbnailFile != null) {
			if (string.isBlank(event.getThumbnailAltCn())) {
				errs.add("thumbnailAltCn", String.format(validateMessageExt.입력없음, "썸네일 Alt태그 내용"));
			}
		}

		if (pcImageFile != null || mobileImageFile != null) {
			if (string.isBlank(event.getImageAltCn())) {
				errs.add("imageAltCn", String.format(validateMessageExt.입력없음, "이미지 Alt태그 내용"));
			}
		}

		if (errs.size() > 0) {
			throw errs;
		}

		// thumbnail
		String pcThumbnailFileId = null;
		String mobileThumbnailFileId = null;
		if (pcThumbnailFile != null && pcThumbnailFile.getSize() > 0) {
			CmmtAtchmnfl pcThumbAtt = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), pcThumbnailFile, CodeExt.uploadExt.imageExt, 0);
			pcThumbnailFileId = pcThumbAtt.getAttachmentId();
		}
		if (mobileThumbnailFile != null && mobileThumbnailFile.getSize() > 0) {
			CmmtAtchmnfl mobileThumbAtt = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), mobileThumbnailFile, CodeExt.uploadExt.imageExt, 0);
			mobileThumbnailFileId = mobileThumbAtt.getAttachmentId();
		}

		// Image
		String pcImageFileId = null;
		String mobileImageFileId = null;
		if (pcImageFile != null && pcImageFile.getSize() > 0) {
			CmmtAtchmnfl pcImageAtt = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), pcImageFile, CodeExt.uploadExt.imageExt, 0);
			pcImageFileId = pcImageAtt.getAttachmentId();
		}
		if (mobileImageFile != null && mobileImageFile.getSize() > 0) {
			CmmtAtchmnfl mobileImageAtt = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), mobileImageFile, CodeExt.uploadExt.imageExt, 0);
			mobileImageFileId = mobileImageAtt.getAttachmentId();
		}

		// 첨부파일
		String attachmentGroupId = null;
		if (attachment != null && attachment.size() > 0) {
			CmmtAtchmnflGroup attGruop = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), attachment, CodeExt.uploadExt.genericExt, 0);
			attachmentGroupId = attGruop.getAttachmentGroupId();
		}

		// 입력값 세팅 및 입력
		CmmtEvent cmmtEvent = CmmtEvent.builder()
				.eventId(string.getNewId("event-"))
				.eventNm(event.getEventNm())
				.posting(true)
				.beginDay(event.getBeginDay())
				.endDay(event.getEndDay())
				.pcThumbnailFileId(pcThumbnailFileId)
				.mobileThumbnailFileId(mobileThumbnailFileId)
				.thumbnailAltCn(event.getThumbnailAltCn())
				.pcImageFileId(pcImageFileId)
				.mobileImageFileId(mobileImageFileId)
				.imageAltCn(event.getImageAltCn())
				.url(event.getUrl())
				.newWindow(event.getNewWindow())
				.attachmentGroupId(attachmentGroupId)
				.readCnt(0L)
				.creatorId(worker.getMemberId())
				.createdDt(now)
				.updaterId(worker.getMemberId())
				.updatedDt(now)
				.build();

		cmmtEventDao.insert(cmmtEvent);

		if (eventCnList != null && eventCnList.size() > 0) {
			List<CmmtEventCn> cnList = new ArrayList<>();
			for(CmmtEventCn eventCn : eventCnList) {
				eventCn = CmmtEventCn.builder()
						.eventCnId(string.getNewId("event-cn-"))
						.eventId(cmmtEvent.getEventId())
						.sortOrder(eventCn.getSortOrder())
						.header(eventCn.getHeader())
						.articleCn(eventCn.getArticleCn())
						.build();
				cnList.add(eventCn);
			}

			if (cnList.size() > 0) {
				cmmtEventCnDao.insertList(cnList);
			}
		}

		cmmtEvent = cmmtEventDao.select(cmmtEvent.getEventId());
		cmmtEvent.setEventCnList(cmmtEventCnDao.selectList(cmmtEvent.getEventId()));

		return cmmtEvent;
	}

	public CmmtEvent getEvent(String eventId) {

		BnMember worker = SecurityUtils.getCurrentMember();

		// 입력값 검증
		if (string.isBlank(eventId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "이벤트ID"));
		}

		CmmtEvent event = cmmtEventDao.select(eventId);
		if (event == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
		}
		else if (event.getPosting() == false) {
			if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
				throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
			}
		}

		if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
			event.setReadCnt(event.getReadCnt() + 1);
			cmmtEventDao.update(event);
		}

		event.setEventCnList(cmmtEventCnDao.selectList(eventId));

		return event;
	}
	
	/**
	 * 이벤트 상세(이미지, 썸네일 다운로드)
	 * @param eventId
	 * @return
	 */
	public CmmtEvent getEventDownloadImage(String eventId) {

		BnMember worker = SecurityUtils.getCurrentMember();

		// 입력값 검증
		if (string.isBlank(eventId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "이벤트ID"));
		}

		CmmtEvent event = cmmtEventDao.select(eventId);
		if (event == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
		}
		else if (event.getPosting() == false) {
			if (worker == null || !string.equals(worker.getMemberType(), CodeExt.memberType.내부사용자)) {
				throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
			}
		}

		event.setEventCnList(cmmtEventCnDao.selectList(eventId));

		return event;
	}

	public CmmtEvent modifyEvent(CmmtEvent event, List<CmmtEventCn> eventCnList, MultipartFile pcThumbnailFile,
			MultipartFile mobileThumbnailFile, MultipartFile pcImageFile, MultipartFile mobileImageFile,
			List<MultipartFile> attachment) {

		Date now = new Date();
		InvalidationsException errs = new InvalidationsException();

		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(event.getEventId())) {
			errs.add("eventId", String.format(validateMessageExt.입력없음, "이벤트ID"));
		}

		if (string.isBlank(event.getEventNm())) {
			errs.add("eventNm", String.format(validateMessageExt.입력없음, "이벤트명"));
		}

		if (string.isBlank(event.getBeginDay())) {
			errs.add("beginDay", String.format(validateMessageExt.입력없음, "시작일"));
		} else {
			Date day = string.toDate(event.getBeginDay());
			if (day == null) {
				errs.add("beginDay", String.format(validateMessageExt.입력오류, "시작일"));
			}
			else {
				event.setBeginDay(date.format(day, "yyyyMMdd"));
			}
		}

		if (string.isBlank(event.getEndDay())) {
			errs.add("endDay", String.format(validateMessageExt.입력없음, "종료일"));
		} else {
			Date day = string.toDate(event.getEndDay());
			if (day == null) {
				errs.add("endDay", String.format(validateMessageExt.입력오류, "종료일"));
			}
			else {
				event.setEndDay(date.format(day, "yyyyMMdd"));
			}
		}

		Date beginDay = string.toDate(event.getBeginDay());
		Date endDay = string.toDate(event.getEndDay());
		if (beginDay.compareTo(endDay) > 0) {
			errs.add("endDt", String.format("종료일이 시작일보다 이전 일입니다."));
		}

		if (errs.size() > 0) {
			throw errs;
		}

		CmmtEvent cmmtEvent = cmmtEventDao.select(event.getEventId());
		if (cmmtEvent == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
		}

		// thumbnail
		String pcThumbnailFileId = event.getPcThumbnailFileId();
		String mobileThumbnailFileId = event.getMobileThumbnailFileId();
		String orgPcThumbnailFileId = pcThumbnailFileId;
		String orgMobileThumbnailFileId = mobileThumbnailFileId;
		boolean pcThumbnailFileChanged = false;
		boolean mobileThumbnailFileChanged = false;

		if (pcThumbnailFile != null && pcThumbnailFile.getSize() > 0) {
			pcThumbnailFileChanged = true;
			CmmtAtchmnfl pcThumbAtt = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), pcThumbnailFile, CodeExt.uploadExt.imageExt, 0);
			pcThumbnailFileId = pcThumbAtt.getAttachmentId();
		}
		if (mobileThumbnailFile != null && mobileThumbnailFile.getSize() > 0) {
			mobileThumbnailFileChanged = true;
			CmmtAtchmnfl mobileThumbAtt = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), mobileThumbnailFile, CodeExt.uploadExt.imageExt, 0);
			mobileThumbnailFileId = mobileThumbAtt.getAttachmentId();
		}

		// Image
		String pcImageFileId = event.getPcImageFileId();
		String mobileImageFileId = event.getMobileImageFileId();
		String orgPcImageFileId = pcImageFileId;
		String orgMobileImageFileId = mobileImageFileId;
		boolean pcImageFileChanged = false;
		boolean mobileImageFileChanged = false;
		if (pcImageFile != null && pcImageFile.getSize() > 0) {
			pcImageFileChanged = true;
			CmmtAtchmnfl pcImageAtt = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), pcImageFile, CodeExt.uploadExt.imageExt, 0);
			pcImageFileId = pcImageAtt.getAttachmentId();
		}
		if (mobileImageFile != null && mobileImageFile.getSize() > 0) {
			mobileImageFileChanged = true;
			CmmtAtchmnfl mobileImageAtt = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), mobileImageFile, CodeExt.uploadExt.imageExt, 0);
			mobileImageFileId = mobileImageAtt.getAttachmentId();
		}

		// 첨부파일
		String attachmentGroupId = event.getAttachmentGroupId();
		if (attachment != null && attachment.size() > 0) {
			if (string.isBlank(attachmentGroupId)) {
				CmmtAtchmnflGroup attGruop = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), attachment, CodeExt.uploadExt.genericExt, 0);
				attachmentGroupId = attGruop.getAttachmentGroupId();
			}
			else {
				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), attachmentGroupId, attachment, CodeExt.uploadExt.genericExt, 0);
			}
		}

		// 입력값 세팅
		cmmtEvent.setEventNm(event.getEventNm());
		cmmtEvent.setBeginDay(event.getBeginDay());
		cmmtEvent.setEndDay(event.getEndDay());
		cmmtEvent.setPcThumbnailFileId(pcThumbnailFileId);
		cmmtEvent.setMobileThumbnailFileId(mobileThumbnailFileId);
		cmmtEvent.setPcImageFileId(pcImageFileId);
		cmmtEvent.setMobileImageFileId(mobileImageFileId);
		cmmtEvent.setUrl(event.getUrl());
		cmmtEvent.setNewWindow(event.getNewWindow());
		cmmtEvent.setAttachmentGroupId(attachmentGroupId);
		cmmtEvent.setUpdaterId(worker.getMemberId());
		cmmtEvent.setUpdatedDt(now);

		cmmtEventDao.update(cmmtEvent);

		if (eventCnList != null && eventCnList.size() > 0) {
			List<CmmtEventCn> cmmtEventCn = cmmtEventCnDao.selectList(event.getEventId());
			if (cmmtEventCn != null) {
				cmmtEventCnDao.deleteList(event.getEventId());
			}
			List<CmmtEventCn> cnList = new ArrayList<>();
			for(CmmtEventCn eventCn : eventCnList) {
				eventCn = CmmtEventCn.builder()
						.eventCnId(string.getNewId("event-cn-"))
						.eventId(cmmtEvent.getEventId())
						.sortOrder(eventCn.getSortOrder())
						.header(eventCn.getHeader())
						.articleCn(eventCn.getArticleCn())
						.build();
				cnList.add(eventCn);
			}

			if (cnList.size() > 0) {
				cmmtEventCnDao.insertList(cnList);
			}
		}

		/*
		 * 결과 조회
		 */
		cmmtEvent = cmmtEventDao.select(cmmtEvent.getEventId());
		cmmtEvent.setEventCnList(cmmtEventCnDao.selectList(cmmtEvent.getEventId()));

		/*
		 * 기존 파일 삭제
		 */
		if (string.isNotBlank(orgPcThumbnailFileId) && pcThumbnailFileChanged) {
			attachmentService.removeFile(config.getDir().getUpload(), orgPcThumbnailFileId);
		}
		if (string.isNotBlank(orgMobileThumbnailFileId) && mobileThumbnailFileChanged) {
			attachmentService.removeFile(config.getDir().getUpload(), orgMobileThumbnailFileId);
		}
		if (string.isNotBlank(orgPcImageFileId) && pcImageFileChanged) {
			attachmentService.removeFile(config.getDir().getUpload(), orgPcImageFileId);
		}
		if (string.isNotBlank(orgMobileImageFileId) && mobileImageFileChanged) {
			attachmentService.removeFile(config.getDir().getUpload(), orgMobileImageFileId);
		}

		return cmmtEvent;
	}

	public void removeEvent(String eventId) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(eventId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "이벤트ID"));
		}

		CmmtEvent cmmtEvent = cmmtEventDao.select(eventId);
		if (cmmtEvent == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
		}

		List<CmmtEventCn> cmmtEventCn = cmmtEventCnDao.selectList(eventId);
		if (cmmtEventCn != null) {
			cmmtEventCnDao.deleteList(eventId);
		}

		cmmtEventDao.delete(eventId);

		// thumbnail
		String pcThumbnailFileId = cmmtEvent.getPcThumbnailFileId();
		String mobileThumbnailFileId = cmmtEvent.getMobileThumbnailFileId();
		if (string.isNotBlank(pcThumbnailFileId)) {
			attachmentService.removeFile(config.getDir().getUpload(), pcThumbnailFileId);
		}

		if (string.isNotBlank(mobileThumbnailFileId)) {
			attachmentService.removeFile(config.getDir().getUpload(), mobileThumbnailFileId);
		}

		// Image
		String pcImageFileId = cmmtEvent.getPcImageFileId();
		String mobileImageFileId = cmmtEvent.getMobileImageFileId();
		if (string.isNotBlank(pcImageFileId)) {
			attachmentService.removeFile(config.getDir().getUpload(), pcImageFileId);
		}

		if (string.isNotBlank(mobileImageFileId)) {
			attachmentService.removeFile(config.getDir().getUpload(), mobileImageFileId);
		}

		// 첨부파일
		String attachmentGroupId = cmmtEvent.getAttachmentGroupId();
		if (string.isNotBlank(attachmentGroupId)) {
			attachmentService.removeFiles_group(config.getDir().getUpload(), attachmentGroupId);
		}

	}

	public CmmtEvent modifyStatus(String eventId, Boolean posting) {

		Date now = new Date();

		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(eventId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "이벤트ID"));
		}
		if (posting == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "게시여부"));
		}

		CmmtEvent cmmtEvent = cmmtEventDao.select(eventId);
		if (cmmtEvent == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "이벤트정보"));
		}

		// 입력값 세팅
		cmmtEvent.setPosting(posting);
		cmmtEvent.setUpdaterId(worker.getMemberId());
		cmmtEvent.setUpdatedDt(now);

		cmmtEventDao.update(cmmtEvent);

		cmmtEvent = cmmtEventDao.select(eventId);

		return cmmtEvent;
	}
}
