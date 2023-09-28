package aicluster.pms.api.evl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.notification.SmsUtils;
import aicluster.framework.notification.dto.SmsSendParam;
import aicluster.framework.notification.nhn.sms.SmsResult;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.evl.dto.EvlCmitExtrcTargetParam;
import aicluster.pms.api.expert.dto.ExpertListParam;
import aicluster.pms.common.dao.UsptCnsltHistDao;
import aicluster.pms.common.dao.UsptEvlCmitDao;
import aicluster.pms.common.dao.UsptEvlMfcmmDao;
import aicluster.pms.common.dao.UsptEvlMfcmmExtrcDao;
import aicluster.pms.common.dao.UsptEvlMfcmmResultDao;
import aicluster.pms.common.dao.UsptExclMfcmmDao;
import aicluster.pms.common.dao.UsptExpertDao;
import aicluster.pms.common.dao.UsptExtrcMfcmmDao;
import aicluster.pms.common.dao.UsptUdstdprcpDao;
import aicluster.pms.common.dto.EvlCmitExtrcExcelResultDto;
import aicluster.pms.common.dto.EvlCmitExtrcResultDto;
import aicluster.pms.common.dto.EvlLoginCmitListDto;
import aicluster.pms.common.entity.UsptCnsltHist;
import aicluster.pms.common.entity.UsptEvlCmit;
import aicluster.pms.common.entity.UsptEvlCmitExtrcTarget;
import aicluster.pms.common.entity.UsptEvlMfcmm;
import aicluster.pms.common.entity.UsptEvlMfcmmExtrc;
import aicluster.pms.common.entity.UsptEvlMfcmmResult;
import aicluster.pms.common.entity.UsptExclMfcmm;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.entity.UsptExpertExcel;
import aicluster.pms.common.entity.UsptExpertResult;
import aicluster.pms.common.entity.UsptExtrcMfcmm;
import aicluster.pms.common.entity.UsptUdstdprcp;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.exception.LoggableException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EvlCmitService {

	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private UsptEvlCmitDao usptEvlCmitDao;

	@Autowired
	private UsptEvlMfcmmExtrcDao usptEvlMfcmmExtrcDao;

	@Autowired
	private UsptExpertDao usptExpertDao;

	@Autowired
	private UsptExtrcMfcmmDao usptExtrcMfcmmDao;

	@Autowired
	private UsptExclMfcmmDao usptExclMfcmmDao;

	@Autowired
	private UsptCnsltHistDao usptCnsltHistDao;

	@Autowired
	private UsptEvlMfcmmDao usptEvlMfcmmDao;

	@Autowired
	private UsptEvlMfcmmResultDao usptEvlMfcmmResultDao;

	@Autowired
	private UsptUdstdprcpDao usptUdstdprcpDao;

	@Autowired
	private SmsUtils smsUtils;

	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

    /**
     * 평가위원회 추출 대상 목록조회
     *
     * @param usptEvlCmitExtrcTarget
     * @param page
     * @return
     */
	public CorePagination<UsptEvlCmitExtrcTarget> getEvlCmitExtrcTargetList(EvlCmitExtrcTargetParam evlCmitExtrcTargetParam, Long page) {

		if(page == null) {
			page = 1L;
		}

		if(evlCmitExtrcTargetParam.getItemsPerPage() == null) {
			evlCmitExtrcTargetParam.setItemsPerPage(ITEMS_PER_PAGE);
		}

		long totalItems = usptEvlCmitDao.selectEvlCmitExtrcTargetListCount(evlCmitExtrcTargetParam);

		evlCmitExtrcTargetParam.setExcel(false);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, evlCmitExtrcTargetParam.getItemsPerPage(), totalItems);

		evlCmitExtrcTargetParam.setBeginRowNum(info.getBeginRowNum());
		List<UsptEvlCmitExtrcTarget> list = usptEvlCmitDao.selectEvlCmitExtrcTargetList(evlCmitExtrcTargetParam);

		//출력할 자료 생성 후 리턴
		CorePagination<UsptEvlCmitExtrcTarget> pagination = new CorePagination<>(info, list);

		return pagination;
	}


	//평가위원회 추출 대상 목록 엑셀다운로드
	public List<UsptEvlCmitExtrcTarget> getEvlCmitExtrcTargetListExcel(EvlCmitExtrcTargetParam evlCmitExtrcTargetParam) {
		evlCmitExtrcTargetParam.setExcel(true);
		return usptEvlCmitDao.selectEvlCmitExtrcTargetList(evlCmitExtrcTargetParam);
	}


	//평가위원 추출 상세조회
	public UsptEvlCmitExtrcTarget get(String evlCmitId) {
		UsptEvlCmitExtrcTarget resultData = usptEvlCmitDao.selectEvlCmitExtrcTarget(evlCmitId);

		if (resultData == null) {
			throw new InvalidationException("위원회 정보가 없습니다.");
		}

		return resultData;
	}


	//평가 위원 추출
	public UsptEvlMfcmmExtrc add(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException exs = new InvalidationsException();

		//유효체크
		UsptEvlCmitExtrcTarget resultData = usptEvlCmitDao.selectEvlCmitExtrcTarget(usptEvlMfcmmExtrc.getEvlCmitId());

		if (resultData == null) {
			throw new InvalidationException("위원회 정보가 없습니다.");
		}

		if (usptEvlMfcmmExtrc.getExtrcMultiple() == null || usptEvlMfcmmExtrc.getExtrcMultiple() < 1) {
			exs.add("extrcMultiple", "추출 배수를 입력해주세요.");
		}

		if (usptEvlMfcmmExtrc.getExpertClId().length < 1) {
			throw new InvalidationException("전문분야를 선택해주세요.");
		}

		if (exs.size() > 0) {
			throw exs;
		}

		//추출 처리
		String newUsptEvlMfcmmExtrc = string.getNewId("evlmfcmmextrc-"); //평가위원추출 테이블 key

		usptEvlMfcmmExtrc.setMfcmmExtrcId(newUsptEvlMfcmmExtrc);
		usptEvlMfcmmExtrc.setCreatedDt(now);
		usptEvlMfcmmExtrc.setCreatorId(worker.getMemberId());

		//1.추출 기본정보 등록
		usptEvlMfcmmExtrcDao.insert(usptEvlMfcmmExtrc);

		//2.추출 위원 등록
		//제외할 위원 외에 전문가 테이블에서 임의로 추출(배수 참고하여)
		//추출할 전문가 정보 list
		//이해관계자 제외, 참여제한 제외, 추가배제조건 제외 하여 추출 대상 로직 추가, 전문가 분류(전문분야) join  필요 -> 완료

		//현재 차수 체크
		//usptEvlMfcmmExtrcDao.selectMaxOdrNo(newUsptEvlMfcmmExtrc);

		//참여제한 체크
		String[] exclCnd = usptEvlMfcmmExtrc.getAditExclCndCd();
		for (String s : exclCnd) {
			if(string.equals(s, Code.aditExclCnd.조건1)){
				usptEvlMfcmmExtrc.setExclCnd1("Y");
			}

			if(string.equals(s, Code.aditExclCnd.조건2)){
				usptEvlMfcmmExtrc.setExclCnd2("Y");
			}

			if(string.equals(s, Code.aditExclCnd.조건3)){
				usptEvlMfcmmExtrc.setExclCnd3("Y");
			}

			if(string.equals(s, Code.aditExclCnd.조건4)){
				usptEvlMfcmmExtrc.setExclCnd4("Y");
			}
		}

		String[] lmttCndCd = usptEvlMfcmmExtrc.getPartcptnLmttCndCd();

		for (String s : lmttCndCd) {
			if(string.equals(s, Code.partcptnLmttCnd.조건1)){
				usptEvlMfcmmExtrc.setLmttCndCd1("Y");
				//주관기관 참여기관에 해당하는 전문가 추출
				//주관기관명 및 참여기관명 추출
				List<UsptExpert> wrcNmList = usptExpertDao.selectInsttNmList(usptEvlMfcmmExtrc.getEvlCmitId());
				String insttExpertListAll = "";
				for(UsptExpert wrcNminfo : wrcNmList) {
					List<UsptExpert> insttExpertList = usptExpertDao.selectExpertListByInsttNm(wrcNminfo.getWrcNm());

					for(UsptExpert insttExpertInfo : insttExpertList) {
						insttExpertListAll = insttExpertListAll + insttExpertInfo.getExpertId() + ",";
					}
				}

				String[] insttExpertListAllArr = insttExpertListAll.split(",");

				if(insttExpertListAllArr.length > 0) {
					usptEvlMfcmmExtrc.setInsttExpertId(insttExpertListAllArr);//주관기관 참여기관 관련 전문가 세팅
				}
			}

			if(string.equals(s, Code.partcptnLmttCnd.조건2)){
				usptEvlMfcmmExtrc.setLmttCndCd2("Y");
			}

			if(string.equals(s, Code.partcptnLmttCnd.조건3)){
				usptEvlMfcmmExtrc.setLmttCndCd3("Y");
			}
		}

		String evlPlanId = usptExpertDao.selectEvlPlanIdByCmitId(usptEvlMfcmmExtrc.getEvlCmitId());

		String intprtsExpertId = "";

		//이해관계자 목록
		List<UsptUdstdprcp> intprtsExpertList = usptUdstdprcpDao.selectList(evlPlanId, null);

		//전문가 목록
		ExpertListParam expertListParam = new ExpertListParam();
		expertListParam.setExpertReqstProcessSttusCd("ERPS02");	//신청 승인 완료건만

		List<UsptExpert> expertlist = usptExpertDao.selectExpertList2(expertListParam);

		for(UsptUdstdprcp inf1 : intprtsExpertList) {
			String nm = inf1.getNm();
			String birth = string.getNumberOnly(inf1.getBrthdy());
			String tel = string.getNumberOnly(inf1.getMbtlnum());
			log.debug("#####	nm : {}", nm);
			log.debug("#####	birth : {}", birth);
			log.debug("#####	tel : {}", tel);

			for(UsptExpert inf2 : expertlist) {
				String nm2 = inf2.getExpertNm();
				String birth2 = string.getNumberOnly(inf2.getBrthdy());
				String tel2 = string.getNumberOnly(inf2.getMbtlnum());

				log.debug("#####	nm2 : {}", nm2);
				log.debug("#####	birth2 : {}", birth2);
				log.debug("#####	tel2 : {}", tel2);

				if(string.equals(nm, nm2) && string.equals(birth, birth2) && string.equals(tel, tel2) ) {
					intprtsExpertId = intprtsExpertId + inf2.getExpertId() + ",";
				}
			}
		}

		String[] intprtsExpertArr = intprtsExpertId.split(",");

		if(intprtsExpertArr.length > 0) {
			usptEvlMfcmmExtrc.setIntprtsExpertId(intprtsExpertArr);//이해관계자 세팅
		}

		List<UsptExpert> expertTargetList = usptExpertDao.selectExpertTargetList(usptEvlMfcmmExtrc);


		if(expertTargetList.size() < 1) {
			throw new InvalidationException("추출된 평가위원이 없습니다.");
		}

		List<UsptExtrcMfcmm> list = new ArrayList<>();

		for(UsptExpert expertTargetInfo : expertTargetList) {
			UsptExtrcMfcmm usptExtrcMfcmm = new UsptExtrcMfcmm();//insert대상 세팅

			usptExtrcMfcmm.setExtrcMfcmmId(string.getNewId("evl-extrcmfcmm")); //추출 위원 테이블 key 세팅
			usptExtrcMfcmm.setMfcmmExtrcId(newUsptEvlMfcmmExtrc); //평가위원추출 테이블 key
			usptExtrcMfcmm.setExpertId(expertTargetInfo.getExpertId());//전문가 id

			usptExtrcMfcmm.setCreatedDt(now);
			usptExtrcMfcmm.setCreatorId(worker.getMemberId());
			usptExtrcMfcmm.setLsnResultCd(Code.lsnResult.대기중);
			list.add(usptExtrcMfcmm);
		}

		usptExtrcMfcmmDao.insertList(list);

		//3.제외 위원 등록 -> 일단 주석 처리
		//1)참여제한조건에 해당되는 위원 제외 - 사유 참여제한
		//2)추가배제조건에 해당되는 위원 제외 - 사유 추가배제조건
		//3)평가계획에 이해관계자로 등록된 위원 제외 - 사유 이해관계자
		//4)기본 제외 - 동일 평가단계의 다른 분과에 소속된 참여 제외는 기본 제외임,  - 기존 추출(1차 추출자 제외 해야 함) - 2차 추출의 경우 - 사유 - 제외위원으로 등록하지는 않음

		List<UsptExpert> expertTargetLmttList = usptExpertDao.selectExpertTargetLmttList(usptEvlMfcmmExtrc);

		List<UsptExclMfcmm> list2 = new ArrayList<>();

		for(UsptExpert expertTargetLmttInfo : expertTargetLmttList) {
			UsptExclMfcmm usptExclMfcmm = new UsptExclMfcmm();//insert대상 세팅

			usptExclMfcmm.setExclMfcmmId(string.getNewId("evl-exclmfcmm")); //제외 위원 테이블 key 세팅
			usptExclMfcmm.setMfcmmExtrcId(newUsptEvlMfcmmExtrc); //평가위원추출 테이블 key
			usptExclMfcmm.setExpertId(expertTargetLmttInfo.getExpertId());//전문가 id
			usptExclMfcmm.setExclResnCn(expertTargetLmttInfo.getExclResnCn());//제외사유내용

			usptExclMfcmm.setCreatedDt(now);
			usptExclMfcmm.setCreatorId(worker.getMemberId());

			list2.add(usptExclMfcmm);

		}

		if(list2.size() > 0) {
			usptExclMfcmmDao.insertList(list2);
		}

		//결과 재조회를 위해 세팅
		usptEvlMfcmmExtrc.setOdrNo(usptEvlMfcmmExtrcDao.selectMaxOdrNo(newUsptEvlMfcmmExtrc));

		return usptEvlMfcmmExtrc;
	}


	//평가위원 차수별 상세조회
	public EvlCmitExtrcResultDto getEvlCmitExtrcResult(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {
		EvlCmitExtrcResultDto resultData = usptEvlMfcmmExtrcDao.selectEvlCmitExtrcResult(usptEvlMfcmmExtrc);//추출조건 상세 조회

		if (resultData == null) {
			throw new InvalidationException("위원회 추출 정보가 없습니다.");
		}


		//추출위원 리스트 세팅
		List<UsptExpert> extrcExpertList = usptExpertDao.selectListByExtrc(usptEvlMfcmmExtrc);
		resultData.setExtrcExpertList(extrcExpertList);

		//제외위원 리스트 세팅
		List<UsptExpert> exclExpertList = usptExpertDao.selectListByExcl(usptEvlMfcmmExtrc);
		resultData.setExclExpertList(exclExpertList);

		log.debug("resultData ====  {}", resultData.toString());

		return resultData;
	 }

	//평가위원 차수별 상세조회-엑셀다운로드
	public EvlCmitExtrcExcelResultDto getEvlCmitExtrcExcelResult(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {
		EvlCmitExtrcExcelResultDto resultData = usptEvlMfcmmExtrcDao.selectEvlCmitExtrcExcelResult(usptEvlMfcmmExtrc);//추출조건 상세 조회

		if (resultData == null) {
			throw new InvalidationException("위원회 추출 정보가 없습니다.");
		}


		//추출위원 리스트 세팅
		List<UsptExpertExcel> extrcExpertExcelList = usptExpertDao.selectListByExtrcExcel(usptEvlMfcmmExtrc);
		resultData.setExtrcExpertExcelList(extrcExpertExcelList);

		//제외위원 리스트 세팅
		List<UsptExpertExcel> exclExpertExcelList = usptExpertDao.selectListByExclExcel(usptEvlMfcmmExtrc);
		resultData.setExclExpertExcelList(exclExpertExcelList);

		log.debug("resultData ====  {}", resultData.toString());

		return resultData;
	 }

	//평가위원 섭외 상세조회
	public UsptEvlCmitExtrcTarget getLsn(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {

		UsptEvlCmitExtrcTarget resultData = usptEvlCmitDao.selectEvlCmitExtrcTarget(usptEvlMfcmmExtrc.getEvlCmitId());

		if (resultData == null) {
			throw new InvalidationException("위원회 정보가 없습니다.");
		}

		UsptEvlCmitExtrcTarget cntResultData = usptEvlCmitDao.selectEvlCmitExtrcTargetCount(usptEvlMfcmmExtrc);

		resultData.setWaitCo(cntResultData.getWaitCo());
		resultData.setAbsnceCo(cntResultData.getAbsnceCo());
		resultData.setRejectCo(cntResultData.getRejectCo());
		resultData.setConfmCo(cntResultData.getConfmCo());
		resultData.setEvasCo(cntResultData.getEvasCo());

		return resultData;
	 }


	//평가위원 섭외 상세 - 위원 목록
	public List<UsptExpert> getLsnMfcmmList(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {

		List<UsptExpert> resultData = usptExpertDao.selectListByExtrc(usptEvlMfcmmExtrc);

//		if (resultData.size() < 1) {
//			throw new InvalidationException("위원회 정보가 없습니다.");
//		}

		return resultData;
	 }

	//평가위원 섭외 상세 - 위원 목록 엑셀다운
	public List<UsptExpertExcel> getLsnMfcmmExcelList(UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {

		List<UsptExpertExcel> resultData = usptExpertDao.selectListByExtrcExcel(usptEvlMfcmmExtrc);

//		if (resultData.size() < 1) {
//			throw new InvalidationException("위원회 정보가 없습니다.");
//		}

		return resultData;
	 }


	//평가위원 상세 정보(섭외 결과 팝업창 상세조회용)
	public UsptExpertResult getMfcmmDetail(HttpServletRequest request, String extrcMfcmmId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		UsptExpertResult resultData = usptExpertDao.selectMfcmmDetailEnc(extrcMfcmmId);

		if (resultData == null) {
			throw new InvalidationException("추출 위원 정보가 없습니다.");
		}

		/* 개인정보 조회 이력 생성  --------------------------------------------------------------------------------*/
		// 평가 위원정보
		LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
				.memberId(worker.getMemberId())
				.memberIp(CoreUtils.webutils.getRemoteIp(request))
				.workTypeNm("조회")
				.workCn("평가위원 섭외 상세조회 - 평가 위원 정보")
				.trgterId(resultData.getExpertId())
				.trgterNm(resultData.getExpertNm())
				.email(resultData.getEmail())
				.birthday("")
				.mobileNo(resultData.getMbtlnum())
				.build();
		indvdlInfSrchUtils.insert(logParam);

		return resultData;
	}


	//평가 위원 섭외결과 등록
	public UsptCnsltHist modifyMfcmmDetail(UsptCnsltHist usptCnsltHist) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException exs = new InvalidationsException();

		//유효체크
		if (usptCnsltHist == null) {
			throw new InvalidationException("등록할 정보가 없습니다.");
		}

		if (string.isBlank(usptCnsltHist.getTalkDe())) {
			exs.add("talkDe", "통화 날짜를 선택해주세요.");
		}

		if (string.isBlank(usptCnsltHist.getTalkTime())) {
			exs.add("talkTime", "통화시간을 선택해주세요.");
		}

		if (string.isBlank(usptCnsltHist.getTalkMin())) {
			exs.add("talkMin", "통화분을 선택해주세요.");
		}

		if (string.isBlank(usptCnsltHist.getLsnResultCd())) {
			exs.add("lsnResultCd", "섭외결과를 선택해주세요.");
		}

		if (string.isBlank(usptCnsltHist.getCnsltCn())) {
			exs.add("cnsltCn", "상담내용을 입력해주세요.");
		}

		if (string.isBlank(usptCnsltHist.getExtrcMfcmmId())) {
			exs.add("extrcMfcmmId", "추출위원 정보가 없습니다.");
		}

		if (exs.size() > 0) {
			throw exs;
		}

		UsptExtrcMfcmm existInfo = usptExtrcMfcmmDao.select(usptCnsltHist.getExtrcMfcmmId());

		if(existInfo == null) {
			throw new InvalidationException("올바르지 않은 위원정보입니다.");
		}


		//추출 위원 테이블 수정 처리, 상담이력 등록 처리
		//1.추출위원 테이블에 섭외결과 코드 업데이트(최종 섭외결과 코드용)
		UsptExtrcMfcmm usptExtrcMfcmm = new UsptExtrcMfcmm();

		usptExtrcMfcmm.setExtrcMfcmmId(usptCnsltHist.getExtrcMfcmmId());
		usptExtrcMfcmm.setLsnResultCd(usptCnsltHist.getLsnResultCd());

		usptExtrcMfcmm.setUpdatedDt(now);
		usptExtrcMfcmm.setUpdaterId(worker.getMemberId());

		usptExtrcMfcmmDao.update(usptExtrcMfcmm);

		//승낙처리이면 평가위원 table 등록 처리
		//TODO : (임병진) 로그인ID 패스워드 발급 및 전송 후 아래 로직 수행(언제 발송하나 확인 필요)
		if(string.equals(usptCnsltHist.getLsnResultCd(), Code.lsnResult.승낙)){
			UsptEvlMfcmm usptEvlMfcmm = new UsptEvlMfcmm();

			usptEvlMfcmm.setExtrcMfcmmId(usptCnsltHist.getExtrcMfcmmId());

			//이미 승낙처리로 등록되어있는지 확인
			if(string.equals(existInfo.getLsnResultCd(), Code.lsnResult.승낙)){
				throw new InvalidationException("이미 승낙 처리된 위원입니다.");
			}else {
				//기존에 승낙처리로 평가위원으로 등록된 건이 있으면 해당건 삭제하고 다시 등록처리함
				usptEvlMfcmmDao.deleteExtrcMfcmmId(usptEvlMfcmm.getExtrcMfcmmId());
			}

			//이미 승낙 처리 했어도 거부할 수 있기에 주석처리 함.
			/*
			 * UsptEvlMfcmm regInfo = usptEvlMfcmmDao.select(usptEvlMfcmm);
			 *
			 * if(regInfo != null) { throw new InvalidationException("이미 등록된 위원 정보입니다."); }
			 */

			String evlMfcmmId = string.getNewId("evl-mfcmm");
			usptEvlMfcmm.setEvlMfcmmId(evlMfcmmId);//key 세팅

			//String loginId = CoreUtils.string.getRandomNo(6);//임시
			String pwd = CoreUtils.password.getRandomPassword();//임시

			log.debug("pwd ="+pwd);

			//섭외결과 저장
			usptEvlMfcmm.setEncPassword(CoreUtils.password.encode(pwd));  //패스워드 암호화
			usptEvlMfcmm.setUseBgnde("20220101");//임시
			usptEvlMfcmm.setUseEndde("20221231");//임시

			usptEvlMfcmm.setCreatedDt(now);
			usptEvlMfcmm.setCreatorId(worker.getMemberId());
			usptEvlMfcmm.setAtendSttusCd(Code.atendSttus.대기중);

			int insertCnt = usptEvlMfcmmDao.insert(usptEvlMfcmm);

			if(insertCnt < 1) {
				throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
			}

			//추출위원ID로 전문가 전화번호 이메일 추출 필요
			//온라인일 경우만 처리
			if(existInfo.getOnlineNm().equals("Y")) {
				UsptExpert expertInfo = usptExpertDao.selectMfcmmDetail(usptCnsltHist.getExtrcMfcmmId());

				if (expertInfo == null) {
					throw new InvalidationException("추출 위원 정보가 없습니다.");
				}

				SmsSendParam param = new SmsSendParam();

				String mobileNo = string.trim(expertInfo.getMbtlnum());
				log.debug("mobileNo basicInfo= " + mobileNo);
				//mobileNo = "010-8937-7106";//임시로 처리
				log.debug("mobileNo temp= " + mobileNo);

				param.setSmsCn("평가위원 로그인 정보안내\n위원님의 로그인 암호는 ##param## 입니다");

				// template Parameter 설정
				Map<String, String> tp = new HashMap<>();
				tp.put("param", pwd);
				param.addRecipient(mobileNo, tp);

				// SMS 발송
		        SmsResult sr = smsUtils.send(param);
		        //sr.getSmsId()

		        // 모두 발송실패일 경우, 오류처리
		        if (sr.getFailCnt() == param.getRecipientList().size()) {
		        	throw new InvalidationException("SMS발송 실패 :"+ sr.getRecipientList().get(0).getResultMessage());
		        }
		        log.info(sr.toString());
			}

		}

		//승낙 처리 이후 거부처리이면 평가위원 table 삭제 처리
		//TODO : (임병진) 승낙 처리 이후 거부 처리 가능한가? 가능하다면 패스워드 발급 회수 등 문제, 불가하다면 불가 유효체크 로직 추가 필요




		//2.상담이력 insert
		String histIdKey = string.getNewId("evl-cnslthist");
		usptCnsltHist.setHistId(histIdKey);
		usptCnsltHist.setExtrcMfcmmId(usptCnsltHist.getExtrcMfcmmId());
		usptCnsltHist.setCreatedDt(now);
		usptCnsltHist.setCreatorId(worker.getMemberId());

		usptCnsltHist.setTalkDt(string.toDate(usptCnsltHist.getTalkDe().replace("-","")+usptCnsltHist.getTalkTime()+usptCnsltHist.getTalkMin()));

		usptCnsltHistDao.insert(usptCnsltHist);

		//등록 결과 조회
		return usptCnsltHistDao.select(histIdKey);

	}

	//평가위원 상세 정보(섭외 결과 팝업창 상세조회용)
	public List<UsptCnsltHist> getMfcmmCnsltHistList(String extrcMfcmmId) {
		List<UsptCnsltHist> resultData = usptCnsltHistDao.selectList(extrcMfcmmId);

		if (resultData.size() < 1) {
			throw new InvalidationException("이력 정보가 없습니다.");
		}

		return resultData;
	}

	//위원회 기본정보 조회
	public UsptEvlCmit getEvlCmitInfo(String evlCmitId) {
		UsptEvlCmit resultData = usptEvlCmitDao.select(evlCmitId);

		if (resultData == null) {
			throw new InvalidationException("위원회 정보가 없습니다.");
		}

		return resultData;
	}


	//종합의견 등록
	public UsptEvlCmit addCharmnOpinion(UsptEvlCmit usptEvlCmit) {
		//TODO : (임병진)추후 평가자 로그인 체크로 변경 필요
		BnMember worker = SecurityUtils.checkWorkerIsEvaluator();
		InvalidationsException exs = new InvalidationsException();

		//유효체크
		if (string.isBlank(usptEvlCmit.getEvlCmitId())) {
			throw new InvalidationException("위원회 정보가 없습니다.");
		}

		UsptEvlCmit resultData = usptEvlCmitDao.select(usptEvlCmit.getEvlCmitId());

		if (resultData == null) {
			throw new InvalidationException("등록된 위원회 정보가 없습니다.");
		}

		if (string.isBlank(usptEvlCmit.getEvlCharmnOpinionCn())) {
			exs.add("evlCharmnOpinionCn", "종합의견을 입력해주세요.");
		}

		if (exs.size() > 0) {
			throw exs;
		}

		Date now = new Date();

		usptEvlCmit.setUpdatedDt(now);
		usptEvlCmit.setUpdaterId(worker.getMemberId());

		usptEvlCmitDao.updateCharmOpinion(usptEvlCmit);

		return usptEvlCmitDao.select(usptEvlCmit.getEvlCmitId());
	}


	//평가완료처리
	public UsptEvlCmit modifyEvlCompt(String evlCmitId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		//유효체크
		if (string.isBlank(evlCmitId)) {
			throw new InvalidationException("위원회 정보가 없습니다.");
		}

		UsptEvlCmit basicInfo = usptEvlCmitDao.select(evlCmitId);

		if( basicInfo == null ) {
			throw new InvalidationException("위원회 정보가 없습니다.");
		}

		if (string.equals(basicInfo.getEvlSttusCd(), Code.evlSttus.평가완료) ){
			throw new InvalidationException("이미 평가완료 처리 되었습니다.");
		}

		UsptEvlCmit usptEvlCmit = new UsptEvlCmit();
		Date now = new Date();

		usptEvlCmit.setEvlCmitId(evlCmitId);
		usptEvlCmit.setEvlSttusCd(Code.evlSttus.평가완료);

		usptEvlCmit.setEvlSttusDt(now); // 평가상태일시
		usptEvlCmit.setUpdatedDt(now);
		usptEvlCmit.setUpdaterId(worker.getMemberId());

		int updateCnt = usptEvlCmitDao.updateEvlSttus(usptEvlCmit);

		if(updateCnt <  1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
		}

		return usptEvlCmitDao.select(usptEvlCmit.getEvlCmitId());
	}


	//평가완료 처리 취소
	public UsptEvlCmit modifyEvlComptCancle(String evlCmitId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		//유효체크
		if (string.isBlank(evlCmitId)) {
			throw new InvalidationException("위원회 정보가 없습니다.");
		}

		UsptEvlCmit basicInfo = usptEvlCmitDao.select(evlCmitId);

		if( basicInfo == null ) {
			throw new InvalidationException("위원회 정보가 없습니다.");
		}

		if (string.equals(basicInfo.getEvlSttusCd(), Code.evlSttus.진행중) ){
			throw new InvalidationException("이미 평가완료 취소 처리 되었습니다.");
		}

		UsptEvlCmit usptEvlCmit = new UsptEvlCmit();
		Date now = new Date();

		usptEvlCmit.setEvlCmitId(evlCmitId);
		usptEvlCmit.setEvlSttusCd(Code.evlSttus.진행중);
		usptEvlCmit.setEvlSttusDt(now); // 평가상태일시
		usptEvlCmit.setUpdatedDt(now);
		usptEvlCmit.setUpdaterId(worker.getMemberId());

		int updateCnt = usptEvlCmitDao.updateEvlSttus(usptEvlCmit);

		if(updateCnt <  1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
		}

		return usptEvlCmitDao.select(usptEvlCmit.getEvlCmitId());

	}


	//평가완료처리-위원별
	public UsptEvlMfcmm modifyEvlComptByEvlMfcmm(String evlMfcmmId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		//유효체크
		if (string.isBlank(evlMfcmmId)) {
			throw new InvalidationException("위원 정보가 없습니다.");
		}

		UsptEvlMfcmm chkParam = new UsptEvlMfcmm();
		chkParam.setEvlMfcmmId(evlMfcmmId);

		UsptEvlMfcmm regInfo = usptEvlMfcmmDao.select(chkParam);

		if(regInfo == null) {
			throw new InvalidationException("위원 정보가 없습니다.");
		}else {
			if(string.equals(regInfo.getMfcmmEvlSttusCd(), Code.mfcmmEvlSttus.평가완료)){
				throw new InvalidationException("이미 평가완료 처리 되었습니다.");
			}
		}

		UsptEvlMfcmmResult insertParam = new UsptEvlMfcmmResult();

		Date now = new Date();

		insertParam.setEvlMfcmmId(evlMfcmmId);
		insertParam.setMfcmmEvlSttusCd(Code.mfcmmEvlSttus.평가완료);//위원평가상태코드 평가완료
		insertParam.setMfcmmEvlSttusDt(now);
		insertParam.setUpdatedDt(now);
		insertParam.setUpdaterId(worker.getMemberId());

		int updateCnt = usptEvlMfcmmResultDao.updateMfcmmEvlSttusAll(insertParam); //해당위원의 모든 대상에 대한 평가완료 처리

		if(updateCnt <  1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "처리"));
		}

		return usptEvlMfcmmDao.select(chkParam);
	}


	//로그인 가능한 위원회 목록
	public List<EvlLoginCmitListDto> getLoginCmitList() {
		return usptEvlCmitDao.selectLoginCmitList();
	}
}

