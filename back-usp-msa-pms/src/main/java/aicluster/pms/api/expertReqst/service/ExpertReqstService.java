package aicluster.pms.api.expertReqst.service;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
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
import aicluster.pms.api.expertReqst.dto.ExpertClIdDto;
import aicluster.pms.api.expertReqst.dto.ExpertClIdParntsDto;
import aicluster.pms.api.expertReqst.dto.ExpertReqstDto;
import aicluster.pms.api.expertReqst.dto.ExpertReqstListParam;
import aicluster.pms.common.dao.UsptExpertAcdmcrDao;
import aicluster.pms.common.dao.UsptExpertCareerDao;
import aicluster.pms.common.dao.UsptExpertClChargerDao;
import aicluster.pms.common.dao.UsptExpertClDao;
import aicluster.pms.common.dao.UsptExpertClMapngDao;
import aicluster.pms.common.dao.UsptExpertCrqfcDao;
import aicluster.pms.common.dao.UsptExpertDao;
import aicluster.pms.common.dao.UsptExpertReqstHistDao;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.entity.UsptExpertAcdmcr;
import aicluster.pms.common.entity.UsptExpertCareer;
import aicluster.pms.common.entity.UsptExpertCl;
import aicluster.pms.common.entity.UsptExpertClCharger;
import aicluster.pms.common.entity.UsptExpertClMapng;
import aicluster.pms.common.entity.UsptExpertCrqfc;
import aicluster.pms.common.entity.UsptExpertReqstHist;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExpertReqstService {

	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private EnvConfig config;
	@Autowired
	private UsptExpertDao usptExpertDao;
	@Autowired
	private UsptExpertReqstHistDao usptExpertReqstHistDao;
	@Autowired
	private UsptExpertClMapngDao usptExpertClMapngDao;
	@Autowired
	private UsptExpertCareerDao usptExpertCareerDao;
	@Autowired
	private UsptExpertAcdmcrDao usptExpertAcdmcrDao;
	@Autowired
	private UsptExpertCrqfcDao usptExpertCrqfcDao;
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private UsptExpertClDao usptExpertClDao;
	@Autowired
	private UsptExpertClChargerDao usptExpertClChargerDao;		/*전문가분류담당자*/
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

	/**
	 * 전문가 신청 목록 조회
	 * @param pblancId
	 * @return
	 */
	public CorePagination<UsptExpert> getExpertReqsList(ExpertReqstListParam expertReqstListParam, Long page) {
		/**회원정보**/
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(expertReqstListParam.getItemsPerPage() == null) {
			expertReqstListParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = expertReqstListParam.getItemsPerPage();
		//신청건 조회 시 관리화면에서 등록한(엑셀) 대상 미조회
		String expertReqstYn = "Y";
		expertReqstListParam.setExpertReqstYn(expertReqstYn);
		expertReqstListParam.setMberId(worker.getMemberId());
		// 전체 건수 확인
		long totalItems = usptExpertDao.selectExpertListCnt(expertReqstListParam);

		// 조회할 페이지 구간 정보 확인
		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);

		expertReqstListParam.setBeginRowNum(info.getBeginRowNum());
		expertReqstListParam.setItemsPerPage(itemsPerPage);

		// 페이지 목록 조회
		List<UsptExpert> list = usptExpertDao.selectExpertList(expertReqstListParam);

		CorePagination<UsptExpert> pagination = new CorePagination<>(info, list);

		// 목록 조회
		return pagination;
	}


	/**
	 * 전문가신청 목록 엑셀다운로드
	 * @param pblancId
	 * @return
	 */
	public List<UsptExpert> getExpertReqstListExcel(ExpertReqstListParam expertReqstListParam) {

		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		//신청건 조회 시 관리화면에서 등록한(엑셀) 대상 미조회
		String expertReqstYn = "Y";
		expertReqstListParam.setExpertReqstYn(expertReqstYn);
		expertReqstListParam.setMberId(worker.getMemberId());
		// 전체 건수 확인
		long totalItems = usptExpertDao.selectExpertListCnt(expertReqstListParam);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		expertReqstListParam.setBeginRowNum(info.getBeginRowNum());
		expertReqstListParam.setItemsPerPage(totalItems);
		return usptExpertDao.selectExpertList(expertReqstListParam);
	}

	/**
	 * 전문가 신청 상세정보
	 * @param expertId
	 * @return
	 */
	@GetMapping("/{expertId}")
	public ExpertReqstDto getExpertReqstInfo(HttpServletRequest request, String expertId) {

		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/**조회 리턴**/
		ExpertReqstDto resultExpertReqstDto = new ExpertReqstDto();

		/**전문가 기본정보**/
		UsptExpert usptExpert = new UsptExpert();
		usptExpert = usptExpertDao.selectOneExpert(expertId);

		if(usptExpert == null) {
			throw new InvalidationException(String.format(Code.validateMessage.조회결과없음, "전문가 신청 내역"));
		}
		usptExpert.setEncBrthdy(usptExpert.getBrthdy());
		usptExpert.setEncEmail(usptExpert.getEmail());
		usptExpert.setEncMbtlnum(usptExpert.getMbtlnum());
		resultExpertReqstDto.setUsptExpert(usptExpert);

		/** 첨부파일 목록 **/
		if (string.isNotBlank(usptExpert.getAttachmentGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptExpert.getAttachmentGroupId());
			resultExpertReqstDto.setAttachFileList(list);
		}

		/** 전문분야 */
		resultExpertReqstDto.setUsptExpertClMapng(usptExpertClMapngDao.selectExpertReqsList(expertId));

		/** 전문가 경력정보 */
		UsptExpertCareer usptExpertCareer = new UsptExpertCareer();
		usptExpertCareer.setExpertId(expertId);
		resultExpertReqstDto.setUsptExpertCareer(usptExpertCareerDao.selectList(usptExpertCareer));

		/** 전문가 학력정보 */
		UsptExpertAcdmcr usptExpertAcdmcr = new UsptExpertAcdmcr();
		usptExpertAcdmcr.setExpertId(expertId);
		resultExpertReqstDto.setUsptExpertAcdmcr(usptExpertAcdmcrDao.selectList(usptExpertAcdmcr));

		/** 전문가 자격증정보 */
		UsptExpertCrqfc usptExpertCrqfc = new UsptExpertCrqfc();
		usptExpertCrqfc.setExpertId(expertId);
		resultExpertReqstDto.setUsptExpertCrqfc(usptExpertCrqfcDao.selectList(usptExpertCrqfc));

		/* 개인정보 조회 이력 생성  --------------------------------------------------------------------------------*/
		// 신청자 정보
		LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("전문가정보 관리 상세조회 - 신청자정보")
				.trgterId(usptExpert.getMemberId())
				.email(usptExpert.getEmail())
				.birthday(usptExpert.getBrthdy())
				.mobileNo(usptExpert.getMbtlnum())
				.build();
		indvdlInfSrchUtils.insert(logParam);

		return resultExpertReqstDto;
	}

	/**
	 * 전문가 신청자 승인
	 * @param frontExpertReqstParam
	 * * @param fileList
	 * @return
	 */
	public ExpertReqstDto modifyExpertReqstConfm(String expertId,  ExpertReqstDto expertReqstDto,  List<MultipartFile> fileList) {

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String strMemberId = worker.getMemberId();

		/*신청상태 체크*/
		UsptExpert usptExpertCheck  = new UsptExpert();
		usptExpertCheck = usptExpertDao.selectOneExpert(expertId);

		if (!string.equals(usptExpertCheck.getExpertReqstProcessSttusCd(), Code.expertReqstProcessSttus.신청)) {
			throw new InvalidationException("승인은 신청상태인 경우만 할 수 있습니다.");
		}

		/********************************* 신청자 정보 승인 및 수정 start**/

		/** 신청자정보(전문가) 변경 */
		UsptExpert usptExpert = expertReqstDto.getUsptExpert();

		if(CoreUtils.string.isBlank(usptExpert.getLastUnivNm())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "최종대학명"));
		}
		if(CoreUtils.string.isBlank(usptExpert.getUnivDeptNm())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "대학학부명"));
		}

		//첨부파일 등록(첨부파일그룹ID)
		if(fileList != null && fileList.size() > 0) {
			if(CoreUtils.string.isNotBlank(usptExpert.getAttachmentGroupId())) {
				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), usptExpert.getAttachmentGroupId(), fileList, null, 0);
			} else {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
				usptExpert.setAttachmentGroupId(fileGroupInfo.getAttachmentGroupId());
			}
		}

		String encBrthdy = CoreUtils.aes256.encrypt( usptExpert.getEncBrthdy(), expertId);	/*생년월일*/
		String encMbtlnum = CoreUtils.aes256.encrypt(usptExpert.getEncMbtlnum(), expertId);	/*휴대폰번호*/
		String encEmail = CoreUtils.aes256.encrypt(usptExpert.getEncEmail(), expertId);	/*이메일*/

		usptExpert.setEncBrthdy(encBrthdy);
		usptExpert.setEncMbtlnum(encMbtlnum);
		usptExpert.setEncEmail(encEmail);
		usptExpert.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.승인);
		usptExpert.setUpdatedDt(now);
		usptExpert.setUpdaterId(strMemberId);
		usptExpertDao.update(usptExpert);

		/**전문가신청처리이력**/
		UsptExpertReqstHist usptExpertReqstHist = new UsptExpertReqstHist();
		usptExpertReqstHist.setExpertReqstHistId(CoreUtils.string.getNewId(Code.prefix.전문가신청처리이력));/** 전문가신청처리이력ID 생성 */
		usptExpertReqstHist.setExpertId(expertId);
		usptExpertReqstHist.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.승인);
		usptExpertReqstHist.setResnCn("접수완료 처리");
		usptExpertReqstHist.setCreatedDt(now);
		usptExpertReqstHist.setCreatorId(strMemberId);
		usptExpertReqstHistDao.insert(usptExpertReqstHist);	//insert

		/** 전문분야 */
		List<UsptExpertClMapng> usptExpertClMapngList =  expertReqstDto.getUsptExpertClMapng();
		if(usptExpertClMapngList.size()>0) {
			//삭제
			usptExpertClMapngDao.deleteExpert(expertId);
			//등록
			for( UsptExpertClMapng input : usptExpertClMapngList) {
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				usptExpertClMapngDao.insertExpertClMapng(input);
			}
		}

		/** 전문가 경력정보 */
		List<UsptExpertCareer> usptExpertCareerList =  expertReqstDto.getUsptExpertCareer();
		if(usptExpertCareerList.size()>0) {
			//등록
			for( UsptExpertCareer input : usptExpertCareerList) {
				String  workBgnde = input.getWorkBgnde();
				String  workEndde = input.getWorkEndde();
				if(workBgnde.compareTo(workEndde) ==1) {
					throw new InvalidationException(String.format("경력정보(근무기간) 시작일자는 종료일자보다 클수 없습니다."));
				}
			}
			//삭제
			usptExpertCareerDao.deleteExpert(expertId);
			//등록
			for( UsptExpertCareer input : usptExpertCareerList) {
				input.setExpertCareerId(CoreUtils.string.getNewId(Code.prefix.전문가경력));	/** 전문가경력ID 생성 */
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptExpertCareerDao.insert(input);
			}
		}

		/** 전문가 학력정보 */
		List<UsptExpertAcdmcr> usptExpertAcdmcrList=  expertReqstDto.getUsptExpertAcdmcr();
		if(usptExpertAcdmcrList.size()>0) {
			for( UsptExpertAcdmcr input : usptExpertAcdmcrList) {
				String  acdmcrBgnde= 	input.getAcdmcrBgnde();
				String  acdmcrEndde = input.getAcdmcrEndde();
				if(acdmcrBgnde.compareTo(acdmcrEndde)==1) {
					throw new InvalidationException(String.format("학력정보(기간) 시작일자는 종료일자보다 클수 없습니다."));
				}
			}
			//삭제
			usptExpertAcdmcrDao.deleteExpert(expertId);
			//등록
			for( UsptExpertAcdmcr input : usptExpertAcdmcrList) {
				input.setExpertAcdmcrId(CoreUtils.string.getNewId(Code.prefix.전문가학력));	/** 전문가학력ID 생성 */
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptExpertAcdmcrDao.insert(input);
			}
		}

		/** 전문가 자격증정보 */
		List<UsptExpertCrqfc> usptExpertCrqfcList=  expertReqstDto.getUsptExpertCrqfc();
		if(usptExpertCrqfcList.size()>0) {
			//삭제
			usptExpertCrqfcDao.deleteExpert(expertId);
			//등록
			for( UsptExpertCrqfc input : usptExpertCrqfcList) {
				input.setExpertCrqfcId(CoreUtils.string.getNewId(Code.prefix.전문가자격증));	/** 전문가자격증ID 생성 */
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptExpertCrqfcDao.insert(input);
			}
		}
		/*************************************** 신청자 정보 등록 end**/
		return expertReqstDto;
	}

	/**
	 * 전문가신청 반려
	 * @param pblancId
	 * @return
	 */
	public ExpertReqstListParam modifyExpertReqstReturn(ExpertReqstListParam expertReqstListParam) {

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String strMemberId = worker.getMemberId();

		/*신청상태 체크*/
		UsptExpert usptExpert  = new UsptExpert();
		usptExpert = usptExpertDao.selectOneExpert(expertReqstListParam.getExpertId());

		if (!string.equals(usptExpert.getExpertReqstProcessSttusCd(), Code.expertReqstProcessSttus.신청)) {
			throw new InvalidationException("반려는 신청상태인 경우만 할 수 있습니다.");
		}

		/**전문가 정보 상태코드 변경**/
		expertReqstListParam.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.반려);
		expertReqstListParam.setUpdatedDt(now);
		expertReqstListParam.setUpdaterId(strMemberId);

		usptExpertDao.updateExpertReturn(expertReqstListParam);

		/**전문가신청처리이력 등록**/
		UsptExpertReqstHist usptExpertReqstHist = new UsptExpertReqstHist();

		usptExpertReqstHist.setExpertReqstHistId(CoreUtils.string.getNewId(Code.prefix.전문가신청처리이력));/** 전문가신청처리이력ID 생성 */
		usptExpertReqstHist.setExpertId(expertReqstListParam.getExpertId());
		usptExpertReqstHist.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.반려);
		usptExpertReqstHist.setResnCn(expertReqstListParam.getResnCn());
		usptExpertReqstHist.setCreatedDt(now);
		usptExpertReqstHist.setCreatorId(strMemberId);

		int updateCnt = usptExpertReqstHistDao.insert(usptExpertReqstHist);

		//sms or email 보내기
		if(updateCnt > 0) {
			sendMsg(expertReqstListParam);
		}

		return expertReqstListParam;
	}

	/**
	 * 전문가 신청 상세_처리이력
	 * @param expertReqstListParam
	 * @return
	 */
	@GetMapping("/{expertId}/hist")
	public  List<UsptExpertReqstHist> getExpertReqstHistList(String expertId) {
		SecurityUtils.checkWorkerIsInsider();
		return usptExpertReqstHistDao.selectExpertReqstHistList(expertId);
	}

	/**
	 * 전문가 신청 sms or email 전송
	 * @param expertReqstListParam
	 * @return
	 */
	public String sendMsg(ExpertReqstListParam param) {
		SecurityUtils.checkWorkerIsInsider();
		StringBuffer sbMsg = new StringBuffer();
		if(param == null) {
			throw new InvalidationException("발송대상이 설정되지 않았습니다.");
		}

		String title = "전문가 신청 반려사유";
		String mainTitle ="전문가 신청 반려사유";
		String memNm = param.getExpertNm();		//회원명
		String email  = param.getEncEmail();
		String mbtlnum= param.getEncMbtlnum();
		EmailResult er = null;
		SmsResult sr = null;

		//****************************이메일
		String emailCn = CoreUtils.file.readFileString("/form/email/email-bsnsPlan.html");//
		EmailSendParam esParam = new EmailSendParam();
		esParam.setContentHtml(true);
		esParam.setEmailCn(emailCn);
		esParam.setTitle(title);
		esParam.setContentHtml(true);

		if(CoreUtils.string.isNotEmpty(param.getEncEmail())){
			Map<String, String> templateParam = new HashMap<>();
			templateParam.put("mainTitle", mainTitle);
			templateParam.put("memNm", memNm);	//회원명
			templateParam.put("cnVal", param.getResnCn());	//발송내용
			esParam.addRecipient(email, memNm, templateParam);
			er = emailUtils.send(esParam);
		} else {
			sbMsg.append("[" + param.getExpertNm() +"] 이메일 정보가 없습니다.\n");
			log.debug("[" + memNm +"] 핸드폰번호 정보가 없습니다.");
		}


		//********************SMS
    	SmsSendParam smsParam = new SmsSendParam();
		if(CoreUtils.string.isNotEmpty(param.getEncMbtlnum())){
			smsParam.setSmsCn("전문가 신청 반려사유\n[##cnVal##]");
			String cnVal = param.getResnCn();//발송내용
			Map<String, String> tp = new HashMap<>();
			tp.put("cnVal", cnVal);
			smsParam.addRecipient(mbtlnum,tp);
			sr = smsUtils.send(smsParam);
		} else {
			sbMsg.append("[" + memNm +"] 핸드폰번호 정보가 없습니다.\n");
			log.debug("[" + memNm +"] 핸드폰번호 정보가 없습니다.");
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
					sbMsg.append("[" +memNm+"] SMS 발송이 실패하였습니다.\n");
					log.debug("[" + memNm +"] SMS 발송이 실패하였습니다.\n");
				}
			}
		}
		return sbMsg.toString();
	}

	/**
	 * 전문가 분류조회_부모전문가분류 조회
	 * @param pblancId
	 * @return
	 */
	public List<ExpertClIdParntsDto> selectParntsExpertClIdList() {
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String memberId = worker.getMemberId();

		// 로그인 사용자의 전문가 담당 분류ID 조회(1depth)
		List<UsptExpertClCharger> selectMyChargerList = usptExpertClChargerDao.selectListMyExpertCl(memberId);
		List<ExpertClIdParntsDto> expertClIdParntsDtoList = new ArrayList <>();
		ExpertClIdParntsDto expertClIdParntsDto = null;
		for(UsptExpertClCharger info : selectMyChargerList) {
			List<UsptExpertCl> usptExpertClList= usptExpertClDao.selectExpertClIdInfoList(info.getExpertClId());
			for(UsptExpertCl returnClId : usptExpertClList) {
				expertClIdParntsDto = new ExpertClIdParntsDto();
				expertClIdParntsDto.setExpertClId(returnClId.getExpertClId());
				expertClIdParntsDto.setExpertClNm(returnClId.getExpertClNm());
				expertClIdParntsDto.setParntsExpertClId(returnClId.getParntsExpertClId());
				expertClIdParntsDtoList.add(expertClIdParntsDto);
			}
		}
		return expertClIdParntsDtoList;
	}

	/**
	 * 전문가 분류조회_전문가분류보 조회
	 * @param pblancId
	 * @return
	 */
	public List<ExpertClIdDto> selectExpertClIdList(String expertClId) {

		List<ExpertClIdDto> expertClIdDtoList = new ArrayList <>();
		//부모전문가분류 조회
		List<UsptExpertCl> usptExpertClList= usptExpertClDao.selectExpertClIdList(expertClId);
		for(UsptExpertCl info : usptExpertClList) {
			ExpertClIdDto expertClIdDto = new ExpertClIdDto();
			expertClIdDto.setParntsExpertClId(info.getParntsExpertClId());
			expertClIdDto.setExpertClId(info.getExpertClId());
			expertClIdDto.setExpertClNm(info.getExpertClNm());
			expertClIdDtoList.add(expertClIdDto);
		}
		return expertClIdDtoList;
	}

	/**
	 * 일괄 다운
	 * @param attachmentGroupId
	 * @return
	 */
	public void getAllFileDown(HttpServletResponse response, String attachmentGroupId) {
		SecurityUtils.checkWorkerIsInsider();
		if (string.isNotBlank(attachmentGroupId)) {
			attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), attachmentGroupId, "전문가 신청 제출 파일");
		}
	}

	/**
	 * 파일 단건 다운
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