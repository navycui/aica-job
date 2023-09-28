package aicluster.pms.api.cnvnchange.service;

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
import aicluster.pms.api.cnvnchange.dto.CnvnChangeParam;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnApplcntHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnSclpstHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnTaskInfoHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskPartcptsHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskPrtcmpnyInfoHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskReqstWctHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskRspnberHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskTaxitmWctHistDto;
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.UsptBsnsCnvnHistDao;
import aicluster.pms.common.dao.UsptBsnsPlanApplcntDao;
import aicluster.pms.common.dao.UsptBsnsPlanDocDao;
import aicluster.pms.common.dao.UsptCnvnApplcntHistDao;
import aicluster.pms.common.dao.UsptCnvnChangeReqDao;
import aicluster.pms.common.dao.UsptCnvnSclpstHistDao;
import aicluster.pms.common.dao.UsptCnvnTaskInfoHistDao;
import aicluster.pms.common.dao.UsptTaskPartcptsDao;
import aicluster.pms.common.dao.UsptTaskPartcptsHistDao;
import aicluster.pms.common.dao.UsptTaskPrtcmpnyDao;
import aicluster.pms.common.dao.UsptTaskPrtcmpnyHistDao;
import aicluster.pms.common.dao.UsptTaskPrtcmpnyInfoHistDao;
import aicluster.pms.common.dao.UsptTaskReqstWctDao;
import aicluster.pms.common.dao.UsptTaskReqstWctHistDao;
import aicluster.pms.common.dao.UsptTaskRspnberDao;
import aicluster.pms.common.dao.UsptTaskRspnberHistDao;
import aicluster.pms.common.dao.UsptTaskTaxitmWctDao;
import aicluster.pms.common.dao.UsptTaskTaxitmWctHistDao;
import aicluster.pms.common.dto.CnvnChangeDto;
import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptBsnsCnvnHist;
import aicluster.pms.common.entity.UsptBsnsPlanApplcnt;
import aicluster.pms.common.entity.UsptBsnsPlanDoc;
import aicluster.pms.common.entity.UsptCnvnApplcntHist;
import aicluster.pms.common.entity.UsptCnvnChangeReq;
import aicluster.pms.common.entity.UsptCnvnSclpstHist;
import aicluster.pms.common.entity.UsptCnvnTaskInfoHist;
import aicluster.pms.common.entity.UsptTaskPartcpts;
import aicluster.pms.common.entity.UsptTaskPartcptsHist;
import aicluster.pms.common.entity.UsptTaskPrtcmpny;
import aicluster.pms.common.entity.UsptTaskPrtcmpnyHist;
import aicluster.pms.common.entity.UsptTaskPrtcmpnyInfoHist;
import aicluster.pms.common.entity.UsptTaskReqstWct;
import aicluster.pms.common.entity.UsptTaskReqstWctHist;
import aicluster.pms.common.entity.UsptTaskRspnber;
import aicluster.pms.common.entity.UsptTaskRspnberHist;
import aicluster.pms.common.entity.UsptTaskTaxitmWct;
import aicluster.pms.common.entity.UsptTaskTaxitmWctHist;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CnvnChangeService {
	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;		/*파일*/
	@Autowired
	private CmmtMemberDao cmmtMemberDao;	/*회원*/
	@Autowired
	private UsptBsnsPlanApplcntDao usptBsnsPlanApplcntDao;		/*사업계획서신청자*/
	@Autowired
	private UsptCnvnChangeReqDao usptCnvnChangeReqDao;		/*협약변경요청*/
	@Autowired
	private UsptBsnsCnvnHistDao usptBsnsCnvnHistDao;		/*사업협약변경이력*/
	@Autowired
	private UsptCnvnSclpstHistDao usptCnvnSclpstHistDao;		/*협약수행기관신분변경이력*/
	@Autowired
	private UsptCnvnTaskInfoHistDao usptCnvnTaskInfoHistDao;		/*협약과제정보변경이력*/
	@Autowired
	private UsptTaskPrtcmpnyInfoHistDao usptTaskPrtcmpnyInfoHistDao;		/*과제참여기업정보변경이력*/
	@Autowired
	private UsptTaskPrtcmpnyDao usptTaskPrtcmpnyDao;		/*과제참여기업*/
	@Autowired
	private UsptTaskPrtcmpnyHistDao usptTaskPrtcmpnyHistDao;		/*과제참여기업변경이력*/
	@Autowired
	private UsptTaskPartcptsDao usptTaskPartcptsDao;		/*과제참여자*/
	@Autowired
	private UsptTaskPartcptsHistDao usptTaskPartcptsHistDao;		/*과제참여자변경이력*/
	@Autowired
	private UsptTaskRspnberDao usptTaskRspnberDao;		/*과제책임자*/
	@Autowired
	private UsptTaskRspnberHistDao usptTaskRspnberHistDao;		/*과제책임자변경이력*/
	@Autowired
	private UsptCnvnApplcntHistDao usptCnvnApplcntHistDao;		/*협약신청자정보변경이력*/
	@Autowired
	private UsptTaskReqstWctDao usptTaskReqstWctDao;		/*과제신청사업비*/
	@Autowired
	private UsptTaskReqstWctHistDao usptTaskReqstWctHistDao;		/*과제신청사업비변경이력*/
	@Autowired
	private UsptTaskTaxitmWctDao usptTaskTaxitmWctDao;		/*과제세목별사업비*/
	@Autowired
	private UsptTaskTaxitmWctHistDao usptTaskTaxitmWctHistDao;		/*과제세목별사업비변경이력*/
	@Autowired
	private UsptBsnsPlanDocDao usptBsnsPlanDocDao;		/*사업계획서*/
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
	/**
	 * 협약변경신청 목록 조회
	 * @param memberId
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	public CorePagination<CnvnChangeDto> selectCnvnChangeReqListAdmin(CnvnChangeParam cnvnChangeParam, Long page){
		SecurityUtils.checkWorkerIsInsider();

		if(page == null) {
			page = 1L;
		}
		if(cnvnChangeParam.getItemsPerPage() == null) {
			cnvnChangeParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = cnvnChangeParam.getItemsPerPage();

		//전체건수 조회
		long totalItems = usptCnvnChangeReqDao.selectCnvnChangeReqListAdminCnt(cnvnChangeParam);

		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		cnvnChangeParam.setBeginRowNum(info.getBeginRowNum());
		cnvnChangeParam.setItemsPerPage(itemsPerPage);
		List<CnvnChangeDto> list = usptCnvnChangeReqDao.selectCnvnChangeReqListAdmin(cnvnChangeParam);

		CorePagination<CnvnChangeDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 협약변경신청 목록 엑셀 다운로드
	 * @param
	 */
	public List<CnvnChangeDto> getListExcel(CnvnChangeParam cnvnChangeParam){
		SecurityUtils.checkWorkerIsInsider();

		//전체건수 조회
		long totalItems = usptCnvnChangeReqDao.selectCnvnChangeReqListAdminCnt(cnvnChangeParam);

		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		cnvnChangeParam.setBeginRowNum(info.getBeginRowNum());
		cnvnChangeParam.setItemsPerPage(totalItems);
		return  usptCnvnChangeReqDao.selectCnvnChangeReqListAdmin(cnvnChangeParam);
	}

	/****************************************수행기관신분변경 start****************************************************/
	/**
	 * 협약변경관리 수행기관신분변경 상세조회
	 * @param   cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptCnvnSclpstHistDto selectChangeCnvnSclpstInfo(HttpServletRequest request, CnvnChangeParam cnvnChangeParam){
		BnMember worker =  SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeIemDivCd =	 Code.changeIemDiv.수행기관신분;		/**협약변경항목구분 - CHIE01*/

		UsptCnvnSclpstHistDto usptCnvnSclpstHistDto = new UsptCnvnSclpstHistDto();

		//기본정보 조회
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId,  changeIemDivCd);

		//사업협약변경이력
		UsptBsnsCnvnHist  usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

		 if(usptBsnsCnvnHist !=null) {
			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptBsnsCnvnHist.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnHist.getAttachmentGroupId());
				usptCnvnSclpstHistDto.setAttachFileList(list);
				 //등록에 필요한 첨부파일그룹ID 셋팅
				 usptCnvnChangeReq.setAttachmentGroupId(usptBsnsCnvnHist.getAttachmentGroupId());
			}
		 }
		//셋팅
		usptCnvnSclpstHistDto.setUsptCnvnChangeReq(usptCnvnChangeReq);

		/***************************수행기관 변경 전 내용*/
		UsptBsnsPlanApplcnt usptBsnsPlanApplcnt = new UsptBsnsPlanApplcnt();	//조회
		UsptBsnsPlanApplcnt usptBsnsPlanApplcntBefore = new UsptBsnsPlanApplcnt();	//리턴
		usptBsnsPlanApplcnt.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());

		usptBsnsPlanApplcntBefore = usptBsnsPlanApplcntDao.selectOne(usptBsnsPlanApplcnt);
		usptBsnsPlanApplcntBefore.setEncBrthdy(usptBsnsPlanApplcntBefore.getBrthdy());
		usptBsnsPlanApplcntBefore.setEncEmail(usptBsnsPlanApplcntBefore.getEmail());
		usptBsnsPlanApplcntBefore.setEncMbtlnum(usptBsnsPlanApplcntBefore.getMbtlnum());
		usptCnvnSclpstHistDto.setUsptBsnsPlanApplcntBefore(usptBsnsPlanApplcntBefore);	//사업계획신청자


		/***************************수행기관 변경 후 내용*/
		//협약수행기관신분변경 최근이력조회
		UsptCnvnSclpstHist  usptCnvnSclpstHistAfter = usptCnvnSclpstHistDao.selectMaxHist(cnvnChangeReqId);
		//셋팅
		usptCnvnSclpstHistDto.setUsptCnvnSclpstHistAfter(usptCnvnSclpstHistAfter);

		/* 개인정보 조회 이력 생성  --------------------------------------------------------------------------------*/
		// 관리정보 이력 생성
		LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("협약변경관리 수행기관신분변경 - 관리정보")
				.trgterId(usptCnvnChangeReq.getMemberId())
				.email(usptCnvnChangeReq.getEmail())
				.birthday("")
				.mobileNo(usptCnvnChangeReq.getMbtlnum())
				.build();
		indvdlInfSrchUtils.insert(logParam);
		// 수행기관신분변경 이력 생성
		LogIndvdlInfSrch changeCnvnSclpstParamBefore = LogIndvdlInfSrch.builder()
						.memberId(worker.getMemberId())
						.memberIp(CoreUtils.webutils.getRemoteIp(request))
						.workTypeNm("조회")
						.workCn("협약변경관리 수행기관신분변경 - 변경 전")
						.trgterId(usptCnvnChangeReq.getMemberId())
						.email(usptBsnsPlanApplcntBefore.getEmail())
						.birthday(usptBsnsPlanApplcntBefore.getBrthdy())
						.mobileNo(usptBsnsPlanApplcntBefore.getMbtlnum())
						.build();
				indvdlInfSrchUtils.insert(changeCnvnSclpstParamBefore);
		return usptCnvnSclpstHistDto;
	}
	/**
	 * 협약변경관리 수행기관신분변경 승인
	 * cnvnChangeReqId
	 */
	public void modifyChangeCnvnSclpstApproval(UsptCnvnSclpstHistDto usptCnvnSclpstHistDto) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/** 협약변경요청ID */
		String cnvnChangeReqId=usptCnvnSclpstHistDto.getUsptCnvnChangeReq().getCnvnChangeReqId();
		/** 신청자ID */
		String applcntId = usptCnvnSclpstHistDto.getUsptCnvnChangeReq().getApplcntId();
		/**협약변경항목구분 - CHIE01*/
		String changeIemDivCd = Code.changeIemDiv.수행기관신분;

		/** 협약변경요청, 이력테이블 변경*/
		modifyCnvnChangeReqApproval(cnvnChangeReqId,  changeIemDivCd);

		//협약수행기관신분 변경 전 정보 셋팅
		UsptBsnsPlanApplcnt usptBsnsPlanApplcntBefore = usptCnvnSclpstHistDto.getUsptBsnsPlanApplcntBefore();
		//협약수행기관신분 변경 후 정보 셋팅
		UsptCnvnSclpstHist usptCnvnSclpstHistAfter = usptCnvnSclpstHistDto.getUsptCnvnSclpstHistAfter();

		/** 협약수행기관신분변경이력 변경전 정보 등록*/
		UsptCnvnSclpstHist usptCnvnSclpstHistBefore = new  UsptCnvnSclpstHist();

		String newCnvnSclpstHistId =CoreUtils.string.getNewId(Code.prefix.협약수행기관신분변경이력);
		String confmCnvnSclpstHistId = usptCnvnSclpstHistAfter.getCnvnSclpstHistId();

		usptCnvnSclpstHistBefore.setCnvnSclpstHistId(newCnvnSclpstHistId);						/*협약수행기관신분변경이력ID*/
		usptCnvnSclpstHistBefore.setCnvnChangeReqId(cnvnChangeReqId);							/*협약변경요청ID*/
		usptCnvnSclpstHistBefore.setConfmCnvnSclpstHistId(confmCnvnSclpstHistId);				/*승인협약수행기관신분변경이력ID*/
		usptCnvnSclpstHistBefore.setNm(usptBsnsPlanApplcntBefore.getApplcntNm());	  		/*이름*/
		usptCnvnSclpstHistBefore.setIndvdlBsnmDivCd(usptBsnsPlanApplcntBefore.getIndvdlBsnmDivCd());	/*개인사업자구분코드*/
		usptCnvnSclpstHistBefore.setGenderCd(usptBsnsPlanApplcntBefore.getGenderCd());
		if(string.isNotBlank(usptBsnsPlanApplcntBefore.getEncBrthdy())){
			usptCnvnSclpstHistBefore.setEncBrthdy(CoreUtils.aes256.encrypt(usptBsnsPlanApplcntBefore.getEncBrthdy(), newCnvnSclpstHistId));			/* 암호화된 생년월일 */
		}
		if(string.isNotBlank(usptBsnsPlanApplcntBefore.getEncEmail())){
			usptCnvnSclpstHistBefore.setEncEmail(CoreUtils.aes256.encrypt(usptBsnsPlanApplcntBefore.getEncEmail(), newCnvnSclpstHistId));			 	/*암호화된 이메일*/
		}
		if(string.isNotBlank(usptBsnsPlanApplcntBefore.getEncMbtlnum())){
			usptCnvnSclpstHistBefore.setEncMbtlnum(CoreUtils.aes256.encrypt(usptBsnsPlanApplcntBefore.getEncMbtlnum(), newCnvnSclpstHistId));		/*암호화된 휴대폰번호*/
		}
		usptCnvnSclpstHistBefore.setCreatedDt(now);
		usptCnvnSclpstHistBefore.setCreatorId(strMemberId);
		usptCnvnSclpstHistDao.insert(usptCnvnSclpstHistBefore);

		/** 협약수행기관신분변경이력 변경 후 정보 변경*/
		/*사업계획신청자 변경*/
		//기본정보 조회-사업계획서ID
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);
		/*사업계획서ID 셋팅*/
		String bsnsPlanDocId = usptCnvnChangeReq.getBsnsPlanDocId();

		UsptBsnsPlanApplcnt usptBsnsPlanApplcnt = new UsptBsnsPlanApplcnt();
		usptBsnsPlanApplcnt.setBsnsPlanDocId(bsnsPlanDocId);
		usptBsnsPlanApplcnt.setApplcntNm(usptCnvnSclpstHistAfter.getBsnmNm());
		usptBsnsPlanApplcnt.setIndvdlBsnmDivCd(Code.indvdlBsnmDiv.사업자);
		usptBsnsPlanApplcnt.setChargerNm(usptCnvnSclpstHistAfter.getChargerNm());
		usptBsnsPlanApplcnt.setCeoNm(usptCnvnSclpstHistAfter.getCeoNm());
		usptBsnsPlanApplcnt.setBizrno(usptCnvnSclpstHistAfter.getBizrno());
		usptBsnsPlanApplcnt.setUpdatedDt(now);
		usptBsnsPlanApplcnt.setUpdaterId(strMemberId);
		int updateCnt = usptBsnsPlanApplcntDao.update(usptBsnsPlanApplcnt);

		/** email, sms */
		if(updateCnt>0) {
			CnvnChangeParam param = new  CnvnChangeParam();
			param.setCnvnChangeSttusCd(Code.cnvnChangeSttus.승인);		/* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
			param.setChangeIemDivCd(changeIemDivCd);							/** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
			param.setApplcntId(applcntId);		/** 신청자ID */
			sendMsg(param);
		}
	}
	/****************************************수행기관신분변경****************************************************/

	/****************************************과제정보****************************************************/
	/**
	 * 협약변경관리 과제정보 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptCnvnTaskInfoHistDto selectChangeCnvnTaskInfo(CnvnChangeParam cnvnChangeParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.과제정보;					/**협약변경항목구분 - CHIE02*/

		 UsptCnvnTaskInfoHistDto usptCnvnTaskInfoHistDto = new UsptCnvnTaskInfoHistDto();

		//기본정보 조회
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);

		//사업협약변경이력 최신정보 조회
		UsptBsnsCnvnHist  usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

		 if(usptBsnsCnvnHist !=null) {
			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptBsnsCnvnHist.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnHist.getAttachmentGroupId());
				usptCnvnTaskInfoHistDto.setAttachFileList(list);
				 //등록에 필요한 첨부파일그룹ID 셋팅
				 usptCnvnChangeReq.setAttachmentGroupId(usptBsnsCnvnHist.getAttachmentGroupId());
			}
		 }
		//셋팅
		 usptCnvnTaskInfoHistDto.setUsptCnvnChangeReq(usptCnvnChangeReq);

		/************************************************과제정보 변경 전 내용*/
		 UsptCnvnTaskInfoHist usptCnvnTaskInfoHistBefore = new UsptCnvnTaskInfoHist();
		 usptCnvnTaskInfoHistBefore.setTaskNmKo(usptCnvnChangeReq.getTaskNmKo());		/** 과제명(국문) */
		 usptCnvnTaskInfoHistBefore.setTaskNmEn(usptCnvnChangeReq.getTaskNmEn()); 	/** 과제명(영문) */
		 usptCnvnTaskInfoHistBefore.setApplyField(usptCnvnChangeReq.getApplyField());	/** 지원분야 */

		 //과제정보 사업기간
		Date bsnsBgndt = string.toDate(usptCnvnChangeReq.getBsnsBgnde());
		Date bsnsEnddt = string.toDate(usptCnvnChangeReq.getBsnsEndde());
		if (bsnsBgndt != null && bsnsEnddt != null ) {
			Date lastdt = string.toDate(date.getCurrentDate("yyyy") + "1231");
			usptCnvnTaskInfoHistBefore.setBsnsPd("협약 체결일 ~ " + date.format(bsnsEnddt, "yyyy-MM-dd"));
			int allDiffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, bsnsEnddt);
			if(date.compareDay(bsnsEnddt, lastdt) == 1) {
				int diffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, lastdt);
				usptCnvnTaskInfoHistBefore.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(lastdt, "yyyy-MM-dd") + "(" + diffMonths + "개월)");
			} else {
				usptCnvnTaskInfoHistBefore.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
			}
			usptCnvnTaskInfoHistBefore.setBsnsPdAll(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
		}
		//전 내용 셋팅
		usptCnvnTaskInfoHistDto.setUsptCnvnTaskInfoHistBefore(usptCnvnTaskInfoHistBefore);

		/**********************************************과제정보 변경 후 내용*/
		//조회
		UsptCnvnTaskInfoHist usptCnvnTaskInfoHistAfter = usptCnvnTaskInfoHistDao.selectMaxHist(cnvnChangeReqId);
		if (bsnsBgndt != null && bsnsEnddt != null ) {
			Date lastdt = string.toDate(date.getCurrentDate("yyyy") + "1231");
			usptCnvnTaskInfoHistAfter.setBsnsPd("협약 체결일 ~ " + date.format(bsnsEnddt, "yyyy-MM-dd"));
			int allDiffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, bsnsEnddt);
			if(date.compareDay(bsnsEnddt, lastdt) == 1) {
				int diffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, lastdt);
				usptCnvnTaskInfoHistAfter.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(lastdt, "yyyy-MM-dd") + "(" + diffMonths + "개월)");
			} else {
				usptCnvnTaskInfoHistAfter.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
			}
			usptCnvnTaskInfoHistAfter.setBsnsPdAll(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
		}
		//후 내용 셋팅
		usptCnvnTaskInfoHistDto.setUsptCnvnTaskInfoHistAfter(usptCnvnTaskInfoHistAfter);

		return usptCnvnTaskInfoHistDto;
	}
	/**
	 * 협약변경관리 과제정보 승인
	 * changeIemDivCd, cnvnChangeReqId
	 */
	public void modifyChangeCnvnTaskInfoApproval(UsptCnvnTaskInfoHistDto usptCnvnTaskInfoHistDto) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/** 협약변경요청ID */
		String cnvnChangeReqId=usptCnvnTaskInfoHistDto.getUsptCnvnChangeReq().getCnvnChangeReqId();
		/** 신청자ID */
		String applcntId = usptCnvnTaskInfoHistDto.getUsptCnvnChangeReq().getApplcntId();
		/**협약변경항목구분 - CHIE02*/
		String changeIemDivCd = Code.changeIemDiv.과제정보;

		/** 협약변경요청, 이력테이블 변경*/
		modifyCnvnChangeReqApproval(cnvnChangeReqId,  changeIemDivCd);

		//협약과제정보변경이력 변경 전 정보 셋팅
		UsptCnvnTaskInfoHist usptCnvnTaskInfoHistBefore = usptCnvnTaskInfoHistDto.getUsptCnvnTaskInfoHistBefore();
		//협약과제정보변경이력 변경정보 셋팅
		UsptCnvnTaskInfoHist usptCnvnTaskInfoHistAfter = usptCnvnTaskInfoHistDto.getUsptCnvnTaskInfoHistAfter();

		/** 협약수행기관신분변경이력 변경 전 정보 등록*/
		String newCnvnTaskInfoHistId =CoreUtils.string.getNewId(Code.prefix.협약과제정보변경이력);
		String confmCnvnTaskInfoHistId = usptCnvnTaskInfoHistAfter.getCnvnTaskInfoHistId();

		usptCnvnTaskInfoHistBefore.setCnvnTaskInfoHistId(newCnvnTaskInfoHistId);					/*협약과제정보변경이력ID*/
		usptCnvnTaskInfoHistBefore.setCnvnChangeReqId(cnvnChangeReqId);							/*협약변경요청ID*/
		usptCnvnTaskInfoHistBefore.setConfmCnvnTaskInfoHistId(confmCnvnTaskInfoHistId);			/*승인협약과제정보변경이력ID*/
		usptCnvnTaskInfoHistBefore.setCreatedDt(now);
		usptCnvnTaskInfoHistBefore.setCreatorId(strMemberId);
		usptCnvnTaskInfoHistDao.insert(usptCnvnTaskInfoHistBefore);

		/** 협약수행기관신분변경이력 변경 후 정보 등록*/
		/**사업계획서 과제정보 변경*/
		//기본정보 조회-사업계획서ID
		UsptCnvnChangeReq usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);
		/*사업계획서ID 셋팅*/
		String bsnsPlanDocId = usptCnvnChangeReq.getBsnsPlanDocId();

		//변경
		UsptBsnsPlanDoc usptBsnsPlanDoc = new UsptBsnsPlanDoc();
		usptBsnsPlanDoc.setBsnsPlanDocId(bsnsPlanDocId);
		usptBsnsPlanDoc.setTaskNmKo(usptCnvnTaskInfoHistAfter.getTaskNmKo());		/** 과제명(국문) */
		usptBsnsPlanDoc.setTaskNmEn(usptCnvnTaskInfoHistAfter.getTaskNmEn()); 		/** 과제명(영문) */
		usptBsnsPlanDoc.setApplyField(usptCnvnTaskInfoHistAfter.getApplyField());		/** 지원분야 */
		usptBsnsPlanDoc.setUpdatedDt(now);
		usptBsnsPlanDoc.setUpdaterId(strMemberId);
		int updateCnt = usptBsnsPlanDocDao.updateTaskInfo(usptBsnsPlanDoc);

		/** email, sms */
		if(updateCnt>0) {
			CnvnChangeParam param = new  CnvnChangeParam();
			param.setCnvnChangeSttusCd(Code.cnvnChangeSttus.승인);		/* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
			param.setChangeIemDivCd(changeIemDivCd);							/** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
			param.setApplcntId(applcntId);		/** 신청자ID */
			sendMsg(param);
		}
	}
	/****************************************과제정보****************************************************/

	/****************************************참여기업****************************************************/
	/**
	 * 협약변경관리 참여기업 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptTaskPrtcmpnyInfoHistDto selectTaskPrtcmpnyInfoHist(HttpServletRequest request, CnvnChangeParam cnvnChangeParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.참여기업;					/**협약변경항목구분 - CHIE03*/

		UsptTaskPrtcmpnyInfoHistDto usptTaskPrtcmpnyInfoHistDto = new UsptTaskPrtcmpnyInfoHistDto();

		//기본정보 조회
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);

		//사업협약변경이력 최신정보 조회
		UsptBsnsCnvnHist  usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

		 if(usptBsnsCnvnHist !=null) {
			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptBsnsCnvnHist.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnHist.getAttachmentGroupId());
				usptTaskPrtcmpnyInfoHistDto.setAttachFileList(list);
				 //신청시 등록에 필요한 첨부파일그룹ID 셋팅
				 usptCnvnChangeReq.setAttachmentGroupId(usptBsnsCnvnHist.getAttachmentGroupId());
			}
		 }
		//셋팅
		usptTaskPrtcmpnyInfoHistDto.setUsptCnvnChangeReq(usptCnvnChangeReq);

		/********************************************** 참여기업 변경 전 정보*/
		/*참여기업 개요 셋팅*/
		Long partcptnCompanyCnt = usptCnvnChangeReq.getPartcptnCompanyCnt();	/* 참여기업수 */
		Long smlpzCnt = usptCnvnChangeReq.getSmlpzCnt();								/* 중소기업수 */
		Long mspzCnt= usptCnvnChangeReq.getMspzCnt();									/* 중견기업수 */
		Long etcCnt = usptCnvnChangeReq.getEtcCnt();										/* 기타 */

		UsptTaskPrtcmpnyInfoHist usptTaskPrtcmpnyInfoHistBeforeList = new UsptTaskPrtcmpnyInfoHist();
		usptTaskPrtcmpnyInfoHistBeforeList.setPartcptnCompanyCnt(partcptnCompanyCnt);
		usptTaskPrtcmpnyInfoHistBeforeList.setSmlpzCnt(smlpzCnt);
		usptTaskPrtcmpnyInfoHistBeforeList.setMspzCnt(mspzCnt);
		usptTaskPrtcmpnyInfoHistBeforeList.setEtcCnt(etcCnt);
		//전 내용 셋팅
		usptTaskPrtcmpnyInfoHistDto.setUsptTaskPrtcmpnyInfoHistBefore(usptTaskPrtcmpnyInfoHistBeforeList);

		/*과제참여기업 정보 조회(bsnsPlanDocId)*/
		UsptTaskPrtcmpny usptTaskPrtcmpny = new UsptTaskPrtcmpny();
		usptTaskPrtcmpny.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		List<UsptTaskPrtcmpny> usptTaskPrtcmpnyList= usptTaskPrtcmpnyDao.selectList(usptTaskPrtcmpny);

		List<UsptTaskPrtcmpnyHist> usptTaskPrtcmpnyHistBeforeList = new ArrayList<>();
		UsptTaskPrtcmpnyHist usptTaskPrtcmpnyHist = null;

		for(UsptTaskPrtcmpny usptTaskPrtcmpnyHistInfo : usptTaskPrtcmpnyList) {
			usptTaskPrtcmpnyHist = new UsptTaskPrtcmpnyHist();
			usptTaskPrtcmpnyHist.setMemberId(usptTaskPrtcmpnyHistInfo.getMemberId());		/*회원ID*/
			usptTaskPrtcmpnyHist.setEntrpsNm(usptTaskPrtcmpnyHistInfo.getEntrpsNm());		/* 업체명 */
			usptTaskPrtcmpnyHist.setRspnberNm(usptTaskPrtcmpnyHistInfo.getRspnberNm());	/* 책임자명 */
			usptTaskPrtcmpnyHist.setClsfNm(usptTaskPrtcmpnyHistInfo.getClsfNm());				/* 직급명 */
			usptTaskPrtcmpnyHist.setEncTelno(usptTaskPrtcmpnyHistInfo.getTelno());				/* 복호화된 전화번호 */
			usptTaskPrtcmpnyHist.setEncMbtlnum(usptTaskPrtcmpnyHistInfo.getMbtlnum());		/* 복호화된 휴대폰번호 */
			usptTaskPrtcmpnyHist.setEncEmail(usptTaskPrtcmpnyHistInfo.getEmail());				/* 복호화된 이메일 */
			usptTaskPrtcmpnyHistBeforeList.add(usptTaskPrtcmpnyHist);
		}
		//전 내용 셋팅
		usptTaskPrtcmpnyInfoHistDto.setUsptTaskPrtcmpnyHistBeforeList(usptTaskPrtcmpnyHistBeforeList);

		/********************************************** 참여기업 변경 후 정보*/

		/** 과제참여기업정보변경이력 변경 후*/
		 UsptTaskPrtcmpnyInfoHist usptTaskPrtcmpnyInfoHistAfter = usptTaskPrtcmpnyInfoHistDao.selectMaxHist(cnvnChangeReqId);
		//후 내용 셋팅
		usptTaskPrtcmpnyInfoHistDto.setUsptTaskPrtcmpnyInfoHistAfter(usptTaskPrtcmpnyInfoHistAfter);

		/** 과제참여기업변경이력 변경 후*/
		String taskPrtcmpnyInfoHistId = usptTaskPrtcmpnyInfoHistAfter.getTaskPrtcmpnyInfoHistId();	//과제참여기업정보변경이력ID
		List<UsptTaskPrtcmpnyHist> usptTaskPrtcmpnyHistAfterList= usptTaskPrtcmpnyHistDao.selectList(taskPrtcmpnyInfoHistId);

		for(UsptTaskPrtcmpnyHist usptTaskPrtcmpnyHistInfo : usptTaskPrtcmpnyHistAfterList) {
			usptTaskPrtcmpnyHistInfo.setEncTelno(usptTaskPrtcmpnyHistInfo.getTelno());				/* 복호화된 전화번호 */
			usptTaskPrtcmpnyHistInfo.setEncMbtlnum(usptTaskPrtcmpnyHistInfo.getMbtlnum());		/* 복호화된 휴대폰번호 */
			usptTaskPrtcmpnyHistInfo.setEncEmail(usptTaskPrtcmpnyHistInfo.getEmail());				/* 복호화된 이메일 */
		}
		//후 내용 셋팅
		usptTaskPrtcmpnyInfoHistDto.setUsptTaskPrtcmpnyHistAfterList(usptTaskPrtcmpnyHistAfterList);

		/* 개인정보 조회 이력 생성  --------------------------------------------------------------------------------*/
		// 참여기업 이력 생성
//		if(usptTaskPrtcmpnyHistBeforeList != null) {
//			for(UsptTaskPrtcmpnyHist tpInfo : usptTaskPrtcmpnyHistBeforeList) {
//				LogIndvdlInfSrch tpLogParam = LogIndvdlInfSrch.builder()
//						.memberId(worker.getMemberId())
//						.memberIp(CoreUtils.webutils.getRemoteIp(request))
//						.workTypeNm("조회")
//						.workCn("협약변경 관리 상세조회 - 참여기업 변경 전")
//						.trgterId(tpInfo.getMemberId())
//						.email(tpInfo.getEmail())
//						.birthday("")
//						.mobileNo(tpInfo.getMbtlnum())
//						.build();
//				indvdlInfSrchUtils.insert(tpLogParam);
//			}
//		}
//		// 참여기업 이력 생성
//		if(usptTaskPrtcmpnyHistAfterList != null) {
//			for(UsptTaskPrtcmpnyHist tpInfo : usptTaskPrtcmpnyHistAfterList) {
//				LogIndvdlInfSrch tpLogParam = LogIndvdlInfSrch.builder()
//						.memberId(worker.getMemberId())
//						.memberIp(CoreUtils.webutils.getRemoteIp(request))
//						.workTypeNm("조회")
//						.workCn("협약변경 관리 상세조회 - 참여기업 변경 후")
//						.trgterId(tpInfo.getMemberId())
//						.email(tpInfo.getEmail())
//						.birthday("")
//						.mobileNo(tpInfo.getMbtlnum())
//						.build();
//				indvdlInfSrchUtils.insert(tpLogParam);
//			}
//		}
		return usptTaskPrtcmpnyInfoHistDto;
	}
	/**
	 * 협약변경관리 참여기업 승인
	 *  cnvnChangeReqId
	 */
	public void modifyTaskPrtcmpnyInfoHistApproval(UsptTaskPrtcmpnyInfoHistDto usptTaskPrtcmpnyInfoHistDto) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/** 협약변경요청ID */
		String cnvnChangeReqId=usptTaskPrtcmpnyInfoHistDto.getUsptCnvnChangeReq().getCnvnChangeReqId();
		/** 신청자ID */
		String applcntId = usptTaskPrtcmpnyInfoHistDto.getUsptCnvnChangeReq().getApplcntId();
		/**협약변경항목구분 - CHIE03*/
		String changeIemDivCd = Code.changeIemDiv.참여기업;

		/** 협약변경요청, 이력테이블 변경*/
		modifyCnvnChangeReqApproval(cnvnChangeReqId,  changeIemDivCd);

		//기본정보 조회-사업계획서ID
		UsptCnvnChangeReq usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);
		/*사업계획서ID 셋팅*/
		String bsnsPlanDocId = usptCnvnChangeReq.getBsnsPlanDocId();

		/**************************************************************사업계획서-참여기업수 변경*/
		//과제참여기업정보변경이력 변경 전 정보 셋팅
		UsptTaskPrtcmpnyInfoHist usptTaskPrtcmpnyInfoHistBefore = usptTaskPrtcmpnyInfoHistDto.getUsptTaskPrtcmpnyInfoHistBefore();
		//과제참여기업정보변경이력 변경 후 정보 셋팅
		UsptTaskPrtcmpnyInfoHist usptTaskPrtcmpnyInfoHistAfter = usptTaskPrtcmpnyInfoHistDto.getUsptTaskPrtcmpnyInfoHistAfter();

		/** 과제참여기업정보변경이력 변경 전 정보 등록*/
		String newTaskPrtcmpnyInfoHistId=CoreUtils.string.getNewId(Code.prefix.과제참여기업정보변경이력);
		String confmTaskPrtcmpnyInfoHistId = usptTaskPrtcmpnyInfoHistAfter.getTaskPrtcmpnyInfoHistId();

		usptTaskPrtcmpnyInfoHistBefore.setTaskPrtcmpnyInfoHistId(newTaskPrtcmpnyInfoHistId);				/** 과제참여기업정보변경이력ID */
		usptTaskPrtcmpnyInfoHistBefore.setCnvnChangeReqId(cnvnChangeReqId);									 /** 협약변경요청ID */;
		usptTaskPrtcmpnyInfoHistBefore.setConfmTaskPrtcmpnyInfoHistId(confmTaskPrtcmpnyInfoHistId);		/** 승인과제참여기업정보변경이력ID */
		usptTaskPrtcmpnyInfoHistBefore.setCreatedDt(now);
		usptTaskPrtcmpnyInfoHistBefore.setCreatorId(strMemberId);
		 usptTaskPrtcmpnyInfoHistDao.insert(usptTaskPrtcmpnyInfoHistBefore);

		/** 참여기업 변경 후 정보 등록*/
		UsptBsnsPlanDoc usptBsnsPlanDoc = new UsptBsnsPlanDoc();
		usptBsnsPlanDoc.setBsnsPlanDocId(bsnsPlanDocId);
		usptBsnsPlanDoc.setPartcptnCompanyCnt(usptTaskPrtcmpnyInfoHistAfter.getPartcptnCompanyCnt());		/*참여기업수 */
		usptBsnsPlanDoc.setSmlpzCnt(usptTaskPrtcmpnyInfoHistAfter.getSmlpzCnt());			 						/*중소기업수 */
		usptBsnsPlanDoc.setMspzCnt(usptTaskPrtcmpnyInfoHistAfter.getMspzCnt());									/*중견기업수 */
		usptBsnsPlanDoc.setEtcCnt(usptTaskPrtcmpnyInfoHistAfter.getEtcCnt());											/*기타수 */
		usptBsnsPlanDoc.setUpdaterId(strMemberId);
		usptBsnsPlanDocDao.updatePrtcmpnyCnt(usptBsnsPlanDoc);

		/*************************************************************과제참여기업-변경*/
		//과제참여기업변경이력 변경 전 정보 셋팅
		List<UsptTaskPrtcmpnyHist> usptTaskPrtcmpnyHistBeforeList = usptTaskPrtcmpnyInfoHistDto.getUsptTaskPrtcmpnyHistBeforeList();
		//과제참여기업변경이력 변경 후 정보 셋팅
		List<UsptTaskPrtcmpnyHist> usptTaskPrtcmpnyHistAfterList = usptTaskPrtcmpnyInfoHistDto.getUsptTaskPrtcmpnyHistAfterList();

		/** 과제참여기업정보변경이력 변경 전 정보 등록*/
		for(UsptTaskPrtcmpnyHist usptTaskPrtcmpnyHistInfo : usptTaskPrtcmpnyHistBeforeList) {
			String newTaskPartcptnEntrprsHistId=CoreUtils.string.getNewId(Code.prefix.과제참여기업변경이력);
			String taskPrtcmpnyInfoHistId = newTaskPrtcmpnyInfoHistId;

			usptTaskPrtcmpnyHistInfo.setTaskPartcptnEntrprsHistId(newTaskPartcptnEntrprsHistId);				/*과제참여기업변경이력ID*/
			usptTaskPrtcmpnyHistInfo.setTaskPrtcmpnyInfoHistId(taskPrtcmpnyInfoHistId);							/*과제참여기업정보변경이력ID*/
			if(string.isNotBlank(usptTaskPrtcmpnyHistInfo.getEncTelno())){
				usptTaskPrtcmpnyHistInfo.setEncTelno(CoreUtils.aes256.encrypt(usptTaskPrtcmpnyHistInfo.getEncTelno(), newTaskPartcptnEntrprsHistId));			/* 암호화된 전화번호 */
			}
			if(string.isNotBlank(usptTaskPrtcmpnyHistInfo.getEncMbtlnum())){
				usptTaskPrtcmpnyHistInfo.setEncMbtlnum(CoreUtils.aes256.encrypt(usptTaskPrtcmpnyHistInfo.getEncMbtlnum(), newTaskPartcptnEntrprsHistId));			/* 암호화된 전화번호 */
			}
			if(string.isNotBlank(usptTaskPrtcmpnyHistInfo.getEncEmail())){
				usptTaskPrtcmpnyHistInfo.setEncEmail(CoreUtils.aes256.encrypt(usptTaskPrtcmpnyHistInfo.getEncEmail(), newTaskPartcptnEntrprsHistId));			/* 암호화된 전화번호 */
			}
			usptTaskPrtcmpnyHistInfo.setCreatedDt(now);
			usptTaskPrtcmpnyHistInfo.setCreatorId(strMemberId);
			usptTaskPrtcmpnyHistDao.insert(usptTaskPrtcmpnyHistInfo);
		}

		/** 과제참여기업 변경 후 정보 변경*/
		//삭제(과제참여기업변경이력과 과제참여기업의 일치하지 않아 삭제 후 등록)
		usptTaskPrtcmpnyDao.deleteBsnsPlanDocIdAll(bsnsPlanDocId);

		//등록
		int insertCnt = 0;
		UsptTaskPrtcmpny insertParam = null;
		for(UsptTaskPrtcmpnyHist input : usptTaskPrtcmpnyHistAfterList) {
			insertParam = new UsptTaskPrtcmpny();
			String newTaskPartcptnEntrprsId = CoreUtils.string.getNewId(Code.prefix.과제참여기업);

			insertParam.setTaskPartcptnEntrprsId(newTaskPartcptnEntrprsId);		/** 과제참여기업ID */
			insertParam.setBsnsPlanDocId(bsnsPlanDocId);							/** 사업계획서ID */
			insertParam.setEntrpsNm(input.getEntrpsNm());							/** 업체명 */
			insertParam.setRspnberNm(input.getRspnberNm());						/** 책임자명 */
			insertParam.setClsfNm(input.getClsfNm());									/** 직급명 */
			if(CoreUtils.string.isNotBlank(input.getEncTelno())) {
				insertParam.setEncTelno(CoreUtils.aes256.encrypt(input.getEncTelno(), newTaskPartcptnEntrprsId));			/** 암호화된 전화번호 */
			}
			if(CoreUtils.string.isNotBlank(input.getEncMbtlnum())) {
				insertParam.setEncMbtlnum(CoreUtils.aes256.encrypt(input.getEncMbtlnum(), newTaskPartcptnEntrprsId));	/** 암호화된 휴대폰번호 */
			}
			if(CoreUtils.string.isNotBlank(input.getEncEmail())) {
				insertParam.setEncEmail(CoreUtils.aes256.encrypt(input.getEncEmail(), newTaskPartcptnEntrprsId));			/** 암호화된 이메일 */
			}
			insertParam.setTlsyRegistNo(input.getTlsyRegistNo());		/** 국가연구자번호 */
			insertParam.setCreatedDt(now);
			insertParam.setCreatorId(strMemberId);
			insertParam.setUpdatedDt(now);
			insertParam.setUpdaterId(strMemberId);
			insertCnt += usptTaskPrtcmpnyDao.insert(insertParam);
		}

		/** email, sms */
		if(insertCnt == usptTaskPrtcmpnyHistAfterList.size()) {
			CnvnChangeParam param = new  CnvnChangeParam();
			param.setCnvnChangeSttusCd(Code.cnvnChangeSttus.승인);		/* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
			param.setChangeIemDivCd(changeIemDivCd);							/** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
			param.setApplcntId(applcntId);		/** 신청자ID */
			sendMsg(param);
		}
	}
	/****************************************참여기업 end****************************************************/

	/****************************************과제참여인력변경이력 start****************************************************/
	/**
	 * 협약변경관리 참여인력 조회
	 * @param   cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptTaskPartcptsHistDto selectTaskPartcptsHist(HttpServletRequest request, CnvnChangeParam cnvnChangeParam){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId =cnvnChangeParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.참여인력;					/**협약변경항목구분 - CHIE04*/

		UsptTaskPartcptsHistDto usptTaskPartcptsHistDto = new UsptTaskPartcptsHistDto();

		/**기본정보 조회*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);

		//사업협약변경이력 최신정보 조회
		UsptBsnsCnvnHist  usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

		 if(usptBsnsCnvnHist !=null) {
			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptBsnsCnvnHist.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnHist.getAttachmentGroupId());
				usptTaskPartcptsHistDto.setAttachFileList(list);
				 //신청시 등록에 필요한 첨부파일그룹ID 셋팅
				 usptCnvnChangeReq.setAttachmentGroupId(usptBsnsCnvnHist.getAttachmentGroupId());
			}
		 }
		//셋팅
		 usptTaskPartcptsHistDto.setUsptCnvnChangeReq(usptCnvnChangeReq);

		/********************************************** 참여인력 변경 전 정보*/
		UsptTaskPartcpts usptTaskPartcpts = new UsptTaskPartcpts();
		usptTaskPartcpts.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		List<UsptTaskPartcpts> usptTaskPartcptsList= usptTaskPartcptsDao.selectList(usptTaskPartcpts);

		List<UsptTaskPartcptsHist> usptTaskPartcptsHistBeforeList = new ArrayList<>();
		UsptTaskPartcptsHist usptTaskPartcptsHist = null;

		for(UsptTaskPartcpts usptTaskPartcptsInfo : usptTaskPartcptsList) {
			usptTaskPartcptsHist = new UsptTaskPartcptsHist();
			usptTaskPartcptsHist.setMemberId(usptTaskPartcptsInfo.getMemberId());				/**업체회원ID*/
			usptTaskPartcptsHist.setPartcptsNm(usptTaskPartcptsInfo.getPartcptsNm());			/** 참여자명 */
			usptTaskPartcptsHist.setChrgRealmNm(usptTaskPartcptsInfo.getChrgRealmNm());		/** 담당분야명 */
			usptTaskPartcptsHist.setEncMbtlnum(usptTaskPartcptsInfo.getMbtlnum());				/** 복호화된 휴대폰번호 */
			usptTaskPartcptsHist.setEncBrthdy(usptTaskPartcptsInfo.getBrthdy());					/** 복호화된 생년월일 */
			usptTaskPartcptsHist.setPartcptnRate(usptTaskPartcptsInfo.getPartcptnRate());			/** 참여율 */
			usptTaskPartcptsHistBeforeList.add(usptTaskPartcptsHist);
		}
		//전 내용 셋팅
		usptTaskPartcptsHistDto.setUsptTaskPartcptsHistBeforeList(usptTaskPartcptsHistBeforeList);

		/********************************************** 참여인력 변경 후 정보*/
		List<UsptTaskPartcptsHist>  usptTaskPartcptsHistAfterList = usptTaskPartcptsHistDao.selectMaxHistList(cnvnChangeReqId);

		for(UsptTaskPartcptsHist input : usptTaskPartcptsHistAfterList) {
			input.setEncMbtlnum(input.getMbtlnum());
			input.setEncBrthdy(input.getBrthdy());
		}
		//후 내용 셋팅
		usptTaskPartcptsHistDto.setUsptTaskPartcptsHistAfterList(usptTaskPartcptsHistAfterList);

		/* 개인정보 조회 이력 생성  --------------------------------------------------------------------------------*/
		// 참여인력 이력 생성 (전)
		if(usptTaskPartcptsHistBeforeList != null) {
			for(UsptTaskPartcptsHist tpInfo : usptTaskPartcptsHistBeforeList) {
				LogIndvdlInfSrch tpLogParam = LogIndvdlInfSrch.builder()
						.memberId(worker.getMemberId())
						.memberIp(CoreUtils.webutils.getRemoteIp(request))
						.workTypeNm("조회")
						.workCn("협약변경 관리 상세조회 - 참여인력 변경 전")
						.trgterId(tpInfo.getMemberId())
						.email("")
						.birthday(tpInfo.getBrthdy())
						.mobileNo(tpInfo.getMbtlnum())
						.build();
				indvdlInfSrchUtils.insert(tpLogParam);
			}
		}
		// 참여인력 이력 생성(후)
		if(usptTaskPartcptsHistAfterList != null) {
			for(UsptTaskPartcptsHist tpInfo : usptTaskPartcptsHistAfterList) {
				LogIndvdlInfSrch tpLogParam = LogIndvdlInfSrch.builder()
						.memberId(worker.getMemberId())
						.memberIp(CoreUtils.webutils.getRemoteIp(request))
						.workTypeNm("조회")
						.workCn("협약변경 관리 상세조회 - 참여인력 변경 후")
						.trgterId(tpInfo.getMemberId())
						.email("")
						.birthday(tpInfo.getBrthdy())
						.mobileNo(tpInfo.getMbtlnum())
						.build();
				indvdlInfSrchUtils.insert(tpLogParam);
			}
		}
		return usptTaskPartcptsHistDto;
	}
	/**
	 * 협약변경관리 참여인력 승인
	 *  cnvnChangeReqId
	 */
	public void modifyTaskPartcptsHistApproval(UsptTaskPartcptsHistDto usptTaskPartcptsHistDto) {

		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/** 협약변경요청ID */
		String cnvnChangeReqId=usptTaskPartcptsHistDto.getUsptCnvnChangeReq().getCnvnChangeReqId();
		/** 신청자ID */
		String applcntId = usptTaskPartcptsHistDto.getUsptCnvnChangeReq().getApplcntId();
		/**협약변경항목구분 - CHIE04*/
		String changeIemDivCd = Code.changeIemDiv.참여인력;

		/** 협약변경요청, 이력테이블 변경*/
		modifyCnvnChangeReqApproval(cnvnChangeReqId,  changeIemDivCd);

		//기본정보 조회-사업계획서ID
		UsptCnvnChangeReq usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);
		/*사업계획서ID 셋팅*/
		String bsnsPlanDocId = usptCnvnChangeReq.getBsnsPlanDocId();

		/**************************************************************변경*/
		//과제참여자변경이력 변경 전 정보 셋팅*/
		List<UsptTaskPartcptsHist>  usptTaskPartcptsHistBeforeList = usptTaskPartcptsHistDto.getUsptTaskPartcptsHistBeforeList();
		//과제참여자변경이력 변경 후 정보 셋팅*/
		List<UsptTaskPartcptsHist>  usptTaskPartcptsHistAfterList = usptTaskPartcptsHistDto.getUsptTaskPartcptsHistAfterList();

		/** 과제참여자변경이력 변경 전 정보 등록*/
		for(UsptTaskPartcptsHist input : usptTaskPartcptsHistBeforeList) {
			String newtaskPartcptsHistId=CoreUtils.string.getNewId(Code.prefix.과제참여자변경이력);
			String confmTaskPartcptsHistId = usptTaskPartcptsHistAfterList.get(0).getTaskPartcptsHistId();

			input.setTaskPartcptsHistId(newtaskPartcptsHistId);				/** 과제참여자변경이력ID */
			input.setCnvnChangeReqId(cnvnChangeReqId);			 		/** 협약변경요청ID */
			input.setConfmTaskPartcptsHistId(confmTaskPartcptsHistId);	/** 승인과제참여자변경이력IDID */
			if(CoreUtils.string.isNotBlank(input.getEncMbtlnum())) {
				input.setEncMbtlnum(CoreUtils.aes256.encrypt(input.getEncMbtlnum(), newtaskPartcptsHistId));			/** 암호화된 휴대폰번호 */
			}
			if(CoreUtils.string.isNotBlank(input.getEncBrthdy())) {
				input.setEncBrthdy(CoreUtils.aes256.encrypt(input.getEncBrthdy(), newtaskPartcptsHistId));			/** 암호화된 생년월일 */
			}
			input.setCreatedDt(now);
			input.setCreatorId(strMemberId);
			usptTaskPartcptsHistDao.insert(input);
		}

		/** 과제참여자 변경 후 정보 등록*/
		//삭제
		//(과제참여자변경이력과 과제참여자변경 일치하지 않아 삭제 후 등록)
		usptTaskPartcptsDao.deleteBsnsPlanDocIdAll(bsnsPlanDocId);

		//등록
		int insertCnt=0;
		UsptTaskPartcpts insertParam = null;
		for(UsptTaskPartcptsHist input : usptTaskPartcptsHistAfterList) {
			insertParam = new UsptTaskPartcpts();

			String newTaskPartcptsId = CoreUtils.string.getNewId(Code.prefix.과제참여자);
			insertParam.setTaskPartcptsId(newTaskPartcptsId);			/** 과제참여자ID */
			insertParam.setBsnsPlanDocId(bsnsPlanDocId);			 	/** 사업계획서ID */
			insertParam.setPartcptsNm(input.getPartcptsNm());			/** 참여자명 */
			insertParam.setChrgRealmNm(input.getChrgRealmNm());	/** 담당분야명 */
			if(CoreUtils.string.isNotBlank(input.getEncMbtlnum())) {
				insertParam.setEncMbtlnum(CoreUtils.aes256.encrypt(input.getEncMbtlnum(), newTaskPartcptsId));	/** 암호화된 휴대폰번호 */
			}
			if(CoreUtils.string.isNotBlank(input.getEncBrthdy())) {
				insertParam.setEncBrthdy(CoreUtils.aes256.encrypt(input.getEncBrthdy(), newTaskPartcptsId));		/** 암호화된 생년월일 */
			}
			insertParam.setPartcptnRate(input.getPartcptnRate());		/** 참여율 */
			insertParam.setMemberId(strMemberId);						/** 회원ID : 소속업체회원ID */
			insertParam.setCreatedDt(now);
			insertParam.setCreatorId(strMemberId);
			insertParam.setUpdatedDt(now);
			insertParam.setUpdaterId(strMemberId);

			insertCnt += usptTaskPartcptsDao.insert(insertParam);
		}

		/** email, sms */
		if(insertCnt == usptTaskPartcptsHistAfterList.size()) {
			CnvnChangeParam param = new  CnvnChangeParam();
			param.setCnvnChangeSttusCd(Code.cnvnChangeSttus.승인);		/* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
			param.setChangeIemDivCd(changeIemDivCd);							/** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
			param.setApplcntId(applcntId);		/** 신청자ID */
			sendMsg(param);
		}
	}
	/****************************************과제참여인력변경이력****************************************************/

	/****************************************과제신청사업비변경이력 start****************************************************/
	/**
	 * 협약변경관리 과제신청사업비변경이력 조회
	 * @param  cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptTaskReqstWctHistDto selectTaskReqstWctHist(CnvnChangeParam cnvnChangeParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.사업비;						/**협약변경항목구분 - CHIE05*/

		UsptTaskReqstWctHistDto usptTaskReqstWctHistDto = new UsptTaskReqstWctHistDto();

		/**기본정보 조회*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

		 if(usptBsnsCnvnHist !=null) {
			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptBsnsCnvnHist.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnHist.getAttachmentGroupId());
				usptTaskReqstWctHistDto.setAttachFileList(list);
				 //신청시 등록에 필요한 첨부파일그룹ID 셋팅
				 usptCnvnChangeReq.setAttachmentGroupId(usptBsnsCnvnHist.getAttachmentGroupId());
			}
		 }
		//셋팅
		 usptTaskReqstWctHistDto.setUsptCnvnChangeReq(usptCnvnChangeReq);

		/********************************************** 과제신청사업비 변경 전 정보*/
		//사업계획-과제신청사업비(신청예산) 조회
		UsptTaskReqstWct usptTaskReqstWct = new UsptTaskReqstWct();
		usptTaskReqstWct.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		List <UsptTaskReqstWct> usptTaskReqstWctList = usptTaskReqstWctDao.selectList(usptTaskReqstWct);

		List<UsptTaskReqstWctHist> usptTaskReqstWctHistBefore = new ArrayList<>();
		UsptTaskReqstWctHist usptTaskReqstWctHist = null;

		for(UsptTaskReqstWct usptTaskReqstWctInfo : usptTaskReqstWctList) {
			usptTaskReqstWctHist = new UsptTaskReqstWctHist();
			usptTaskReqstWctHist.setBsnsYear(usptTaskReqstWctInfo.getBsnsYear());				/** 사업년도 */
			usptTaskReqstWctHist.setTotalWct(usptCnvnChangeReq.getTotalWct());					/** 총사업비-사업계획서*/
			usptTaskReqstWctHist.setSportBudget(usptTaskReqstWctInfo.getSportBudget());		/** 지원예산 */
			usptTaskReqstWctHist.setAlotmCash(usptTaskReqstWctInfo.getAlotmCash());			/** 부담금현금 */
			usptTaskReqstWctHist.setAlotmActhng(usptTaskReqstWctInfo.getAlotmActhng());	/** 부담금현물 */
			usptTaskReqstWctHist.setAlotmSum(usptTaskReqstWctInfo.getAlotmSum());			/** 부담금소계 (현금+현물) */
			usptTaskReqstWctHist.setAlotmSumTot(usptTaskReqstWctInfo.getAlotmSumTot());	/** 부담금합계 (지원예산+현금+현물) */
			usptTaskReqstWctHistBefore.add(usptTaskReqstWctHist);
		}
		//총사업비-사업계획서(전후 변경되지 않음)
		usptTaskReqstWctHistDto.setTotalWct(usptCnvnChangeReq.getTotalWct());
		//전 내용 셋팅
		usptTaskReqstWctHistDto.setUsptTaskReqstWctHistBeforeList(usptTaskReqstWctHistBefore);

		/********************************************** 과제신청사업비 변경 후 정보*/
		/** 과제신청사업비변경이력*/
		List<UsptTaskReqstWctHist> usptTaskReqstWctHistAfterList  = usptTaskReqstWctHistDao.selectMaxHistList(cnvnChangeReqId);
		//후 내용 셋팅
		usptTaskReqstWctHistDto.setUsptTaskReqstWctHistAfterList(usptTaskReqstWctHistAfterList);

		return usptTaskReqstWctHistDto;
	}
	/**
	 * 협약변경관리 과제신청사업비변경이력 승인
	 * cnvnChangeReqId
	 */
	public void modifyTaskReqstWctHistApproval(UsptTaskReqstWctHistDto usptTaskReqstWctHistDto) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/** 협약변경요청ID */
		String cnvnChangeReqId=usptTaskReqstWctHistDto.getUsptCnvnChangeReq().getCnvnChangeReqId();
		/** 신청자ID */
		String applcntId = usptTaskReqstWctHistDto.getUsptCnvnChangeReq().getApplcntId();
		/**협약변경항목구분 - CHIE05*/
		String changeIemDivCd = Code.changeIemDiv.사업비;

		/** 협약변경요청, 이력테이블 변경*/
		modifyCnvnChangeReqApproval(cnvnChangeReqId,  changeIemDivCd);

		//기본정보 조회-사업계획서ID
		UsptCnvnChangeReq usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);
		/*사업계획서ID 셋팅*/
		String bsnsPlanDocId = usptCnvnChangeReq.getBsnsPlanDocId();

		/************************************************************** 변경*/
		//과제신청사업비변경이력 변경 전 정보 셋팅*/
		List<UsptTaskReqstWctHist> usptTaskReqstWctHistBeforeList  = usptTaskReqstWctHistDto.getUsptTaskReqstWctHistBeforeList();
		//과제신청사업비변경이력 변경 후 정보 셋팅*/
		List<UsptTaskReqstWctHist> usptTaskReqstWctHistAfterList  = usptTaskReqstWctHistDto.getUsptTaskReqstWctHistAfterList();

		/** 과제신청사업비변경이력 변경 전 정보 등록*/
		for(UsptTaskReqstWctHist input : usptTaskReqstWctHistBeforeList) {

			String newTaskReqstWctHistId = CoreUtils.string.getNewId(Code.prefix.과제신청사업비변경이력);
			String confmTaskReqstWctHistId = usptTaskReqstWctHistAfterList.get(0).getTaskReqstWctHistId();

			input.setTaskReqstWctHistId(newTaskReqstWctHistId);				/** 과제신청사업비변경이력ID */
			input.setCnvnChangeReqId(cnvnChangeReqId);			 			/** 협약변경요청ID */
			input.setConfmTaskReqstWctHistId(confmTaskReqstWctHistId);	/** 승인과제신청사업비변경이력IDID */
			input.setCreatedDt(now);
			input.setCreatorId(strMemberId);

			usptTaskReqstWctHistDao.insert(input);
		}

		 /** 과제신청사업비 변경 후 정보 등록*/
		//삭제
		 //(과제신청사업비변경이력 과제신청사업비 일치하지 않아 삭제 후 등록)
		usptTaskReqstWctDao.deleteBsnsPlanDocIdAll(bsnsPlanDocId);

		//등록
		int insertCnt =0;
		UsptTaskReqstWct insertParam = null;
		for(UsptTaskReqstWctHist input : usptTaskReqstWctHistAfterList) {
			insertParam = new UsptTaskReqstWct();

			String newTaskPartcptsId = CoreUtils.string.getNewId(Code.prefix.과제참여자);
			insertParam.setTaskReqstWctId(newTaskPartcptsId);			/** 과제신청사업비ID */
			insertParam.setBsnsPlanDocId(bsnsPlanDocId);				/** 사업계획서ID */
			insertParam.setBsnsYear(input.getBsnsYear());					/**사업년도*/;
			insertParam.setSportBudget(input.getSportBudget());		/** 지원예산 */
			insertParam.setAlotmCash(input.getAlotmCash());			/** 부담금현금 */
			insertParam.setAlotmActhng(input.getAlotmActhng());		/** 부담금현물 */
			insertParam.setCreatedDt(now);
			insertParam.setCreatorId(strMemberId);
			insertParam.setUpdatedDt(now);
			insertParam.setUpdaterId(strMemberId);

			insertCnt += usptTaskReqstWctDao.insert(insertParam);
		}

		/** email, sms */
		if(insertCnt == usptTaskReqstWctHistAfterList.size()) {
			CnvnChangeParam param = new  CnvnChangeParam();
			param.setCnvnChangeSttusCd(Code.cnvnChangeSttus.승인);		/* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
			param.setChangeIemDivCd(changeIemDivCd);							/** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
			param.setApplcntId(applcntId);	/** 신청자ID */
			sendMsg(param);
		}
	}
	/****************************************과제신청사업비변경이력****************************************************/


	/****************************************과제세목별사업비변경 start****************************************************/
	/**
	 * 협약변경관리 과제세목별사업비변경 전체 사업년도 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public List<String> selectBsnsPlanTaxitmWctBsnsYearList(CnvnChangeParam cnvnChangeParam){
		SecurityUtils.checkWorkerIsInsider();

		String bsnsCnvnId		= cnvnChangeParam.getBsnsCnvnId();				/** 사업협약ID */
		String cnvnChangeReqId =cnvnChangeParam.getCnvnChangeReqId();		/** 협약변경요청ID */
		String changeIemDivCd = Code.changeIemDiv.비목별사업비;		/**협약변경항목구분 - CHIE06*/

		/**기본정보 조회(changeIemDivCd, cnvnChangeReqId, bsnsCnvnId)*/
		UsptCnvnChangeReq usptCnvnChangeReq = usptCnvnChangeReqDao.selectCnvnChangeBaseInfoFront(changeIemDivCd, bsnsCnvnId, cnvnChangeReqId) ;

		/**과제세목별사업비 조회*/
		UsptTaskTaxitmWct usptTaskTaxitmWct = new UsptTaskTaxitmWct();
		usptTaskTaxitmWct.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		List <String> usptTaskTaxitmWctYearList = usptTaskTaxitmWctDao.selectBsnsPlanTaxitmWctBsnsYearList(usptTaskTaxitmWct);

		return usptTaskTaxitmWctYearList;
	}
	/**
	 * 협약변경관리 과제세목별사업비변경 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptTaskTaxitmWctHistDto selectTaskTaxitmWctHist(CnvnChangeParam cnvnChangeParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.비목별사업비;				/**협약변경항목구분 - CHIE06*/

		UsptTaskTaxitmWctHistDto usptTaskTaxitmWctHistDto = new UsptTaskTaxitmWctHistDto();

		/**기본정보 조회*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);
		 if(usptBsnsCnvnHist !=null) {
			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptBsnsCnvnHist.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnHist.getAttachmentGroupId());
				usptTaskTaxitmWctHistDto.setAttachFileList(list);
				 //신청시 등록에 필요한 첨부파일그룹ID 셋팅
				 usptCnvnChangeReq.setAttachmentGroupId(usptBsnsCnvnHist.getAttachmentGroupId());
			}
		 }
		//셋팅
		 usptTaskTaxitmWctHistDto.setUsptCnvnChangeReq(usptCnvnChangeReq);

		/********************************************** 과제세목별사업비 변경 전 정보*/
		UsptTaskTaxitmWct usptTaskTaxitmWct = new UsptTaskTaxitmWct();
		usptTaskTaxitmWct.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		usptTaskTaxitmWct.setBsnsYear(cnvnChangeParam.getBsnsYear());
		List <UsptTaskTaxitmWct> usptTaskTaxitmWctList = usptTaskTaxitmWctDao.selectBsnsPlanTaxitmWctList(usptTaskTaxitmWct);

		List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctHistBeforeList = new ArrayList<>();
		//조회되 값 셋팅
		for(UsptTaskTaxitmWct usptTaskTaxitmWctInfo : usptTaskTaxitmWctList) {
			UsptTaskTaxitmWctHist usptTaskTaxitmWctHist = new UsptTaskTaxitmWctHist();
			usptTaskTaxitmWctHist.setBsnsYear(usptTaskTaxitmWctInfo.getBsnsYear());							/*사업년도*/
			usptTaskTaxitmWctHist.setWctIoeNm(usptTaskTaxitmWctInfo.getWctIoeNm()); 					/*사업비비목명*/
			usptTaskTaxitmWctHist.setWctTaxitmId(usptTaskTaxitmWctInfo.getWctTaxitmId());				/*사업비세목ID */
			usptTaskTaxitmWctHist.setWctTaxitmNm(usptTaskTaxitmWctInfo.getWctTaxitmNm());			/*사업비세목명*/
			usptTaskTaxitmWctHist.setComputBasisCn(usptTaskTaxitmWctInfo.getComputBasisCn());	 		/*산출근거내용 */
			usptTaskTaxitmWctHist.setSportBudget(usptTaskTaxitmWctInfo.getSportBudget());				/*지원예산 */
			usptTaskTaxitmWctHist.setAlotmCash(usptTaskTaxitmWctInfo.getAlotmCash());					/*부담금현금 */
			usptTaskTaxitmWctHist.setAlotmActhng(usptTaskTaxitmWctInfo.getAlotmActhng());				/*부담금현물 */
			usptTaskTaxitmWctHist.setAlotmSumTot(usptTaskTaxitmWctInfo.getAlotmSumTot());				/*합계-지원예산+부담금현금+부담금현물*/
			usptTaskTaxitmWctHistBeforeList.add(usptTaskTaxitmWctHist);
		}
		//전 내용 셋팅
		usptTaskTaxitmWctHistDto.setUsptTaskTaxitmWctHistBeforeList(usptTaskTaxitmWctHistBeforeList);

		/********************************************** 과제세목별사업비변경 변경 후 정보*/
		List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctHistAfterList = usptTaskTaxitmWctHistDao.selectMaxHistList(cnvnChangeReqId,cnvnChangeParam.getBsnsYear());

		//후 내용 셋팅
		usptTaskTaxitmWctHistDto.setUsptTaskTaxitmWctHistAfterList(usptTaskTaxitmWctHistAfterList);

		return usptTaskTaxitmWctHistDto;
	}
	/**
	 * 협약변경관리 과제세목별사업비변경 신청
	 * changeIemDivCd, cnvnChangeReqId
	 */
	public void modifyTaskTaxitmWctHistApproval(UsptTaskTaxitmWctHistDto usptTaskTaxitmWctHistDto) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/** 협약변경요청ID */
		String cnvnChangeReqId=usptTaskTaxitmWctHistDto.getUsptCnvnChangeReq().getCnvnChangeReqId();
		/** 신청자ID */
		String applcntId = usptTaskTaxitmWctHistDto.getUsptCnvnChangeReq().getApplcntId();
		/**협약변경항목구분 - CHIE06*/
		String changeIemDivCd = Code.changeIemDiv.비목별사업비;

		/** 협약변경요청, 이력테이블 변경*/
		modifyCnvnChangeReqApproval(cnvnChangeReqId,  changeIemDivCd);

		//기본정보 조회-사업계획서ID
		UsptCnvnChangeReq usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);
		/*사업계획서ID 셋팅*/
		String bsnsPlanDocId = usptCnvnChangeReq.getBsnsPlanDocId();

		/************************************************************** 변경*/
		//과제신청사업비변경이력 변경 전 정보 셋팅*/
		List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctHistBeforeList  = usptTaskTaxitmWctHistDto.getUsptTaskTaxitmWctHistBeforeList();
		//과제신청사업비변경이력 변경 후 정보 셋팅*/
		List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctHistAfterList  = usptTaskTaxitmWctHistDto.getUsptTaskTaxitmWctHistAfterList();


		/** 과제세목별사업비변경이력 변경 전 정보 등록*/
		for(UsptTaskTaxitmWctHist usptTaskTaxitmWctHistInfo : usptTaskTaxitmWctHistBeforeList) {
			String taskTaxitmWctHistId = CoreUtils.string.getNewId(Code.prefix.과제세목별사업비변경이력);
			String  confmTaskTaxitmWctHistId = usptTaskTaxitmWctHistAfterList.get(0).getTaskTaxitmWctHistId();

			usptTaskTaxitmWctHistInfo.setTaskTaxitmWctHistId(taskTaxitmWctHistId);
			usptTaskTaxitmWctHistInfo.setConfmTaskTaxitmWctHistId(confmTaskTaxitmWctHistId);
			usptTaskTaxitmWctHistInfo.setCnvnChangeReqId(cnvnChangeReqId);
			usptTaskTaxitmWctHistInfo.setCreatedDt(now);
			usptTaskTaxitmWctHistInfo.setCreatorId(strMemberId);
			usptTaskTaxitmWctHistDao.insert(usptTaskTaxitmWctHistInfo);
		}

		 /** 과제세목별사업비 변경 후 정보 등록*/
		//삭제
		 //(과제세목별사업비 과제신청사업비 일치하지 않아 삭제 후 등록)
		usptTaskTaxitmWctDao.deleteBsnsPlanDocIdAll(bsnsPlanDocId);

		//등록
		int insertCnt = 0;
		UsptTaskTaxitmWct insertParam = null;
		for(UsptTaskTaxitmWctHist input : usptTaskTaxitmWctHistAfterList) {
			insertParam = new UsptTaskTaxitmWct();
			String newTaskTaxitmWctId = CoreUtils.string.getNewId(Code.prefix.과제세목별사업비);
			insertParam.setTaskTaxitmWctId(newTaskTaxitmWctId);		/** 과제세목별사업비ID */
			insertParam.setBsnsPlanDocId(bsnsPlanDocId);				/** 사업계획서ID */
			insertParam.setBsnsYear(input.getBsnsYear());					/**사업년도*/
			insertParam.setWctTaxitmId(input.getWctTaxitmId());		/** 사업비세목ID */
			insertParam.setComputBasisCn(input.getComputBasisCn());/** 산출근거내용 */
			insertParam.setSportBudget(input.getSportBudget());		/** 지원예산 */
			insertParam.setAlotmCash(input.getAlotmCash());			/** 부담금현금 */
			insertParam.setAlotmActhng(input.getAlotmActhng());		/** 부담금현물 */
			insertParam.setCreatedDt(now);
			insertParam.setCreatorId(strMemberId);
			insertParam.setUpdatedDt(now);
			insertParam.setUpdaterId(strMemberId);

			 insertCnt += usptTaskTaxitmWctDao.insert(insertParam);
		}
		/** email, sms */
		if(insertCnt == usptTaskTaxitmWctHistAfterList.size()) {
			CnvnChangeParam param = new  CnvnChangeParam();
			param.setCnvnChangeSttusCd(Code.cnvnChangeSttus.승인);		/* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
			param.setChangeIemDivCd(changeIemDivCd);							/** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
			param.setApplcntId(applcntId);	/** 신청자ID */
			sendMsg(param);
		}
	}
	/****************************************과제세목별사업비변경****************************************************/


	/****************************************협약신청자정보변경이력 start****************************************************/
	/**
	 * 협약변경관리 협약신청자정보변경이력 조회
	 * @param  cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptCnvnApplcntHistDto selectCnvnApplcntHist(HttpServletRequest request, CnvnChangeParam cnvnChangeParam){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.신청자정보;					/**협약변경항목구분 - CHIE07*/

		UsptCnvnApplcntHistDto usptCnvnApplcntHistDto = new UsptCnvnApplcntHistDto();

		/**기본정보 조회*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

		 if(usptBsnsCnvnHist !=null) {
			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptBsnsCnvnHist.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnHist.getAttachmentGroupId());
				usptCnvnApplcntHistDto.setAttachFileList(list);
				 //신청시 등록에 필요한 첨부파일그룹ID 셋팅
				 usptCnvnChangeReq.setAttachmentGroupId(usptBsnsCnvnHist.getAttachmentGroupId());
			}
		 }
		//셋팅
		 usptCnvnApplcntHistDto.setUsptCnvnChangeReq(usptCnvnChangeReq);

		/********************************************** 협약신청자정보변경이력 변경 전 정보*/
		/**사업계획신청자 정보 조회(bsnsPlanDocId)*/
		UsptBsnsPlanApplcnt usptBsnsPlanApplcnt = new UsptBsnsPlanApplcnt();
		usptBsnsPlanApplcnt.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		usptBsnsPlanApplcnt = usptBsnsPlanApplcntDao.selectOne(usptBsnsPlanApplcnt);

		UsptCnvnApplcntHist usptCnvnApplcntHistBefore = new UsptCnvnApplcntHist();
		usptCnvnApplcntHistBefore.setApplcntNm(usptBsnsPlanApplcnt.getApplcntNm());	/** 신청자명 */
		usptCnvnApplcntHistBefore.setEncMbtlnum(usptBsnsPlanApplcnt.getMbtlnum());		/** 복호화된 휴대폰번호 */
		usptCnvnApplcntHistBefore.setEncEmail(usptBsnsPlanApplcnt.getEmail());				/** 복호화된 이메일 */
		usptCnvnApplcntHistBefore.setEncBrthdy(usptBsnsPlanApplcnt.getBrthdy());			/** 복호화된 생년월일 */
		usptCnvnApplcntHistBefore.setIndvdlBsnmDivCd(usptBsnsPlanApplcnt.getIndvdlBsnmDivCd());					/** 개인사업자구분코드 */
		usptCnvnApplcntHistBefore.setChargerNm(usptBsnsPlanApplcnt.getChargerNm());	/** 담당자명 */
		usptCnvnApplcntHistBefore.setCeoNm(usptBsnsPlanApplcnt.getCeoNm());				/** 대표자명 */
		usptCnvnApplcntHistBefore.setBizrno(usptBsnsPlanApplcnt.getBizrno());					/** 사업자번호(기업회원) */
		//전 내용 셋팅
		usptCnvnApplcntHistDto.setUsptCnvnApplcntHistBefore(usptCnvnApplcntHistBefore);

		/********************************************** 협약신청자정보변경이력 변경 후 정보*/
		/**협약신청자정보변경이력*/
		UsptCnvnApplcntHist usptCnvnApplcntHistAfter = usptCnvnApplcntHistDao.selectMaxHist(cnvnChangeReqId);
		usptCnvnApplcntHistAfter.setEncMbtlnum(usptCnvnApplcntHistAfter.getMbtlnum());		/** 복호화된 휴대폰번호 */
		usptCnvnApplcntHistAfter.setEncEmail(usptCnvnApplcntHistAfter.getEmail());				/** 복호화된 이메일 */
		usptCnvnApplcntHistAfter.setEncBrthdy(usptCnvnApplcntHistAfter.getBrthdy());			/** 복호화된 생년월일 */
		//후 내용 셋팅
		usptCnvnApplcntHistDto.setUsptCnvnApplcntHistAfter(usptCnvnApplcntHistAfter);

		/* 개인정보 조회 이력 생성  --------------------------------------------------------------------------------*/
		// 신청자정보 전 이력 생성
		LogIndvdlInfSrch cnvnApplcntHistBefore = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("협약변경 관리 상세조회 - 신청자정보 변경 전")
				.trgterId(usptCnvnChangeReq.getMemberId())
				.email(usptCnvnApplcntHistBefore.getEmail())
				.birthday(usptCnvnApplcntHistBefore.getBrthdy())
				.mobileNo(usptCnvnApplcntHistBefore.getMbtlnum())
				.build();
		indvdlInfSrchUtils.insert(cnvnApplcntHistBefore);
		// 신청자정보 이력 생성
		LogIndvdlInfSrch cnvnApplcntHistAfter  = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("협약변경 관리 상세조회 - 신청자정보 변경 후")
				.trgterId(usptCnvnChangeReq.getMemberId())
				.email(usptCnvnApplcntHistAfter.getEmail())
				.birthday(usptCnvnApplcntHistAfter.getBrthdy())
				.mobileNo(usptCnvnApplcntHistAfter.getMbtlnum())
				.build();
		indvdlInfSrchUtils.insert(cnvnApplcntHistAfter);


		return usptCnvnApplcntHistDto;
	}
	/**
	 * 협약변경관리 협약신청자정보변경이력 승인
	 * changeIemDivCd, cnvnChangeReqId
	 */
	public void modifyCnvnApplcntHistApproval(UsptCnvnApplcntHistDto usptCnvnApplcntHistDto) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/** 협약변경요청ID */
		String cnvnChangeReqId=usptCnvnApplcntHistDto.getUsptCnvnChangeReq().getCnvnChangeReqId();
		/** 신청자ID */
		String applcntId = usptCnvnApplcntHistDto.getUsptCnvnChangeReq().getApplcntId();
		/**협약변경항목구분 - CHIE07*/
		String changeIemDivCd = Code.changeIemDiv.신청자정보;

		/** 협약변경요청, 이력테이블 변경*/
		modifyCnvnChangeReqApproval(cnvnChangeReqId,  changeIemDivCd);

		//기본정보 조회-사업계획서ID
		UsptCnvnChangeReq usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);
		/*사업계획서ID 셋팅*/
		String bsnsPlanDocId = usptCnvnChangeReq.getBsnsPlanDocId();

		/************************************************************** 변경*/
		//협약신청자정보변경이력 변경 전 정보 셋팅*/
		UsptCnvnApplcntHist usptCnvnApplcntHistBefore  = usptCnvnApplcntHistDto.getUsptCnvnApplcntHistBefore();
		//협약신청자정보변경이력 변경 후 정보 셋팅*/
		UsptCnvnApplcntHist usptCnvnApplcntHistAfter  = usptCnvnApplcntHistDto.getUsptCnvnApplcntHistAfter();

		/******************************************************** 협약신청자정보변경이력 변경 전 정보 등록*/
		String newCnvnApplcntHistId = CoreUtils.string.getNewId(Code.prefix.협약신청자정보변경이력);
		String  confmCnvnApplcntHistId = usptCnvnApplcntHistAfter.getCnvnApplcntHistId();

		usptCnvnApplcntHistBefore.setCnvnApplcntHistId(newCnvnApplcntHistId);
		usptCnvnApplcntHistBefore.setCnvnChangeReqId(cnvnChangeReqId);
		usptCnvnApplcntHistBefore.setConfmCnvnApplcntHistId(confmCnvnApplcntHistId);
		if(CoreUtils.string.isNotBlank(usptCnvnApplcntHistBefore.getEncBrthdy())) {
			usptCnvnApplcntHistBefore.setEncBrthdy(aes256.encrypt(usptCnvnApplcntHistBefore.getEncBrthdy(), newCnvnApplcntHistId));			/** 암호화된 생년월일 */
		}
		if(CoreUtils.string.isNotBlank(usptCnvnApplcntHistBefore.getEncMbtlnum())) {
			usptCnvnApplcntHistBefore.setEncMbtlnum(aes256.encrypt(usptCnvnApplcntHistBefore.getEncMbtlnum(), newCnvnApplcntHistId));		/** 암호화된 휴대폰번호 */
		}
		if(CoreUtils.string.isNotBlank(usptCnvnApplcntHistBefore.getEncEmail())) {
			usptCnvnApplcntHistBefore.setEncEmail(aes256.encrypt(usptCnvnApplcntHistBefore.getEncEmail(), newCnvnApplcntHistId));				/** 암호화된 이메일 */
		}
		usptCnvnApplcntHistBefore.setCreatedDt(now);
		usptCnvnApplcntHistBefore.setCreatorId(strMemberId);
		usptCnvnApplcntHistDao.insert(usptCnvnApplcntHistBefore);

		/******************************************************** 협약신청자정보변경이력 변경 후 정보 등록*/
		/**사업계획신청자 정보 조회(bsnsPlanDocId)*/
		UsptBsnsPlanApplcnt usptBsnsPlanApplcnt = new UsptBsnsPlanApplcnt();
		usptBsnsPlanApplcnt.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		usptBsnsPlanApplcnt = usptBsnsPlanApplcntDao.selectOne(usptBsnsPlanApplcnt);
		// 사업계획신청자ID-기존(update에서 암호화 시 사용)
		String bsnsPlanApplcntIdUp = usptBsnsPlanApplcnt.getBsnsPlanApplcntId();

		usptBsnsPlanApplcnt.setBsnsPlanDocId(bsnsPlanDocId);
		usptBsnsPlanApplcnt.setApplcntNm(usptCnvnApplcntHistAfter.getApplcntNm());			/** 신청자명 */
		usptBsnsPlanApplcnt.setGenderCd(usptCnvnApplcntHistAfter.getGenderCd());				/** 성별코드(G:GENDER) */
		if(CoreUtils.string.isNotBlank(usptCnvnApplcntHistAfter.getEncBrthdy())) {
			usptBsnsPlanApplcnt.setEncBrthdy(aes256.encrypt(usptCnvnApplcntHistAfter.getEncBrthdy(), bsnsPlanApplcntIdUp));			/** 암호화된 생년월일 */
		}
		if(CoreUtils.string.isNotBlank(usptCnvnApplcntHistAfter.getEncMbtlnum())) {
			usptBsnsPlanApplcnt.setEncMbtlnum(aes256.encrypt(usptCnvnApplcntHistAfter.getEncMbtlnum(), bsnsPlanApplcntIdUp));	/** 암호화된 휴대폰번호 */
		}
		if(CoreUtils.string.isNotBlank(usptCnvnApplcntHistAfter.getEncEmail())) {
			usptBsnsPlanApplcnt.setEncEmail(aes256.encrypt(usptCnvnApplcntHistAfter.getEncEmail(), bsnsPlanApplcntIdUp));				/** 암호화된 이메일 */
		}
		usptBsnsPlanApplcnt.setNativeYn(usptCnvnApplcntHistAfter.getNativeYn());						/** 내국인여부 */
		usptBsnsPlanApplcnt.setIndvdlBsnmDivCd(usptCnvnApplcntHistAfter.getIndvdlBsnmDivCd());	/** 개인사업자구분코드(INDVDL_BSNM_DIV) */
		usptBsnsPlanApplcnt.setChargerNm(usptCnvnApplcntHistAfter.getChargerNm());					/** 담당자명 */
		usptBsnsPlanApplcnt.setCeoNm(usptCnvnApplcntHistAfter.getCeoNm());							 /** 대표자명 */
		usptBsnsPlanApplcnt.setBizrno(usptCnvnApplcntHistAfter.getBizrno());								 /** 사업자번호(기업회원) */

		usptBsnsPlanApplcnt.setUpdatedDt(now);
		usptBsnsPlanApplcnt.setUpdaterId(strMemberId);
		int updateCnt = usptBsnsPlanApplcntDao.update(usptBsnsPlanApplcnt);

		/** email, sms */
		if(updateCnt >0 ) {
			CnvnChangeParam param = new  CnvnChangeParam();
			param.setCnvnChangeSttusCd(Code.cnvnChangeSttus.승인);		/* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
			param.setChangeIemDivCd(changeIemDivCd);							/** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
			param.setApplcntId(applcntId);		/** 신청자ID */
			sendMsg(param);
		}


	}
	/****************************************협약신청자정보변경이력****************************************************/

	/****************************************과제책임자변경이력 start****************************************************/
	/**
	 * 협약변경관리 과제책임자변경이력 조회
	 * @param    cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptTaskRspnberHistDto selectTaskRspnberHist(CnvnChangeParam cnvnChangeParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.과제책임자;					/**협약변경항목구분 - CHIE08*/

		UsptTaskRspnberHistDto usptTaskRspnberHistDto = new UsptTaskRspnberHistDto();

		/**기본정보 조회*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

		 if(usptBsnsCnvnHist !=null) {
			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptBsnsCnvnHist.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnHist.getAttachmentGroupId());
				usptTaskRspnberHistDto.setAttachFileList(list);
				 //신청시 등록에 필요한 첨부파일그룹ID 셋팅
				 usptCnvnChangeReq.setAttachmentGroupId(usptBsnsCnvnHist.getAttachmentGroupId());
			}
		 }
		//셋팅
		 usptTaskRspnberHistDto.setUsptCnvnChangeReq(usptCnvnChangeReq);

		 /********************************************** 과제책임자변경이력 변경 전 정보*/
		/**과제책임자 정보 조회(bsnsPlanDocId)*/
		UsptTaskRspnber usptTaskRspnber = new UsptTaskRspnber();
		usptTaskRspnber.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		UsptTaskRspnber usptTaskPartcptsBefore= usptTaskRspnberDao.selectOne(usptTaskRspnber);

		UsptTaskRspnberHist usptTaskRspnberHistBefore = new UsptTaskRspnberHist();
		usptTaskRspnberHistBefore.setRspnberNm(usptTaskPartcptsBefore.getRspnberNm());		/** 책임자명 */
		usptTaskRspnberHistBefore.setEncBrthdy(usptTaskPartcptsBefore.getBrthdy());					/** 복호화된 생년월일 */
		usptTaskRspnberHistBefore.setEncMbtlnum(usptTaskPartcptsBefore.getMbtlnum());			/** 복호화된 휴대폰번호 */
		usptTaskRspnberHistBefore.setEncEmail(usptTaskPartcptsBefore.getEmail());					/** 복호화된 이메일 */
		usptTaskRspnberHistBefore.setDeptNm(usptTaskPartcptsBefore.getDeptNm());		 		/** 부서명 */
		usptTaskRspnberHistBefore.setClsfNm(usptTaskPartcptsBefore.getClsfNm());					/** 직급명 */
		usptTaskRspnberHistBefore.setAdres(usptTaskPartcptsBefore.getAdres());						/** 주소 */
		usptTaskRspnberHistBefore.setEncTelno(usptTaskPartcptsBefore.getTelno());					/** 복호화된 전화번호 */
		usptTaskRspnberHistBefore.setEncFxnum(usptTaskPartcptsBefore.getFxnum());					/** 복호화된 팩스번호 */
		usptTaskRspnberHistBefore.setTlsyRegistNo(usptTaskPartcptsBefore.getTlsyRegistNo());		/** 과학기술인등록번호 */
		//전 내용 셋팅
		usptTaskRspnberHistDto.setUsptTaskRspnberHistBefore(usptTaskRspnberHistBefore);

		/********************************************** 과제책임자변경이력 변경 후 정보*/
		/**과제책임자변경이력*/
		UsptTaskRspnberHist usptTaskRspnberHistAfter = usptTaskRspnberHistDao.selectMaxHist(cnvnChangeReqId);

		usptTaskRspnberHistAfter.setEncBrthdy(usptTaskRspnberHistAfter.getBrthdy());			/** 복호화된 생년월일*/
		usptTaskRspnberHistAfter.setEncEmail(usptTaskRspnberHistAfter.getEmail());				/** 복호화된 이메일*/
		usptTaskRspnberHistAfter.setEncFxnum(usptTaskRspnberHistAfter.getFxnum());			/** 복호화된 팩스번호*/
		usptTaskRspnberHistAfter.setEncMbtlnum(usptTaskRspnberHistAfter.getMbtlnum());		/** 복호화된 휴대폰번호*/
		usptTaskRspnberHistAfter.setEncTelno(usptTaskRspnberHistAfter.getTelno());				/** 복호화된 전화번호*/

		//후 내용 셋팅
		usptTaskRspnberHistDto.setUsptTaskRspnberHistAfter(usptTaskRspnberHistAfter);

		return usptTaskRspnberHistDto;
	}
	/**
	 * 협약변경관리 과제책임자변경이력 승인
	 * cnvnChangeReqId
	 */
	public void modifyTaskRspnberHistApproval(UsptTaskRspnberHistDto usptTaskRspnberHistDto) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/** 협약변경요청ID */
		String cnvnChangeReqId=usptTaskRspnberHistDto.getUsptCnvnChangeReq().getCnvnChangeReqId();
		/** 신청자ID */
		String applcntId = usptTaskRspnberHistDto.getUsptCnvnChangeReq().getApplcntId();
		/**협약변경항목구분 - CHIE08*/
		String changeIemDivCd = Code.changeIemDiv.과제책임자;

		/** 협약변경요청, 이력테이블 변경*/
		modifyCnvnChangeReqApproval(cnvnChangeReqId,  changeIemDivCd);

		//기본정보 조회-사업계획서ID
		UsptCnvnChangeReq usptCnvnChangeReq = selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd);
		/*사업계획서ID 셋팅*/
		String bsnsPlanDocId = usptCnvnChangeReq.getBsnsPlanDocId();

		/************************************************************** 변경*/
		//과제책임자변경이력 변경 전 정보 셋팅*/
		UsptTaskRspnberHist usptTaskRspnberHistBefore  = usptTaskRspnberHistDto.getUsptTaskRspnberHistBefore();
		//과제책임자변경이력 변경 후 정보 셋팅*/
		UsptTaskRspnberHist usptTaskRspnberHistAfter  = usptTaskRspnberHistDto.getUsptTaskRspnberHistAfter();

		/******************************************************** 과제책임자변경이력 변경 전 정보 등록*/
		String newTaskPartcptsHistId = CoreUtils.string.getNewId(Code.prefix.과제책임자변경이력);
		String  confmTaskRspnberHistId = usptTaskRspnberHistAfter.getTaskRspnberHistId();

		usptTaskRspnberHistBefore.setTaskRspnberHistId(newTaskPartcptsHistId);				/** 과제책임자변경이력ID */
		usptTaskRspnberHistBefore.setCnvnChangeReqId(cnvnChangeReqId);					/** 협약변경요청ID */
		usptTaskRspnberHistBefore.setConfmTaskRspnberHistId(confmTaskRspnberHistId);	/** 승인과제책임자변경이력ID */
		usptTaskRspnberHistBefore.setEncBrthdy(aes256.encrypt(usptTaskRspnberHistBefore.getEncBrthdy(), newTaskPartcptsHistId));			/** 암호화된 생년월일 */
		usptTaskRspnberHistBefore.setEncMbtlnum(aes256.encrypt(usptTaskRspnberHistBefore.getEncMbtlnum(), newTaskPartcptsHistId));	/** 암호화된 휴대폰번호 */
		usptTaskRspnberHistBefore.setEncEmail(aes256.encrypt(usptTaskRspnberHistBefore.getEncEmail(), newTaskPartcptsHistId));				/** 암호화된 이메일 */
		usptTaskRspnberHistBefore.setEncTelno(aes256.encrypt(usptTaskRspnberHistBefore.getEncTelno(), newTaskPartcptsHistId));				/** 암호화된 전화번호 */
		usptTaskRspnberHistBefore.setEncFxnum(aes256.encrypt(usptTaskRspnberHistBefore.getEncFxnum(), newTaskPartcptsHistId));			/** 암호화된 팩스번호 */
		usptTaskRspnberHistBefore.setCreatedDt(now);
		usptTaskRspnberHistBefore.setCreatorId(strMemberId);
		usptTaskRspnberHistDao.insert(usptTaskRspnberHistBefore);

		/******************************************************** 과제책임자변경이력 변경 후 정보 등록*/

		/**과제책임자 정보 조회(bsnsPlanDocId)*/
		UsptTaskRspnber usptTaskRspnberUp = new UsptTaskRspnber();
		usptTaskRspnberUp.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		usptTaskRspnberUp= usptTaskRspnberDao.selectOne(usptTaskRspnberUp);
		String taskRspnberHistIdUp = usptTaskRspnberUp.getTaskRspnberId();

		System.out.println("===================================================");
		System.out.println(usptTaskRspnberHistAfter.toString());
		System.out.println("===================================================");
		UsptTaskRspnber usptTaskRspnber = new UsptTaskRspnber();
		usptTaskRspnber.setBsnsPlanDocId(bsnsPlanDocId);
		usptTaskRspnber.setRspnberNm(usptTaskRspnberHistAfter.getRspnberNm());		/** 책임자명 */
		usptTaskRspnber.setEncBrthdy(aes256.encrypt(usptTaskRspnberHistAfter.getEncBrthdy(), taskRspnberHistIdUp));	/** 암호화된 담당자 생년월일 */
		usptTaskRspnber.setEncMbtlnum(aes256.encrypt(usptTaskRspnberHistAfter.getEncMbtlnum(), taskRspnberHistIdUp));	/** 암호화된 담당자 휴대폰번호 */
		usptTaskRspnber.setEncEmail(aes256.encrypt(usptTaskRspnberHistAfter.getEncEmail(), taskRspnberHistIdUp));			/** 암호화된 담당자 이메일 */
		usptTaskRspnber.setDeptNm(usptTaskRspnberHistAfter.getDeptNm());		 			/** 부서명 */
		usptTaskRspnber.setClsfNm(usptTaskRspnberHistAfter.getClsfNm());						/** 직급명 */
		usptTaskRspnber.setAdres(usptTaskRspnberHistAfter.getAdres());						/** 주소 */
		usptTaskRspnber.setEncTelno(aes256.encrypt(usptTaskRspnberHistAfter.getEncTelno(), taskRspnberHistIdUp));		/** 암호화된 담당자 전화번호 */
		usptTaskRspnber.setEncFxnum(aes256.encrypt(usptTaskRspnberHistAfter.getEncFxnum(), taskRspnberHistIdUp));	/** 암호화된 담당자 팩스번호 */
		usptTaskRspnber.setTlsyRegistNo(usptTaskRspnberHistAfter.getTlsyRegistNo());										/** 과학기술인등록번호 */
		usptTaskRspnber.setUpdatedDt(now);
		usptTaskRspnber.setUpdaterId(strMemberId);
		int updateCnt = usptTaskRspnberDao.updateByBsnsPlanDocId(usptTaskRspnber);

		/** email, sms */
		if(updateCnt >0 ) {
			CnvnChangeParam param = new  CnvnChangeParam();
			param.setCnvnChangeSttusCd(Code.cnvnChangeSttus.승인);		/* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
			param.setChangeIemDivCd(changeIemDivCd);							/** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
			param.setApplcntId(applcntId);		/** 신청자ID */
			sendMsg(param);
		}

	}
	/****************************************과제책임자변경이력****************************************************/

	/** *********************************************************  협약변경 공통함수 **************************************************/

	/**
	 * 협약변경관리 기본정보 조회
	 * @param BsnsCnvnId, CnvnChangeReqId,ChangeIemDivCd
	 * @return
	 */
	public UsptCnvnChangeReq selectCnvnChangeBaseInfo(String cnvnChangeReqId,  String changeIemDivCd) {

		/*기본정보 조회*/
		UsptCnvnChangeReq usptCnvnChangeReq = usptCnvnChangeReqDao.selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd ) ;
		if(usptCnvnChangeReq == null){
			throw new InvalidationException("조회된 협약변경 내역이 없습니다.");
		}
		usptCnvnChangeReq.setEncEmail(usptCnvnChangeReq.getEmail());
		usptCnvnChangeReq.setEncMbtlnum(usptCnvnChangeReq.getMbtlnum());

		return usptCnvnChangeReq;
	}

	/**
	 * 협약변경관리 협약변경요청 신청 승인
	 * @param cnvnChangeReqId , changeIemDivCd
	 * @return
	 */

	public void modifyCnvnChangeReqApproval(String cnvnChangeReqId,  String changeIemDivCd) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();

		 /** 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
		String  cnvnChangeSttusCd = Code.cnvnChangeSttus.승인;
		 /** 협약변경유형(CNVN_CHANGE_TYPE) */
		String  cnvnChangeTypeCd ="";
		/**첨부파일 그룹ID*/
		String attachmentGroupId ="";
		/**사유*/
		String resnCn = "승인 처리되었습니다.";

		if(CoreUtils.string.equals( Code.changeIemDiv.신청자정보, changeIemDivCd)  || CoreUtils.string.equals( Code.changeIemDiv.과제책임자, changeIemDivCd)){
			cnvnChangeTypeCd = Code.cnvnChangeType.통보;
		}else {
			cnvnChangeTypeCd = Code.cnvnChangeType.승인;
		}
		/************** 협약변경*/
		usptCnvnChangeReqDao.updateCnvnChangeSttus(cnvnChangeReqId, cnvnChangeSttusCd);

		/************** 사업협약변경이력*/
		//사업협약변경이력 최신정보 조회(첨부파일 그룹ID조회)
		 UsptBsnsCnvnHist  usptBsnsCnvnHistFileGrpupId = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);
		if(usptBsnsCnvnHistFileGrpupId !=null) {
			 /*첨부파일그룹ID*/
			 attachmentGroupId = usptBsnsCnvnHistFileGrpupId.getAttachmentGroupId();
		}

		UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		usptBsnsCnvnHist.setBsnsCnvnHistId(CoreUtils.string.getNewId(Code.prefix.협약변경이력));
		usptBsnsCnvnHist.setCnvnChangeReqId(cnvnChangeReqId);			 /*협약변경요청ID*/
		usptBsnsCnvnHist.setChangeIemDivCd(changeIemDivCd);				/*협약변경항목구분코드(G:CHANGE_IEM_DIV)*/
		usptBsnsCnvnHist.setCnvnChangeSttusCd(cnvnChangeSttusCd);		/*협약변경상태코드(G:CNVN_CHANGE_STTUS)*/
		usptBsnsCnvnHist.setCnvnChangeTypeCd(cnvnChangeTypeCd);		/* 협약변경유형(CNVN_CHANGE_TYPE) */
		usptBsnsCnvnHist.setResnCn(resnCn);										/*사유내용*/
		usptBsnsCnvnHist.setAttachmentGroupId(attachmentGroupId);		/*첨부파일그룹ID*/
		usptBsnsCnvnHist.setCreatedDt(now);
		usptBsnsCnvnHist.setCreatorId(strMemberId);
		usptBsnsCnvnHistDao.insert(usptBsnsCnvnHist);
	}

	/**************************************** 반려  ****************************************************/
	/*
	 * 협약변경관리 수행기관신분변경 반려
	 * cnvnChangeReqId ,changeIemDivCd,  resnCn(사유)
	 */
	public void modifyCnvnChangeReject(CnvnChangeParam cnvnChangeParam	) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/* 현재 시간*/
		Date now = new Date();
		/* 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/* 협약변경요청ID */
		String cnvnChangeReqId = cnvnChangeParam.getCnvnChangeReqId();
		/*협약변경항목구분코드**/
		String changeIemDivCd = cnvnChangeParam.getChangeIemDivCd();
		 /* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
		String  cnvnChangeSttusCd = Code.cnvnChangeSttus.반려;
		/*사유*/
		String resnCn = cnvnChangeParam.getResnCn();
		 /* 협약변경유형(CNVN_CHANGE_TYPE) */
		String  cnvnChangeTypeCd ="";
		/*첨부파일 그룹ID*/
		String attachmentGroupId ="";

		 if(CoreUtils.string.equals( Code.changeIemDiv.신청자정보, changeIemDivCd)  || CoreUtils.string.equals( Code.changeIemDiv.과제책임자, changeIemDivCd)){
				cnvnChangeTypeCd = Code.cnvnChangeType.통보;
		}else {
			cnvnChangeTypeCd = Code.cnvnChangeType.승인;
		}

		 /************** 협약변경*/
		usptCnvnChangeReqDao.updateCnvnChangeSttus(cnvnChangeReqId, cnvnChangeSttusCd);

		/************** 사업협약변경이력*/
		//첨부파일 조회(사업협약변경이력 최신정보 조회-첨부파일그룹ID 조회)
		 UsptBsnsCnvnHist  usptBsnsCnvnHistFile =usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);
		 if(usptBsnsCnvnHistFile != null) {
			 attachmentGroupId  = usptBsnsCnvnHistFile.getAttachmentGroupId();
		 }

		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 usptBsnsCnvnHist.setBsnsCnvnHistId(CoreUtils.string.getNewId(Code.prefix.협약변경이력));
		 usptBsnsCnvnHist.setCnvnChangeReqId(cnvnChangeReqId);
		 usptBsnsCnvnHist.setChangeIemDivCd(changeIemDivCd);
		 usptBsnsCnvnHist.setCnvnChangeSttusCd(cnvnChangeSttusCd);
		 usptBsnsCnvnHist.setCnvnChangeTypeCd(cnvnChangeTypeCd);
		 usptBsnsCnvnHist.setResnCn(resnCn);			//사유
		 usptBsnsCnvnHist.setAttachmentGroupId(attachmentGroupId);
		 usptBsnsCnvnHist.setCreatedDt(now);
		 usptBsnsCnvnHist.setCreatorId(strMemberId);
		 usptBsnsCnvnHistDao.insert(usptBsnsCnvnHist);
	}

	/****************************************보완요청  ****************************************************/
	/*
	 * 협약변경관리 수행기관신분변경 보완요청
	 * cnvnChangeReqId ,changeIemDivCd,  resnCn(사유), applcntId(신청자)
	 */
	public void modifyCnvnChangeReason(CnvnChangeParam cnvnChangeParam	) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/* 현재 시간*/
		Date now = new Date();
		/* 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/* 협약변경요청ID */
		String cnvnChangeReqId = cnvnChangeParam.getCnvnChangeReqId();
		/*협약변경항목구분코드**/
		String changeIemDivCd = cnvnChangeParam.getChangeIemDivCd();
		 /* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
		String  cnvnChangeSttusCd = Code.cnvnChangeSttus.보완요청;
		/*사유*/
		String resnCn = cnvnChangeParam.getResnCn();
		 /* 협약변경유형(CNVN_CHANGE_TYPE) */
		String  cnvnChangeTypeCd ="";
		/*첨부파일 그룹ID*/
		String attachmentGroupId ="";


		 if(CoreUtils.string.equals( Code.changeIemDiv.신청자정보, changeIemDivCd)  || CoreUtils.string.equals( Code.changeIemDiv.과제책임자, changeIemDivCd)){
				cnvnChangeTypeCd = Code.cnvnChangeType.통보;
		}else {
			cnvnChangeTypeCd = Code.cnvnChangeType.승인;
		}

		/**협약변경요청*/
		usptCnvnChangeReqDao.updateCnvnChangeSttus(cnvnChangeReqId, cnvnChangeSttusCd);

		 /**사업협약변경이력*/
		//첨부파일 조회(사업협약변경이력 최신정보 조회-첨부파일그룹ID 조회)
		 UsptBsnsCnvnHist  usptBsnsCnvnHistFile = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);
		 if(usptBsnsCnvnHistFile != null) {
			 attachmentGroupId  = usptBsnsCnvnHistFile.getAttachmentGroupId();
		 }

		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 usptBsnsCnvnHist.setBsnsCnvnHistId(CoreUtils.string.getNewId(Code.prefix.협약변경이력));
		 usptBsnsCnvnHist.setCnvnChangeReqId(cnvnChangeReqId);
		 usptBsnsCnvnHist.setChangeIemDivCd(changeIemDivCd);
		 usptBsnsCnvnHist.setCnvnChangeSttusCd(cnvnChangeSttusCd);
		 usptBsnsCnvnHist.setCnvnChangeTypeCd(cnvnChangeTypeCd);
		 usptBsnsCnvnHist.setResnCn(resnCn);			//사유
		 usptBsnsCnvnHist.setAttachmentGroupId(attachmentGroupId);
		 usptBsnsCnvnHist.setCreatedDt(now);
		 usptBsnsCnvnHist.setCreatorId(strMemberId);
		 int insertCnt = usptBsnsCnvnHistDao.insert(usptBsnsCnvnHist);

		if(insertCnt>0) {
			CnvnChangeParam param = new  CnvnChangeParam();
			param.setCnvnChangeSttusCd(cnvnChangeSttusCd);			/* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
			param.setChangeIemDivCd(changeIemDivCd);					/** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
			param.setApplcntId(cnvnChangeParam.getApplcntId());		/** 신청자ID */
			param.setResnCn(resnCn);			//사유
			sendMsg(param);
		}

	}

	/****************************************협약변경항목구분별 이력 조회 ****************************************************/
	/**
	 * 협약변경항목구분별 이력 조회
	 * @param  cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public List <UsptBsnsCnvnHist> selectBsnsCnvnHist(CnvnChangeParam cnvnChangeParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeParam.getCnvnChangeReqId();		/**협약변경요청ID*/
		String changeIemDivCd = cnvnChangeParam.getChangeIemDivCd();			/**협약변경항목구분*/

		List<UsptBsnsCnvnHist> list = usptBsnsCnvnHistDao.selectBsnsCnvnHist(cnvnChangeReqId, changeIemDivCd);
		for(UsptBsnsCnvnHist outPut : list ) {
			CmmtMember cmmtMember = 	cmmtMemberDao.selectLoginIdNm(outPut.getMemberId());
			outPut.setMemberId(cmmtMember.getLoginId());
			outPut.setMemberNm(cmmtMember.getMemberNm());
		}

		return list;
	}

	/**
	 * 협약변경관리 첨부파일 일괄 다운
	 * @param cnvnCnclsParam(cnvnFileGroupId)
	 * @return
	 */
	public void getCnvnChangeFileDown(HttpServletResponse response, CnvnChangeParam cnvnChangeParam){
		SecurityUtils.checkWorkerIsInsider();
		if (string.isNotBlank(cnvnChangeParam.getAttachmentGroupId())) {
			attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), cnvnChangeParam.getAttachmentGroupId(), "협약변경 신청 첨부 파일");
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

	/*******************************************************************************************************************************/
	/**
	 * sms or email 전송
	 * @param CnvnCnclsParam
	 * @return
	 */
	public String sendMsg(CnvnChangeParam param) {
		SecurityUtils.checkWorkerIsInsider();
		StringBuffer sbMsg = new StringBuffer();

		if(param == null) {
			throw new InvalidationException("발송대상이 설정되지 않았습니다.");
		}
		String changeIemDivCd  = param.getChangeIemDivCd();
		String cnvnChangeSttusCd = param.getCnvnChangeSttusCd();	/* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
		String memId = param.getApplcntId();									/*신청자ID*/
		String resnCn = param.getResnCn();									 //사유

		String title = "협약변경";
		String mainTitle= "";
		String changeIemDivNm = "";	//변경항목명
		String cnVal		= "";		//발송내용


		CmmtMember memberInfo = cmmtMemberDao.select(memId);
		String memNm	= memberInfo.getMemberNm();		//회원명
		String email		= memberInfo.getEmail();	//email
		String mbtlnum	= memberInfo.getMobileNo();	//휴대폰번호

		EmailResult er = null;
		SmsResult sr = null;

		//변경항목명
		if(CoreUtils.string.equals( Code.changeIemDiv.수행기관신분, changeIemDivCd)){
			changeIemDivNm = "수행기관신분";
		}else if(CoreUtils.string.equals( Code.changeIemDiv.과제정보, changeIemDivCd)){
			changeIemDivNm = "과제정보";
		}else if(CoreUtils.string.equals( Code.changeIemDiv.참여기업, changeIemDivCd)){
			changeIemDivNm = "참여기업";
		}else if(CoreUtils.string.equals( Code.changeIemDiv.참여인력, changeIemDivCd)){
			changeIemDivNm = "참여인력";
		}else if(CoreUtils.string.equals( Code.changeIemDiv.사업비, changeIemDivCd)){
			changeIemDivNm = "사업비";
		}else if(CoreUtils.string.equals( Code.changeIemDiv.비목별사업비, changeIemDivCd)){
			changeIemDivNm = "비목별사업비";
		}else if(CoreUtils.string.equals( Code.changeIemDiv.신청자정보, changeIemDivCd)){
			changeIemDivNm = "신청자정보";
		}else if(CoreUtils.string.equals( Code.changeIemDiv.과제책임자, changeIemDivCd)){
			changeIemDivNm = "과제책임자";
		}

		if( CoreUtils.string.equals( cnvnChangeSttusCd ,Code.cnvnChangeSttus.보완요청)){
			mainTitle = "협약변경 보완요청";
			cnVal = resnCn;			//발송내용

		}else if( CoreUtils.string.equals( cnvnChangeSttusCd, Code.cnvnChangeSttus.승인)){
			mainTitle = "협약변경 승인완료";
			cnVal =  changeIemDivNm+"변경 요청이 승인 처리 되었습니다.";			//발송내용
		}

		//****************************이메일
		String emailCn = CoreUtils.file.readFileString("/form/email/email-bsnsPlan.html");//
		EmailSendParam esParam = new EmailSendParam();
		esParam.setContentHtml(true);
		esParam.setEmailCn(emailCn);
		esParam.setTitle(title);
		esParam.setContentHtml(true);

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


		//********************SMS
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

}