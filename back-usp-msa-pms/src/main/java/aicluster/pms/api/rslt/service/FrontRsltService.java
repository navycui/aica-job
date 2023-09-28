package aicluster.pms.api.rslt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.bsns.dto.RsltIdxDetailIem;
import aicluster.pms.api.bsns.dto.RsltIdxStdIem;
import aicluster.pms.api.rslt.dto.BsnsMvnRsltParam;
import aicluster.pms.api.rslt.dto.FrontRsltListParam;
import aicluster.pms.api.rslt.dto.RsltDto;
import aicluster.pms.api.rslt.dto.RsltHistDto;
import aicluster.pms.api.rslt.dto.RsltIdxIemDto;
import aicluster.pms.api.rslt.dto.RsltIdxIemHistDto;
import aicluster.pms.api.rslt.dto.RsltIdxParam;
import aicluster.pms.api.rslt.dto.RsltMakeupDto;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptBsnsRsltIdxDao;
import aicluster.pms.common.dao.UsptRsltDao;
import aicluster.pms.common.dao.UsptRsltHistDao;
import aicluster.pms.common.dao.UsptRsltIdxIemCnDao;
import aicluster.pms.common.dao.UsptRsltIdxIemCnHistDao;
import aicluster.pms.common.dao.UsptRsltIdxIemDao;
import aicluster.pms.common.dao.UsptRsltIdxIemHistDao;
import aicluster.pms.common.dto.ApplcntBsnsBasicDto;
import aicluster.pms.common.dto.FrontRsltListDto;
import aicluster.pms.common.dto.RsltHistPresentnDto;
import aicluster.pms.common.entity.UsptBsnsRsltIdx;
import aicluster.pms.common.entity.UsptRslt;
import aicluster.pms.common.entity.UsptRsltHist;
import aicluster.pms.common.entity.UsptRsltIdxIem;
import aicluster.pms.common.entity.UsptRsltIdxIemCn;
import aicluster.pms.common.entity.UsptRsltIdxIemCnHist;
import aicluster.pms.common.entity.UsptRsltIdxIemHist;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class FrontRsltService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private UsptRsltDao usptRsltDao;
	@Autowired
	private UsptRsltHistDao usptRsltHistDao;
	@Autowired
	private UsptRsltIdxIemDao usptRsltIdxIemDao;
	@Autowired
	private UsptRsltIdxIemHistDao usptRsltIdxIemHistDao;
	@Autowired
	private UsptRsltIdxIemCnDao usptRsltIdxIemCnDao;
	@Autowired
	private UsptRsltIdxIemCnHistDao usptRsltIdxIemCnHistDao;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;
	@Autowired
	private UsptBsnsRsltIdxDao usptBsnsRsltIdxDao;

	/**
	 * 성과목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<FrontRsltListDto> getList(FrontRsltListParam param, Long page){
		BnMember worker = SecurityUtils.checkLogin();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setMemberId(worker.getMemberId());
		long totalItems = usptRsltDao.selectFrontListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<FrontRsltListDto> list = usptRsltDao.selectFrontList(param);
		CorePagination<FrontRsltListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 보완요청 조회
	 * @param rsltId
	 * @return
	 */
	public RsltMakeupDto getMakeup(String rsltId) {
		SecurityUtils.checkLogin();
		UsptRslt rsltInfo = usptRsltDao.select(rsltId);
		if(rsltInfo == null) {
			throw new InvalidationException("요청한 성과정보가 존재하지않습니다.");
		}
		RsltMakeupDto dto = new RsltMakeupDto();
		dto.setMakeupReqDate(CoreUtils.date.format(rsltInfo.getMakeupReqDe(), "yyyy-MM-dd HH:mm"));
		dto.setMakeupReqCn(rsltInfo.getMakeupReqCn());
		if(!CoreUtils.string.isBlank(rsltInfo.getMakeupAttachmentGroupId())) {
			dto.setAttachFileList(attachmentService.getFileInfos_group(rsltInfo.getMakeupAttachmentGroupId()));
		}
		return dto;
	}

	/**
	 * 보완요청 첨부파일 다운로드
	 * @param response
	 * @param rsltId
	 * @param attachmentId
	 */
	public void getMakeupAtchmnfl(HttpServletResponse response, String rsltId, String attachmentId) {
		SecurityUtils.checkLogin();
		UsptRslt rsltInfo = usptRsltDao.select(rsltId);
		if(rsltInfo == null) {
			throw new InvalidationException("요청한 성과정보가 존재하지않습니다.");
		}
		if(CoreUtils.string.isBlank(attachmentId)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "첨부파일ID"));
		}

		List<CmmtAtchmnfl> fileList = attachmentService.getFileInfos_group(rsltInfo.getMakeupAttachmentGroupId());
		Optional<CmmtAtchmnfl> opt = fileList.stream().filter(x-> CoreUtils.string.equals(x.getAttachmentId(), attachmentId)).findFirst();
		if(opt.isPresent()) {
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		} else {
			throw new InvalidationException("요청한 정보의 첨부파일이 존재하지않습니다.");
		}
	}


	/**
	 * 성과 상세조회
	 * @param rsltId
	 * @return
	 */
	public RsltDto get(String rsltId) {
		SecurityUtils.checkLogin();
		UsptRslt info = usptRsltDao.select(rsltId);
		if(info == null) {
			throw new InvalidationException("요청한 성과정보가 존재하지않습니다.");
		}
		RsltDto dto = new RsltDto();
		dto.setRsltYear(info.getRsltYear());
		dto.setRsltSttusCd(info.getRsltSttusCd());
		dto.setRsltSttus(info.getRsltSttus());
		dto.setBasicInfo(usptBsnsPblancApplcntDao.selectBsnsBasic(info.getApplyId(), null));
		dto.setAttachFileList(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));

		List<RsltIdxIemDto> rsltIdxIemDtoList = new ArrayList<RsltIdxIemDto>();
		List<UsptRsltIdxIem> rsltIdxIemList = usptRsltIdxIemDao.selectList(rsltId);
		if(rsltIdxIemList != null) {
			for(UsptRsltIdxIem idxIemInfo : rsltIdxIemList) {
				RsltIdxIemDto iemDto = new RsltIdxIemDto();
				iemDto.setRsltIdxId(idxIemInfo.getRsltIdxId());
				iemDto.setRsltIdxIemId(idxIemInfo.getRsltIdxIemId());
				iemDto.setRsltIdxNm(idxIemInfo.getRsltIdxNm());
				iemDto.setStdIdx(idxIemInfo.getStdIdx());
				iemDto.setRsltIdxTypeCd(idxIemInfo.getRsltIdxTypeCd());
				iemDto.setPrufFile(attachmentService.getFileInfo(idxIemInfo.getPrufAttachmentId()));
				iemDto.setRsltIdxIemCnList(usptRsltIdxIemCnDao.selectList(idxIemInfo.getRsltIdxIemId()));
				rsltIdxIemDtoList.add(iemDto);
			}
		}
		dto.setRsltIdxIemList(rsltIdxIemDtoList);
		return dto;
	}

	/**
	 * 성과 제출
	 * @param rsltId
	 * @param infoList
	 * @param rsltIdxFileList
	 * @param attachFileList
	 */
	public void modify(String rsltId, List<RsltIdxParam> infoList, List<CmmtAtchmnfl> deleteAttachFileList, List<MultipartFile> rsltIdxFileList, List<MultipartFile> attachFileList) {
		BnMember worker = SecurityUtils.checkLogin();
		UsptRslt info = usptRsltDao.select(rsltId);
		if(info == null) {
			throw new InvalidationException("요청한 성과정보가 존재하지않습니다.");
		}

		String[] exts = {"DOCX", "PPTX", "XLSX", "PDF", "PNG"};
		int size = 100 * (1024 * 1024);
		String attachmentGroupId = "";

		/* 기존의 첨부파일을 새로운 첨부파일 그룹ID로 복사하고 삭제할 첨부파일을 새로운 첨부파일 그룹ID에서 삭제해준다. ------------ */
		if(deleteAttachFileList != null && deleteAttachFileList.size() > 0) {
			if(CoreUtils.string.isBlank(info.getAttachmentGroupId())) {
				throw new InvalidationException("잘못된 정보로 요청하셨습니다.\n확인 후 다시 요청해주세요.");
			}
			List<CmmtAtchmnfl> orgFileList = attachmentService.getFileInfos_group(info.getAttachmentGroupId());
			List<CmmtAtchmnfl> delFileList = new ArrayList<CmmtAtchmnfl>();
			for(CmmtAtchmnfl attachmentInfo : deleteAttachFileList) {
				for(CmmtAtchmnfl attachInfo : orgFileList) {
					if(CoreUtils.string.equals(attachmentInfo.getAttachmentId(), attachInfo.getAttachmentId())){
						delFileList.add(attachInfo);
						break;
					}
				}
			}
			CmmtAtchmnflGroup groupInfo = attachmentService.copyGroupFiles_toNewGroup(config.getDir().getUpload(), info.getAttachmentGroupId(), worker.getMemberId());
			attachmentGroupId = groupInfo.getAttachmentGroupId();
			List<CmmtAtchmnfl> newFileList = attachmentService.getFileInfos_group(attachmentGroupId);
			for(CmmtAtchmnfl delAttachInfo : delFileList) {
				for(CmmtAtchmnfl attachInfo : newFileList) {
					if(CoreUtils.string.equals(delAttachInfo.getFileNm(), attachInfo.getFileNm())){
						attachmentService.removeFile_keepEmptyGroup(config.getDir().getUpload(), attachInfo.getAttachmentId());
					}
				}
			}
		}
		/* --------------------------------------------------------------------------------------------------------------------- */

		if(attachFileList != null && attachFileList.size() > 0) {
			for(MultipartFile fileInfo : attachFileList) {
				if(CoreUtils.string.isBlank(attachmentGroupId)) {
					CmmtAtchmnfl caInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), fileInfo, exts, size);
					attachmentGroupId = caInfo.getAttachmentGroupId();
				} else {
					attachmentService.uploadFile_toGroup(config.getDir().getUpload(), attachmentGroupId, fileInfo, exts, size);
				}
			}
		}


		Date now = new Date();
		if(!CoreUtils.string.isBlank(attachmentGroupId)) {
			info.setAttachmentGroupId(attachmentGroupId);
		}
		info.setRsltSttusCd(Code.rsltSttus.제출);
		info.setPresentnDt(now);
		if(usptRsltDao.update(info) != 1) {
			throw new InvalidationException("성과 저장 중 오류가 발생했습니다.");
		}

		UsptRsltHist hist = new UsptRsltHist();
		hist.setRsltId(info.getRsltId());
		hist.setRsltHistId(CoreUtils.string.getNewId(Code.prefix.성과이력));
		hist.setRsltSttusCd(info.getRsltSttusCd());
		hist.setProcessCn("성과 내용을 제출합니다.");
		hist.setAttachmentGroupId(info.getAttachmentGroupId());
		hist.setPresentnDt(now);
		hist.setProcessMberType(Code.MBR_CODE_TYPE);
		hist.setCreatorId(worker.getMemberId());
		hist.setCreatedDt(now);
		usptRsltHistDao.insert(hist);
		/*-------------------------------------------------------------------*/

		for(RsltIdxParam param : infoList) {
			UsptRsltIdxIem regIdxIem = usptRsltIdxIemDao.select(param.getRsltIdxIemId());
			regIdxIem.setUpdatedDt(now);
			regIdxIem.setUpdaterId(worker.getMemberId());

			if(CoreUtils.string.isNotBlank(param.getDeleteAttachmentId())){
				if(CoreUtils.string.equals(param.getDeleteAttachmentId(), regIdxIem.getPrufAttachmentId())) {
					regIdxIem.setPrufAttachmentId(null);
				} else {
					throw new InvalidationException("성과지표에 해당되는 증빙자료 첨부파일ID를 입력해주세요");
				}
			}

			if(param.getAttachFileOrder() > -1) {
				if(rsltIdxFileList != null && rsltIdxFileList.size() > param.getAttachFileOrder()) {
					MultipartFile file = rsltIdxFileList.get(param.getAttachFileOrder());
					CmmtAtchmnfl caInfo = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), file, exts, size);
					regIdxIem.setPrufAttachmentId(caInfo.getAttachmentId());
				} else {
					throw new InvalidationException("성과지표 증빙자료 파일과 첨부파일 순서 정보가 일치하지 않습니다.");
				}
			}
			if(usptRsltIdxIemDao.update(regIdxIem) != 1) {
				throw new InvalidationException("성과지표 항목 저장 중 오류가 발생했습니다.");
			}

			UsptRsltIdxIemHist rsltIdxIemHist = new UsptRsltIdxIemHist();
			rsltIdxIemHist.setRsltHistId(hist.getRsltHistId());
			rsltIdxIemHist.setRsltIdxIemId(regIdxIem.getRsltIdxIemId());
			rsltIdxIemHist.setPrufAttachmentId(regIdxIem.getPrufAttachmentId());
			rsltIdxIemHist.setCreatorId(worker.getMemberId());
			rsltIdxIemHist.setCreatedDt(now);
			usptRsltIdxIemHistDao.insert(rsltIdxIemHist);
			/*-------------------------------------------------------------------*/

			for(UsptRsltIdxIemCn rsltIdxIemCn : param.getRsltIdxIemCnList()) {
				rsltIdxIemCn.setUpdatedDt(now);
				rsltIdxIemCn.setUpdaterId(worker.getMemberId());
				if( CoreUtils.string.equals("LIST", regIdxIem.getRsltIdxTypeCd())) {
					if(CoreUtils.string.equals(Code.flag.등록, rsltIdxIemCn.getFlag())) {
						rsltIdxIemCn.setRsltIdxIemCnId(CoreUtils.string.getNewId(Code.prefix.성과지표항목내용));
						rsltIdxIemCn.setCreatedDt(now);
						rsltIdxIemCn.setCreatorId(worker.getMemberId());
						rsltIdxIemCn.setRsltIdxIemId(param.getRsltIdxIemId());
						rsltIdxIemCn.setRsltIdxStdIemId(null);
						rsltIdxIemCn.setDeleteAt(false);
						if(CoreUtils.string.isBlank(rsltIdxIemCn.getRsltIdxDetailIemId())) {
							throw new InvalidationException("성과지표 세부항목ID를 입력해주세요");
						}
						if(rsltIdxIemCn.getSortOrder() == null) {
							throw new InvalidationException("정렬순서를 입력해주세요");
						}
						usptRsltIdxIemCnDao.insert(rsltIdxIemCn);
					} else if(CoreUtils.string.equals(Code.flag.수정, rsltIdxIemCn.getFlag())) {
						if(usptRsltIdxIemCnDao.update(rsltIdxIemCn) != 1) {
							throw new InvalidationException("성과지표 항목 내용 저장 중 오류가 발생했습니다.");
						}
					} else if(CoreUtils.string.equals(Code.flag.삭제, rsltIdxIemCn.getFlag())) {
						rsltIdxIemCn.setDeleteAt(true);
						if(usptRsltIdxIemCnDao.updateDeleteAt(rsltIdxIemCn) != 1) {
							throw new InvalidationException("성과지표 항목 내용 삭제 중 오류가 발생했습니다.");
						}
					} else {
						throw new InvalidationException("성과지표유형이 목록형일 경우 flag 값은 필수 입력 항목입니다.");
					}
				} else {
					if(usptRsltIdxIemCnDao.update(rsltIdxIemCn) != 1) {
						throw new InvalidationException("성과지표 항목 내용 저장 중 오류가 발생했습니다.");
					}
				}
				if(!CoreUtils.string.equals(Code.flag.삭제, rsltIdxIemCn.getFlag())) {
					UsptRsltIdxIemCnHist rsltIdxIemCnHist = new UsptRsltIdxIemCnHist();
					rsltIdxIemCnHist.setRsltHistId(hist.getRsltHistId());
					rsltIdxIemCnHist.setRsltIdxIemId(regIdxIem.getRsltIdxIemId());
					rsltIdxIemCnHist.setRsltIdxIemCnId(rsltIdxIemCn.getRsltIdxIemCnId());
					rsltIdxIemCnHist.setRsltIdxIemCn(rsltIdxIemCn.getRsltIdxIemCn());
					rsltIdxIemCnHist.setSortOrder(rsltIdxIemCn.getSortOrder());
					rsltIdxIemCnHist.setCreatedDt(now);
					rsltIdxIemCnHist.setCreatorId(worker.getMemberId());
					usptRsltIdxIemCnHistDao.insert(rsltIdxIemCnHist);
				}
			}
		}
	}


	/**
	 * 제출이력 제출일 목록 조회
	 * @param rsltId
	 * @return
	 */
	public JsonList<RsltHistPresentnDto> getPresentnDtList(String rsltId) {
		SecurityUtils.checkLogin();
		UsptRslt info = usptRsltDao.select(rsltId);
		if(info == null) {
			throw new InvalidationException("요청한 성과정보가 존재하지않습니다.");
		}
		return new JsonList<>(usptRsltHistDao.selectPresentnDtList(rsltId));
	}


	/**
	 * 제출이력 조회
	 * @param rsltId
	 * @param rsltHistId
	 * @return
	 */
	public RsltHistDto getHist(String rsltId, String rsltHistId) {
		SecurityUtils.checkLogin();
		UsptRsltHist info = usptRsltHistDao.select(rsltId, rsltHistId);
		if(info == null) {
			throw new InvalidationException("요청한 성과정보가 존재하지않습니다.");
		}
		UsptRslt orgInfo = usptRsltDao.select(rsltId);

		RsltHistDto dto = new RsltHistDto();
		dto.setBasicInfo(usptBsnsPblancApplcntDao.selectBsnsBasic(orgInfo.getApplyId(), null));
		dto.setAttachFileList(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));

		List<RsltIdxIemHistDto> rsltIdxIemDtoList = new ArrayList<RsltIdxIemHistDto>();
		List<UsptRsltIdxIemHist> rsltIdxIemHistList = usptRsltIdxIemHistDao.selectList(rsltHistId);
		if(rsltIdxIemHistList != null) {
			for(UsptRsltIdxIemHist idxIemHist : rsltIdxIemHistList) {
				RsltIdxIemHistDto iemDto = new RsltIdxIemHistDto();
				iemDto.setRsltIdxIemId(idxIemHist.getRsltIdxIemId());
				iemDto.setRsltIdxNm(idxIemHist.getRsltIdxNm());
				iemDto.setRsltIdxTypeCd(idxIemHist.getRsltIdxTypeCd());
				iemDto.setPrufFile(attachmentService.getFileInfo(idxIemHist.getPrufAttachmentId()));
				iemDto.setRsltIdxIemCnList(usptRsltIdxIemCnHistDao.selectList(rsltHistId, idxIemHist.getRsltIdxIemId()));
				rsltIdxIemDtoList.add(iemDto);
			}
		}
		dto.setRsltIdxIemList(rsltIdxIemDtoList);
		return dto;
	}


	/**
	 * 성과 제출이력 첨부파일 일괄 다운로드
	 * @param response
	 * @param rsltId
	 * @param rsltHistId
	 */
	public void getAtchmnflAll(HttpServletResponse response, String rsltId, String rsltHistId) {
		SecurityUtils.checkLogin();
		UsptRsltHist info = usptRsltHistDao.select(rsltId, rsltHistId);
		if(info == null) {
			throw new InvalidationException("요청한 성과정보가 존재하지않습니다.");
		}
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), info.getAttachmentGroupId(), "성과첨부파일");
	}



	/**
	 * 성과 제출이력 첨부파일 다운로드
	 * @param response
	 * @param rsltId
	 * @param attachmentId
	 * @param rsltHistId
	 */
	public void getAtchmnfl(HttpServletResponse response, String rsltId, String attachmentId, String rsltHistId) {
		SecurityUtils.checkLogin();
		UsptRsltHist info = usptRsltHistDao.select(rsltId, rsltHistId);
		if(info == null) {
			throw new InvalidationException("요청한 성과정보가 존재하지않습니다.");
		}
		if(CoreUtils.string.isBlank(attachmentId)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "첨부파일ID"));
		}
		attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
	}


	/**
	 * 입주사업 성과ID 조회
	 * @param param
	 * @return
	 */
	public String getBsnsMvnRsltId(BsnsMvnRsltParam param) {
		BnMember worker = SecurityUtils.checkLogin();
		if(CoreUtils.string.isBlank(param.getApplyId())) {
			throw new InvalidationException("신청ID를 입력해주세요");
		}
		if(CoreUtils.string.isBlank(param.getRsltYm())) {
			throw new InvalidationException("년월을 입력해주세요");
		}
		ApplcntBsnsBasicDto dto = usptBsnsPblancApplcntDao.selectBsnsBasic(param.getApplyId(), null);
		if(dto == null) {
			throw new InvalidationException("요청하신 사업공고 지원자 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(dto.getMemberId(), worker.getMemberId())){
			throw new InvalidationException("로그인한 회원의 정보가 아닙니다.");
		}
		if(!CoreUtils.string.equals(dto.getBsnsTypeCd(), Code.bsnsType.입주사업)){
			throw new InvalidationException("입주사업의 정보가 아닙니다.");
		}

		UsptRslt rsltInfo = usptRsltDao.selectByMvnApplyId(param);
		if(rsltInfo != null) {
			return rsltInfo.getRsltId();
		}

		Date now = new Date();
		UsptRslt regInfo = new UsptRslt();
		regInfo.setCreatorId(worker.getMemberId());
		regInfo.setCreatedDt(now);
		regInfo.setUpdaterId(worker.getMemberId());
		regInfo.setUpdatedDt(now);
		regInfo.setRsltSttusCd(Code.rsltSttus.제출요청);
		regInfo.setApplyId(param.getApplyId());
		regInfo.setRsltYm(param.getRsltYm());
		regInfo.setRsltId(CoreUtils.string.getNewId(Code.prefix.성과));
		usptRsltDao.insert(regInfo);

		UsptRsltHist hist = new UsptRsltHist();
		hist.setRsltId(regInfo.getRsltId());
		hist.setRsltHistId(CoreUtils.string.getNewId(Code.prefix.성과이력));
		hist.setRsltSttusCd(regInfo.getRsltSttusCd());
		hist.setProcessCn("입주사업 성과 제출요청");
		hist.setProcessMberType(Code.MBR_CODE_TYPE);
		hist.setCreatorId(worker.getMemberId());
		hist.setCreatedDt(now);
		usptRsltHistDao.insert(hist);


		/* 성과지표항목 / 성과지표항목내용 미리저장 시작.---------------------------------------------------------------------------------------*/
		ApplcntBsnsBasicDto basicInfo = usptBsnsPblancApplcntDao.selectBsnsBasic(param.getApplyId(), null);
		List<UsptBsnsRsltIdx> rsltIdxList = usptBsnsRsltIdxDao.selectList(basicInfo.getBsnsCd());

		UsptRsltIdxIem regIdxIem = new UsptRsltIdxIem();
		regIdxIem.setRsltId(regInfo.getRsltId());
		regIdxIem.setCreatedDt(now);
		regIdxIem.setCreatorId(worker.getMemberId());
		regIdxIem.setUpdatedDt(now);
		regIdxIem.setUpdaterId(worker.getMemberId());

		for(UsptBsnsRsltIdx bsnsRsltIdx : rsltIdxList) {
			regIdxIem.setRsltIdxIemId(CoreUtils.string.getNewId(Code.prefix.성과지표항목));
			regIdxIem.setRsltIdxId(bsnsRsltIdx.getRsltIdxId());
			usptRsltIdxIemDao.insert(regIdxIem);

			for(RsltIdxDetailIem rsltidxdetailiem : bsnsRsltIdx.getDetailIemList()) {
				UsptRsltIdxIemCn rsltIdxIemCn = new UsptRsltIdxIemCn();
				rsltIdxIemCn.setRsltIdxDetailIemId(rsltidxdetailiem.getRsltIdxDetailIemId());
				rsltIdxIemCn.setRsltIdxIemId(regIdxIem.getRsltIdxIemId());
				rsltIdxIemCn.setCreatedDt(now);
				rsltIdxIemCn.setCreatorId(worker.getMemberId());
				rsltIdxIemCn.setUpdatedDt(now);
				rsltIdxIemCn.setUpdaterId(worker.getMemberId());
				rsltIdxIemCn.setSortOrder(1);
				rsltIdxIemCn.setDeleteAt(false);

				if(bsnsRsltIdx.getStdIdx()) {
					for(RsltIdxStdIem rsltIdxStdIem :bsnsRsltIdx.getStdIemList()) {
						rsltIdxIemCn.setRsltIdxIemCnId(CoreUtils.string.getNewId(Code.prefix.성과지표항목내용));
						rsltIdxIemCn.setRsltIdxStdIemId(rsltIdxStdIem.getRsltIdxStdIemId());
						rsltIdxIemCn.setRsltIdxIemCn("");
						usptRsltIdxIemCnDao.insert(rsltIdxIemCn);
					}
				} else {
					rsltIdxIemCn.setRsltIdxIemCnId(CoreUtils.string.getNewId(Code.prefix.성과지표항목내용));
					rsltIdxIemCn.setRsltIdxIemCn("");
					usptRsltIdxIemCnDao.insert(rsltIdxIemCn);
				}
			}
		}
		/* 성과지표항목 / 성과지표항목내용 미리저장 끝.---------------------------------------------------------------------------------------*/

		return regInfo.getRsltId();
	}
}
