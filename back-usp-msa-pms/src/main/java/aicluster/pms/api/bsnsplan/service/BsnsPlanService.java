package aicluster.pms.api.bsnsplan.service;

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
import aicluster.pms.api.bsnsplan.dto.BsnsPlanDto;
import aicluster.pms.api.bsnsplan.dto.BsnsPlanParam;
import aicluster.pms.api.bsnsplan.dto.BsnsPlanResnDto;
import aicluster.pms.api.bsnsplan.dto.BsnsPlanResnParam;
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.UsptBsnsCnvnDao;
import aicluster.pms.common.dao.UsptBsnsCnvnPrtcmpnySignDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyTaskDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyTaskPartcptsDao;
import aicluster.pms.common.dao.UsptBsnsPlanDocDao;
import aicluster.pms.common.dao.UsptBsnsPlanProcessHistDao;
import aicluster.pms.common.dao.UsptBsnsSlctnDao;
import aicluster.pms.common.dao.UsptTaskPartcptsDao;
import aicluster.pms.common.dao.UsptTaskPrtcmpnyDao;
import aicluster.pms.common.dao.UsptTaskReqstWctDao;
import aicluster.pms.common.dao.UsptTaskRspnberDao;
import aicluster.pms.common.dao.UsptTaskTaxitmWctDao;
import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptBsnsCnvn;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTask;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTaskPartcpts;
import aicluster.pms.common.entity.UsptBsnsPlanDoc;
import aicluster.pms.common.entity.UsptBsnsPlanProcessHist;
import aicluster.pms.common.entity.UsptBsnsSlctn;
import aicluster.pms.common.entity.UsptTaskPartcpts;
import aicluster.pms.common.entity.UsptTaskPrtcmpny;
import aicluster.pms.common.entity.UsptTaskReqstWct;
import aicluster.pms.common.entity.UsptTaskRspnber;
import aicluster.pms.common.entity.UsptTaskTaxitmWct;
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
public class BsnsPlanService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;		/*파일*/
	@Autowired
	private UsptBsnsPlanDocDao usptBsnsPlanDocDao;		/*사업계획서*/
	@Autowired
	private UsptTaskRspnberDao usptTaskRspnberDao;		/*과제책임자*/
	@Autowired
	private UsptTaskPrtcmpnyDao usptTaskPrtcmpnyDao;		/*과제참여기업*/
	@Autowired
	private UsptTaskPartcptsDao usptTaskPartcptsDao;		/*과제참여자*/
	@Autowired
	private UsptTaskReqstWctDao usptTaskReqstWctDao;		/*과제신청사업비*/
	@Autowired
	private UsptTaskTaxitmWctDao usptTaskTaxitmWctDao;		/*과제세목별사업비*/
	@Autowired
	private UsptBsnsPlanProcessHistDao usptBsnsPlanProcessHistDao; /*사업계획서처리이력*/
	@Autowired
	private UsptBsnsPblancApplyTaskDao usptBsnsPblancApplyTaskDao;		/*사업공고 신청 과제*/
	@Autowired
	private UsptBsnsPblancApplyTaskPartcptsDao usptBsnsPblancApplyTaskPartcptsDao;		/*사업공고신청과제참여자*/
	@Autowired
	private UsptBsnsSlctnDao usptBsnsSlctnDao;		/*사업선정대상*/
	@Autowired
	private CmmtMemberDao cmmtMemberDao;	/*회원*/
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private UsptBsnsCnvnDao usptBsnsCnvnDao;		/*사업협약*/
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
	@Autowired
	private UsptBsnsCnvnPrtcmpnySignDao usptBsnsCnvnPrtcmpnySignDao;


	/**
	 * 사업계획서 접수 관리 목록 조회
	 * @param bsnsPlanParam
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	public CorePagination<UsptBsnsPlanDoc> getList(BsnsPlanParam bsnsPlanParam,  Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String insiderId = worker.getMemberId();
		if(page == null) {
			page = 1L;
		}
		if(bsnsPlanParam.getItemsPerPage() == null) {
			bsnsPlanParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = bsnsPlanParam.getItemsPerPage();

		//당담자ID
		bsnsPlanParam.setInsiderId(insiderId);
		//전체건수 조회
		long totalItems = usptBsnsPlanDocDao.selectBsnsPlanDocCnt(bsnsPlanParam);

		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		bsnsPlanParam.setBeginRowNum(info.getBeginRowNum());
		bsnsPlanParam.setItemsPerPage(itemsPerPage);
		List<UsptBsnsPlanDoc> list = usptBsnsPlanDocDao.selectBsnsPlanDocList(bsnsPlanParam);

		CorePagination<UsptBsnsPlanDoc> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 사업계획서목록 제출파일- 전체 다운
	 * @param bsnsPlanParam
	 * @return
	 */
	public void getAllFileDown(HttpServletResponse response, BsnsPlanParam bsnsPlanParam) {
		SecurityUtils.checkWorkerIsInsider();

		List<UsptBsnsPlanDoc> fileList =  usptBsnsPlanDocDao.selectBsnsPlanDocFileList(bsnsPlanParam);

		if(fileList.size()>0){
			String[] attachmentIds = new String[fileList.size()];
			for(int idx=0; idx<fileList.size(); idx++) {
				UsptBsnsPlanDoc fileDto = fileList.get(idx);
				attachmentIds[idx] = fileDto.getAttachmentId();
			}
			attachmentService.downloadFiles(response, config.getDir().getUpload(), attachmentIds, "사업계획서접수 관리_첨부파일");
		}else {
			throw new InvalidationException("요청한 첨부파일이 존재하지 않습니다.");
		}
	}

	/**
	 * 사업계획서 목록 엑셀 다운로드
	 * @param pblancId
	 * @return
	 */
	public List<UsptBsnsPlanDoc> getListExcel(BsnsPlanParam bsnsPlanParam) {

		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String insiderId = worker.getMemberId();

		//당담자ID
		bsnsPlanParam.setInsiderId(insiderId);
		//전체건수 조회
		long totalItems = usptBsnsPlanDocDao.selectBsnsPlanDocCnt(bsnsPlanParam);

		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		bsnsPlanParam.setBeginRowNum(info.getBeginRowNum());
		bsnsPlanParam.setItemsPerPage(totalItems);
		return usptBsnsPlanDocDao.selectBsnsPlanDocList(bsnsPlanParam);
	}

	/**
	 * 사업계획서접수 관리 상세 조회
	 * @param bsnsPlanParam
	 * @return
	 */
	public BsnsPlanDto getBsnsPlanDocInfo(HttpServletRequest request, BsnsPlanParam bsnsPlanParam){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/**조회 리턴**/
		BsnsPlanDto resultBsnsPlanDto = new BsnsPlanDto();

		/**기본정보 조회 - 사업선정대상ID ,  사업계획서ID 조회*/
		UsptBsnsPlanDoc usptBsnsPlanDoc =usptBsnsPlanDocDao.selectBsnsPlanBaseDocInfo(bsnsPlanParam);

		if(usptBsnsPlanDoc == null) {
			throw new InvalidationException("사업계획서 상세 내용이 없습니다.");
		}
		//사업계획서ID
		String bsnsPlanDocId = usptBsnsPlanDoc.getBsnsPlanDocId();

		/** 신청자정보(주관기업)*/
		String strMemberId = usptBsnsPlanDoc.getMemberId();
		//회원정보 조회
		CmmtMember memberInfo = cmmtMemberDao.select(strMemberId);
		//개인
		if(CoreUtils.string.equals(memberInfo.getMemberType(), "INDIVIDUAL")){
			usptBsnsPlanDoc.setMemberType(memberInfo.getMemberType());	//회원구분
			usptBsnsPlanDoc.setMemberNm(memberInfo.getMemberNm());		//신청자명
			usptBsnsPlanDoc.setGender(memberInfo.getGender());					//성별
			usptBsnsPlanDoc.setMbtlnum( memberInfo.getMobileNo());			//휴대폰번호
			usptBsnsPlanDoc.setBrthdy(memberInfo.getBirthday());					//생년월일
			usptBsnsPlanDoc.setEmail(memberInfo.getEmail());						//이메일
		}else {
			//사업자
			usptBsnsPlanDoc.setMemberType("COMPANY");							//회원구분
			usptBsnsPlanDoc.setMemberNm(memberInfo.getMemberNm());		//신청자명(사업자명)
			usptBsnsPlanDoc.setBizrno(memberInfo.getBizrno());						//사업자등록번호
			usptBsnsPlanDoc.setCeoNm(memberInfo.getCeoNm());					//대표자명
			usptBsnsPlanDoc.setChargerNm(memberInfo.getChargerNm());		//담당자명
			usptBsnsPlanDoc.setMbtlnum( memberInfo.getMobileNo());			//휴대폰번호
			usptBsnsPlanDoc.setEmail(memberInfo.getEmail());						//이메일
		}

		//과제정보 사업기간
		Date bsnsBgndt = string.toDate(usptBsnsPlanDoc.getBsnsBgnde());
		Date bsnsEnddt = string.toDate(usptBsnsPlanDoc.getBsnsEndde());
		if (bsnsBgndt != null && bsnsEnddt != null ) {
			Date lastdt = string.toDate(date.getCurrentDate("yyyy") + "1231");
			usptBsnsPlanDoc.setBsnsPd("협약 체결일 ~ " + date.format(bsnsEnddt, "yyyy-MM-dd"));
			int allDiffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, bsnsEnddt);
			if(date.compareDay(bsnsEnddt, lastdt) == 1) {
				int diffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, lastdt);
				usptBsnsPlanDoc.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(lastdt, "yyyy-MM-dd") + "(" + diffMonths + "개월)");
			} else {
				usptBsnsPlanDoc.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
			}
			usptBsnsPlanDoc.setBsnsPdAll(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
		}

		List <UsptTaskPrtcmpny> usptTaskPrtcmpnyList  = new ArrayList<>();
		//컨소시엄여부 true에만 조회
		if(usptBsnsPlanDoc.getCnsrtm()) {
			/**참여기업 조회*/
			UsptTaskPrtcmpny usptTaskPrtcmpny = new UsptTaskPrtcmpny();
			usptTaskPrtcmpny.setBsnsPlanDocId(bsnsPlanDocId);
			usptTaskPrtcmpnyList = usptTaskPrtcmpnyDao.selectList(usptTaskPrtcmpny);
			if(usptTaskPrtcmpnyList.size()>0) {
				for(UsptTaskPrtcmpny resultParam: usptTaskPrtcmpnyList) {
					resultParam.setRegTelno(resultParam.getTelno());
					resultParam.setRegMbtlnum(resultParam.getMbtlnum());
					resultParam.setRegEmail(resultParam.getEmail());
				}
				//참여기업 리턴 셋팅
				resultBsnsPlanDto.setUsptTaskPrtcmpny(usptTaskPrtcmpnyList);
				//사업계획서 참여기업체 총수 셋팅
				usptBsnsPlanDoc.setPartcptnCompanyCnt(new Long(usptTaskPrtcmpnyList.size())); /*참여기업체 총수*/
			}
		}
		//사업계획서 리턴 셋팅
		resultBsnsPlanDto.setUsptBsnsPlanDoc(usptBsnsPlanDoc);

		/**과제책임자 조회*/
		UsptTaskRspnber usptTaskRspnber = new UsptTaskRspnber();
		usptTaskRspnber.setBsnsPlanDocId(bsnsPlanDocId);
		usptTaskRspnber = usptTaskRspnberDao.selectOne(usptTaskRspnber);

		if(usptTaskRspnber != null) {
			//과제책임자 리턴 셋팅
			usptTaskRspnber.setEncMbtlnum(usptTaskRspnber.getMbtlnum());
			usptTaskRspnber.setEncBrthdy(usptTaskRspnber.getBrthdy());
			usptTaskRspnber.setEncTelno(usptTaskRspnber.getTelno());
			usptTaskRspnber.setEncEmail(usptTaskRspnber.getEmail());
			usptTaskRspnber.setEncFxnum(usptTaskRspnber.getFxnum());
			resultBsnsPlanDto.setUsptTaskRspnber(usptTaskRspnber);
		}else {
			//사업공고 신청 과제 조회
			UsptBsnsPblancApplyTask usptBsnsPblancApplyTask = usptBsnsPblancApplyTaskDao.select(usptBsnsPlanDoc.getApplyId());
			UsptTaskRspnber usptTaskRspnberInfo= new UsptTaskRspnber();

			usptTaskRspnberInfo.setRspnberNm(usptBsnsPblancApplyTask.getRspnberNm());	/** 책임자명 */
			usptTaskRspnberInfo.setEncBrthdy(usptBsnsPblancApplyTask.getDecBrthdy()); 			/** 생년월일 */
			usptTaskRspnberInfo.setEncMbtlnum(usptBsnsPblancApplyTask.getDecMbtlnum());		/** 휴대폰번호 */
			usptTaskRspnberInfo.setEncEmail(usptBsnsPblancApplyTask.getDecEmail()); 				/** 이메일 */
			usptTaskRspnberInfo.setDeptNm(usptBsnsPblancApplyTask.getDeptNm());			/** 부서명 */
			usptTaskRspnberInfo.setClsfNm(usptBsnsPblancApplyTask.getOfcpsNm());			/** 직급명 */
			usptTaskRspnberInfo.setAdres(usptBsnsPblancApplyTask.getAdres());					/** 주소 */
			usptTaskRspnberInfo.setEncTelno(usptBsnsPblancApplyTask.getDecTelno());				/** 전화번호 */
			usptTaskRspnberInfo.setEncFxnum(usptBsnsPblancApplyTask.getDecFxnum());  			/** 팩스번호 */
			usptTaskRspnberInfo.setTlsyRegistNo(usptBsnsPblancApplyTask.getSctecrno());	/** 과학기술인등록번호 */
			//과제책임자 리턴 셋팅
			resultBsnsPlanDto.setUsptTaskRspnber(usptTaskRspnberInfo);
		}

		/**참여인력 조회*/
		UsptTaskPartcpts usptTaskPartcpts = new UsptTaskPartcpts();
		usptTaskPartcpts.setBsnsPlanDocId(bsnsPlanDocId);
		List <UsptTaskPartcpts> usptTaskPartcptsList = usptTaskPartcptsDao.selectList(usptTaskPartcpts);
		//조회값 리턴 list 선언
		List<UsptTaskPartcpts> usptTaskPartcptsListReturn = new ArrayList<>();

		if(usptTaskPartcptsList.size()>0) {
			for( UsptTaskPartcpts out :  usptTaskPartcptsList) {
				out.setEncMbtlnum(out.getMbtlnum());	  			/** 휴대폰번호 */
				out.setEncBrthdy(out.getBrthdy());					/** 생년월일 */
				usptTaskPartcptsListReturn.add(out);
			}
			//참여인력 리턴 셋팅
			resultBsnsPlanDto.setUsptTaskPartcpts(usptTaskPartcptsListReturn);
		}else{
			List<UsptBsnsPblancApplyTaskPartcpts> usptBsnsPblancApplyTaskPartcptsList = usptBsnsPblancApplyTaskPartcptsDao.selectList(usptBsnsPlanDoc.getApplyId());
			//조회값 리턴 list 선언
			List<UsptTaskPartcpts> usptTaskPartcptsInfoList = new ArrayList<>();

			for( UsptBsnsPblancApplyTaskPartcpts out :  usptBsnsPblancApplyTaskPartcptsList) {
				UsptTaskPartcpts usptTaskPartcptsReturn = new UsptTaskPartcpts();

				//회원정보 조회
//				CmmtMember memInfo = cmmtMemberDao.select(usptBsnsPlanDoc.getMemberId());

// 				usptTaskPartcptsReturn.setMemberId(usptBsnsPlanDoc.getMemberId());	//소속기관에 주관기업의 업체를 기본으로 셋팅
// 				usptTaskPartcptsReturn.setMemberNm(memInfo.getMemberNm());			//소속기관에 주관기업의 업체를 기본으로 셋팅
				usptTaskPartcptsReturn.setPartcptsNm(out.getPartcptsNm());				/** 참여자명 */
				usptTaskPartcptsReturn.setChrgRealmNm(out.getChrgRealmNm());		/** 담당분야명 */
				usptTaskPartcptsReturn.setEncMbtlnum(out.getDecMbtlnum());	  			/** 휴대폰번호 */
				usptTaskPartcptsReturn.setEncBrthdy(out.getDecBrthdy());						/** 생년월일 */
				usptTaskPartcptsReturn.setPartcptnRate(out.getPartcptnRate());			/** 참여율 */
				usptTaskPartcptsInfoList.add(usptTaskPartcptsReturn);
			}
			//참여인력 리턴 셋팅
			resultBsnsPlanDto.setUsptTaskPartcpts(usptTaskPartcptsInfoList);
		}


		/**과제신청사업비(신청예산) 조회*/
		UsptTaskReqstWct usptTaskReqstWct = new UsptTaskReqstWct();
		usptTaskReqstWct.setBsnsPlanDocId(bsnsPlanDocId);
		List <UsptTaskReqstWct> usptTaskReqstWctList = usptTaskReqstWctDao.selectList(usptTaskReqstWct);
		if(usptTaskReqstWctList.size()>0 ) {
			//과제신청사업비 리턴 셋팅
			resultBsnsPlanDto.setUsptTaskReqstWct(usptTaskReqstWctList);
		}else {
			//	사업선정대상 예산 조회
			List <UsptBsnsSlctn> usptBsnsSlctnList = usptBsnsSlctnDao.selectList(usptBsnsPlanDoc.getBsnsSlctnId());
			//조회값 리턴 list 선언
			List<UsptTaskReqstWct> usptTaskReqstWctInfoList = new ArrayList<>();

			for(UsptBsnsSlctn out : usptBsnsSlctnList) {
				UsptTaskReqstWct UsptTaskReqstWctReturn = new UsptTaskReqstWct();
				UsptTaskReqstWctReturn.setBsnsYear(usptBsnsPlanDoc.getBsnsYear()); 	/** 사업년도 */
				UsptTaskReqstWctReturn.setSportBudget(out.getSportBudget());        /** 지원예산 */
				UsptTaskReqstWctReturn.setAlotmCash(out.getAlotmCash());          	/** 부담금현금 */
				UsptTaskReqstWctReturn.setAlotmActhng(out.getAlotmActhng());    	/** 부담금현물 */
				UsptTaskReqstWctReturn.setAlotmSum(out.getAlotmSum());				/** 소계(현금+현물)*/
				UsptTaskReqstWctReturn.setAlotmSumTot(out.getTotalWct());		 	/** 부담금합계 (지원예산+현금+현물) */
				UsptTaskReqstWctReturn.setTotalWct(out.getTotalWct());					/** 총사업비 */
				usptTaskReqstWctInfoList.add(UsptTaskReqstWctReturn);
			}
			resultBsnsPlanDto.setUsptTaskReqstWct(usptTaskReqstWctInfoList);
		}

		/** 제출첨부파일 목록 **/
		if (string.isNotBlank(usptBsnsPlanDoc.getPrsentrAttachmentGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsPlanDoc.getPrsentrAttachmentGroupId());
			resultBsnsPlanDto.setAttachFileList(list);
		}

		/* 개인정보 조회 이력 생성  --------------------------------------------------------------------------------*/
		// 과제책임자 이력 생성
		if(usptTaskRspnber != null) {
			LogIndvdlInfSrch taskLogParam = LogIndvdlInfSrch.builder()
					.memberId(worker.getMemberId())
					.memberIp(CoreUtils.webutils.getRemoteIp(request))
					.workTypeNm("조회")
					.workCn("사업계획서접수 관리 상세조회 - 과제책임자정보 : " + usptTaskRspnber.getRspnberNm())
					.trgterId(usptBsnsPlanDoc.getMemberId())
					.email(usptTaskRspnber.getEmail())
					.birthday(usptTaskRspnber.getBrthdy())
					.mobileNo(usptTaskRspnber.getMbtlnum())
					.build();
			indvdlInfSrchUtils.insert(taskLogParam);
		}

		// 참여기업 이력 생성
		if(usptTaskPrtcmpnyList != null) {
			for(UsptTaskPrtcmpny tpInfo : usptTaskPrtcmpnyList) {
				LogIndvdlInfSrch tpLogParam = LogIndvdlInfSrch.builder()
						.memberId(worker.getMemberId())
						.memberIp(CoreUtils.webutils.getRemoteIp(request))
						.workTypeNm("조회")
						.workCn("사업계획서접수 관리 상세조회 - 참여기업정보 : " + tpInfo.getRspnberNm())
						.trgterId(tpInfo.getMemberId())
						.email(tpInfo.getEmail())
						.birthday("")
						.mobileNo(tpInfo.getMbtlnum())
						.build();
				indvdlInfSrchUtils.insert(tpLogParam);
			}
		}

		// 참여인력 이력 생성
		if(usptTaskPartcptsList != null) {
			for(UsptTaskPartcpts tpInfo : usptTaskPartcptsList) {
				LogIndvdlInfSrch tpLogParam = LogIndvdlInfSrch.builder()
						.memberId(worker.getMemberId())
						.memberIp(CoreUtils.webutils.getRemoteIp(request))
						.workTypeNm("조회")
						.workCn("사업계획서접수 관리 상세조회 - 참여인력정보 : " + tpInfo.getPartcptsNm())
						.trgterId(tpInfo.getMemberId())
						.email("")
						.birthday(tpInfo.getBrthdy())
						.mobileNo(tpInfo.getMbtlnum())
						.build();
				indvdlInfSrchUtils.insert(tpLogParam);
			}
		}

		return resultBsnsPlanDto;
	}

	/**
	 * 사업계획서 제출파일 일괄 다운
	 * @param tBsnsPlanParam
	 * @return
	 */
	public void getPrsentAllFileDown(HttpServletResponse response, BsnsPlanParam bsnsPlanParam) {
		SecurityUtils.checkWorkerIsInsider();
		if (string.isNotBlank(bsnsPlanParam.getPrsentrAttachmentGroupId())) {
			attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), bsnsPlanParam.getPrsentrAttachmentGroupId(), "사업계획서 제출 파일");
		}
	}

	/**
	 * 사업계획서첨부파일 단건 다운
	 * @param tBsnsPlanParam
	 * @return
	 */
	public void getPrsentFileDownOne(HttpServletResponse response, BsnsPlanParam bsnsPlanParam){
		SecurityUtils.checkWorkerIsInsider();
		if (string.isNotBlank(bsnsPlanParam.getAttachmentId())) {
			attachmentService.downloadFile(response, config.getDir().getUpload(), bsnsPlanParam.getAttachmentId());
		}
	}

	/**
	 * 전문가 신청 상세_처리이력
	 * @param expertReqstListParam
	 * @return
	 */
	public  List<UsptBsnsPlanProcessHist> getBsnsPlanHistList(BsnsPlanParam bsnsPlanParam){
		SecurityUtils.checkWorkerIsInsider();

		UsptBsnsPlanProcessHist usptBsnsPlanProcessHist = new UsptBsnsPlanProcessHist();
		usptBsnsPlanProcessHist.setBsnsPlanDocId(bsnsPlanParam.getBsnsPlanDocId());

		List<UsptBsnsPlanProcessHist> processHistList = usptBsnsPlanProcessHistDao.selectList(usptBsnsPlanProcessHist);

		return processHistList;
	}

	/**
	 * 사업계획서 사유 확인팝업
	 * @param bsnsPlanParam
	 * @return
	 */
	public BsnsPlanResnDto getResnInfo(BsnsPlanParam bsnsPlanParam){
		SecurityUtils.checkWorkerIsInsider();

		/**조회 리턴**/
		BsnsPlanResnDto resultBsnsPlanResnDto= new BsnsPlanResnDto();

		if(CoreUtils.string.isBlank(bsnsPlanParam.getBsnsPlanDocId())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업계획서ID"));
		}
		//보완요청 상태값
		bsnsPlanParam.setPlanPresentnSttusCd(Code.planPresentnSttus.보완요청);
		UsptBsnsPlanDoc result  = 	usptBsnsPlanDocDao.selectOne(bsnsPlanParam);

		if(result == null) {
			throw new InvalidationException("보완요청된 사유내용이 없습니다.");
		}

		//조회된 사유내용 셋팅
		resultBsnsPlanResnDto.setBsnsPlanDocId(result.getBsnsPlanDocId());
		resultBsnsPlanResnDto.setBsnsSlctnId(result.getBsnsSlctnId());
		resultBsnsPlanResnDto.setMakeupReqCn(result.getMakeupReqCn());
		resultBsnsPlanResnDto.setMakeupReqFileGroupId(result.getMakeupReqFileGroupId());
		resultBsnsPlanResnDto.setMemberId(result.getMemberId());

		/** 첨부파일 목록 **/
		if (string.isNotBlank(result.getMakeupReqFileGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(result.getMakeupReqFileGroupId());
			resultBsnsPlanResnDto.setAttachFileList(list);
		}

		return resultBsnsPlanResnDto;
	}

	/**
	 * * 사업계획서 사유 확인팝업-전송
	 * @param bsnsPlanParam (bsnsPlanDocId, bsnsSlctnId, memberId, prsentrAttachmentGroupId)
	 * @return
	 */
	public void modifyResnInfo( BsnsPlanResnParam bsnsPlanResnParam, List<MultipartFile> fileList) {

		BnMember worker =SecurityUtils.checkWorkerIsInsider();
		String strMemberId = worker.getMemberId();
		/** 현재 시간*/
		Date now = new Date();

		//상세내용 조회
		UsptBsnsPlanDoc result  = 	usptBsnsPlanDocDao.selectOne(bsnsPlanResnParam);

		/**입력값 셋팅 **/
		String makeupReqFileGroupId = result.getMakeupReqFileGroupId();

		UsptBsnsPlanDoc usptBsnsPlanDoc  = new UsptBsnsPlanDoc();
		/** 보완요청내용 셋팅*/
		usptBsnsPlanDoc.setBsnsPlanDocId(result.getBsnsPlanDocId());
		usptBsnsPlanDoc.setBsnsSlctnId(result.getBsnsSlctnId());
		usptBsnsPlanDoc.setMakeupReqCn(bsnsPlanResnParam.getMakeupReqCn());		/** 보완요청내용 */
		usptBsnsPlanDoc.setMakeupRqesterId(strMemberId);	/** 보완요청자ID */
		usptBsnsPlanDoc.setMakeupReqDt(now);					/** 보완요청일시 */
		usptBsnsPlanDoc.setPlanPresentnSttusCd(Code.planPresentnSttus.보완요청);/** 사업계획제출상태코드(G:PLAN_PRESENTN_STTUS) */
		usptBsnsPlanDoc.setUpdatedDt(now);
		usptBsnsPlanDoc.setUpdaterId(strMemberId);
		usptBsnsPlanDoc.setMakeupReqFileGroupId(makeupReqFileGroupId);	 /** 보완요청파일그룹ID */


		/** 첨부파일*/
		if(fileList != null && fileList.size() > 0) {
			if(CoreUtils.string.isNotBlank(usptBsnsPlanDoc.getMakeupReqFileGroupId())) {
				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), usptBsnsPlanDoc.getMakeupReqFileGroupId(), fileList, null, 0);
			} else {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
				 /** 보완요청파일그룹ID */
				usptBsnsPlanDoc.setMakeupReqFileGroupId(fileGroupInfo.getAttachmentGroupId());
			}
		}
		/*사유 수정*/
		int updateCnt = usptBsnsPlanDocDao.updateResnInfo(usptBsnsPlanDoc);

		if(updateCnt < 1) {
			throw new InvalidationException("보완요청 중 오류가 발생했습니다.(사유 저장 오류");
		}

		/**사업계획서처리이력*/
		UsptBsnsPlanProcessHist usptBsnsPlanProcessHist = new UsptBsnsPlanProcessHist();
		usptBsnsPlanProcessHist.setPlanProcessHistId(CoreUtils.string.getNewId(Code.prefix.사업계획서이력));
		usptBsnsPlanProcessHist.setBsnsPlanDocId(usptBsnsPlanDoc.getBsnsPlanDocId());
		usptBsnsPlanProcessHist.setPlanPresentnSttusCd(Code.planPresentnSttus.보완요청);	/** 사업계획제출상태코드(G:PLAN_PRESENTN_STTUS) */
		usptBsnsPlanProcessHist.setResnCn(bsnsPlanResnParam.getMakeupReqCn());				/** 보완요청내용 */
		usptBsnsPlanProcessHist.setCreatedDt(now);
		usptBsnsPlanProcessHist.setCreatorId(strMemberId);

		usptBsnsPlanProcessHistDao.insert(usptBsnsPlanProcessHist);

		/***************  이메일 or sms 전송 ******************/
		/*신청자정보*/
		String prsentMemberId = result.getMemberId();
		//회원정보 조회
		CmmtMember memberInfo = cmmtMemberDao.select(prsentMemberId);
		usptBsnsPlanDoc.setMemberNm(memberInfo.getMemberNm());		//신청자명
		usptBsnsPlanDoc.setMbtlnum(memberInfo.getMobileNo());		//휴대폰번호
		usptBsnsPlanDoc.setEmail(memberInfo.getEmail()	);	//이메일
		//send sms, email
		sendMsg(usptBsnsPlanDoc);

		/* 첨부파일 삭제 */
		List<CmmtAtchmnfl> attachFileDeleteList = bsnsPlanResnParam.getAttachFileDeleteList();
		if( attachFileDeleteList !=null && attachFileDeleteList.size() >0) {
			for(CmmtAtchmnfl attachInfo : attachFileDeleteList) {
				attachmentService.removeFile_keepEmptyGroup( config.getDir().getUpload(), attachInfo.getAttachmentId());
			}
		}
	}

	/**
	 * * 사업계획서 승인, 승인취소
	 * @param bsnsPlanParam :사업계획서ID(bsnsPlanDocId), 상태코드(planPresentnSttusCd), 휴대폰번호, email, 신청자Id,  신청ID(APPLY_ID)
	 * @return
	 */
	public void modifyPlanSttus( BsnsPlanParam bsnsPlanParam) {

		BnMember worker =SecurityUtils.checkWorkerIsInsider();
		String strMemberId = worker.getMemberId();
		/** 현재 시간*/
		Date now = new Date();
		//사업계획제출상태코드
		String planPresentnSttusCd =  bsnsPlanParam.getPlanPresentnSttusCd();

		/*
		 * 승인		= "PLPR04";
		 * 승인취소	= "PLPR05";
		 */

		String strResnCn = "";
		if(CoreUtils.string.equals(planPresentnSttusCd, Code.planPresentnSttus.승인)){		//PLPR04
			strResnCn = "사업계획서 승인";
		}else if(CoreUtils.string.equals(planPresentnSttusCd, Code.planPresentnSttus.승인취소)){		// PLPR05
			strResnCn = "사업계획서 승인취소";
		}
		bsnsPlanParam.setUpdatedDt(now);
		bsnsPlanParam.setUpdaterId(strMemberId);

		int updateCnt = usptBsnsPlanDocDao.updatePlanSttus(bsnsPlanParam);

		if(updateCnt < 1 ) {
			throw new InvalidationException( strResnCn + " 중 오류가 발생했습니다.");
		}

		/**사업계획서처리이력*/
		UsptBsnsPlanProcessHist usptBsnsPlanProcessHist = new UsptBsnsPlanProcessHist();
		usptBsnsPlanProcessHist.setPlanProcessHistId(CoreUtils.string.getNewId(Code.prefix.사업계획서이력));
		usptBsnsPlanProcessHist.setBsnsPlanDocId(bsnsPlanParam.getBsnsPlanDocId());
		usptBsnsPlanProcessHist.setPlanPresentnSttusCd(planPresentnSttusCd);	/** 사업계획제출상태코드(G:PLAN_PRESENTN_STTUS) */
		usptBsnsPlanProcessHist.setResnCn(strResnCn);				/** 보완요청내용 */
		usptBsnsPlanProcessHist.setCreatedDt(now);
		usptBsnsPlanProcessHist.setCreatorId(strMemberId);

		usptBsnsPlanProcessHistDao.insert(usptBsnsPlanProcessHist);

		/***************  사업협약 등록 및 삭제 ******************/
		/*
		 * uspt_bsns_cnvn(사업협약), uspt_bsns_cnvn_prtcmpny_sign(참여기업서명)
		 * 승인 : 등록 --> 대기중(CNST01)
		 * 취소 : 삭제
		 */
		UsptBsnsCnvn usptBsnsCnvn = new UsptBsnsCnvn();
		//승인
		if(CoreUtils.string.equals(planPresentnSttusCd, "PLPR04")){
			/**사업협약ID*/
			String newBsnsCnvnId = CoreUtils.string.getNewId(Code.prefix.사업협약);

			usptBsnsCnvn.setApplyId(bsnsPlanParam.getApplyId());
			usptBsnsCnvn.setBsnsCnvnId(newBsnsCnvnId);
			usptBsnsCnvn.setCnvnSttusCd(Code.cnvnSttus.대기중);
			usptBsnsCnvn.setBsnsPlanDocId(bsnsPlanParam.getBsnsPlanDocId());
			usptBsnsCnvn.setCreatedDt(now);
			usptBsnsCnvn.setCreatorId(strMemberId);
			usptBsnsCnvn.setUpdatedDt(now);
			usptBsnsCnvn.setUpdaterId(strMemberId);

			usptBsnsCnvnDao.insert(usptBsnsCnvn);

		//취소
		}else if(CoreUtils.string.equals(planPresentnSttusCd, "PLPR05")){

			usptBsnsCnvn = usptBsnsCnvnDao.selectCnvnSttusByBsnsPlanDocId(bsnsPlanParam.getBsnsPlanDocId());

			if(CoreUtils.string.equals(usptBsnsCnvn.getCnvnSttusCd(), Code.cnvnSttus.대기중) || CoreUtils.string.equals(usptBsnsCnvn.getCnvnSttusCd(), Code.cnvnSttus.협약생성)) {

				usptBsnsCnvnPrtcmpnySignDao.delete(usptBsnsCnvn.getBsnsCnvnId());
				usptBsnsCnvnDao.deleteByBsnsPlanDocId(bsnsPlanParam.getBsnsPlanDocId());
			}else {
				throw new InvalidationException("협약 상태가 대기중, 협약상태에서만 사업계획서 승인 취소가 가능합니다.");
			}
		}

		/***************  이메일 or sms 전송 ******************/
		/**입력값 셋팅 **/
		UsptBsnsPlanDoc usptBsnsPlanDoc  = new UsptBsnsPlanDoc();

		/* 사업계획제출상태코드 셋팅*/
		usptBsnsPlanDoc.setPlanPresentnSttusCd(planPresentnSttusCd);
		/*신청자정보*/
		String prsentMemberId = bsnsPlanParam.getMemberId();
		//회원정보 조회
		CmmtMember memberInfo = cmmtMemberDao.select(prsentMemberId);

		if(CoreUtils.string.isNotBlank(bsnsPlanParam.getMemberNm())){
			usptBsnsPlanDoc.setMemberNm(bsnsPlanParam.getMemberNm()); //신청자명
		}else {
			usptBsnsPlanDoc.setMemberNm(memberInfo.getMemberNm());		//신청자명
		}
		if(CoreUtils.string.isNotBlank(bsnsPlanParam.getMbtlnum())){
			usptBsnsPlanDoc.setMbtlnum(bsnsPlanParam.getMbtlnum()); //휴대폰번호
		}else {
			usptBsnsPlanDoc.setMbtlnum(memberInfo.getMobileNo());		//휴대폰번호
		}
		if(CoreUtils.string.isNotBlank(bsnsPlanParam.getEmail())){
			usptBsnsPlanDoc.setEmail(bsnsPlanParam.getEmail()); //이메일
		}else {
			usptBsnsPlanDoc.setEmail(memberInfo.getEmail()	);	//이메일
		}

		//send sms, email
		sendMsg(usptBsnsPlanDoc);
	}

	/**
	 * 사업계획서 비목별 사업비 구성팝업_조회
	 * @param bsnsPlanParam
	 * @return
	 */
	public List<UsptTaskTaxitmWct> getTaxitmWct(BsnsPlanParam bsnsPlanParam){
		SecurityUtils.checkWorkerIsInsider();
		UsptTaskTaxitmWct usptTaskTaxitmWct = new UsptTaskTaxitmWct();
		usptTaskTaxitmWct.setBsnsPlanDocId(bsnsPlanParam.getBsnsPlanDocId());
		usptTaskTaxitmWct.setBsnsYear(bsnsPlanParam.getBsnsYear());

		return usptTaskTaxitmWctDao.selectBsnsPlanTaxitmWctList(usptTaskTaxitmWct);
	}

	/*******************************************************************************************************************************/
	/**
	 * 전문가 신청 sms or email 전송
	 * @param expertReqstListParam
	 * @return
	 */
	public String sendMsg(UsptBsnsPlanDoc param) {
		SecurityUtils.checkWorkerIsInsider();
		StringBuffer sbMsg = new StringBuffer();

		if(param == null) {
			throw new InvalidationException("발송대상이 설정되지 않았습니다.");
		}

		String title = "";
		String mainTitle = "";
		String memNm ="";
		String cnVal = "";
		String email  = param.getEmail();
		String mbtlnum= param.getMbtlnum();

		EmailResult er = null;
		SmsResult sr = null;

		if(CoreUtils.string.equals(param.getPlanPresentnSttusCd(), Code.planPresentnSttus.보완요청)){		//보완요청
			title 		= "사업계획접수 보완요청";
			mainTitle = "사업계획접수 보완요청";
			memNm = param.getMemberNm();			//회원명
			cnVal 		= param.getMakeupReqCn();			//발송내용

		}else if(CoreUtils.string.equals(param.getPlanPresentnSttusCd(), Code.planPresentnSttus.승인)){		//승인
			title 		= "사업계획접수 승인 완료";
			mainTitle = "사업계획접수 승인 완료";
			memNm = param.getMemberNm();			//회원명
			cnVal 		= "제출한 사업계획서가 승인 완료 됬습니다.";//발송내용
		}else if(CoreUtils.string.equals(param.getPlanPresentnSttusCd(), Code.planPresentnSttus.승인취소)){	//승인취소
			title 		= "사업계획접수 승인 취소";
			mainTitle = "사업계획접수 승인 취소";
			memNm = param.getMemberNm();			//회원명
			cnVal 		= "제출한 사업계획서가 승인 취소 됬습니다.";//발송내용
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

	/**
	 * 사업계획서 파일 단건 다운
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