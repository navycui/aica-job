package aicluster.common.api.banner.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.common.api.banner.dto.BannerGetListParam;
import aicluster.common.api.banner.dto.BannerParam;
import aicluster.common.common.dao.CmmtBannerDao;
import aicluster.common.common.entity.CmmtBanner;
import aicluster.common.common.util.CodeExt;
import aicluster.common.common.util.CodeExt.validateMessageExt;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class BannerService {

	@Autowired
	private CmmtBannerDao cmmtBannerDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private EnvConfig config;

	public CorePagination<CmmtBanner> getList(BannerGetListParam param, CorePaginationParam pageParam) {
		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		InvalidationsException errs = new InvalidationsException();
		if(string.isBlank(param.getSystemId())) {
			errs.add("systemId", String.format(validateMessageExt.입력없음, "시스템ID"));
		}
		if (string.isBlank(param.getBeginDay())) {
			errs.add("beginDay", String.format(validateMessageExt.입력없음, "조회시작일"));
		} else {
			Date day = string.toDate(param.getBeginDay());
			if (day == null) {
				errs.add("beginDay", String.format(validateMessageExt.입력오류, "조회시작일"));
			}
			else {
				param.setBeginDay(date.format(day, "yyyyMMdd"));
			}
		}

		if (string.isBlank(param.getEndDay())) {
			errs.add("endDay", String.format(validateMessageExt.입력없음, "조회종료일"));
		} else {
			Date day = string.toDate(param.getEndDay());
			if (day == null) {
				errs.add("endDay", String.format(validateMessageExt.입력오류, "조회종료일"));
			}
			else {
				param.setEndDay(date.format(day, "yyyyMMdd"));
			}
		}

		if (errs.size() > 0) {
			throw errs;
		}

		// 건수 조회
		Long totalItems = cmmtBannerDao.selectList_count(param);

		// 조회
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		List<CmmtBanner> list = cmmtBannerDao.selectList(param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems);

		//CorePagination 생성하여 출력
		return new CorePagination<>(info,list);
	}

	public CmmtBanner get(String bannerId) {
		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();
		CmmtBanner cmmtBanner = cmmtBannerDao.select(bannerId);
		if(cmmtBanner == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자료"));
		}
		return cmmtBanner;
	}

	public CmmtBanner modify(BannerParam param, MultipartFile pcImageFile, MultipartFile mobileImageFile) {

		Date now = new Date();
		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		CmmtBanner banner = cmmtBannerDao.select(param.getBannerId());
		if(banner == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "배너정보"));
		}

		InvalidationsException errs = new InvalidationsException();

		if(string.isBlank(param.getBannerNm())) {
			errs.add("bannerNm", String.format(validateMessageExt.입력없음, "배너명"));
		}
		if(param.getBeginDt() == null) {
			errs.add("beginDt", String.format(validateMessageExt.입력없음, "시작일시"));
		}
		if(param.getEndDt() == null) {
			errs.add("endDt", String.format(validateMessageExt.입력없음, "종료일시"));
		}
		if (param.getBeginDt() != null && param.getEndDt() != null) {
			if (param.getBeginDt().compareTo(param.getEndDt()) > 0) {
				errs.add("endDt", String.format("종료일이 시작일보다 이전 일입니다."));
			}
		}

		if (errs.size() > 0) {
			throw errs;
		}

		//기본값 설정
		if(param.getNewWindow() == null) {
			param.setNewWindow(true);
		}
		if(param.getAnimation() == null) {
			param.setAnimation(true);
		}
		if(param.getSortOrder() == null || param.getSortOrder() < 1) {
			param.setSortOrder(1L);
		}
		if(param.getEnabled() == null) {
			param.setEnabled(true);
		}
		if(string.isBlank(param.getSystemId())) {
			param.setSystemId("PORTAL_USP");
		}

		//업로드된 이미지 저장
		String pcImageFileId = banner.getPcImageFileId();
		String mobileImageFileId = banner.getMobileImageFileId();
		String orgPcImageFileId = pcImageFileId;
		String orgMobileImageFileId = mobileImageFileId;
		boolean pcImageFileChanged = false;
		boolean mobileFileChanged = false;

		if(pcImageFile != null && pcImageFile.getSize() > 0) {
			pcImageFileChanged = true;
			CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), pcImageFile, CodeExt.uploadExt.imageExt, 0);
			pcImageFileId = att.getAttachmentId();
		}

		if(mobileImageFile != null && mobileImageFile.getSize() > 0) {
			mobileFileChanged = true;
			CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), mobileImageFile, CodeExt.uploadExt.imageExt, 0);
			mobileImageFileId = att.getAttachmentId();
		}

		//입력값 세팅
		banner.setBannerNm(param.getBannerNm());
		banner.setSystemId(param.getSystemId());
		banner.setBannerType(param.getBannerType());
		banner.setBeginDt(param.getBeginDt());
		banner.setEndDt(param.getEndDt());
		banner.setTargetUrl(param.getTargetUrl());
		banner.setNewWindow(param.getNewWindow());
		banner.setAnimation(param.getAnimation());
		banner.setTextHtml(param.getTextHtml());
		banner.setImageAltCn(param.getImageAltCn());
		banner.setSortOrder(param.getSortOrder());
		banner.setEnabled(param.getEnabled());
		banner.setPcImageFileId(pcImageFileId);
		banner.setMobileImageFileId(mobileImageFileId);
		banner.setUpdatedDt(now);
		banner.setUpdaterId(worker.getMemberId());

		cmmtBannerDao.update(banner);

		CmmtBanner updateBanner = cmmtBannerDao.select(param.getBannerId());

		if (string.isNotBlank(orgPcImageFileId) && pcImageFileChanged) {
			attachmentService.removeFile(config.getDir().getUpload(), orgPcImageFileId);
		}
		if (string.isNotBlank(orgMobileImageFileId) && mobileFileChanged) {
			attachmentService.removeFile(config.getDir().getUpload(), orgMobileImageFileId);
		}

		return updateBanner;

	}

	public CmmtBanner add(BannerParam param, MultipartFile pcImageFile, MultipartFile mobileImageFile) {

		Date now = new Date();
		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();


		InvalidationsException errs = new InvalidationsException();

		if(string.isBlank(param.getBannerNm())) {
			errs.add("bannerNm", String.format(validateMessageExt.입력없음, "배너명"));
		}
		if(param.getBeginDt() == null) {
			errs.add("beginDt", String.format(validateMessageExt.입력없음, "시작일시"));
		}
		if(param.getEndDt() == null) {
			errs.add("endDt", String.format(validateMessageExt.입력없음, "종료일시"));
		}
		if (param.getBeginDt() != null && param.getEndDt() != null) {
			if (param.getBeginDt().compareTo(param.getEndDt()) > 0) {
				errs.add("endDt", String.format("종료일이 시작일보다 이전 일입니다."));
			}
		}

		if (errs.size() > 0) {
			throw errs;
		}
		
		if(string.isBlank(param.getSystemId())) {
			param.setSystemId("PORTAL_USP");
		}

		String pcImageFileId = null;
		String mobileImageFileId = null;
		if(pcImageFile != null && pcImageFile.getSize() > 0) {
			CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), pcImageFile, CodeExt.uploadExt.imageExt, 0);
			pcImageFileId = att.getAttachmentId();
		}

		if(mobileImageFile != null && mobileImageFile.getSize() > 0) {
			CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), mobileImageFile, CodeExt.uploadExt.imageExt, 0);
			mobileImageFileId = att.getAttachmentId();
		}

		// 입력값 세팅 및 입력
		String bannerId = CoreUtils.string.getNewId("banner-");
		CmmtBanner cmmtBanner = CmmtBanner.builder()
				.bannerId(bannerId)
				.bannerNm(param.getBannerNm())
				.systemId(param.getSystemId())
				.bannerType(param.getBannerType())
				.beginDt(param.getBeginDt())
				.endDt(param.getEndDt())
				.targetUrl(param.getTargetUrl())
				.newWindow(param.getNewWindow())
				.animation(param.getAnimation())
				.textHtml(param.getTextHtml())
				.pcImageFileId(pcImageFileId)
				.mobileImageFileId(mobileImageFileId)
				.imageAltCn(param.getImageAltCn())
				.sortOrder(param.getSortOrder())
				.enabled(true)
				.creatorId(worker.getMemberId())
				.createdDt(now)
				.updaterId(worker.getMemberId())
				.updatedDt(now)
				.build();

		cmmtBannerDao.insert(cmmtBanner);

		CmmtBanner insertBanner = cmmtBannerDao.select(bannerId);

		return insertBanner;
	}

	public void remove(String bannerId) {
		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		CmmtBanner cmmtBanner = cmmtBannerDao.select(bannerId);
		if(cmmtBanner == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "배너정보"));
		}

		cmmtBannerDao.delete(bannerId);
		String pcImageFileId = cmmtBanner.getPcImageFileId();
		if(string.isNotBlank(pcImageFileId)) {
			attachmentService.removeFile(config.getDir().getUpload(), pcImageFileId);
		}
		String mobileImageFileId = cmmtBanner.getMobileImageFileId();
		if(string.isNotBlank(mobileImageFileId)) {
			attachmentService.removeFile(config.getDir().getUpload(), mobileImageFileId);
		}
	}

	public void downloadImage(HttpServletResponse response, String bannerId, String platformType) {
		CmmtBanner cmmtBanner = cmmtBannerDao.select(bannerId);
		if(cmmtBanner == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "배너정보"));
		}

		if(platformType.equals("PC")) {
			attachmentService.downloadFile_contentType(response, config.getDir().getUpload(), cmmtBanner.getPcImageFileId());
		}
		if(platformType.equals("MOBILE")) {
			attachmentService.downloadFile_contentType(response, config.getDir().getUpload(), cmmtBanner.getMobileImageFileId());
		}

	}

	public JsonList<CmmtBanner> getTodayList(String systemId) {
		List<CmmtBanner> list = cmmtBannerDao.selectList_today(systemId);
		return new JsonList<>(list);
	}

	public CmmtBanner modifyStatus(String bannerId, Boolean useAt) {
		Date now = new Date();
		BnMember worker = SecurityUtils.getCurrentMember();

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(bannerId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "배너 ID"));
		}

		if (useAt == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "전시여부"));
		}

		CmmtBanner cmmtBanner = cmmtBannerDao.select(bannerId);
		if (cmmtBanner == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "배너정보"));
		}
		
		// 입력값 세팅
		cmmtBanner.setEnabled(useAt);
		cmmtBanner.setUpdaterId(worker.getMemberId());
		cmmtBanner.setUpdatedDt(now);

		cmmtBannerDao.update(cmmtBanner);
		
		return cmmtBanner;
	}

}
