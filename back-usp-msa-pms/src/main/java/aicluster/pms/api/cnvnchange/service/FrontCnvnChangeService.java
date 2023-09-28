package aicluster.pms.api.cnvnchange.service;

import java.util.ArrayList;
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
import aicluster.pms.api.cnvnchange.dto.FrontCnvnChangeParam;
import aicluster.pms.api.cnvnchange.dto.FrontCnvnChangeRequestDto;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnApplcntHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnSclpstHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptCnvnTaskInfoHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskPartcptsHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskPrtcmpnyInfoHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskReqstWctHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskRspnberHistDto;
import aicluster.pms.api.cnvnchange.dto.UsptTaskTaxitmWctHistDto;
import aicluster.pms.common.dao.CmmtCodeDao;
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.UsptBsnsCnvnDao;
import aicluster.pms.common.dao.UsptBsnsCnvnHistDao;
import aicluster.pms.common.dao.UsptBsnsPlanApplcntDao;
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
import aicluster.pms.common.entity.CmmtCode;
import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptBsnsCnvn;
import aicluster.pms.common.entity.UsptBsnsCnvnHist;
import aicluster.pms.common.entity.UsptBsnsPlanApplcnt;
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


@Service
public class FrontCnvnChangeService {
	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private CmmtCodeDao cmmtCodeDao;	/*코드*/
	@Autowired
	private AttachmentService attachmentService;		/*파일*/
	@Autowired
	private CmmtMemberDao cmmtMemberDao;	/*회원*/
	@Autowired
	private UsptBsnsCnvnDao usptBsnsCnvnDao;		/*사업협약*/
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



	/**
	 * 협약변경관리  변경요청 조회
	 * @param frontCnvnChangeParam(bsnsYear,taskNmKo, memberId)
	 * @return
	 */
	public FrontCnvnChangeRequestDto selectCnvnChangeReqOne(FrontCnvnChangeParam frontCnvnChangeParam){
		BnMember worker =  SecurityUtils.checkWorkerIsMember();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();

		if(CoreUtils.string.isBlank(frontCnvnChangeParam.getBsnsYear())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업년도"));
		}
		if(CoreUtils.string.isBlank(frontCnvnChangeParam.getTaskNmKo())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "과제명"));
		}

		/**조회-사업협약*/
		frontCnvnChangeParam.setMemberId(strMemberId);
		UsptBsnsCnvn usptBsnsCnvnResult =usptBsnsCnvnDao.selectCnvnChangeReqMaxOne(frontCnvnChangeParam);
		FrontCnvnChangeRequestDto frontCnvnChangeRequestDto = new FrontCnvnChangeRequestDto();

		if(usptBsnsCnvnResult != null) {
			frontCnvnChangeRequestDto.setBsnsNm(usptBsnsCnvnResult.getBsnsNm());
			frontCnvnChangeRequestDto.setBsnsYear(usptBsnsCnvnResult.getBsnsYear());
			frontCnvnChangeRequestDto.setReceiptNo(usptBsnsCnvnResult.getReceiptNo());
			frontCnvnChangeRequestDto.setTaskNmKo(usptBsnsCnvnResult.getTaskNmKo());
			frontCnvnChangeRequestDto.setCnvnDe(usptBsnsCnvnResult.getCnvnDe());
			frontCnvnChangeRequestDto.setCnvnBgnde(usptBsnsCnvnResult.getCnvnBgnde());
			frontCnvnChangeRequestDto.setCnvnEndde(usptBsnsCnvnResult.getCnvnEndde());
			frontCnvnChangeRequestDto.setCnvnSttusCd(usptBsnsCnvnResult.getCnvnSttusCd());
			frontCnvnChangeRequestDto.setCnvnSttusNm(usptBsnsCnvnResult.getCnvnSttusNm());
			frontCnvnChangeRequestDto.setBsnsCnvnId(usptBsnsCnvnResult.getBsnsCnvnId());
			frontCnvnChangeRequestDto.setApplyId(usptBsnsCnvnResult.getApplyId());
			frontCnvnChangeRequestDto.setBsnsPlanDocId(usptBsnsCnvnResult.getBsnsPlanDocId());
			//협약기간
			Date cnvnBgndt = string.toDate(usptBsnsCnvnResult.getCnvnBgnde());
			Date cnvnEnddt = string.toDate(usptBsnsCnvnResult.getCnvnEndde());
			frontCnvnChangeRequestDto.setCnvnDePdAll(date.format(cnvnBgndt, "yyyy-MM-dd") + " ~ " + date.format(cnvnEnddt, "yyyy-MM-dd"));

			String getCnvnSttusCd ="";
			/*
			 * 협약변경항목구분 조회
			 * 신청내역 존재 여부 조회	-신청건 존재 시 변경요청 불가
			 */
			 List<CmmtCode> cmmtCodeList = cmmtCodeDao.selectList("CHANGE_IEM_DIV");

			 for(CmmtCode cmmtCodeInfo : cmmtCodeList) {
				if(CoreUtils.string.equals( Code.changeIemDiv.수행기관신분, cmmtCodeInfo.getCode())){
					cmmtCodeInfo.setCodeGroup("승인정보");
					getCnvnSttusCd = usptCnvnChangeReqDao.selectCnvnChangeReqCCHS01(strMemberId, usptBsnsCnvnResult.getBsnsCnvnId(), Code.changeIemDiv.수행기관신분);
					//수행기관신분변경 시 신청, 승인건 불가
					if(string.equals(getCnvnSttusCd, "CCHS01")) {		//신청
						cmmtCodeInfo.setEnabled(false);
					}else if(string.equals(getCnvnSttusCd, "CCHS05")) {		//승인
						cmmtCodeInfo.setEnabled(false);
						cmmtCodeInfo.setRemark("CCHS05");
					}else {
						cmmtCodeInfo.setEnabled(true);
					}
				}else if(CoreUtils.string.equals( Code.changeIemDiv.과제정보, cmmtCodeInfo.getCode())){
					cmmtCodeInfo.setCodeGroup("승인정보");
					getCnvnSttusCd = usptCnvnChangeReqDao.selectCnvnChangeReqCCHS01(strMemberId, usptBsnsCnvnResult.getBsnsCnvnId(), Code.changeIemDiv.과제정보);
					if(string.equals(getCnvnSttusCd, "CCHS01")) {		//신청
						cmmtCodeInfo.setEnabled(false);
					}else {
						cmmtCodeInfo.setEnabled(true);
					}
				}else if(CoreUtils.string.equals( Code.changeIemDiv.참여기업, cmmtCodeInfo.getCode())){
					cmmtCodeInfo.setCodeGroup("승인정보");
					getCnvnSttusCd = usptCnvnChangeReqDao.selectCnvnChangeReqCCHS01(strMemberId, usptBsnsCnvnResult.getBsnsCnvnId(), Code.changeIemDiv.참여기업);
					if(string.equals(getCnvnSttusCd, "CCHS01")) {		//신청
						cmmtCodeInfo.setEnabled(false);
					}else {
						cmmtCodeInfo.setEnabled(true);
					}
				}else if(CoreUtils.string.equals( Code.changeIemDiv.참여인력, cmmtCodeInfo.getCode())){
					cmmtCodeInfo.setCodeGroup("승인정보");
					getCnvnSttusCd = usptCnvnChangeReqDao.selectCnvnChangeReqCCHS01(strMemberId, usptBsnsCnvnResult.getBsnsCnvnId(), Code.changeIemDiv.참여인력);
					if(string.equals(getCnvnSttusCd, "CCHS01")) {		//신청
						cmmtCodeInfo.setEnabled(false);
					}else {
						cmmtCodeInfo.setEnabled(true);
					}
				}else if(CoreUtils.string.equals( Code.changeIemDiv.사업비, cmmtCodeInfo.getCode())){
					cmmtCodeInfo.setCodeGroup("승인정보");
					getCnvnSttusCd = usptCnvnChangeReqDao.selectCnvnChangeReqCCHS01(strMemberId, usptBsnsCnvnResult.getBsnsCnvnId(), Code.changeIemDiv.사업비);
					if(string.equals(getCnvnSttusCd, "CCHS01")) {		//신청
						cmmtCodeInfo.setEnabled(false);
					}else {
						cmmtCodeInfo.setEnabled(true);
					}
				}else if(CoreUtils.string.equals( Code.changeIemDiv.비목별사업비, cmmtCodeInfo.getCode())){
					cmmtCodeInfo.setCodeGroup("승인정보");
					getCnvnSttusCd = usptCnvnChangeReqDao.selectCnvnChangeReqCCHS01(strMemberId, usptBsnsCnvnResult.getBsnsCnvnId(), Code.changeIemDiv.비목별사업비);
					if(string.equals(getCnvnSttusCd, "CCHS01")) {		//신청
						cmmtCodeInfo.setEnabled(false);
					}
				}else if(CoreUtils.string.equals( Code.changeIemDiv.신청자정보, cmmtCodeInfo.getCode())){
					cmmtCodeInfo.setCodeGroup("통보정보");
					getCnvnSttusCd = usptCnvnChangeReqDao.selectCnvnChangeReqCCHS01(strMemberId, usptBsnsCnvnResult.getBsnsCnvnId(), Code.changeIemDiv.신청자정보);
					if(string.equals(getCnvnSttusCd, "CCHS01")) {		//신청
						cmmtCodeInfo.setEnabled(false);
					}else {
						cmmtCodeInfo.setEnabled(true);
					}
				}else if(CoreUtils.string.equals( Code.changeIemDiv.과제책임자, cmmtCodeInfo.getCode())){
					cmmtCodeInfo.setCodeGroup("통보정보");
					getCnvnSttusCd = usptCnvnChangeReqDao.selectCnvnChangeReqCCHS01(strMemberId, usptBsnsCnvnResult.getBsnsCnvnId(), Code.changeIemDiv.과제책임자);
					if(string.equals(getCnvnSttusCd, "CCHS01")) {		//신청
						cmmtCodeInfo.setEnabled(false);
					}else {
						cmmtCodeInfo.setEnabled(true);
					}
				}
			 }
			 frontCnvnChangeRequestDto.setCmmtCode(cmmtCodeList);
		}else {
			frontCnvnChangeRequestDto =null;
		}
		return frontCnvnChangeRequestDto;
	}

	/**
	 * 협약변경관리 신청내역조회
	 * @param memberId
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	public CorePagination<UsptCnvnChangeReq> selectCnvnChangeReqList(FrontCnvnChangeParam frontCnvnChangeParam, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();

		if(page == null) {
			page = 1L;
		}
		if(frontCnvnChangeParam.getItemsPerPage() == null) {
			frontCnvnChangeParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = frontCnvnChangeParam.getItemsPerPage();

		//변경요청한 ID
		frontCnvnChangeParam.setMemberId(strMemberId);
		//전체건수 조회
		long totalItems = usptCnvnChangeReqDao.selectCnvnChangeReqListCnt(frontCnvnChangeParam);

		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		frontCnvnChangeParam.setBeginRowNum(info.getBeginRowNum());
		frontCnvnChangeParam.setItemsPerPage(itemsPerPage);
		List<UsptCnvnChangeReq> list = usptCnvnChangeReqDao.selectCnvnChangeReqList(frontCnvnChangeParam);

		CorePagination<UsptCnvnChangeReq> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/****************************************수행기관신분변경 start****************************************************/
	/**
	 * 협약변경관리 수행기관신분변경 상세조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptCnvnSclpstHistDto selectChangeCnvnSclpstInfoFront(FrontCnvnChangeParam frontCnvnChangeParam){
		SecurityUtils.checkWorkerIsMember();

		String cnvnChangeReqId = "";											/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.수행기관신분;		/**협약변경항목구분 - CHIE01*/

		UsptCnvnSclpstHistDto usptCnvnSclpstHistDto = new UsptCnvnSclpstHistDto();

		//기본정보 조회
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		frontCnvnChangeParam.setChangeIemDivCd(changeIemDivCd);
		usptCnvnChangeReq = selectCnvnChangeBaseInfoFront(frontCnvnChangeParam);

		//사업협약변경이력
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 cnvnChangeReqId = usptCnvnChangeReq.getCnvnChangeReqId();
		 usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

		 if(usptBsnsCnvnHist !=null) {
			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptBsnsCnvnHist.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnHist.getAttachmentGroupId());
				usptCnvnSclpstHistDto.setAttachFileList(list);
				 //신청시 등록에 필요한 첨부파일그룹ID 셋팅
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
		//사업계획신청자 조회
		String strMemberId = usptCnvnChangeReq.getMemberId();
		CmmtMember memberInfo = cmmtMemberDao.select(strMemberId);

		UsptCnvnSclpstHist usptCnvnSclpstHistAfter = new UsptCnvnSclpstHist();

		usptCnvnSclpstHistAfter.setIndvdlBsnmDivCd(Code.indvdlBsnmDiv.사업자);		//회원구분
		usptCnvnSclpstHistAfter.setBsnmNm(memberInfo.getMemberNm());				 /*사업자명*/
		usptCnvnSclpstHistAfter.setBizrno(memberInfo.getBizrno());						/*사업자번호*/
		usptCnvnSclpstHistAfter.setCeoNm(memberInfo.getCeoNm());	 				/*대표자명*/
		usptCnvnSclpstHistAfter.setChargerNm(memberInfo.getChargerNm());			/*담당자명*/

		usptCnvnSclpstHistDto.setUsptCnvnSclpstHistAfter(usptCnvnSclpstHistAfter);

		return usptCnvnSclpstHistDto;
	}
	/**
	 * 협약변경관리 수행기관신분변경 신청
	 * cnvnChangeReqId , resnCn(사유)
	 */
	public void modifyChangeCnvnSclpstFront(UsptCnvnSclpstHistDto usptCnvnSclpstHistDto,	List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		UsptCnvnChangeReq usptCnvnChangeReq = usptCnvnSclpstHistDto.getUsptCnvnChangeReq();
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		String changeIemDivCd = Code.changeIemDiv.수행기관신분;		/**협약변경항목구분 - CHIE01*/
		usptCnvnChangeReq.setChangeIemDivCd(changeIemDivCd);

		/** 협약변경요청ID */
		//협약변경요청  등록 후 리턴
		String cnvnChangeReqId =modifyCnvnChangeReq(usptCnvnChangeReq, usptCnvnSclpstHistDto.getAttachFileDeleteList(), fileList);

		/**협약수행기관신분변경이력*/
		UsptCnvnSclpstHist usptCnvnSclpstHist = usptCnvnSclpstHistDto.getUsptCnvnSclpstHistAfter();
		String cnvnSclpstHistId = CoreUtils.string.getNewId(Code.prefix.협약수행기관신분변경이력);
		usptCnvnSclpstHist.setCnvnSclpstHistId(cnvnSclpstHistId);
		usptCnvnSclpstHist.setCnvnChangeReqId(cnvnChangeReqId);
		usptCnvnSclpstHist.setIndvdlBsnmDivCd(Code.indvdlBsnmDiv.사업자);	//회원구분

		usptCnvnSclpstHist.setCreatedDt(now);
		usptCnvnSclpstHist.setCreatorId(strMemberId);
		usptCnvnSclpstHistDao.insert(usptCnvnSclpstHist);
	}
	/****************************************수행기관신분변경****************************************************/

	/****************************************과제정보****************************************************/
	/**
	 * 협약변경관리 과제정보 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptCnvnTaskInfoHistDto selectChangeCnvnTaskInfoFront(FrontCnvnChangeParam frontCnvnChangeParam){
		SecurityUtils.checkWorkerIsMember();

		String cnvnChangeReqId = "";										/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.과제정보;		/**협약변경항목구분 - CHIE02*/

		 UsptCnvnTaskInfoHistDto usptCnvnTaskInfoHistDto = new UsptCnvnTaskInfoHistDto();

		//기본정보 조회
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		frontCnvnChangeParam.setChangeIemDivCd(changeIemDivCd);
		usptCnvnChangeReq = selectCnvnChangeBaseInfoFront(frontCnvnChangeParam);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 cnvnChangeReqId = usptCnvnChangeReq.getCnvnChangeReqId();
		 usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

		 if(usptBsnsCnvnHist !=null) {
			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptBsnsCnvnHist.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnHist.getAttachmentGroupId());
				usptCnvnTaskInfoHistDto.setAttachFileList(list);
				 //신청시 등록에 필요한 첨부파일그룹ID 셋팅
				 usptCnvnChangeReq.setAttachmentGroupId(usptBsnsCnvnHist.getAttachmentGroupId());
			}
		 }
		//셋팅
		 usptCnvnTaskInfoHistDto.setUsptCnvnChangeReq(usptCnvnChangeReq);

		/**과제정보 변경 전 내용*/
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
		if(CoreUtils.string.isBlank(cnvnChangeReqId)) {
			//최신정보 없으면 변경전 정보 셋팅
			usptCnvnTaskInfoHistDto.setUsptCnvnTaskInfoHistAfter(usptCnvnTaskInfoHistBefore);
		}else {
			//조회(이력에서 가장 최신 정보)
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
		}
		return usptCnvnTaskInfoHistDto;
	}
	/**
	 * 협약변경관리 과제정보 신청
	 * changeIemDivCd, cnvnChangeReqId
	 */
	public void modifyChangeCnvnTaskInfoFront(UsptCnvnTaskInfoHistDto usptCnvnTaskInfoHistDto,	List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		UsptCnvnChangeReq usptCnvnChangeReq = usptCnvnTaskInfoHistDto.getUsptCnvnChangeReq();
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		String changeIemDivCd = Code.changeIemDiv.과제정보;		/**협약변경항목구분 - CHIE02*/
		usptCnvnChangeReq.setChangeIemDivCd(changeIemDivCd);

		/** 협약변경요청ID */
		//협약변경요청  등록 후 리턴
		String cnvnChangeReqId =modifyCnvnChangeReq(usptCnvnChangeReq, usptCnvnTaskInfoHistDto.getAttachFileDeleteList(), fileList);

		/**협약수행기관신분변경이력*/
		UsptCnvnTaskInfoHist usptCnvnTaskInfoHistAfter = usptCnvnTaskInfoHistDto.getUsptCnvnTaskInfoHistAfter();
		String cnvnTaskInfoHistId = CoreUtils.string.getNewId(Code.prefix.협약수행기관신분변경이력);
		usptCnvnTaskInfoHistAfter.setCnvnTaskInfoHistId(cnvnTaskInfoHistId);
		usptCnvnTaskInfoHistAfter.setCnvnChangeReqId(cnvnChangeReqId);
		usptCnvnTaskInfoHistAfter.setCreatedDt(now);
		usptCnvnTaskInfoHistAfter.setCreatorId(strMemberId);
		usptCnvnTaskInfoHistDao.insert(usptCnvnTaskInfoHistAfter);
	}
	/****************************************과제정보****************************************************/

	/****************************************참여기업****************************************************/
	/**
	 * 협약변경관리 참여기업 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptTaskPrtcmpnyInfoHistDto selectTaskPrtcmpnyInfoHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		SecurityUtils.checkWorkerIsMember();

		String cnvnChangeReqId = "";										/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.참여기업;		/**협약변경항목구분 - CHIE03*/

		UsptTaskPrtcmpnyInfoHistDto usptTaskPrtcmpnyInfoHistDto = new UsptTaskPrtcmpnyInfoHistDto();

		/**기본정보 조회(changeIemDivCd, cnvnChangeReqId, bsnsCnvnId)*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		frontCnvnChangeParam.setChangeIemDivCd(changeIemDivCd);
		usptCnvnChangeReq = selectCnvnChangeBaseInfoFront(frontCnvnChangeParam);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 cnvnChangeReqId = usptCnvnChangeReq.getCnvnChangeReqId();
		 usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

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

		/**참여기업 개요 셋팅-과제참여기업정보변경*/
		Long partcptnCompanyCnt = usptCnvnChangeReq.getPartcptnCompanyCnt();	/* 참여기업수 */
		Long smlpzCnt = usptCnvnChangeReq.getSmlpzCnt();								/* 중소기업수 */
		Long mspzCnt= usptCnvnChangeReq.getMspzCnt();									/* 중견기업수 */
		Long etcCnt = usptCnvnChangeReq.getEtcCnt();										/* 기타 */

		UsptTaskPrtcmpnyInfoHist usptTaskPrtcmpnyInfoHistBefore = new UsptTaskPrtcmpnyInfoHist();
		usptTaskPrtcmpnyInfoHistBefore.setPartcptnCompanyCnt(partcptnCompanyCnt);
		usptTaskPrtcmpnyInfoHistBefore.setSmlpzCnt(smlpzCnt);
		usptTaskPrtcmpnyInfoHistBefore.setMspzCnt(mspzCnt);
		usptTaskPrtcmpnyInfoHistBefore.setEtcCnt(etcCnt);
		//전 내용 셋팅
		usptTaskPrtcmpnyInfoHistDto.setUsptTaskPrtcmpnyInfoHistBefore(usptTaskPrtcmpnyInfoHistBefore);

		/**과제참여기업변경 정보 조회(bsnsPlanDocId)*/
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

		if(CoreUtils.string.isBlank(cnvnChangeReqId)) {
			/** 과제참여기업정보변경이력 변경 후*/
			usptTaskPrtcmpnyInfoHistDto.setUsptTaskPrtcmpnyInfoHistAfter(usptTaskPrtcmpnyInfoHistBefore);
			/** 과제참여기업변경이력 변경 후*/
			usptTaskPrtcmpnyInfoHistDto.setUsptTaskPrtcmpnyHistAfterList(usptTaskPrtcmpnyHistBeforeList);
		}else {
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
			usptTaskPrtcmpnyInfoHistDto.setUsptTaskPrtcmpnyHistAfterList(usptTaskPrtcmpnyHistAfterList);
		}
		return usptTaskPrtcmpnyInfoHistDto;
	}
	/**
	 * 협약변경관리 참여기업 신청
	 * changeIemDivCd, cnvnChangeReqId
	 */
	public void modifyTaskPrtcmpnyInfoHistFront(UsptTaskPrtcmpnyInfoHistDto usptTaskPrtcmpnyInfoHistDto,	List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		UsptCnvnChangeReq usptCnvnChangeReq = usptTaskPrtcmpnyInfoHistDto.getUsptCnvnChangeReq();
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		String changeIemDivCd = Code.changeIemDiv.참여기업;		/**협약변경항목구분 - CHIE03*/
		usptCnvnChangeReq.setChangeIemDivCd(changeIemDivCd);

		/** 협약변경요청 */
		//협약변경요청  등록 후 리턴(협약변경요청ID)
		String cnvnChangeReqId =modifyCnvnChangeReq(usptCnvnChangeReq, usptTaskPrtcmpnyInfoHistDto.getAttachFileDeleteList(), fileList);

		/**과제참여기업정보변경이력*/
		UsptTaskPrtcmpnyInfoHist usptTaskPrtcmpnyInfoHistAfter = usptTaskPrtcmpnyInfoHistDto.getUsptTaskPrtcmpnyInfoHistAfter();

		String taskPrtcmpnyInfoHistId= CoreUtils.string.getNewId(Code.prefix.과제참여기업정보변경이력);
		usptTaskPrtcmpnyInfoHistAfter.setTaskPrtcmpnyInfoHistId(taskPrtcmpnyInfoHistId);
		usptTaskPrtcmpnyInfoHistAfter.setCnvnChangeReqId(cnvnChangeReqId);
		usptTaskPrtcmpnyInfoHistAfter.setCreatedDt(now);
		usptTaskPrtcmpnyInfoHistAfter.setCreatorId(strMemberId);;
		usptTaskPrtcmpnyInfoHistDao.insert(usptTaskPrtcmpnyInfoHistAfter);

		/**과제참여기업변경이력*/
		List<UsptTaskPrtcmpnyHist>  usptTaskPrtcmpnyHistAfterList = usptTaskPrtcmpnyInfoHistDto.getUsptTaskPrtcmpnyHistAfterList();

		String taskPartcptnEntrprsHistId="";
		for(UsptTaskPrtcmpnyHist input : usptTaskPrtcmpnyHistAfterList ) {
			taskPartcptnEntrprsHistId = CoreUtils.string.getNewId(Code.prefix.과제참여기업변경이력);
			input.setTaskPartcptnEntrprsHistId(taskPartcptnEntrprsHistId);
			input.setTaskPrtcmpnyInfoHistId(taskPrtcmpnyInfoHistId);
			input.setEncTelno(aes256.encrypt(input.getEncTelno(), taskPartcptnEntrprsHistId));
			input.setEncMbtlnum(aes256.encrypt(input.getEncMbtlnum(), taskPartcptnEntrprsHistId));
			input.setEncEmail(aes256.encrypt(input.getEncEmail(), taskPartcptnEntrprsHistId));;
			input.setCreatedDt(now);
			input.setCreatorId(strMemberId);
			usptTaskPrtcmpnyHistDao.insert(input);
		}
	}
	/****************************************참여기업 end****************************************************/

	/****************************************과제참여인력변경이력 start****************************************************/
	/**
	 * 협약변경관리 참여인력 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptTaskPartcptsHistDto selectTaskPartcptsHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		SecurityUtils.checkWorkerIsMember();

		String cnvnChangeReqId = "";											/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.참여인력;		/**협약변경항목구분 - CHIE04*/

		UsptTaskPartcptsHistDto usptTaskPartcptsHistDto = new UsptTaskPartcptsHistDto();

		/**기본정보 조회(changeIemDivCd, cnvnChangeReqId, bsnsCnvnId)*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		frontCnvnChangeParam.setChangeIemDivCd(changeIemDivCd);
		usptCnvnChangeReq = selectCnvnChangeBaseInfoFront(frontCnvnChangeParam);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 cnvnChangeReqId = usptCnvnChangeReq.getCnvnChangeReqId();
		 usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

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

		/**참여인력 정보 조회(bsnsPlanDocId)*/
		 UsptTaskPartcpts usptTaskPartcpts = new UsptTaskPartcpts();
		 usptTaskPartcpts.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		List<UsptTaskPartcpts> usptTaskPartcptsList= usptTaskPartcptsDao.selectList(usptTaskPartcpts);

		List<UsptTaskPartcptsHist> usptTaskPartcptsHistBeforeList = new ArrayList<>();
		UsptTaskPartcptsHist usptTaskPartcptsHist = null;

		for(UsptTaskPartcpts usptTaskPartcptsInfo : usptTaskPartcptsList) {
			usptTaskPartcptsHist = new UsptTaskPartcptsHist();
			usptTaskPartcptsHist.setMemberId(usptTaskPartcptsInfo.getMemberId());				/**업체회원ID*/
			usptTaskPartcptsHist.setMemberNm(usptTaskPartcptsInfo.getMemberNm());			/**업체명*/
			usptTaskPartcptsHist.setPartcptsNm(usptTaskPartcptsInfo.getPartcptsNm());				/** 참여자명 */
			usptTaskPartcptsHist.setChrgRealmNm(usptTaskPartcptsInfo.getChrgRealmNm());		/** 담당분야명 */
			usptTaskPartcptsHist.setEncMbtlnum(usptTaskPartcptsInfo.getMbtlnum());				/** 복호화된 휴대폰번호 */
			usptTaskPartcptsHist.setEncBrthdy(usptTaskPartcptsInfo.getBrthdy());						/** 복호화된 생년월일 */
			usptTaskPartcptsHist.setPartcptnRate(usptTaskPartcptsInfo.getPartcptnRate());			/** 참여율 */
			usptTaskPartcptsHistBeforeList.add(usptTaskPartcptsHist);
		}
		//전 내용 셋팅
		usptTaskPartcptsHistDto.setUsptTaskPartcptsHistBeforeList(usptTaskPartcptsHistBeforeList);

		/********************************************** 참여인력 변경 후 정보*/
		if(string.isBlank(cnvnChangeReqId)) {
			//후 내용 셋팅
			usptTaskPartcptsHistDto.setUsptTaskPartcptsHistAfterList(usptTaskPartcptsHistBeforeList);
		}else {
			List<UsptTaskPartcptsHist>  usptTaskPartcptsHistAfterList = usptTaskPartcptsHistDao.selectMaxHistList(cnvnChangeReqId);

			for(UsptTaskPartcptsHist input : usptTaskPartcptsHistAfterList) {
				input.setEncMbtlnum(input.getMbtlnum());
				input.setEncBrthdy(input.getBrthdy());
			}
			//후 내용 셋팅
			usptTaskPartcptsHistDto.setUsptTaskPartcptsHistAfterList(usptTaskPartcptsHistAfterList);
		}

		return usptTaskPartcptsHistDto;
	}
	/**
	 * 협약변경관리 참여인력 신청
	 * changeIemDivCd, cnvnChangeReqId
	 */
	public void modifyTaskPartcptsHistFront(UsptTaskPartcptsHistDto usptTaskPartcptsHistDto,	List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		UsptCnvnChangeReq usptCnvnChangeReq = usptTaskPartcptsHistDto.getUsptCnvnChangeReq();
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		String changeIemDivCd = Code.changeIemDiv.참여인력;		/**협약변경항목구분 - CHIE04*/
		usptCnvnChangeReq.setChangeIemDivCd(changeIemDivCd);

		/** 협약변경요청 */
		//협약변경요청  등록 후 리턴(협약변경요청ID)
		String cnvnChangeReqId =modifyCnvnChangeReq(usptCnvnChangeReq, usptTaskPartcptsHistDto.getAttachFileDeleteList(), fileList);

		/**과제참여자변경이력*/
		List<UsptTaskPartcptsHist>  usptTaskPartcptsHistList = usptTaskPartcptsHistDto.getUsptTaskPartcptsHistAfterList();

		String taskPartcptsHistId="";
		for(UsptTaskPartcptsHist input : usptTaskPartcptsHistList ) {
			taskPartcptsHistId = CoreUtils.string.getNewId(Code.prefix.과제참여자변경이력);
			input.setTaskPartcptsHistId(taskPartcptsHistId);
			input.setCnvnChangeReqId(cnvnChangeReqId);
			input.setEncMbtlnum(aes256.encrypt(input.getEncMbtlnum(), taskPartcptsHistId));
			input.setEncBrthdy(aes256.encrypt(input.getEncBrthdy(), taskPartcptsHistId));
			input.setCreatedDt(now);
			input.setCreatorId(strMemberId);
			usptTaskPartcptsHistDao.insert(input);
		}
	}
	/****************************************과제참여인력변경이력****************************************************/

	/****************************************과제신청사업비변경이력 start****************************************************/
	/**
	 * 협약변경관리 과제신청사업비변경이력 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptTaskReqstWctHistDto selectTaskReqstWctHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		SecurityUtils.checkWorkerIsMember();

		String cnvnChangeReqId = "";									/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.사업비;		/**협약변경항목구분 - CHIE05*/

		UsptTaskReqstWctHistDto usptTaskReqstWctHistDto = new UsptTaskReqstWctHistDto();

		/**기본정보 조회(changeIemDivCd, cnvnChangeReqId, bsnsCnvnId)*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		frontCnvnChangeParam.setChangeIemDivCd(changeIemDivCd);
		usptCnvnChangeReq = selectCnvnChangeBaseInfoFront(frontCnvnChangeParam);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 cnvnChangeReqId = usptCnvnChangeReq.getCnvnChangeReqId();
		 usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

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

		/**과제신청사업비(신청예산) 조회*/
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
		//총사업비-사업계획서
		usptTaskReqstWctHistDto.setTotalWct(usptCnvnChangeReq.getTotalWct());
		//전 내용 셋팅
		usptTaskReqstWctHistDto.setUsptTaskReqstWctHistBeforeList(usptTaskReqstWctHistBefore);

		/********************************************** 과제신청사업비 변경 후 정보*/
		if(CoreUtils.string.isBlank(cnvnChangeReqId)) {
			//후 내용 셋팅
			usptTaskReqstWctHistDto.setUsptTaskReqstWctHistAfterList(usptTaskReqstWctHistBefore);
		}else {
			/** 과제신청사업비변경이력*/
			List<UsptTaskReqstWctHist> usptTaskReqstWctHistAfterList  = usptTaskReqstWctHistDao.selectMaxHistList(cnvnChangeReqId);
			//후 내용 셋팅
			usptTaskReqstWctHistDto.setUsptTaskReqstWctHistAfterList(usptTaskReqstWctHistAfterList);
		}
		return usptTaskReqstWctHistDto;
	}
	/**
	 * 협약변경관리 과제신청사업비변경이력 신청
	 * changeIemDivCd, cnvnChangeReqId
	 */
	public void modifyTaskReqstWctHistFront(UsptTaskReqstWctHistDto usptTaskReqstWctHistDto,	List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		UsptCnvnChangeReq usptCnvnChangeReq = usptTaskReqstWctHistDto.getUsptCnvnChangeReq();
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		String changeIemDivCd = Code.changeIemDiv.사업비;		/**협약변경항목구분 - CHIE05*/
		usptCnvnChangeReq.setChangeIemDivCd(changeIemDivCd);

		/** 협약변경요청 */
		//협약변경요청  등록 후 리턴(협약변경요청ID)
		String cnvnChangeReqId =modifyCnvnChangeReq(usptCnvnChangeReq, usptTaskReqstWctHistDto.getAttachFileDeleteList(), fileList);

		/**과제신청사업비변경이력*/
		List<UsptTaskReqstWctHist> usptTaskReqstWctHistList  = usptTaskReqstWctHistDto.getUsptTaskReqstWctHistAfterList();

		for(UsptTaskReqstWctHist usptTaskReqstWctHistInfo : usptTaskReqstWctHistList) {
			String taskReqstWctHistId = CoreUtils.string.getNewId(Code.prefix.과제신청사업비변경이력);
			usptTaskReqstWctHistInfo.setTaskReqstWctHistId(taskReqstWctHistId);
			usptTaskReqstWctHistInfo.setCnvnChangeReqId(cnvnChangeReqId);
			usptTaskReqstWctHistInfo.setCreatedDt(now);
			usptTaskReqstWctHistInfo.setCreatorId(strMemberId);
			usptTaskReqstWctHistDao.insert(usptTaskReqstWctHistInfo);
		}
	}
	/****************************************과제신청사업비변경이력****************************************************/


	/****************************************과제세목별사업비변경 start****************************************************/

	/**
	 * 협약변경관리 과제세목별사업비변경 전체 사업년도 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public List<String> selectBsnsPlanTaxitmWctBsnsYearList(FrontCnvnChangeParam frontCnvnChangeParam){
		SecurityUtils.checkWorkerIsMember();

		String bsnsCnvnId		= frontCnvnChangeParam.getBsnsCnvnId();				/** 사업협약ID */
		String cnvnChangeReqId =frontCnvnChangeParam.getCnvnChangeReqId();		/** 협약변경요청ID */
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
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID), bsnsYear(사업년도)
	 * @return
	 */
	public UsptTaskTaxitmWctHistDto selectTaskTaxitmWctHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		SecurityUtils.checkWorkerIsMember();

		String cnvnChangeReqId = "";											/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.비목별사업비;		/**협약변경항목구분 - CHIE06*/

		UsptTaskTaxitmWctHistDto usptTaskTaxitmWctHistDto = new UsptTaskTaxitmWctHistDto();

		/**기본정보 조회(changeIemDivCd, cnvnChangeReqId, bsnsCnvnId)*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		frontCnvnChangeParam.setChangeIemDivCd(changeIemDivCd);
		usptCnvnChangeReq = selectCnvnChangeBaseInfoFront(frontCnvnChangeParam);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 cnvnChangeReqId = usptCnvnChangeReq.getCnvnChangeReqId();
		 usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

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

		/**과제세목별사업비 조회*/
		UsptTaskTaxitmWct usptTaskTaxitmWct = new UsptTaskTaxitmWct();
		usptTaskTaxitmWct.setBsnsPlanDocId(usptCnvnChangeReq.getBsnsPlanDocId());
		usptTaskTaxitmWct.setBsnsYear(frontCnvnChangeParam.getBsnsYear());
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
		if(CoreUtils.string.isBlank(cnvnChangeReqId)) {
			//후 내용 셋팅
			usptTaskTaxitmWctHistDto.setUsptTaskTaxitmWctHistAfterList(usptTaskTaxitmWctHistBeforeList);
		}else {
			List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctHistAfterList = usptTaskTaxitmWctHistDao.selectMaxHistList(cnvnChangeReqId, frontCnvnChangeParam.getBsnsYear());
			//후 내용 셋팅
			usptTaskTaxitmWctHistDto.setUsptTaskTaxitmWctHistAfterList(usptTaskTaxitmWctHistAfterList);
		}
		return usptTaskTaxitmWctHistDto;
	}
	/**
	 * 협약변경관리 과제세목별사업비변경 신청
	 * changeIemDivCd, cnvnChangeReqId
	 */
	public void modifyTaskTaxitmWctHistFront(UsptTaskTaxitmWctHistDto usptTaskTaxitmWctHistDto,	List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		UsptCnvnChangeReq usptCnvnChangeReq = usptTaskTaxitmWctHistDto.getUsptCnvnChangeReq();
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		String changeIemDivCd = Code.changeIemDiv.비목별사업비;		/**협약변경항목구분 - CHIE06*/
		usptCnvnChangeReq.setChangeIemDivCd(changeIemDivCd);

		/** 협약변경요청 */
		//협약변경요청  등록 후 리턴(협약변경요청ID)
		String cnvnChangeReqId =modifyCnvnChangeReq(usptCnvnChangeReq, usptTaskTaxitmWctHistDto.getAttachFileDeleteList(), fileList);

		/**과제세목별사업비변경이력*/
		List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctHistList  = usptTaskTaxitmWctHistDto.getUsptTaskTaxitmWctHistAfterList();

		for(UsptTaskTaxitmWctHist usptTaskTaxitmWctHistInfo : usptTaskTaxitmWctHistList) {
			String taskTaxitmWctHistId = CoreUtils.string.getNewId(Code.prefix.과제세목별사업비변경이력);
			usptTaskTaxitmWctHistInfo.setTaskTaxitmWctHistId(taskTaxitmWctHistId);
			usptTaskTaxitmWctHistInfo.setCnvnChangeReqId(cnvnChangeReqId);
			usptTaskTaxitmWctHistInfo.setCreatedDt(now);
			usptTaskTaxitmWctHistInfo.setCreatorId(strMemberId);
			usptTaskTaxitmWctHistDao.insert(usptTaskTaxitmWctHistInfo);
		}
	}
	/****************************************과제세목별사업비변경****************************************************/

	/****************************************협약신청자정보변경이력 start****************************************************/
	/**
	 * 협약변경관리 협약신청자정보변경이력 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptCnvnApplcntHistDto selectCnvnApplcntHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		SecurityUtils.checkWorkerIsMember();

		String cnvnChangeReqId = "";											/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.신청자정보;		/**협약변경항목구분 - CHIE07*/

		UsptCnvnApplcntHistDto usptCnvnApplcntHistDto = new UsptCnvnApplcntHistDto();

		/**기본정보 조회(changeIemDivCd, cnvnChangeReqId, bsnsCnvnId)*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		frontCnvnChangeParam.setChangeIemDivCd(changeIemDivCd);
		usptCnvnChangeReq = selectCnvnChangeBaseInfoFront(frontCnvnChangeParam);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 cnvnChangeReqId = usptCnvnChangeReq.getCnvnChangeReqId();
		 usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

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
		usptCnvnApplcntHistBefore.setGenderCd(usptBsnsPlanApplcnt.getGenderCd());
		//전 내용 셋팅
		usptCnvnApplcntHistDto.setUsptCnvnApplcntHistBefore(usptCnvnApplcntHistBefore);

		/********************************************** 협약신청자정보변경이력 변경 후 정보*/
		if(CoreUtils.string.isBlank(cnvnChangeReqId)) {
			//후 내용 셋팅
			usptCnvnApplcntHistDto.setUsptCnvnApplcntHistAfter(usptCnvnApplcntHistBefore);
		}else {
			/**협약신청자정보변경이력*/
			UsptCnvnApplcntHist usptCnvnApplcntHistAfter = usptCnvnApplcntHistDao.selectMaxHist(cnvnChangeReqId);
			usptCnvnApplcntHistAfter.setEncMbtlnum(usptCnvnApplcntHistAfter.getMbtlnum());		/** 복호화된 휴대폰번호 */
			usptCnvnApplcntHistAfter.setEncEmail(usptCnvnApplcntHistAfter.getEmail());				/** 복호화된 이메일 */
			usptCnvnApplcntHistAfter.setEncBrthdy(usptCnvnApplcntHistAfter.getBrthdy());			/** 복호화된 생년월일 */
			//후 내용 셋팅
			usptCnvnApplcntHistDto.setUsptCnvnApplcntHistAfter(usptCnvnApplcntHistAfter);
		}
		return usptCnvnApplcntHistDto;
	}
	/**
	 * 협약변경관리 협약신청자정보변경이력 신청
	 * changeIemDivCd, cnvnChangeReqId
	 */
	public void modifyCnvnApplcntHistFront(UsptCnvnApplcntHistDto usptCnvnApplcntHistDto,	List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		UsptCnvnChangeReq usptCnvnChangeReq = usptCnvnApplcntHistDto.getUsptCnvnChangeReq();
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		String changeIemDivCd = Code.changeIemDiv.신청자정보;		/**협약변경항목구분 - CHIE07*/
		usptCnvnChangeReq.setChangeIemDivCd(changeIemDivCd);

		/** 협약변경요청 */
		//협약변경요청  등록 후 리턴(협약변경요청ID)
		String cnvnChangeReqId =modifyCnvnChangeReq(usptCnvnChangeReq, usptCnvnApplcntHistDto.getAttachFileDeleteList(), fileList);

		/**협약신청자정보변경이력*/
		UsptCnvnApplcntHist usptCnvnApplcntHist = usptCnvnApplcntHistDto.getUsptCnvnApplcntHistAfter();

		String cnvnApplcntHistId = CoreUtils.string.getNewId(Code.prefix.협약신청자정보변경이력);
		usptCnvnApplcntHist.setCnvnApplcntHistId(cnvnApplcntHistId);
		usptCnvnApplcntHist.setCnvnChangeReqId(cnvnChangeReqId);
		if(CoreUtils.string.isNotBlank(usptCnvnApplcntHist.getEncBrthdy())) {
			usptCnvnApplcntHist.setEncBrthdy(aes256.encrypt(usptCnvnApplcntHist.getEncBrthdy(), cnvnApplcntHistId));			/** 암호화된 생년월일 */
		}
		if(CoreUtils.string.isNotBlank(usptCnvnApplcntHist.getEncMbtlnum())) {
			usptCnvnApplcntHist.setEncMbtlnum(aes256.encrypt(usptCnvnApplcntHist.getEncMbtlnum(), cnvnApplcntHistId));		/** 암호화된 휴대폰번호 */
		}
		if(CoreUtils.string.isNotBlank(usptCnvnApplcntHist.getEncEmail())) {
			usptCnvnApplcntHist.setEncEmail(aes256.encrypt(usptCnvnApplcntHist.getEncEmail(), cnvnApplcntHistId));				/** 암호화된 이메일 */
		}
		usptCnvnApplcntHist.setCreatedDt(now);
		usptCnvnApplcntHist.setCreatorId(strMemberId);
		usptCnvnApplcntHistDao.insert(usptCnvnApplcntHist);
	}
	/****************************************협약신청자정보변경이력****************************************************/

	/****************************************과제책임자변경이력 start****************************************************/
	/**
	 * 협약변경관리 과제책임자변경이력 조회
	 * @param   bsnsCnvnId(사업협약ID), cnvnChangeReqId(협약변경요청ID)
	 * @return
	 */
	public UsptTaskRspnberHistDto selectTaskRspnberHistFront(FrontCnvnChangeParam frontCnvnChangeParam){
		SecurityUtils.checkWorkerIsMember();

		String cnvnChangeReqId = "";											/**협약변경요청ID*/
		String changeIemDivCd = Code.changeIemDiv.과제책임자;		/**협약변경항목구분 - CHIE08*/

		UsptTaskRspnberHistDto usptTaskRspnberHistDto = new UsptTaskRspnberHistDto();

		/**기본정보 조회(changeIemDivCd, cnvnChangeReqId, bsnsCnvnId)*/
		UsptCnvnChangeReq usptCnvnChangeReq = new UsptCnvnChangeReq();
		frontCnvnChangeParam.setChangeIemDivCd(changeIemDivCd);
		usptCnvnChangeReq = selectCnvnChangeBaseInfoFront(frontCnvnChangeParam);

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 cnvnChangeReqId = usptCnvnChangeReq.getCnvnChangeReqId();
		 usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);

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
		if(CoreUtils.string.isBlank(cnvnChangeReqId)) {
			//후 내용 셋팅
			usptTaskRspnberHistDto.setUsptTaskRspnberHistAfter(usptTaskRspnberHistBefore);
		}else {
			/**과제책임자변경이력*/
			UsptTaskRspnberHist usptTaskRspnberHistAfter = usptTaskRspnberHistDao.selectMaxHist(cnvnChangeReqId);
			usptTaskRspnberHistAfter.setEncBrthdy(usptTaskRspnberHistAfter.getBrthdy());			/** 복호화된 생년월일*/
			usptTaskRspnberHistAfter.setEncEmail(usptTaskRspnberHistAfter.getEmail());				/** 복호화된 이메일*/
			usptTaskRspnberHistAfter.setEncFxnum(usptTaskRspnberHistAfter.getFxnum());			/** 복호화된 팩스번호*/
			usptTaskRspnberHistAfter.setEncMbtlnum(usptTaskRspnberHistAfter.getMbtlnum());		/** 복호화된 휴대폰번호*/
			usptTaskRspnberHistAfter.setEncTelno(usptTaskRspnberHistAfter.getTelno());				/** 복호화된 전화번호*/
			//후 내용 셋팅
			usptTaskRspnberHistDto.setUsptTaskRspnberHistAfter(usptTaskRspnberHistAfter);
		}
		return usptTaskRspnberHistDto;
	}
	/**
	 * 협약변경관리 과제책임자변경이력 신청
	 * changeIemDivCd, cnvnChangeReqId
	 */
	public void modifyTaskRspnberHistFront(UsptTaskRspnberHistDto usptTaskRspnberHistDto,	List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		UsptCnvnChangeReq usptCnvnChangeReq = usptTaskRspnberHistDto.getUsptCnvnChangeReq();
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		String changeIemDivCd = Code.changeIemDiv.과제책임자;		/**협약변경항목구분 - CHIE08*/
		usptCnvnChangeReq.setChangeIemDivCd(changeIemDivCd);

		/** 협약변경요청 */
		//협약변경요청  등록 후 리턴(협약변경요청ID)
		String cnvnChangeReqId =modifyCnvnChangeReq(usptCnvnChangeReq, usptTaskRspnberHistDto.getAttachFileDeleteList(), fileList);

		/**과제책임자변경이력*/
		UsptTaskRspnberHist usptTaskRspnberHist = usptTaskRspnberHistDto.getUsptTaskRspnberHistAfter();

		String taskPartcptsHistId = CoreUtils.string.getNewId(Code.prefix.과제책임자변경이력);
		usptTaskRspnberHist.setTaskRspnberHistId(taskPartcptsHistId);
		usptTaskRspnberHist.setCnvnChangeReqId(cnvnChangeReqId);
		if(CoreUtils.string.isNotBlank(usptTaskRspnberHist.getEncBrthdy())) {
			usptTaskRspnberHist.setEncBrthdy(aes256.encrypt(usptTaskRspnberHist.getEncBrthdy(), taskPartcptsHistId));			/** 암호화된 생년월일 */
		}
		if(CoreUtils.string.isNotBlank(usptTaskRspnberHist.getEncMbtlnum())) {
			usptTaskRspnberHist.setEncMbtlnum(aes256.encrypt(usptTaskRspnberHist.getEncMbtlnum(), taskPartcptsHistId));		/** 암호화된 휴대폰번호 */
		}
		if(CoreUtils.string.isNotBlank(usptTaskRspnberHist.getEncEmail())) {
			usptTaskRspnberHist.setEncEmail(aes256.encrypt(usptTaskRspnberHist.getEncEmail(), taskPartcptsHistId));				/** 암호화된 이메일 */
		}
		if(CoreUtils.string.isNotBlank(usptTaskRspnberHist.getEncTelno())) {
			usptTaskRspnberHist.setEncTelno(aes256.encrypt(usptTaskRspnberHist.getEncTelno(), taskPartcptsHistId));				/** 암호화된 전화번호 */
		}
		if(CoreUtils.string.isNotBlank(usptTaskRspnberHist.getEncFxnum())) {
			usptTaskRspnberHist.setEncFxnum(aes256.encrypt(usptTaskRspnberHist.getEncFxnum(), taskPartcptsHistId));			/** 암호화된 팩스번호 */
		}
		usptTaskRspnberHist.setCreatedDt(now);
		usptTaskRspnberHist.setCreatorId(strMemberId);
		usptTaskRspnberHistDao.insert(usptTaskRspnberHist);
	}
	/****************************************과제책임자변경이력****************************************************/


	/** *********************************************************   공통함수 **************************************************/

	/**
	 * 협약변경관리 기본정보 조회
	 * @param BsnsCnvnId, CnvnChangeReqId,ChangeIemDivCd
	 * @return
	 */
	public UsptCnvnChangeReq selectCnvnChangeBaseInfoFront(FrontCnvnChangeParam frontCnvnChangeParam){
		//셋팅
		String bsnsCnvnId = frontCnvnChangeParam.getBsnsCnvnId();						/*사업협약ID */
		String cnvnChangeReqId  = frontCnvnChangeParam.getCnvnChangeReqId();	/*협약변경요청ID*/
		String changeIemDivCd = frontCnvnChangeParam.getChangeIemDivCd();		/*협약변경항목구분*/
		String approval = Code.cnvnChangeType.승인;
		String notification = Code.cnvnChangeType.통보;

		//필수값 체크
		if(CoreUtils.string.isBlank(bsnsCnvnId)) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업협약ID"));
		}

		/*기본정보 조회*/
		UsptCnvnChangeReq usptCnvnChangeReq = usptCnvnChangeReqDao.selectCnvnChangeBaseInfoFront(changeIemDivCd, bsnsCnvnId, cnvnChangeReqId) ;
		if(usptCnvnChangeReq == null){
			throw new InvalidationException("조회된 협약변경 내역이 없습니다.");
		}

		//등록된 협약변경 내용 없으면 신청상태 및 변경항목 셋팅
		if(CoreUtils.string.isBlank(usptCnvnChangeReq.getCnvnChangeReqId())) {
			usptCnvnChangeReq.setCnvnChangeSttusCd(Code.cnvnChangeSttus.신청);
			usptCnvnChangeReq.setChangeIemDivCd(changeIemDivCd);	//협약변경항목구분코드
			//승인
			if(CoreUtils.string.equals( Code.changeIemDiv.수행기관신분, changeIemDivCd)){
				usptCnvnChangeReq.setChangeIemDivNm("수행기관신분(개인 -> 사업자)");
				usptCnvnChangeReq.setCnvnChangeTypeCd(approval);
			}else if(CoreUtils.string.equals( Code.changeIemDiv.과제정보, changeIemDivCd)){
				usptCnvnChangeReq.setChangeIemDivNm("과제정보");
				usptCnvnChangeReq.setCnvnChangeTypeCd(approval);
			}else if(CoreUtils.string.equals( Code.changeIemDiv.참여기업, changeIemDivCd)){
				usptCnvnChangeReq.setChangeIemDivNm("참여기업");
				usptCnvnChangeReq.setCnvnChangeTypeCd(approval);
			}else if(CoreUtils.string.equals( Code.changeIemDiv.참여인력, changeIemDivCd)){
				usptCnvnChangeReq.setChangeIemDivNm("참여인력");
				usptCnvnChangeReq.setCnvnChangeTypeCd(approval);
			}else if(CoreUtils.string.equals( Code.changeIemDiv.사업비, changeIemDivCd)){
				usptCnvnChangeReq.setChangeIemDivNm("사업비");
				usptCnvnChangeReq.setCnvnChangeTypeCd(approval);
			}else if(CoreUtils.string.equals( Code.changeIemDiv.비목별사업비, changeIemDivCd)){
				usptCnvnChangeReq.setChangeIemDivNm("비목별사업비");
				usptCnvnChangeReq.setCnvnChangeTypeCd(approval);
			//통보
			}else if(CoreUtils.string.equals( Code.changeIemDiv.신청자정보, changeIemDivCd)){
				usptCnvnChangeReq.setChangeIemDivNm("신청자정보");
				usptCnvnChangeReq.setCnvnChangeTypeCd(notification);
			}else if(CoreUtils.string.equals( Code.changeIemDiv.과제책임자, changeIemDivCd)){
				usptCnvnChangeReq.setChangeIemDivNm("과제책임자");
				usptCnvnChangeReq.setCnvnChangeTypeCd(notification);
			}
		}
		return usptCnvnChangeReq;
	}

	/**
	 * 협약변경관리 협약변경요청 신청 정보 등록
	 * @param cnvnChangeReqId , resnCn(사유)
	 * @return
	 */

	public String modifyCnvnChangeReq(UsptCnvnChangeReq usptCnvnChangeReq, List<CmmtAtchmnfl> attachFileDeleteListInput, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		Date now = new Date();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();

		/** 협약변경요청ID */
		String cnvnChangeReqId = usptCnvnChangeReq.getCnvnChangeReqId();
		 /** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
		String  changeIemDivCd = usptCnvnChangeReq.getChangeIemDivCd();
		 /** 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
		String  cnvnChangeSttusCd = Code.cnvnChangeSttus.신청;
		 /** 협약변경유형(CNVN_CHANGE_TYPE) */
		String  cnvnChangeTypeCd ="";
		/**첨부파일 그룹ID*/
		String attachmentGroupId ="";

		if(CoreUtils.string.equals( Code.changeIemDiv.신청자정보, changeIemDivCd)  || CoreUtils.string.equals( Code.changeIemDiv.과제책임자, changeIemDivCd)){
			if(string.isBlank(usptCnvnChangeReq.getCnvnChangeTypeCd())) {
				cnvnChangeTypeCd = Code.cnvnChangeType.통보;		//승인형의 경우 수정 불가함
			}else {
				cnvnChangeTypeCd = usptCnvnChangeReq.getCnvnChangeTypeCd();	//통보형의 경우 승인/통보를 선택
			}
		}else {
			cnvnChangeTypeCd = Code.cnvnChangeType.승인;		//승인형의 경우 수정 불가함
		}
		//동일한 신청건 조회
		Long regYnCnt = usptCnvnChangeReqDao.selectCnvnChangeRegCnt(cnvnChangeReqId, changeIemDivCd, cnvnChangeSttusCd);
		if(regYnCnt >0) {
			throw new InvalidationException(String.format("이미 등록된 변경신청 입니다."));
		}

		//필수값 체크(변경사유)
		if(CoreUtils.string.isBlank(usptCnvnChangeReq.getResnCn())){
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경사유"));
		}

		if(CoreUtils.string.isBlank(cnvnChangeReqId)) {
			/**협약변경요청*/
			cnvnChangeReqId = CoreUtils.string.getNewId(Code.prefix.협약변경요청);
			usptCnvnChangeReq.setCnvnChangeReqId(cnvnChangeReqId);
			usptCnvnChangeReq.setCnvnChangeSttusCd(cnvnChangeSttusCd);
			usptCnvnChangeReq.setCnvnChangeTypeCd(cnvnChangeTypeCd);
			usptCnvnChangeReq.setCreatedDt(now);
			usptCnvnChangeReq.setCreatorId(strMemberId);

			usptCnvnChangeReqDao.insert(usptCnvnChangeReq);
		}else {
			usptCnvnChangeReq.setCnvnChangeSttusCd(cnvnChangeSttusCd);
			usptCnvnChangeReq.setCnvnChangeTypeCd(cnvnChangeTypeCd);
			usptCnvnChangeReqDao.update(usptCnvnChangeReq);
		}

		//사업협약변경이력 최신정보 조회
		 UsptBsnsCnvnHist  usptBsnsCnvnHistFileGrpupId = new UsptBsnsCnvnHist();
		 usptBsnsCnvnHistFileGrpupId = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);
		if(usptBsnsCnvnHistFileGrpupId !=null) {
			 /*첨부파일그룹ID*/
			 attachmentGroupId = usptBsnsCnvnHistFileGrpupId.getAttachmentGroupId();
		}

		 /**사업협약변경이력*/
		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();

		/** 첨부파일*/
		if(fileList != null && fileList.size() > 0) {
			if(CoreUtils.string.isNotBlank(attachmentGroupId)) {
				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), attachmentGroupId, fileList, null, 0);
			} else {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
				attachmentGroupId = fileGroupInfo.getAttachmentGroupId();
			}
		}
		usptBsnsCnvnHist.setBsnsCnvnHistId(CoreUtils.string.getNewId(Code.prefix.협약변경이력));
		usptBsnsCnvnHist.setCnvnChangeReqId(cnvnChangeReqId);
		usptBsnsCnvnHist.setChangeIemDivCd(usptCnvnChangeReq.getChangeIemDivCd());
		usptBsnsCnvnHist.setCnvnChangeSttusCd(cnvnChangeSttusCd);
		usptBsnsCnvnHist.setCnvnChangeTypeCd(cnvnChangeTypeCd);
		usptBsnsCnvnHist.setResnCn("신청 처리되었습니다.");				//사유
		usptBsnsCnvnHist.setAttachmentGroupId(attachmentGroupId);
		usptBsnsCnvnHist.setCreatedDt(now);
		usptBsnsCnvnHist.setCreatorId(strMemberId);
		usptBsnsCnvnHistDao.insert(usptBsnsCnvnHist);

		/* 첨부파일 삭제 */
		List<CmmtAtchmnfl> attachFileDeleteList = attachFileDeleteListInput;
		if( attachFileDeleteList !=null && attachFileDeleteList.size() >0) {
			for(CmmtAtchmnfl attachInfo : attachFileDeleteList) {
				attachmentService.removeFile_keepEmptyGroup( config.getDir().getUpload(), attachInfo.getAttachmentId());
			}
		}

		return cnvnChangeReqId;
	}

	/*
	 * 협약변경관리 신청 취소(공통 사용)
	 * cnvnChangeReqId, changeIemDivCd
	 */
	public void modifyCnvnChangeReqCancel(FrontCnvnChangeParam frontCnvnChangeParam	) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		/* 현재 시간*/
		Date now = new Date();
		/* 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/* 협약변경요청ID */
		String cnvnChangeReqId = frontCnvnChangeParam.getCnvnChangeReqId();
		/*협약변경항목구분코드**/
		String changeIemDivCd = frontCnvnChangeParam.getChangeIemDivCd();
		 /* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
		String  cnvnChangeSttusCd = Code.cnvnChangeSttus.신청취소;
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
		 UsptBsnsCnvnHist  usptBsnsCnvnHistFile = new UsptBsnsCnvnHist();
		 usptBsnsCnvnHistFile = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistMax(cnvnChangeReqId, changeIemDivCd);
		 if(usptBsnsCnvnHistFile != null) {
			 attachmentGroupId  = usptBsnsCnvnHistFile.getAttachmentGroupId();
		 }

		 UsptBsnsCnvnHist  usptBsnsCnvnHist = new UsptBsnsCnvnHist();
		 usptBsnsCnvnHist.setBsnsCnvnHistId(CoreUtils.string.getNewId(Code.prefix.협약변경이력));
		 usptBsnsCnvnHist.setCnvnChangeReqId(cnvnChangeReqId);
		 usptBsnsCnvnHist.setChangeIemDivCd(changeIemDivCd);
		 usptBsnsCnvnHist.setCnvnChangeSttusCd(cnvnChangeSttusCd);
		 usptBsnsCnvnHist.setCnvnChangeTypeCd(cnvnChangeTypeCd);
		 usptBsnsCnvnHist.setResnCn("취소 처리되었습니다.");			//사유
		 usptBsnsCnvnHist.setAttachmentGroupId(attachmentGroupId);
		 usptBsnsCnvnHist.setCreatedDt(now);
		 usptBsnsCnvnHist.setCreatorId(strMemberId);
		 usptBsnsCnvnHistDao.insert(usptBsnsCnvnHist);
	}


	/****************************************협약변경관리   사유 확인(반려, 보완요청)****************************************************/
	/**
	 * 협약변경관리 사유 확인팝업
	 * @param cnvnChangeReqId, changeIemDivCd, cnvnChangeSttusCd
	 * @return
	 */
	public UsptBsnsCnvnHist getResnInfo(FrontCnvnChangeParam frontCnvnChangeParam){
		 SecurityUtils.checkWorkerIsMember();
		/* 협약변경요청ID */
		String cnvnChangeReqId = frontCnvnChangeParam.getCnvnChangeReqId();
		/*협약변경항목구분코드**/
		String changeIemDivCd = frontCnvnChangeParam.getChangeIemDivCd();
		 /* 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
		String  cnvnChangeSttusCd = frontCnvnChangeParam.getCnvnChangeSttusCd();

		UsptBsnsCnvnHist usptBsnsCnvnHist = usptBsnsCnvnHistDao.selectUsptBsnsCnvnHistResnCnMax(cnvnChangeReqId, changeIemDivCd, cnvnChangeSttusCd);
		 return usptBsnsCnvnHist;
	}

	/**
	 * 협약변경관리  변경요청 사업년도 조회
	 * @param
	 * @return
	 */
	public List<String> selectCnvnChangeReqBsnsYear(){
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();

		List <String> usptBsnsCnvnCodeYearDtoList = usptBsnsCnvnDao.selectCnvnChangeReqBsnsYear(strMemberId);

		//필수값 체크
		if(usptBsnsCnvnCodeYearDtoList.size() ==0) {
			throw new InvalidationException(String.format(Code.validateMessage.조회결과없음, "완료된 사업협약"));
		}
		return usptBsnsCnvnCodeYearDtoList;
	}

	/**
	 * 협약변경관리  변경요청 과제명 조회
	 * @param
	 * @return
	 */
	public List<String> selectCnvnChangeReqTaskNm(FrontCnvnChangeParam frontCnvnChangeParam){
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		/** 로그인 회원ID **/
		String strMemberId = worker.getMemberId();
		/** 사업년도**/
		String strBsnsYear = frontCnvnChangeParam.getBsnsYear();

		List <String> usptBsnsCnvnCodeTaskNmDtoList = usptBsnsCnvnDao.selectCnvnChangeReqTaskNm(strBsnsYear, strMemberId);

		return usptBsnsCnvnCodeTaskNmDtoList;
	}

	/**
	 * 사업계획서 참여기업 조회
	 * @param bsnsPlanDocId
	 * @return
	 */
	public List<UsptTaskPrtcmpny> selectCnvnChangeEntrpsNm(FrontCnvnChangeParam frontCnvnChangeParam){

		/**참여기업 조회*/
		List <UsptTaskPrtcmpny> usptTaskPrtcmpnyList = usptTaskPrtcmpnyDao.selectBoxList(frontCnvnChangeParam.getBsnsPlanDocId());
		return usptTaskPrtcmpnyList;
	}

	/**
	 * 파일 단건 다운
	 * @param attachmentId
	 * @return
	 */
	public void getOneFileDown(HttpServletResponse response, String attachmentId) {
		SecurityUtils.checkWorkerIsMember();
		if (string.isNotBlank(attachmentId)) {
			attachmentService.downloadFile(response, config.getDir().getUpload(), attachmentId);
		}
	}

}