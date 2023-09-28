package aicluster.pms.api.rslt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
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
import aicluster.pms.api.bsns.dto.RsltIdxDetailIem;
import aicluster.pms.api.bsns.dto.RsltIdxStdIem;
import aicluster.pms.api.rslt.dto.PresentnReqParam;
import aicluster.pms.api.rslt.dto.RsltDto;
import aicluster.pms.api.rslt.dto.RsltIdxIemDto;
import aicluster.pms.api.rslt.dto.RsltIdxParam;
import aicluster.pms.api.rslt.dto.RsltListParam;
import aicluster.pms.api.rslt.dto.RsltMakeupParam;
import aicluster.pms.api.rslt.dto.RsltSaveBeforeDto;
import aicluster.pms.api.rslt.dto.RsltStatsDto;
import aicluster.pms.api.rslt.dto.RsltStatsParam;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptBsnsRsltIdxDao;
import aicluster.pms.common.dao.UsptRsltDao;
import aicluster.pms.common.dao.UsptRsltHistDao;
import aicluster.pms.common.dao.UsptRsltIdxIemCnDao;
import aicluster.pms.common.dao.UsptRsltIdxIemCnHistDao;
import aicluster.pms.common.dao.UsptRsltIdxIemDao;
import aicluster.pms.common.dao.UsptRsltIdxIemHistDao;
import aicluster.pms.common.dto.ApplcntBsnsBasicDto;
import aicluster.pms.common.dto.RsltListDto;
import aicluster.pms.common.dto.RsltStatsListDto;
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
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RsltService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
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
	private UsptBsnsRsltIdxDao usptBsnsRsltIdxDao;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;

	/**
	 * 성과목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<RsltListDto> getList(RsltListParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptRsltDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<RsltListDto> list = usptRsltDao.selectList(param);
		CorePagination<RsltListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 성과 목록 엑셀 저장
	 * @param param
	 * @return
	 */
	public List<RsltListDto> getListExcelDwld(RsltListParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptRsltDao.selectListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		param.setItemsPerPage(totalItems);
		return usptRsltDao.selectList(param);
	}



	/**
	 * 일괄요청
	 * @param param
	 * @return
	 */
	public String addPresentnReq(PresentnReqParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		RsltListParam listParam = new RsltListParam();
		listParam.setInsiderId(worker.getMemberId());
		listParam.setApplyIdList(param.getApplyIdList());
		List<RsltListDto> list = this.getListExcelDwld(listParam);
		if(!list.isEmpty()) {
			for(RsltListDto dto : list) {
				if(!CoreUtils.string.equals(dto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
					throw new InvalidationException("처리 권한이 없습니다.");
				}
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
		for(RsltListDto info : list) {
			UsptRslt checkInfo = usptRsltDao.selectByApplyId(info.getApplyId());
			if(checkInfo != null) {
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
		if(param.getSndngMth().contains(Code.sndngMth.이메일) && esParam.getRecipientList() != null && esParam.getRecipientList().size() > 0) {
			er = emailUtils.send(esParam);
		}
		if(param.getSndngMth().contains(Code.sndngMth.SMS) && smsParam.getRecipientList() != null && smsParam.getRecipientList().size() > 0){
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

		UsptRslt regInfo = new UsptRslt();
		regInfo.setCreatorId(worker.getMemberId());
		regInfo.setCreatedDt(now);
		regInfo.setUpdaterId(worker.getMemberId());
		regInfo.setUpdatedDt(now);
		regInfo.setRsltSttusCd(Code.rsltSttus.제출요청);
		regInfo.setSndngMth(sndngMth);

		for(RsltListDto info : list) {
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

			regInfo.setApplyId(info.getApplyId());
			regInfo.setRsltId(CoreUtils.string.getNewId(Code.prefix.성과));
			usptRsltDao.insert(regInfo);

			UsptRsltHist hist = new UsptRsltHist();
			hist.setRsltId(regInfo.getRsltId());
			hist.setRsltHistId(CoreUtils.string.getNewId(Code.prefix.성과이력));
			hist.setRsltSttusCd(regInfo.getRsltSttusCd());
			hist.setProcessCn(param.getSndngCn());
			hist.setProcessMberType(Code.INSIDER_CODE_TYPE);
			hist.setCreatorId(worker.getMemberId());
			hist.setCreatedDt(now);
			usptRsltHistDao.insert(hist);

			/* 성과지표항목 / 성과지표항목내용 미리저장 시작.---------------------------------------------------------------------------------------*/
			ApplcntBsnsBasicDto basicInfo = usptBsnsPblancApplcntDao.selectBsnsBasic(info.getApplyId(), worker.getMemberId());
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
		}
		return sbMsg.toString();
	}



	/**
	 * 성과 등록 전 정보 조회
	 * @param applyId
	 * @param processMberType : 처리회원유형
	 * @return
	 */
	public RsltSaveBeforeDto getSaveBefore(String applyId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		RsltSaveBeforeDto dto = new RsltSaveBeforeDto();
		ApplcntBsnsBasicDto basicInfo = usptBsnsPblancApplcntDao.selectBsnsBasic(applyId, worker.getMemberId());
		if(basicInfo == null) {
			throw new InvalidationException("요청한 사업신청 정보가 존재하지않습니다.");
		}
		dto.setBasicInfo(basicInfo);
		dto.setRsltIdxList(usptBsnsRsltIdxDao.selectList(basicInfo.getBsnsCd()));
		return dto;
	}


	/**
	 * 관리자 성과 제출
	 * @param applyId
	 * @param infoList
	 * @param rsltIdxFileList
	 * @param attachFileList
	 * @return
	 */
	public String add(String applyId, List<RsltIdxParam> infoList, List<MultipartFile> rsltIdxFileList, List<MultipartFile> attachFileList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		ApplcntBsnsBasicDto basicInfo = usptBsnsPblancApplcntDao.selectBsnsBasic(applyId, worker.getMemberId());
		if(!CoreUtils.string.equals(basicInfo.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}
		UsptRslt checkInfo = usptRsltDao.selectByApplyId(applyId);
		if(checkInfo != null) {
			throw new InvalidationException("이미 제출 요청한 정보가 존재합니다.\n확인 후 다시 요청하세요!");
		}

		Date now = new Date();
		UsptRslt rslt = new UsptRslt();
		rslt.setRsltId(CoreUtils.string.getNewId(Code.prefix.성과));
		rslt.setApplyId(applyId);
		rslt.setRsltSttusCd(Code.rsltSttus.접수완료);
		rslt.setPresentnDt(now);
		rslt.setCreatedDt(now);
		rslt.setCreatorId(worker.getMemberId());
		rslt.setUpdatedDt(now);
		rslt.setUpdaterId(worker.getMemberId());

		String[] exts = {"DOCX", "PPTX", "XLSX", "PDF", "PNG"};
		int size = 100 * (1024 * 1024);
		if(attachFileList != null && attachFileList.size() > 0) {
			for(MultipartFile fileInfo : attachFileList) {
				if(CoreUtils.string.isBlank(rslt.getAttachmentGroupId())) {
					CmmtAtchmnfl caInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), fileInfo, exts, size);
					rslt.setAttachmentGroupId(caInfo.getAttachmentGroupId());
				} else {
					attachmentService.uploadFile_toGroup(config.getDir().getUpload(), rslt.getAttachmentGroupId(), fileInfo, exts, size);
				}
			}
		}

		usptRsltDao.insert(rslt);

		String presentnHistId = CoreUtils.string.getNewId(Code.prefix.성과이력);

		UsptRsltHist hist = new UsptRsltHist();
		hist.setRsltId(rslt.getRsltId());
		hist.setRsltHistId(presentnHistId);
		hist.setRsltSttusCd(Code.rsltSttus.제출);
		hist.setPresentnDt(now);
		hist.setProcessCn("관리자가 성과를 제출합니다.");
		hist.setProcessMberType(Code.INSIDER_CODE_TYPE);
		hist.setCreatorId(worker.getMemberId());
		hist.setCreatedDt(now);
		usptRsltHistDao.insert(hist);

		hist.setRsltId(rslt.getRsltId());
		hist.setRsltHistId(CoreUtils.string.getNewId(Code.prefix.성과이력));
		hist.setRsltSttusCd(rslt.getRsltSttusCd());
		hist.setPresentnDt(now);
		hist.setProcessCn("접수완료 처리되었습니다");
		hist.setProcessMberType(Code.INSIDER_CODE_TYPE);
		hist.setCreatorId(worker.getMemberId());
		hist.setCreatedDt(now);
		usptRsltHistDao.insert(hist);


		UsptRsltIdxIem regIdxIem = new UsptRsltIdxIem();
		regIdxIem.setRsltId(rslt.getRsltId());
		regIdxIem.setCreatedDt(now);
		regIdxIem.setCreatorId(worker.getMemberId());
		regIdxIem.setUpdatedDt(now);
		regIdxIem.setUpdaterId(worker.getMemberId());

		for(RsltIdxParam param : infoList) {
			regIdxIem.setRsltIdxIemId(CoreUtils.string.getNewId(Code.prefix.성과지표항목));
			regIdxIem.setRsltIdxId(param.getRsltIdxId());
			if(param.getAttachFileOrder() > -1) {
				if(rsltIdxFileList != null && rsltIdxFileList.size() > param.getAttachFileOrder()) {
					MultipartFile file = rsltIdxFileList.get(param.getAttachFileOrder());
					CmmtAtchmnfl caInfo = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), file, exts, size);
					regIdxIem.setPrufAttachmentId(caInfo.getAttachmentId());
				}
			}
			usptRsltIdxIemDao.insert(regIdxIem);

			UsptRsltIdxIemHist rsltIdxIemHist = new UsptRsltIdxIemHist();
			rsltIdxIemHist.setRsltHistId(presentnHistId);
			rsltIdxIemHist.setRsltIdxIemId(regIdxIem.getRsltIdxIemId());
			rsltIdxIemHist.setPrufAttachmentId(regIdxIem.getPrufAttachmentId());
			rsltIdxIemHist.setCreatorId(worker.getMemberId());
			rsltIdxIemHist.setCreatedDt(now);
			usptRsltIdxIemHistDao.insert(rsltIdxIemHist);

			for(UsptRsltIdxIemCn rsltIdxIemCn : param.getRsltIdxIemCnList()) {
				rsltIdxIemCn.setRsltIdxIemCnId(CoreUtils.string.getNewId(Code.prefix.성과지표항목내용));
				rsltIdxIemCn.setRsltIdxIemId(regIdxIem.getRsltIdxIemId());
				rsltIdxIemCn.setCreatedDt(now);
				rsltIdxIemCn.setCreatorId(worker.getMemberId());
				rsltIdxIemCn.setUpdatedDt(now);
				rsltIdxIemCn.setUpdaterId(worker.getMemberId());
				rsltIdxIemCn.setSortOrder(rsltIdxIemCn.getSortOrder());
				rsltIdxIemCn.setDeleteAt(false);
				if(CoreUtils.string.equals("LIST", regIdxIem.getRsltIdxTypeCd())) {
					rsltIdxIemCn.setRsltIdxStdIemId(null);
				}
				usptRsltIdxIemCnDao.insert(rsltIdxIemCn);

				UsptRsltIdxIemCnHist rsltIdxIemCnHist = new UsptRsltIdxIemCnHist();
				rsltIdxIemCnHist.setRsltHistId(presentnHistId);
				rsltIdxIemCnHist.setRsltIdxIemId(regIdxIem.getRsltIdxIemId());
				rsltIdxIemCnHist.setRsltIdxIemCnId(rsltIdxIemCn.getRsltIdxIemCnId());
				rsltIdxIemCnHist.setRsltIdxIemCn(rsltIdxIemCn.getRsltIdxIemCn());
				rsltIdxIemCnHist.setSortOrder(rsltIdxIemCn.getSortOrder());
				rsltIdxIemCnHist.setCreatedDt(now);
				rsltIdxIemCnHist.setCreatorId(worker.getMemberId());
				usptRsltIdxIemCnHistDao.insert(rsltIdxIemCnHist);
			}
		}
		return rslt.getRsltId();
	}



	/**
	 * 성과 상세조회
	 * @param rsltId
	 * @return
	 */
	public RsltDto get(String rsltId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		UsptRslt info = usptRsltDao.select(rsltId);
		if(info == null) {
			throw new InvalidationException("요청한 성과 정보가 존재하지않습니다.");
		}
		ApplcntBsnsBasicDto basicDto = usptBsnsPblancApplcntDao.selectBsnsBasic(info.getApplyId(), worker.getMemberId());
		if(basicDto == null) {
			throw new InvalidationException("권한이 없습니다.");
		}

		RsltDto dto = new RsltDto();
		dto.setRsltYear(info.getRsltYear());
		dto.setRsltSttusCd(info.getRsltSttusCd());
		dto.setRsltSttus(info.getRsltSttus());
		dto.setBasicInfo(basicDto);
		dto.setAttachFileList(attachmentService.getFileInfos_group(info.getAttachmentGroupId()));

		List<UsptRsltIdxIem> rsltIdxIemList = usptRsltIdxIemDao.selectList(rsltId);
		List<RsltIdxIemDto> rsltIdxIemDtoList = new ArrayList<RsltIdxIemDto>();
		if(rsltIdxIemList != null) {
			for(UsptRsltIdxIem idxIemInfo : rsltIdxIemList) {
				RsltIdxIemDto iemDto = new RsltIdxIemDto();
				iemDto.setRsltIdxId(idxIemInfo.getRsltIdxId());
				iemDto.setRsltIdxNm(idxIemInfo.getRsltIdxNm());
				iemDto.setRsltIdxIemId(idxIemInfo.getRsltIdxIemId());
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
	 * 보완요청
	 * @param rsltId
	 * @param makeupInfo
	 * @param fileList
	 */
	public void modifyMakeup(String rsltId, RsltMakeupParam makeupInfo, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		UsptRslt info = usptRsltDao.select(rsltId);
		if(info == null) {
			throw new InvalidationException("요청한 성과 정보가 존재하지않습니다.");
		}
		ApplcntBsnsBasicDto basicDto = usptBsnsPblancApplcntDao.selectBsnsBasic(info.getApplyId(), worker.getMemberId());
		if(!CoreUtils.string.equals(basicDto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}

		if(CoreUtils.string.isBlank(makeupInfo.getMakeupReqCn())){
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "보완요청 사유"));
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
		info.setRsltSttusCd(Code.rsltSttus.보완요청);
		if(usptRsltDao.update(info) != 1) {
			throw new InvalidationException("보완요청 저장 중 오류가 발생했습니다.");
		}

		if(makeupInfo.getDeleteFileList() != null && makeupInfo.getDeleteFileList().size() > 0) {
			for(CmmtAtchmnfl attachInfo : makeupInfo.getDeleteFileList()) {
				attachmentService.removeFile_keepEmptyGroup(config.getDir().getUpload(), attachInfo.getAttachmentId());
			}
		}

		UsptRsltHist hist = new UsptRsltHist();
		hist.setRsltId(info.getRsltId());
		hist.setRsltHistId(CoreUtils.string.getNewId(Code.prefix.성과이력));
		hist.setRsltSttusCd(info.getRsltSttusCd());
		hist.setProcessCn(info.getMakeupReqCn());
		hist.setProcessMberType(Code.INSIDER_CODE_TYPE);
		hist.setCreatorId(worker.getMemberId());
		hist.setCreatedDt(now);
		usptRsltHistDao.insert(hist);
	}


	/**
	 * 성과 접수완료/접수완료취소 처리
	 * @param rsltId
	 * @param rsltSttusCd
	 */
	public void modifyRsltSttus(String rsltId, String rsltSttusCd) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptRslt info = usptRsltDao.select(rsltId);
		if(info == null) {
			throw new InvalidationException("요청한 성과정보가  존재하지않습니다.");
		}
		ApplcntBsnsBasicDto basicDto = usptBsnsPblancApplcntDao.selectBsnsBasic(info.getApplyId(), worker.getMemberId());
		if(!CoreUtils.string.equals(basicDto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}

		String msg = "접수완료취소";
		if(CoreUtils.string.equals(Code.rsltSttus.접수완료, rsltSttusCd)) {
			msg = "접수완료";
		}

		Date now = new Date();
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		info.setRsltSttusCd(rsltSttusCd);
		if(usptRsltDao.update(info) != 1) {
			throw new InvalidationException(msg + " 중 오류가 발생했습니다.");
		}

		UsptRsltHist hist = new UsptRsltHist();
		hist.setRsltId(info.getRsltId());
		hist.setRsltHistId(CoreUtils.string.getNewId(Code.prefix.성과이력));
		hist.setRsltSttusCd(info.getRsltSttusCd());
		hist.setProcessCn(msg + " 처리되었습니다.");
		hist.setProcessMberType(Code.INSIDER_CODE_TYPE);
		hist.setCreatorId(worker.getMemberId());
		hist.setCreatedDt(now);
		usptRsltHistDao.insert(hist);
	}


	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param rsltId
	 * @param attachmentId
	 */
	public void getAtchmnflDwld(HttpServletResponse response, String rsltId, String attachmentId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptRslt info = usptRsltDao.select(rsltId);
		if(info == null) {
			throw new InvalidationException("요청한 성과정보가  존재하지않습니다.");
		}
		ApplcntBsnsBasicDto basicDto = usptBsnsPblancApplcntDao.selectBsnsBasic(info.getApplyId(), worker.getMemberId());
		if(basicDto == null) {
			throw new InvalidationException("권한이 없습니다.");
		}
		if(CoreUtils.string.isBlank(attachmentId)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "첨부파일ID"));
		}
		attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
	}

	/**
	 * 첨부파일 전체 다운로드
	 * @param response
	 * @param rsltId
	 */
	public void getAtchmnflAllDwld(HttpServletResponse response, String rsltId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptRslt info = usptRsltDao.select(rsltId);
		if(info == null) {
			throw new InvalidationException("요청한 성과정보가  존재하지않습니다.");
		}
		ApplcntBsnsBasicDto basicDto = usptBsnsPblancApplcntDao.selectBsnsBasic(info.getApplyId(), worker.getMemberId());
		if(basicDto == null) {
			throw new InvalidationException("권한이 없습니다.");
		}
		if(!CoreUtils.string.isBlank(info.getAttachmentGroupId())) {
			attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), info.getAttachmentGroupId(), "성과_첨부파일");
		} else {
			throw new InvalidationException("다운로드할 첨부파일이 없습니다.");
		}
	}


	/**
	 * 처리이력 조회
	 * @param rsltId
	 * @return
	 */
	public List<UsptRsltHist> getHistList(String rsltId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptRslt info = usptRsltDao.select(rsltId);
		if(info == null) {
			throw new InvalidationException("요청한 성과정보가  존재하지않습니다.");
		}
		ApplcntBsnsBasicDto basicDto = usptBsnsPblancApplcntDao.selectBsnsBasic(info.getApplyId(), worker.getMemberId());
		if(basicDto == null) {
			throw new InvalidationException("권한이 없습니다.");
		}
		return usptRsltHistDao.selectList(rsltId, Code.INSIDER_CODE_TYPE);
	}


	/**
	 * 성과현황 조회
	 * @param param
	 * @return
	 */
	public RsltStatsDto getStats(RsltStatsParam param){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(CoreUtils.string.isBlank(param.getBsnsYear())) {
			throw new InvalidationException("사업연도를 입력해주세요.\n사업연도는 필수항목입니다.");
		}
		if(CoreUtils.string.isBlank(param.getBsnsCd())) {
			throw new InvalidationException("사업을 선택해주세요.\n사업은 필수항목입니다.");
		}
		param.setBsnsBgnde(date.format(string.toDate(param.getBsnsBgnde()), "yyyyMMdd"));
		param.setBsnsEndde(date.format(string.toDate(param.getBsnsEndde()), "yyyyMMdd"));
		param.setInsiderId(worker.getMemberId());
		List<RsltStatsListDto> list = usptRsltDao.selectStatsList(param);

		RsltStatsDto dto = new RsltStatsDto();
		List<List<String>> resultList = new ArrayList<List<String>>();
		String[] sumryIem = null;
		Long[] sumryStats = null;
		if(list != null && list.size() > 0) {
			for(int idx=0; idx<list.size(); idx++) {
				RsltStatsListDto info = list.get(idx);
				String[] arrIemCn = info.getRsltIdxIemCn().split("@@@");
				if(idx == 0) {
					sumryStats = new Long[arrIemCn.length+1];
					sumryStats[0] = (long) list.size();
					// 요약항목 설정
					String title[] = info.getRsltIemNm().split("@@@");
					sumryIem = new String[title.length+1];
					sumryIem[0] = "기업수";
					for(int tIdx=0; tIdx<title.length; tIdx++) {
						sumryIem[tIdx+1] = title[tIdx];
					}
				}
				// 요약 내용 계산
				for(int idy=0; idy<arrIemCn.length; idy++) {
					String val = arrIemCn[idy];
					if(CoreUtils.string.isNotBlank(val)) {
						Long lVal = new Long(val.replaceAll(",", ""));
						if(sumryStats[idy+1] == null) {
							sumryStats[idy+1] = lVal;
						} else {
							sumryStats[idy+1] += lVal;
						}
					}
				}
				ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(arrIemCn));
				dataList.add(0, info.getMemberNm());
				dataList.add(0, String.valueOf(idx+1));
				resultList.add(dataList);
			}
		}
		if(sumryIem != null) {
			dto.setSumryIemList(Arrays.asList(sumryIem));
			dto.setSumryIemCnList(Arrays.asList(sumryStats));
			ArrayList<String> listIemList = new ArrayList<String>(dto.getSumryIemList());
			listIemList.add(0, "번호");
			listIemList.set(1, "사업자명");
			dto.setListIemList(listIemList);
			dto.setList(resultList);
		}

		return dto;
	}

}
