package aicluster.tsp.api.admin.eqpmn.mngm.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmMgtHistParam;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmMgtParam;
import aicluster.tsp.common.dao.TsptEqpmnMngmDao;
import aicluster.tsp.common.dao.TsptEqpmnMngmHistDao;
import aicluster.tsp.common.dao.TsptEqpmnMngmMgtDao;
import aicluster.tsp.common.dto.ApplcntDto;
import aicluster.tsp.common.entity.TsptEqpmnProcessHist;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
public class MngmService {

	@Autowired
	private TsptEqpmnMngmDao tsptEqpmnMngmDao;
	@Autowired
	private TsptEqpmnMngmHistDao tsptEqpmnMngmHistDao;
	@Autowired
	private TsptEqpmnMngmMgtDao tsptEqpmnMngmMgtDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
	@Autowired
	private HttpServletRequest request;

	Date now = new Date();

	// 장비정보 관리정보조회
	public MngmMgtParam getInfo(String eqpmnId) {
//		// 로그인 여부 검사, 내부사용자 여부 검사
//		BnMember worker = SecurityUtils.checkWorkerIsInsider();
//		// 로그인 사용자 정보 추출
//		BnMember worker = SecurityUtils.getCurrentMember();
		MngmMgtParam infoList = tsptEqpmnMngmDao.selectEqpmnMgtInfoFixSelect(eqpmnId);
		if(infoList.isTkoutAt()) {
			infoList.setTkoutParam(tsptEqpmnMngmDao.selectEqpmnMgtInfoTkout(eqpmnId));
			ApplcntDto dto = commonService.getApplcnt(infoList.getApplcntId());
			CoreUtils.property.copyProperties(infoList.getTkoutParam(), dto );

			// log 정보생성
			BnMember worker = TspUtils.getMember();
			if (!CoreUtils.string.equals(worker.getMemberId(), infoList.getApplcntId())) {
				String memberIp = CoreUtils.webutils.getRemoteIp(request);
				if(!"0:0:0:0:0:0:0:1".equals(memberIp)){
					LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
							.memberId(worker.getMemberId())
							.memberIp(memberIp)
							//.memberIp("127.0.0.1")
							.workTypeNm("조회")
							.workCn("장비정보 관리 상세정보")
							.trgterNm(dto.getUserNm())
							.trgterId(infoList.getApplcntId())
							.email(dto.getEmail())
							//.birthday("응답값에 포함된 원문 생년월일")
							.mobileNo(dto.getCttpc())
							.build();
					indvdlInfSrchUtils.insert(logParam);
				}
			}
		}
		infoList.setRepairParam(tsptEqpmnMngmDao.selectEqpmnMgtInfoRepair(infoList.getEqpmnId()));
		infoList.setCorrectParam(tsptEqpmnMngmDao.selectEqpmnMgtInfoCorrect(infoList.getCrrcId(), eqpmnId));
		infoList.setEqpmnClParam(tsptEqpmnMngmMgtDao.selectEqpmnCl());
		return infoList;
	}

	// 장비정보 관리정보삭제
	public void deleteInfo(String eqpmnId) {
		int a = tsptEqpmnMngmDao.countEqpmnUse(eqpmnId);
		if ( a > 0 )  {
			throw new InvalidationException(String.format(TspCode.validateMessage.삭제불가, "사용 이력이 있는 장비 입니다."));
		}else {
			tsptEqpmnMngmDao.deleteEqpmn(eqpmnId);
		}
	}

	// 장비정보 관리정보 불용 저장
	public MngmMgtParam unavilableInfo(String eqpmnId, String sdisuseAt) {
		// 로그인 사용자 정보 추출
		BnMember worker = TspUtils.getMember();
		if (string.isBlank(eqpmnId)) {
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "장비아이디"));
		}
		String disuseSt = TspCode.eqpmnAvailable.AVAILABLE.toString();
		boolean disuseAt = false;
		if (sdisuseAt.equals("true")) {
		disuseAt =true;
		disuseSt = TspCode.eqpmnAvailable.UNAVAILABLE.toString();
		}
		MngmMgtParam param = getInfo(eqpmnId);
		if (param.isDisuseAt()){
			if (!param.getEqpmnSttus().equals(TspCode.eqpmnSttus.AVAILABLE.toString())) {
				MngmMgtHistParam mngmMgtHistParam = getInfoView(eqpmnId, param.getEqpmnSttus());
				mngmMgtHistParam.setManageResult(String.format(TspCode.histMessage.처리이력,TspCode.eqpmnAvailable.UNAVAILABLE.toString()));
				updateInfoFinish(mngmMgtHistParam);
			}
		}
		tsptEqpmnMngmDao.updateUnavailable(eqpmnId, disuseAt);
		param = getInfo(eqpmnId);
		// 완료 처리이력 히스토리 기록
		TsptEqpmnProcessHist tsptEqpmnProcessHist = TsptEqpmnProcessHist.builder()
				.histId(string.getNewId("eqpmnHist-"))
				.eqpmnId(param.getEqpmnId())
				.mberId(worker.getMemberId())
				.processKnd(disuseSt)
				.processResn(String.format(TspCode.histMessage.처리이력,disuseSt))
				.opetrId(worker.getLoginId())
				.build();
		tsptEqpmnMngmHistDao.insertEqpmnHist(tsptEqpmnProcessHist);
		return param;
	}

	// 장비정보 관리정보 저장
	public MngmMgtParam modifyInfo(MngmParam dto) {
		// 로그인 사용자 정보 추출
		BnMember worker = TspUtils.getMember();

		// 입력된 정보 셋팅
		MngmMgtParam mngmMgtParam = tsptEqpmnMngmDao.selectEqpmnMgtInfoFixSelect(dto.getEqpmnId());

		// 시작 시간, 날짜 / 종료 시간, 날짜 / 교정등록 여부 예외처리
		if( mngmMgtParam.isDisuseAt() ){
			throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "불용여부"));
		}
		if( string.isBlank(dto.getEqpmnId()) ){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "장비아이디"));
		}
		if( dto.getRntfeeHour() == null ){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "시간당 사용료"));
		}
		if( dto.getUsefulBeginHour() == null ){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "1일 가용시작시간"));
		}
		if( dto.getUsefulEndHour() == null ){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "1일 가용종료시간"));
		}
		if (dto.isCrrcTrgetAt()) {
			if (dto.getCrrcCycle() == null) {
				throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "교정주기(일)"));
			}
		}
		if (!dto.isChckTrgetAt()) {
			if (TspCode.eqpmnSttus.INSPECT.toString().equals(mngmMgtParam.getEqpmnSttus())) {
				throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "점검여부"));
			}
		}
		// 입력값이 없으면 기존 데이터 사용
		if( dto.getRntfeeHour() != null ) {
			mngmMgtParam.setRntfeeHour(dto.getRntfeeHour());
		}
		if( dto.getUsefulBeginHour() != null ) {
			mngmMgtParam.setUsefulBeginHour(dto.getUsefulBeginHour());
		}
		if( dto.getUsefulEndHour() != null ) {
			mngmMgtParam.setUsefulEndHour(dto.getUsefulEndHour());
		}
		mngmMgtParam.setChckTrgetAt(dto.isChckTrgetAt()); 					// 점검대상여부
		mngmMgtParam.setUseRateInctvSetupAt(dto.isUseRateInctvSetupAt());		// 사용율 저조설정
		mngmMgtParam.setHldyInclsAt(dto.isHldyInclsAt());						// 법정공휴일 휴일포함
		mngmMgtParam.setTkoutHldyInclsAt(dto.isTkoutHldyInclsAt());			// 반출시 휴일포함
		mngmMgtParam.setNttkoutHldyInclsAt(dto.isNttkoutHldyInclsAt());		// 미반출시 휴일포함
		mngmMgtParam.setCrrcTrgetAt(dto.isCrrcTrgetAt());					// 교정대상여부

		if( dto.isCrrcTrgetAt() )  {
			if (dto.getCrrcCycle() != null){
				mngmMgtParam.setCrrcCycle(dto.getCrrcCycle());
			}else
				throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "교정주기"));
		}

		// 셋팅된 정보 업데이트
		tsptEqpmnMngmDao.updateEqpmnMgtInfo(mngmMgtParam);
		// 점검대상여부 TURE시 이력 등록.
		if (dto.isChckTrgetAt()) {
			if (TspCode.eqpmnSttus.AVAILABLE.toString().equals(mngmMgtParam.getEqpmnSttus())) {
				MngmMgtHistParam infoMgtHistParam = MngmMgtHistParam.builder()
						.eqpmnId(dto.getEqpmnId())
						.manageBeginDt(now)
						.manageEndDt(now)
						.manageDiv(TspCode.eqpmnSttus.INSPECT.toString())
						.creatrId(worker.getMemberId())
						.manageResn("")
						.build();
				insertInfoRegister(infoMgtHistParam);
			}
		}
		// 업데이트 이후 변경 값 조회
		mngmMgtParam = getInfo(dto.getEqpmnId());

		// 처리이력 히스토리 기록
		TsptEqpmnProcessHist tsptEqpmnProcessHist = TsptEqpmnProcessHist.builder()
				.histId(string.getNewId("eqpmnHist-"))
				.eqpmnId(dto.getEqpmnId())
				.mberId(worker.getMemberId())
				.opetrId(worker.getLoginId())
				.processKnd("SAVE")
				.processResn(String.format(TspCode.histMessage.처리이력, "저장"))
				.build();
		tsptEqpmnMngmHistDao.insertEqpmnHist(tsptEqpmnProcessHist);
		return mngmMgtParam;
	}

	// 수리/교정/점검 조회
	public MngmMgtHistParam getInfoView(String eqpmnId, String manageDiv) {
		MngmMgtParam infoList = getInfo(eqpmnId);
		String repairId = infoList.getRepairId();
		MngmMgtHistParam infoMgtHistParam = tsptEqpmnMngmMgtDao.selectEqpmnMgtHist(eqpmnId, manageDiv, repairId);
		return infoMgtHistParam;
	}

	// 수리/교정 등록
	public MngmMgtParam insertInfoRegister(MngmMgtHistParam dto) {
		// 로그인 사용자 정보 추출
		BnMember worker = TspUtils.getMember();

		MngmMgtParam mngmMgtParam = tsptEqpmnMngmDao.selectEqpmnMgtInfoFixSelect(dto.getEqpmnId());
		// 예외처리
		if( mngmMgtParam.isDisuseAt() ){
			throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "불용여부"));
		}
		if( string.isBlank(dto.getManageDiv())){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "관리구분"));
		}
		if( dto.getManageBeginDt() == null ){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "시작일"));
		}
		if( dto.getManageEndDt() == null ){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "종료일"));
		}
		if( TspCode.eqpmnSttus.CORRECTION.toString().equals(dto.getManageDiv())){
			if( dto.getCrrcInstt() == null ){
				throw new InvalidationException(String.format(TspCode.validateMessage.체크없음, "교정기관"));
			}
		}else
			if ( string.isNotBlank(dto.getCrrcInstt()) ){
				throw new InvalidationException(String.format(TspCode.validateMessage.체크없음, "교정기관"));
			}
		if( !TspCode.eqpmnSttus.AVAILABLE.toString().equals(mngmMgtParam.getEqpmnSttus())) {
			throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "상태"));
		}
		if ( string.isNotBlank(dto.getManageResult())) {
			throw new InvalidationException(String.format(TspCode.validateMessage.입력오류, "관리결과"));
		}
		// dto값 추가 셋팅
		dto.setCreatrId(worker.getMemberId());
		dto.setOpetrId(worker.getLoginId());
		dto.setMberNm(worker.getMemberNm());
		dto.setManageId(string.getNewId("eqpmnManage-"));
		tsptEqpmnMngmMgtDao.insertEqpmnMgtHist(dto);


		// 등록 처리이력 히스토리 기록
		TsptEqpmnProcessHist tsptEqpmnProcessHist = TsptEqpmnProcessHist.builder()
				.histId(string.getNewId("eqpmnHist-"))
				.eqpmnId(dto.getEqpmnId())
				.mberId(worker.getMemberId())
				.mberNm(worker.getMemberNm())
				.opetrId(worker.getLoginId())
				.processKnd(String.format(TspCode.histMessage.등록,dto.getManageDiv()))
				.processResn(String.format(TspCode.histMessage.등록사유,dto.getManageDiv()))
				.build();
		tsptEqpmnMngmHistDao.insertEqpmnHist(tsptEqpmnProcessHist);

		// 업데이트 이후 변경 값 조회
		mngmMgtParam = getInfo(dto.getEqpmnId());
		return mngmMgtParam;
	}

	// 수리/교정/점검 완료
	public MngmMgtParam updateInfoFinish(MngmMgtHistParam dto) {
		// 로그인 사용자 정보 추출
		BnMember worker = TspUtils.getMember();
		MngmMgtParam mngmMgtParam = tsptEqpmnMngmDao.selectEqpmnMgtInfoFixSelect(dto.getEqpmnId());
		// 예외처리
		if (string.isBlank(dto.getManageResult())) {
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "점검결과"));
		}
		if (!dto.getManageResult().equals(String.format(TspCode.histMessage.처리이력,TspCode.eqpmnAvailable.UNAVAILABLE.toString()))) {
			if (mngmMgtParam.isDisuseAt()) {
				throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "불용여부"));
			}
		}
		if( string.isBlank(dto.getManageId())){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "관리ID"));
		}
		if( string.isBlank(dto.getManageDiv())){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "관리구분"));
		}
		if( dto.getManageBeginDt() == null ){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "시작일"));
		}
		if( dto.getManageEndDt() == null ){
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "종료일"));
		}
		if( TspCode.eqpmnSttus.CORRECTION.toString().equals(dto.getManageDiv())){
			if( dto.getCrrcInstt() == null ){
				throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "교정기관"));
			}
		}else
			if ( string.isNotBlank(dto.getCrrcInstt()) ){
				throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "교정기관"));
			}
		if (!mngmMgtParam.getEqpmnSttus().equals(dto.getManageDiv())) {
			if (TspCode.eqpmnSttus.REPAIR_MODIY.toString().equals(dto.getManageDiv())) {
				if (string.isBlank(mngmMgtParam.getRepairId())) {
					throw new InvalidationException(String.format(TspCode.validateMessage.입력오류, "수리내역등록"));
				}
			}else {
				throw new InvalidationException(String.format(TspCode.validateMessage.입력오류, "장비상태"));
			}
		}
		if ( string.isBlank(dto.getManageResult())) {
			throw new InvalidationException(String.format(TspCode.validateMessage.입력오류, "관리결과"));
		}
		// dto값 추가 셋팅
		dto.setUpdtDt(now);
		dto.setUpdusrId(worker.getMemberId());
		tsptEqpmnMngmMgtDao.updateEqpmnMgtHist(dto);

		// 완료 처리이력 히스토리 기록
		TsptEqpmnProcessHist tsptEqpmnProcessHist = TsptEqpmnProcessHist.builder()
				.histId(string.getNewId("eqpmnHist-"))
				.eqpmnId(dto.getEqpmnId())
				.mberId(worker.getMemberId())
				.mberNm(worker.getMemberNm())
				.opetrId(worker.getLoginId())
				.processKnd(String.format(TspCode.histMessage.완료,dto.getManageDiv()))
				.processResn(String.format(TspCode.histMessage.완료사유,dto.getManageDiv()))
				.build();
		tsptEqpmnMngmHistDao.insertEqpmnHist(tsptEqpmnProcessHist);

		// 업데이트 이후 변경 값 조회
		mngmMgtParam = getInfo(dto.getEqpmnId());
		return mngmMgtParam;
	}
}

