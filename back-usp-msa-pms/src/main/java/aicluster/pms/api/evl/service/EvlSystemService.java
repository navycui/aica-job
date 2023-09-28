package aicluster.pms.api.evl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.common.util.TermsUtils;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.evl.dto.EvlSystemParam;
import aicluster.pms.api.evl.dto.EvlSystemTableDto;
import aicluster.pms.api.evl.dto.EvlTableDto;
import aicluster.pms.api.evl.dto.EvlTableParam;
import aicluster.pms.api.expert.dto.ExpertListParam;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptEvlCmitDao;
import aicluster.pms.common.dao.UsptEvlIemResultDao;
import aicluster.pms.common.dao.UsptEvlMfcmmDao;
import aicluster.pms.common.dao.UsptEvlMfcmmResultDao;
import aicluster.pms.common.dao.UsptEvlPlanDao;
import aicluster.pms.common.dao.UsptEvlTrgetDao;
import aicluster.pms.common.dao.UsptExpertDao;
import aicluster.pms.common.dto.EvlSttusListDto;
import aicluster.pms.common.dto.EvlSystemDto;
import aicluster.pms.common.dto.EvlSystemLogInDto;
import aicluster.pms.common.dto.EvlSystemTargetDto;
import aicluster.pms.common.dto.EvlTableListDto;
import aicluster.pms.common.entity.UsptBsnsPblancApplcnt;
import aicluster.pms.common.entity.UsptEvlCmit;
import aicluster.pms.common.entity.UsptEvlIemResult;
import aicluster.pms.common.entity.UsptEvlMfcmm;
import aicluster.pms.common.entity.UsptEvlMfcmmResult;
import aicluster.pms.common.entity.UsptEvlPlan;
import aicluster.pms.common.entity.UsptEvlTrget;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;

@Service
public class EvlSystemService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;		/*파일*/
	@Autowired
	private UsptEvlCmitDao usptEvlCmitDao;
	@Autowired
	private UsptEvlMfcmmResultDao usptEvlMfcmmResultDao;
	@Autowired
	private UsptEvlIemResultDao usptEvlIemResultDao;
	@Autowired
	private UsptEvlTrgetDao usptEvlTrgetDao;
	@Autowired
	private UsptEvlMfcmmDao usptEvlMfcmmDao;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;
	@Autowired
    private TermsUtils termsUtils;
	@Autowired
	private UsptExpertDao usptExpertDao;
	@Autowired
	private UsptEvlPlanDao usptEvlPlanDao;


	/**
	 * 평가시스템 login 평가위원회ID 조회
	 * @param
	 * @return
	 */
	public List<EvlSystemLogInDto> getEvlCmitId(EvlSystemParam evlSystemParam){

		List<EvlSystemLogInDto> dtoList = new ArrayList<>();
		//승인건만 전문가 전체 조회
		ExpertListParam expertListParam = new ExpertListParam();
		expertListParam.setExpertReqstProcessSttusCd("ERPS02");
		List<UsptExpert> resultCompareList = usptExpertDao.selectList(expertListParam);

		for(UsptExpert resultCompareInfo : resultCompareList) {
			//조회된 전체 건수화 비교 하여 update or insert건수로 구분.
			//이메일, 휴대폰번호, 위원명 비교
			if( string.equals(resultCompareInfo.getEmail(), evlSystemParam.getEvlMfcmmEmail())
					&& string.equals(resultCompareInfo.getMbtlnum(), evlSystemParam.getEvlMfcmmMbtlnum().replaceAll("-", ""))
					&& string.equals(resultCompareInfo.getExpertNm(), evlSystemParam.getEvlMfcmmNm()) ){
				//평가위원회ID 조회
				dtoList = usptEvlCmitDao.selectEvlCmitIdByExpertId(resultCompareInfo.getExpertId());
			}
		}
		if(dtoList.size()==0) {
			throw new InvalidationException( "일치하는 위원회가 존재 하지 않습니다.");
		}
		return dtoList;
	}


	/**
	 * 평가위원 정보 조회
	 * @param
	 * @return
	 */
	public EvlSystemDto getEvMfcmmInfo(EvlSystemParam evlSystemParam){
		//작업자가 평가위원인지 검사
		BnMember worker = SecurityUtils.checkWorkerIsEvaluator();
		String evlMfcmmId   =worker.getMemberId();	/*평가위원ID*/
		String evlMfcmmNm = worker.getMemberNm();	/*평가위원명*/

		//기본 로그인 한 위원으로 세팅 but 강제 id 입력 시
		if(string.isNotBlank(evlSystemParam.getEvlMfcmmId())) {
			evlMfcmmId  =  evlSystemParam.getEvlMfcmmId();		/*평가위원ID*/
			//평가위원명 조회
			UsptEvlMfcmm usptEvlMfcmm = new UsptEvlMfcmm();
			usptEvlMfcmm.setEvlMfcmmId(evlMfcmmId);;
			usptEvlMfcmm = usptEvlMfcmmDao.select(usptEvlMfcmm);
			evlMfcmmNm = usptEvlMfcmm.getEvlMfcmmNm();
		}

		EvlSystemDto evlSystemDto = new EvlSystemDto();
		//평가정보 조회
		evlSystemDto = usptEvlCmitDao.selectEvlBaseInfo(evlMfcmmId);
		evlSystemDto.setEvlMfcmmNm(evlMfcmmNm);
		//완료건 존재 여부조회
		int targetMFEV03Cnt = usptEvlCmitDao.selectEvlSystemTargetMFEV03(evlSystemDto.getEvlCmitId(), evlMfcmmId);

		if(targetMFEV03Cnt >0) {
			throw new InvalidationException( "AICA 온라인평가를 완료한 상태 입니다.");
		}
		return evlSystemDto;
	}

	/**
	 * 평가위원 평가장 입장 - 출석체크
	 * @param  chklstChkAt
	 * @return
	 */
	public void modifyAtendSttus(EvlSystemParam evlSystemParam){
		BnMember worker = SecurityUtils.checkWorkerIsEvaluator();

		/** 현재 시간*/
		Date now = new Date();
		String evlMfcmmId   =worker.getMemberId();	/*평가위원ID*/
		//기본 로그인 한 위원으로 세팅 but 강제 id 입력 시
		if(string.isNotBlank(evlSystemParam.getEvlMfcmmId())) {
			evlMfcmmId  =  evlSystemParam.getEvlMfcmmId();		/*평가위원ID*/
		}
		String atendSttusCd = Code.atendSttus.출석;		/** 출석상태코드(G:ATEND_STTUS) ATTS03*/

		UsptEvlMfcmm usptEvlMfcmm = new UsptEvlMfcmm();
		usptEvlMfcmm.setEvlMfcmmId(evlMfcmmId);
		usptEvlMfcmm.setAtendSttusCd(atendSttusCd);		/** 출석상태코드(G:ATEND_STTUS) */
		usptEvlMfcmm.setChklstChkAt(evlSystemParam.getChklstChkAt());
		usptEvlMfcmm.setAtendDt(now);						/** 출석일시 */
		usptEvlMfcmm.setUpdatedDt(now);
		usptEvlMfcmm.setUpdaterId(evlMfcmmId);

		usptEvlMfcmmDao.updateEvlSysAtendSttus(usptEvlMfcmm);

		// 약관동의정보 생성
		termsUtils.insertList(evlSystemParam.getSessionId(), worker.getMemberId());
	}

	/**
	 * 평가위원 동의 및 퇴장 - 회피동의여부
	 * @param  chklstChkAt
	 * @return
	 */
	public void modifyEvasAgreAt(EvlSystemParam evlSystemParam){
		BnMember worker = SecurityUtils.checkWorkerIsEvaluator();

		/** 현재 시간*/
		Date now = new Date();
		String evlMfcmmId   =worker.getMemberId();	/*평가위원ID*/
		//기본 로그인 한 위원으로 세팅 but 강제 id 입력 시
		if(string.isNotBlank(evlSystemParam.getEvlMfcmmId())) {
			evlMfcmmId  =  evlSystemParam.getEvlMfcmmId();		/*평가위원ID*/
		}

		String atendSttusCd = Code.atendSttus.회피;		/** 출석상태코드(G:ATEND_STTUS) ATTS04*/
		String evasResnCn = "AICA 온라인평가 회피 동의";
		Boolean evasAgreAt = true;		/*회피동의여부*/

		UsptEvlMfcmm usptEvlMfcmm = new UsptEvlMfcmm();
		usptEvlMfcmm.setEvlMfcmmId(evlMfcmmId);
		usptEvlMfcmm.setAtendSttusCd(atendSttusCd);
		usptEvlMfcmm.setEvasAgreAt(evasAgreAt);
		usptEvlMfcmm.setEvasResnCn(evasResnCn);
		usptEvlMfcmm.setUpdatedDt(now);
		usptEvlMfcmm.setUpdaterId(evlMfcmmId);

		usptEvlMfcmmDao.updateEvasAgreAt(usptEvlMfcmm);
	}

	/**
	 * 온라인 평가대상 목록 조회
	 * @param evlCmitId
	 * @return
	 */

	public List<EvlSystemTargetDto> getEvlSystemTargetList(EvlSystemParam evlSystemParam){
		BnMember worker =SecurityUtils.checkWorkerIsEvaluator();

		String evlCmitId = evlSystemParam.getEvlCmitId();		/** 평가위원회ID */
		String evlMfcmmId   =worker.getMemberId();			/** 평가위원ID*/
		/*evlCmitId, evlMfcmmId, charmnYn*/
		List<EvlSystemTargetDto> evlSystemTargetListDto = usptEvlCmitDao.selectEvlSystemTargetList(evlCmitId, evlMfcmmId);

		return evlSystemTargetListDto;
	}

	/**
	 * 온라인 평가대상 목록 - 파일그룹다운(안씀)
	 * @param
	 * @return
	 */
	public void getEvlAllFileDown(HttpServletResponse response, EvlSystemParam evlSystemParam){
		SecurityUtils.checkWorkerIsEvaluator();
		if (string.isNotBlank(evlSystemParam.getAttachmentGroupId())) {
			attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), evlSystemParam.getAttachmentGroupId(), "평가자료 파일");
		}else {
			throw new InvalidationException("요청한 평가자료가 존재하지 않습니다.");
		}
	}

	/**
	 * 평가표 조회-평가자료 다운
	 * @param evlCmitId, evlTrgetId
	 * @return
	 */
	public EvlTableDto getEvlTable(EvlTableParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsEvaluator();
		String evlMfcmmId   =worker.getMemberId();	/*평가위원ID*/
		String evlMfcmmNm = worker.getMemberNm();	/*평가위원명*/

		//기본 로그인 한 위원으로 세팅 but 강제 id 입력 시
		if(string.isNotBlank(param.getEvlMfcmmId())) {
			evlMfcmmId  =  param.getEvlMfcmmId();		/*평가위원ID*/
		}

		//평가위원 조회
		UsptEvlMfcmm usptEvlMfcmm = new UsptEvlMfcmm();
		usptEvlMfcmm.setEvlMfcmmId(evlMfcmmId);
		usptEvlMfcmm = usptEvlMfcmmDao.select(usptEvlMfcmm);
		evlMfcmmNm = usptEvlMfcmm.getEvlMfcmmNm();

		if(CoreUtils.string.isBlank(param.getEvlTrgetId())) {
			throw new InvalidationException("평가대상ID를 입력해주세요.");
		}
		if(CoreUtils.string.isBlank(param.getEvlCmitId())) {
			throw new InvalidationException("평가위원ID를 입력해주세요.");
		}
		//정보조회
		UsptEvlTrget usptEvlTrget = usptEvlTrgetDao.select(param.getEvlTrgetId());
		UsptBsnsPblancApplcnt applcnt = usptBsnsPblancApplcntDao.select(usptEvlTrget.getApplyId());

		EvlTableDto evltableDto = new EvlTableDto();
		evltableDto.setMemberNm(applcnt.getMemberNm());	/*사업자명*/
		evltableDto.setReceiptNo(applcnt.getReceiptNo());		/*접수번호*/
		evltableDto.setEvlMfcmmNm(evlMfcmmNm);				/*평가위원명*/
		//평가표조회
		param.setEvlMfcmmId(evlMfcmmId);
		List<EvlTableListDto> list = usptEvlCmitDao.selectEvlSystemTableList(param);
		//배점변환
		for(EvlTableListDto scoreParam : list) {
			scoreParam.setEvlScore(this.reEvlScore(scoreParam.getEvlScore()));
		}
		evltableDto.setList(list);
		if(list != null && list.size() > 0) {
			evltableDto.setEvlResultId(list.get(0).getEvlResultId());	/** 평가결과ID */
			evltableDto.setEvlMthdCd(list.get(0).getEvlMthdCd());	/* 평가방식코드(G:EVL_MTHD) */
			evltableDto.setEvlOpinion(list.get(0).getEvlOpinion());	/*의견 */
		}
		return evltableDto;
	}

	/**
	 * 평가표 등록
	 * @param evlResultId, evlTrgetId, evlOpinion
	 * @return
	 */
	public void modifyEvlTable(EvlSystemTableDto evlSystemTableDto){
		BnMember worker = SecurityUtils.checkWorkerIsEvaluator();
		String evlMfcmmId   =worker.getMemberId();			/** 평가위원ID*/
		/** 현재 시간*/
		Date now = new Date();
		String evlResultId	= evlSystemTableDto.getEvlResultId();		/** 평가결과ID */
		String evlTrgetId	= evlSystemTableDto.getEvlTrgetId();		/**평가대상ID*/
		String evlOpinion	= evlSystemTableDto.getEvlOpinion();		/** 평가의견 */
		String evlMthdCd	= evlSystemTableDto.getEvlMthdCd();		/** 평가방식코드(G:EVL_MTHD) */
		UsptEvlMfcmmResult usptEvlMfcmmResult = new UsptEvlMfcmmResult();

		//평가위원 기본정보 조회
		UsptEvlMfcmm usptEvlMfcmm = new UsptEvlMfcmm();
		usptEvlMfcmm 	= 	usptEvlMfcmmDao.selectEvlMfcmmIdInfo(evlMfcmmId);
		String evlCmitId = usptEvlMfcmm.getEvlCmitId();								/** 평가위원회ID */
		String evlPlanId  = usptEvlPlanDao.selectEvlPlanIdByEvlCmitId(evlCmitId);	/** 평가계획ID */

		if(string.isBlank(evlResultId)){
			//**평가결과(평가위원별) 등록*/
			evlResultId =CoreUtils.string.getNewId(Code.prefix.평가결과);
			usptEvlMfcmmResult.setEvlResultId(evlResultId);
			usptEvlMfcmmResult.setEvlMfcmmId(evlMfcmmId);
			usptEvlMfcmmResult.setEvlTrgetId(evlTrgetId);
			usptEvlMfcmmResult.setMfcmmEvlSttusCd(Code.mfcmmEvlSttus.임시저장);
			usptEvlMfcmmResult.setMfcmmEvlSttusDt(now);
			usptEvlMfcmmResult.setEvlOpinion(evlOpinion);
			usptEvlMfcmmResult.setCreatedDt(now);
			usptEvlMfcmmResult.setCreatorId(evlMfcmmId);
			usptEvlMfcmmResult.setUpdatedDt(now);
			usptEvlMfcmmResult.setUpdaterId(evlMfcmmId);
			usptEvlMfcmmResultDao.insert(usptEvlMfcmmResult);

			/*평가위원회*/
			UsptEvlCmit usptEvlCmit = new UsptEvlCmit();
			usptEvlCmit.setEvlCmitId(evlCmitId);
			usptEvlCmit.setEvlSttusCd(Code.evlSttus.진행중);
			usptEvlCmit.setEvlSttusDt(now);
			usptEvlCmit.setUpdatedDt(now);
			usptEvlCmit.setUpdaterId(evlMfcmmId);
			usptEvlCmitDao.updateEvlSttus(usptEvlCmit);

			/*평가계획*/
			UsptEvlPlan usptEvlPlan = new UsptEvlPlan();
			usptEvlPlan.setEvlPlanId(evlPlanId);
			usptEvlPlan.setEvlSttusCd(Code.evlSttus.진행중);
			usptEvlPlan.setUpdatedDt(now);
			usptEvlPlan.setUpdaterId(evlMfcmmId);
			usptEvlPlanDao.updateEvlSttusCd(usptEvlPlan);

		}else {
			usptEvlMfcmmResult.setEvlResultId(evlResultId);
			usptEvlMfcmmResult.setMfcmmEvlSttusCd(Code.mfcmmEvlSttus.임시저장);
			usptEvlMfcmmResult.setMfcmmEvlSttusDt(now);
			usptEvlMfcmmResult.setEvlOpinion(evlSystemTableDto.getEvlOpinion());
			usptEvlMfcmmResult.setUpdatedDt(now);
			usptEvlMfcmmResult.setUpdaterId(evlMfcmmId);
			usptEvlMfcmmResultDao.update(usptEvlMfcmmResult);
		}

		//**평가항목결과 등록*/
		//evlIemResultId, evlIemId, evlScore, evlCn
		List<UsptEvlIemResult> usptEvlIemResultList = evlSystemTableDto.getUsptEvlIemResultList();
		UsptEvlIemResult usptEvlIemResult = null;
		String evlIemResultId = "";				/** 평가항목결과ID */

		/*
		EVMT01	배점
		EVMT02	5점척도
		EVMT03	서술형
		*/
		for(UsptEvlIemResult inputParam : usptEvlIemResultList) {
			usptEvlIemResult = new UsptEvlIemResult();
			evlIemResultId = inputParam.getEvlIemResultId();
			if(string.isBlank(evlIemResultId)){
				evlIemResultId =CoreUtils.string.getNewId(Code.prefix.평가항목결과);
				usptEvlIemResult.setEvlIemResultId(evlIemResultId);
				usptEvlIemResult.setEvlResultId(evlResultId);
				usptEvlIemResult.setEvlIemId(inputParam.getEvlIemId());
				if(string.equals(evlMthdCd, Code.evlMthd.척도형) ) {
					usptEvlIemResult.setEvlScore(this.changeEvlScore(inputParam.getEvlScore()));
				}else {
					usptEvlIemResult.setEvlScore(inputParam.getEvlScore());
				}
				usptEvlIemResult.setEvlCn(inputParam.getEvlCn());
				usptEvlIemResult.setCreatedDt(now);
				usptEvlIemResult.setCreatorId(evlMfcmmId);
				usptEvlIemResult.setUpdatedDt(now);
				usptEvlIemResult.setUpdaterId(evlMfcmmId);
				usptEvlIemResultDao.insert(usptEvlIemResult);
			}else {
				usptEvlIemResult.setEvlIemResultId(evlIemResultId);
				if(string.equals(evlMthdCd, Code.evlMthd.척도형) ) {
					usptEvlIemResult.setEvlScore(this.changeEvlScore(inputParam.getEvlScore()));
				}else {
					usptEvlIemResult.setEvlScore(inputParam.getEvlScore());
				}
				usptEvlIemResult.setEvlCn(inputParam.getEvlCn());
				usptEvlIemResult.setUpdatedDt(now);
				usptEvlIemResult.setUpdaterId(evlMfcmmId);
				usptEvlIemResultDao.update(usptEvlIemResult);
			}
		}
	}

	/**
	 * 평가표 등록-평가완료(일반위원)
	 * @param evlCmitId
	 * @return
	 */
	public void modifyEvlTableComplete(EvlSystemParam evlSystemParam){
		BnMember worker =SecurityUtils.checkWorkerIsEvaluator();

		/** 현재 시간*/
		Date now = new Date();
		String evlCmitId = evlSystemParam.getEvlCmitId();		/** 평가위원회ID */
		String evlMfcmmId   =worker.getMemberId();			/** 평가위원ID*/
		/*evlCmitId, evlMfcmmId, charmnYn*/
		List<EvlSystemTargetDto> evlSystemTargetListDto = usptEvlCmitDao.selectEvlSystemTargetList(evlCmitId, evlMfcmmId);
		int compleCnt = 0;
		for(EvlSystemTargetDto inputParam : evlSystemTargetListDto) {
			if(string.equals(inputParam.getMfcmmEvlSttusCd(), Code.mfcmmEvlSttus.대기중)){
				throw new InvalidationException("평가대기 상태의 대상이 존재 하여 평가완료가 불가능 합니다.");
			}
			compleCnt++;
		}

		//평가완료
		if(evlSystemTargetListDto.size() ==compleCnt) {
			/*평가위원결과*/
			UsptEvlMfcmmResult usptEvlMfcmmResult = new UsptEvlMfcmmResult();
			usptEvlMfcmmResult.setEvlMfcmmId(evlMfcmmId);
			usptEvlMfcmmResult.setMfcmmEvlSttusCd(Code.mfcmmEvlSttus.평가완료);
			usptEvlMfcmmResult.setMfcmmEvlSttusDt(now);
			usptEvlMfcmmResult.setUpdatedDt(now);
			usptEvlMfcmmResult.setUpdaterId(evlMfcmmId);
			usptEvlMfcmmResultDao.updateMfcmmEvlSttusAll(usptEvlMfcmmResult);
		}
	}

	/**
	 * 평가완료-위원장 평가완료 여부 및 위원평가 여부 조회
	 * @param evlCmitId
	 * @return
	 */
	public EvlSystemParam getCharmnEvlSttus(String evlCmitId) {
		BnMember worker =SecurityUtils.checkWorkerIsEvaluator();

		String evlMfcmmId   =worker.getMemberId();			/** 평가위원ID*/
		EvlSystemParam result = new EvlSystemParam();

		//위원장 평가여부
		/*evlCmitId, evlMfcmmId, charmnYn*/
		List<EvlSystemTargetDto> myEvlSystemTargetListDto = usptEvlCmitDao.selectEvlSystemTargetList(evlCmitId, evlMfcmmId);

		String myCompleteYn = "Y";
		for(EvlSystemTargetDto inputParam : myEvlSystemTargetListDto) {
			if(string.equals(inputParam.getMfcmmEvlSttusCd(), Code.mfcmmEvlSttus.대기중)){
				myCompleteYn= "N";
			}
		}
		//위원장 제외 위원들의 평가여부
		/*evlCmitId, evlMfcmmId, charmnYn*/
		String evlMfcmmCompleteYn = "Y";
		List<EvlSystemTargetDto> evlSystemTargetListDto = usptEvlCmitDao.selectEvlMfcmmCompleteYn(evlCmitId);
		for(EvlSystemTargetDto inputParam : evlSystemTargetListDto) {
			if(string.equals(inputParam.getMfcmmEvlSttusCd(), Code.mfcmmEvlSttus.대기중)){
				evlMfcmmCompleteYn = "N";
			}
		}
		result.setMyCompleteYn(myCompleteYn);
		result.setEvlMfcmmCompleteYn(evlMfcmmCompleteYn);
		return result;
	}


	/**
	 * 위원장 종합의견 조회
	 * @param evlCmitId
	 * @return
	 */
	public List<EvlSttusListDto> getEvlSttusList(String evlCmitId) {
		SecurityUtils.checkWorkerIsEvaluator();
		/*evlCmitId, evlMfcmmId, charmnYn*/
		List<EvlSystemTargetDto> evlSystemTargetListDto = usptEvlCmitDao.selectEvlSystemTargetList(evlCmitId, null);
		if(evlSystemTargetListDto.size() <1) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		return usptEvlCmitDao.selectEvlSttusList(evlCmitId);
	}

	/**
	 * 위원장 종합의견 등록
	 * @param evlCmitId ,evlCharmnOpinionCn
	 * @return
	 */
	public void modifyEvlCharmnComplete(EvlSystemParam evlSystemParam){
		BnMember worker =SecurityUtils.checkWorkerIsEvaluator();

		/** 현재 시간*/
		Date now = new Date();
		String evlCmitId = evlSystemParam.getEvlCmitId();		/** 평가위원회ID */
		String evlMfcmmId   =worker.getMemberId();			/** 평가위원ID*/
		String evlCharmnOpinionCn = evlSystemParam.getEvlCharmnOpinionCn();	/** 평가위원장종합의견내용 */

		//평가결과 변경
		UsptEvlMfcmmResult usptEvlMfcmmResult = new UsptEvlMfcmmResult();
		usptEvlMfcmmResult.setEvlMfcmmId(evlMfcmmId);
		usptEvlMfcmmResult.setMfcmmEvlSttusCd(Code.mfcmmEvlSttus.평가완료);
		usptEvlMfcmmResult.setMfcmmEvlSttusDt(now);
		usptEvlMfcmmResult.setUpdatedDt(now);
		usptEvlMfcmmResult.setUpdaterId(evlMfcmmId);
		usptEvlMfcmmResultDao.updateMfcmmEvlSttusAll(usptEvlMfcmmResult);

		//평가위원회 의견등록
		UsptEvlCmit usptEvlCmit = new UsptEvlCmit();
		usptEvlCmit.setEvlCmitId(evlCmitId);
		usptEvlCmit.setEvlCharmnOpinionCn(evlCharmnOpinionCn);
		usptEvlCmit.setEvlSttusDt(now);
		usptEvlCmit.setUpdatedDt(now);
		usptEvlCmit.setUpdaterId(evlMfcmmId);
		usptEvlCmitDao.updateCharmnOpinion(usptEvlCmit);
	}

	/*
	 * 	EVMT02	5점척도
	 *  부족(0)	미흡(25)	보통(50)	양호(75)	우수(100)
	 */
	public Integer changeEvlScore( int evlScore) {
		if(evlScore ==1) {
			evlScore = 0;
		}else if(evlScore ==2) {
			evlScore = 25;
		}else if(evlScore ==3) {
			evlScore = 50;
		}else if(evlScore ==4) {
			evlScore = 75;
		}else if(evlScore ==5) {
			evlScore = 100;
		}
		return evlScore;
	}

	/*
	 * 	EVMT02	5점척도
	 *  부족(1)	미흡(2)	보통(3)	양호(4)	우수(5)
	 */
	public Integer reEvlScore( int evlScore) {
		if(evlScore ==0) {
			evlScore = 1;
		}else if(evlScore ==25) {
			evlScore = 2;
		}else if(evlScore ==50) {
			evlScore = 3;
		}else if(evlScore ==75) {
			evlScore = 4;
		}else if(evlScore ==100) {
			evlScore = 5;
		}
		return evlScore;
	}
}
