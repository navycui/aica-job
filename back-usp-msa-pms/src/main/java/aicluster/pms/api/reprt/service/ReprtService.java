package aicluster.pms.api.reprt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.SmsUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.dto.SmsSendParam;
import aicluster.framework.notification.nhn.email.EmailReceiverResult;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.notification.nhn.sms.SmsRecipientResult;
import aicluster.framework.notification.nhn.sms.SmsResult;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.reprt.dto.PresentnReqParam;
import aicluster.pms.api.reprt.dto.ReprtListParam;
import aicluster.pms.api.reprt.dto.ReprtMakeupDto;
import aicluster.pms.api.reprt.dto.ReprtMakeupParam;
import aicluster.pms.common.dao.UsptReprtDao;
import aicluster.pms.common.dao.UsptReprtHistDao;
import aicluster.pms.common.dto.ReprtDto;
import aicluster.pms.common.dto.ReprtListDto;
import aicluster.pms.common.entity.UsptReprt;
import aicluster.pms.common.entity.UsptReprtHist;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReprtService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private UsptReprtDao usptReprtDao;
	@Autowired
	private UsptReprtHistDao usptReprtHistDao;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

	/**
	 * 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<ReprtListDto> getList(ReprtListParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptReprtDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<ReprtListDto> list = usptReprtDao.selectList(param);
		CorePagination<ReprtListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 목록 엑셀 저장 조회
	 * @param param
	 * @return
	 */
	public List<ReprtListDto> getListExcelDwld(ReprtListParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptReprtDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		param.setItemsPerPage(totalItems);
		return usptReprtDao.selectList(param);
	}


	/**
	 * 목록 첨부파일 다운로드
	 * @param response
	 * @param param
	 */
	public void getAtchmnflDwld(HttpServletResponse response, ReprtListParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		param.setInsiderId(worker.getMemberId());
		List<ReprtListDto> list = this.getListExcelDwld(param);
		List<CmmtAtchmnfl> attachList = new ArrayList<CmmtAtchmnfl>();

		if(list.size() > 0) {
			for(ReprtListDto info : list) {
				if(!CoreUtils.string.isBlank(info.getAttachmentGroupId())) {
					attachList.addAll(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));
				}
			}
			if(attachList.size() > 0) {
				String[] attachmentIds = attachList.stream().map(x->x.getAttachmentId()).toArray(String[]::new);
				attachmentService.downloadFiles(response, config.getDir().getUpload(), attachmentIds, "중간보고서_첨부파일");
			} else {
				throw new InvalidationException("다운받을 첨부파일이 없습니다.");
			}
		} else {
			throw new InvalidationException("다운받을 첨부파일이 없습니다.");
		}
	}


	/**
	 * 목록 일괄 제출요청 처리
	 * @param trgetIdList
	 * @param reprtTypeCd
	 */
	public String addPresentnReq(PresentnReqParam param, String reprtTypeCd) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		ReprtListParam listParam = new ReprtListParam();
		listParam.setInsiderId(worker.getMemberId());
		listParam.setTrgetIdList(param.getTrgetIdList());
		listParam.setReprtTypeCd(reprtTypeCd);
		List<ReprtListDto> list = this.getListExcelDwld(listParam);
		if(!list.isEmpty()) {
			if(!CoreUtils.string.equals(list.get(0).getChargerAuthorCd(), Code.사업담당자_수정권한)) {
				throw new InvalidationException("처리 권한이 없습니다.");
			}
		} else {
			throw new InvalidationException("처리 권한이 없습니다.");
		}

		if(CoreUtils.string.isBlank(param.getSndngMth())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "발송방법"));
		}
		if(CoreUtils.string.isBlank(param.getSndngCn())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "발송내용"));
		}

		/* 이메일 / SMS 전송 Start -------------------------------------------------------------------*/
		String emailCn = CoreUtils.file.readFileString("/form/email/email-common.html");
		SmsSendParam smsParam = new SmsSendParam();
		EmailSendParam esParam = new EmailSendParam();
		esParam.setContentHtml(true);
		esParam.setEmailCn(emailCn);
		esParam.setTitle("AICA 안내문");

		StringBuffer sbMsg = new StringBuffer();
		for(ReprtListDto info : list) {
			Integer cnt = usptReprtDao.selectCount(info.getLastSlctnTrgetId(), reprtTypeCd);
			if(cnt > 0) {
				throw new InvalidationException("이미 제출 요청한 회원이 존재합니다.\n확인 후 다시 요청하세요!");
			}

			esParam.setContentHtml(true);
			if(param.getSndngMth().contains(Code.sndngMth.이메일)) {
				if(CoreUtils.string.isNotEmpty(info.getEmail())){
					Map<String, String> templateParam = new HashMap<>();
					templateParam.put("memNm", info.getMemberNm());
					templateParam.put("cnVal", param.getSndngCn());
					esParam.addRecipient(info.getEmail(), info.getMemberNm(), templateParam);
				} else {
					sbMsg.append("[" + info.getMemberNm() +"] 이메일 정보가 없습니다.\n");
					log.debug("[" + info.getMemberNm() +"] 이메일 정보가 없습니다.");
				}
			}

			if(param.getSndngMth().contains(Code.sndngMth.SMS)){
				if(CoreUtils.string.isNotEmpty(info.getMobileNo())){
					smsParam.setSmsCn("AICA 안내문\n " + param.getSndngCn());
					smsParam.addRecipient(info.getMobileNo());
				} else {
					sbMsg.append("[" + info.getMemberNm() +"] 핸드폰번호 정보가 없습니다.\n");
					log.debug("[" + info.getMemberNm() +"] 핸드폰번호 정보가 없습니다.");
				}
			}
		}

		EmailResult er = null;
		SmsResult sr = null;
		if(param.getSndngMth().contains(Code.sndngMth.이메일) && esParam.getRecipientList() != null && esParam.getRecipientList().size() > 0 ) {
			er = emailUtils.send(esParam);
		}
		if(param.getSndngMth().contains(Code.sndngMth.SMS) && smsParam.getRecipientList() != null && smsParam.getRecipientList().size() > 0 ){
			sr = smsUtils.send(smsParam);
		}
		/* 이메일 / SMS 전송 End -------------------------------------------------------------------*/

		Date now = new Date();
		String sndngMth = "";
		if(param.getSndngMth().contains(Code.sndngMth.이메일) && param.getSndngMth().contains(Code.sndngMth.SMS)) {
			sndngMth = Code.sndngMth.ALL;
		} else {
			sndngMth = param.getSndngMth();
		}

		UsptReprt regInfo = new UsptReprt();
		regInfo.setCreatorId(worker.getMemberId());
		regInfo.setCreatedDt(now);
		regInfo.setUpdaterId(worker.getMemberId());
		regInfo.setUpdatedDt(now);
		regInfo.setReprtTypeCd(reprtTypeCd);
		regInfo.setReprtSttusCd(Code.reprtSttus.제출요청);
		regInfo.setSndngMth(sndngMth);

		for(ReprtListDto info : list) {
			if(er != null) {
				List<EmailReceiverResult> erList = er.getReceiverList();
				Optional<EmailReceiverResult> opt = erList.stream().filter(x->x.getReceiveMailAddr().equals(info.getEmail())).findFirst();
				if(opt.isPresent()) {
					EmailReceiverResult result = opt.get();
					if(result.getIsSuccessful()) {
						regInfo.setSndngCn(param.getSndngCn());
						regInfo.setRecentSendDate(now);
					} else {
						sbMsg.append("[" + info.getMemberNm() +"] 이메일 발송이 실패하였습니다.\n");
						log.debug("[" + info.getMemberNm() +"] 이메일 발송이 실패하였습니다.");
					}
				}
			}
			if(sr != null) {
				List<SmsRecipientResult> srList = sr.getRecipientList();
				Optional<SmsRecipientResult> opt = srList.stream().filter(x->x.getRecipientNo().equals(info.getMobileNo())).findFirst();
				if(opt.isPresent()) {
					SmsRecipientResult result = opt.get();
					if(result.getIsSuccessful()) {
						regInfo.setSndngCn(param.getSndngCn());
						regInfo.setRecentSendDate(now);
					} else {
						sbMsg.append("[" + info.getMemberNm() +"] SMS 발송이 실패하였습니다.\n");
						log.debug("[" + info.getMemberNm() +"] SMS 발송이 실패하였습니다.");
					}
				}
			}
			regInfo.setLastSlctnTrgetId(info.getLastSlctnTrgetId());
			regInfo.setReprtId(CoreUtils.string.getNewId(Code.prefix.보고서));
			usptReprtDao.insert(regInfo);

			UsptReprtHist hist = new UsptReprtHist();
			hist.setReprtId(regInfo.getReprtId());
			hist.setHistId(CoreUtils.string.getNewId(Code.prefix.보고서이력));
			hist.setOpetrId(worker.getMemberId());
			hist.setReprtSttusCd(regInfo.getReprtSttusCd());
			hist.setProcessCn(param.getSndngCn());
			hist.setCreatorId(worker.getMemberId());
			hist.setCreatedDt(now);
			usptReprtHistDao.insert(hist);
		}
		return sbMsg.toString();
	}


	/**
	 * 보고서 상세조회
	 * @param reprtId
	 * @return
	 */
	public ReprtDto get(HttpServletRequest request, String reprtId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		ReprtDto info = usptReprtDao.selectBasic(reprtId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		if(!CoreUtils.string.isBlank(info.getAttachmentGroupId())) {
			info.setAttachFileList(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));
		}

		// 이력 생성
		LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("보고서 상세조회")
				.trgterId(info.getMemberId())
				.email("")
				.birthday("")
				.mobileNo(info.getMbtlnum())
				.build();
		indvdlInfSrchUtils.insert(logParam);

		return info;
	}


	/**
	 * 보완요청 조회
	 * @param reprtId
	 * @return
	 */
	public ReprtMakeupDto getMakeup(String reprtId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		ReprtDto info = usptReprtDao.selectBasic(reprtId, worker.getMemberId());
		if(info == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		UsptReprt reprtInfo = usptReprtDao.select(reprtId);
		ReprtMakeupDto dto = new ReprtMakeupDto();
		dto.setMakeupReqDate(date.format(reprtInfo.getMakeupReqDe(), "yyyy-MM-dd HH:mm"));
		dto.setMakeupReqCn(reprtInfo.getMakeupReqCn());
		if(!CoreUtils.string.isBlank(reprtInfo.getMakeupAttachmentGroupId())) {
			dto.setAttachFileList(attachmentService.getFileInfos_group(reprtInfo.getMakeupAttachmentGroupId()));
		}
		return dto;
	}

	/**
	 * 보완요청
	 * @param reprtId
	 * @param makeupInfo
	 * @param fileList
	 */
	public void makeupInfo(String reprtId, ReprtMakeupParam makeupInfo, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		ReprtDto chkInfo = usptReprtDao.selectBasic(reprtId, worker.getMemberId());
		if(chkInfo == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		if(!CoreUtils.string.equals(chkInfo.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}

		if(CoreUtils.string.isBlank(makeupInfo.getMakeupReqCn())){
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "보완요청 사유"));
		}
		UsptReprt info = usptReprtDao.select(reprtId);
		if(info == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}

		if(fileList != null && fileList.size() > 0) {
			String[] exts = {"DOCX", "PPTX", "XLSX", "PDF", "PNG"};
			int size = 100 * (1024 * 1024);
			for(MultipartFile fileInfo : fileList) {
				if(CoreUtils.string.isBlank(info.getMakeupAttachmentGroupId())) {
					CmmtAtchmnfl caInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), fileInfo, exts, size);
					info.setMakeupAttachmentGroupId(caInfo.getAttachmentGroupId());
				} else {
					attachmentService.uploadFile_toGroup(config.getDir().getUpload(), info.getMakeupAttachmentGroupId(), fileInfo, exts, size);
				}
			}
		}

		Date now = new Date();
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		info.setMakeupReqDe(now);
		info.setMakeupReqCn(makeupInfo.getMakeupReqCn());
		info.setReprtSttusCd(Code.reprtSttus.보완요청);
		if(usptReprtDao.update(info) != 1) {
			throw new InvalidationException("보완요청 저장 중 오류가 발생했습니다.");
		}

		if(makeupInfo.getDeleteFileList() != null && makeupInfo.getDeleteFileList().size() > 0) {
			for(CmmtAtchmnfl attachInfo : makeupInfo.getDeleteFileList()) {
				attachmentService.removeFile_keepEmptyGroup(config.getDir().getUpload(), attachInfo.getAttachmentId());
			}
		}

		UsptReprtHist hist = new UsptReprtHist();
		hist.setReprtId(info.getReprtId());
		hist.setHistId(CoreUtils.string.getNewId(Code.prefix.보고서이력));
		hist.setOpetrId(worker.getMemberId());
		hist.setReprtSttusCd(info.getReprtSttusCd());
		hist.setProcessCn(info.getMakeupReqCn());
		hist.setCreatorId(worker.getMemberId());
		hist.setCreatedDt(now);
		usptReprtHistDao.insert(hist);
	}


	/**
	 * 접수완료 / 접수완료취소 처리
	 * @param reprtId
	 * @param reprtSttusCd
	 */
	public void modifyReprtSttus(String reprtId, String reprtSttusCd) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		ReprtDto chkInfo = usptReprtDao.selectBasic(reprtId, worker.getMemberId());
		if(chkInfo == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		if(!CoreUtils.string.equals(chkInfo.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}
		UsptReprt info = usptReprtDao.select(reprtId);
		if(info == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}

		String msg = "접수완료취소";
		if(CoreUtils.string.equals(Code.reprtSttus.접수완료, reprtSttusCd)) {
			msg = "접수완료";
		}

		Date now = new Date();
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		info.setReprtSttusCd(reprtSttusCd);
		if(usptReprtDao.update(info) != 1) {
			throw new InvalidationException(msg + " 중 오류가 발생했습니다.");
		}

		UsptReprtHist hist = new UsptReprtHist();
		hist.setReprtId(info.getReprtId());
		hist.setHistId(CoreUtils.string.getNewId(Code.prefix.보고서이력));
		hist.setOpetrId(worker.getMemberId());
		hist.setReprtSttusCd(info.getReprtSttusCd());
		hist.setProcessCn(msg + " 처리되었습니다.");
		hist.setCreatorId(worker.getMemberId());
		hist.setCreatedDt(now);
		usptReprtHistDao.insert(hist);
	}


	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param reprtId
	 * @param attachmentId
	 */
	public void getAtchmnflDwld(HttpServletResponse response, String reprtId, String attachmentId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		ReprtDto chkInfo = usptReprtDao.selectBasic(reprtId, worker.getMemberId());
		if(chkInfo == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		if(CoreUtils.string.isBlank(attachmentId)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "첨부파일ID"));
		}
		UsptReprt info = usptReprtDao.select(reprtId);
		if(info == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
	}

	/**
	 * 첨부파일 전체 다운로드
	 * @param response
	 * @param reprtId
	 */
	public void getAtchmnflAllDwld(HttpServletResponse response, String reprtId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		ReprtDto chkInfo = usptReprtDao.selectBasic(reprtId, worker.getMemberId());
		if(chkInfo == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		UsptReprt info = usptReprtDao.select(reprtId);
		if(info == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		if(!CoreUtils.string.isBlank(info.getAttachmentGroupId())) {
			attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), info.getAttachmentGroupId(), "보고서_첨부파일");
		} else {
			throw new InvalidationException("다운로드할 첨부파일이 없습니다.");
		}
	}


	/**
	 * 처리이력 조회
	 * @param reprtId
	 * @return
	 */
	public JsonList<UsptReprtHist> getHistList(String reprtId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		ReprtDto chkInfo = usptReprtDao.selectBasic(reprtId, worker.getMemberId());
		if(chkInfo == null) {
			throw new InvalidationException("요청한 보고서가 존재하지않습니다.");
		}
		return new JsonList<>(usptReprtHistDao.selectList(reprtId));
	}
}
