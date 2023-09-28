package aicluster.pms.api.expert.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.expert.dto.ExpertDto;
import aicluster.pms.api.expert.dto.ExpertListExcelDto;
import aicluster.pms.api.expert.dto.ExpertListParam;
import aicluster.pms.api.expertReqst.dto.ExpertClIdDto;
import aicluster.pms.api.expertReqst.dto.ExpertClIdParntsDto;
import aicluster.pms.common.dao.UsptExclMfcmmDao;
import aicluster.pms.common.dao.UsptExpertAcdmcrDao;
import aicluster.pms.common.dao.UsptExpertCareerDao;
import aicluster.pms.common.dao.UsptExpertClChargerDao;
import aicluster.pms.common.dao.UsptExpertClDao;
import aicluster.pms.common.dao.UsptExpertClMapngDao;
import aicluster.pms.common.dao.UsptExpertCrqfcDao;
import aicluster.pms.common.dao.UsptExpertDao;
import aicluster.pms.common.dao.UsptExpertReqstHistDao;
import aicluster.pms.common.dao.UsptExtrcMfcmmDao;
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


@Service
public class ExpertService {

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
	private UsptExpertClDao usptExpertClDao;
	@Autowired
	private UsptExpertClChargerDao usptExpertClChargerDao;		/*전문가분류담당자*/
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
	@Autowired
	private UsptExclMfcmmDao usptExclMfcmmDao;		/*제외위원*/
	@Autowired
	private UsptExtrcMfcmmDao usptExtrcMfcmmDao;	/*추출위원*/


	/**
	 * 전문가 목록 조회
	 * @param expertListParam
	 * @return
	 */
	public CorePagination<UsptExpert> getExpertList(ExpertListParam expertListParam, Long page) {
		/**회원정보**/
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(expertListParam.getItemsPerPage() == null) {
			expertListParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = expertListParam.getItemsPerPage();
		expertListParam.setExpertReqstProcessSttusCd("ERPS02");	//신청 승인 완료건만
		expertListParam.setMberId(worker.getMemberId());
		// 전체 건수 확인
		long totalItems = usptExpertDao.selectExpertListCnt(expertListParam);

		// 조회할 페이지 구간 정보 확인
		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);

		expertListParam.setBeginRowNum(info.getBeginRowNum());
		expertListParam.setItemsPerPage(itemsPerPage);
		// 페이지 목록 조회
		List<UsptExpert> list = usptExpertDao.selectExpertList(expertListParam);

		for(UsptExpert outParam : list) {
			if(string.equals(outParam.getGenderCd(), "M")) {
				outParam.setGenderCd("남성");
			}else if(string.equals(outParam.getGenderCd(), "F")) {
				outParam.setGenderNm("여성");
			}
		}
		CorePagination<UsptExpert> pagination = new CorePagination<>(info, list);

		// 목록 조회
		return pagination;
	}


	/**
	 * 전문가 목록 엑셀다운로드
	 * @param pblancId
	 * @return
	 */
	public List<UsptExpert> getExpertListExcel(ExpertListParam expertListParam) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		// 전체 건수 확인
		expertListParam.setExpertReqstProcessSttusCd("ERPS02");	//신청 승인 완료건만
		expertListParam.setMberId(worker.getMemberId());
		long totalItems = usptExpertDao.selectExpertListCnt(expertListParam);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		expertListParam.setBeginRowNum(info.getBeginRowNum());
		expertListParam.setItemsPerPage(totalItems);
		return usptExpertDao.selectExpertList(expertListParam);
	}

	/**
	 * 전문가 상세정보
	 * @param expertId
	 * @return
	 */
	public ExpertDto getExpertInfo(HttpServletRequest request, String expertId) {

		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		/**조회 리턴**/
		ExpertDto resultExpertDto = new ExpertDto();

		/**전문가 기본정보**/
		UsptExpert usptExpert = new UsptExpert();
		usptExpert = usptExpertDao.selectOneExpert(expertId);

		if(usptExpert == null) {
			throw new InvalidationException(String.format(Code.validateMessage.조회결과없음, "등독된 전문"));
		}

		usptExpert.setEncBrthdy(usptExpert.getBrthdy());
		usptExpert.setEncEmail(usptExpert.getEmail());
		usptExpert.setEncMbtlnum(usptExpert.getMbtlnum());
		resultExpertDto.setUsptExpert(usptExpert);

		/** 첨부파일 목록 **/
		if (string.isNotBlank(usptExpert.getAttachmentGroupId())) {
			List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptExpert.getAttachmentGroupId());
			resultExpertDto.setAttachFileList(list);
		}

		/** 전문분야 */
		resultExpertDto.setUsptExpertClMapng(usptExpertClMapngDao.selectExpertReqsList(expertId));

		/** 전문가 경력정보 */
		UsptExpertCareer usptExpertCareer = new UsptExpertCareer();
		usptExpertCareer.setExpertId(expertId);
		resultExpertDto.setUsptExpertCareer(usptExpertCareerDao.selectList(usptExpertCareer));

		/** 전문가 학력정보 */
		UsptExpertAcdmcr usptExpertAcdmcr = new UsptExpertAcdmcr();
		usptExpertAcdmcr.setExpertId(expertId);
		resultExpertDto.setUsptExpertAcdmcr(usptExpertAcdmcrDao.selectList(usptExpertAcdmcr));

		/** 전문가 자격증정보 */
		UsptExpertCrqfc usptExpertCrqfc = new UsptExpertCrqfc();
		usptExpertCrqfc.setExpertId(expertId);
		resultExpertDto.setUsptExpertCrqfc(usptExpertCrqfcDao.selectList(usptExpertCrqfc));

		/* 개인정보 조회 이력 생성  --------------------------------------------------------------------------------*/
		//관리자가 전문가 엑셀로 등록 시 MemberId 없음.
		String targetMemberId = "";
		if(string.isBlank(usptExpert.getMemberId())){
			targetMemberId = worker.getMemberId();
		}else {
			targetMemberId = usptExpert.getMemberId();
		}
		// 신청자 정보
		LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("전문가 관리 상세조회 - 신청자정보")
				.trgterId(targetMemberId)
				.email(usptExpert.getEmail())
				.birthday(usptExpert.getBrthdy())
				.mobileNo(usptExpert.getMbtlnum())
				.build();
		indvdlInfSrchUtils.insert(logParam);

		return resultExpertDto;
	}

	/**
	 * 전문가 변경
	 * @param frontExpertParam
	 * * @param fileList
	 * @return
	 */
	public ExpertDto modifyExpert(String expertId,  ExpertDto expertDto,  List<MultipartFile> fileList) {

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String strMemberId = worker.getMemberId();

		/********************************* 신청자 정보 승인 및 수정 start**/

		/** 신청자정보(전문가) 변경 */
		UsptExpert usptExpert = expertDto.getUsptExpert();

		if(usptExpert == null) {
			throw new InvalidationException("입력된 내용이 없습니다.");
		}

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
		usptExpert.setUpdatedDt(now);
		usptExpert.setUpdaterId(strMemberId);
		usptExpertDao.update(usptExpert);

		/**전문가신청처리이력**/
		UsptExpertReqstHist usptExpertReqstHist = new UsptExpertReqstHist();
		usptExpertReqstHist.setExpertReqstHistId(CoreUtils.string.getNewId(Code.prefix.전문가신청처리이력));/** 전문가신청처리이력ID 생성 */
		usptExpertReqstHist.setExpertId(expertId);
		usptExpertReqstHist.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.승인);
		usptExpertReqstHist.setResnCn("저장 처리");
		usptExpertReqstHist.setCreatedDt(now);
		usptExpertReqstHist.setCreatorId(strMemberId);
		usptExpertReqstHistDao.insert(usptExpertReqstHist);	//insert

		/** 전문분야 */
		List<UsptExpertClMapng> usptExpertClMapngList =  expertDto.getUsptExpertClMapng();
		if(usptExpertClMapngList.size()>0) {
			//삭제
			usptExpertClMapngDao.deleteExpert(expertId);
			//등록
			for( UsptExpertClMapng input : usptExpertClMapngList) {
				input.setExpertId(expertId);
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				usptExpertClMapngDao.insertExpertClMapng(input);
			}
		}

		/** 전문가 경력정보 */
		List<UsptExpertCareer> usptExpertCareerList =  expertDto.getUsptExpertCareer();
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
				input.setExpertId(expertId);
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptExpertCareerDao.insert(input);
			}
		}

		/** 전문가 학력정보 */
		List<UsptExpertAcdmcr> usptExpertAcdmcrList=  expertDto.getUsptExpertAcdmcr();
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
				input.setExpertId(expertId);
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptExpertAcdmcrDao.insert(input);
			}
		}

		/** 전문가 자격증정보 */
		List<UsptExpertCrqfc> usptExpertCrqfcList=  expertDto.getUsptExpertCrqfc();
		if(usptExpertCrqfcList.size()>0) {
			//삭제
			usptExpertCrqfcDao.deleteExpert(expertId);
			//등록
			for( UsptExpertCrqfc input : usptExpertCrqfcList) {
				input.setExpertCrqfcId(CoreUtils.string.getNewId(Code.prefix.전문가자격증));	/** 전문가자격증ID 생성 */
				input.setExpertId(expertId);
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptExpertCrqfcDao.insert(input);
			}
		}
		/*************************************** 신청자 정보 등록 end**/
		return expertDto;
	}

	/**
	 * 전문가 삭제
	 * @param expertId
	 * @param expertListParam
	 * @return
	 */
	public void deleteExpert(ExpertListParam expertListParam){
		/**회원정보**/
		SecurityUtils.checkWorkerIsInsider();

		String expertId = expertListParam.getExpertId();
		String attachmentGroupId = expertListParam.getAttachmentGroupId();

		//매칭이력이 없는 전문가만 삭제처리 가능(uspt_extrc_mfcmm(추출위원)
		//위원회조회
		 int selectCntExtrcExpert= usptExtrcMfcmmDao.selectCntExtrcExpert(expertId);

		 if(selectCntExtrcExpert >0) {
			 throw new InvalidationException(String.format(Code.validateMessage.삭제불가, "매칭이력이 존재하는 전문가"));
		 }


//		 List<EvlSystemLogInDto> resultEvlCmitIdList = usptEvlCmitDao.selectEvlCmitIdByExpertId(expertId);
//		if(resultEvlCmitIdList.size()>0) {
//			throw new InvalidationException(String.format(Code.validateMessage.삭제불가, "위원회로 등록된 전문가"));
//		}

		 /**제외위원**/
		 usptExclMfcmmDao.deleteByExpertId(expertId);
		/**전문가신청처리이력**/
		usptExpertReqstHistDao.deleteExpert(expertId);
		/** 전문분야 */
		usptExpertClMapngDao.deleteExpert(expertId);
		/** 전문가 경력정보 */
		usptExpertCareerDao.deleteExpert(expertId);
		/** 전문가 학력정보 */
		 usptExpertAcdmcrDao.deleteExpert(expertId);
		/** 전문가 자격증정보 */
		usptExpertCrqfcDao.deleteExpert(expertId);
		/** 전문가 삭제 */
		usptExpertDao.delete(expertId);
		/** 첨부파일 목록 삭제 **/
		if (string.isNotBlank(attachmentGroupId)) {
			attachmentService.removeFiles_group(config.getDir().getUpload(), attachmentGroupId);
		}
	}

	/**
	 * 전전문가 매칭이력 조회
	 * @param expertListParam
	 * @return
	 */
	public CorePagination<UsptExpert> getExpertMatchHistList(ExpertListParam expertListParam, Long page) {
		/**회원정보**/
		SecurityUtils.checkWorkerIsInsider();

		if(page == null) {
			page = 1L;
		}
		if(expertListParam.getItemsPerPage() == null) {
			expertListParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = expertListParam.getItemsPerPage();

		// 전체 건수 확인
		long totalItems = usptExpertDao.selectExpertMatchHistListCnt(expertListParam);

		// 조회할 페이지 구간 정보 확인
		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);

		expertListParam.setBeginRowNum(info.getBeginRowNum());
		expertListParam.setItemsPerPage(itemsPerPage);

		// 페이지 목록 조회
		List<UsptExpert> list = usptExpertDao.selectExpertMatchHistList(expertListParam);

		CorePagination<UsptExpert> pagination = new CorePagination<>(info, list);

		// 목록 조회
		return pagination;
	}

	/**
	 * 전문가 매칭이력엑셀 다운로드
	 * @param expertListParam
	 * @return
	 */
	public List<UsptExpert> getExpertMatchHistListExcel(ExpertListParam expertListParam) {

		SecurityUtils.checkWorkerIsInsider();
		// 전체 건수 확인
		long totalItems = usptExpertDao.selectExpertMatchHistListCnt(expertListParam);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		expertListParam.setBeginRowNum(info.getBeginRowNum());
		expertListParam.setItemsPerPage(totalItems);

		return usptExpertDao.selectExpertMatchHistList(expertListParam);
	}

	/**
	 * 전문가 excel 등록
	 * @param frontExpertParam
	 * * @param fileList
	 * @return
	 */
	public List<ExpertListExcelDto> modifyExpertExcel( List<ExpertListExcelDto> expertListExcelDto) {

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String strMemberId = worker.getMemberId();

		/************************ 체크 밸류 start*/
		//checking
		List<ExpertListExcelDto> expertListExcelDtoCheck= expertListExcelDto;
//		getCheckValueExpertExcel(expertListExcelDto);
		/************************ 체크 밸류 end*/

		ExpertListParam expertListParam = new ExpertListParam();
		expertListParam.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.승인);
		//*** 승인건  전체 조회하여 이름, 생년월일, 휴대폰 번호, 이메일 모두 일치하면 중복건으로 판단
		List<UsptExpert> resultCompareList = usptExpertDao.selectList(expertListParam);

		//전문가 정보
		for( ExpertListExcelDto checkInputParam : expertListExcelDtoCheck) {
			/** 전문가ID 생성 */
			String newExpertId ="";
			String encBrthdy ="";
			String encMbtlnum ="";
			String encEmail ="";
			String expertId ="";
			String successYn = checkInputParam.getSuccessYn();

			//value 체크 후 정상적인 데이터만 등록
			if("Y".equals(successYn)) {
					/**
					 * 이름,생년월일, 휴대폰 번호, 이메일 모두 일치하면 중복건으로 판단
					 */
					int updateCnt = 0;
					/** 전문가 */
					UsptExpert usptExpert = new UsptExpert();

					for(UsptExpert resultCompareInfo : resultCompareList) {
						//조회된 전체 건수화 비교 하여 update or insert건수로 구분.
						if( string.equals(resultCompareInfo.getExpertClNm(), checkInputParam.getExpertClNm())
								&& 	string.equals(resultCompareInfo.getEmail(), checkInputParam.getEncEmail())
								&& string.equals(resultCompareInfo.getMbtlnum(), checkInputParam.getEncMbtlnum())){
//							&& string.equals(resultCompareInfo.getBrthdy(), checkInputParam.getEncBrthdy())
							updateCnt ++;
							//********* update에서 사용하기 위해 셋팅  */
							usptExpert = resultCompareInfo;
						}
					}	//resultCompareList for end

					/**==================================================== 등록 및 수정 start*/
					if(updateCnt>0) {
						/** 전문가 */
						encBrthdy = CoreUtils.aes256.encrypt( checkInputParam.getEncBrthdy(), usptExpert.getExpertId());	/*생년월일*/
						encMbtlnum = CoreUtils.aes256.encrypt(checkInputParam.getEncMbtlnum(), usptExpert.getExpertId());	/*휴대폰번호*/
						encEmail = CoreUtils.aes256.encrypt(checkInputParam.getEncEmail(), usptExpert.getExpertId());	/*이메일*/

						usptExpert.setExpertNm(checkInputParam.getExpertNm());	 /** 전문가명 */
						usptExpert.setGenderCd(checkInputParam.getGenderCd());	 /** 성별코드(G:GENDER) */
						usptExpert.setNativeYn(checkInputParam.getNativeYn());	 /** 내국인여부 */
						usptExpert.setEncBrthdy(encBrthdy);				   			/** 암호화된 생년월일 */
						usptExpert.setEncMbtlnum(encMbtlnum);		 				/** 암호화된 휴대폰번호 */
						usptExpert.setEncEmail(encEmail);					 			/** 암호화된 이메일 */
						usptExpert.setWrcNm(checkInputParam.getWrcNm());	 	/** 직장명 */
						usptExpert.setOfcpsNm(checkInputParam.getOfcpsNm());	 /** 직위명 */
						usptExpert.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.승인);	//접수완료
						usptExpert.setUpdatedDt(now);
						usptExpert.setUpdaterId(strMemberId);
						//수정
						usptExpertDao.update(usptExpert);

						//전문가신청처리이력용 ID 셋팅
						expertId = usptExpert.getExpertId();
					}else{
						/** 전문가ID 생성 */
						newExpertId = CoreUtils.string.getNewId(Code.prefix.전문가);
						encBrthdy = CoreUtils.aes256.encrypt( checkInputParam.getEncBrthdy(), newExpertId);	/*생년월일*/
						encMbtlnum = CoreUtils.aes256.encrypt(checkInputParam.getEncMbtlnum(), newExpertId);	/*휴대폰번호*/
						encEmail = CoreUtils.aes256.encrypt(checkInputParam.getEncEmail(), newExpertId);	/*이메일*/

						/** 전문가 */
						UsptExpert usptExpertInsert = new UsptExpert();

						usptExpertInsert.setExpertId(newExpertId);				 				/** 전문가ID */
						usptExpertInsert.setExpertNm(checkInputParam.getExpertNm());	/** 전문가명 */
						usptExpertInsert.setGenderCd(checkInputParam.getGenderCd());	/** 성별코드(G:GENDER) */
						usptExpertInsert.setNativeYn(checkInputParam.getNativeYn());		/** 내국인여부 */
						usptExpertInsert.setEncBrthdy(encBrthdy);				   				/** 암호화된 생년월일 */
						usptExpertInsert.setEncMbtlnum(encMbtlnum);		 				/** 암호화된 휴대폰번호 */
						usptExpertInsert.setEncEmail(encEmail);					 				/** 암호화된 이메일 */
						usptExpertInsert.setWrcNm(checkInputParam.getWrcNm());	 	/** 직장명 */
						usptExpertInsert.setOfcpsNm(checkInputParam.getOfcpsNm());	/** 직위명 */
						usptExpertInsert.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.승인);	//접수완료
						usptExpertInsert.setCreatedDt(now);
						usptExpertInsert.setCreatorId(strMemberId);
						usptExpertInsert.setUpdatedDt(now);
						usptExpertInsert.setUpdaterId(strMemberId);
						//등록
						usptExpertDao.insert(usptExpertInsert);

						//전문가신청처리이력용 ID 셋팅
						expertId =newExpertId;
					}

					/** 전문분야 */
					UsptExpertClMapng usptExpertClMapng = new UsptExpertClMapng();
					usptExpertClMapng.setExpertId(expertId);				 		/** 전문가ID */
					usptExpertClMapng.setExpertClId(checkInputParam.getExpertClId());	/**전문가분류ID**/

					int dupCnt = usptExpertClMapngDao.selectCheckCnt(usptExpertClMapng);

					if(dupCnt <=0) {
						usptExpertClMapng.setCreatedDt(now);
						usptExpertClMapng.setCreatorId(strMemberId);
						//등록
						usptExpertClMapngDao.insertExpertClMapng(usptExpertClMapng);
					}

					/**전문가신청처리이력**/
					UsptExpertReqstHist usptExpertReqstHist = new UsptExpertReqstHist();
					usptExpertReqstHist.setExpertReqstHistId(CoreUtils.string.getNewId(Code.prefix.전문가신청처리이력));/** 전문가신청처리이력ID 생성 */
					usptExpertReqstHist.setExpertId(expertId);
					usptExpertReqstHist.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.승인);
					usptExpertReqstHist.setResnCn("excelUpload 처리");
					usptExpertReqstHist.setCreatedDt(now);
					usptExpertReqstHist.setCreatorId(strMemberId);
					//등록
					usptExpertReqstHistDao.insert(usptExpertReqstHist);	//insert
					/**==================================================== 등록 및 수정 end*/
					//초기화
					updateCnt=0;
			}else {
				checkInputParam.setSuccessYn("N");
			}	//successYn end

		}	//checkInputParam for end

		return expertListExcelDtoCheck;
	}

	/**
	 * 전문가 excel 등록 value check
	 * @param expertListExcelDto
	 * @return List<ExpertListExcelDto>
	 */
	public List<ExpertListExcelDto> getCheckValueExpertExcel( List<ExpertListExcelDto> expertListExcelDto) {
		SecurityUtils.checkWorkerIsInsider();
		int index=0;
		for(ExpertListExcelDto inputParam : expertListExcelDto) {

			String expertClId	= inputParam.getExpertClId();          			/** 전문가분류ID */
			String expertNm 	= inputParam.getExpertNm();                  /** 전문가명 */
			String encBrthdy 	= inputParam.getEncBrthdy();                  /** 암호화된 생년월일 */
			String encMbtlnum = inputParam.getEncMbtlnum();              /** 암호화된 휴대폰번호 */
			String encEmail 	= inputParam.getEncEmail();                   /** 암호화된 이메일 */

			//기본값 셋팅
			inputParam.setSuccessYn("Y");

			if(string.isBlank(expertNm) || string.isBlank(encBrthdy) || string.isBlank(encMbtlnum)
					|| string.isBlank(encEmail)|| string.isBlank(expertClId) ) {
				inputParam.setSuccessYn("N");
			}
			//생년월일
			if(string.length(encBrthdy) != 8){
				inputParam.setSuccessYn("N");
			}

			expertListExcelDto.set(index, inputParam);
			index++;
		}
		return expertListExcelDto;
	}

	/**
	 * 전문가 등록 템플릿 엑셀 다운로드 전문가분류 조회
	 * @param
	 * @return
	 */
	public List<UsptExpertCl> selectExpertClfcTreeList() {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String memberId = worker.getMemberId();

		List<UsptExpertCl> treeList = new ArrayList<>();
		List<UsptExpertCl> returnTreeList = new ArrayList<>();

		//나의 목록 조회
		List<UsptExpertClCharger> selectResultList = usptExpertClChargerDao.selectListMyExpertCl(memberId);

		for(UsptExpertClCharger result : selectResultList) {
			treeList =usptExpertClDao.selectExpertClfcMyTreeList(result.getExpertClId());
			returnTreeList.addAll(treeList);
		}
		return returnTreeList;
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
}