package aicluster.pms.api.pblanc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.pblanc.dto.BsnsPblancDto;
import aicluster.pms.api.pblanc.dto.BsnsPblancListParam;
import aicluster.pms.api.pblanc.dto.BsnsPblancParam;
import aicluster.pms.common.dao.UsptBsnsPblancAppnTaskDao;
import aicluster.pms.common.dao.UsptBsnsPblancDao;
import aicluster.pms.common.dao.UsptBsnsPblancDetailDao;
import aicluster.pms.common.dao.UsptBsnsPblancRceptDao;
import aicluster.pms.common.dto.BsnsPblancListDto;
import aicluster.pms.common.dto.FrontBsnsPblancDto;
import aicluster.pms.common.entity.UsptBsnsPblanc;
import aicluster.pms.common.entity.UsptBsnsPblancAppnTask;
import aicluster.pms.common.entity.UsptBsnsPblancDetail;
import aicluster.pms.common.entity.UsptBsnsPblancRcept;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class BsnsPblancService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private UsptBsnsPblancDao usptBsnsPblancDao;
	@Autowired
	private UsptBsnsPblancRceptDao usptBsnsPblancRceptDao;
	@Autowired
	private UsptBsnsPblancDetailDao usptBsnsPblancDetailDao;
	@Autowired
	private UsptBsnsPblancAppnTaskDao usptBsnsPblancAppnTaskDao;

	/**
	 * 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<BsnsPblancListDto> getList(BsnsPblancListParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setIsExcel(false);
		param.setInsiderId(worker.getMemberId());
		param.setRceptBgndeStart(date.format(string.toDate(param.getRceptBgndeStart()), "yyyyMMdd"));
		param.setRceptBgndeEnd(date.format(string.toDate(param.getRceptBgndeEnd()), "yyyyMMdd"));
		param.setRceptEnddeStart(date.format(string.toDate(param.getRceptEnddeStart()), "yyyyMMdd"));
		param.setRceptEnddeEnd(date.format(string.toDate(param.getRceptEnddeEnd()), "yyyyMMdd"));
		param.setPblancDayStart(date.format(string.toDate(param.getPblancDayStart()), "yyyyMMdd"));
		param.setPblancDayEnd(date.format(string.toDate(param.getPblancDayEnd()), "yyyyMMdd"));

		long totalItems = usptBsnsPblancDao.selectListCount(param);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<BsnsPblancListDto> list = usptBsnsPblancDao.selectList(param);

		//출력할 자료 생성 후 리턴
		CorePagination<BsnsPblancListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 엑셀 다운로드
	 * @param bsnsPblancListParam
	 * @return
	 */
	public List<BsnsPblancListDto> getListExcelDwld(BsnsPblancListParam bsnsPblancListParam){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		bsnsPblancListParam.setIsExcel(true);
		bsnsPblancListParam.setInsiderId(worker.getMemberId());
		return usptBsnsPblancDao.selectList(bsnsPblancListParam);
	}

	/**
	 * 공고 등록
	 * @param bsnsPblancParam
	 * @param fileList
	 * @return
	 */
	public String add(BsnsPblancParam bsnsPblancParam, List<MultipartFile> fileList, MultipartFile thumbnailFile) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException ies = new InvalidationsException();
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getBsnsCd())) {
			ies.add("bsnsCd", String.format(Code.validateMessage.입력없음, "사업코드"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getPblancNm())) {
			ies.add("pblancNm", String.format(Code.validateMessage.입력없음, "공고명"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getPblancNo())) {
			ies.add("pblancNo", String.format(Code.validateMessage.입력없음, "공고번호"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getRceptBgnde())) {
			ies.add("rceptBgnde", String.format(Code.validateMessage.입력없음, "접수기간"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getRceptEndde())) {
			ies.add("rceptEndde", String.format(Code.validateMessage.입력없음, "접수기간"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getBsnsBgnde())) {
			ies.add("rceptBgnde", String.format(Code.validateMessage.입력없음, "사업기간"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getBsnsEndde())) {
			ies.add("rceptEndde", String.format(Code.validateMessage.입력없음, "사업기간"));
		}
		if(bsnsPblancParam.getOrdtmRcrit() == null) {
			ies.add("ordtmRcrit", String.format(Code.validateMessage.입력없음, "모집유형"));
		}

		String bsnsBgnde = null;
		Date bsnsBgndt = string.toDate(bsnsPblancParam.getBsnsBgnde());
		if (bsnsBgndt == null) {
			ies.add("bsnsBgnde", String.format(Code.validateMessage.일자형식오류, "사업기간"));
		}
		bsnsBgnde = date.format(bsnsBgndt, "yyyyMMdd");
		String bsnsEndde = null;
		Date bsnsEnddt = string.toDate(bsnsPblancParam.getBsnsEndde());
		if (bsnsEnddt == null) {
			ies.add("bsnsEndde", String.format(Code.validateMessage.일자형식오류, "사업기간"));
		}
		bsnsEndde = date.format(bsnsEnddt, "yyyyMMdd");

		Date rceptBgndt = string.toDate(bsnsPblancParam.getRceptBgnde());
		String rceptBgnde = date.format(rceptBgndt, "yyyyMMdd");
		if (rceptBgndt == null) {
			ies.add("rceptBgnde", String.format(Code.validateMessage.일자형식오류, "접수기간"));
		}

		Date rceptEnddt = string.toDate(bsnsPblancParam.getRceptEndde());
		String rceptEndde = date.format(rceptEnddt, "yyyyMMdd");
		if (rceptEnddt == null) {
			ies.add("rceptEndde", String.format(Code.validateMessage.일자형식오류, "접수기간"));
		}

		String pblancDay = null;
		if(CoreUtils.string.isNotEmpty(bsnsPblancParam.getPblancDay())){
			Date pblancDt = string.toDate(bsnsPblancParam.getPblancDay());
			if (pblancDt == null) {
				ies.add("pblancDe", String.format(Code.validateMessage.일자형식오류, "공고일"));
			}
			pblancDay = date.format(pblancDt, "yyyyMMdd");
		}

		if (ies.size() > 0) {
			throw ies;
		}

		Date now = new Date();
		UsptBsnsPblanc regInfo = new UsptBsnsPblanc();
		BeanUtils.copyProperties(bsnsPblancParam, regInfo);
		regInfo.setPblancId(CoreUtils.string.getNewId(Code.prefix.공고));
		regInfo.setCreatedDt(now);
		regInfo.setCreatorId(worker.getMemberId());
		regInfo.setUpdatedDt(now);
		regInfo.setUpdaterId(worker.getMemberId());
		regInfo.setRceptBgnde(rceptBgnde);
		regInfo.setRceptEndde(rceptEndde);
		regInfo.setPblancDay(pblancDay);
		regInfo.setBsnsBgnde(bsnsBgnde);
		regInfo.setBsnsEndde(bsnsEndde);
		regInfo.setPblancSttusCd(Code.pblancSttus.접수대기);
		regInfo.setNtce(false);
		regInfo.setEnabled(true);

		int chkPblancNoCount = usptBsnsPblancDao.selectPblancNoCount(regInfo.getPblancNo());
		if(chkPblancNoCount > 0 ) {
			throw new InvalidationException("이미 등록된 공고번호입니다.\n다른 공고번호를 입력하세요");
		}

		if(fileList != null && fileList.size() > 0) {
			CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
			regInfo.setAttachmentGroupId(fileGroupInfo.getAttachmentGroupId());
		}

		if (thumbnailFile != null && thumbnailFile.getSize() > 0) {
			String[] exts = {"jpg", "jpeg", "png", "gif"};
			CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), thumbnailFile, exts, 0);
			regInfo.setThumbnailFileId(att.getAttachmentId());
		}

		usptBsnsPblancDao.insert(regInfo);

		UsptBsnsPblancRcept recptInfo = new UsptBsnsPblancRcept();
		recptInfo.setPblancId(regInfo.getPblancId());
		recptInfo.setBeginDt(CoreUtils.string.toDate(regInfo.getRceptBgnde()));
		recptInfo.setEndDt(CoreUtils.string.toDate(regInfo.getRceptEndde()));
		recptInfo.setCreatedDt(now);
		recptInfo.setCreatorId(worker.getMemberId());
		recptInfo.setUpdatedDt(now);
		recptInfo.setUpdaterId(worker.getMemberId());
		usptBsnsPblancRceptDao.insert(recptInfo);

		if(bsnsPblancParam.getPblancDetailList() != null) {
			for(UsptBsnsPblancDetail detailInfo : bsnsPblancParam.getPblancDetailList()) {
				detailInfo.setPblancId(regInfo.getPblancId());
				detailInfo.setPblancDetailId(CoreUtils.string.getNewId(Code.prefix.공고상세));
				detailInfo.setCreatedDt(now);
				detailInfo.setCreatorId(worker.getMemberId());
				detailInfo.setUpdatedDt(now);
				detailInfo.setUpdaterId(worker.getMemberId());
				usptBsnsPblancDetailDao.insert(detailInfo);
			}
		}

		if(bsnsPblancParam.getTaskList() != null) {
			for(UsptBsnsPblancAppnTask appnTask : bsnsPblancParam.getTaskList()) {
				appnTask.setPblancId(regInfo.getPblancId());
				appnTask.setCreatedDt(now);
				appnTask.setCreatorId(worker.getMemberId());
				usptBsnsPblancAppnTaskDao.insert(appnTask);
			}
		}

		return regInfo.getPblancId();
	}


	/**
	 * 공고 상세조회
	 * @param pblancId
	 * @return
	 */
	public BsnsPblancDto get(String pblancId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		BsnsPblancDto dto = new BsnsPblancDto();
		UsptBsnsPblanc info = usptBsnsPblancDao.select(pblancId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 공고가 없습니다.");
		}
		BeanUtils.copyProperties(info, dto);
		dto.setPblancDetailList(usptBsnsPblancDetailDao.selectList(pblancId));
		dto.setTaskList(usptBsnsPblancAppnTaskDao.selectList(pblancId));
		dto.setAttachFileList(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));
		dto.setRegistDe(CoreUtils.date.format(info.getCreatedDt(), "yyyy-MM-dd"));
		dto.setThumbnailFile(attachmentService.getFileInfo(info.getThumbnailFileId()));

		if(dto.getOrdtmRcrit()) {
			UsptBsnsPblancRcept usptBsnsPblancRcept = new UsptBsnsPblancRcept();
			usptBsnsPblancRcept.setPblancId(pblancId);
			usptBsnsPblancRcept.setBeginRowNum((long) 1);
			usptBsnsPblancRcept.setItemsPerPage((long) 100);
			dto.setRceptList(usptBsnsPblancRceptDao.selectList(usptBsnsPblancRcept));
		} else {
			dto.setRceptList(new ArrayList<UsptBsnsPblancRcept>());
		}

		return dto;
	}

	/**
	 * 썸네일 이미지 파일 다운로드
	 * @param response
	 * @param pblancId
	 */
	public void downloadThumbnail(HttpServletResponse response, String pblancId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblanc info = usptBsnsPblancDao.select(pblancId, worker.getMemberId());
		if(info == null || CoreUtils.string.isEmpty(info.getThumbnailFileId())) {
			throw new InvalidationException("해당되는 썸네일 이미지 파일이 없습니다.");
		}
		attachmentService.downloadFile_contentType(response, config.getDir().getUpload(), info.getThumbnailFileId());
	}

	/**
	 * 첨부파일 전체 다운로드
	 * @param response
	 * @param pblancId
	 * @param attachmentGroupId
	 */
	public void downloadAttFile(HttpServletResponse response, String pblancId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblanc info = usptBsnsPblancDao.select(pblancId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 공고가 없습니다.");
		}
		String orgAttachmentGroupId = usptBsnsPblancDao.selectAttachmentGroupId(pblancId);
		if(CoreUtils.string.isBlank(orgAttachmentGroupId)) {
			throw new InvalidationException("해당되는 첨부파일이 없습니다.");
		}
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), orgAttachmentGroupId, "지원사업공고_첨부파일");
	}

	/**
	 * 공고 수정
	 * @param pblancId
	 * @param bsnsPblancParam
	 * @param fileList
	 */
	public void modify(String pblancId, BsnsPblancParam bsnsPblancParam, List<MultipartFile> fileList, MultipartFile thumbnailFile) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsPblancDao.selectPblancApplcntCount(pblancId) > 0 ) {
			throw new InvalidationException("공고접수 이력이 있는 공고입니다. \n공고를 수정할 수 없습니다.");
		}

		UsptBsnsPblanc info = usptBsnsPblancDao.select(pblancId, worker.getMemberId());
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, info.getChargerAuthorCd())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		InvalidationsException ies = new InvalidationsException();
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getBsnsCd())) {
			//ies.add(String.format(Code.validateMessage.입력없음, "사업코드"));
			ies.add("bsnsCd", String.format(Code.validateMessage.입력없음, "사업코드"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getPblancNm())) {
			ies.add("pblancNm", String.format(Code.validateMessage.입력없음, "공고명"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getPblancNo())) {
			ies.add("pblancNo", String.format(Code.validateMessage.입력없음, "공고번호"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getRceptBgnde())) {
			ies.add("rceptBgnde", String.format(Code.validateMessage.입력없음, "접수기간"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getRceptEndde())) {
			ies.add("rceptEndde", String.format(Code.validateMessage.입력없음, "접수기간"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getBsnsBgnde())) {
			ies.add("rceptBgnde", String.format(Code.validateMessage.입력없음, "사업기간"));
		}
		if(CoreUtils.string.isEmpty(bsnsPblancParam.getBsnsEndde())) {
			ies.add("rceptEndde", String.format(Code.validateMessage.입력없음, "사업기간"));
		}

		String bsnsBgnde = null;
		Date bsnsBgndt = string.toDate(bsnsPblancParam.getBsnsBgnde());
		if (bsnsBgndt == null) {
			ies.add("bsnsBgnde", String.format(Code.validateMessage.일자형식오류, "사업기간"));
		}
		bsnsBgnde = date.format(bsnsBgndt, "yyyyMMdd");
		String bsnsEndde = null;
		Date bsnsEnddt = string.toDate(bsnsPblancParam.getBsnsEndde());
		if (bsnsEnddt == null) {
			ies.add("bsnsEndde", String.format(Code.validateMessage.일자형식오류, "사업기간"));
		}
		bsnsEndde = date.format(bsnsEnddt, "yyyyMMdd");

		Date rceptBgndt = string.toDate(bsnsPblancParam.getRceptBgnde());
		String rceptBgnde = date.format(rceptBgndt, "yyyyMMdd");
		if (rceptBgndt == null) {
			ies.add("rceptBgnde", String.format(Code.validateMessage.일자형식오류, "접수기간"));
		}

		Date rceptEnddt = string.toDate(bsnsPblancParam.getRceptEndde());
		String rceptEndde = date.format(rceptEnddt, "yyyyMMdd");
		if (rceptEnddt == null) {
			ies.add("rceptEndde", String.format(Code.validateMessage.일자형식오류, "접수기간"));
		}

		String pblancDay = null;
		if(CoreUtils.string.isNotEmpty(bsnsPblancParam.getPblancDay())){
			Date pblancDt = string.toDate(bsnsPblancParam.getPblancDay());
			if (pblancDt == null) {
				ies.add("pblancDe", String.format(Code.validateMessage.일자형식오류, "공고일"));
			}
			pblancDay = date.format(pblancDt, "yyyyMMdd");
		}

		if(ies.size() > 0) {
			throw ies;
		}

		Date now = new Date();
		UsptBsnsPblanc regInfo = new UsptBsnsPblanc();
		BeanUtils.copyProperties(bsnsPblancParam, regInfo);
		regInfo.setPblancId(pblancId);
		regInfo.setCreatedDt(now);
		regInfo.setCreatorId(worker.getMemberId());
		regInfo.setUpdatedDt(now);
		regInfo.setUpdaterId(worker.getMemberId());
		regInfo.setRceptBgnde(rceptBgnde);
		regInfo.setRceptEndde(rceptEndde);
		regInfo.setPblancDay(pblancDay);
		regInfo.setBsnsBgnde(bsnsBgnde);
		regInfo.setBsnsEndde(bsnsEndde);

		String attachmentGroupId = usptBsnsPblancDao.selectAttachmentGroupId(pblancId);
		regInfo.setAttachmentGroupId(attachmentGroupId);

		String uploadDir = config.getDir().getUpload();
		if(fileList != null && fileList.size() > 0) {
			if(CoreUtils.string.isNotBlank(regInfo.getAttachmentGroupId())) {
				attachmentService.uploadFiles_toGroup(uploadDir, regInfo.getAttachmentGroupId(), fileList, null, 0);
			} else {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(uploadDir, fileList, null, 0);
				regInfo.setAttachmentGroupId(fileGroupInfo.getAttachmentGroupId());
			}
		}

		if(thumbnailFile != null && thumbnailFile.getSize() > 0) {
			String[] exts = {"jpg", "jpeg", "png", "gif"};
			CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), thumbnailFile, exts, 0);
			regInfo.setThumbnailFileId(att.getAttachmentId());
		}

		if(usptBsnsPblancDao.update(regInfo) != 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}

		/* 사업공고접수 */
		UsptBsnsPblancRcept recptInfo = new UsptBsnsPblancRcept();
		recptInfo.setPblancId(regInfo.getPblancId());
		recptInfo.setBeginDt(CoreUtils.string.toDate(regInfo.getRceptBgnde()));
		recptInfo.setEndDt(CoreUtils.string.toDate(regInfo.getRceptEndde()));
		recptInfo.setUpdatedDt(now);
		recptInfo.setUpdaterId(worker.getMemberId());
		usptBsnsPblancRceptDao.update(recptInfo);

		if(bsnsPblancParam.getPblancDetailList() != null) {
			for(UsptBsnsPblancDetail detailInfo : bsnsPblancParam.getPblancDetailList()) {
				detailInfo.setPblancId(regInfo.getPblancId());
				detailInfo.setCreatedDt(now);
				detailInfo.setCreatorId(worker.getMemberId());
				detailInfo.setUpdatedDt(now);
				detailInfo.setUpdaterId(worker.getMemberId());

				if(CoreUtils.string.equals(Code.flag.등록, detailInfo.getFlag())){
					detailInfo.setPblancDetailId(CoreUtils.string.getNewId(Code.prefix.공고상세));
					usptBsnsPblancDetailDao.insert(detailInfo);
				} else if(CoreUtils.string.equals(Code.flag.수정, detailInfo.getFlag())){
					if(usptBsnsPblancDetailDao.update(detailInfo) != 1)
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
				} else if(CoreUtils.string.equals(Code.flag.삭제, detailInfo.getFlag())){
					if(usptBsnsPblancDetailDao.delete(detailInfo) != 1)
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
				} else {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
				}
			}
		}

		/* 첨부파일 삭제 */
		if(bsnsPblancParam.getDeleteFileList() != null) {
			for(CmmtAtchmnfl attachInfo : bsnsPblancParam.getDeleteFileList()) {
				attachmentService.removeFile_keepEmptyGroup(uploadDir, attachInfo.getAttachmentId());
			}
		}
	}


	/**
	 * 공고 삭제
	 * @param pblancId
	 */
	public void remove(String pblancId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsPblancDao.selectPblancApplcntCount(pblancId) > 0 ) {
			throw new InvalidationException("공고접수 이력이 있는 공고입니다. \n공고를 삭제할 수 없습니다.");
		}
		UsptBsnsPblanc info = usptBsnsPblancDao.select(pblancId, worker.getMemberId());
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, info.getChargerAuthorCd())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		Date now = new Date();
		info.setPblancId(pblancId);
		info.setEnabled(false);
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		if(usptBsnsPblancDao.updateUnable(info) != 1 ) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
		}
	}

	/**
	 * 포털 게시여부 설정
	 * @param pblancId
	 * @param ntce
	 */
	public void modifyNotice(String pblancId, Boolean ntce) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblanc info = usptBsnsPblancDao.select(pblancId, worker.getMemberId());
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, info.getChargerAuthorCd())) {
			throw new InvalidationException("권한이 없습니다.");
		}
		if(ntce == null) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "게시여부"));
		}

		Date now = new Date();
		info.setPblancId(pblancId);
		info.setNtce(ntce);
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		if(usptBsnsPblancDao.updateNtce(info) != 1 ) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}
	}

	/**
	 * 접수상태 설정
	 * @param pblancId
	 * @param pblancSttusCd
	 */
	public void modifyReceipt(String pblancId, String pblancSttusCd) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblanc info = usptBsnsPblancDao.select(pblancId, worker.getMemberId());
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, info.getChargerAuthorCd())) {
			throw new InvalidationException("권한이 없습니다.");
		}
		if(CoreUtils.string.isEmpty(pblancSttusCd)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "접수상태"));
		}
		Date now = new Date();
		info.setPblancId(pblancId);
		info.setPblancSttusCd(pblancSttusCd);
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		if(usptBsnsPblancDao.updatePblancSttus(info) != 1 ) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}
	}


	/**
	 * 조기마감
	 * @param pblancId
	 */
	public void modifyClose(String pblancId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblanc info = usptBsnsPblancDao.select(pblancId, worker.getMemberId());
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, info.getChargerAuthorCd())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		Date now = new Date();
		info.setPblancId(pblancId);
		info.setPblancSttusCd(Code.pblancSttus.접수마감);
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		if(usptBsnsPblancDao.updatePblancSttus(info) != 1 ) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}
	}


	/**
	 * 상시접수처리
	 * @param pblancId
	 */
	public void modifyOrdinaryReceipt(String pblancId) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsnsPblanc info = usptBsnsPblancDao.select(pblancId, worker.getMemberId());
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, info.getChargerAuthorCd())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		if(info.getOrdtmRcrit()) {
			/* 기존의 사업공고접수의 종료일시를 현재 시간으로 업데이트 */
			UsptBsnsPblancRcept recptInfo = new UsptBsnsPblancRcept();
			recptInfo.setPblancId(pblancId);
			recptInfo.setBeginDt(CoreUtils.string.toDate(info.getRceptBgnde()));
			recptInfo.setEndDt(now);
			recptInfo.setCreatedDt(now);
			recptInfo.setCreatorId(worker.getMemberId());
			recptInfo.setUpdatedDt(now);
			recptInfo.setUpdaterId(worker.getMemberId());
			if(usptBsnsPblancRceptDao.update(recptInfo) != 1) {
				throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			}

			/* 사업공고접수의 시작일시를 현재 시간으로 종료일시를 접수종료일로 설정 후 저장 */
			recptInfo.setBeginDt(now);
			recptInfo.setEndDt(CoreUtils.string.toDate(info.getRceptEndde()));
			usptBsnsPblancRceptDao.insert(recptInfo);
		} else {
			throw new InvalidationException("상시모집 공고가 아닙니다.");
		}
	}


	/**
	 * 미리보기
	 * @param pblancId
	 * @return
	 */
	public FrontBsnsPblancDto getPreview(String pblancId) {
		SecurityUtils.checkWorkerIsInsider();
		FrontBsnsPblancDto info = usptBsnsPblancDao.selectFront(pblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 지원공고가 없습니다.");
		}
		info.setDetailList(usptBsnsPblancDetailDao.selectList(pblancId));
		info.setFileList(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));
		return info;
	}
}
