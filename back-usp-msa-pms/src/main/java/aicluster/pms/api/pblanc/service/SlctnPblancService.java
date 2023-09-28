package aicluster.pms.api.pblanc.service;

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
import aicluster.pms.api.pblanc.dto.PblancEvlTargetParam;
import aicluster.pms.api.pblanc.dto.SlctnPblancListParam;
import aicluster.pms.api.pblanc.dto.SlctnPblancParam;
import aicluster.pms.api.pblanc.dto.SlctnPblancTargetListParam;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptBsnsPblancDao;
import aicluster.pms.common.dao.UsptEvlPlanDao;
import aicluster.pms.common.dao.UsptSlctnPblancDao;
import aicluster.pms.common.dao.UsptSlctnPblancDetailDao;
import aicluster.pms.common.dto.BsnsPblancListDto;
import aicluster.pms.common.dto.EvlApplcntListDto;
import aicluster.pms.common.dto.EvlPlanPblancStepDto;
import aicluster.pms.common.dto.FrontSlctnPblancDto;
import aicluster.pms.common.dto.SlctnPblancDto;
import aicluster.pms.common.dto.SlctnPblancListDto;
import aicluster.pms.common.entity.UsptBsnsPblanc;
import aicluster.pms.common.entity.UsptSlctnPblanc;
import aicluster.pms.common.entity.UsptSlctnPblancDetail;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class SlctnPblancService {
	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private UsptSlctnPblancDao usptSlctnPblancDao;
	@Autowired
	private UsptSlctnPblancDetailDao usptSlctnPblancDetailDao;
	@Autowired
	private UsptEvlPlanDao usptEvlPlanDao;
	@Autowired
	private UsptBsnsPblancDao usptBsnsPblancDao;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;

	/**
	 * 선정공고 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<SlctnPblancListDto> getList(SlctnPblancListParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setIsExcel(false);
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptSlctnPblancDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<SlctnPblancListDto> list = usptSlctnPblancDao.selectList(param);
		CorePagination<SlctnPblancListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 선정공고 목록 엑셀 다운로드
	 * @param slctnPblancListParam
	 * @return
	 */
	public List<SlctnPblancListDto> getListExcelDwld(SlctnPblancListParam slctnPblancListParam){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		slctnPblancListParam.setIsExcel(true);
		slctnPblancListParam.setInsiderId(worker.getMemberId());
		return usptSlctnPblancDao.selectList(slctnPblancListParam);
	}


	/**
	 * 공고대상 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<BsnsPblancListDto> getPblancList(SlctnPblancTargetListParam param, Long page) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptBsnsPblancDao.selectPblancListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<BsnsPblancListDto> list = usptBsnsPblancDao.selectPblancList(param);
		CorePagination<BsnsPblancListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 공고대상 선정구분 목록조회
	 * @param pblancId
	 * @param rceptOdr
	 */
	public JsonList<EvlPlanPblancStepDto> getPblancEvlStepList(String pblancId, Integer rceptOdr) {
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptEvlPlanDao.selectPblancEvlStepList(pblancId, rceptOdr));
	}


	/**
	 * 선정공고 대상자 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<EvlApplcntListDto> getPblancEvlTargetList(PblancEvlTargetParam param, Long page) {
		SecurityUtils.checkWorkerIsInsider();

		if(CoreUtils.string.isEmpty(param.getEvlStepId()) && CoreUtils.string.isEmpty(param.getEvlLastSlctnId())) {
			throw new InvalidationException("필수 항목값을 입력해주세요");
		}
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}

		long totalItems = usptBsnsPblancApplcntDao.selectEvlApplcntListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<EvlApplcntListDto> list = usptBsnsPblancApplcntDao.selectEvlApplcntList(param);
		CorePagination<EvlApplcntListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 선정공고 등록
	 * @param slctnPblancParam
	 * @return
	 */
	public String add(SlctnPblancParam slctnPblancParam, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException ies = new InvalidationsException();
		if(CoreUtils.string.isEmpty(slctnPblancParam.getPblancId())) {
			ies.add("모집공고를 선택해주세요!");
		}
		if(CoreUtils.string.isEmpty(slctnPblancParam.getSlctnPblancNm())) {
			ies.add("slctnPblancNm", String.format(Code.validateMessage.입력없음, "공고명"));
		}
		if(CoreUtils.string.isEmpty(slctnPblancParam.getSlctnPblancNo())) {
			ies.add("slctnPblancNo", String.format(Code.validateMessage.입력없음, "공고번호"));
		}
		if(CoreUtils.string.isEmpty(slctnPblancParam.getEvlStepId()) && CoreUtils.string.isEmpty(slctnPblancParam.getEvlLastSlctnId())) {
			ies.add("공고대상을 선택해주세요!");
		}
		if(CoreUtils.string.isNotEmpty(slctnPblancParam.getEvlStepId()) && CoreUtils.string.isNotEmpty(slctnPblancParam.getEvlLastSlctnId())) {
			ies.add("공고대상을 확인하고 등록해주세요!");
		}
		String slctnPblancDay = null;
		if(CoreUtils.string.isNotEmpty(slctnPblancParam.getSlctnPblancDay())) {
			Date slctnPblancDayDt = string.toDate(slctnPblancParam.getSlctnPblancDay());
			if (slctnPblancDayDt == null) {
				ies.add("slctnPblancDay", String.format(Code.validateMessage.일자형식오류, "공고일"));
			}
			slctnPblancDay = date.format(slctnPblancDayDt, "yyyyMMdd");
		}
		if (ies.size() > 0) {
			throw ies;
		}

		UsptBsnsPblanc info = usptBsnsPblancDao.select(slctnPblancParam.getPblancId(), worker.getMemberId());
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, info.getChargerAuthorCd())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		Date now = new Date();
		UsptSlctnPblanc regInfo = new UsptSlctnPblanc();
		BeanUtils.copyProperties(slctnPblancParam, regInfo);
		regInfo.setSlctnPblancId(CoreUtils.string.getNewId(Code.prefix.선정결과공고));
		regInfo.setSlctnPblancDay(slctnPblancDay);
		regInfo.setCreatedDt(now);
		regInfo.setCreatorId(worker.getMemberId());
		regInfo.setUpdatedDt(now);
		regInfo.setUpdaterId(worker.getMemberId());
		regInfo.setNtce(false);
		regInfo.setEnabled(true);

		if(fileList != null && fileList.size() > 0) {
			CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
			regInfo.setAttachmentGroupId(fileGroupInfo.getAttachmentGroupId());
		}

		usptSlctnPblancDao.insert(regInfo);

		if(slctnPblancParam.getDetailList() != null) {
			for(UsptSlctnPblancDetail detailInfo : slctnPblancParam.getDetailList()) {
				detailInfo.setSlctnPblancId(regInfo.getSlctnPblancId());
				detailInfo.setSlctnPblancDetailId(CoreUtils.string.getNewId(Code.prefix.선정결과공고상세));
				detailInfo.setCreatedDt(now);
				detailInfo.setCreatorId(worker.getMemberId());
				detailInfo.setUpdatedDt(now);
				detailInfo.setUpdaterId(worker.getMemberId());
				usptSlctnPblancDetailDao.insert(detailInfo);
			}
		}

		return regInfo.getSlctnPblancId();
	}


	/**
	 * 선정공고 상세조회
	 * @param slctnPblancId
	 * @return
	 */
	public SlctnPblancDto get(String slctnPblancId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnPblancDto info = usptSlctnPblancDao.select(slctnPblancId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 선정공고가 없습니다.");
		}

		if(CoreUtils.string.isNotEmpty(info.getEvlStepId())) {
			info.setPblancTarget(usptEvlPlanDao.selectEvlStep(info.getPblancId(), info.getRceptOdr(), info.getEvlStepId()));
		}
		if(CoreUtils.string.isNotEmpty(info.getEvlLastSlctnId())) {
			info.setPblancTarget(usptEvlPlanDao.selectEvlLastSlctn(info.getPblancId(), info.getRceptOdr(), info.getEvlLastSlctnId()));
		}
		info.setDetailList(usptSlctnPblancDetailDao.selectList(slctnPblancId));
		info.setAttachFileList(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));
		return info;
	}


	/**
	 * 선정공고 수정
	 * @param slctnPblancId
	 * @param slctnPblancParam
	 */
	public void modify(String slctnPblancId, SlctnPblancParam slctnPblancParam, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException ies = new InvalidationsException();
		if(CoreUtils.string.isEmpty(slctnPblancParam.getSlctnPblancNm())) {
			ies.add("slctnPblancNm", String.format(Code.validateMessage.입력없음, "공고명"));
		}
		if(CoreUtils.string.isEmpty(slctnPblancParam.getSlctnPblancNo())) {
			ies.add("slctnPblancNo", String.format(Code.validateMessage.입력없음, "공고번호"));
		}
		String slctnPblancDay = null;
		if(CoreUtils.string.isNotEmpty(slctnPblancParam.getSlctnPblancDay())) {
			Date slctnPblancDayDt = string.toDate(slctnPblancParam.getSlctnPblancDay());
			if (slctnPblancDayDt == null) {
				ies.add("slctnPblancDay", String.format(Code.validateMessage.일자형식오류, "공고일"));
			}
			slctnPblancDay = date.format(slctnPblancDayDt, "yyyyMMdd");
		}
		if (ies.size() > 0) {
			throw ies;
		}

		Date now = new Date();
		UsptSlctnPblanc regInfo = new UsptSlctnPblanc();
		BeanUtils.copyProperties(slctnPblancParam, regInfo);
		regInfo.setSlctnPblancId(slctnPblancId);
		regInfo.setSlctnPblancDay(slctnPblancDay);
		regInfo.setUpdatedDt(now);
		regInfo.setUpdaterId(worker.getMemberId());

		SlctnPblancDto info = usptSlctnPblancDao.select(slctnPblancId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 선정공고가 없습니다.");
		}
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, info.getChargerAuthorCd())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		regInfo.setAttachmentGroupId(info.getAttachmentGroupId());

		String uploadDir = config.getDir().getUpload();
		if(fileList != null && fileList.size() > 0) {
			if(CoreUtils.string.isNotBlank(regInfo.getAttachmentGroupId())) {
				attachmentService.uploadFiles_toGroup(uploadDir, regInfo.getAttachmentGroupId(), fileList, null, 0);
			} else {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(uploadDir, fileList, null, 0);
				regInfo.setAttachmentGroupId(fileGroupInfo.getAttachmentGroupId());
			}
		}

		if(usptSlctnPblancDao.update(regInfo) != 1)
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));

		if(slctnPblancParam.getDetailList() != null) {
			for(UsptSlctnPblancDetail detailInfo : slctnPblancParam.getDetailList()) {
				detailInfo.setSlctnPblancId(regInfo.getSlctnPblancId());
				detailInfo.setCreatedDt(now);
				detailInfo.setCreatorId(worker.getMemberId());
				detailInfo.setUpdatedDt(now);
				detailInfo.setUpdaterId(worker.getMemberId());

				if(CoreUtils.string.equals(Code.flag.등록, detailInfo.getFlag())){
					detailInfo.setSlctnPblancDetailId(CoreUtils.string.getNewId(Code.prefix.선정결과공고상세));
					usptSlctnPblancDetailDao.insert(detailInfo);
				} else  if(CoreUtils.string.equals(Code.flag.수정, detailInfo.getFlag())){
					if(usptSlctnPblancDetailDao.update(detailInfo) != 1)
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
				} else  if(CoreUtils.string.equals(Code.flag.삭제, detailInfo.getFlag())){
					if(usptSlctnPblancDetailDao.delete(detailInfo) != 1)
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
				} else {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
				}
			}
		}

		/* 첨부파일 삭제 */
		if(slctnPblancParam.getDeleteFileList() != null) {
			for(CmmtAtchmnfl attachInfo : slctnPblancParam.getDeleteFileList()) {
				attachmentService.removeFile_keepEmptyGroup(uploadDir, attachInfo.getAttachmentId());
			}
		}
	}


	/**
	 * 포털 게시여부 설정
	 * @param slctnPblancId
	 * @param ntce
	 */
	public void modifyNotice(String slctnPblancId, Boolean ntce) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(ntce == null) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "게시여부"));
		}

		SlctnPblancDto chkInfo = usptSlctnPblancDao.select(slctnPblancId, worker.getMemberId());
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, chkInfo.getChargerAuthorCd())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		Date now = new Date();
		UsptSlctnPblanc info = new UsptSlctnPblanc();
		info.setSlctnPblancId(slctnPblancId);
		info.setNtce(ntce);
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		if(usptSlctnPblancDao.updateNtce(info) != 1 ) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}
	}

	/**
	 * 선정공고 삭제
	 * @param slctnPblancId
	 */
	public void remove(String slctnPblancId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnPblancDto chkInfo = usptSlctnPblancDao.select(slctnPblancId, worker.getMemberId());
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, chkInfo.getChargerAuthorCd())) {
			throw new InvalidationException("권한이 없습니다.");
		}

		Date now = new Date();
		UsptSlctnPblanc info = new UsptSlctnPblanc();
		info.setSlctnPblancId(slctnPblancId);
		info.setEnabled(false);
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		if(usptSlctnPblancDao.updateUnable(info) != 1 ) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
		}
	}


	/**
	 * 천제파일 다운로드
	 * @param response
	 * @param slctnPblancId
	 * @param attachmentGroupId
	 */
	public void downloadAttFile(HttpServletResponse response, String slctnPblancId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnPblancDto info = usptSlctnPblancDao.select(slctnPblancId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 선정공고가 없습니다.");
		}
		if(CoreUtils.string.isBlank(info.getAttachmentGroupId())) {
			throw new InvalidationException("해당되는 첨부파일이 없습니다.");
		}
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), info.getAttachmentGroupId(), "선정결과공고_첨부파일");
	}


	/**
	 * 선정공고 미리보기
	 * @param slctnPblancId
	 * @return
	 */
	public FrontSlctnPblancDto getPreview(String slctnPblancId) {
		SecurityUtils.checkWorkerIsInsider();
		FrontSlctnPblancDto info = usptSlctnPblancDao.selectPreview(slctnPblancId);
		if(info == null) {
			throw new InvalidationException("해당되는 선정결과공고가 없습니다.");
		}
		info.setDetailList(usptSlctnPblancDetailDao.selectList(slctnPblancId));
		info.setFileList(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));
		info.setSlctnList(usptSlctnPblancDao.selectFrontSlctnList(slctnPblancId));
		return info;
	}
}
