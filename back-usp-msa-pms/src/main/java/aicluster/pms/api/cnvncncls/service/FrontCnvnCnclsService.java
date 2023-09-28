package aicluster.pms.api.cnvncncls.service;

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
import aicluster.pms.api.cnvncncls.dto.FrontCnvnCnclsDto;
import aicluster.pms.api.cnvncncls.dto.FrontCnvnCnclsInputParam;
import aicluster.pms.api.cnvncncls.dto.FrontCnvnCnclsParam;
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.UsptBsnsCnvnDao;
import aicluster.pms.common.dao.UsptBsnsCnvnPrtcmpnySignDao;
import aicluster.pms.common.dao.UsptTaskReqstWctDao;
import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptBsnsCnvn;
import aicluster.pms.common.entity.UsptBsnsCnvnPrtcmpnySign;
import aicluster.pms.common.entity.UsptTaskReqstWct;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;


@Service
public class FrontCnvnCnclsService {

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

	/**
	 * 전자협약 관리 목록조회
	 * @param frontCnvnCnclsParam
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	public CorePagination<UsptBsnsCnvn> getList(FrontCnvnCnclsParam frontCnvnCnclsParam,  Long page){
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		String strMemberId = worker.getMemberId();

		if(page == null) {
			page = 1L;
		}
		if(frontCnvnCnclsParam.getItemsPerPage() == null) {
			frontCnvnCnclsParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = frontCnvnCnclsParam.getItemsPerPage();
		//신청자ID
		frontCnvnCnclsParam.setMemberId(strMemberId);
		//전체건수 조회
		long totalItems = usptBsnsCnvnDao.selectFrontBsnsCnvnListCnt(frontCnvnCnclsParam);

		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		frontCnvnCnclsParam.setBeginRowNum(info.getBeginRowNum());
		frontCnvnCnclsParam.setItemsPerPage(itemsPerPage);
		List<UsptBsnsCnvn> list = usptBsnsCnvnDao.selectFrontBsnsCnvnList(frontCnvnCnclsParam);

		CorePagination<UsptBsnsCnvn> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 전자협약 관리 상세 조회
	 * @param frontCnvnCnclsParam
	 * @return
	 */
	public FrontCnvnCnclsDto getBsnsCnvnInfo(FrontCnvnCnclsParam frontCnvnCnclsParam){
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		/**조회 리턴**/
		FrontCnvnCnclsDto resultFrontCnvnCnclsDto = new FrontCnvnCnclsDto();
		//인증서용 사업자번호
		CmmtMember membInfo= cmmtMemberDao.select(worker.getMemberId());
		resultFrontCnvnCnclsDto.setBizrno(membInfo.getBizrno());

		/**기본정보 and 협약주체 조회*/
		UsptBsnsCnvn usptBsnsCnvnResult =usptBsnsCnvnDao.selectBsnsCnvnDetail(frontCnvnCnclsParam);
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

//		/** 제출첨부파일 목록   2022.11.15 삭제**/
//		if (string.isNotBlank(usptBsnsCnvnResult.getCnvnFileGroupId())) {
//			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptBsnsCnvnResult.getCnvnFileGroupId());
//			resultFrontCnvnCnclsDto.setAttachFileList(list);
//		}
		return resultFrontCnvnCnclsDto;
	}

	/**
	 * 전자협약 참여기업 서명
	 * @param frontCnvnCnclsParam
	 * * @param fileList
	 * @return
	 */
	public void modifyPrtcmpnySign( FrontCnvnCnclsInputParam frontCnvnCnclsInputParam) {

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		String strMemberId = worker.getMemberId();

		String bsnsCnvnId 		 = frontCnvnCnclsInputParam.getBsnsCnvnId();			/* 사업협약ID */
		String applyId 			 = frontCnvnCnclsInputParam.getApplyId();				/* 신청ID */
		String mainMemberYn 	 = "N";		/*주관기업 여부*/
		String certSessionId = frontCnvnCnclsInputParam.getCertSessionId()	;			/*인증서세션ID*/

		/*주관기업 서명여부 조회*/
		UsptBsnsCnvn usptBsnsCnvn =usptBsnsCnvnDao.selectBsnsCnvnDetail(frontCnvnCnclsInputParam);
		if( CoreUtils.string.equals(strMemberId, usptBsnsCnvn.getMemberId()) && CoreUtils.string.isNotBlank(usptBsnsCnvn.getCharBsnmSignDt())) {
			throw new InvalidationException("이미 서명한 대상 입니다.");
		}
		//주관기업
		if(CoreUtils.string.equals( strMemberId, usptBsnsCnvn.getMemberId())){
			mainMemberYn ="Y";
		}

		//주관기업
		if("Y".equals(mainMemberYn)) {
			usptBsnsCnvn.setBsnmSignDt(now);
			usptBsnsCnvn.setBsnmCertSessionId(certSessionId);
			usptBsnsCnvn.setUpdatedDt(now);
			usptBsnsCnvn.setUpdaterId(strMemberId);
			usptBsnsCnvnDao.updateBsnmSignDt(usptBsnsCnvn);

		//참여기업
		}else {

			/*참여기업 서명여부 조회*/
			UsptBsnsCnvnPrtcmpnySign usptBsnsCnvnPrtcmpnySign = new UsptBsnsCnvnPrtcmpnySign();

			usptBsnsCnvnPrtcmpnySign = usptBsnsCnvnPrtcmpnySignDao.selectSignRegYn(bsnsCnvnId, strMemberId);
			if(usptBsnsCnvnPrtcmpnySign == null) {
				throw new InvalidationException("서명 대상이 아닙니다.");
			}else if(CoreUtils.string.isNotBlank(usptBsnsCnvnPrtcmpnySign.getCharBsnmSignDt())) {
				throw new InvalidationException("이미 서명한 대상 입니다.");
			}

			/**사업협약참여기업서명 등록*/
			usptBsnsCnvnPrtcmpnySign.setBsnsCnvnPrtcmpnySignId(usptBsnsCnvnPrtcmpnySign.getBsnsCnvnPrtcmpnySignId());
			usptBsnsCnvnPrtcmpnySign.setBsnsCnvnId(bsnsCnvnId);
			usptBsnsCnvnPrtcmpnySign.setMemberId(strMemberId);
			usptBsnsCnvnPrtcmpnySign.setBsnmSignDt(now);
			usptBsnsCnvnPrtcmpnySign.setCertSessionId(certSessionId);
			usptBsnsCnvnPrtcmpnySign.setUpdatedDt(now);
			usptBsnsCnvnPrtcmpnySign.setUpdaterId(strMemberId);
			//서명등록
			usptBsnsCnvnPrtcmpnySignDao.updateSign(usptBsnsCnvnPrtcmpnySign);
		}

		/** 협약 첨부파일2022.11.15 삭제*/
//		if(fileList != null && fileList.size() > 0) {
//			if(CoreUtils.string.isNotBlank(usptBsnsCnvn.getCnvnFileGroupId())) {
//				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), usptBsnsCnvn.getCnvnFileGroupId(), fileList, null, 0);
//			} else {
//				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
//				 /** 협약파일그룹ID */
//				usptBsnsCnvn.setCnvnFileGroupId(fileGroupInfo.getAttachmentGroupId());
//			}
//		}

		/*사업협약 협약파일그룹ID  변경*/
//		usptBsnsCnvn.setUpdatedDt(now);
//		usptBsnsCnvn.setUpdaterId(strMemberId);
//		usptBsnsCnvnDao.updateCnvnFileGroupId(usptBsnsCnvn);

		/**주관업체서명 완료, 참여업체 전체 서명 완료 이면 서명완료로 변경 한다. */
		//참여기업 서명할 전체
		int signTotCnt = usptBsnsCnvnPrtcmpnySignDao.selectSignTotCnt(bsnsCnvnId);
		//참여기업 서명한 전체
		int signCnt = usptBsnsCnvnPrtcmpnySignDao.selectSignCnt(bsnsCnvnId);
		//주관기업 서명여부
		if(CoreUtils.string.isNotBlank(usptBsnsCnvn.getCharBsnmSignDt())) {
			if(signTotCnt == signCnt) {
				usptBsnsCnvn.setCnvnSttusCd(Code.cnvnSttus.서명완료);
				usptBsnsCnvn.setApplyId(applyId);
				usptBsnsCnvn.setUpdatedDt(now);
				usptBsnsCnvn.setUpdaterId(strMemberId);
				usptBsnsCnvnDao.updateCnvnSttus(usptBsnsCnvn);
			}
		}

		/* 협약파일 삭제 2022.11.15 삭제*/
//		List<CmmtAtchmnfl> attachFileDeleteList = frontCnvnCnclsInputParam.getAttachFileDeleteList();
//		if( attachFileDeleteList !=null && attachFileDeleteList.size() >0) {
//			for(CmmtAtchmnfl attachInfo : attachFileDeleteList) {
//				attachmentService.removeFile_keepEmptyGroup( config.getDir().getUpload(), attachInfo.getAttachmentId());
//			}
//		}
	}

	/**
	 * 전자협약 관리 협약서 다운 정보 조회
	 * @param frontCnvnCnclsParam
	 * @return
	 */
	public FrontCnvnCnclsDto getCnclsDocInfo(FrontCnvnCnclsParam frontCnvnCnclsParam){
		SecurityUtils.checkWorkerIsMember();

		/**조회 리턴**/
		FrontCnvnCnclsDto resultFrontCnvnCnclsDto = new FrontCnvnCnclsDto();

		/**기본정보 and 협약주체 조회*/
		UsptBsnsCnvn usptBsnsCnvnResult =usptBsnsCnvnDao.selectBsnsCnvnDocInfo(frontCnvnCnclsParam);

		if(usptBsnsCnvnResult == null) {
			throw new InvalidationException("완료된 협약이 존재하지 않습니다.");
		}
		if(!CoreUtils.string.equals(Code.cnvnSttus.협약완료, usptBsnsCnvnResult.getCnvnSttusCd())) {
			throw new InvalidationException("협약완료된 경우에만 협약서 다운로드를 할수 있습니다.");
		}

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