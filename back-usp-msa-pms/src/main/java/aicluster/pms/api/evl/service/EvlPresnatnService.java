package aicluster.pms.api.evl.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.UsptEvlTrgetDao;
import aicluster.pms.common.dao.UsptEvlTrgetPresentnHistDao;
import aicluster.pms.common.dto.CmmtSmsEmailSendDto;
import aicluster.pms.common.dto.EvlPresnatnListDto;
import aicluster.pms.common.dto.EvlPresnatnRequstDto;
import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptEvlTrget;
import aicluster.pms.common.entity.UsptEvlTrgetPresentnHist;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.LoggableException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EvlPresnatnService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;		/*파일*/
	@Autowired
	private CmmtMemberDao cmmtMemberDao;	/*회원*/
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private UsptEvlTrgetDao usptEvlTrgetDao;
	@Autowired
	private UsptEvlTrgetPresentnHistDao usptEvlTrgetPresentnHistDao;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

	//발표자료 제출대상 목록조회
	public CorePagination<EvlPresnatnListDto> getPresnatnTargetList(EvlPresnatnListDto evlPresnatnListDto, Long page) {

		if(page == null) {
			page = 1L;
		}

		if(evlPresnatnListDto.getItemsPerPage() == null) {
			evlPresnatnListDto.setItemsPerPage(ITEMS_PER_PAGE);
		}

		long totalItems = usptEvlTrgetDao.selectPresnatnTargetListCount(evlPresnatnListDto);

		evlPresnatnListDto.setExcel(false);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, evlPresnatnListDto.getItemsPerPage(), totalItems);
		evlPresnatnListDto.setBeginRowNum(info.getBeginRowNum());
		List<EvlPresnatnListDto> list = usptEvlTrgetDao.selectPresnatnTargetList(evlPresnatnListDto);

		//출력할 자료 생성 후 리턴
		CorePagination<EvlPresnatnListDto> pagination = new CorePagination<>(info, list);

		return pagination;
	}


	//발표자료 제출대상 목록조회 목록 엑셀다운로드
	public List<EvlPresnatnListDto> getPresnatnTargetListExcel(EvlPresnatnListDto evlPresnatnListDto) {
		evlPresnatnListDto.setExcel(true);
		return usptEvlTrgetDao.selectPresnatnTargetList(evlPresnatnListDto);
	}


	//발표자료 대상 상세조회
	public EvlPresnatnListDto get(HttpServletRequest request, String evlTrgetId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		EvlPresnatnListDto inputParam = new EvlPresnatnListDto();
		inputParam.setEvlTrgetId(evlTrgetId);
		List<EvlPresnatnListDto> resultData = usptEvlTrgetDao.selectPresnatnTargetList(inputParam);

		if (resultData.size() < 1) {
			throw new InvalidationException("대상 정보가 없습니다.");
		}

		/** 제출첨부파일 목록 **/
		if (string.isNotBlank(resultData.get(0).getAttachmentGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(resultData.get(0).getAttachmentGroupId());
			resultData.get(0).setAttachFileList(list);
		}

		/* 개인정보 조회 이력 생성  --------------------------------------------------------------------------------*/
		// 사업자정보
		LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("발표자료 관리 상세조회 - 사업자정보")
				.trgterId(resultData.get(0).getMemberId())
				.email("")
				.birthday("")
				.mobileNo(resultData.get(0).getMobileNo())
				.build();
		indvdlInfSrchUtils.insert(logParam);

		return resultData.get(0);
	}


	//제출요청
	public void modifyPresnatnRequst(EvlPresnatnRequstDto evlPresnatnRequstDto){
		Date now = new Date();

		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if(string.isBlank(evlPresnatnRequstDto.getResnCn())) {
			throw new InvalidationException("요청 사유 내용이 없습니다.");
		}

		if(evlPresnatnRequstDto.getUsptEvlTrgetList().size()<1) {
			throw new InvalidationException("제출 요청 대상자가 없습니다.");
		}

		// 유효체크
		for(UsptEvlTrget checkInfo : evlPresnatnRequstDto.getUsptEvlTrgetList()) {

				if(string.isBlank(checkInfo.getEvlTrgetId())) {
					throw new InvalidationException("대상자 정보가 없습니다.");
				}
				//대상조회
				if(usptEvlTrgetDao.select(checkInfo.getEvlTrgetId()) == null) {
					throw new InvalidationException("대상자 정보가 없습니다.");
				}

				EvlPresnatnListDto inputParam = new EvlPresnatnListDto();
				inputParam.setEvlTrgetId(checkInfo.getEvlTrgetId());

				List<EvlPresnatnListDto> resultData = usptEvlTrgetDao.selectPresnatnTargetList(inputParam);

				if (resultData.size() < 1) {
					throw new InvalidationException("대상 정보가 없습니다.");
				}else{
					String sttusCd = resultData.get(0).getPresnatnProcessDivCd(); //제출상태

					/*
					 * null        미제출
					 * PRPR01	제출요청
					 * PRPR02	제출
					 * PRPR03	보완요청
					 * PRPR04	접수완료
					 * PRPR05	접수완료취소
					 */

//					if(!string.equals(sttusCd, Code.presnatnProcessDiv.제출요청) && string.isNotBlank(sttusCd)){
//						throw new InvalidationException("제출 요청 상태 또는 미진행 건만 제출 요청 처리가 가능합니다.");
//					}

					if(string.isNotBlank(sttusCd)){
						throw new InvalidationException("이미 요청한 건이 존재합니다.");
					}
				}
		}

		//저장처리
		int updateCnt = 0;
		int insertCnt = 0;

		for(UsptEvlTrget inputInfo : evlPresnatnRequstDto.getUsptEvlTrgetList()) {
				//1.요청상태로 수정
				inputInfo.setPresnatnProcessDivCd(Code.presnatnProcessDiv.제출요청); //요청
				inputInfo.setUpdatedDt(now);
				inputInfo.setUpdaterId(worker.getMemberId());

				updateCnt = usptEvlTrgetDao.updatePresnatnSttus(inputInfo);

				if(updateCnt < 1) {
					throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
				}

				//2.제출이력 등록
				UsptEvlTrgetPresentnHist insertInfo = new UsptEvlTrgetPresentnHist();

				insertInfo.setPresentnHistId(string.getNewId(Code.prefix.평가대상제출이력));
				insertInfo.setEvlTrgetId(inputInfo.getEvlTrgetId());
				insertInfo.setPresnatnProcessDivCd(Code.presnatnProcessDiv.제출요청); //요청
				insertInfo.setResnCn(evlPresnatnRequstDto.getResnCn());
				insertInfo.setProcessDt(now);
				insertInfo.setCreatedDt(now);
				insertInfo.setCreatorId(worker.getMemberId());

				insertCnt = usptEvlTrgetPresentnHistDao.insert(insertInfo);

				if(insertCnt < 1) {
					throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
				}

				//sms 및 email 전송
				CmmtSmsEmailSendDto cmmtSmsEmailSendDto = new CmmtSmsEmailSendDto();
				cmmtSmsEmailSendDto.setMemberId(inputInfo.getMemberId());
				cmmtSmsEmailSendDto.setResnCn(evlPresnatnRequstDto.getResnCn());
				cmmtSmsEmailSendDto.setPblancNm(inputInfo.getPblancNm());
				cmmtSmsEmailSendDto.setSendSmsEmail(evlPresnatnRequstDto.getSendSmsEmail());
				sendMsg(cmmtSmsEmailSendDto);
		}//for
	}


	//보완요청
	public void modifyPresnatnReRequst(UsptEvlTrgetPresentnHist usptEvlTrgetPresentnHist){
		Date now = new Date();

		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if(string.isBlank(usptEvlTrgetPresentnHist.getEvlTrgetId())) {
			throw new InvalidationException("대상자 정보가 없습니다.");
		}

		if(string.isBlank(usptEvlTrgetPresentnHist.getResnCn())) {
			throw new InvalidationException("보완 요청 사유 내용이 없습니다.");
		}

		String evlTrgetId = usptEvlTrgetPresentnHist.getEvlTrgetId();

		// 유효체크
		if(usptEvlTrgetDao.select(evlTrgetId) == null) {
			throw new InvalidationException("대상자 정보가 없습니다.");
		}

		EvlPresnatnListDto inputParam = new EvlPresnatnListDto();
		inputParam.setEvlTrgetId(evlTrgetId);

		List<EvlPresnatnListDto> resultData = usptEvlTrgetDao.selectPresnatnTargetList(inputParam);

		if(resultData.size() < 1){
			throw new InvalidationException("대상 정보가 없습니다.");
		}else{
			String sttusCd = resultData.get(0).getPresnatnProcessDivCd(); //제출상태

			if(!string.equals(sttusCd, Code.presnatnProcessDiv.제출) && !string.equals(sttusCd, Code.presnatnProcessDiv.접수완료취소)){
				throw new InvalidationException("제출 또는 접수완료취소 상태의 건만 처리가 가능합니다.");
			}
		}

		//저장처리
		int updateCnt = 0;
		int insertCnt = 0;

		//1.요청상태로 수정
		UsptEvlTrget inputInfo = new UsptEvlTrget();

		inputInfo.setEvlTrgetId(evlTrgetId);
		inputInfo.setPresnatnProcessDivCd(Code.presnatnProcessDiv.보완요청);
		inputInfo.setUpdatedDt(now);
		inputInfo.setUpdaterId(worker.getMemberId());

		updateCnt = usptEvlTrgetDao.updatePresnatnSttus(inputInfo);

		if(updateCnt < 1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
		}

		//2.제출이력 등록
		usptEvlTrgetPresentnHist.setPresentnHistId(string.getNewId(Code.prefix.평가대상제출이력));
		usptEvlTrgetPresentnHist.setPresnatnProcessDivCd(Code.presnatnProcessDiv.보완요청); //요청
		usptEvlTrgetPresentnHist.setProcessDt(now);
		usptEvlTrgetPresentnHist.setCreatedDt(now);
		usptEvlTrgetPresentnHist.setCreatorId(worker.getMemberId());

		insertCnt = usptEvlTrgetPresentnHistDao.insert(usptEvlTrgetPresentnHist);

		if(insertCnt < 1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
		}
	}

	//접수완료
	public void modifyPresnatnRcept(UsptEvlTrgetPresentnHist usptEvlTrgetPresentnHist){
		Date now = new Date();

		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if(string.isBlank(usptEvlTrgetPresentnHist.getEvlTrgetId())) {
			throw new InvalidationException("대상자 정보가 없습니다.");
		}

		if(string.isBlank(usptEvlTrgetPresentnHist.getResnCn())) {
			usptEvlTrgetPresentnHist.setResnCn("접수완료 처리합니다.");//내용없을 경우에.
		}

		String evlTrgetId = usptEvlTrgetPresentnHist.getEvlTrgetId();

		// 유효체크
		if(usptEvlTrgetDao.select(evlTrgetId) == null) {
			throw new InvalidationException("대상자 정보가 없습니다.");
		}

		EvlPresnatnListDto inputParam = new EvlPresnatnListDto();
		inputParam.setEvlTrgetId(evlTrgetId);

		List<EvlPresnatnListDto> resultData = usptEvlTrgetDao.selectPresnatnTargetList(inputParam);

		if(resultData.size() < 1){
			throw new InvalidationException("대상 정보가 없습니다.");
		}else{
			String sttusCd = resultData.get(0).getPresnatnProcessDivCd(); //제출상태

			if(!string.equals(sttusCd, Code.presnatnProcessDiv.제출) && !string.equals(sttusCd, Code.presnatnProcessDiv.접수완료취소)){
				throw new InvalidationException("제출 또는 접수완료취소 상태의 건만 처리가 가능합니다.");
			}
		}

		//저장처리
		int updateCnt = 0;
		int insertCnt = 0;

		//1.접수완료 상태로 수정
		UsptEvlTrget inputInfo = new UsptEvlTrget();

		inputInfo.setEvlTrgetId(evlTrgetId);
		inputInfo.setPresnatnProcessDivCd(Code.presnatnProcessDiv.접수완료);
		inputInfo.setUpdatedDt(now);
		inputInfo.setUpdaterId(worker.getMemberId());

		updateCnt = usptEvlTrgetDao.updatePresnatnSttus(inputInfo);

		if(updateCnt < 1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
		}

		//2.제출이력 등록
		usptEvlTrgetPresentnHist.setPresentnHistId(string.getNewId(Code.prefix.평가대상제출이력));
		usptEvlTrgetPresentnHist.setPresnatnProcessDivCd(Code.presnatnProcessDiv.접수완료);
		usptEvlTrgetPresentnHist.setProcessDt(now);
		usptEvlTrgetPresentnHist.setCreatedDt(now);
		usptEvlTrgetPresentnHist.setCreatorId(worker.getMemberId());

		insertCnt = usptEvlTrgetPresentnHistDao.insert(usptEvlTrgetPresentnHist);

		if(insertCnt < 1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
		}
	}

	//접수완료 취소
	public void modifyPresnatnCancl(UsptEvlTrgetPresentnHist usptEvlTrgetPresentnHist){
		Date now = new Date();

		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if(string.isBlank(usptEvlTrgetPresentnHist.getEvlTrgetId())) {
			throw new InvalidationException("대상자 정보가 없습니다.");
		}

		if(string.isBlank(usptEvlTrgetPresentnHist.getResnCn())) {
			usptEvlTrgetPresentnHist.setResnCn("접수완료 취소 처리합니다.");//내용없을 경우에.
		}

		String evlTrgetId = usptEvlTrgetPresentnHist.getEvlTrgetId();

		// 유효체크
		if(usptEvlTrgetDao.select(evlTrgetId) == null) {
			throw new InvalidationException("대상자 정보가 없습니다.");
		}

		EvlPresnatnListDto inputParam = new EvlPresnatnListDto();
		inputParam.setEvlTrgetId(evlTrgetId);

		List<EvlPresnatnListDto> resultData = usptEvlTrgetDao.selectPresnatnTargetList(inputParam);

		if(resultData.size() < 1){
			throw new InvalidationException("대상 정보가 없습니다.");
		}else{
			String sttusCd = resultData.get(0).getPresnatnProcessDivCd(); //제출상태

			if(!string.equals(sttusCd, Code.presnatnProcessDiv.접수완료)){
				throw new InvalidationException("접수완료 상태의 건만 처리가 가능합니다.");
			}
		}

		//저장처리
		int updateCnt = 0;
		int insertCnt = 0;

		//1.접수완료 상태로 수정
		UsptEvlTrget inputInfo = new UsptEvlTrget();

		inputInfo.setEvlTrgetId(evlTrgetId);
		inputInfo.setPresnatnProcessDivCd(Code.presnatnProcessDiv.접수완료취소);
		inputInfo.setUpdatedDt(now);
		inputInfo.setUpdaterId(worker.getMemberId());

		updateCnt = usptEvlTrgetDao.updatePresnatnSttus(inputInfo);

		if(updateCnt < 1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
		}

		//2.제출이력 등록
		usptEvlTrgetPresentnHist.setPresentnHistId(string.getNewId(Code.prefix.평가대상제출이력));
		usptEvlTrgetPresentnHist.setPresnatnProcessDivCd(Code.presnatnProcessDiv.접수완료취소);
		usptEvlTrgetPresentnHist.setProcessDt(now);
		usptEvlTrgetPresentnHist.setCreatedDt(now);
		usptEvlTrgetPresentnHist.setCreatorId(worker.getMemberId());

		insertCnt = usptEvlTrgetPresentnHistDao.insert(usptEvlTrgetPresentnHist);

		if(insertCnt < 1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
		}
	}



	//발표자료 처리이력 리스트
	public List<UsptEvlTrgetPresentnHist> getHistList(String evlTrgetId) {

		if(string.isBlank(evlTrgetId)) {
			throw new InvalidationException("대상자 정보가 없습니다.");
		}

		return usptEvlTrgetPresentnHistDao.selectHistList(evlTrgetId);
	}


	public UsptEvlTrgetPresentnHist getSplemntResnDetail(UsptEvlTrgetPresentnHist usptEvlTrgetPresentnHist) {
		if (string.isBlank(usptEvlTrgetPresentnHist.getPresentnHistId()) ) {
			throw new InvalidationException("잘못된 조회 조건 입니다.");
		}

		return usptEvlTrgetPresentnHistDao.select(usptEvlTrgetPresentnHist);
	}

	/**
	 * 발표자료 목록 제출파일- 전체 다운
	 * @param evlPresnatnListDto
	 * @return
	 */
	public void getTargetListAllFileDown(HttpServletResponse response, EvlPresnatnListDto evlPresnatnListDto){
		SecurityUtils.checkWorkerIsInsider();
		List<EvlPresnatnListDto> fileList =  usptEvlTrgetDao.selectPresnatnTargetFileList(evlPresnatnListDto);

		if(fileList.size()>0){
			String[] attachmentIds = new String[fileList.size()];
			for(int idx=0; idx<fileList.size(); idx++) {
				EvlPresnatnListDto fileDto = fileList.get(idx);
				attachmentIds[idx] = fileDto.getAttachmentId();
			}
			attachmentService.downloadFiles(response, config.getDir().getUpload(), attachmentIds, "발표자료관리 목록 첨부파일");
		}else {
			throw new InvalidationException("요청한 첨부파일이 존재하지 않습니다.");
		}
	}

	/**
	 * 발표자료  상세-파일 일괄다운로드
	 * @param attachmentGroupId
	 * @return
	 */
	public void getAllFileDown(HttpServletResponse response, String attachmentGroupId) {
		SecurityUtils.checkWorkerIsInsider();
		if (string.isNotBlank(attachmentGroupId)) {
			attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), attachmentGroupId, "발표자료 첨부파일");
		}
	}

	/*******************************************************************************************************************************/
	/**
	 * sms or email 전송
	 * @param CnvnCnclsParam
	 * @return
	 */
	public String sendMsg(CmmtSmsEmailSendDto param) {
		SecurityUtils.checkWorkerIsInsider();
		StringBuffer sbMsg = new StringBuffer();

		if(param == null) {
			throw new InvalidationException("발송대상이 설정되지 않았습니다.");
		}

		String memId = param.getMemberId();									/*신청자ID*/
		String resnCn = param.getResnCn();									 //사유
		String pblancNm = param.getPblancNm();
		String title = "발표자료 제출";
		String mainTitle= "";
		String cnVal		= "";		//발송내용
		String sendSmsEmail = param.getSendSmsEmail();		// sms-email 선택

		CmmtMember memberInfo = cmmtMemberDao.select(memId);
		String memNm = memberInfo.getMemberNm();		//회원명
		String email  = memberInfo.getEmail();//email
		String mbtlnum= memberInfo.getMobileNo();//휴대폰번호
		EmailResult er = null;
		SmsResult sr = null;

		mainTitle = pblancNm + " 발표자료 제출 요청";
		cnVal = resnCn;			//발송내용

		//****************************이메일
		String emailCn = CoreUtils.file.readFileString("/form/email/email-bsnsPlan.html");//
		EmailSendParam esParam = new EmailSendParam();
		esParam.setContentHtml(true);
		esParam.setEmailCn(emailCn);
		esParam.setTitle(title);
		esParam.setContentHtml(true);

		if(CoreUtils.string.equals(sendSmsEmail, Code.sndngMth.이메일)){		//SNMT02
			if(CoreUtils.string.isNotEmpty(param.getEmail())){
				Map<String, String> templateParam = new HashMap<>();
				templateParam.put("mainTitle", mainTitle);
				templateParam.put("memNm", memNm);	//회원명
				templateParam.put("cnVal", cnVal);	//발송내용
				esParam.addRecipient(email, memNm, templateParam);
				er = emailUtils.send(esParam);
			} else {
				sbMsg.append("[" + memNm +"] 이메일 정보가 없습니다.\n");
				log.debug("[" + memNm +"] 이메일 정보가 없습니다.");
			}
		}

		//********************SMS
		if(CoreUtils.string.equals(sendSmsEmail,Code.sndngMth.SMS)){		//SNMT01
	    	SmsSendParam smsParam = new SmsSendParam();
			if(CoreUtils.string.isNotEmpty(param.getMbtlnum())){
				smsParam.setSmsCn(mainTitle+"\n[##cnVal##]");
				Map<String, String> tp = new HashMap<>();
				tp.put("cnVal", cnVal);
				smsParam.addRecipient(mbtlnum,tp);
				sr = smsUtils.send(smsParam);
			} else {
				sbMsg.append("[" + memNm +"] 핸드폰번호 정보가 없습니다.\n");
				log.debug("[" + memNm +"] 핸드폰번호 정보가 없습니다.");
			}
		}

		//발송체크
		if(er != null) {
			List<EmailReceiverResult> erList = er.getReceiverList();
			Optional<EmailReceiverResult> opt = erList.stream().filter(x->x.getReceiveMailAddr().equals(email)).findFirst();
			if(opt.isPresent()) {
				EmailReceiverResult result = opt.get();
				if(result.getIsSuccessful()) {
				} else {
					sbMsg.append("[" + memNm +"] 이메일 발송이 실패하였습니다.\n");
					log.debug("[" + memNm +"] 이메일 발송이 실패하였습니다.\n");
				}
			}
		}
		if(sr != null) {
			List<SmsRecipientResult> srList = sr.getRecipientList();
			Optional<SmsRecipientResult> opt = srList.stream().filter(x->x.getRecipientNo().equals(mbtlnum)).findFirst();
			if(opt.isPresent()) {
				SmsRecipientResult result = opt.get();
				if(result.getIsSuccessful()) {
				} else {
					sbMsg.append("[" +memNm +"] SMS 발송이 실패하였습니다.\n");
					log.debug("[" + memNm +"] SMS 발송이 실패하였습니다.\n");
				}
			}
		}
		return sbMsg.toString();
	}

	/**
	 * 발표자료 파일 단건 다운
	 * @param attachmentId
	 * @return
	 */
	public void getOneFileDown(HttpServletResponse response, String attachmentId) {
		SecurityUtils.checkWorkerIsInsider();
		if (string.isNotBlank(attachmentId)) {
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		}
	}
}
