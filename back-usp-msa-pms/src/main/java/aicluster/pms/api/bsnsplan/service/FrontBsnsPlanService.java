package aicluster.pms.api.bsnsplan.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import aicluster.pms.api.bsnsplan.dto.FrontBsnsPlanDto;
import aicluster.pms.api.bsnsplan.dto.FrontBsnsPlanParam;
import aicluster.pms.api.bsnsplan.dto.FrontBsnsPlanResnDto;
import aicluster.pms.api.bsnsplan.dto.TaskTaxitmWctDto;
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.UsptBsnsApplyRealmDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyTaskDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplyTaskPartcptsDao;
import aicluster.pms.common.dao.UsptBsnsPlanApplcntDao;
import aicluster.pms.common.dao.UsptBsnsPlanDocDao;
import aicluster.pms.common.dao.UsptBsnsPlanProcessHistDao;
import aicluster.pms.common.dao.UsptBsnsSlctnDao;
import aicluster.pms.common.dao.UsptTaskPartcptsDao;
import aicluster.pms.common.dao.UsptTaskPrtcmpnyDao;
import aicluster.pms.common.dao.UsptTaskReqstWctDao;
import aicluster.pms.common.dao.UsptTaskRspnberDao;
import aicluster.pms.common.dao.UsptTaskTaxitmWctDao;
import aicluster.pms.common.dto.CmmtMemberBizDto;
import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTask;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTaskPartcpts;
import aicluster.pms.common.entity.UsptBsnsPlanApplcnt;
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


@Service
public class FrontBsnsPlanService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;		/*파일*/
	@Autowired
	private UsptBsnsPlanDocDao usptBsnsPlanDocDao;		/*사업계획서*/
	@Autowired
	private UsptBsnsPlanApplcntDao usptBsnsPlanApplcntDao;		/*사업계획서신청자*/
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
	private UsptBsnsApplyRealmDao usptBsnsApplyRealmDao;


	/**
	 * 사업계획서 목록 조회
	 * @param frontBsnsPlanParam
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	public CorePagination<UsptBsnsPlanDoc> getList(FrontBsnsPlanParam frontBsnsPlanParam,  Long page){
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		String strMemberId = worker.getMemberId();

		if(page == null) {
			page = 1L;
		}
		if(frontBsnsPlanParam.getItemsPerPage() == null) {
			frontBsnsPlanParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = frontBsnsPlanParam.getItemsPerPage();

		//신청자ID
		frontBsnsPlanParam.setMemberId(strMemberId);
		//전체건수 조회
		long totalItems = usptBsnsPlanDocDao.selectFrontBsnsPlanDocCnt(frontBsnsPlanParam);

		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		frontBsnsPlanParam.setBeginRowNum(info.getBeginRowNum());
		frontBsnsPlanParam.setItemsPerPage(itemsPerPage);
		List<UsptBsnsPlanDoc> list = usptBsnsPlanDocDao.selectFrontBsnsPlanDocList(frontBsnsPlanParam);

		CorePagination<UsptBsnsPlanDoc> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 사업계획서 사유 확인팝업
	 * @param frontBsnsPlanParam
	 * @return
	 */
	public FrontBsnsPlanResnDto getResnInfo(FrontBsnsPlanParam frontBsnsPlanParam){
		SecurityUtils.checkWorkerIsMember();

		/**조회 리턴**/
		FrontBsnsPlanResnDto frontBsnsPlanResnDto = new FrontBsnsPlanResnDto();

		if(CoreUtils.string.isBlank(frontBsnsPlanParam.getBsnsPlanDocId())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업계획서ID"));
		}

		//보안요청 상태값
		frontBsnsPlanParam.setPlanPresentnSttusCd(Code.planPresentnSttus.보완요청);
		UsptBsnsPlanDoc result  = 	usptBsnsPlanDocDao.selectOne(frontBsnsPlanParam);

		if(result == null) {
			throw new InvalidationException("보안요청된 사유내용이 없습니다.");
		}

		//조회된 사유내용 셋팅
		frontBsnsPlanResnDto.setBsnsPlanDocId(result.getBsnsPlanDocId());
		frontBsnsPlanResnDto.setBsnsSlctnId(result.getBsnsSlctnId());
		frontBsnsPlanResnDto.setMakeupReqCn(result.getMakeupReqCn());
		frontBsnsPlanResnDto.setMakeupReqFileGroupId(result.getMakeupReqFileGroupId());
		frontBsnsPlanResnDto.setRequestDt(result.getRequestDt());

		/** 첨부파일 목록 **/
		if (string.isNotBlank(result.getMakeupReqFileGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(result.getMakeupReqFileGroupId());
			frontBsnsPlanResnDto.setAttachFileList(list);
		}

		return frontBsnsPlanResnDto;
	}

	/**
	 * 사업계획서 사유 확인팝업-파일 전체 다운
	 * @param frontBsnsPlanParam
	 * @return
	 */
	public void getAllFileDown(HttpServletResponse response, String makeupReqFileGroupId) {
		SecurityUtils.checkWorkerIsMember();
		if (string.isNotBlank(makeupReqFileGroupId)) {
			attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), makeupReqFileGroupId, "사업계획서 보안요청_첨부파일");
		}
	}

	/**
	 * 사업계획서 상세 조회
	 * @param frontBsnsPlanParam
	 * @return
	 */
	public FrontBsnsPlanDto getBsnsPlanDocInfo(FrontBsnsPlanParam frontBsnsPlanParam){
		SecurityUtils.checkWorkerIsMember();

		/**조회 리턴**/
		FrontBsnsPlanDto resultFrontBsnsPlanDto = new FrontBsnsPlanDto();

		/**기본정보 조회 - 사업선정대상ID ,  사업계획서ID 조회*/
		UsptBsnsPlanDoc usptBsnsPlanDoc =usptBsnsPlanDocDao.selectBsnsPlanBaseDocInfo(frontBsnsPlanParam);

		if(usptBsnsPlanDoc == null) {
			throw new InvalidationException("사업계획서 상세 내용이 없습니다.");
		}
		//사업계획서ID
		String bsnsPlanDocId = usptBsnsPlanDoc.getBsnsPlanDocId();
		/** 지원분야(과제분야) 목록 */
		resultFrontBsnsPlanDto.setApplyRealmList(usptBsnsApplyRealmDao.selectList(usptBsnsPlanDoc.getBsnsCd()));

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


		//컨소시엄여부 true에만 조회
		if(usptBsnsPlanDoc.getCnsrtm()) {
			/**참여기업 조회*/
			UsptTaskPrtcmpny usptTaskPrtcmpny = new UsptTaskPrtcmpny();
			usptTaskPrtcmpny.setBsnsPlanDocId(bsnsPlanDocId);
			List <UsptTaskPrtcmpny> usptTaskPrtcmpnyList = usptTaskPrtcmpnyDao.selectList(usptTaskPrtcmpny);
			if(usptTaskPrtcmpnyList.size()>0) {
				for(UsptTaskPrtcmpny resultParam: usptTaskPrtcmpnyList) {
					resultParam.setRegTelno(resultParam.getTelno());
					resultParam.setRegMbtlnum(resultParam.getMbtlnum());
					resultParam.setRegEmail(resultParam.getEmail());
				}
				//참여기업 리턴 셋팅
				resultFrontBsnsPlanDto.setUsptTaskPrtcmpny(usptTaskPrtcmpnyList);
				//사업계획서 참여기업체 총수 셋팅
				usptBsnsPlanDoc.setPartcptnCompanyCnt(new Long(usptTaskPrtcmpnyList.size())); /*참여기업체 총수*/
			}
		}
		//사업계획서 리턴 셋팅
		resultFrontBsnsPlanDto.setUsptBsnsPlanDoc(usptBsnsPlanDoc);

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
			resultFrontBsnsPlanDto.setUsptTaskRspnber(usptTaskRspnber);
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
			resultFrontBsnsPlanDto.setUsptTaskRspnber(usptTaskRspnberInfo);
		}

		/**참여인력 조회*/
		UsptTaskPartcpts usptTaskPartcpts = new UsptTaskPartcpts();
		usptTaskPartcpts.setBsnsPlanDocId(bsnsPlanDocId);
		List <UsptTaskPartcpts> usptTaskPartcptsList = usptTaskPartcptsDao.selectList(usptTaskPartcpts);
		//조회값 리턴 list 선언
		List<UsptTaskPartcpts> usptTaskPartcptsListReturn = new ArrayList<>();

		if(usptTaskPartcptsList.size()>0) {
			for( UsptTaskPartcpts out :  usptTaskPartcptsList) {
				out.setEncMbtlnum(out.getMbtlnum());	  		/** 휴대폰번호 */
				out.setEncBrthdy(out.getBrthdy());					/** 생년월일 */
				usptTaskPartcptsListReturn.add(out);
			}
			//참여인력 리턴 셋팅
			resultFrontBsnsPlanDto.setUsptTaskPartcpts(usptTaskPartcptsListReturn);

		}else {
			List<UsptBsnsPblancApplyTaskPartcpts> usptBsnsPblancApplyTaskPartcptsList = usptBsnsPblancApplyTaskPartcptsDao.selectList(usptBsnsPlanDoc.getApplyId());
			//조회값 리턴 list 선언
			List<UsptTaskPartcpts> usptTaskPartcptsInfoList = new ArrayList<>();

			for( UsptBsnsPblancApplyTaskPartcpts out :  usptBsnsPblancApplyTaskPartcptsList) {
				UsptTaskPartcpts usptTaskPartcptsReturn = new UsptTaskPartcpts();

				//회원정보 조회
//				CmmtMember memberInfo = cmmtMemberDao.select(usptBsnsPlanDoc.getMemberId());

// 				usptTaskPartcptsReturn.setMemberId(usptBsnsPlanDoc.getMemberId());		//소속기관에 주관기업의 업체를 기본으로 셋팅
// 				usptTaskPartcptsReturn.setMemberNm(memberInfo.getMemberNm());			//소속기관에 주관기업의 업체를 기본으로 셋팅
				usptTaskPartcptsReturn.setPartcptsNm(out.getPartcptsNm());			/** 참여자명 */
				usptTaskPartcptsReturn.setChrgRealmNm(out.getChrgRealmNm());		/** 담당분야명 */
				usptTaskPartcptsReturn.setEncMbtlnum(out.getDecMbtlnum());	  			/** 휴대폰번호 */
				usptTaskPartcptsReturn.setEncBrthdy(out.getDecBrthdy());					/** 생년월일 */
				usptTaskPartcptsReturn.setPartcptnRate(out.getPartcptnRate());			/** 참여율 */
				usptTaskPartcptsInfoList.add(usptTaskPartcptsReturn);
			}
			//참여인력 리턴 셋팅
			resultFrontBsnsPlanDto.setUsptTaskPartcpts(usptTaskPartcptsInfoList);
		}


		/**과제신청사업비(신청예산) 조회*/
		UsptTaskReqstWct usptTaskReqstWct = new UsptTaskReqstWct();
		usptTaskReqstWct.setBsnsPlanDocId(bsnsPlanDocId);
		List <UsptTaskReqstWct> usptTaskReqstWctList = usptTaskReqstWctDao.selectList(usptTaskReqstWct);
		if(usptTaskReqstWctList.size()>0 ) {
			//과제신청사업비 리턴 셋팅
			resultFrontBsnsPlanDto.setUsptTaskReqstWct(usptTaskReqstWctList);
		}else {
			//	사업선정대상 예산 조회
			List <UsptBsnsSlctn> usptBsnsSlctnList = usptBsnsSlctnDao.selectList(usptBsnsPlanDoc.getBsnsSlctnId());

			//조회값 리턴 list 선언
			List<UsptTaskReqstWct> usptTaskReqstWctInfoList = new ArrayList<>();

			for(UsptBsnsSlctn out : usptBsnsSlctnList) {
				UsptTaskReqstWct UsptTaskReqstWctReturn = new UsptTaskReqstWct();
				UsptTaskReqstWctReturn.setBsnsYear(usptBsnsPlanDoc.getBsnsYear());
				UsptTaskReqstWctReturn.setSportBudget(out.getSportBudget());        /** 지원예산 */
				UsptTaskReqstWctReturn.setAlotmCash(out.getAlotmCash());          	/** 부담금현금 */
				UsptTaskReqstWctReturn.setAlotmActhng(out.getAlotmActhng());    	/** 부담금현물 */
				UsptTaskReqstWctReturn.setAlotmSum(out.getAlotmSum());				/** 소계(현금+현물)*/
				UsptTaskReqstWctReturn.setAlotmSumTot(out.getTotalWct());		 	/** 부담금합계 (지원예산+현금+현물) */
				UsptTaskReqstWctReturn.setTotalWct(out.getTotalWct());					/** 총사업비 */
				usptTaskReqstWctInfoList.add(UsptTaskReqstWctReturn);
			}
			/** 총사업비 */
			usptBsnsPlanDoc.setTotalWct(usptTaskReqstWctInfoList.get(0).getTotalWct());

			/*
			 *	사업선정대상 예산 조회는 무조건 단건 조회 0번째에는 종료년월 셋팅
			 *   diffYear 만큼 row갯수 증가 시킨다.
			 *   각각 데이터는 0원 셋팅
			 */
			//다년사업시 0번째에(최신년도) 데이터 나머지는 0 으로 년도수 만큼 row를 생성
			String bsnsBgndeYear = usptBsnsPlanDoc.getBsnsBgnde().substring(0, 4);		//사업시작연도
			String bsnsEnddeYear = usptBsnsPlanDoc.getBsnsEndde().substring(0, 4);		//사업종료연도
			//시작 종료 연월이 다르면....
			if(!CoreUtils.string.equals(bsnsBgndeYear, bsnsEnddeYear)){
				Date toDate = bsnsBgndt;
				Date fromDate = bsnsEnddt;

				long baseDay = 24 * 60 * 60 * 1000; 	// 일
				long baseMonth = baseDay * 30;		// 월
				long baseYear = baseMonth * 12;		// 년
				// from 일자와 to 일자의 시간 차이를 계산한다.
				long calDate = fromDate.getTime() - toDate.getTime();
				// from 일자와 to 일자의 시간 차 값을 하루기준으로 나눠 준다.
				//연도차
				long diffYear = calDate / baseYear;
				//연도 폼 설정
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
				Calendar cal = Calendar.getInstance();

				for(int i = 0;  i < diffYear + 1 ; i++) {
					if( i ==0) {
						usptTaskReqstWctInfoList.get(0).setBsnsYear(bsnsEnddeYear);
					}else {
						cal.setTime(fromDate); 		// 시간 설정(사업종료일)
						cal.add(Calendar.YEAR, - i ); // 년 연산

						UsptTaskReqstWct usptTaskReqstWctIsBlank = new UsptTaskReqstWct();
						usptTaskReqstWctIsBlank.setBsnsYear(dateFormat.format(cal.getTime())); 	/** 사업년도 */
						usptTaskReqstWctIsBlank.setSportBudget(0L);        								/** 지원예산 */
						usptTaskReqstWctIsBlank.setAlotmCash(0L);          								/** 부담금현금 */
						usptTaskReqstWctIsBlank.setAlotmActhng(0L);    								/** 부담금현물 */
						usptTaskReqstWctIsBlank.setAlotmSum(0L);										/** 소계(현금+현물)*/
						usptTaskReqstWctIsBlank.setAlotmSumTot(0L);		 							/** 부담금합계 (지원예산+현금+현물*/
						usptTaskReqstWctInfoList.add(i, usptTaskReqstWctIsBlank);
					}
				}
			}
			resultFrontBsnsPlanDto.setUsptTaskReqstWct(usptTaskReqstWctInfoList);
		}

		/** 제출첨부파일 목록 **/
		if (string.isNotBlank(usptBsnsPlanDoc.getPrsentrAttachmentGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsPlanDoc.getPrsentrAttachmentGroupId());
			resultFrontBsnsPlanDto.setAttachFileList(list);
		}
		return resultFrontBsnsPlanDto;
	}


	/**
	 * 사업계획서 비목별 사업비 구성팝업_조회
	 * @param bsnsPlanDocId, bsnsYear
	 * @return
	 */
	public List<UsptTaskTaxitmWct> getTaxitmWct(FrontBsnsPlanParam frontBsnsPlanParam){
		SecurityUtils.checkWorkerIsMember();


		UsptTaskTaxitmWct usptTaskTaxitmWct = new UsptTaskTaxitmWct();

		usptTaskTaxitmWct.setBsnsPlanDocId(frontBsnsPlanParam.getBsnsPlanDocId());
		usptTaskTaxitmWct.setBsnsYear(frontBsnsPlanParam.getBsnsYear());

		return usptTaskTaxitmWctDao.selectBsnsPlanTaxitmWctList(usptTaskTaxitmWct);
	}

	/**
	 * 사업계획서 비목별 사업비 구성팝업_저장
	 * @param usptTaskTaxitmWctList
	 * @return
	 */
	public void addTaxitmWct( TaskTaxitmWctDto taskTaxitmWctDto){
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		String strMemberId = worker.getMemberId();

		 List <UsptTaskTaxitmWct> usptTaskTaxitmWctList = taskTaxitmWctDto.getUsptTaskTaxitmWctList();
		 /** 사업계획서ID */
		String  bsnsPlanDocId = taskTaxitmWctDto.getBsnsPlanDocId();
		/**사업년도*/
		String bsnsYear = taskTaxitmWctDto.getBsnsYear();
		//등록
		for(UsptTaskTaxitmWct input : usptTaskTaxitmWctList) {
			//과제세목별사업비ID 없을 시
			if(CoreUtils.string.isBlank(input.getTaskTaxitmWctId())) {
				String newTaskTaxitmWctId = CoreUtils.string.getNewId(Code.prefix.과제세목별사업비);
				input.setTaskTaxitmWctId(newTaskTaxitmWctId);
				input.setBsnsPlanDocId(bsnsPlanDocId);
				input.setBsnsYear(bsnsYear);;
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptTaskTaxitmWctDao.insert(input);
			}else {
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptTaskTaxitmWctDao.update(input);
			}
		}
	}

	/**
	 * 사업계획서 임시저장 및 제출
	 * @param frontBsnsPlanDto
	 * * @param fileList
	 * @return
	 */
	public String modifyBsnsPlan( String planPresentnSttusCd, FrontBsnsPlanDto frontBsnsPlanDto,  List<MultipartFile> fileList) {

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		String strMemberId = worker.getMemberId();
		/*
		 * 미제출 	= "PLPR01";
		 * 제출 		= "PLPR02";
		 * 보완요청 	= "PLPR03";
		 * 승인		= "PLPR04";
		 */
		String strResnCn = "";								//사유내용
		if(CoreUtils.string.equals(planPresentnSttusCd, "PLPR01")){
			strResnCn = "사업계획서 임시저장";
		}else if(CoreUtils.string.equals(planPresentnSttusCd, "PLPR02")){
			strResnCn = "사업계획서 제출";
		}

		/**사업계획서*/
		UsptBsnsPlanDoc usptBsnsPlanDoc = frontBsnsPlanDto.getUsptBsnsPlanDoc();
		//************************* value check start
		//제출에만 체크
		if(CoreUtils.string.equals(planPresentnSttusCd, "PLPR02")){
			if(CoreUtils.string.isBlank(usptBsnsPlanDoc.getTaskNmKo())) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "과제명(국문)"));
			}
			if(CoreUtils.string.isBlank(usptBsnsPlanDoc.getApplyField())) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "과제분야"));
			}

			/**과제책임자*/
			UsptTaskRspnber usptTaskRspnber = frontBsnsPlanDto.getUsptTaskRspnber();
			if(usptTaskRspnber != null) {
				if(CoreUtils.string.isBlank(usptTaskRspnber.getRspnberNm())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "책임자명"));
				}
				if(CoreUtils.string.isBlank(usptTaskRspnber.getEncMbtlnum())) {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "과제책임자의 휴대폰번호"));
				}
				if(CoreUtils.string.isBlank(usptTaskRspnber.getEncEmail())) {
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "과제책임자의 이메일"));
				}
			}

			/**과제참여기업(참여기업)*/
			List <UsptTaskPrtcmpny> usptTaskPrtcmpnyList = frontBsnsPlanDto.getUsptTaskPrtcmpny();
			if(usptTaskPrtcmpnyList.size() > 0) {
				for(UsptTaskPrtcmpny inputParam : usptTaskPrtcmpnyList) {
					if(CoreUtils.string.isBlank(inputParam.getEntrpsNm())){
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "업체명"));
					}
					if(CoreUtils.string.isBlank(inputParam.getRspnberNm())){
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "참여기업의 책임자명"));
					}
					if(CoreUtils.string.isBlank(inputParam.getClsfNm())){
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "참여기업의 직위/직급"));
					}
					if(CoreUtils.string.isBlank(inputParam.getRegMbtlnum())){
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "참여기업의 휴대폰번호"));
					}
					if(CoreUtils.string.isBlank(inputParam.getRegEmail())){
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "참여기업의 이메일"));
					}
				}
			}
		}
		//************************* value check end

		//사업계획서ID(이력등록시 사용)
		String bsnsPlanDocId ="";

		if (string.isNotBlank(usptBsnsPlanDoc.getBsnsPlanDocId())) {
			FrontBsnsPlanParam frontBsnsPlanParam = new FrontBsnsPlanParam();
			frontBsnsPlanParam.setBsnsPlanDocId(usptBsnsPlanDoc.getBsnsPlanDocId());
			UsptBsnsPlanDoc checkResult = usptBsnsPlanDocDao.selectOne(frontBsnsPlanParam);
			/*
			 * PLPR02 제출
			 * PLPR04 승인
			 */
			if(CoreUtils.string.equals(checkResult.getPlanPresentnSttusCd(), "PLPR02")){
				throw new InvalidationException(String.format("제출 완료된 상태 입니다."));
			}else if(CoreUtils.string.equals(checkResult.getPlanPresentnSttusCd(), "PLPR04")){
				throw new InvalidationException(String.format("승인 완료된 상태 입니다."));
			}
			//수정
			bsnsPlanDocId = modify(planPresentnSttusCd, frontBsnsPlanDto, fileList);
		}else {
			//등록
			bsnsPlanDocId = insert(planPresentnSttusCd,  frontBsnsPlanDto, fileList);
		}

		/**사업계획서처리이력*/
		UsptBsnsPlanProcessHist usptBsnsPlanProcessHist = new UsptBsnsPlanProcessHist();
		usptBsnsPlanProcessHist.setPlanProcessHistId(CoreUtils.string.getNewId(Code.prefix.사업계획서이력));
		usptBsnsPlanProcessHist.setBsnsPlanDocId(bsnsPlanDocId);
		usptBsnsPlanProcessHist.setPlanPresentnSttusCd(planPresentnSttusCd);
		usptBsnsPlanProcessHist.setResnCn(strResnCn);
		usptBsnsPlanProcessHist.setCreatedDt(now);
		usptBsnsPlanProcessHist.setCreatorId(strMemberId);

		usptBsnsPlanProcessHistDao.insert(usptBsnsPlanProcessHist);
		//참여인력 탭 조회 시 참여기업 조회
		return bsnsPlanDocId;
	}

	/**
	 * 사업계획서 등록
	 * @param planPresentnSttusCd, frontBsnsPlanDto
	 * @return strBsnsPlanDocId
	 */
	public String insert( String planPresentnSttusCd, FrontBsnsPlanDto frontBsnsPlanDto, List<MultipartFile> fileList) {
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		String strMemberId = worker.getMemberId();
		String newBsnsPlanDocId ="";
		String saveMsg = "";

		if(CoreUtils.string.equals(planPresentnSttusCd, Code.planPresentnSttus.미제출)) {
			saveMsg = "임시저장";
		}else {
			saveMsg = "제출";
		}

		/**사업계획서*/
		UsptBsnsPlanDoc usptBsnsPlanDoc = frontBsnsPlanDto.getUsptBsnsPlanDoc();

		/**사업계획서ID*/
		newBsnsPlanDocId = CoreUtils.string.getNewId(Code.prefix.사업계획서);

		/** 첨부파일*/
		if(fileList != null && fileList.size() > 0) {
			if(CoreUtils.string.isNotBlank(usptBsnsPlanDoc.getPrsentrAttachmentGroupId())) {
				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), usptBsnsPlanDoc.getPrsentrAttachmentGroupId(), fileList, null, 0);
			} else {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
				usptBsnsPlanDoc.setPrsentrAttachmentGroupId(fileGroupInfo.getAttachmentGroupId());
			}
		}

		usptBsnsPlanDoc.setBsnsPlanDocId(newBsnsPlanDocId);
		usptBsnsPlanDoc.setPlanPresentnSttusCd(planPresentnSttusCd);
		if(CoreUtils.string.equals(planPresentnSttusCd, Code.planPresentnSttus.제출)) {
			usptBsnsPlanDoc.setPresentnDt(now);
			usptBsnsPlanDoc.setPrsentrId(strMemberId);
		}
		/*사업계획신청자 ID*/
		usptBsnsPlanDoc.setMemberId(strMemberId);//신청자ID 추가
		usptBsnsPlanDoc.setCreatedDt(now);
		usptBsnsPlanDoc.setCreatorId(strMemberId);
		usptBsnsPlanDoc.setUpdatedDt(now);
		usptBsnsPlanDoc.setUpdaterId(strMemberId);

		if(usptBsnsPlanDocDao.insert(usptBsnsPlanDoc) != 1) {
			throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(일반현황정보 저장 오류)");
		}

		//사업계획신청자 조회
		CmmtMember memberInfo = cmmtMemberDao.select(strMemberId);
		/** 개인사업자구분코드 */
		String indvdlBsnmDivCd ="";
		if(CoreUtils.string.equals(memberInfo.getMemberType(), Code.memberType.개인사업자)){
			indvdlBsnmDivCd = Code.indvdlBsnmDiv.개인;
		}else {
			indvdlBsnmDivCd = Code.indvdlBsnmDiv.사업자;
		}
		UsptBsnsPlanApplcnt usptBsnsPlanApplcnt = new UsptBsnsPlanApplcnt();
		String newBsnsPlanApplcntId = CoreUtils.string.getNewId(Code.prefix.사업계획신청자);
		usptBsnsPlanApplcnt.setBsnsPlanApplcntId(newBsnsPlanApplcntId);			/** 사업계획신청자ID */
		usptBsnsPlanApplcnt.setBsnsPlanDocId(newBsnsPlanDocId);					/** 사업계획서ID */
		usptBsnsPlanApplcnt.setApplcntNm(memberInfo.getMemberNm());			/** 신청자명 */
		usptBsnsPlanApplcnt.setGenderCd(memberInfo.getGender());					/** 성별코드(G:GENDER) */
		if(CoreUtils.string.isNotBlank(memberInfo.getMobileNo())) {
			usptBsnsPlanApplcnt.setEncMbtlnum(CoreUtils.aes256.encrypt(memberInfo.getMobileNo(), newBsnsPlanApplcntId));	/** 암호화된 휴대폰번호 */
		}
		if(CoreUtils.string.isNotBlank(memberInfo.getBirthday())) {
			usptBsnsPlanApplcnt.setEncBrthdy(CoreUtils.aes256.encrypt(memberInfo.getBirthday(), newBsnsPlanApplcntId));		/** 암호화된 생년월일 */
		}
		if(CoreUtils.string.isNotBlank(memberInfo.getEmail())) {
			usptBsnsPlanApplcnt.setEncEmail(CoreUtils.aes256.encrypt(memberInfo.getEmail(), newBsnsPlanApplcntId));				/** 암호화된 이메일 */
		}
		usptBsnsPlanApplcnt.setNativeYn(null);												/** 내국인여부 */
		usptBsnsPlanApplcnt.setIndvdlBsnmDivCd(indvdlBsnmDivCd);					/** 개인사업자구분코드 */
		usptBsnsPlanApplcnt.setChargerNm(memberInfo.getChargerNm());			/** 담당자명 */
		usptBsnsPlanApplcnt.setCeoNm(memberInfo.getCeoNm());						 /** 대표자명 */
		usptBsnsPlanApplcnt.setBizrno(memberInfo.getBizrno());						 /** 사업자번호(기업회원) */
		usptBsnsPlanApplcnt.setCreatedDt(now);
		usptBsnsPlanApplcnt.setCreatorId(strMemberId);
		usptBsnsPlanApplcnt.setUpdatedDt(now);
		usptBsnsPlanApplcnt.setUpdaterId(strMemberId);

		if(usptBsnsPlanApplcntDao.insert(usptBsnsPlanApplcnt) != 1) {
			throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(사업계획신청자 저장 오류)");
		}

		/**과제책임자*/
		UsptTaskRspnber usptTaskRspnber = frontBsnsPlanDto.getUsptTaskRspnber();
		if(usptTaskRspnber != null ) {
			String newTaskRspnberId = CoreUtils.string.getNewId(Code.prefix.과제책임자);

			usptTaskRspnber.setTaskRspnberId(newTaskRspnberId);
			usptTaskRspnber.setBsnsPlanDocId(newBsnsPlanDocId);
			usptTaskRspnber.setEncMbtlnum(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncMbtlnum(), newTaskRspnberId));
			usptTaskRspnber.setEncBrthdy(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncBrthdy(), newTaskRspnberId));
			usptTaskRspnber.setEncTelno(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncTelno(), newTaskRspnberId));
			usptTaskRspnber.setEncEmail(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncEmail(), newTaskRspnberId));
			usptTaskRspnber.setEncFxnum(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncFxnum(), newTaskRspnberId));
			usptTaskRspnber.setCreatedDt(now);
			usptTaskRspnber.setCreatorId(strMemberId);
			usptTaskRspnber.setUpdatedDt(now);
			usptTaskRspnber.setUpdaterId(strMemberId);

			if(usptTaskRspnberDao.insert(usptTaskRspnber) != 1) {
				throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(과제책임자 저장 오류)");
			}
		}

		/**과제참여기업(참여기업)*/
		List <UsptTaskPrtcmpny> usptTaskPrtcmpnyList = frontBsnsPlanDto.getUsptTaskPrtcmpny();
		if(usptTaskPrtcmpnyList.size() > 0) {
			CmmtMember memberInfoCompany = new CmmtMember();
			for(UsptTaskPrtcmpny input : usptTaskPrtcmpnyList) {

				String newTaskPartcptnEntrprsId = CoreUtils.string.getNewId(Code.prefix.과제참여기업);

				input.setTaskPartcptnEntrprsId(newTaskPartcptnEntrprsId);

				if(string.isNotBlank(input.getMemberId())){
					//업체명 조회
					memberInfoCompany = cmmtMemberDao.select(input.getMemberId());
					input.setEntrpsNm(memberInfoCompany.getMemberNm());	/** 업체명 */
				}
				input.setBsnsPlanDocId(newBsnsPlanDocId);
				if(CoreUtils.string.isNotBlank(input.getRegTelno())) {
					input.setEncTelno(CoreUtils.aes256.encrypt(input.getRegTelno(), newTaskPartcptnEntrprsId));
				}
				if(CoreUtils.string.isNotBlank(input.getRegMbtlnum())) {
					input.setEncMbtlnum(CoreUtils.aes256.encrypt(input.getRegMbtlnum(), newTaskPartcptnEntrprsId));
				}
				if(CoreUtils.string.isNotBlank(input.getRegEmail())) {
					input.setEncEmail(CoreUtils.aes256.encrypt(input.getRegEmail(), newTaskPartcptnEntrprsId));
				}
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);

				if(usptTaskPrtcmpnyDao.insert(input) != 1) {
					throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(참여기업 저장 오류)");
				}
			}
		}

		/**과제참여자(참여인력)*/
		List <UsptTaskPartcpts> usptTaskPartcptsList = frontBsnsPlanDto.getUsptTaskPartcpts();
		if(usptTaskPartcptsList.size() > 0) {
			for(UsptTaskPartcpts input : usptTaskPartcptsList) {

				String newTaskPartcptsId = CoreUtils.string.getNewId(Code.prefix.과제참여자);

				input.setTaskPartcptsId(newTaskPartcptsId);
				input.setBsnsPlanDocId(newBsnsPlanDocId);
				if(CoreUtils.string.isNotBlank(input.getEncMbtlnum())) {
					input.setEncMbtlnum(CoreUtils.aes256.encrypt(input.getEncMbtlnum(), newTaskPartcptsId));
				}
				if(CoreUtils.string.isNotBlank(input.getEncBrthdy())) {
					input.setEncBrthdy(CoreUtils.aes256.encrypt(input.getEncBrthdy(), newTaskPartcptsId));
				}
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);

				if(usptTaskPartcptsDao.insert(input) != 1) {
					throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(참여인력 저장 오류)");
				}
			}
		}

		/**과제신청사업비(신청예산)*/
		List<UsptTaskReqstWct> usptTaskReqstWctList = frontBsnsPlanDto.getUsptTaskReqstWct();
		if(usptTaskReqstWctList.size() > 0) {
			for(UsptTaskReqstWct input : usptTaskReqstWctList) {
				String newTaskReqstWctId = CoreUtils.string.getNewId(Code.prefix.과제신청사업비);

				input.setTaskReqstWctId(newTaskReqstWctId);
				input.setBsnsPlanDocId(newBsnsPlanDocId);
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);

				if(usptTaskReqstWctDao.insert(input) != 1) {
					throw new InvalidationException(saveMsg + " 중 오류가 발생했습니다.(신청예산 저장 오류)");
				}
			}
		}
		/* 첨부파일 삭제 */
		List<CmmtAtchmnfl> attachFileDeleteList = frontBsnsPlanDto.getAttachFileDeleteList();
		if( attachFileDeleteList !=null && attachFileDeleteList.size() >0) {
			for(CmmtAtchmnfl attachInfo : attachFileDeleteList) {
				attachmentService.removeFile_keepEmptyGroup( config.getDir().getUpload(), attachInfo.getAttachmentId());
			}
		}
		return newBsnsPlanDocId;
	}

	/**
	 * 사업계획서 변경
	 * @param planPresentnSttusCd, frontBsnsPlanDto
	 * @return strBsnsPlanDocId
	 */
	public String modify( String planPresentnSttusCd, FrontBsnsPlanDto frontBsnsPlanDto, List<MultipartFile> fileList) {
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		String strMemberId = worker.getMemberId();
		String strBsnsPlanDocId ="";

		/**사업계획서*/
		UsptBsnsPlanDoc usptBsnsPlanDoc = frontBsnsPlanDto.getUsptBsnsPlanDoc();
		//사업계획서ID
		strBsnsPlanDocId = usptBsnsPlanDoc.getBsnsPlanDocId();

		/** 첨부파일*/
		if(fileList != null && fileList.size() > 0) {
			if(CoreUtils.string.isNotBlank(usptBsnsPlanDoc.getPrsentrAttachmentGroupId())) {
				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), usptBsnsPlanDoc.getPrsentrAttachmentGroupId(), fileList, null, 0);
			} else {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
				usptBsnsPlanDoc.setPrsentrAttachmentGroupId(fileGroupInfo.getAttachmentGroupId());
			}
		}

		//사업계획서 변경
		usptBsnsPlanDoc.setPlanPresentnSttusCd(planPresentnSttusCd);
		if(CoreUtils.string.equals(planPresentnSttusCd, Code.planPresentnSttus.제출)) {
			usptBsnsPlanDoc.setPresentnDt(now);
			usptBsnsPlanDoc.setPrsentrId(strMemberId);
		}
		/*사업계획신청자ID*/
		usptBsnsPlanDoc.setMemberId(strMemberId);
		usptBsnsPlanDoc.setUpdatedDt(now);
		usptBsnsPlanDoc.setUpdaterId(strMemberId);
		//변경
		usptBsnsPlanDocDao.update(usptBsnsPlanDoc);

		/**사업계획신청자ID*/
		//사업계획신청자 회원정보 조회
		CmmtMember memberInfo = cmmtMemberDao.select(strMemberId);
		/** 개인사업자구분코드 */
		String indvdlBsnmDivCd ="";
		if(CoreUtils.string.equals(memberInfo.getMemberType(), Code.memberType.개인사업자)){
			indvdlBsnmDivCd = Code.indvdlBsnmDiv.개인;
		}else {
			indvdlBsnmDivCd = Code.indvdlBsnmDiv.사업자;
		}
		//사업계획신청자 조회
		UsptBsnsPlanApplcnt usptBsnsPlanApplcntParam = new UsptBsnsPlanApplcnt();
		usptBsnsPlanApplcntParam.setBsnsPlanDocId(strBsnsPlanDocId);					/** 사업계획서ID */
		UsptBsnsPlanApplcnt usptBsnsPlanApplcnt = usptBsnsPlanApplcntDao.selectOne(usptBsnsPlanApplcntParam);

		String bsnsPlanApplcntId = usptBsnsPlanApplcnt.getBsnsPlanApplcntId();

		usptBsnsPlanApplcnt.setApplcntNm(memberInfo.getMemberNm());			/** 신청자명 */
		usptBsnsPlanApplcnt.setGenderCd(memberInfo.getGender());					/** 성별코드(G:GENDER) */
		if(!CoreUtils.string.isBlank(memberInfo.getMobileNo())) {
			usptBsnsPlanApplcnt.setEncMbtlnum(CoreUtils.aes256.encrypt(memberInfo.getMobileNo(), bsnsPlanApplcntId));	/** 암호화된 휴대폰번호 */
		}
		if(!CoreUtils.string.isBlank(memberInfo.getBirthday())) {
			usptBsnsPlanApplcnt.setEncBrthdy(CoreUtils.aes256.encrypt(memberInfo.getBirthday(), bsnsPlanApplcntId));		/** 암호화된 생년월일 */
		}
		if(!CoreUtils.string.isBlank(memberInfo.getEmail())) {
			usptBsnsPlanApplcnt.setEncEmail(CoreUtils.aes256.encrypt(memberInfo.getEmail(), bsnsPlanApplcntId));				/** 암호화된 이메일 */
		}
		usptBsnsPlanApplcnt.setNativeYn(null);												/** 내국인여부 */
		usptBsnsPlanApplcnt.setIndvdlBsnmDivCd(indvdlBsnmDivCd);					/** 개인사업자구분코드 */
		usptBsnsPlanApplcnt.setChargerNm(memberInfo.getChargerNm());			/** 담당자명 */
		usptBsnsPlanApplcnt.setCeoNm(memberInfo.getCeoNm());						 /** 대표자명 */
		usptBsnsPlanApplcnt.setBizrno(memberInfo.getBizrno());						 /** 사업자번호(기업회원) */
		usptBsnsPlanApplcnt.setUpdatedDt(now);
		usptBsnsPlanApplcnt.setUpdaterId(strMemberId);
		usptBsnsPlanApplcntDao.update(usptBsnsPlanApplcnt);

		/**과제책임자*/
		UsptTaskRspnber usptTaskRspnber = frontBsnsPlanDto.getUsptTaskRspnber();
		if(usptTaskRspnber !=null) {
			if(CoreUtils.string.isNotBlank(usptTaskRspnber.getTaskRspnberId())) {
				String taskRspnberId= usptTaskRspnber.getTaskRspnberId();
				usptTaskRspnber.setEncMbtlnum(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncMbtlnum(), taskRspnberId));
				usptTaskRspnber.setEncBrthdy(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncBrthdy(), taskRspnberId));
				usptTaskRspnber.setEncTelno(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncTelno(), taskRspnberId));
				usptTaskRspnber.setEncEmail(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncEmail(), taskRspnberId));
				usptTaskRspnber.setEncFxnum(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncFxnum(), taskRspnberId));
				usptTaskRspnber.setUpdatedDt(now);
				usptTaskRspnber.setUpdaterId(strMemberId);
				//변경
				usptTaskRspnberDao.update(usptTaskRspnber);
			}else {
				String newTaskRspnberId = CoreUtils.string.getNewId(Code.prefix.과제책임자);
				usptTaskRspnber.setTaskRspnberId(newTaskRspnberId);
				usptTaskRspnber.setBsnsPlanDocId(strBsnsPlanDocId);
				usptTaskRspnber.setEncMbtlnum(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncMbtlnum(), newTaskRspnberId));
				usptTaskRspnber.setEncBrthdy(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncBrthdy(), newTaskRspnberId));
				usptTaskRspnber.setEncTelno(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncTelno(), newTaskRspnberId));
				usptTaskRspnber.setEncEmail(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncEmail(), newTaskRspnberId));
				usptTaskRspnber.setEncFxnum(CoreUtils.aes256.encrypt(usptTaskRspnber.getEncFxnum(), newTaskRspnberId));
				usptTaskRspnber.setCreatedDt(now);
				usptTaskRspnber.setCreatorId(strMemberId);
				usptTaskRspnber.setUpdatedDt(now);
				usptTaskRspnber.setUpdaterId(strMemberId);
				usptTaskRspnberDao.insert(usptTaskRspnber);
			}
		}
		/**과제참여기업(참여기업)*/
		List <UsptTaskPrtcmpny> usptTaskPrtcmpnyList = frontBsnsPlanDto.getUsptTaskPrtcmpny();
		CmmtMember memberInfoCompany = new CmmtMember();
		//전체 삭제
		usptTaskPrtcmpnyDao.deleteBsnsPlanDocIdAll(strBsnsPlanDocId);
		//등록
		if(usptTaskPrtcmpnyList.size() >0) {
			for(UsptTaskPrtcmpny input : usptTaskPrtcmpnyList) {

				String newTaskPartcptnEntrprsId = CoreUtils.string.getNewId(Code.prefix.과제참여기업);

				input.setTaskPartcptnEntrprsId(newTaskPartcptnEntrprsId);
				if(string.isNotBlank(input.getMemberId())){
					//업체명 조회
					memberInfoCompany = cmmtMemberDao.select(input.getMemberId());
					input.setEntrpsNm(memberInfoCompany.getMemberNm());	/** 업체명 */
				}
				input.setBsnsPlanDocId(strBsnsPlanDocId);
				if(CoreUtils.string.isNotBlank(input.getRegTelno())) {
					input.setEncTelno(CoreUtils.aes256.encrypt(input.getRegTelno(), newTaskPartcptnEntrprsId));
				}
				if(CoreUtils.string.isNotBlank(input.getRegMbtlnum())) {
					input.setEncMbtlnum(CoreUtils.aes256.encrypt(input.getRegMbtlnum(), newTaskPartcptnEntrprsId));
				}
				if(CoreUtils.string.isNotBlank(input.getRegEmail())) {
					input.setEncEmail(CoreUtils.aes256.encrypt(input.getRegEmail(), newTaskPartcptnEntrprsId));
				}
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptTaskPrtcmpnyDao.insert(input);
			}
		}

		/**과제참여자(참여인력)*/
		//TODO 소속기관의 존재유무 체크, 전체 삭제 후 등록
		List <UsptTaskPartcpts> usptTaskPartcptsList = frontBsnsPlanDto.getUsptTaskPartcpts();
		CmmtMember memberInfoYn = new CmmtMember();

		//전체 삭제
		usptTaskPartcptsDao.deleteBsnsPlanDocIdAll(strBsnsPlanDocId);
		//등록
		if(usptTaskPartcptsList.size() >0) {
			for(UsptTaskPartcpts input : usptTaskPartcptsList) {

				//소속기관 회원테이블 등록 여부
				memberInfoYn = cmmtMemberDao.select(input.getMemberId());
				if(memberInfoYn == null) {
					throw new InvalidationException("등록되지 않은 소속기관 입니다.");
				}
				String newTaskPartcptsId = CoreUtils.string.getNewId(Code.prefix.과제참여자);
				input.setTaskPartcptsId(newTaskPartcptsId);
				input.setBsnsPlanDocId(strBsnsPlanDocId);
				if(CoreUtils.string.isNotBlank(input.getEncMbtlnum())) {
					input.setEncMbtlnum(CoreUtils.aes256.encrypt(input.getEncMbtlnum(), newTaskPartcptsId));
				}
				if(CoreUtils.string.isNotBlank(input.getEncBrthdy())) {
					input.setEncBrthdy(CoreUtils.aes256.encrypt(input.getEncBrthdy(), newTaskPartcptsId));
				}
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptTaskPartcptsDao.insert(input);
			}
		}
		/**과제신청사업비(신청예산)*/
		List<UsptTaskReqstWct> usptTaskReqstWctList = frontBsnsPlanDto.getUsptTaskReqstWct();
		if(usptTaskReqstWctList.size() >0) {
			if(CoreUtils.string.isNotBlank(usptTaskReqstWctList.get(0).getTaskReqstWctId())) {
				for(UsptTaskReqstWct input : usptTaskReqstWctList) {
					input.setCreatedDt(now);
					input.setCreatorId(strMemberId);
					input.setUpdatedDt(now);
					input.setUpdaterId(strMemberId);
					usptTaskReqstWctDao.update(input);
				}
			}else {
				for(UsptTaskReqstWct input : usptTaskReqstWctList) {
					String newTaskReqstWctId = CoreUtils.string.getNewId(Code.prefix.과제신청사업비);
					input.setTaskReqstWctId(newTaskReqstWctId);
					input.setBsnsPlanDocId(strBsnsPlanDocId);
					input.setCreatedDt(now);
					input.setCreatorId(strMemberId);
					input.setUpdatedDt(now);
					input.setUpdaterId(strMemberId);
					usptTaskReqstWctDao.insert(input);
				}
			}
		}

		/* 첨부파일 삭제 */
		List<CmmtAtchmnfl> attachFileDeleteList = frontBsnsPlanDto.getAttachFileDeleteList();
		if( attachFileDeleteList !=null && attachFileDeleteList.size() >0) {
			for(CmmtAtchmnfl attachInfo : attachFileDeleteList) {
				attachmentService.removeFile_keepEmptyGroup( config.getDir().getUpload(), attachInfo.getAttachmentId());
			}
		}
		return strBsnsPlanDocId;
	}

	/**
	 * 사업계획서 참여기업 조회
	 * @param bsnsPlanDocId
	 * @return
	 */
	public List<UsptTaskPrtcmpny> selectUsptTaskPrtcmpnyList( String bsnsPlanDocId){
		/**참여기업 조회*/
		List <UsptTaskPrtcmpny> usptTaskPrtcmpnyList = usptTaskPrtcmpnyDao.selectBoxList(bsnsPlanDocId);
		return usptTaskPrtcmpnyList;
	}

	/**
	 * 사업계획서 기업검색 팝업
	 * @param frontBsnsPlanParam
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	public CorePagination<CmmtMemberBizDto> selectBizList(FrontBsnsPlanParam frontBsnsPlanParam,  Long page){
		SecurityUtils.checkWorkerIsMember();

		if(page == null) {
			page = 1L;
		}
		if(frontBsnsPlanParam.getItemsPerPage() == null) {
			frontBsnsPlanParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = frontBsnsPlanParam.getItemsPerPage();

		//전체건수 조회
		long totalItems = cmmtMemberDao.selectBizListTotCnt(frontBsnsPlanParam.getMemberNm());

		CmmtMemberBizDto cmmtMemberBizDto = new CmmtMemberBizDto();
		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		cmmtMemberBizDto.setMemberNm(frontBsnsPlanParam.getMemberNm());
		cmmtMemberBizDto.setBeginRowNum(info.getBeginRowNum());
		cmmtMemberBizDto.setItemsPerPage(itemsPerPage);
		List<CmmtMemberBizDto> list = cmmtMemberDao.selectBizList(cmmtMemberBizDto);

		CorePagination<CmmtMemberBizDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 사업계획서 파일 단건 다운
	 * @param frontBsnsPlanParam
	 * @return
	 */
	public void getOneFileDown(HttpServletResponse response, String attachmentId) {
		SecurityUtils.checkWorkerIsMember();
		if (string.isNotBlank(attachmentId)) {
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		}
	}
}