package aicluster.pms.api.evl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import aicluster.pms.api.evl.dto.DspthResultDto;
import aicluster.pms.api.evl.dto.EvasResnCnDto;
import aicluster.pms.api.evl.dto.EvlCharmnOpinionDto;
import aicluster.pms.api.evl.dto.EvlOfflineTableDto;
import aicluster.pms.api.evl.dto.EvlResultConsentFormDto;
import aicluster.pms.api.evl.dto.EvlResultDspthParam;
import aicluster.pms.api.evl.dto.EvlResultDto;
import aicluster.pms.api.evl.dto.EvlTableDto;
import aicluster.pms.api.evl.dto.EvlTableParam;
import aicluster.pms.api.evl.dto.EvlTargetListParam;
import aicluster.pms.api.evl.dto.EvlTrgetIdParam;
import aicluster.pms.api.evl.dto.GnrlzEvlDto;
import aicluster.pms.api.evl.dto.PointListDto;
import aicluster.pms.api.evl.dto.PointListParam;
import aicluster.pms.api.evl.dto.PointParam;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptEvlCmitDao;
import aicluster.pms.common.dao.UsptEvlIemResultAdsbtrDao;
import aicluster.pms.common.dao.UsptEvlIemResultDao;
import aicluster.pms.common.dao.UsptEvlMfcmmDao;
import aicluster.pms.common.dao.UsptEvlMfcmmResultDao;
import aicluster.pms.common.dao.UsptEvlPlanDao;
import aicluster.pms.common.dao.UsptEvlStepDao;
import aicluster.pms.common.dao.UsptEvlTrgetDao;
import aicluster.pms.common.dao.UsptExpertDao;
import aicluster.pms.common.dto.EvlAtendListDto;
import aicluster.pms.common.dto.EvlPointListDto;
import aicluster.pms.common.dto.EvlResultListDto;
import aicluster.pms.common.dto.EvlSttusListDto;
import aicluster.pms.common.dto.EvlSttusMfcmmListDto;
import aicluster.pms.common.dto.EvlTableListDto;
import aicluster.pms.common.dto.EvlTargetListDto;
import aicluster.pms.common.dto.SlctnListDto;
import aicluster.pms.common.entity.UsptBsnsPblancApplcnt;
import aicluster.pms.common.entity.UsptEvlCmit;
import aicluster.pms.common.entity.UsptEvlIemResult;
import aicluster.pms.common.entity.UsptEvlIemResultAdsbtr;
import aicluster.pms.common.entity.UsptEvlMfcmm;
import aicluster.pms.common.entity.UsptEvlMfcmmResult;
import aicluster.pms.common.entity.UsptEvlPlan;
import aicluster.pms.common.entity.UsptEvlStep;
import aicluster.pms.common.entity.UsptEvlTrget;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.LoggableException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EvlOfflineService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private UsptEvlCmitDao usptEvlCmitDao;
	@Autowired
	private UsptExpertDao usptExpertDao;
	@Autowired
	private UsptEvlStepDao usptEvlStepDao;
	@Autowired
	private UsptEvlTrgetDao usptEvlTrgetDao;
	@Autowired
	private UsptEvlMfcmmDao usptEvlMfcmmDao;
	@Autowired
	private UsptEvlIemResultAdsbtrDao usptEvlIemResultAdsbtrDao;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private UsptEvlMfcmmResultDao usptEvlMfcmmResultDao;
	@Autowired
	private UsptEvlIemResultDao usptEvlIemResultDao;
	@Autowired
	private UsptEvlPlanDao usptEvlPlanDao;

	/**
	 * 온라인평가 기본정보 조회
	 * @param evlCmitId
	 * @return
	 */
	public EvlTargetListDto getEvlBasicInfo(String evlCmitId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		EvlTargetListParam param = new EvlTargetListParam();
		param.setInsiderId(worker.getMemberId());
		param.setEvlCmitId(evlCmitId);
		param.setBeginRowNum(1L);
		param.setItemsPerPage(ITEMS_PER_PAGE);
		List<EvlTargetListDto> list = usptEvlCmitDao.selectEvlTargetList(param);
		if(list == null || list.size() == 0) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		return list.get(0);
	}


	/**
	 * 평가위원 출석표 목록 조회
	 * @param evlCmitId
	 * @return
	 */
	public List<EvlAtendListDto> getEvlAtendList(String evlCmitId) {
		return usptEvlMfcmmDao.selectEvlAtendList(evlCmitId);
	}


	/**
	 * 위원장 설정
	 * @param uemInfo
	 */
	public void modifyCharmn(UsptEvlMfcmm uemInfo) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptEvlMfcmm info = usptEvlMfcmmDao.select(uemInfo);
		if(info == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원 정보가 없습니다.");
		}

		EvlTargetListDto dto = this.getEvlBasicInfo(info.getEvlCmitId());
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(dto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}

		if(CoreUtils.string.equals(dto.getEvlSttusCd(), Code.evlSttus.평가완료)) {
			throw new InvalidationException("이미 평가완료 처리가 되었습니다. \n처리할 수 없습니다. ");
		}

		Date now = new Date();
		UsptEvlMfcmm orgCharmnInfo = usptEvlMfcmmDao.selectCharmn(uemInfo.getEvlCmitId());
		if(orgCharmnInfo != null) {
			orgCharmnInfo.setCharmn(false);
			orgCharmnInfo.setUpdatedDt(now);
			orgCharmnInfo.setUpdaterId(worker.getMemberId());
			usptEvlMfcmmDao.updateCharmn(orgCharmnInfo);
		}

		info.setCharmn(true);
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());
		if(usptEvlMfcmmDao.updateCharmn(info) != 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}
	}


	/**
	 * 출석상태 수정
	 * @param uemInfo
	 */
	public void modifyAtendSttus(UsptEvlMfcmm uemInfo) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptEvlMfcmm info = usptEvlMfcmmDao.select(uemInfo);
		if(info == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원 정보가 없습니다.");
		}
		EvlTargetListDto dto = this.getEvlBasicInfo(info.getEvlCmitId());
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(dto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}
		if(CoreUtils.string.equals(dto.getEvlSttusCd(), Code.evlSttus.평가완료)) {
			throw new InvalidationException("이미 평가완료 처리가 되었습니다. \n처리할 수 없습니다. ");
		}

		if(CoreUtils.string.equals(Code.atendSttus.출석, uemInfo.getAtendSttusCd())) {
			if(!string.equals(info.getAtendSttusCd(), Code.atendSttus.대기중)){
				throw new InvalidationException("대기중인 건만 출석 처리가 가능합니다.");
			}
			uemInfo.setEvasResnCn(info.getEvasResnCn());
		}

		if(CoreUtils.string.equals(Code.atendSttus.불참, uemInfo.getAtendSttusCd())) {
			if(!string.equals(info.getAtendSttusCd(), Code.atendSttus.대기중)){
				throw new InvalidationException("대기중인 건만 불참 처리가 가능합니다.");
			}
			uemInfo.setEvasResnCn(info.getEvasResnCn());
		}

		if(CoreUtils.string.equals(Code.atendSttus.회피, uemInfo.getAtendSttusCd())) {
			if(CoreUtils.string.isBlank(uemInfo.getEvasResnCn())){
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "회피사유"));
			}
			if(!string.equals(info.getAtendSttusCd(), Code.atendSttus.대기중) &&  !string.equals(info.getAtendSttusCd(), Code.atendSttus.출석)){
				throw new InvalidationException("대기중 또는 출석인 건만 회피 처리가 가능합니다.");
			}
		}

		Date now = new Date();
		uemInfo.setUpdatedDt(now);
		uemInfo.setUpdaterId(worker.getMemberId());
		if(usptEvlMfcmmDao.updateAtendSttus(uemInfo) != 1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "수정"));
		}
	}


	/**
	 * 출석상태 취소 수정
	 * @param uemInfo
	 */
	public void modifyAtendSttusCancel(UsptEvlMfcmm uemInfo) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptEvlMfcmm info = usptEvlMfcmmDao.select(uemInfo);
		if(info == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원 정보가 없습니다.");
		}
		EvlTargetListDto dto = this.getEvlBasicInfo(info.getEvlCmitId());
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(dto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}
		if(CoreUtils.string.equals(dto.getEvlSttusCd(), Code.evlSttus.평가완료)) {
			throw new InvalidationException("이미 평가완료 처리가 되었습니다. \n처리할 수 없습니다. ");
		}

		if(CoreUtils.string.equals(Code.atendSttus.불참, uemInfo.getAtendSttusCd())) {
			if(!string.equals(info.getAtendSttusCd(), Code.atendSttus.불참)) {
				throw new InvalidationException("불참 상태가 아닌 평가위원입니다.");
			}
			uemInfo.setEvasResnCn(info.getEvasResnCn());
		}

		if(CoreUtils.string.equals(Code.atendSttus.회피, uemInfo.getAtendSttusCd())) {
			if(!string.equals(info.getAtendSttusCd(), Code.atendSttus.회피)) {
				throw new InvalidationException("회피 상태가 아닌 평가위원입니다.");
			}
			uemInfo.setEvasResnCn("");
		}

		Date now = new Date();
		uemInfo.setUpdatedDt(now);
		uemInfo.setUpdaterId(worker.getMemberId());
		uemInfo.setAtendSttusCd(Code.atendSttus.대기중);
		if(usptEvlMfcmmDao.updateAtendSttus(uemInfo) != 1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "수정"));
		}
	}

	/**
	 * 회피사유 조회
	 * @param uemInfo
	 * @return
	 */
	public EvasResnCnDto getEvasResnCn(UsptEvlMfcmm uemInfo){
		SecurityUtils.checkWorkerIsInsider();
		UsptEvlMfcmm info = usptEvlMfcmmDao.select(uemInfo);
		if(info == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원 정보가 없습니다.");
		}
		if(!string.equals(info.getAtendSttusCd(), Code.atendSttus.회피)) {
			throw new InvalidationException("회피 상태가 아닌 평가위원입니다.");
		}

		UsptExpert expertInfo = usptExpertDao.selectMfcmmDetail(info.getExtrcMfcmmId());
		EvasResnCnDto dto = new EvasResnCnDto();
		dto.setEvasResnCn(info.getEvasResnCn());
		dto.setEvlMfcmmNm(expertInfo.getExpertNm());
		return dto;
	}

	/**
	 *  회피사유 저장
	 * @param uemInfo
	 */
	public void modifyEvasResnCn(UsptEvlMfcmm uemInfo){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptEvlMfcmm info = usptEvlMfcmmDao.select(uemInfo);
		if(info == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원 정보가 없습니다.");
		}
		EvlTargetListDto dto = this.getEvlBasicInfo(info.getEvlCmitId());
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(dto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}
		if(CoreUtils.string.equals(dto.getEvlSttusCd(), Code.evlSttus.평가완료)) {
			throw new InvalidationException("이미 평가완료 처리가 되었습니다. \n처리할 수 없습니다. ");
		}

		if(string.isBlank(uemInfo.getEvasResnCn())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "회피 사유"));
		}

		if(!string.equals(info.getAtendSttusCd(), Code.atendSttus.회피)) {
			throw new InvalidationException("회피 상태가 아니라서 회피사유를 저장할 수 없습니다.");
		}

		Date now = new Date();
		uemInfo.setUpdatedDt(now);
		uemInfo.setUpdaterId(worker.getMemberId());
		uemInfo.setAtendSttusCd(info.getAtendSttusCd());
		if(usptEvlMfcmmDao.updateAtendSttus(uemInfo) != 1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "수정"));
		}
	}


	/**
	 * 평가현황 목록 조회
	 * @param evlCmitId
	 * @return
	 */
	public List<EvlSttusListDto> getEvlSttusList(String evlCmitId) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		return usptEvlCmitDao.selectEvlSttusList(evlCmitId);
	}


	/**
	 * 가감점수 목록 조회
	 * @param param
	 * @return
	 */
	public PointListDto getPointList(PointListParam param) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(param.getEvlCmitId());
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(CoreUtils.string.isBlank(param.getEvlTrgetId())) {
			throw new InvalidationException("평가대상ID를 입력하세요.");
		}

		UsptEvlTrget usptEvlTrget = usptEvlTrgetDao.select(param.getEvlTrgetId());
		UsptBsnsPblancApplcnt applcnt = usptBsnsPblancApplcntDao.select(usptEvlTrget.getApplyId());

		EvlTableDto evltableDto = new EvlTableDto();
		evltableDto.setMemberNm(applcnt.getMemberNm());
		evltableDto.setReceiptNo(applcnt.getReceiptNo());

		PointListDto pointDto = new PointListDto();
		pointDto.setMemberNm(applcnt.getMemberNm());
		pointDto.setReceiptNo(applcnt.getReceiptNo());

		List<EvlPointListDto> list = usptEvlCmitDao.selectEvlPointList(param);
		pointDto.setPointList(list);
		if(list != null && list.size() > 0) {
			pointDto.setAdsbtrResnCn(list.get(0).getAdsbtrResnCn());
			pointDto.setEvlMthdCd(list.get(0).getEvlMthdCd());
		}
		return pointDto;
	}


	/**
	 * 가감점수 저장
	 * @param evlCmitId
	 * @param evlTrgetId
	 * @param param
	 */
	public void modifyPoint(String evlCmitId, String evlTrgetId, PointParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(dto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}
		if(CoreUtils.string.equals(dto.getEvlSttusCd(), Code.evlSttus.평가완료)) {
			throw new InvalidationException("이미 평가완료 처리가 되었습니다. \n처리할 수 없습니다. ");
		}
		if(!CoreUtils.string.equals(dto.getOrgnzrId(), worker.getMemberId())) {
			throw new InvalidationException("평가위원회의 간사만 저장할 수 있습니다.");
		}

		List<UsptEvlIemResultAdsbtr> resultList = param.getResultList();
		if(resultList == null || resultList.size() == 0) {
			throw new InvalidationException("정보가 입력되지 않았습니다.");
		}

		Date now = new Date();
		for(UsptEvlIemResultAdsbtr regInfo : resultList) {
			regInfo.setCreatedDt(now);
			regInfo.setCreatorId(worker.getMemberId());
			regInfo.setUpdatedDt(now);
			regInfo.setUpdaterId(worker.getMemberId());
			regInfo.setAdsbtrResnCn(param.getAdsbtrResnCn());
			regInfo.setEvlTrgetId(evlTrgetId);
			regInfo.setEvlCmitId(evlCmitId);

			if(CoreUtils.string.isBlank(regInfo.getEvlIemResultAdsbtrId())) {
				regInfo.setEvlIemResultAdsbtrId(CoreUtils.string.getNewId(Code.prefix.평가항목결과가감));
				usptEvlIemResultAdsbtrDao.insert(regInfo);
			} else {
				if(usptEvlIemResultAdsbtrDao.update(regInfo) != 1) {
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
				}
			}
		}
	}



	/**
	 * 평가표 다운로드
	 * @param param
	 * @return
	 */
	public List<EvlTableDto> getDownEvlTableList(String evlCmitId) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}

		String evlTrgetId ="";			/** 평가대상ID */
		String evlMfcmmId ="";		/** 평가위원ID */

		EvlTableParam inputParam = null;		//평가표 input조회
		EvlTableDto returnInfo		 = null;		//평가표 result
		List<EvlSttusMfcmmListDto> evlMfcmmList =  new ArrayList<>();	//평가위원 목록
		List<EvlTableDto> downEvlTableLis = new ArrayList<>();				//return

		//대상조회
		List<EvlSttusListDto> selectTargetList =  usptEvlCmitDao.selectEvlSttusList(evlCmitId);

		for( EvlSttusListDto target : selectTargetList) {
			//평가대상ID
			evlTrgetId = target.getEvlTrgetId();
			//평가위원 목록
			evlMfcmmList= target.getEvlSttusMfcmmList();

			for(EvlSttusMfcmmListDto evlMfcmm : evlMfcmmList ) {
				evlMfcmmId = evlMfcmm.getEvlMfcmmId();		/** 평가위원ID */
				//평가표 조회
				inputParam = new EvlTableParam();
				inputParam.setEvlCmitId(evlCmitId);
				inputParam.setEvlTrgetId(evlTrgetId);
				inputParam.setEvlMfcmmId(evlMfcmmId);
				returnInfo= new EvlTableDto();
				returnInfo = this.getEvlTable(inputParam);

				//평가내용 초기화
				List<EvlTableListDto> list =  returnInfo.getList();
				for(EvlTableListDto evlTableDto : list ) {
					evlTableDto.setEvlCn("");			/** 평가내용 */
				}
				//return List setting
				downEvlTableLis.add(returnInfo);
			}
		}
		 return downEvlTableLis;
	}

	/**
	 * 평가표 조회
	 * @param param
	 * @return
	 */
	public EvlTableDto getEvlTable(EvlTableParam param) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(param.getEvlCmitId());
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}

		if(CoreUtils.string.isBlank(param.getEvlTrgetId())) {
			throw new InvalidationException("평가대상ID를 입력해주세요.");
		}
		if(CoreUtils.string.isBlank(param.getEvlMfcmmId())) {
			throw new InvalidationException("평가위원ID를 입력해주세요.");
		}
		//평가위원info 조회
		UsptEvlMfcmm evlMfcmm = new UsptEvlMfcmm();
		evlMfcmm.setEvlMfcmmId(param.getEvlMfcmmId());
		evlMfcmm = usptEvlMfcmmDao.select(evlMfcmm);
		UsptEvlTrget usptEvlTrget = usptEvlTrgetDao.select(param.getEvlTrgetId());
		UsptBsnsPblancApplcnt applcnt = usptBsnsPblancApplcntDao.select(usptEvlTrget.getApplyId());

		EvlTableDto evltableDto = new EvlTableDto();
		evltableDto.setMemberNm(applcnt.getMemberNm());
		evltableDto.setReceiptNo(applcnt.getReceiptNo());
		evltableDto.setEvlMfcmmNm(evlMfcmm.getEvlMfcmmNm());
		//평가점수 조회....미평가는 0점
		List<EvlTableListDto> list = usptEvlCmitDao.selectEvlSystemTableList(param);
		//배점변환
		for(EvlTableListDto scoreParam : list) {
			scoreParam.setEvlScore(this.reEvlScore(scoreParam.getEvlScore()));
		}
		evltableDto.setList(list);
		if(list != null && list.size() > 0) {
			evltableDto.setEvlResultId(list.get(0).getEvlResultId());	/** 평가결과ID */
			evltableDto.setEvlMthdCd(list.get(0).getEvlMthdCd());	/* 평가방식코드(G:EVL_MTHD) */
			evltableDto.setEvlOpinion(list.get(0).getEvlOpinion());
		}

		return evltableDto;
	}

	/**
	 * 평가표 등록
	 * @param evlResultId, evlTrgetId, evlOpinion
	 * @return
	 */
	public void modifyEvlTable(EvlOfflineTableDto evlOfflineTableDto){
		SecurityUtils.checkWorkerIsInsider();
		/** 현재 시간*/
		Date now = new Date();
		String evlMfcmmId	=evlOfflineTableDto.getEvlMfcmmId();	/** 평가위원ID*/
		String evlResultId 	= evlOfflineTableDto.getEvlResultId();	/** 평가결과ID */
		String evlTrgetId 	= evlOfflineTableDto.getEvlTrgetId();	/**평가대상ID*/
		String evlOpinion 	= evlOfflineTableDto.getEvlOpinion();	/** 평가의견 */
		String evlMthdCd 	= evlOfflineTableDto.getEvlMthdCd();	/** 평가방식코드(G:EVL_MTHD) */
		UsptEvlMfcmmResult usptEvlMfcmmResult = new UsptEvlMfcmmResult();

		//평가위원 기본정보 조회
		UsptEvlMfcmm usptEvlMfcmm = new UsptEvlMfcmm();
		usptEvlMfcmm 	= 	usptEvlMfcmmDao.selectEvlMfcmmIdInfo(evlMfcmmId);
		String evlCmitId = usptEvlMfcmm.getEvlCmitId();								/** 평가위원회ID */
		String evlPlanId  = usptEvlPlanDao.selectEvlPlanIdByEvlCmitId(evlCmitId);	/** 평가계획ID */

		//평가결과(평가위원별) 등록
		//evlResultId evlTrgetId evlOpinion
		if(string.isBlank(evlResultId)){
			evlResultId =CoreUtils.string.getNewId(Code.prefix.평가결과);
			usptEvlMfcmmResult.setEvlResultId(evlResultId);
			usptEvlMfcmmResult.setEvlMfcmmId(evlMfcmmId);
			usptEvlMfcmmResult.setEvlTrgetId(evlTrgetId);
			usptEvlMfcmmResult.setMfcmmEvlSttusCd(Code.mfcmmEvlSttus.평가완료);
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
			usptEvlMfcmmResult.setMfcmmEvlSttusCd(Code.mfcmmEvlSttus.평가완료);
			usptEvlMfcmmResult.setMfcmmEvlSttusDt(now);
			usptEvlMfcmmResult.setEvlOpinion(evlOfflineTableDto.getEvlOpinion());
			usptEvlMfcmmResult.setUpdatedDt(now);
			usptEvlMfcmmResult.setUpdaterId(evlMfcmmId);
			usptEvlMfcmmResultDao.update(usptEvlMfcmmResult);
		}

		//평가항목결과 등록
		//evlIemResultId, evlIemId, evlScore, evlCn
		List<UsptEvlIemResult> usptEvlIemResultList = evlOfflineTableDto.getUsptEvlIemResultList();
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
	 * 위원장의견 조회
	 * @param evlCmitId
	 * @return
	 */
	public EvlCharmnOpinionDto getEvlCharmnOpinion(String evlCmitId) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}

		UsptEvlCmit evlCmit = usptEvlCmitDao.select(evlCmitId);
		EvlCharmnOpinionDto ecoDto = new EvlCharmnOpinionDto();
		ecoDto.setEvlCharmnOpinionCn(evlCmit.getEvlCharmnOpinionCn());
		return ecoDto;
	}


	/**
	 * 위원장 종합의견 등록
	 * @param evlCmitId ,evlCharmnOpinionCn
	 * @return
	 */
	public void modifyCharmnOpinionCn(String evlCmitId, EvlCharmnOpinionDto evlCharmnOpinionDto){
		BnMember worker =SecurityUtils.checkWorkerIsInsider();

		/** 현재 시간*/
		Date now = new Date();
		String memberId   =worker.getMemberId();			/** 평가위원ID*/
		String evlCharmnOpinionCn = evlCharmnOpinionDto.getEvlCharmnOpinionCn();	/** 평가위원장종합의견내용 */

		//평가위원회 변경
		UsptEvlCmit usptEvlCmit = new UsptEvlCmit();
		usptEvlCmit.setEvlCmitId(evlCmitId);
		usptEvlCmit.setEvlCharmnOpinionCn(evlCharmnOpinionCn);
		usptEvlCmit.setUpdatedDt(now);
		usptEvlCmit.setUpdaterId(memberId);
		usptEvlCmitDao.updateCharmOpinion(usptEvlCmit);
	}

	/**
	 * 평가결과 조회
	 * @param evlCmitId
	 * @return
	 */
	public EvlResultDto getEvlResult(String evlCmitId) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}

		EvlResultDto resultDto = new EvlResultDto();
		resultDto.setResultList(usptEvlCmitDao.selectEvlResultList(evlCmitId, null));

		UsptEvlCmit evlCmit = usptEvlCmitDao.select(evlCmitId);
		if(!CoreUtils.string.isBlank(evlCmit.getAttachmentGroupId())) {
			resultDto.setAttachmentList(attachmentService.getFileInfos_group(evlCmit.getAttachmentGroupId()));
		}

		return resultDto;
	}


	/**
	 * 선정/탈락 처리
	 * @param evlCmitId
	 * @param evlTrgetList
	 * @param slctn
	 */
	public void modifyEvlSlctn(String evlCmitId, List<EvlTrgetIdParam> evlTrgetList, Boolean slctn) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(dto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}
		if(CoreUtils.string.equals(dto.getEvlSttusCd(), Code.evlSttus.평가완료)) {
			throw new InvalidationException("이미 평가완료 처리가 되었습니다. \n처리할 수 없습니다. ");
		}

		if(evlTrgetList == null || evlTrgetList.size() == 0) {
			throw new InvalidationException("평가대상 목록을 입력해주세요.");
		}
		Date now = new Date();
		UsptEvlTrget evlTrget = new UsptEvlTrget();
		evlTrget.setSlctn(slctn);
		evlTrget.setSlctnDt(now);
		evlTrget.setUpdatedDt(now);
		evlTrget.setUpdaterId(worker.getMemberId());
		for(EvlTrgetIdParam evlTrgetIdInfo : evlTrgetList) {
			evlTrget.setEvlTrgetId(evlTrgetIdInfo.getEvlTrgetId());
			if(usptEvlTrgetDao.updateSlctn(evlTrget) != 1) {
				throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			}
		}
	}


	/**
	 * 자동선정 처리
	 * @param evlCmitId
	 */
	public void modifyAutoEvlSlctn(String evlCmitId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(dto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}
		if(CoreUtils.string.equals(dto.getEvlSttusCd(), Code.evlSttus.평가완료)) {
			throw new InvalidationException("이미 평가완료 처리가 되었습니다. \n처리할 수 없습니다. ");
		}

		List<EvlResultListDto> resultList = usptEvlCmitDao.selectEvlResultList(evlCmitId, null);
		if(resultList == null || resultList.size() == 0) {
			throw new InvalidationException("평가결과 대상이 없습니다.");
		}

		UsptEvlStep evlStep = usptEvlStepDao.select(dto.getEvlStepId());

		Date now = new Date();
		UsptEvlTrget evlTrget = new UsptEvlTrget();
		evlTrget.setSlctnDt(now);
		evlTrget.setUpdatedDt(now);
		evlTrget.setUpdaterId(worker.getMemberId());
		for(int idx=0; idx<resultList.size(); idx++) {
			EvlResultListDto evlResult = resultList.get(idx);
			if(evlStep.getSlctnTargetCo() > idx) {
				evlTrget.setSlctn(true);
			} else {
				evlTrget.setSlctn(false);
			}
			evlTrget.setEvlTrgetId(evlResult.getEvlTrgetId());
			if(usptEvlTrgetDao.updateSlctn(evlTrget) != 1) {
				throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			}
		}
	}


	/**
	 * 평가완료 처리
	 * @param evlCmitId
	 */
	public void modifyEvlCompt(String evlCmitId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(dto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}

		UsptEvlCmit evlCmit = usptEvlCmitDao.select(evlCmitId);
		if(CoreUtils.string.equals(evlCmit.getEvlSttusCd(), Code.evlSttus.평가완료)) {
			throw new InvalidationException("이미 평가완료 처리가 되었습니다. \n중복으로 처리할 수 없습니다. ");
		}

		Date now = new Date();
		/* 1. 평가위원회 - 평가진행상태코드(G:EVL_STTUS) - 평가완료 처리 */
		evlCmit.setUpdatedDt(now);
		evlCmit.setUpdaterId(worker.getMemberId());
		evlCmit.setEvlSttusDt(now);
		evlCmit.setEvlSttusCd(Code.evlSttus.평가완료);
		if(usptEvlCmitDao.updateEvlSttus(evlCmit) != 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}

		/* 2. 평가단계 - 단계별평가진행상태코드(G:EVL_STEP_STTUS) - 평가완료 처리 */
		UsptEvlStep evlStep = usptEvlStepDao.select(evlCmit.getEvlStepId());
		evlStep.setUpdatedDt(now);
		evlStep.setUpdaterId(worker.getMemberId());
		evlStep.setEvlStepSttusCd(Code.evlStepSttus.평가완료);
		if(usptEvlStepDao.update(evlStep) != 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}

		List<UsptEvlStep> evlStepList = usptEvlStepDao.selectList(evlStep.getEvlPlanId(), null);
		if(evlStepList == null || evlStepList.isEmpty()) {
			throw new InvalidationException("평가단계 정보가 없습니다.");
		}


		List<SlctnListDto> evlTrgetList = usptEvlTrgetDao.selectSlctnList(evlCmit.getEvlStepId(), evlCmit.getSectId(), true);
		if(evlTrgetList == null || evlTrgetList.isEmpty()) {
			throw new InvalidationException("선정된 대상이 없습니다.");
		}

		/* 다음 평가단계의 평가대상자 설정 */
		if(evlStepList.size() == evlStep.getSortOrdrNo()) {
			/* 평가단계가 마지막이었을경우 상태값만 변경 후 완료 (선정관리에서 처리함) */
		} else if(evlStepList.size() < evlStep.getSortOrdrNo()) {
			throw new InvalidationException("평가단계 정렬순서를 확인해주세요.");
		} else {
			/* 평가단계가 마지막이 아닌경우 다음단계의 평가단계에 선정된 대상자를 평가대상에 insert 처리 */
			UsptEvlStep nextEvlStep = evlStepList.get(evlStep.getSortOrdrNo());
			UsptEvlTrget regEvlTrget = new UsptEvlTrget();
			regEvlTrget.setCreatedDt(now);
			regEvlTrget.setCreatorId(worker.getMemberId());
			regEvlTrget.setUpdatedDt(now);
			regEvlTrget.setUpdaterId(worker.getMemberId());
			regEvlTrget.setEvlStepId(nextEvlStep.getEvlStepId());
			regEvlTrget.setSectId(evlCmit.getSectId());

			for(SlctnListDto slctnEvlTrget : evlTrgetList) {
				regEvlTrget.setApplyId(slctnEvlTrget.getApplyId());
				regEvlTrget.setEvlTrgetId(Code.prefix.평가대상);
				usptEvlTrgetDao.insert(regEvlTrget);
			}
		}
	}

	/**
	 * 평가결과취소 처리
	 * @param evlCmitId
	 */
	public void modifyEvlComptInit(String evlCmitId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String memberId = worker.getMemberId();
		Date now = new Date();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}

		String evlTrgetId ="";			/** 평가대상ID */
		String evlMfcmmId ="";		/** 평가위원ID */

		EvlTableParam inputParam = null;		//평가표 input조회
		List<EvlSttusMfcmmListDto> evlMfcmmList =  new ArrayList<>();	//평가위원 목록
		List<EvlTableListDto> list =  new ArrayList<>();						//평가항목결과

		//평가위원회 변경
		UsptEvlCmit usptEvlCmit = new UsptEvlCmit();
		usptEvlCmit.setEvlCmitId(evlCmitId);
		usptEvlCmit.setUpdatedDt(now);
		usptEvlCmit.setUpdaterId(memberId);
		usptEvlCmit.setEvlSttusDt(now);
		usptEvlCmit.setEvlSttusCd(Code.evlSttus.대기중);
		if(usptEvlCmitDao.updateEvlSttus(usptEvlCmit) != 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}

		//평가현황 대상조회
		List<EvlSttusListDto> selectTargetList =  usptEvlCmitDao.selectEvlSttusList(evlCmitId);
		for( EvlSttusListDto target : selectTargetList) {
			//평가대상ID
			evlTrgetId = target.getEvlTrgetId();
			//평가위원 목록
			evlMfcmmList= target.getEvlSttusMfcmmList();

			for(EvlSttusMfcmmListDto evlMfcmm : evlMfcmmList ) {
				evlMfcmmId = evlMfcmm.getEvlMfcmmId();		/** 평가위원ID */
				//평가결과
				UsptEvlMfcmmResult usptEvlMfcmmResult = new UsptEvlMfcmmResult();
				usptEvlMfcmmResult.setEvlMfcmmId(evlMfcmmId);
				usptEvlMfcmmResult.setMfcmmEvlSttusCd(Code.mfcmmEvlSttus.대기중);
				usptEvlMfcmmResult.setMfcmmEvlSttusDt(now);
				usptEvlMfcmmResult.setUpdatedDt(now);
				usptEvlMfcmmResult.setUpdaterId(memberId);
				usptEvlMfcmmResultDao.updateMfcmmEvlSttusAll(usptEvlMfcmmResult);

				//평가항목결과 삭제
				inputParam = new EvlTableParam();
				inputParam.setEvlCmitId(evlCmitId);
				inputParam.setEvlMfcmmId(evlMfcmmId);
				inputParam.setEvlTrgetId(evlTrgetId);
				list = usptEvlCmitDao.selectEvlTableList(inputParam);
				for(EvlTableListDto evlIemResult : list ) {
					usptEvlIemResultDao.delete(evlIemResult.getEvlIemResultId());
				}
			}
		}
	}


	/**
	 * 첨부파일 일괄다운로드
	 * @param response
	 * @param evlCmitId
	 */
	public void downloaAllAtchmnfl(HttpServletResponse response, String evlCmitId) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		UsptEvlCmit evlCmit = usptEvlCmitDao.select(evlCmitId);
		if(CoreUtils.string.isBlank(evlCmit.getAttachmentGroupId())) {
			throw new InvalidationException("다운로드 가능한 첨부파일이 없습니다.");
		}
		attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), evlCmit.getAttachmentGroupId(), "온라인평가_첨부파일");
	}


	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param evlCmitId
	 * @param info
	 */
	public void downloadAtchmnfl(HttpServletResponse response, String evlCmitId, CmmtAtchmnfl info) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(CoreUtils.string.isBlank(info.getAttachmentId())) {
			throw new InvalidationException("첨부파일ID를 입력해주세요");
		}
		attachmentService.downloadFile(response, config.getDir().getUpload(), info.getAttachmentId());
	}


	/**
	 * 첨부파일 삭제
	 * @param evlCmitId
	 * @param deleteList
	 */
	public void delAtchmnfl(String evlCmitId, List<CmmtAtchmnfl> deleteList) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(deleteList == null || deleteList.isEmpty()) {
			throw new InvalidationException("삭제할 첨부파일ID를 입력해주세요");
		}

		for(CmmtAtchmnfl info : deleteList) {
			attachmentService.removeFile_keepEmptyGroup(config.getDir().getUpload(), info.getAttachmentId());
		}
	}


	/**
	 * 첨부파일 저장
	 * @param evlCmitId
	 * @param file
	 */
	public void addAtchmnfl(String evlCmitId, MultipartFile file) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}

//		String[] exts = {"jpg", "jpeg", "png", "gif"};

		UsptEvlCmit evlCmit = usptEvlCmitDao.select(evlCmitId);
		if(CoreUtils.string.isBlank(evlCmit.getAttachmentGroupId())) {
			CmmtAtchmnfl attachmentInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), file, null, 0);
			evlCmit.setAttachmentGroupId(attachmentInfo.getAttachmentGroupId());
			if(usptEvlCmitDao.update(evlCmit) != 1) {
				throw new InvalidationException(String.format(Code.validateMessage.DB오류, "첨부파일 등록"));
			}
		} else {
			attachmentService.uploadFile_toGroup(config.getDir().getUpload(), evlCmit.getAttachmentGroupId(), file, null, 0);
		}
	}



	/**
	 * 발송
	 * @param evlCmitId
	 * @param param
	 * @return
	 */
	public DspthResultDto modifyEvlResultDspth(String evlCmitId, EvlResultDspthParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}
		if(!CoreUtils.string.equals(dto.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("처리 권한이 없습니다.");
		}
		if(CoreUtils.string.isBlank(param.getSndngTrget())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "발송대상"));
		}
		if(CoreUtils.string.isBlank(param.getSndngMth())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "발송방법"));
		}
		if(CoreUtils.string.isBlank(param.getSndngCn())) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "발송내용"));
		}

		UsptEvlCmit evlCmit = usptEvlCmitDao.select(evlCmitId);
		if(!CoreUtils.string.equals(evlCmit.getEvlSttusCd(), Code.evlSttus.평가완료)) {
			throw new InvalidationException("평가완료 처리 후 발송이 가능합니다.");
		}

		List<SlctnListDto> evlTrgetList = null;
		if(CoreUtils.string.equalsIgnoreCase("SLCTN", param.getSndngTrget())) {
			evlTrgetList = usptEvlTrgetDao.selectSlctnList(evlCmit.getEvlStepId(), evlCmit.getSectId(), true);
		} else if(CoreUtils.string.equalsIgnoreCase("PTRMSN", param.getSndngTrget())) {
			evlTrgetList = usptEvlTrgetDao.selectSlctnList(evlCmit.getEvlStepId(), evlCmit.getSectId(), false);
		} else {
			throw new InvalidationException(String.format(Code.validateMessage.유효오류, "발송대상"));
		}


		DspthResultDto result = new DspthResultDto();
		/* 이메일 / SMS 전송 Start -------------------------------------------------------------------*/
		String emailCn = CoreUtils.file.readFileString("/form/email/email-common.html");
		SmsSendParam smsParam = new SmsSendParam();
		EmailSendParam esParam = new EmailSendParam();
		esParam.setContentHtml(true);
		esParam.setEmailCn(emailCn);
		esParam.setTitle("AICA 안내문");

		StringBuffer sbMsg = new StringBuffer();
		for(SlctnListDto info : evlTrgetList) {
			esParam.setContentHtml(true);
			if(param.getSndngMth().contains(Code.sndngMth.이메일)) {
				if(CoreUtils.string.isNotEmpty(info.getEmail())){
					Map<String, String> templateParam = new HashMap<>();
					templateParam.put("memNm", info.getMemberNm());
					templateParam.put("cnVal", param.getSndngCn());
					esParam.addRecipient(info.getEmail(), info.getMemberNm(), templateParam);
				} else {
					sbMsg.append("[" + info.getMemberNm() +"] 이메일 정보가 없습니다.\n");
					log.debug("[" + info.getMemberNm() +"] 이메일 정보가 없습니다.");
				}
			}

			if(param.getSndngMth().contains(Code.sndngMth.SMS)){
				if(CoreUtils.string.isNotEmpty(info.getMobileNo())){
					smsParam.setSmsCn("AICA 안내문\n " + param.getSndngCn());
					smsParam.addRecipient(info.getMobileNo());
				} else {
					sbMsg.append("[" + info.getMemberNm() +"] 핸드폰번호 정보가 없습니다.\n");
					log.debug("[" + info.getMemberNm() +"] 핸드폰번호 정보가 없습니다.");
				}
			}
		}

		result.setTotalCnt(evlTrgetList.size());
		EmailResult er = null;
		SmsResult sr = null;
		if(param.getSndngMth().contains(Code.sndngMth.이메일) && esParam.getRecipientList() != null && esParam.getRecipientList().size() > 0) {
			er = emailUtils.send(esParam);
			if(er != null) {
				result.setEmailTotalCnt(er.getTotalCnt());
				result.setEmailFailCnt(er.getFailCnt());
				result.setEmailSuccessCnt(er.getSuccessCnt());
			}
		}
		if(param.getSndngMth().contains(Code.sndngMth.SMS) && smsParam.getRecipientList() != null && smsParam.getRecipientList().size() > 0){
			sr = smsUtils.send(smsParam);
			if(sr != null) {
				result.setSmsTotalCnt(sr.getTotalCnt());
				result.setSmsFailCnt(sr.getFailCnt());
				result.setSmsSuccessCnt(sr.getSuccessCnt());
			}
		}
		/* 이메일 / SMS 전송 End -------------------------------------------------------------------*/

		Date now = new Date();
		evlCmit.setUpdatedDt(now);
		evlCmit.setUpdaterId(worker.getMemberId());

		String sndngMth = "";
		if(param.getSndngMth().contains(Code.sndngMth.이메일) && param.getSndngMth().contains(Code.sndngMth.SMS)) {
			sndngMth = Code.sndngMth.ALL;
		} else {
			sndngMth = param.getSndngMth();
		}

		if(CoreUtils.string.equalsIgnoreCase("SLCTN", param.getSndngTrget())) {
			evlCmit.setPtrmsnDspth(null);
			evlCmit.setSlctnDspthDt(now);
			evlCmit.setSlctnSndngMth(sndngMth);
			evlCmit.setSlctnSndngCn(param.getSndngCn());

			if(er != null && er.getResultCode() != -1) {
				evlCmit.setSlctnEmailId(er.getEmailId());
				evlCmit.setSlctnDspth(true);
			} else {
				evlCmit.setSlctnEmailId(null);
				evlCmit.setSlctnDspth(false);
			}

			if(sr != null && sr.getResultCode() != -1) {
				evlCmit.setSlctnSmsId(sr.getSmsId());
				evlCmit.setSlctnDspth(true);
			} else {
				evlCmit.setSlctnSmsId(null);
				evlCmit.setSlctnDspth(false);
			}
		} else if(CoreUtils.string.equalsIgnoreCase("PTRMSN", param.getSndngTrget())) {
			evlCmit.setSlctnDspth(null);
			evlCmit.setPtrmsnDspthDt(now);
			evlCmit.setPtrmsnSndngMth(sndngMth);
			evlCmit.setPtrmsnSndngCn(param.getSndngCn());

			if(er != null && er.getResultCode() != -1) {
				evlCmit.setPtrmsnEmailId(er.getEmailId());
				evlCmit.setPtrmsnDspth(true);
			} else {
				evlCmit.setPtrmsnEmailId(null);
				evlCmit.setPtrmsnDspth(false);
			}

			if(sr != null && sr.getResultCode() != -1) {
				evlCmit.setPtrmsnSmsId(sr.getSmsId());
				evlCmit.setPtrmsnDspth(true);
			} else {
				evlCmit.setPtrmsnSmsId(null);
				evlCmit.setPtrmsnDspth(false);
			}
		}

		if(usptEvlCmitDao.updateDspth(evlCmit) != 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "발송정보 수정"));
		}

		for(SlctnListDto info : evlTrgetList) {
			if(er != null) {
				List<EmailReceiverResult> erList = er.getReceiverList();
				Optional<EmailReceiverResult> opt = erList.stream().filter(x->x.getReceiveMailAddr().equals(info.getEmail())).findFirst();
				if(opt.isPresent()) {
					EmailReceiverResult receiverResult = opt.get();
					if(receiverResult.getIsSuccessful()) {
					} else {
						sbMsg.append("[" + info.getMemberNm() +"] 이메일 발송이 실패하였습니다.\n");
						log.debug("[" + info.getMemberNm() +"] 이메일 발송이 실패하였습니다.");
					}
				}
			}
			if(sr != null) {
				List<SmsRecipientResult> srList = sr.getRecipientList();
				Optional<SmsRecipientResult> opt = srList.stream().filter(x->x.getRecipientNo().equals(info.getMobileNo())).findFirst();
				if(opt.isPresent()) {
					SmsRecipientResult receiverResult = opt.get();
					if(receiverResult.getIsSuccessful()) {
					} else {
						sbMsg.append("[" + info.getMemberNm() +"] SMS 발송이 실패하였습니다.\n");
						log.debug("[" + info.getMemberNm() +"] SMS 발송이 실패하였습니다.");
					}
				}
			}
		}
		result.setResultMessage(sbMsg.toString());
		return result;
	}


	/**
	 * 종합평가 조회
	 * @param evlCmitId
	 * @param evlTrgetId
	 * @return
	 */
	public GnrlzEvlDto getGnrlzEvl(String evlCmitId, String evlTrgetId) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}

		UsptEvlTrget usptEvlTrget = usptEvlTrgetDao.select(evlTrgetId);
		UsptBsnsPblancApplcnt applcnt = usptBsnsPblancApplcntDao.select(usptEvlTrget.getApplyId());

		GnrlzEvlDto gnrlzEvlDto = new GnrlzEvlDto();
		gnrlzEvlDto.setMemberNm(applcnt.getMemberNm());
		gnrlzEvlDto.setReceiptNo(applcnt.getReceiptNo());
		gnrlzEvlDto.setEvlIemList(usptEvlCmitDao.selectGnrlzEvlIemList(evlCmitId, evlTrgetId));
		gnrlzEvlDto.setEvlOpinionList(usptEvlCmitDao.selectGnrlzEvlOpinionList(evlCmitId, evlTrgetId));
		return gnrlzEvlDto;
	}


	/**
	 * 평가결과 동의서 조회
	 * @param evlCmitId
	 * @return
	 */
	public EvlResultConsentFormDto getEvlResultConsentForm(String evlCmitId) {
		SecurityUtils.checkWorkerIsInsider();
		EvlTargetListDto dto = this.getEvlBasicInfo(evlCmitId);
		if(dto == null) {
			throw new InvalidationException("요청하신 정보에 해당되는 평가위원회 정보가 없습니다.");
		}

		UsptEvlCmit evlCmit = usptEvlCmitDao.select(evlCmitId);
		EvlResultConsentFormDto cfDto = new EvlResultConsentFormDto();
		cfDto.setBsnsNm(dto.getBsnsNm());
		cfDto.setPblancNm(dto.getPblancNm());
		cfDto.setEvlStepNm(dto.getEvlStepNm());
		cfDto.setSectNm(dto.getSectNm());
		cfDto.setEvlPrarnde(dto.getEvlPrarnde());
		cfDto.setEvlPlace(dto.getEvlPlace());
		cfDto.setChrgDeptNm(dto.getChrgDeptNm());
		cfDto.setOrgnzrNm(dto.getOrgnzrNm());
		cfDto.setEvlCharmnOpinionCn(evlCmit.getEvlCharmnOpinionCn());
		cfDto.setSlctnResultList(usptEvlCmitDao.selectEvlResultList(evlCmitId, "Y"));
		cfDto.setEvlMfcmmList(usptEvlMfcmmDao.selectEvlAtendList(evlCmitId));
		return cfDto;
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
