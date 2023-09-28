package aicluster.pms.api.expertReqst.service;

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
import aicluster.framework.common.util.TermsUtils;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.expertReqst.dto.ExpertClIdDto;
import aicluster.pms.api.expertReqst.dto.ExpertClIdParntsDto;
import aicluster.pms.api.expertReqst.dto.FrontExpertReqstCntDto;
import aicluster.pms.api.expertReqst.dto.FrontExpertReqstDto;
import aicluster.pms.common.dao.CmmtMemberDao;
import aicluster.pms.common.dao.UsptExpertAcdmcrDao;
import aicluster.pms.common.dao.UsptExpertCareerDao;
import aicluster.pms.common.dao.UsptExpertClDao;
import aicluster.pms.common.dao.UsptExpertClMapngDao;
import aicluster.pms.common.dao.UsptExpertCrqfcDao;
import aicluster.pms.common.dao.UsptExpertDao;
import aicluster.pms.common.dao.UsptExpertReqstHistDao;
import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.entity.UsptExpertAcdmcr;
import aicluster.pms.common.entity.UsptExpertCareer;
import aicluster.pms.common.entity.UsptExpertCl;
import aicluster.pms.common.entity.UsptExpertClMapng;
import aicluster.pms.common.entity.UsptExpertCrqfc;
import aicluster.pms.common.entity.UsptExpertReqstHist;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;


@Service
public class FrontExpertReqstService {

	@Autowired
	private CmmtMemberDao cmmtMemberDao;
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
	private TermsUtils termsUtils;
	/**
	 * 전문가 신청자 유무
	 * @param
	 * @return
	 */
	public FrontExpertReqstCntDto getExpertReqstCnt() {

		FrontExpertReqstCntDto frontExpertReqstCntDto = new FrontExpertReqstCntDto();
		//회원정보 조회
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		String strMemberId = worker.getMemberId();

		//현재 전문가 상태 조회
		String expertReqstProcessSttusCd = usptExpertDao.selectExpertSttusCd(strMemberId);
		if(string.isBlank(expertReqstProcessSttusCd)) {
			expertReqstProcessSttusCd = "ERPS04";	//신규(ui에서 사용하기 위한 코드)
		}
		frontExpertReqstCntDto.setExpertReqstProcessSttusCd(expertReqstProcessSttusCd);

		return frontExpertReqstCntDto;
	}

	/**
	 * 전문가 신청자정보 조회
	 * @param pblancId
	 * @return
	 */
	public FrontExpertReqstDto getExpertReqstInfo() {
		//회원정보 조회
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		String strMemberId = worker.getMemberId();
		String expertId = usptExpertDao.selectExpertId(strMemberId);

		FrontExpertReqstDto dto = new FrontExpertReqstDto();

		//전문가 미등록 시
		if(string.isBlank(expertId)) {
			CmmtMember selectResult = cmmtMemberDao.select(strMemberId);
			if(selectResult !=null ) {

				/**전문가 기본정보**/
				UsptExpert usptExpert = new UsptExpert();
				usptExpert.setMemberId(selectResult.getMemberId());		/**회원ID**/
				usptExpert.setExpertNm(selectResult.getMemberNm());		/**회원명(사업자명)**/
				usptExpert.setGenderCd(selectResult.getGender());			/**성별**/
				if(string.equals(selectResult.getGender(), "M")) {
					usptExpert.setGenderNm("남자");
				}else {
					usptExpert.setGenderNm("여자");
				}
				usptExpert.setEncBrthdy(selectResult.getBirthday());			/**생년월일**/
				usptExpert.setEncEmail(selectResult.getEmail());				/**이메일**/
				usptExpert.setEncMbtlnum(selectResult.getMobileNo());	/**휴대폰번호**/
				dto.setUsptExpert(usptExpert);
//				dto.setMemberId(selectResult.getMemberId());		/**회원ID**/
//				dto.setMemberNm(selectResult.getMemberNm());	/**회원명(사업자명)**/
//				dto.setGender(selectResult.getGender());				/**성별**/
//				dto.setEncBirthday(selectResult.getBirthday());		/**생년월일**/
//				dto.setEncMobileNo(selectResult.getMobileNo());	/**휴대폰번호**/
//				dto.setEncEmail(selectResult.getEmail());				/**이메일**/
			}
		}else {

			/**전문가 기본정보**/
			UsptExpert usptExpert = new UsptExpert();
			usptExpert = usptExpertDao.selectOneExpert(expertId);

			if(usptExpert == null) {
				throw new InvalidationException(String.format(Code.validateMessage.조회결과없음, "전문가 신청 내역"));
			}
			usptExpert.setEncBrthdy(usptExpert.getBrthdy());
			usptExpert.setEncEmail(usptExpert.getEmail());
			usptExpert.setEncMbtlnum(usptExpert.getMbtlnum());
			dto.setUsptExpert(usptExpert);

			/** 첨부파일 목록 **/
			if (string.isNotBlank(usptExpert.getAttachmentGroupId())) {
				List<CmmtAtchmnfl> list = attachmentService.getFileInfos_group(usptExpert.getAttachmentGroupId());
				dto.setAttachFileList(list);
			}

			/** 전문분야 */
			dto.setUsptExpertClMapng(usptExpertClMapngDao.selectExpertReqsList(expertId));

			/** 전문가 경력정보 */
			UsptExpertCareer usptExpertCareer = new UsptExpertCareer();
			usptExpertCareer.setExpertId(expertId);
			dto.setUsptExpertCareer(usptExpertCareerDao.selectList(usptExpertCareer));

			/** 전문가 학력정보 */
			UsptExpertAcdmcr usptExpertAcdmcr = new UsptExpertAcdmcr();
			usptExpertAcdmcr.setExpertId(expertId);
			dto.setUsptExpertAcdmcr(usptExpertAcdmcrDao.selectList(usptExpertAcdmcr));

			/** 전문가 자격증정보 */
			UsptExpertCrqfc usptExpertCrqfc = new UsptExpertCrqfc();
			usptExpertCrqfc.setExpertId(expertId);
			dto.setUsptExpertCrqfc(usptExpertCrqfcDao.selectList(usptExpertCrqfc));
		}
		return dto;
	}

	/**
	 * 전문가 신청자 등록
	 * @param frontExpertReqstDto
	 * * @param fileList
	 * @return
	 */
	public void add( FrontExpertReqstDto frontExpertReqstDto,  List<MultipartFile> fileList) {

		/** 현재 시간*/
		Date now = new Date();
		/** 로그인 회원ID **/
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		String strMemberId = worker.getMemberId();

		/********************************* 신청자 정보 등록 start**/

		/** 신청자정보(전문가) 등록 */
		UsptExpert usptExpert = frontExpertReqstDto.getUsptExpert();

		if(CoreUtils.string.isBlank(usptExpert.getLastUnivNm())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "최종대학명"));
		}
		if(CoreUtils.string.isBlank(usptExpert.getUnivDeptNm())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "대학학부명"));
		}

		/** 전문가 분야정보 */
		List<UsptExpertClMapng> usptExpertClMapngListCheck = frontExpertReqstDto.getUsptExpertClMapng();
		if(usptExpertClMapngListCheck.size()<1) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "전문분야"));
		}else {
			for(UsptExpertClMapng inputParam : usptExpertClMapngListCheck ) {
				if(string.isBlank(inputParam.getExpertClId())){
					throw new InvalidationException("전문가분야를 선택하세요.");
				}
			}
		}

		/** 전문가 경력정보 */
		List<UsptExpertCareer> usptExpertCareerListCheck = frontExpertReqstDto.getUsptExpertCareer();
		if(usptExpertCareerListCheck.size()<1) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "경력정보"));
		}else {
			for(UsptExpertCareer inputParam : usptExpertCareerListCheck ) {
				if(string.isBlank(inputParam.getWorkBgnde())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "근무시작일"));
				}else if(string.isBlank(inputParam.getWorkEndde())){
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "근무종료일"));
				}else if(string.isBlank(inputParam.getWrcNm())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "직장명"));
				}else if(string.isBlank(inputParam.getDeptNm())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "부서명"));
				}else if(string.isBlank(inputParam.getOfcpsNm())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "직위"));
				}else if(string.isBlank(inputParam.getChrgJobNm())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "담당업무"));
				}
			}
		}

		/** 전문가 학력정보 */
		List<UsptExpertAcdmcr> usptExpertAcdmcrListCheck = frontExpertReqstDto.getUsptExpertAcdmcr();
		if(usptExpertAcdmcrListCheck.size()<1) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "학력정보"));
		}else {
			for(UsptExpertAcdmcr inputParam : usptExpertAcdmcrListCheck ) {
				if(string.isBlank(inputParam.getAcdmcrBgnde())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "학력시작일"));
				}else if(string.isBlank(inputParam.getAcdmcrEndde())){
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "학력종료일"));
				}else if(string.isBlank(inputParam.getDgriCd())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "학위"));
				}else if(string.isBlank(inputParam.getSchulNm())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "학교명"));
				}else if(string.isBlank(inputParam.getMajorNm())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "전공명"));
				}else if(string.isBlank(inputParam.getProfsrNm())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "지도교수명"));
				}else if(string.isBlank(inputParam.getGrdtnDivCd())){
					throw new InvalidationException(String.format(Code.validateMessage.입력없음, "졸업구분"));
				}
			}
		}

		/** 첨부파일*/
		if(fileList != null && fileList.size() > 0) {
			if(CoreUtils.string.isNotBlank(usptExpert.getAttachmentGroupId())) {
				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), usptExpert.getAttachmentGroupId(), fileList, null, 0);
			} else {
				CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, null, 0);
				 /** 보완요청파일그룹ID */
				usptExpert.setAttachmentGroupId(fileGroupInfo.getAttachmentGroupId());
			}
		}

		/** 신청자정보(전문가) 변경 */
		String expertId = usptExpert.getExpertId();
		String newExpertId = "";
		//신규, 기존 구분
		if(string.isBlank(expertId)){
			/** 전문가ID 생성 */
			newExpertId = CoreUtils.string.getNewId(Code.prefix.전문가);
		}else {
			newExpertId = expertId;
		}

		String encBrthdy = CoreUtils.aes256.encrypt( usptExpert.getEncBrthdy(), newExpertId);	/*생년월일*/
		String encMbtlnum = CoreUtils.aes256.encrypt(usptExpert.getEncMbtlnum(), newExpertId);	/*휴대폰번호*/
		String encEmail = CoreUtils.aes256.encrypt(usptExpert.getEncEmail(), newExpertId);	/*이메일*/
		usptExpert.setExpertId(newExpertId);
		usptExpert.setMemberId(strMemberId);
		usptExpert.setEncBrthdy(encBrthdy);
		usptExpert.setEncMbtlnum(encMbtlnum);
		usptExpert.setEncEmail(encEmail);
		usptExpert.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.신청);
		usptExpert.setCreatedDt(now);
		usptExpert.setCreatorId(strMemberId);
		usptExpert.setUpdatedDt(now);
		usptExpert.setUpdaterId(strMemberId);

		if(string.isBlank(expertId)){
			usptExpertDao.insert(usptExpert);	//insert
		}else {
			usptExpertDao.update(usptExpert);
		}

		/**전문가신청처리이력**/
		UsptExpertReqstHist usptExpertReqstHist = new UsptExpertReqstHist();
		usptExpertReqstHist.setExpertReqstHistId(CoreUtils.string.getNewId(Code.prefix.전문가신청처리이력));/** 전문가신청처리이력ID 생성 */
		usptExpertReqstHist.setExpertId(newExpertId);
		usptExpertReqstHist.setExpertReqstProcessSttusCd(Code.expertReqstProcessSttus.신청);
		usptExpertReqstHist.setResnCn("접수신청 처리");
		usptExpertReqstHist.setCreatedDt(now);
		usptExpertReqstHist.setCreatorId(strMemberId);
		usptExpertReqstHistDao.insert(usptExpertReqstHist);	//insert

		/** 전문분야 */
		List<UsptExpertClMapng> usptExpertClMapngList =  frontExpertReqstDto.getUsptExpertClMapng();
		if(usptExpertClMapngList.size()>0) {
			//삭제
			usptExpertClMapngDao.deleteExpert(newExpertId);
			//등록
			for( UsptExpertClMapng input : usptExpertClMapngList) {
				input.setExpertId(newExpertId);
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				usptExpertClMapngDao.insertExpertClMapng(input);
			}
		}

		/** 전문가 경력정보 */
		List<UsptExpertCareer> usptExpertCareerList =  frontExpertReqstDto.getUsptExpertCareer();
		if(usptExpertCareerList.size()>0) {
			//등록
			for( UsptExpertCareer input : usptExpertCareerList) {
				String  workBgnde = input.getWorkBgnde();
				String  workEndde = input.getWorkEndde();
				if(workBgnde.compareTo(workEndde)==1) {
					throw new InvalidationException(String.format("경력정보(근무기간) 시작일자는 종료일자보다 클수 없습니다."));
				}
			}
			//삭제
			usptExpertCareerDao.deleteExpert(newExpertId);
			//등록
			for( UsptExpertCareer input : usptExpertCareerList) {
				input.setExpertCareerId(CoreUtils.string.getNewId(Code.prefix.전문가경력));	/** 전문가경력ID 생성 */
				input.setExpertId(newExpertId);
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptExpertCareerDao.insert(input);
			}
		}

		/** 전문가 학력정보 */
		List<UsptExpertAcdmcr> usptExpertAcdmcrList=  frontExpertReqstDto.getUsptExpertAcdmcr();
		if(usptExpertAcdmcrList.size()>0) {

			for( UsptExpertAcdmcr input : usptExpertAcdmcrList) {
				String  acdmcrBgnde= 	input.getAcdmcrBgnde();
				String  acdmcrEndde = input.getAcdmcrEndde();
				if(acdmcrBgnde.compareTo(acdmcrEndde)==1) {
					throw new InvalidationException(String.format("학력정보(기간) 시작일자는 종료일자보다 클수 없습니다."));
				}
			}
			//삭제
			usptExpertAcdmcrDao.deleteExpert(newExpertId);
			//등록
			for( UsptExpertAcdmcr input : usptExpertAcdmcrList) {
				input.setExpertAcdmcrId(CoreUtils.string.getNewId(Code.prefix.전문가학력));	/** 전문가학력ID 생성 */
				input.setExpertId(newExpertId);
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptExpertAcdmcrDao.insert(input);
			}
		}

		/** 전문가 자격증정보 */
		List<UsptExpertCrqfc> usptExpertCrqfcList=  frontExpertReqstDto.getUsptExpertCrqfc();
		if(usptExpertCrqfcList.size()>0) {
			//삭제
			usptExpertCrqfcDao.deleteExpert(newExpertId);
			for( UsptExpertCrqfc input : usptExpertCrqfcList) {
				input.setExpertCrqfcId(CoreUtils.string.getNewId(Code.prefix.전문가자격증));	/** 전문가자격증ID 생성 */
				input.setExpertId(newExpertId);
				input.setCreatedDt(now);
				input.setCreatorId(strMemberId);
				input.setUpdatedDt(now);
				input.setUpdaterId(strMemberId);
				usptExpertCrqfcDao.insert(input);
			}
			/*************************************** 신청자 정보 등록 end**/
		}

		/* 첨부파일 삭제 */
		List<CmmtAtchmnfl> attachFileDeleteList = frontExpertReqstDto.getAttachFileDeleteList();
		if( attachFileDeleteList !=null && attachFileDeleteList.size() >0) {
			for(CmmtAtchmnfl attachInfo : attachFileDeleteList) {
				attachmentService.removeFile_keepEmptyGroup( config.getDir().getUpload(), attachInfo.getAttachmentId());
			}
		}

		// 약관동의정보 생성
		//2022.11.11 한번 개인정보 동의하면 다시 동의 필요없음
		//ui에서 SessionId 안보내줌...
		if( string.isNotBlank(frontExpertReqstDto.getSessionId())){
			termsUtils.insertList(frontExpertReqstDto.getSessionId(), worker.getMemberId());
		}
	}

	/**
	 * 전문가 분류조회_부모전문가분류 조회
	 * @param pblancId
	 * @return
	 */
	public List<ExpertClIdParntsDto> selectParntsExpertClIdList() {
		/** 로그인 회원ID **/
		SecurityUtils.checkWorkerIsMember();

		// 로그인 사용자의 전문가 분류 조회-기본(무조건 common)
		String expertClId ="COMMON";
		List<ExpertClIdParntsDto> expertClIdParntsDtoList = new ArrayList <>();
		List<UsptExpertCl> usptExpertClList= usptExpertClDao.selectExpertClIdInfoList(expertClId);

		for(UsptExpertCl info : usptExpertClList) {
			ExpertClIdParntsDto expertClIdParntsDto = new ExpertClIdParntsDto();
			expertClIdParntsDto.setParntsExpertClId(info.getParntsExpertClId());
			expertClIdParntsDto.setExpertClId(info.getExpertClId());
			expertClIdParntsDto.setExpertClNm(info.getExpertClNm());
			expertClIdParntsDtoList.add(expertClIdParntsDto);
		}
		return expertClIdParntsDtoList;
	}

	/**
	 * 전문가 분류조회_전문가분류 조회
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