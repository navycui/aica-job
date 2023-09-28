package aicluster.pms.api.cnvnchangehist.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.cnvnchange.dto.CnvnChangeParam;
import aicluster.pms.api.cnvnchangehist.dto.CnvnChangeHistBaseInfoDto;
import aicluster.pms.api.cnvnchangehist.dto.CnvnChangeHistParam;
import aicluster.pms.api.cnvnchangehist.dto.UsptCnvnApplcntChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptCnvnSclpstChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptCnvnTaskInfoChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptTaskPartcptsChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptTaskPrtcmpnyInfoChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptTaskReqstWctChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptTaskRspnberChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptTaskTaxitmWctChangeHistDto;
import aicluster.pms.common.dao.UsptCnvnApplcntHistDao;
import aicluster.pms.common.dao.UsptCnvnChangeReqDao;
import aicluster.pms.common.dao.UsptCnvnSclpstHistDao;
import aicluster.pms.common.dao.UsptCnvnTaskInfoHistDao;
import aicluster.pms.common.dao.UsptTaskPartcptsHistDao;
import aicluster.pms.common.dao.UsptTaskPrtcmpnyHistDao;
import aicluster.pms.common.dao.UsptTaskPrtcmpnyInfoHistDao;
import aicluster.pms.common.dao.UsptTaskReqstWctHistDao;
import aicluster.pms.common.dao.UsptTaskRspnberHistDao;
import aicluster.pms.common.dao.UsptTaskTaxitmWctHistDao;
import aicluster.pms.common.dto.CnvnChangeChangeIemDivDto;
import aicluster.pms.common.dto.CnvnChangeDto;
import aicluster.pms.common.entity.UsptCnvnApplcntHist;
import aicluster.pms.common.entity.UsptCnvnChangeReq;
import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;
import aicluster.pms.common.entity.UsptCnvnSclpstHist;
import aicluster.pms.common.entity.UsptCnvnTaskInfoHist;
import aicluster.pms.common.entity.UsptTaskPartcptsHist;
import aicluster.pms.common.entity.UsptTaskPrtcmpnyHist;
import aicluster.pms.common.entity.UsptTaskPrtcmpnyInfoHist;
import aicluster.pms.common.entity.UsptTaskReqstWctHist;
import aicluster.pms.common.entity.UsptTaskRspnberHist;
import aicluster.pms.common.entity.UsptTaskTaxitmWctHist;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;


@Service
public class CnvnChangeHistService {
	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private UsptCnvnChangeReqDao usptCnvnChangeReqDao;		/*협약변경요청*/
	@Autowired
	private UsptCnvnSclpstHistDao usptCnvnSclpstHistDao;		/*협약수행기관신분변경이력*/
	@Autowired
	private UsptCnvnTaskInfoHistDao usptCnvnTaskInfoHistDao;		/*협약과제정보변경이력*/
	@Autowired
	private UsptTaskPrtcmpnyInfoHistDao usptTaskPrtcmpnyInfoHistDao;		/*과제참여기업정보변경이력*/
	@Autowired
	private UsptTaskPrtcmpnyHistDao usptTaskPrtcmpnyHistDao;		/*과제참여기업변경이력*/
	@Autowired
	private UsptTaskPartcptsHistDao usptTaskPartcptsHistDao;		/*과제참여자변경이력*/
	@Autowired
	private UsptTaskRspnberHistDao usptTaskRspnberHistDao;		/*과제책임자변경이력*/
	@Autowired
	private UsptCnvnApplcntHistDao usptCnvnApplcntHistDao;		/*협약신청자정보변경이력*/
	@Autowired
	private UsptTaskReqstWctHistDao usptTaskReqstWctHistDao;		/*과제신청사업비변경이력*/
	@Autowired
	private UsptTaskTaxitmWctHistDao usptTaskTaxitmWctHistDao;		/*과제세목별사업비변경이력*/

	/****************************************협약변경내역 ****************************************************/
	/**
	 * 협약변경내역 조회
	 * @param
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	public CorePagination<CnvnChangeDto> selectProcessHist(CnvnChangeHistParam cnvnChangeHistParam, Long page){
		SecurityUtils.checkWorkerIsInsider();

		if(page == null) {
			page = 1L;
		}
		if(cnvnChangeHistParam.getItemsPerPage() == null) {
			cnvnChangeHistParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = cnvnChangeHistParam.getItemsPerPage();

		//전체건수 조회
		long totalItems = usptCnvnChangeReqDao.selectProcessHistCnt(cnvnChangeHistParam);

		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		cnvnChangeHistParam.setBeginRowNum(info.getBeginRowNum());
		cnvnChangeHistParam.setItemsPerPage(itemsPerPage);
		List<CnvnChangeDto> list = usptCnvnChangeReqDao.selectProcessHistList(cnvnChangeHistParam);

		CorePagination<CnvnChangeDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 협약변경내역 목록 엑셀 다운로드
	 * @param
	 */
	public List<CnvnChangeDto> getListExcel(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		//전체건수 조회
		long totalItems = usptCnvnChangeReqDao.selectProcessHistCnt(cnvnChangeHistParam);

		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		cnvnChangeHistParam.setBeginRowNum(info.getBeginRowNum());
		cnvnChangeHistParam.setItemsPerPage(totalItems);
		return  usptCnvnChangeReqDao.selectProcessHistList(cnvnChangeHistParam);
	}

	/****************************************협약변경내역 상세 조회 ****************************************************/
	/**
	 * 협약변경내역 기본정보 및 변경항목 조회
	 * @param   bsnsCnvnId(사업협약ID)
	 * @return
	 */
	public CnvnChangeHistBaseInfoDto selectCnvnChangeDetailInfo(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		String  bsnsCnvnId = cnvnChangeHistParam.getBsnsCnvnId();  /** 사업협약ID */
		//신청상세 기본정보 조회
		CnvnChangeDto cnvnChangeDto =  usptCnvnChangeReqDao.selectProcessHistBaseInfo(bsnsCnvnId);
		//신청상세 변경항목 조회
		List<CnvnChangeChangeIemDivDto>  cnvnChangeChangeIemDivDtoList= usptCnvnChangeReqDao.selectChangeChangeIemDiv( bsnsCnvnId);

		//리턴 셋팅
		CnvnChangeHistBaseInfoDto cnvnChangeHistBaseInfoDto = new CnvnChangeHistBaseInfoDto();
		cnvnChangeHistBaseInfoDto.setCnvnChangeHistBaseInfo(cnvnChangeDto);
		cnvnChangeHistBaseInfoDto.setCnvnChangeChangeIemDivList(cnvnChangeChangeIemDivDtoList);

		return cnvnChangeHistBaseInfoDto;
	}


	/**
	 * 협약변경관리_변경 항목별 전체 변경 일
	 * @param   cnvnChangeReqId(협약변경요청ID), changeIemDivCd(협약변경항목구분코드)
	 * @return
	 */
	public List<UsptCnvnChangeReqHistDt> selectCnvnChangeReqHistDe(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeHistParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeIemDivCd =	 cnvnChangeHistParam.getChangeIemDivCd();		 /** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */

		List<UsptCnvnChangeReqHistDt> returnReqHistDt = new ArrayList<>();
		 if(CoreUtils.string.equals( Code.changeIemDiv.수행기관신분, changeIemDivCd)) {
			 returnReqHistDt = usptCnvnSclpstHistDao.selectUsptCnvnSclpstHistChangeDe(cnvnChangeReqId);
		 }else if(CoreUtils.string.equals( Code.changeIemDiv.과제정보, changeIemDivCd)) {
			 returnReqHistDt = usptCnvnTaskInfoHistDao.selectUsptCnvnTaskInfoHistChangeDe(cnvnChangeReqId);
		 }else if(CoreUtils.string.equals( Code.changeIemDiv.참여기업, changeIemDivCd)) {
			 returnReqHistDt = usptTaskPrtcmpnyInfoHistDao.selectUsptTaskPrtcmpnyInfoHistChangeDe(cnvnChangeReqId);
		 }else if(CoreUtils.string.equals( Code.changeIemDiv.참여인력, changeIemDivCd)) {
			 returnReqHistDt = usptTaskPartcptsHistDao.selectUsptTaskPartcptsHistChangeDe(cnvnChangeReqId);
		 }else if(CoreUtils.string.equals( Code.changeIemDiv.사업비, changeIemDivCd)) {
			 returnReqHistDt = usptTaskReqstWctHistDao.selectUsptTaskReqstWctHistChangeDe(cnvnChangeReqId);
		 }else if(CoreUtils.string.equals( Code.changeIemDiv.비목별사업비, changeIemDivCd)) {
			 returnReqHistDt = usptTaskTaxitmWctHistDao.selectUsptTaskTaxitmWctHistChangeDe(cnvnChangeReqId);
		 }else if(CoreUtils.string.equals( Code.changeIemDiv.신청자정보, changeIemDivCd)) {
			 returnReqHistDt =  usptCnvnApplcntHistDao.selectUsptCnvnApplcntHistChangeDe(cnvnChangeReqId);
		 }else if(CoreUtils.string.equals( Code.changeIemDiv.과제책임자, changeIemDivCd)) {
			 returnReqHistDt =  usptTaskRspnberHistDao.selectUsptTaskRspnberHistChangeDe(cnvnChangeReqId);
		 }
		return returnReqHistDt;
	}

	/****************************************수행기관신분변경 start****************************************************/
	/**
	 * 협약변경관리 수행기관신분변경 이력조회 CHIE01
	 * @param   cnvnChangeReqId(협약변경요청ID), changeDt(생성일시)
	 * @return
	 */
	public UsptCnvnSclpstChangeHistDto selectChangeCnvnSclpstHist(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeHistParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeDt = cnvnChangeHistParam.getChangeDt();		/*생성일시*/

		UsptCnvnSclpstChangeHistDto usptCnvnSclpstChangeHistDto= new UsptCnvnSclpstChangeHistDto();

		/***************************수행기관 변경 후 내용*/
		//협약수행기관신분변경 최근이력조회
		List<UsptCnvnSclpstHist>  usptCnvnSclpstChangeHistList = usptCnvnSclpstHistDao.selectUsptCnvnSclpstHistByDt(cnvnChangeReqId, changeDt);

		for(UsptCnvnSclpstHist outParam : usptCnvnSclpstChangeHistList ) {
			 if(CoreUtils.string.equals( "before", outParam.getChangeBeAf())) {
				 usptCnvnSclpstChangeHistDto.setUsptCnvnSclpstHistBefore(outParam);
			 }else if(CoreUtils.string.equals( "after", outParam.getChangeBeAf())) {
				 usptCnvnSclpstChangeHistDto.setUsptCnvnSclpstHistAfter(outParam);
			 }
		}
		return usptCnvnSclpstChangeHistDto;
	}

	/****************************************과제정보****************************************************/
	/**
	 * 협약변경내역 과제정보 조회
	 * @param   cnvnChangeReqId(협약변경요청ID), changeDt(생성일시)
	 * @return
	 */
	public UsptCnvnTaskInfoChangeHistDto selectChangeCnvnTaskInfoHist(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeHistParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.과제정보;					/**협약변경항목구분 - CHIE02*/
		String changeDt = cnvnChangeHistParam.getChangeDt();		/*생성일시*/

		UsptCnvnTaskInfoChangeHistDto usptCnvnTaskInfoChangeHistDto = new UsptCnvnTaskInfoChangeHistDto();

		//기본정보 조회
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		usptCnvnChangeReq = usptCnvnChangeReqDao.selectCnvnChangeBaseInfo(cnvnChangeReqId, changeIemDivCd );

		 //과제정보 사업기간
		Date bsnsBgndt = string.toDate(usptCnvnChangeReq.getBsnsBgnde());
		Date bsnsEnddt = string.toDate(usptCnvnChangeReq.getBsnsEndde());

		/**********************************************과제정보 변경 내용*/
		//조회
		List <UsptCnvnTaskInfoHist> usptCnvnTaskInfoChangeHistList = usptCnvnTaskInfoHistDao.selectUsptCnvnTaskInfoHistByDt(cnvnChangeReqId, changeDt);

		for(UsptCnvnTaskInfoHist outParam : usptCnvnTaskInfoChangeHistList ) {
			 if(CoreUtils.string.equals( "before", outParam.getChangeBeAf())) {
				 if (bsnsBgndt != null && bsnsEnddt != null ) {
						Date lastdt = string.toDate(date.getCurrentDate("yyyy") + "1231");
						outParam.setBsnsPd("협약 체결일 ~ " + date.format(bsnsEnddt, "yyyy-MM-dd"));
						int allDiffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, bsnsEnddt);
						if(date.compareDay(bsnsEnddt, lastdt) == 1) {
							int diffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, lastdt);
							outParam.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(lastdt, "yyyy-MM-dd") + "(" + diffMonths + "개월)");
						} else {
							outParam.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
						}
						outParam.setBsnsPdAll(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
					}
				 usptCnvnTaskInfoChangeHistDto.setUsptCnvnTaskInfoHistBefore(outParam);
			 }else if(CoreUtils.string.equals( "after", outParam.getChangeBeAf())) {
				 if (bsnsBgndt != null && bsnsEnddt != null ) {
						Date lastdt = string.toDate(date.getCurrentDate("yyyy") + "1231");
						outParam.setBsnsPd("협약 체결일 ~ " + date.format(bsnsEnddt, "yyyy-MM-dd"));
						int allDiffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, bsnsEnddt);
						if(date.compareDay(bsnsEnddt, lastdt) == 1) {
							int diffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, lastdt);
							outParam.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(lastdt, "yyyy-MM-dd") + "(" + diffMonths + "개월)");
						} else {
							outParam.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
						}
						outParam.setBsnsPdAll(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
					}
				 usptCnvnTaskInfoChangeHistDto.setUsptCnvnTaskInfoHistAfter(outParam);
			 }
		}
		return usptCnvnTaskInfoChangeHistDto;
	}

	/****************************************참여기업****************************************************/
	/**
	 * 협약변경내역 참여기업 조회 CHIE03
	 * @param   cnvnChangeReqId(협약변경요청ID), changeDt(생성일시)
	 * @return
	 */
	public UsptTaskPrtcmpnyInfoChangeHistDto selectChangeTaskPrtcmpnyInfoHist(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeHistParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeDt = cnvnChangeHistParam.getChangeDt();		/*생성일시*/

		UsptTaskPrtcmpnyInfoChangeHistDto usptTaskPrtcmpnyInfoChangeHistDto = new UsptTaskPrtcmpnyInfoChangeHistDto();

		/********************************************** 과제참여기업정보변경이력 변경 정보*/
		/** 과제참여기업정보변경이력 변경  내용 */
		String taskPrtcmpnyInfoHistIdBefore = "";		//과제참여기업정보변경이력ID
		String taskPrtcmpnyInfoHistIdAfter = "";
		//조회
		List <UsptTaskPrtcmpnyInfoHist> usptTaskPrtcmpnyInfoChangeHistList = usptTaskPrtcmpnyInfoHistDao.selectUsptTaskPrtcmpnyInfoHistByDt(cnvnChangeReqId, changeDt);
		for(UsptTaskPrtcmpnyInfoHist outParam : usptTaskPrtcmpnyInfoChangeHistList ) {
			 if(CoreUtils.string.equals( "before", outParam.getChangeBeAf())) {
				 usptTaskPrtcmpnyInfoChangeHistDto.setUsptTaskPrtcmpnyInfoHistBefore(outParam);
				 taskPrtcmpnyInfoHistIdBefore = outParam.getTaskPrtcmpnyInfoHistId();			//과제참여기업정보변경이력ID
			 }else if(CoreUtils.string.equals( "after", outParam.getChangeBeAf())) {
				 usptTaskPrtcmpnyInfoChangeHistDto.setUsptTaskPrtcmpnyInfoHistAfter(outParam);
				 taskPrtcmpnyInfoHistIdAfter = outParam.getTaskPrtcmpnyInfoHistId();				//과제참여기업정보변경이력ID
			 }
		}

		/** 과제참여기업변경이력 변경 전 내용 */
		List<UsptTaskPrtcmpnyHist> usptTaskPrtcmpnyBeforeHistList= usptTaskPrtcmpnyHistDao.selectList(taskPrtcmpnyInfoHistIdBefore);

		for(UsptTaskPrtcmpnyHist usptTaskPrtcmpnyHistInfo : usptTaskPrtcmpnyBeforeHistList) {
			usptTaskPrtcmpnyHistInfo.setEncTelno(usptTaskPrtcmpnyHistInfo.getTelno());				/* 복호화된 전화번호 */
			usptTaskPrtcmpnyHistInfo.setEncMbtlnum(usptTaskPrtcmpnyHistInfo.getMbtlnum());		/* 복호화된 휴대폰번호 */
			usptTaskPrtcmpnyHistInfo.setEncEmail(usptTaskPrtcmpnyHistInfo.getEmail());				/* 복호화된 이메일 */
		}
		//셋팅
		usptTaskPrtcmpnyInfoChangeHistDto.setUsptTaskPrtcmpnyHistBeforeList(usptTaskPrtcmpnyBeforeHistList);

		/** 과제참여기업변경이력 변경 후 내용 */
		List<UsptTaskPrtcmpnyHist> usptTaskPrtcmpnyAfterHistList= usptTaskPrtcmpnyHistDao.selectList(taskPrtcmpnyInfoHistIdAfter);

		for(UsptTaskPrtcmpnyHist usptTaskPrtcmpnyHistInfo : usptTaskPrtcmpnyAfterHistList) {
			usptTaskPrtcmpnyHistInfo.setEncTelno(usptTaskPrtcmpnyHistInfo.getTelno());				/* 복호화된 전화번호 */
			usptTaskPrtcmpnyHistInfo.setEncMbtlnum(usptTaskPrtcmpnyHistInfo.getMbtlnum());		/* 복호화된 휴대폰번호 */
			usptTaskPrtcmpnyHistInfo.setEncEmail(usptTaskPrtcmpnyHistInfo.getEmail());				/* 복호화된 이메일 */
		}
		//셋팅
		usptTaskPrtcmpnyInfoChangeHistDto.setUsptTaskPrtcmpnyHistAfterList(usptTaskPrtcmpnyAfterHistList);

		return usptTaskPrtcmpnyInfoChangeHistDto;
	}

	/****************************************과제참여인력변경이력 start****************************************************/
	/**
	 * 협약변경내역 참여인력 조회 CHIE04
	 * @param   cnvnChangeReqId(협약변경요청ID), changeDt(생성일시)
	 * @return
	 */
	public  UsptTaskPartcptsChangeHistDto selectChangeTaskPartcptsHist(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId =cnvnChangeHistParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeDt = cnvnChangeHistParam.getChangeDt();		/*생성일시*/

		UsptTaskPartcptsChangeHistDto usptTaskPartcptsChangeHistDto = new UsptTaskPartcptsChangeHistDto();


		/********************************************** 참여인력 변경 후 정보*/
		List<UsptTaskPartcptsHist>  usptTaskPartcptsChangeHistList = usptTaskPartcptsHistDao.selectUsptTaskPartcptsHistByDt(cnvnChangeReqId, changeDt);
		List<UsptTaskPartcptsHist>  returnBefore  = new ArrayList<>();
		List<UsptTaskPartcptsHist>   returnAfter  = new ArrayList<>();

		for(UsptTaskPartcptsHist outParam : usptTaskPartcptsChangeHistList) {
			 if(CoreUtils.string.equals( "before", outParam.getChangeBeAf())) {
				 outParam.setEncMbtlnum(outParam.getMbtlnum());
				 outParam.setEncBrthdy(outParam.getBrthdy());
				 returnBefore.add(outParam);
			 }else if(CoreUtils.string.equals( "after", outParam.getChangeBeAf())) {
				 outParam.setEncMbtlnum(outParam.getMbtlnum());
				 outParam.setEncBrthdy(outParam.getBrthdy());
				 returnAfter.add(outParam);
			 }
		}
		//내용 셋팅
		usptTaskPartcptsChangeHistDto.setUsptTaskPartcptsHistBeforeList(returnBefore);
		usptTaskPartcptsChangeHistDto.setUsptTaskPartcptsHistAfterList(returnAfter);

		return usptTaskPartcptsChangeHistDto;
	}

	/****************************************과제신청사업비변경이력 start****************************************************/
	/**
	 * 협약변경내역 과제신청사업비변경이력 조회	 CHIE05
	 * @param  cnvnChangeReqId(협약변경요청ID) changeDt(생성일시)
	 * @return
	 */
	public UsptTaskReqstWctChangeHistDto selectChangeTaskReqstWctHist(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeHistParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeDt = cnvnChangeHistParam.getChangeDt();		/*생성일시*/

		UsptTaskReqstWctChangeHistDto usptTaskReqstWctChangeHistDto = new UsptTaskReqstWctChangeHistDto();


		/********************************************** 과제신청사업비 변경 후 정보*/
		/** 과제신청사업비변경이력*/
		List<UsptTaskReqstWctHist> usptTaskReqstWctChangeHistList  = usptTaskReqstWctHistDao.selectUsptTaskReqstWctHistByDt(cnvnChangeReqId, changeDt);
		List<UsptTaskReqstWctHist>  returnBefore  = new ArrayList<>();
		List<UsptTaskReqstWctHist>   returnAfter  = new ArrayList<>();

		for(UsptTaskReqstWctHist outParam : usptTaskReqstWctChangeHistList) {
			 if(CoreUtils.string.equals( "before", outParam.getChangeBeAf())) {
				 returnBefore.add(outParam);
			 }else if(CoreUtils.string.equals( "after", outParam.getChangeBeAf())) {
				 returnAfter.add(outParam);
			 }
		}

		//후 내용 셋팅
		usptTaskReqstWctChangeHistDto.setTotalWct(returnBefore.get(0).getTotalWct());			//총사업비
		usptTaskReqstWctChangeHistDto.setUsptTaskReqstWctHistBeforeList(returnBefore);
		usptTaskReqstWctChangeHistDto.setUsptTaskReqstWctHistAfterList(returnAfter);

		return usptTaskReqstWctChangeHistDto;
	}

	/****************************************과제세목별사업비변경 start****************************************************/
	/**
	 * 협약변경내역 과제세목별사업비변경 조회 CHIE06
	 * @param  cnvnChangeReqId(협약변경요청ID) changeDt(생성일시)
	 * @return
	 */
	public UsptTaskTaxitmWctChangeHistDto selectChangeTaskTaxitmWctHist(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeHistParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeDt = cnvnChangeHistParam.getChangeDt();		/*생성일시*/

		UsptTaskTaxitmWctChangeHistDto usptTaskTaxitmWctChangeHistDto = new UsptTaskTaxitmWctChangeHistDto();

		/********************************************** 과제세목별사업비변경 변경 후 정보*/
		List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctChangeHistAfterList = usptTaskTaxitmWctHistDao.selectUsptTaskTaxitmWctHistByDt(cnvnChangeReqId, changeDt);
		List<UsptTaskTaxitmWctHist>  returnBefore  = new ArrayList<>();
		List<UsptTaskTaxitmWctHist>   returnAfter  = new ArrayList<>();
		for(UsptTaskTaxitmWctHist outParam : usptTaskTaxitmWctChangeHistAfterList) {
			 if(CoreUtils.string.equals( "before", outParam.getChangeBeAf())) {
				 returnBefore.add(outParam);
			 }else if(CoreUtils.string.equals( "after", outParam.getChangeBeAf())) {
				 returnAfter.add(outParam);
			 }
		}
		//후 내용 셋팅
		usptTaskTaxitmWctChangeHistDto.setUsptTaskTaxitmWctHistBeforeList(returnBefore);
		usptTaskTaxitmWctChangeHistDto.setUsptTaskTaxitmWctHistAfterList(returnAfter);

		return usptTaskTaxitmWctChangeHistDto;
	}

	/****************************************협약신청자정보변경이력 start****************************************************/
	/**
	 * 협약변경내역 협약신청자정보변경이력 조회		CHIE07
	 * @param  cnvnChangeReqId(협약변경요청ID) changeDt(생성일시)
	 * @return
	 */
	public UsptCnvnApplcntChangeHistDto selectChangeCnvnApplcntHist(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeHistParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeDt = cnvnChangeHistParam.getChangeDt();		/*생성일시*/

		UsptCnvnApplcntChangeHistDto usptCnvnApplcntChangeHistDto = new UsptCnvnApplcntChangeHistDto();

		/********************************************** 협약신청자정보변경이력 변경 후 정보*/
		/**협약신청자정보변경이력*/
		List<UsptCnvnApplcntHist> usptCnvnApplcntChangeHistList = usptCnvnApplcntHistDao.selectUsptCnvnApplcntHistByDt(cnvnChangeReqId, changeDt);

		for(UsptCnvnApplcntHist outParam : usptCnvnApplcntChangeHistList ) {
			 if(CoreUtils.string.equals( "before", outParam.getChangeBeAf())) {
				 outParam.setEncMbtlnum(outParam.getMbtlnum());				/** 복호화된 휴대폰번호 */
				 outParam.setEncEmail(outParam.getEmail());							/** 복호화된 이메일 */
				 outParam.setEncBrthdy(outParam.getBrthdy());						/** 복호화된 생년월일 */
				 usptCnvnApplcntChangeHistDto.setUsptCnvnApplcntHistBefore(outParam);
			 }else if(CoreUtils.string.equals( "after", outParam.getChangeBeAf())) {
				 outParam.setEncMbtlnum(outParam.getMbtlnum());				/** 복호화된 휴대폰번호 */
				 outParam.setEncEmail(outParam.getEmail());							/** 복호화된 이메일 */
				 outParam.setEncBrthdy(outParam.getBrthdy());						/** 복호화된 생년월일 */
				 usptCnvnApplcntChangeHistDto.setUsptCnvnApplcntHistAfter(outParam);
			 }
		}
		return usptCnvnApplcntChangeHistDto;
	}

	/****************************************과제책임자변경이력 start****************************************************/
	/**
	  * 협약변경내역 과제책임자 조회	 CHIE08
	 * @param    cnvnChangeReqId(협약변경요청ID) changeDt(생성일시)
	 * @return
	 */
	public UsptTaskRspnberChangeHistDto selectChangeTaskRspnberHist(CnvnChangeHistParam cnvnChangeHistParam){
		SecurityUtils.checkWorkerIsInsider();

		String cnvnChangeReqId = cnvnChangeHistParam.getCnvnChangeReqId();	/**협약변경요청ID*/
		String changeDt = cnvnChangeHistParam.getChangeDt();		/*생성일시*/
		//return 용
		UsptTaskRspnberChangeHistDto usptTaskRspnberChangeHistDto = new UsptTaskRspnberChangeHistDto();

		/********************************************** 과제책임자변경이력 변경 후 정보*/
		/**과제책임자변경이력*/
		List<UsptTaskRspnberHist> usptTaskRspnberChangeHistList = usptTaskRspnberHistDao.selectUsptTaskRspnberHistByDt(cnvnChangeReqId, changeDt);

		for(UsptTaskRspnberHist outParam : usptTaskRspnberChangeHistList ) {
			 if(CoreUtils.string.equals( "before", outParam.getChangeBeAf())) {
				 outParam.setEncBrthdy(outParam.getBrthdy());				/** 복호화된 생년월일*/
				 outParam.setEncEmail(outParam.getEmail());					/** 복호화된 이메일*/
				 outParam.setEncFxnum(outParam.getFxnum());				/** 복호화된 팩스번호*/
				 outParam.setEncMbtlnum(outParam.getMbtlnum());		/** 복호화된 휴대폰번호*/
				 outParam.setEncTelno(outParam.getTelno());					/** 복호화된 전화번호*/
				 usptTaskRspnberChangeHistDto.setUsptTaskRspnberHistBefore(outParam);
			 }else if(CoreUtils.string.equals( "after", outParam.getChangeBeAf())) {
				 outParam.setEncBrthdy(outParam.getBrthdy());				/** 복호화된 생년월일*/
				 outParam.setEncEmail(outParam.getEmail());					/** 복호화된 이메일*/
				 outParam.setEncFxnum(outParam.getFxnum());				/** 복호화된 팩스번호*/
				 outParam.setEncMbtlnum(outParam.getMbtlnum());		/** 복호화된 휴대폰번호*/
				 outParam.setEncTelno(outParam.getTelno());					/** 복호화된 전화번호*/
				 usptTaskRspnberChangeHistDto.setUsptTaskRspnberHistAfter(outParam);
			 }
		}
		return usptTaskRspnberChangeHistDto;
	}
}