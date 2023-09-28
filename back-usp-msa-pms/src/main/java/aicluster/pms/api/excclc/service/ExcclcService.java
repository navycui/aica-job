package aicluster.pms.api.excclc.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.excclc.dto.ExcclcListParam;
import aicluster.pms.api.excclc.dto.ExcclcParam;
import aicluster.pms.common.dao.UsptBsnsCnvnDao;
import aicluster.pms.common.dao.UsptBsnsExcclcDao;
import aicluster.pms.common.dto.ExcclcDto;
import aicluster.pms.common.dto.ExcclcListDto;
import aicluster.pms.common.entity.UsptBsnsExcclc;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class ExcclcService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private UsptBsnsCnvnDao usptBsnsCnvnDao;
	@Autowired
	private UsptBsnsExcclcDao usptBsnsExcclcDao;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

	/**
	 * 정산관리 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<ExcclcListDto> getList(ExcclcListParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptBsnsCnvnDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<ExcclcListDto> list = usptBsnsCnvnDao.selectList(param);
		CorePagination<ExcclcListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 정산관리 목록 엑셀 저장
	 * @param param
	 * @return
	 */
	public List<ExcclcListDto> getListExcelDwld(ExcclcListParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptBsnsCnvnDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		param.setItemsPerPage(totalItems);
		return usptBsnsCnvnDao.selectList(param);
	}

	/**
	 * 정산내역 상세조회
	 * @param request
	 * @param bsnsCnvnId
	 * @param taskReqstWctId
	 * @return
	 */
	public ExcclcDto get(HttpServletRequest request, String bsnsCnvnId, String taskReqstWctId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		ExcclcDto dto = usptBsnsCnvnDao.selectDetail(bsnsCnvnId, taskReqstWctId, worker.getMemberId());
		if(dto == null) {
			throw new InvalidationException("요청하신 정산내역이 존재하지 않습니다.");
		}
		if(!CoreUtils.string.isBlank(dto.getAttachmentGroupId())) {
			dto.setFileList(attachmentService.getFileInfos_group(dto.getAttachmentGroupId()));
		}

		// 이력 생성
		LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("정산내역 상세조회")
				.trgterId(dto.getMemberId())
				.email("")
				.birthday("")
				.mobileNo(dto.getMbtlnum())
				.build();
		indvdlInfSrchUtils.insert(logParam);

		return dto;
	}


	/**
	 * 정산내역 저장
	 * @param bsnsCnvnId
	 * @param param
	 * @param fileList
	 */
	public void modify(String bsnsCnvnId, ExcclcParam param, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		ExcclcDto info = usptBsnsCnvnDao.selectDetail(bsnsCnvnId, param.getTaskReqstWctId(), worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청하신 정산내역이 존재하지 않습니다.");
		}
		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, info.getChargerAuthorCd())) {
			throw new InvalidationException("저장 권한이 없습니다.");
		}
		if(param.getExcutSbsidy() == null) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "보조금"));
		}
		if(param.getExcutBsnmAlotm() == null) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "집행 사업자 부담금"));
		}
		if(param.getExcutCnvnTotamt() == null) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "집행 협약총액"));
		}

		if(fileList != null && fileList.size() > 0) {
			String[] exts = {"DOCX", "PPTX", "XLSX", "PDF", "PNG"};
			int size = 100 * (1024 * 1024);
			for(MultipartFile fileInfo : fileList) {
				if(CoreUtils.string.isBlank(info.getAttachmentGroupId())) {
					CmmtAtchmnfl caInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), fileInfo, exts, size);
					info.setAttachmentGroupId(caInfo.getAttachmentGroupId());
				} else {
					attachmentService.uploadFile_toGroup(config.getDir().getUpload(), info.getAttachmentGroupId(), fileInfo, exts, size);
				}
			}
		}
		List<CmmtAtchmnfl> attachList = attachmentService.getFileInfos_group(info.getAttachmentGroupId());
		if(param.getDeleteFileList() != null && !param.getDeleteFileList().isEmpty()) {
			if(attachList == null) {
				throw new InvalidationException("요청한 삭제 첨부파일ID에 해당되는 정보가 없습니다.");
			}
			String attachStr = attachList.stream().map(CmmtAtchmnfl::getAttachmentId).collect(Collectors.joining());
			for(CmmtAtchmnfl attachmenInfo : param.getDeleteFileList()) {
				if(attachStr.contains(attachmenInfo.getAttachmentId())) {
					attachmentService.removeFile_keepEmptyGroup(config.getDir().getUpload(), attachmenInfo.getAttachmentId());
				} else {
					throw new InvalidationException("요청한 삭제 첨부파일ID에 해당되는 정보가 없습니다.");
				}
			}
		}

		Date now = new Date();
		UsptBsnsExcclc regInfo = usptBsnsExcclcDao.select(bsnsCnvnId, param.getTaskReqstWctId());
		if(regInfo == null) {
			regInfo = new UsptBsnsExcclc();
			regInfo.setBsnsExcclcId(CoreUtils.string.getNewId(Code.prefix.사업정산));
			regInfo.setBsnsCnvnId(bsnsCnvnId);
			regInfo.setTaskReqstWctId(param.getTaskReqstWctId());
			regInfo.setExcutSbsidy(param.getExcutSbsidy());
			regInfo.setExcutBsnmAlotm(param.getExcutBsnmAlotm());
			regInfo.setExcutCnvnTotamt(param.getExcutCnvnTotamt());
			regInfo.setAttachmentGroupId(info.getAttachmentGroupId());
			regInfo.setCreatedDt(now);
			regInfo.setCreatorId(worker.getMemberId());
			regInfo.setUpdatedDt(now);
			regInfo.setUpdaterId(worker.getMemberId());
			usptBsnsExcclcDao.insert(regInfo);
		} else {
			regInfo.setExcutSbsidy(param.getExcutSbsidy());
			regInfo.setExcutBsnmAlotm(param.getExcutBsnmAlotm());
			regInfo.setExcutCnvnTotamt(param.getExcutCnvnTotamt());
			regInfo.setAttachmentGroupId(info.getAttachmentGroupId());
			regInfo.setUpdatedDt(now);
			regInfo.setUpdaterId(worker.getMemberId());
			if(usptBsnsExcclcDao.update(regInfo) != 1) {
				throw new InvalidationException(String.format(Code.validateMessage.DB오류, "정산내역 저장"));
			}
		}
	}
}
