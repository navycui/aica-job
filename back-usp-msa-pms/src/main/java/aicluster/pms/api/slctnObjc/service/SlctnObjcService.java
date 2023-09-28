package aicluster.pms.api.slctnObjc.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import aicluster.pms.api.slctnObjc.dto.DlbrtParam;
import aicluster.pms.api.slctnObjc.dto.SlctnObjcReqstListParam;
import aicluster.pms.common.dao.UsptEvlObjcProcessHistDao;
import aicluster.pms.common.dao.UsptEvlSlctnObjcReqstDao;
import aicluster.pms.common.dao.UsptLastSlctnObjcProcessHistDao;
import aicluster.pms.common.dao.UsptLastSlctnObjcReqstDao;
import aicluster.pms.common.dto.HistDto;
import aicluster.pms.common.dto.SlctnObjcDto;
import aicluster.pms.common.dto.SlctnObjcReqstListDto;
import aicluster.pms.common.entity.UsptEvlObjcProcessHist;
import aicluster.pms.common.entity.UsptEvlSlctnObjcReqst;
import aicluster.pms.common.entity.UsptLastSlctnObjcProcessHist;
import aicluster.pms.common.entity.UsptLastSlctnObjcReqst;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class SlctnObjcService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
	@Autowired
	private UsptEvlSlctnObjcReqstDao usptEvlSlctnObjcReqstDao;
	@Autowired
	private UsptEvlObjcProcessHistDao usptEvlObjcProcessHistDao;
	@Autowired
	private UsptLastSlctnObjcReqstDao usptLastSlctnObjcReqstDao;
	@Autowired
	private UsptLastSlctnObjcProcessHistDao usptLastSlctnObjcProcessHistDao;

	/**
	 * 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<SlctnObjcReqstListDto> getList(SlctnObjcReqstListParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptEvlSlctnObjcReqstDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<SlctnObjcReqstListDto> list = usptEvlSlctnObjcReqstDao.selectList(param);
		CorePagination<SlctnObjcReqstListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 엑셀저장
	 * @param param
	 * @return
	 */
	public List<SlctnObjcReqstListDto> getListExcelDwld(SlctnObjcReqstListParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptEvlSlctnObjcReqstDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		param.setItemsPerPage(totalItems);
		return usptEvlSlctnObjcReqstDao.selectList(param);
	}


	/**
	 * 상세조회
	 * @param request
	 * @param objcReqstId
	 * @return
	 */
	public SlctnObjcDto get(HttpServletRequest request, String objcReqstId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		if(!CoreUtils.string.isBlank(info.getApplcntAttachmentGroupId())) {
			info.setApplcntAttachmentFileList(attachmentService.getFileInfos_group(info.getApplcntAttachmentGroupId()));
		}
		if(!CoreUtils.string.isBlank(info.getDlbrtAttachmentGroupId())) {
			info.setDlbrtAttachmentFileList(attachmentService.getFileInfos_group(info.getDlbrtAttachmentGroupId()));
		}

		// 이력 생성
		LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("이의신청접수 상세조회")
				.trgterId(info.getMemberId())
				.email("")
				.birthday("")
				.mobileNo(info.getMobileNo())
				.build();
		indvdlInfSrchUtils.insert(logParam);

		return info;
	}


	/**
	 * 접수완료
	 * @param objcReqstId
	 */
	public void modifyReceipt(String objcReqstId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		Date now = new Date();
		if(CoreUtils.string.equals(info.getSlctnType(), "LAST")) {
			UsptLastSlctnObjcReqst orgInfo = usptLastSlctnObjcReqstDao.select(objcReqstId);
			orgInfo.setLastSlctnObjcProcessSttusCd(Code.lastSlctnObjcProcessSttus.접수완료);
			orgInfo.setUpdatedDt(now);
			orgInfo.setUpdaterId(worker.getMemberId());
			if(usptLastSlctnObjcReqstDao.update(orgInfo) != 1) {
				throw new InvalidationException("접수완료 처리 중 오류가 발생했습니다.");
			}

			UsptLastSlctnObjcProcessHist hist = new UsptLastSlctnObjcProcessHist();
			hist.setCreatedDt(now);
			hist.setCreatorId(worker.getMemberId());
			hist.setLastSlctnObjcProcessHistId(CoreUtils.string.getNewId(Code.prefix.최종선정이의처리이력));
			hist.setLastSlctnObjcReqstId(orgInfo.getLastSlctnObjcReqstId());
			hist.setResnCn("접수완료 처리되었습니다.");
			hist.setSlctnObjcProcessSttusCd(orgInfo.getLastSlctnObjcProcessSttusCd());
			usptLastSlctnObjcProcessHistDao.insert(hist);

		} else {
			UsptEvlSlctnObjcReqst orgInfo = usptEvlSlctnObjcReqstDao.select(objcReqstId);
			orgInfo.setLastSlctnObjcProcessSttusCd(Code.lastSlctnObjcProcessSttus.접수완료);
			orgInfo.setUpdatedDt(now);
			orgInfo.setUpdaterId(worker.getMemberId());
			if(usptEvlSlctnObjcReqstDao.update(orgInfo) != 1) {
				throw new InvalidationException("접수완료 처리 중 오류가 발생했습니다.");
			}

			UsptEvlObjcProcessHist hist = new UsptEvlObjcProcessHist();
			hist.setCreatedDt(now);
			hist.setCreatorId(worker.getMemberId());
			hist.setEvlObjcProcessHistId(CoreUtils.string.getNewId(Code.prefix.평가이의처리이력));
			hist.setEvlSlctnObjcReqstId(orgInfo.getEvlSlctnObjcReqstId());
			hist.setResnCn("접수완료 처리되었습니다.");
			hist.setSlctnObjcProcessSttusCd(orgInfo.getLastSlctnObjcProcessSttusCd());
			usptEvlObjcProcessHistDao.insert(hist);
		}
	}


	/**
	 * 반려
	 * @param objcReqstId
	 * @param rejectReasonCn
	 */
	public void modifyReject(String objcReqstId, String rejectReasonCn) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		if(CoreUtils.string.isBlank(rejectReasonCn)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "반려 내용"));
		}

		Date now = new Date();
		if(CoreUtils.string.equals(info.getSlctnType(), "LAST")) {
			UsptLastSlctnObjcReqst orgInfo = usptLastSlctnObjcReqstDao.select(objcReqstId);
			orgInfo.setLastSlctnObjcProcessSttusCd(Code.lastSlctnObjcProcessSttus.반려);
			orgInfo.setRejectReasonCn(rejectReasonCn);
			orgInfo.setUpdatedDt(now);
			orgInfo.setUpdaterId(worker.getMemberId());
			if(usptLastSlctnObjcReqstDao.update(orgInfo) != 1) {
				throw new InvalidationException("반려 처리 중 오류가 발생했습니다.");
			}

			UsptLastSlctnObjcProcessHist hist = new UsptLastSlctnObjcProcessHist();
			hist.setCreatedDt(now);
			hist.setCreatorId(worker.getMemberId());
			hist.setLastSlctnObjcProcessHistId(CoreUtils.string.getNewId(Code.prefix.최종선정이의처리이력));
			hist.setLastSlctnObjcReqstId(orgInfo.getLastSlctnObjcReqstId());
			hist.setResnCn(rejectReasonCn);
			hist.setSlctnObjcProcessSttusCd(orgInfo.getLastSlctnObjcProcessSttusCd());
			usptLastSlctnObjcProcessHistDao.insert(hist);
		} else {
			UsptEvlSlctnObjcReqst orgInfo = usptEvlSlctnObjcReqstDao.select(objcReqstId);
			orgInfo.setLastSlctnObjcProcessSttusCd(Code.lastSlctnObjcProcessSttus.반려);
			orgInfo.setRejectReasonCn(rejectReasonCn);
			orgInfo.setUpdatedDt(now);
			orgInfo.setUpdaterId(worker.getMemberId());
			if(usptEvlSlctnObjcReqstDao.update(orgInfo) != 1) {
				throw new InvalidationException("반려 처리 중 오류가 발생했습니다.");
			}

			UsptEvlObjcProcessHist hist = new UsptEvlObjcProcessHist();
			hist.setCreatedDt(now);
			hist.setCreatorId(worker.getMemberId());
			hist.setEvlObjcProcessHistId(CoreUtils.string.getNewId(Code.prefix.평가이의처리이력));
			hist.setEvlSlctnObjcReqstId(orgInfo.getEvlSlctnObjcReqstId());
			hist.setResnCn(rejectReasonCn);
			hist.setSlctnObjcProcessSttusCd(orgInfo.getLastSlctnObjcProcessSttusCd());
			usptEvlObjcProcessHistDao.insert(hist);
		}
	}


	/**
	 * 심의완료
	 * @param objcReqstId
	 * @param param
	 * @param fileList
	 */
	public void modifyDlbrt(String objcReqstId, DlbrtParam param, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		Date dlbrtDe = string.toDate(param.getDlbrtDate());
		String dlbrtDeDt = date.format(dlbrtDe, "yyyyMMdd");
		if (dlbrtDeDt == null) {
			throw new InvalidationException(String.format(Code.validateMessage.일자형식오류, "심의일"));
		}
		if(CoreUtils.string.isBlank(param.getDlbrtCn())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "심의결과 내용"));
		}


		Date now = new Date();
		int size = 100 * (1024 * 1024);
		String[] exts = {"DOCX", "PPTX", "XLSX", "PDF", "PNG"};

		if(CoreUtils.string.equals(info.getSlctnType(), "LAST")) {
			UsptLastSlctnObjcReqst orgInfo = usptLastSlctnObjcReqstDao.select(objcReqstId);
			orgInfo.setLastSlctnObjcProcessSttusCd(Code.lastSlctnObjcProcessSttus.심의완료);
			orgInfo.setUpdatedDt(now);
			orgInfo.setUpdaterId(worker.getMemberId());
			orgInfo.setDlbrtDe(dlbrtDeDt);
			orgInfo.setDlbrtCn(param.getDlbrtCn());

			if(fileList != null && fileList.size() > 0) {
				for(MultipartFile fileInfo : fileList) {
					if(CoreUtils.string.isBlank(orgInfo.getDlbrtAttachmentGroupId())) {
						CmmtAtchmnfl caInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), fileInfo, exts, size);
						orgInfo.setDlbrtAttachmentGroupId(caInfo.getAttachmentGroupId());
					} else {
						attachmentService.uploadFile_toGroup(config.getDir().getUpload(), orgInfo.getDlbrtAttachmentGroupId(), fileInfo, exts, size);
					}
				}
			}
			if(usptLastSlctnObjcReqstDao.update(orgInfo) != 1) {
				throw new InvalidationException("심의완료 처리 중 오류가 발생했습니다.");
			}

			UsptLastSlctnObjcProcessHist hist = new UsptLastSlctnObjcProcessHist();
			hist.setCreatedDt(now);
			hist.setCreatorId(worker.getMemberId());
			hist.setLastSlctnObjcProcessHistId(CoreUtils.string.getNewId(Code.prefix.최종선정이의처리이력));
			hist.setLastSlctnObjcReqstId(orgInfo.getLastSlctnObjcReqstId());
			hist.setResnCn("심의완료 처리되었습니다.");
			hist.setSlctnObjcProcessSttusCd(orgInfo.getLastSlctnObjcProcessSttusCd());
			usptLastSlctnObjcProcessHistDao.insert(hist);
		} else {
			UsptEvlSlctnObjcReqst orgInfo = usptEvlSlctnObjcReqstDao.select(objcReqstId);
			orgInfo.setLastSlctnObjcProcessSttusCd(Code.lastSlctnObjcProcessSttus.심의완료);
			orgInfo.setUpdatedDt(now);
			orgInfo.setUpdaterId(worker.getMemberId());
			orgInfo.setDlbrtDe(dlbrtDeDt);
			orgInfo.setDlbrtCn(param.getDlbrtCn());

			if(fileList != null && fileList.size() > 0) {
				for(MultipartFile fileInfo : fileList) {
					if(CoreUtils.string.isBlank(orgInfo.getDlbrtAttachmentGroupId())) {
						CmmtAtchmnfl caInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), fileInfo, exts, size);
						orgInfo.setDlbrtAttachmentGroupId(caInfo.getAttachmentGroupId());
					} else {
						attachmentService.uploadFile_toGroup(config.getDir().getUpload(), orgInfo.getDlbrtAttachmentGroupId(), fileInfo, exts, size);
					}
				}
			}

			if(usptEvlSlctnObjcReqstDao.update(orgInfo) != 1) {
				throw new InvalidationException("심의완료 처리 중 오류가 발생했습니다.");
			}

			UsptEvlObjcProcessHist hist = new UsptEvlObjcProcessHist();
			hist.setCreatedDt(now);
			hist.setCreatorId(worker.getMemberId());
			hist.setEvlObjcProcessHistId(CoreUtils.string.getNewId(Code.prefix.평가이의처리이력));
			hist.setEvlSlctnObjcReqstId(orgInfo.getEvlSlctnObjcReqstId());
			hist.setResnCn("심의완료 처리되었습니다.");
			hist.setSlctnObjcProcessSttusCd(orgInfo.getLastSlctnObjcProcessSttusCd());
			usptEvlObjcProcessHistDao.insert(hist);
		}

		if(param.getDeleteFileList() != null && param.getDeleteFileList().size() > 0) {
			for(CmmtAtchmnfl fileInfo : param.getDeleteFileList()) {
				attachmentService.removeFile_keepEmptyGroup(config.getDir().getUpload(), fileInfo.getAttachmentId());
			}
		}
	}

	/**
	 * 이의신청 첨부파일 일괄 다운로드
	 * @param response
	 * @param objcReqstId
	 */
	public void getObjcAtchmnflAll(HttpServletResponse response, String objcReqstId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		String applcntAttachmentGroupId = "";
		if(CoreUtils.string.equals(info.getSlctnType(), "LAST")) {
			UsptLastSlctnObjcReqst orgInfo = usptLastSlctnObjcReqstDao.select(objcReqstId);
			if(CoreUtils.string.isBlank(orgInfo.getApplcntAttachmentGroupId())){
				throw new InvalidationException("파일이 존재하지 않습니다.");
			}
			applcntAttachmentGroupId = orgInfo.getApplcntAttachmentGroupId();
		} else {
			UsptEvlSlctnObjcReqst orgInfo = usptEvlSlctnObjcReqstDao.select(objcReqstId);
			if(CoreUtils.string.isBlank(orgInfo.getApplcntAttachmentGroupId())){
				throw new InvalidationException("파일이 존재하지 않습니다.");
			}
			applcntAttachmentGroupId = orgInfo.getApplcntAttachmentGroupId();
		}
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), applcntAttachmentGroupId, "이의신청_첨부파일");
	}


	/**
	 * 이의신청 첨부파일 다운로드
	 * @param response
	 * @param objcReqstId
	 * @param attachmentId
	 */
	public void getObjcAtchmnfl(HttpServletResponse response, String objcReqstId, String attachmentId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		if(CoreUtils.string.equals(info.getSlctnType(), "LAST")) {
			UsptLastSlctnObjcReqst orgInfo = usptLastSlctnObjcReqstDao.select(objcReqstId);
			if(CoreUtils.string.isBlank(orgInfo.getApplcntAttachmentGroupId())){
				throw new InvalidationException("파일이 존재하지 않습니다.");
			}
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		} else {
			UsptEvlSlctnObjcReqst orgInfo = usptEvlSlctnObjcReqstDao.select(objcReqstId);
			if(CoreUtils.string.isBlank(orgInfo.getApplcntAttachmentGroupId())){
				throw new InvalidationException("파일이 존재하지 않습니다.");
			}
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		}
	}


	/**
	 * 심의결과 첨부파일 일괄 다운로드
	 * @param response
	 * @param objcReqstId
	 */
	public void getDlbrtAtchmnflAll(HttpServletResponse response, String objcReqstId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		String dlbrtAttachmentGroupId = "";
		if(CoreUtils.string.equals(info.getSlctnType(), "LAST")) {
			UsptLastSlctnObjcReqst orgInfo = usptLastSlctnObjcReqstDao.select(objcReqstId);
			if(CoreUtils.string.isBlank(orgInfo.getDlbrtAttachmentGroupId())){
				throw new InvalidationException("파일이 존재하지 않습니다.");
			}
			dlbrtAttachmentGroupId = orgInfo.getDlbrtAttachmentGroupId();
		} else {
			UsptEvlSlctnObjcReqst orgInfo = usptEvlSlctnObjcReqstDao.select(objcReqstId);
			if(CoreUtils.string.isBlank(orgInfo.getDlbrtAttachmentGroupId())){
				throw new InvalidationException("파일이 존재하지 않습니다.");
			}
			dlbrtAttachmentGroupId = orgInfo.getDlbrtAttachmentGroupId();
		}
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), dlbrtAttachmentGroupId, "이의신청_심의완료_첨부파일");
	}


	/**
	 * 심의결과 첨부파일 다운로드
	 * @param response
	 * @param objcReqstId
	 * @param attachmentId
	 */
	public void getDlbrtAtchmnfl(HttpServletResponse response, String objcReqstId, String attachmentId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		if(CoreUtils.string.equals(info.getSlctnType(), "LAST")) {
			UsptLastSlctnObjcReqst orgInfo = usptLastSlctnObjcReqstDao.select(objcReqstId);
			if(CoreUtils.string.isBlank(orgInfo.getDlbrtAttachmentGroupId())){
				throw new InvalidationException("파일이 존재하지 않습니다.");
			}
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		} else {
			UsptEvlSlctnObjcReqst orgInfo = usptEvlSlctnObjcReqstDao.select(objcReqstId);
			if(CoreUtils.string.isBlank(orgInfo.getDlbrtAttachmentGroupId())){
				throw new InvalidationException("파일이 존재하지 않습니다.");
			}
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		}
	}


	/**
	 * 이력조회
	 * @param objcReqstId
	 * @return
	 */
	public JsonList<HistDto> getHist(String objcReqstId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		SlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		if(CoreUtils.string.equals(info.getSlctnType(), "LAST")) {
			return new JsonList<>(usptLastSlctnObjcProcessHistDao.selectList(objcReqstId));
		} else {
			return new JsonList<>(usptEvlObjcProcessHistDao.selectList(objcReqstId));
		}
	}
}
