package aicluster.pms.api.cnvncncls.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import aicluster.pms.api.cnvncncls.dto.CnvnCnclsDto;
import aicluster.pms.api.cnvncncls.dto.CnvnCnclsParam;
import aicluster.pms.api.cnvncncls.dto.FrontCnvnCnclsDto;
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.UsptBsnsCnvnDao;
import aicluster.pms.common.dao.UsptBsnsCnvnPrtcmpnySignDao;
import aicluster.pms.common.dao.UsptTaskPrtcmpnyDao;
import aicluster.pms.common.dao.UsptTaskReqstWctDao;
import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptBsnsCnvn;
import aicluster.pms.common.entity.UsptBsnsCnvnPrtcmpnySign;
import aicluster.pms.common.entity.UsptTaskPrtcmpny;
import aicluster.pms.common.entity.UsptTaskReqstWct;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CnvnCnclsService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;		/*파일*/
	@Autowired
	private UsptBsnsCnvnDao usptBsnsCnvnDao;		/*사업협약*/
	@Autowired
	private UsptTaskReqstWctDao usptTaskReqstWctDao;		/*과제신청사업비*/
	@Autowired
	private UsptBsnsCnvnPrtcmpnySignDao usptBsnsCnvnPrtcmpnySignDao;		/*사업협약참여기업서명*/
	@Autowired
	private CmmtMemberDao cmmtMemberDao;	/*회원*/
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private UsptTaskPrtcmpnyDao usptTaskPrtcmpnyDao;		/*과제참여기업*/

	/**
	 * 전자협약 관리 목록조회
	 * @param cnvnCnclsParam(bsnsYear, bsnsNm, cnvnSttusCd, cnvnDeStart, cnvnDeEnd, taskNmKo, rspnberNm, memberNm)
	 * @param page
	 * @return
	 */
	public CorePagination<UsptBsnsCnvn> getList(CnvnCnclsParam cnvnCnclsParam,  Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String insiderId = worker.getMemberId();

		if(page == null) {
			page = 1L;
		}
		if(cnvnCnclsParam.getItemsPerPage() == null) {
			cnvnCnclsParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = cnvnCnclsParam.getItemsPerPage();
		//당담자ID
		cnvnCnclsParam.setInsiderId(insiderId);
		//전체건수 조회
		long totalItems = usptBsnsCnvnDao.selectBsnsCnvnListCnt(cnvnCnclsParam);

		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		cnvnCnclsParam.setBeginRowNum(info.getBeginRowNum());
		cnvnCnclsParam.setItemsPerPage(itemsPerPage);
		List<UsptBsnsCnvn> list = usptBsnsCnvnDao.selectBsnsCnvnList(cnvnCnclsParam);

		CorePagination<UsptBsnsCnvn> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 전자협약 관리 목록 엑셀 다운로드
	 * @param cnvnCnclsParam(bsnsYear, bsnsNm, cnvnSttusCd, cnvnDeStart, cnvnDeEnd, taskNmKo, rspnberNm, memberNm)
	 * @return
	 */
	public List<UsptBsnsCnvn> getListExcel(CnvnCnclsParam cnvnCnclsParam){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String insiderId = worker.getMemberId();

		//당담자ID
		cnvnCnclsParam.setInsiderId(insiderId);
		//전체건수 조회
		long totalItems = usptBsnsCnvnDao.selectBsnsCnvnListCnt(cnvnCnclsParam);

		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		cnvnCnclsParam.setBeginRowNum(info.getBeginRowNum());
		cnvnCnclsParam.setItemsPerPage(totalItems);
		return  usptBsnsCnvnDao.selectBsnsCnvnList(cnvnCnclsParam);
	}

	/**
	 * 전자협약 관리 상세 조회
	 * @param cnvnCnclsParam(bsnsCnvnId, applyId)
	 * @return
	 */
	public CnvnCnclsDto getBsnsCnvnInfo(CnvnCnclsParam cnvnCnclsParam){
		SecurityUtils.checkWorkerIsInsider();

		/**조회 리턴**/
		CnvnCnclsDto resultCnvnCnclsDto = new CnvnCnclsDto();

		/**기본정보 and 협약주체 조회*/
		UsptBsnsCnvn usptBsnsCnvnResult =usptBsnsCnvnDao.selectBsnsCnvnDetail(cnvnCnclsParam);

		//기본정보 리턴 셋팅
		resultCnvnCnclsDto.setUsptBsnsCnvn(usptBsnsCnvnResult);

		/** 사업계획서ID */
		String  bsnsPlanDocId = usptBsnsCnvnResult.getBsnsPlanDocId();

		/**협약금액 (과제신청사업비(신청예산)) 조회*/
		UsptTaskReqstWct usptTaskReqstWct = new UsptTaskReqstWct();
		usptTaskReqstWct.setBsnsPlanDocId(bsnsPlanDocId);
		List <UsptTaskReqstWct> usptTaskReqstWctList = usptTaskReqstWctDao.selectList(usptTaskReqstWct);
		//과제신청사업비 리턴 셋팅
		resultCnvnCnclsDto.setUsptTaskReqstWct(usptTaskReqstWctList);

		/**참여기업 조회*/
		String bsnsCnvnId = usptBsnsCnvnResult.getBsnsCnvnId();
		List <UsptBsnsCnvnPrtcmpnySign> usptBsnsCnvnPrtcmpnySign  = new ArrayList<>();
		if(string.isNotBlank(bsnsCnvnId)) {
			usptBsnsCnvnPrtcmpnySign = usptBsnsCnvnPrtcmpnySignDao.selectList(bsnsCnvnId);
			//참여기업 리턴 셋팅
			resultCnvnCnclsDto.setUsptBsnsCnvnPrtcmpnySign(usptBsnsCnvnPrtcmpnySign);
		}else {
			usptBsnsCnvnPrtcmpnySign= usptTaskPrtcmpnyDao.selectBsnsCnvnList(bsnsPlanDocId);
			resultCnvnCnclsDto.setUsptBsnsCnvnPrtcmpnySign(usptBsnsCnvnPrtcmpnySign);
		}

		/** 제출첨부파일 목록 2022.11.15 삭제**/
//		if (string.isNotBlank(usptBsnsCnvnResult.getCnvnFileGroupId())) {
//			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnResult.getCnvnFileGroupId());
//			resultCnvnCnclsDto.setAttachFileList(list);
//		}
		return resultCnvnCnclsDto;
	}

	/**
	 * 전자협약 관리 협약첨부파일 일괄 다운
	 * @param cnvnCnclsParam(cnvnFileGroupId)
	 * @return
	 */
	public void getCnvnFileDown(HttpServletResponse response, CnvnCnclsParam cnvnCnclsParam){
		SecurityUtils.checkWorkerIsInsider();
		if (string.isNotBlank(cnvnCnclsParam.getCnvnFileGroupId())) {
			attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), cnvnCnclsParam.getCnvnFileGroupId(), "전자협약 첨부 파일");
		}
	}

	/**
	 * 전자협약 관리 협약첨부파일 단건 다운
	 * @param cnvnCnclsParam(attachmentId)
	 * @return
	 */
	public void getCnvnFileDownOne(HttpServletResponse response, CnvnCnclsParam cnvnCnclsParam){
		SecurityUtils.checkWorkerIsInsider();
		if (string.isNotBlank(cnvnCnclsParam.getAttachmentId())) {
			attachmentService.downloadFile(response, config.getDir().getUpload(), cnvnCnclsParam.getAttachmentId());
		}
	}

	/**
	 *전자협약 관리 해지정보 조회
	 * @param  cnvnCnclsParam(bsnsCnvnId, applyId)
	 * @return
	 */
	public CnvnCnclsDto getBsnsCnvnTrmnatInfo(CnvnCnclsParam cnvnCnclsParam){
		SecurityUtils.checkWorkerIsInsider();

		/**조회 리턴**/
		CnvnCnclsDto resultCnvnCnclsDto = new CnvnCnclsDto();

		/**기본정보 and 협약주체 조회*/
		cnvnCnclsParam.setCnvnSttusCd(Code.cnvnSttus.협약해지);
		UsptBsnsCnvn usptBsnsCnvnResult =usptBsnsCnvnDao.selectBsnsCnvnDetail(cnvnCnclsParam);

		if(usptBsnsCnvnResult ==null) {
			throw new InvalidationException("해지된 전자협약 내용이 없습니다.");
		}

		//기본정보 리턴 셋팅
		resultCnvnCnclsDto.setUsptBsnsCnvn(usptBsnsCnvnResult);

		/** 사업계획서ID */
		String  bsnsPlanDocId = usptBsnsCnvnResult.getBsnsPlanDocId();

		/**협약금액 (과제신청사업비(신청예산)) 조회*/
		UsptTaskReqstWct usptTaskReqstWct = new UsptTaskReqstWct();
		usptTaskReqstWct.setBsnsPlanDocId(bsnsPlanDocId);
		List <UsptTaskReqstWct> usptTaskReqstWctList = usptTaskReqstWctDao.selectList(usptTaskReqstWct);
		//과제신청사업비 리턴 셋팅
		resultCnvnCnclsDto.setUsptTaskReqstWct(usptTaskReqstWctList);

		/**참여기업 조회*/
		String bsnsCnvnId = usptBsnsCnvnResult.getBsnsCnvnId();
		List <UsptBsnsCnvnPrtcmpnySign> usptBsnsCnvnPrtcmpnySign = usptBsnsCnvnPrtcmpnySignDao.selectList(bsnsCnvnId);
		//참여기업 리턴 셋팅
		resultCnvnCnclsDto.setUsptBsnsCnvnPrtcmpnySign(usptBsnsCnvnPrtcmpnySign);

		/** 제출첨부파일 목록 **/
//		if (string.isNotBlank(usptBsnsCnvnResult.getCnvnFileGroupId())) {
//			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnResult.getCnvnFileGroupId());
//			resultCnvnCnclsDto.setAttachFileList(list);
//		}

		/** 협약해지첨부파일 목록 **/
		if (string.isNotBlank(usptBsnsCnvnResult.getCnvnTrmnatFileGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnResult.getCnvnTrmnatFileGroupId());
			resultCnvnCnclsDto.setTrmnatAttachFileList(list);
		}
		return resultCnvnCnclsDto;
	}

	/**
	 * 전자협약 관리 해지첨부파일 일괄 다운
	 * @param cnvnCnclsParam(cnvnFileGroupId)
	 * @return
	 */
	public void getCnvnTrmnatFileDown(HttpServletResponse response, CnvnCnclsParam cnvnCnclsParam){
		SecurityUtils.checkWorkerIsInsider();
		if (string.isNotBlank(cnvnCnclsParam.getCnvnTrmnatFileGroupId())) {
			attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), cnvnCnclsParam.getCnvnTrmnatFileGroupId(), "전자협약 해지 첨부 파일");
		}
	}

	/**
	 * 전자협약 관리 협약서 생성
	 * @param cnvnCnclsParam
	 * @return
	 */
	public void modifyCnvnCncls( CnvnCnclsParam cnvnCnclsParam) {
		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String strMemberId = worker.getMemberId();

		//협약샹태 체크
		String cnvnSttusCd = usptBsnsCnvnDao.selectCnvnSttusCd(cnvnCnclsParam.getBsnsCnvnId(), cnvnCnclsParam.getApplyId());

		if(!CoreUtils.string.equals( Code.cnvnSttus.대기중, cnvnSttusCd)){
			throw new InvalidationException("협약상태가 <대기중> 경우에만 협약서 생성이 가능합니다.");
		}

		UsptBsnsCnvn usptBsnsCnvn = new UsptBsnsCnvn();

		usptBsnsCnvn.setBsnsCnvnId(cnvnCnclsParam.getBsnsCnvnId());	/*사업협약ID*/
		usptBsnsCnvn.setApplyId(cnvnCnclsParam.getApplyId());			/*신청ID*/
		usptBsnsCnvn.setCnvnSttusCd(Code.cnvnSttus.협약생성);			//CNST02
		usptBsnsCnvn.setCnvnDe(cnvnCnclsParam.getCnvnDe());		    /*협약일자*/
		usptBsnsCnvn.setCnvnBgnde(cnvnCnclsParam.getCnvnBgnde());  	/*협약시작일*/
		usptBsnsCnvn.setCnvnEndde(cnvnCnclsParam.getCnvnEndde());  	/*협약종료일*/
		usptBsnsCnvn.setCnvnBdt(cnvnCnclsParam.getCnvnBdt()); 			/*협약서본문*/
		usptBsnsCnvn.setBsnsMgcNm(cnvnCnclsParam.getBsnsMgcNm());				/*사업단명*/
		usptBsnsCnvn.setBsnsMgcCeoNm(cnvnCnclsParam.getBsnsMgcCeoNm());	/*사업단대표자명*/
		usptBsnsCnvn.setBsnsMgcBizrno(cnvnCnclsParam.getBsnsMgcBizrno());		/*사업단사업자번호*/
		usptBsnsCnvn.setUpdatedDt(now);
		usptBsnsCnvn.setUpdaterId(strMemberId);
		//협약서 생성
		usptBsnsCnvnDao.updateCnvnCnclsCreat(usptBsnsCnvn);

		/**참여기업 서명 대상 등록*/
		/*참여기업 조회*/
		UsptTaskPrtcmpny usptTaskPrtcmpny = new UsptTaskPrtcmpny();
		usptTaskPrtcmpny.setBsnsPlanDocId(cnvnCnclsParam.getBsnsPlanDocId());
		List <UsptTaskPrtcmpny> usptTaskPrtcmpnyList = usptTaskPrtcmpnyDao.selectList(usptTaskPrtcmpny);

		UsptBsnsCnvnPrtcmpnySign usptBsnsCnvnPrtcmpnySign = null;
		for(UsptTaskPrtcmpny param : usptTaskPrtcmpnyList ) {
			usptBsnsCnvnPrtcmpnySign = new UsptBsnsCnvnPrtcmpnySign();
			usptBsnsCnvnPrtcmpnySign.setBsnsCnvnPrtcmpnySignId(CoreUtils.string.getNewId(Code.prefix.사업협약참여기업서명));
			usptBsnsCnvnPrtcmpnySign.setBsnsCnvnId(cnvnCnclsParam.getBsnsCnvnId());
			usptBsnsCnvnPrtcmpnySign.setMemberId(param.getMemberId());
			usptBsnsCnvnPrtcmpnySign.setCreatedDt(now);
			usptBsnsCnvnPrtcmpnySign.setCreatorId(strMemberId);
			usptBsnsCnvnPrtcmpnySign.setUpdatedDt(now);
			usptBsnsCnvnPrtcmpnySign.setUpdaterId(strMemberId);
			/*사업협약참여기업서명 등록*/
			usptBsnsCnvnPrtcmpnySignDao.insert(usptBsnsCnvnPrtcmpnySign);
		}
	}


	/**
	 * 전자협약 상태 변경 및 협약완료
	 * @param cnvnCnclsParam(bsnsCnvnId, 	applyId, cnvnSttusCd, memberId, taskNmKo)
	 * @return
	 */
	public void modifyRequestSign( CnvnCnclsParam cnvnCnclsParam) {

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String strMemberId = worker.getMemberId();					/*관리자ID(사업단)*/

		String bsnsCnvnId 		= cnvnCnclsParam.getBsnsCnvnId();		/* 사업협약ID */
		String applyId 			= cnvnCnclsParam.getApplyId();			/* 신청ID */
		String cnvnSttusCd 	= cnvnCnclsParam.getCnvnSttusCd();	/* 협약상태코드*/
		String memberId 		= cnvnCnclsParam.getMemberId();		/* 회원ID(주관업체)*/
		String taskNmKo		= cnvnCnclsParam.getTaskNmKo();		/* 과제명(국문) */
		String bsnsgCertSessionId = cnvnCnclsParam.getBsnsgCertSessionId();	/*사업단인증서세션ID*/
		/*
		 * 	협약상태코드
		 * 	CNST01	대기중
		 * 	CNST02	협약생성
		 * 	CNST03	서명요청
		 * 	CNST04	서명완료
		 * 	CNST05	협약완료
		 * 	CNST06	협약해지
		 * 	CNST07	서명재요청
		 * 	CNST08	서명요청취소
		 */

		//협약샹태 체크
		String selectCnvnSttusCd = usptBsnsCnvnDao.selectCnvnSttusCd(bsnsCnvnId, applyId);

		if(CoreUtils.string.equals( Code.cnvnSttus.서명요청, cnvnSttusCd)){
			if(!CoreUtils.string.equals( Code.cnvnSttus.협약생성, selectCnvnSttusCd)){
				throw new InvalidationException("협약상태가 <협약생성> 경우에만 서명요청이 가능합니다.");
			}
		}else if(CoreUtils.string.equals( Code.cnvnSttus.서명재요청, cnvnSttusCd)){
			if(!CoreUtils.string.equals( Code.cnvnSttus.서명요청, selectCnvnSttusCd)){
				throw new InvalidationException("협약상태가 <서명요청> 경우에만 서명재요청이 가능합니다.");
			}
		}else if(CoreUtils.string.equals( Code.cnvnSttus.서명요청취소, cnvnSttusCd)){
			if(!CoreUtils.string.equals( Code.cnvnSttus.서명요청, selectCnvnSttusCd)){
				throw new InvalidationException("협약상태가 <서명요청> 경우에만 서명요청취소가 가능합니다.");
			}
		}else if(CoreUtils.string.equals( Code.cnvnSttus.협약완료, cnvnSttusCd)){
			if(!CoreUtils.string.equals( Code.cnvnSttus.서명완료, selectCnvnSttusCd)){
				throw new InvalidationException("협약상태가 <서명완료> 경우에만 협약완료가 가능합니다.");
			}
		}

		/*협약상태코드 변경-공통부분 셋팅*/
		UsptBsnsCnvn usptBsnsCnvn = new UsptBsnsCnvn();
		usptBsnsCnvn.setBsnsCnvnId(bsnsCnvnId);
		usptBsnsCnvn.setApplyId(applyId);
		usptBsnsCnvn.setCnvnSttusCd(cnvnSttusCd);
		usptBsnsCnvn.setUpdatedDt(now);
		usptBsnsCnvn.setUpdaterId(strMemberId);

		if(CoreUtils.string.equals( Code.cnvnSttus.서명요청, cnvnSttusCd)){
			//상태값 변경
			usptBsnsCnvnDao.updateCnvnSttus(usptBsnsCnvn);

			/**************주관업체*/
			//협약서명초기화
			usptBsnsCnvn.setBsnmSignDt(null);		/*사업자서명일시*/
			usptBsnsCnvnDao.updateCnvnCnclsInit(usptBsnsCnvn);
			/*알림 전송*/
			CmmtMember memberInfoMain = cmmtMemberDao.select(memberId);	//회원정보 조회
			CnvnCnclsParam sendSmsEmailMain = new CnvnCnclsParam();
			sendSmsEmailMain.setMemberId(memberId);
			sendSmsEmailMain.setMemberNm(memberInfoMain.getMemberNm());
			sendSmsEmailMain.setEmail(memberInfoMain.getEmail());
			sendSmsEmailMain.setMbtlnum(memberInfoMain.getMobileNo());
			sendSmsEmailMain.setTaskNmKo(taskNmKo);

			sendMsg(sendSmsEmailMain);

			/**************참여기업*/
			List <UsptBsnsCnvnPrtcmpnySign> selectList= usptBsnsCnvnPrtcmpnySignDao.selectList(bsnsCnvnId);
			for( UsptBsnsCnvnPrtcmpnySign param :  selectList) {
				//서명 초기화
				param.setBsnmSignDt(null);
				usptBsnsCnvnPrtcmpnySignDao.updateSign(param);

				/*알림 전송*/
				CmmtMember memberInfo = cmmtMemberDao.select(param.getMemberId());		//회원정보 조회
				CnvnCnclsParam sendSmsEmail = new CnvnCnclsParam();
				sendSmsEmail.setMemberId(memberInfo.getMemberId());
				sendSmsEmail.setMemberNm(memberInfo.getMemberNm());
				sendSmsEmail.setEmail(memberInfo.getEmail());
				sendSmsEmail.setMbtlnum(memberInfo.getMobileNo());
				sendSmsEmail.setTaskNmKo(taskNmKo);

				sendMsg(sendSmsEmail);

			}
		}else if(CoreUtils.string.equals( Code.cnvnSttus.서명재요청, cnvnSttusCd) || CoreUtils.string.equals( Code.cnvnSttus.서명요청취소, cnvnSttusCd)){

			//재요청 및 요청취소 시 입력받은 코드가 아닌 값 셋팅.
			if(CoreUtils.string.equals( Code.cnvnSttus.서명재요청, cnvnSttusCd) ) {
				usptBsnsCnvn.setCnvnSttusCd(Code.cnvnSttus.서명재요청);
			}else if(CoreUtils.string.equals( Code.cnvnSttus.서명요청취소, cnvnSttusCd) ) {
				usptBsnsCnvn.setCnvnSttusCd(Code.cnvnSttus.협약생성);
			}

			/**************주관업체*/
			//협약서명초기화
			usptBsnsCnvn.setBsnmSignDt(null);		/*사업자서명일시*/
			usptBsnsCnvnDao.updateCnvnCnclsInit(usptBsnsCnvn);
			/*알림 전송*/
			CmmtMember memberInfoMain = cmmtMemberDao.select(memberId);	//회원정보 조회
			CnvnCnclsParam sendSmsEmailMain = new CnvnCnclsParam();
			sendSmsEmailMain.setMemberId(memberId);
			sendSmsEmailMain.setMemberNm(memberInfoMain.getMemberNm());
			sendSmsEmailMain.setEmail(memberInfoMain.getEmail());
			sendSmsEmailMain.setMbtlnum(memberInfoMain.getMobileNo());
			sendSmsEmailMain.setTaskNmKo(taskNmKo);

			sendMsg(sendSmsEmailMain);


			/**************참여기업*/
			List <UsptBsnsCnvnPrtcmpnySign> selectList= usptBsnsCnvnPrtcmpnySignDao.selectList(bsnsCnvnId);
			for( UsptBsnsCnvnPrtcmpnySign param :  selectList) {
				//서명 초기화
				param.setBsnmSignDt(null);
				usptBsnsCnvnPrtcmpnySignDao.updateSign(param);

				/*알림 전송*/
				CmmtMember memberInfo = cmmtMemberDao.select(param.getMemberId());		//회원정보 조회
				CnvnCnclsParam sendSmsEmail = new CnvnCnclsParam();
				sendSmsEmail.setMemberId(memberInfo.getMemberId());
				sendSmsEmail.setMemberNm(memberInfo.getMemberNm());
				sendSmsEmail.setEmail(memberInfo.getEmail());
				sendSmsEmail.setMbtlnum(memberInfo.getMobileNo());
				sendSmsEmail.setTaskNmKo(taskNmKo);

				sendMsg(sendSmsEmail);

			}
		}else if(CoreUtils.string.equals( Code.cnvnSttus.협약완료, cnvnSttusCd)){
			usptBsnsCnvn.setBsnsMgcSignDt(now);									/*사업단서명일시*/
			usptBsnsCnvn.setBsnsMgcSignId(strMemberId);						/*사업단서명ID*/
			usptBsnsCnvn.setBsnsgCertSessionId(bsnsgCertSessionId);			/*사업단인증서세션ID*/
			//변경
			usptBsnsCnvnDao.updateCnvnCnclsComplete(usptBsnsCnvn);
		}
	}

	/**
	 * 전자협약 관리 협약서 다운 정보 조회
	 * @param cnvnCnclsParam(bsnsCnvnId, applyId)
	 * @return
	 */
	public FrontCnvnCnclsDto getCnclsDocInfo(CnvnCnclsParam cnvnCnclsParam){
		SecurityUtils.checkWorkerIsInsider();

		/**조회 리턴**/
		FrontCnvnCnclsDto resultFrontCnvnCnclsDto = new FrontCnvnCnclsDto();

		/**기본정보 and 협약주체 조회*/
		UsptBsnsCnvn usptBsnsCnvnResult =usptBsnsCnvnDao.selectBsnsCnvnDocInfo(cnvnCnclsParam);

		//기본정보 리턴 셋팅
		resultFrontCnvnCnclsDto.setUsptBsnsCnvn(usptBsnsCnvnResult);

		/** 사업계획서ID */
		String  bsnsPlanDocId = usptBsnsCnvnResult.getBsnsPlanDocId();

		/**협약금액 (과제신청사업비(신청예산)) 조회*/
		UsptTaskReqstWct usptTaskReqstWct = new UsptTaskReqstWct();
		usptTaskReqstWct.setBsnsPlanDocId(bsnsPlanDocId);
		List <UsptTaskReqstWct> usptTaskReqstWctList = usptTaskReqstWctDao.selectList(usptTaskReqstWct);
		//과제신청사업비 리턴 셋팅
		resultFrontCnvnCnclsDto.setUsptTaskReqstWct(usptTaskReqstWctList);

		/**참여기업 조회*/
		String bsnsCnvnId = usptBsnsCnvnResult.getBsnsCnvnId();
		List <UsptBsnsCnvnPrtcmpnySign> usptBsnsCnvnPrtcmpnySign = usptBsnsCnvnPrtcmpnySignDao.selectList(bsnsCnvnId);
		//참여기업 리턴 셋팅
		resultFrontCnvnCnclsDto.setUsptBsnsCnvnPrtcmpnySign(usptBsnsCnvnPrtcmpnySign);

		return resultFrontCnvnCnclsDto;
	}

	/*******************************************************************************************************************************/
	/**
	 * sms or email 전송
	 * @param CnvnCnclsParam
	 * @return
	 */
	public String sendMsg(CnvnCnclsParam param) {
		SecurityUtils.checkWorkerIsInsider();
		StringBuffer sbMsg = new StringBuffer();

		if(param == null) {
			throw new InvalidationException("발송대상이 설정되지 않았습니다.");
		}

		String title = "전자협약 서명요청";
		String mainTitle = "전자협약 서명요청";
		String memNm = param.getMemberNm();		//회원명
		String cnVal =  param.getTaskNmKo()+" - "+ param.getMemberNm() +"님 서명을 요청 드립니다.";			//발송내용
		String email  = param.getEmail();
		String mbtlnum= param.getMbtlnum();

		EmailResult er = null;
		SmsResult sr = null;

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