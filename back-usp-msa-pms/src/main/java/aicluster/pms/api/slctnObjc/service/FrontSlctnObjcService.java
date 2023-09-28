package aicluster.pms.api.slctnObjc.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.slctnObjc.dto.FrontSlctnObjcReqstListParam;
import aicluster.pms.common.dao.UsptEvlObjcProcessHistDao;
import aicluster.pms.common.dao.UsptEvlSlctnObjcReqstDao;
import aicluster.pms.common.dao.UsptLastSlctnObjcProcessHistDao;
import aicluster.pms.common.dao.UsptLastSlctnObjcReqstDao;
import aicluster.pms.common.dto.FrontSlctnObjcDto;
import aicluster.pms.common.dto.FrontSlctnObjcReqstListDto;
import aicluster.pms.common.dto.RejectDto;
import aicluster.pms.common.entity.UsptEvlSlctnObjcReqst;
import aicluster.pms.common.entity.UsptLastSlctnObjcReqst;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

/**
 * @author brainednet
 *
 */
@Service
public class FrontSlctnObjcService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private UsptEvlSlctnObjcReqstDao usptEvlSlctnObjcReqstDao;
	@Autowired
	private UsptLastSlctnObjcReqstDao usptLastSlctnObjcReqstDao;
	@Autowired
	private UsptEvlObjcProcessHistDao usptEvlObjcProcessHistDao;
	@Autowired
	private UsptLastSlctnObjcProcessHistDao usptLastSlctnObjcProcessHistDao;

	/**
	 * 결과 이의신청 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<FrontSlctnObjcReqstListDto> getList(FrontSlctnObjcReqstListParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setMemberId(worker.getMemberId());
		long totalItems = usptEvlSlctnObjcReqstDao.selectFrontListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<FrontSlctnObjcReqstListDto> list = usptEvlSlctnObjcReqstDao.selectFrontList(param);
		CorePagination<FrontSlctnObjcReqstListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 결과 이의신청 상세조회
	 * @param objcReqstId
	 * @return
	 */
	public FrontSlctnObjcDto get(String objcReqstId) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		FrontSlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectFrontDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		if(!CoreUtils.string.isBlank(info.getApplcntAttachmentGroupId())) {
			info.setApplcntAttachmentFileList(attachmentService.getFileInfos_group(info.getApplcntAttachmentGroupId()));
		}
		if(!CoreUtils.string.isBlank(info.getDlbrtAttachmentGroupId())) {
			info.setDlbrtAttachmentFileList(attachmentService.getFileInfos_group(info.getDlbrtAttachmentGroupId()));
		}
		return info;
	}


	/**
	 * 결과이의신청 첨부파일 일괄 다운로드
	 * @param response
	 * @param objcReqstId
	 */
	public void getObjcAtchmnflAll(HttpServletResponse response, String objcReqstId) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		FrontSlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectFrontDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}
		if(CoreUtils.string.isBlank(info.getApplcntAttachmentGroupId())) {
			throw new InvalidationException("요청한 이의신청의 첨부파일이 존재하지 않습니다.");
		}
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), info.getApplcntAttachmentGroupId(), "결과이의신청_첨부파일");
	}


	/**
	 * 결과이의신청 첨부파일 다운로드
	 * @param response
	 * @param objcReqstId
	 * @param attachmentId
	 */
	public void getObjcAtchmnfl(HttpServletResponse response, String objcReqstId, String attachmentId) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		FrontSlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectFrontDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}

		if(CoreUtils.string.isBlank(info.getApplcntAttachmentGroupId())) {
			throw new InvalidationException("요청한 이의신청의 첨부파일이 존재하지 않습니다.");
		}

		List<CmmtAtchmnfl> fileList = attachmentService.getFileInfos_group(info.getApplcntAttachmentGroupId());
		Optional<CmmtAtchmnfl> opt = fileList.stream().filter(x -> CoreUtils.string.equals(x.getAttachmentId(), attachmentId)).findFirst();
		if(!opt.isPresent()) {
			throw new InvalidationException("요청한 이의신청의 첨부파일 정보가 없습니다.");
		}

		attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
	}


	/**
	 * 심의결과 첨부파일 일괄 다운로드
	 * @param response
	 * @param objcReqstId
	 */
	public void getDlbrtAtchmnflAll(HttpServletResponse response, String objcReqstId) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		FrontSlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectFrontDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}
		if(CoreUtils.string.isBlank(info.getDlbrtAttachmentGroupId())) {
			throw new InvalidationException("요청한 이의신청의 심의결과 첨부파일이 존재하지 않습니다.");
		}
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), info.getDlbrtAttachmentGroupId(), "결과이의신청_심의결과_첨부파일");
	}


	/**
	 * 심의결과 첨부파일 다운로드
	 * @param response
	 * @param objcReqstId
	 * @param attachmentId
	 */
	public void getDlbrtAtchmnfl(HttpServletResponse response, String objcReqstId, String attachmentId) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		FrontSlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectFrontDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}
		if(CoreUtils.string.isBlank(info.getDlbrtAttachmentGroupId())) {
			throw new InvalidationException("요청한 이의신청의 심의결과 첨부파일이 존재하지 않습니다.");
		}

		List<CmmtAtchmnfl> fileList = attachmentService.getFileInfos_group(info.getDlbrtAttachmentGroupId());
		Optional<CmmtAtchmnfl> opt = fileList.stream().filter(x -> CoreUtils.string.equals(x.getAttachmentId(), attachmentId)).findFirst();
		if(!opt.isPresent()) {
			throw new InvalidationException("요청한 이의신청의 첨부파일 정보가 없습니다.");
		}

		attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
	}


	/**
	 * 신청취소
	 * @param objcReqstId
	 */
	public void modifyCancel(String objcReqstId) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		FrontSlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectFrontDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}
		Date now = new Date();
		if(CoreUtils.string.equals("최종선정", info.getEvlStepNm())) {
			UsptLastSlctnObjcReqst lastInfo = usptLastSlctnObjcReqstDao.select(objcReqstId);
			lastInfo.setLastSlctnObjcProcessSttusCd(Code.lastSlctnObjcProcessSttus.신청취소);
			lastInfo.setUpdatedDt(now);
			lastInfo.setUpdaterId(worker.getMemberId());
			if(usptLastSlctnObjcReqstDao.update(lastInfo) != 1) {
				throw new InvalidationException("신청취소 처리 중 오류가 발생했습니다.");
			}
		} else {
			UsptEvlSlctnObjcReqst evlInfo = usptEvlSlctnObjcReqstDao.select(objcReqstId);
			evlInfo.setLastSlctnObjcProcessSttusCd(Code.lastSlctnObjcProcessSttus.신청취소);
			evlInfo.setUpdatedDt(now);
			evlInfo.setUpdaterId(worker.getMemberId());

			if(usptEvlSlctnObjcReqstDao.update(evlInfo) != 1) {
				throw new InvalidationException("신청취소 처리 중 오류가 발생했습니다.");
			}
		}
	}

	/**
	 * 반려사유 조회
	 * @param objcReqstId
	 * @return
	 */
	public RejectDto getRejectReason(String objcReqstId) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		FrontSlctnObjcDto info = usptEvlSlctnObjcReqstDao.selectFrontDetail(objcReqstId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 이의신청 정보가 존재하지않습니다.");
		}
		if(!CoreUtils.string.equals(Code.lastSlctnObjcProcessSttus.반려, info.getLastSlctnObjcProcessSttusCd())) {
			throw new InvalidationException("요청한 이의신청의 상태가 반려상태가 아닙니다.");
		}
		if(CoreUtils.string.equals("최종선정", info.getEvlStepNm())) {
			return usptLastSlctnObjcProcessHistDao.selectRejectReason(objcReqstId);
		} else {
			return usptEvlObjcProcessHistDao.selectRejectReason(objcReqstId);
		}
	}


}
