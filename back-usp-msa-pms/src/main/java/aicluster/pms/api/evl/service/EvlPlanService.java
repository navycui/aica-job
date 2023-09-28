package aicluster.pms.api.evl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.dao.CmmtAtchmnflDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.pblanc.dto.BsnsPblancListParam;
import aicluster.pms.common.dao.UsptBsnsPblancDao;
import aicluster.pms.common.dao.UsptBsnsPblancRceptDao;
import aicluster.pms.common.dao.UsptEvlCmitDao;
import aicluster.pms.common.dao.UsptEvlLastSlctnDao;
import aicluster.pms.common.dao.UsptEvlMfcmmExtrcDao;
import aicluster.pms.common.dao.UsptEvlPlanDao;
import aicluster.pms.common.dao.UsptEvlSectDao;
import aicluster.pms.common.dao.UsptEvlStepDao;
import aicluster.pms.common.dao.UsptEvlTrgetDao;
import aicluster.pms.common.dao.UsptUdstdprcpDao;
import aicluster.pms.common.dto.EvlCmitExtrcResultDto;
import aicluster.pms.common.entity.UsptBsnsPblancRcept;
import aicluster.pms.common.entity.UsptEvlCmit;
import aicluster.pms.common.entity.UsptEvlCmitDta;
import aicluster.pms.common.entity.UsptEvlLastSlctn;
import aicluster.pms.common.entity.UsptEvlMfcmmExtrc;
import aicluster.pms.common.entity.UsptEvlPlan;
import aicluster.pms.common.entity.UsptEvlStep;
import aicluster.pms.common.entity.UsptEvlTrget;
import aicluster.pms.common.entity.UsptSect;
import aicluster.pms.common.entity.UsptUdstdprcp;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EvlPlanService {

	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private UsptEvlPlanDao usptEvlPlanDao;

	@Autowired
	private UsptEvlSectDao usptEvlSectDao;

	@Autowired
	private UsptEvlStepDao usptEvlStepDao;

	@Autowired
	private UsptEvlCmitDao usptEvlCmitDao;

	@Autowired
	private UsptUdstdprcpDao usptUdstdprcpDao;

	@Autowired
	private UsptEvlTrgetDao usptEvlTrgetDao;

	@Autowired
	private UsptBsnsPblancRceptDao usptBsnsPblancRceptDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private UsptBsnsPblancDao usptBsnsPblancDao;

	@Autowired
    private CmmtAtchmnflDao attachmentDao;

//	@Autowired
//	private UsptCnsltHistDao usptCnsltHistDao;
//
//	@Autowired
//	private UsptExtrcMfcmmDao usptExtrcMfcmmDao;
//
//	@Autowired
//	private UsptExclMfcmmDao usptExclMfcmmDao;

	@Autowired
	private UsptEvlMfcmmExtrcDao usptEvlMfcmmExtrcDao;

	@Autowired
	private UsptEvlLastSlctnDao usptEvlLastSlctnDao;


	@Autowired
	private EnvConfig config;

	//평가계획 목록조회
	public CorePagination<UsptEvlPlan> getList(UsptEvlPlan usptEvlPlanParam, Long page) {
		if(page == null) {
			page = 1L;
		}

		if(usptEvlPlanParam.getItemsPerPage() == null) {
			usptEvlPlanParam.setItemsPerPage(ITEMS_PER_PAGE);
		}

		usptEvlPlanParam.setExcel(false);

		long totalItems = usptEvlPlanDao.selectListCount(usptEvlPlanParam);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, usptEvlPlanParam.getItemsPerPage(), totalItems);

		usptEvlPlanParam.setBeginRowNum(info.getBeginRowNum());
		List<UsptEvlPlan> list = usptEvlPlanDao.selectList(usptEvlPlanParam);

		//출력할 자료 생성 후 리턴
		CorePagination<UsptEvlPlan> pagination = new CorePagination<>(info, list);

		return pagination;

	 }


	//평가계획 목록 엑셀다운로드
	public List<UsptEvlPlan> getListExcelDwld(UsptEvlPlan usptEvlPlanParam) {
		usptEvlPlanParam.setExcel(true);

		return usptEvlPlanDao.selectList(usptEvlPlanParam);
	}

	//평가계획 기본정보 등록
	public String insert(UsptEvlPlan usptEvlPlan) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if (usptEvlPlan == null) {
			throw new InvalidationException("입력정보가 없습니다.");
		}

		if (usptEvlPlan.getUsptSectList() == null) {
			throw new InvalidationException("입력한 분과 정보가 없습니다.");
		}

		InvalidationsException exs = new InvalidationsException();

		if (string.isBlank(usptEvlPlan.getPblancId())) {
			exs.add("pblancId", "공고ID 정보가 없습니다.");
		}

		//해당 공고가 평가/선정 단계가 있는 공고인지 확인함
		validChkPblanc(usptEvlPlan.getPblancId());

		if (usptEvlPlan.getRceptOdr() == null) {
			exs.add("rceptOdr", "접수차수 정보가 없습니다.");
		}

		if (string.isBlank(usptEvlPlan.getEvlTypeCd())) {
			exs.add("indvdlzOpinionWritng", "평가유형을 선택하세요.");
		}

		if (usptEvlPlan.getWctMdatDlbrt() == null) {
			exs.add("indvdlzOpinionWritng", "사업비조정심의여부를 선택하세요.");
		}

		if (exs.size() > 0) {
			throw exs;
		}

		String evlPlanIdKey= string.getNewId("evlplan-");

		usptEvlPlan.setEvlPlanId(evlPlanIdKey);
		usptEvlPlan.setEvlSttusCd(Code.evlSttus.대기중);
		usptEvlPlan.setCreatorId(worker.getMemberId());
		usptEvlPlan.setUpdaterId(worker.getMemberId());
		usptEvlPlan.setCreatedDt(now);
		usptEvlPlan.setUpdatedDt(now);

		//평가계획 등록
		if(usptEvlPlanDao.selectListExistCount(usptEvlPlan) > 0L){
			throw new InvalidationException("이미 등록된 평가계획입니다.(공고, 차수)");
		}

		if(usptEvlPlanDao.insert(usptEvlPlan) < 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "평가계획 등록"));
		}

		//평가분과 등록
		upstSetcRegFn(evlPlanIdKey, usptEvlPlan.getUsptSectList());

		//평가최종선정 등록
		UsptEvlLastSlctn usptEvlLastSlctn =  new UsptEvlLastSlctn();
		usptEvlLastSlctn.setEvlLastSlctnId(string.getNewId("evllastslctn-"));
		usptEvlLastSlctn.setEvlPlanId(evlPlanIdKey);
		usptEvlLastSlctn.setLastSlctnProcessDivCd(Code.lastSlctnProcessDiv.대기);

		usptEvlLastSlctn.setCreatorId(worker.getMemberId());
		usptEvlLastSlctn.setUpdaterId(worker.getMemberId());
		usptEvlLastSlctn.setCreatedDt(now);
		usptEvlLastSlctn.setUpdatedDt(now);

		if(usptEvlLastSlctnDao.insert(usptEvlLastSlctn) < 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "평가최종선정 등록"));
		}

		return evlPlanIdKey;
	}


	//평가계획 기본정보 수정
	public UsptEvlPlan modify(UsptEvlPlan usptEvlPlan) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if (usptEvlPlan.getUsptSectList() == null) {
			throw new InvalidationException("입력한 분과 정보가 없습니다.");
		}

		//유효체크 1: 입력값
		InvalidationsException exs = new InvalidationsException();

		if (string.isBlank(usptEvlPlan.getEvlPlanId())) {
			exs.add("pblancId", "평가계획ID가 입력되지 않았습니다.");
		}

		if (string.isBlank(usptEvlPlan.getPblancId())) {
			exs.add("pblancId", "공고ID 정보가 없습니다.");
		}

		if (usptEvlPlan.getRceptOdr() == null) {
			exs.add("rceptOdr", "접수차수 정보가 없습니다.");
		}

		if (string.isBlank(usptEvlPlan.getEvlTypeCd())) {
			exs.add("indvdlzOpinionWritng", "평가유형을 선택하세요.");
		}

		if (usptEvlPlan.getWctMdatDlbrt() == null) {
			exs.add("indvdlzOpinionWritng", "사업비조정심의여부를 선택하세요.");
		}

		if (exs.size() > 0) {
			throw exs;
		}

		//유효체크 2 : 진행상태
		UsptEvlPlan existPlanInfo = usptEvlPlanDao.select(usptEvlPlan.getEvlPlanId());

		if(existPlanInfo == null ){
			throw new InvalidationException("평가계획 정보가 없습니다.");
		}else {
			if(!string.equals(existPlanInfo.getEvlSttusCd(), Code.evlSttus.대기중)) {
				throw new InvalidationException("진행상태가 대기중인 건만 수정이 가능합니다.");
			}
		}

		//유효체크 3 : 해당 공고가 평가/선정 단계가 있는 공고인지 확인함(
		validChkPblanc(usptEvlPlan.getPblancId());

		existPlanInfo.setPblancId(usptEvlPlan.getPblancId());
		existPlanInfo.setRceptOdr(usptEvlPlan.getRceptOdr());
		existPlanInfo.setEvlTypeCd(usptEvlPlan.getEvlTypeCd());
		existPlanInfo.setWctMdatDlbrt(usptEvlPlan.getWctMdatDlbrt());

		existPlanInfo.setUpdaterId(worker.getMemberId());
		existPlanInfo.setUpdatedDt(now);

		//평가계획 수정
		if(usptEvlPlanDao.update(existPlanInfo) < 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}

		//평가분과 수정
		upstSetcRegFn(usptEvlPlan.getEvlPlanId(), usptEvlPlan.getUsptSectList());

		return usptEvlPlan;
	}

	//평가계획 기본정보 사용여부 수정
	public UsptEvlPlan modifyEnable(UsptEvlPlan usptEvlPlan) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		//유효체크 1: 입력값
		InvalidationsException exs = new InvalidationsException();

		if (string.isBlank(usptEvlPlan.getEvlPlanId())) {
			exs.add("pblancId", "평가계획ID가 입력되지 않았습니다.");
		}

		if (usptEvlPlan.getEnabled() == null) {
			exs.add("enabled", "사용여부가 입력되지 않았습니다.");
		}


		//유효체크 2 : 진행상태
		UsptEvlPlan existPlanInfo = usptEvlPlanDao.select(usptEvlPlan.getEvlPlanId());

		if(existPlanInfo == null ){
			throw new InvalidationException("평가계획 정보가 없습니다.");
		}else {
			if(!string.equals(existPlanInfo.getEvlSttusCd(), Code.evlSttus.대기중)) {
				throw new InvalidationException("진행상태가 대기중인 건만 수정이 가능합니다.");
			}
		}

		//사용상태 유효체크
		if(usptEvlPlan.getEnabled()) {
			if(existPlanInfo.getEnabled()) {
				throw new InvalidationException("이미 사용 상태인 평가계획입니다.");
			}
		}else {
			if(!existPlanInfo.getEnabled()) {
				throw new InvalidationException("이미 미사용 상태인 평가계획입니다.");
			}
		}

		existPlanInfo.setEnabled(usptEvlPlan.getEnabled());
		existPlanInfo.setUpdaterId(worker.getMemberId());
		existPlanInfo.setUpdatedDt(now);

		//평가계획 사용여부 수정
		if(usptEvlPlanDao.updateEnable(existPlanInfo) < 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}

		return usptEvlPlan;
	}

	 //평가계획 상세조회
	 public UsptEvlPlan get(String evlPlanId) {
		 UsptEvlPlan usptEvlPlan = usptEvlPlanDao.select(evlPlanId);

		 if (usptEvlPlan == null) {
			 throw new InvalidationException("평가계획 정보가 없습니다.");
		 }

		 List<UsptSect> usptSect = usptEvlSectDao.selectList(evlPlanId); //분과정보 리스트
		 usptEvlPlan.setUsptSectList(usptSect);

		 return usptEvlPlan;
	 }


	//평가분과 등록 공통 처리
	public void upstSetcRegFn(String evlPlanIdKey, List<UsptSect> usptSectList){
		Date now = new Date();
		InvalidationsException exs = new InvalidationsException();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		int evlIemCount = 0;
		String _flag = "";

		//분과 정보 유효체크
		for(UsptSect usptSectInfo : usptSectList) {
			evlIemCount ++;

			if (string.equals(_flag, "I") || string.equals(_flag, "U") ) {
				if (string.isBlank(usptSectInfo.getSectNm())) {
					throw new InvalidationException(evlIemCount + "번째 행의 분과명을 입력해주세요.");
				}

				if (usptSectInfo.getEvlTrgetCo() == null) {
					throw new InvalidationException(evlIemCount + "번째 행의 평가대상수를 입력해주세요.");
				}
			}else if(string.equals(_flag, "D")) {
				String sectId = usptSectInfo.getSectId();
				//해당 분과의 위원회 검색
				UsptEvlCmit cmitParam = new UsptEvlCmit();
				cmitParam.setSectId(sectId);
				List<UsptEvlCmit> existEvlCmitInfoList = usptEvlCmitDao.selectCmitList(cmitParam);

				if(existEvlCmitInfoList.size() > 0){
					throw new InvalidationException(evlIemCount + "번째 행 - 이미 등록된 위원회가 존재하므로 삭제할 수 없습니다.");
				}
			}
		}

		if (exs.size() > 0) {
			throw exs;
		}

		//평가 분과 등록,수정,삭제 처리
		for(UsptSect usptSectInfo : usptSectList) {
			_flag =  usptSectInfo.getFlag();

			if (string.equals(_flag, "I")) {
				usptSectInfo.setCreatedDt(now);
				usptSectInfo.setCreatorId(worker.getMemberId());
				usptSectInfo.setUpdatedDt(now);
				usptSectInfo.setUpdaterId(worker.getMemberId());

				usptSectInfo.setEvlPlanId(evlPlanIdKey);
				usptSectInfo.setSectId(string.getNewId("evlsect-"));

				if(usptEvlSectDao.insert(usptSectInfo) < 1) {
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "등록"));
				}

			} else if ( string.equals(_flag, "U") || string.equals(_flag, "D") ) {
				//수정시에는 기존 분과 확인 후 세팅 후 수정 처리함.
				//해당계획에 존재하는 분과인지 확인
				if(string.isBlank(usptSectInfo.getSectId())){
					throw new InvalidationException(evlIemCount + "번째 행 - 분과 정보가 없습니다.");
				}
				UsptSect paramSectChk = new UsptSect();
				paramSectChk.setEvlPlanId(evlPlanIdKey);
				paramSectChk.setSectId(usptSectInfo.getSectId());

				List<UsptSect> updateInfoList = usptEvlSectDao.selectExistList(paramSectChk);

				if(updateInfoList.size() < 1) {
					throw new InvalidationException(evlIemCount + "번째 행 - 해당 계획에 존재하지 않는 분과 정보입니다.");
				}

				if(string.equals(_flag, "U")) {
					updateInfoList.get(0).setSectNm(usptSectInfo.getSectNm());
					updateInfoList.get(0).setEvlTrgetCo(usptSectInfo.getEvlTrgetCo());

					updateInfoList.get(0).setUpdatedDt(now);
					updateInfoList.get(0).setUpdaterId(worker.getMemberId());

					if(usptEvlSectDao.update(updateInfoList.get(0)) < 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					}
				} else if (string.equals(_flag, "D")) {

					//이미 평가위원회가 구성된 분과는 삭제불가
					UsptEvlCmit chkParam = new UsptEvlCmit();
					chkParam.setSectId(usptSectInfo.getSectId());

					if(usptEvlCmitDao.selectExistList(chkParam).size() > 0) {
						throw new InvalidationException(evlIemCount + "번째 행 - 이미 평가위원회가 구성된 분과는 삭제할 수 없습니다.");
					}

					if(usptEvlSectDao.delete(usptSectInfo.getSectId()) < 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					}
				}

			}

//				main.해당 분과의 워원회 기준으로 모두 삭제 -> 삭제 하지 않고 유효체크로 막기로 함
//				for(UsptEvlCmit UsptEvlCmitInfo : UsptEvlCmitList) {
//					String cmitId = UsptEvlCmitInfo.getEvlCmitId();
//
//					removeCmitInfo(cmitId);//하위 위원 관련 테이블 모두 삭제
//				}
//
//				removeCmitTargetInfo(sectId);//해당 위원 관련 대상 테이블 모두 삭제
		}//for
	}


	//평가단계 목록
	public List<UsptEvlStep> getEvlStepList(String evlPlanId) {
		return usptEvlStepDao.selectList(evlPlanId, "");
	}


	//평가단계 저장
	public List<UsptEvlStep> modifyEvlStepInfo(String evlPlanId, List<UsptEvlStep> usptEvlStepList) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException exs = new InvalidationsException();


		//1.입력값 null 체크
		int evlIemCount = 0;
		String _flag = "";

		for(UsptEvlStep usptEvlStepinfo : usptEvlStepList) {
			_flag =  usptEvlStepinfo.getFlag();

			if (string.equals(_flag, "I") || string.equals(_flag, "U") || string.equals(_flag, "D")) {
				evlIemCount++;
			}
		}

		if (evlIemCount < 1 ) {
			throw new InvalidationException("변경할 정보가 없습니다.");
		}


		//2.평가계획 체크
		UsptEvlPlan planInfo = usptEvlPlanDao.select(evlPlanId);

		if(planInfo == null ){
			throw new InvalidationException("평가계획 정보가 없습니다.");
		}else {
			if(!string.equals(planInfo.getEvlSttusCd(), Code.evlSttus.대기중)) {
				throw new InvalidationException("평가계획의 진행상태가 대기중인 건만 수정이 가능합니다.");
			}
		}

		//2-1.분과정보 등록 여부 체크 추가
		UsptSect paramSectChk = new UsptSect();
		paramSectChk.setEvlPlanId(evlPlanId);
		if(usptEvlSectDao.selectExistList(paramSectChk).size() < 1) {
			throw new InvalidationException("해당 평가계획의 분과정보가 없습니다. 분과정보를 먼저 등록해주세요.");
		}

		//3.입력값 유효체크 및 상태 체크
		evlIemCount = 0;

		for(UsptEvlStep usptEvlStepinfo : usptEvlStepList) {
			usptEvlStepinfo.setEvlPlanId(evlPlanId);
			evlIemCount ++;

			_flag =  usptEvlStepinfo.getFlag();

			if (string.equals(_flag, "U") || string.equals(_flag, "D")) {
				if (string.isBlank(usptEvlStepinfo.getEvlStepId())) {
					exs.add("evlStepId", evlIemCount + "번째 행의 평가단계ID가 없습니다.");
				}

				//존재하는 평가단계인지 확인
				UsptEvlStep paramStepChk = new UsptEvlStep();
				paramStepChk.setEvlPlanId(evlPlanId);
				paramStepChk.setEvlStepId(usptEvlStepinfo.getEvlStepId());

				List<UsptEvlStep> stepInfo = usptEvlStepDao.selectExistList(paramStepChk);

				if(stepInfo.size() < 1) {
					throw new InvalidationException(evlIemCount + "번째 행의 평가단계는 해당 계획에 존재하지 않습니다.");
				}

				//단계별 평가진행상태가 대기중인건만 수정 및 삭제 가능
				if( !string.equals(stepInfo.get(0).getEvlStepSttusCd(), Code.evlStepSttus.대기중)) {
					throw new InvalidationException(evlIemCount + "번째 행 - 대기중인 평가단계만 수정 및 삭제가 가능합니다.");
				}

				if (string.equals(_flag, "D")) {
					//이미 평가위원회가 구성된 평가단계는 삭제불가
					UsptEvlCmit chkParam = new UsptEvlCmit();
					chkParam.setEvlStepId(usptEvlStepinfo.getEvlStepId());

					if(usptEvlCmitDao.selectExistList(chkParam).size() > 0) {
						throw new InvalidationException(evlIemCount + "번째 행 - 이미 평가위원회가 구성된 평가 단계는 삭제할 수 없습니다.");
					}
				}

			}

			//등록 및 수정 시 필수값 체크
			if (string.equals(_flag, "I") || string.equals(_flag, "U") ) {
				if (usptEvlStepinfo.getSortOrdrNo() == null) {
					exs.add("topLwetExcl", evlIemCount + "번째 행의 정렬순서(번호)를 입력해주세요");
				}

				if (string.isBlank(usptEvlStepinfo.getEvlStepNm())) {
					exs.add("evlStepNm", evlIemCount + "번째 행의 평가단계명을 입력해주세요.");
				}

				if (string.isBlank(usptEvlStepinfo.getDrptStdrCd())) {
					exs.add("drptStdrCd", evlIemCount + "번째 행의 탈락처리기준을 선택해주세요.");
				}

				if (usptEvlStepinfo.getTopLwetExcl() == null) {
					exs.add("topLwetExcl", evlIemCount + "번째 행의 최고/최저 제외여부를 선택해주세요.");
				}
			}
		}

		if (exs.size() > 0) {
			throw exs;
		}

		//4.평가단계 저장처리(등록, 수정, 삭제)
		for(UsptEvlStep usptEvlStepInfo : usptEvlStepList) {
			_flag =  usptEvlStepInfo.getFlag();

			usptEvlStepInfo.setCreatedDt(now);
			usptEvlStepInfo.setCreatorId(worker.getMemberId());
			usptEvlStepInfo.setUpdatedDt(now);
			usptEvlStepInfo.setUpdaterId(worker.getMemberId());

			if (string.equals(_flag, "I")) {
				usptEvlStepInfo.setEvlPlanId(evlPlanId);
				usptEvlStepInfo.setEvlStepId(string.getNewId("evlstep-"));
				usptEvlStepInfo.setEvlStepSttusCd(Code.evlStepSttus.대기중);

				if(usptEvlStepDao.insert(usptEvlStepInfo) < 1) {
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "등록"));
				}
			}else if (string.equals(_flag, "U")) {
				//기존 정보 조회
				UsptEvlStep paramStepChk = new UsptEvlStep();
				paramStepChk.setEvlPlanId(evlPlanId);
				paramStepChk.setEvlStepId(usptEvlStepInfo.getEvlStepId());

				UsptEvlStep updateInfo = usptEvlStepDao.selectExistList(paramStepChk).get(0);

				//수정데이터 set후 수정 처리
				updateInfo.setSortOrdrNo(usptEvlStepInfo.getSortOrdrNo());
				updateInfo.setEvlStepNm(usptEvlStepInfo.getEvlStepNm());
				updateInfo.setSlctnTargetCo(usptEvlStepInfo.getSlctnTargetCo());
				updateInfo.setDrptStdrCd(usptEvlStepInfo.getDrptStdrCd());
				updateInfo.setTopLwetExcl(usptEvlStepInfo.getTopLwetExcl());

				if(usptEvlStepDao.update(updateInfo) < 1) {
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
				}
			}else if (string.equals(_flag, "D")) {
				if(usptEvlStepDao.delete(usptEvlStepInfo) < 1) {
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
				}

			}
		}

		return usptEvlStepDao.selectList(evlPlanId, "");
	}


	//평가위원회 목록
	public List<UsptEvlCmit> getEvlCmitList(String evlPlanId) {
		return usptEvlCmitDao.selectList(evlPlanId);
	}


	//평가위원회 저장
	public List<UsptEvlCmit> modifyEvlCmitInfo(String evlPlanId, List<UsptEvlCmit> usptEvlCmitList) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		int evlIemCount = 0;
		String _flag = "";

		for(UsptEvlCmit usptEvlCmitInfo : usptEvlCmitList) {
			_flag =  usptEvlCmitInfo.getFlag();

			if (string.equals(_flag, "U") || string.equals(_flag, "D")) {
				evlIemCount++;
			}
		}

		if (evlIemCount < 1 ) {
			throw new InvalidationException("저장할 정보가 없습니다.");
		}

		//유효체크
		evlIemCount = 0;

		for(UsptEvlCmit usptEvlCmitInfo : usptEvlCmitList) {
			evlIemCount ++;

			if (string.equals(_flag, "U")) {
				if (string.isBlank(usptEvlCmitInfo.getEvlCmitNm())) {
					throw new InvalidationException(evlIemCount + "번째 행의 평가위원회명을 입력해주세요.");
				}

				if (usptEvlCmitInfo.getMfcmmCo() == null) {
					throw new InvalidationException(evlIemCount + "번째 행의 위원수를 입력해주세요.");
				}

				if (usptEvlCmitInfo.getMfcmmCo() < 1) {
					throw new InvalidationException(evlIemCount + "번째 행의 위원수는 0 이상이어야 합니다.");
				}

				if (string.isBlank(usptEvlCmitInfo.getOrgnzrId())) {
					throw new InvalidationException(evlIemCount + "번째 행의 간사를 선택해주세요.");
				}

				if (string.isBlank(usptEvlCmitInfo.getEvlTableId())) {
					throw new InvalidationException(evlIemCount + "번째 행의 평가표를 선택해주세요.");
				}

				if (usptEvlCmitInfo.getOnline() == null) {
					throw new InvalidationException(evlIemCount + "번째 행의 온라인여부를 선택해주세요.");
				}
			}else if (string.equals(_flag, "D")) {
				//위원회 삭제시 처리
				if ( string.isBlank(usptEvlCmitInfo.getEvlCmitId()) ) {//수정처리
					throw new InvalidationException(evlIemCount + "번째 행 - 삭제할 위원회 정보가 없습니다.");
				}
			}
		}

		//평가위원회 저장처리
		//usptEvlCmitDao.delete(evlPlanId); //전체 삭제(by 평가계획id)
		//유효체크 : 진행상태가 대기중인경우만 변경 가능
		UsptEvlPlan basicInfo = usptEvlPlanDao.select(evlPlanId);

		if(basicInfo == null ){
			throw new InvalidationException("평가계획 정보가 없습니다.");
		}else {
			if(!string.equals(basicInfo.getEvlSttusCd(), Code.evlSttus.대기중)) {
				throw new InvalidationException("진행상태가 대기중인 건만 수정이 가능합니다.");
			}
		}


		evlIemCount = 0;

		for(UsptEvlCmit usptEvlCmitInfo : usptEvlCmitList) {
			evlIemCount ++;
			_flag =  usptEvlCmitInfo.getFlag();

			if (string.equals(_flag, "U")) {
				if ( !string.isBlank(usptEvlCmitInfo.getEvlCmitId()) ) {//수정처리
					UsptEvlCmit existChk = usptEvlCmitDao.select(usptEvlCmitInfo.getEvlCmitId());
					if(existChk == null) {
						throw new InvalidationException(evlIemCount + "번째 행의 평가위원회는 존재하지 않습니다.");
					}

					//수정할 정보 세팅
					existChk.setEvlTableId(usptEvlCmitInfo.getEvlTableId());
					existChk.setEvlCmitNm(usptEvlCmitInfo.getEvlCmitNm());
					existChk.setMfcmmCo(usptEvlCmitInfo.getMfcmmCo());

					if (!string.isBlank(usptEvlCmitInfo.getEvlPrarnde())) {
						String evlPrarnde = usptEvlCmitInfo.getEvlPrarnde();
						if( string.getNumberOnly(evlPrarnde).length() != 8 ) {
							throw new InvalidationException(evlIemCount + "번째 행 - 평가예정일 날짜 형식 오류입니다.");
						}
						Date day = string.toDate(usptEvlCmitInfo.getEvlPrarnde());
						if (day == null) {
							throw new InvalidationException(evlIemCount + "번째 행 - 평가예정일 날짜 형식 오류입니다.");
						}
						String ymd = date.format(day, "yyyyMMdd");
						existChk.setEvlPrarnde(ymd);
					}

					if (!string.isBlank(usptEvlCmitInfo.getBeginHour())) {
						String beginHour = usptEvlCmitInfo.getBeginHour();
						if( string.isEmpty(string.getNumberOnly(beginHour)) ) {
							throw new InvalidationException(evlIemCount + "번째 행 - 평가예정일 시작 시간 형식 오류입니다.");
						} else if( string.toInt(string.getNumberOnly(beginHour)) > 24 || string.toInt(string.getNumberOnly(beginHour)) < 1 ) {
							throw new InvalidationException(evlIemCount + "번째 행 - 평가예정일 시작 시간은 1 ~ 24만 입력 가능합니다.");
						}

						existChk.setBeginHour(beginHour);
					}

					if (!string.isBlank(usptEvlCmitInfo.getEndHour())) {
						String endHour = usptEvlCmitInfo.getEndHour();
						if( string.isBlank(string.getNumberOnly(endHour)) ) {
							throw new InvalidationException(evlIemCount + "번째 행 - 평가예정일 종료 시간 형식 오류입니다.");
						} else if( string.toInt(string.getNumberOnly(endHour)) > 24 || string.toInt(string.getNumberOnly(endHour)) < 1 ) {
							throw new InvalidationException(evlIemCount + "번째 행 - 평가예정일 종료 시간은 1 ~ 24만 입력 가능합니다.");
						}

						existChk.setEndHour(usptEvlCmitInfo.getEndHour());
					}

					if (!string.isBlank(usptEvlCmitInfo.getBeginHour()) && !string.isBlank(usptEvlCmitInfo.getEndHour())) {
						if( string.toInt(string.getNumberOnly(usptEvlCmitInfo.getBeginHour())) > string.toInt(string.getNumberOnly(usptEvlCmitInfo.getEndHour())) ){
							throw new InvalidationException(evlIemCount + "번째 행 - 평가예정일 시작 시간이 종료 시간보다 큽니다.");
						}
					}

					existChk.setEvlPlace(usptEvlCmitInfo.getEvlPlace());
					existChk.setOrgnzrId(usptEvlCmitInfo.getOrgnzrId());
					existChk.setOnline(usptEvlCmitInfo.getOnline());
					existChk.setUpdatedDt(now);
					existChk.setUpdaterId(worker.getMemberId());

					if(usptEvlCmitDao.update(existChk) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					}

				} else { //등록처리
					String insertKey = string.getNewId("evlcmit-");

					usptEvlCmitInfo.setEvlCmitId(insertKey);

					//해당계획에 존재하는 단계인지확인
					if(string.isBlank(usptEvlCmitInfo.getEvlStepId())){
						throw new InvalidationException(evlIemCount + "번째 행 - 평가단계 정보가 없습니다.");
					}

					UsptEvlStep paramStepChk = new UsptEvlStep();
					paramStepChk.setEvlPlanId(evlPlanId);
					paramStepChk.setEvlStepId(usptEvlCmitInfo.getEvlStepId());

					if(usptEvlStepDao.selectExistList(paramStepChk).size() < 1) {
						throw new InvalidationException(evlIemCount + "번째 행의 평가단계는 해당 계획에 존재하지 않습니다.");
					}

					//해당계획에 존재하는 분과인지 확인
					if(string.isBlank(usptEvlCmitInfo.getSectId())){
						throw new InvalidationException(evlIemCount + "번째 행 - 분과 정보가 없습니다.");
					}
					UsptSect paramSectChk = new UsptSect();
					paramSectChk.setEvlPlanId(evlPlanId);
					paramSectChk.setSectId(usptEvlCmitInfo.getSectId());

					if(usptEvlSectDao.selectExistList(paramSectChk).size() < 1) {
						throw new InvalidationException(evlIemCount + "번째 행의 분과는 해당 계획에 존재하지 않습니다.");
					}

					//이미 등록된 위원회인지 체크
					UsptEvlCmit chkParam = new UsptEvlCmit();

					chkParam.setSectId(usptEvlCmitInfo.getSectId());
					chkParam.setEvlStepId(usptEvlCmitInfo.getEvlStepId());

					if(usptEvlCmitDao.selectExistList(chkParam).size() > 0) {
						throw new InvalidationException(evlIemCount + "번째 행의 정보로 이미 등록된 위원회가 존재합니다.");
					}

					usptEvlCmitInfo.setEvlSttusCd(Code.evlSttus.대기중);
					usptEvlCmitInfo.setEvlSttusDt(now);

					usptEvlCmitInfo.setCreatedDt(now);
					usptEvlCmitInfo.setCreatorId(worker.getMemberId());
					usptEvlCmitInfo.setUpdatedDt(now);
					usptEvlCmitInfo.setUpdaterId(worker.getMemberId());

					if(usptEvlCmitDao.insert(usptEvlCmitInfo) != 1) {
						throw new InvalidationException(String.format(Code.validateMessage.DB오류, "등록"));
					}
				}
			} else if (string.equals(_flag, "D")) {
				//위원회 삭제시 처리
				//1.존재여부 확인
				UsptEvlCmit cmitInfo = usptEvlCmitDao.select(usptEvlCmitInfo.getEvlCmitId());

				if(cmitInfo == null) {
					throw new InvalidationException(evlIemCount + "번째 행의 평가위원회는 존재하지 않습니다.");
				}

				//2.위원회 대기중 상태인지 확인
				if(!string.equals(cmitInfo.getEvlSttusCd(),Code.evlSttus.대기중)){
					throw new InvalidationException(evlIemCount + "번째 행 - 평가가 이미 진행중이거나 완료된 위원회 정보는 삭제할 수 없습니다.");
				}

				//3.평가대상자 등록된 상태인지 확인
				if(usptEvlTrgetDao.selectExistChkByCmitId(usptEvlCmitInfo.getEvlCmitId()).size() > 0) {
					throw new InvalidationException(evlIemCount + "번째 행 - 이미 등록된 평가 대상자가 있습니다. 평가 대상자를 삭제한 후 위원회 삭제가 가능합니다.");
				}

				//4.평가위원 추출 여부 확인
				UsptEvlMfcmmExtrc chkParam = new UsptEvlMfcmmExtrc();
				chkParam.setEvlCmitId(usptEvlCmitInfo.getEvlCmitId());
				chkParam.setOdrNo(1);
				EvlCmitExtrcResultDto existMfcmmInfo = usptEvlMfcmmExtrcDao.selectEvlCmitExtrcResult(chkParam);//추출조건 상세 조회

				if (existMfcmmInfo != null) {
					throw new InvalidationException(evlIemCount + "번째 행 - 이미 평가위원 추출이 진행된 위원회는 삭제할 수 없습니다.");
				}

				//5.삭제 처리
				if( usptEvlCmitDao.deleteByCmitId(usptEvlCmitInfo.getEvlCmitId()) != 1 ){
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
				}
			}
		}

		return usptEvlCmitDao.selectList(evlPlanId);
	}


	//이해관계자 목록
	public List<UsptUdstdprcp> getEvlUdstdprcpList(String evlPlanId) {
		return usptUdstdprcpDao.selectList(evlPlanId, null);
	}


	//이해관계자 저장->삭제 기능만
	public List<UsptUdstdprcp> modifyEvlUdstdprcp(String evlPlanId, List<UsptUdstdprcp> usptUdstdprcpList) {
		SecurityUtils.checkWorkerIsInsider();

		if (string.isBlank(evlPlanId) ) {
			throw new InvalidationException("평가계획 정보가 없습니다.");
		}

		UsptEvlPlan existInfo = usptEvlPlanDao.select(evlPlanId);

		if (existInfo == null) {
			throw new InvalidationException("평가계획 정보가 없습니다.");
		} else if (!string.equals(existInfo.getEvlSttusCd(), Code.evlSttus.대기중)) {
			throw new InvalidationException("진행상태가 대기중인 건만 수정이 가능합니다.");
		}

		int evlIemCount = 0;
		String _flag = "";

		for(UsptUdstdprcp UsptUdstdprcpInfo : usptUdstdprcpList) {
			_flag =  UsptUdstdprcpInfo.getFlag();

			if (string.equals(_flag, "D")) {
				evlIemCount++;
			}
		}

		if (evlIemCount < 1 ) {
			throw new InvalidationException("삭제할 정보가 없습니다.");
		}

		//유효체크
		evlIemCount = 0;

		for(UsptUdstdprcp usptUdstdprcpInfo : usptUdstdprcpList) {
			evlIemCount ++;
			_flag =  usptUdstdprcpInfo.getFlag();
			if (string.equals(_flag, "D")) {

				if (string.isBlank(usptUdstdprcpInfo.getUdstdprcpId())) {
					throw new InvalidationException(evlIemCount + "번째 행의 삭제할 정보가 없습니다.");
				}

				int existCnt = usptUdstdprcpDao.selectList(evlPlanId, usptUdstdprcpInfo.getUdstdprcpId()).size();

				if(existCnt < 1) {
					throw new InvalidationException(evlIemCount + "번째 행의 삭제할 대상이 존재하지 않습니다.");
				}
			}
		}

		//이해관계자 삭제처리
		for(UsptUdstdprcp usptUdstdprcpInfo : usptUdstdprcpList) {
			_flag =  usptUdstdprcpInfo.getFlag();
			if (string.equals(_flag, "D")) {
				usptUdstdprcpDao.delete(usptUdstdprcpInfo.getUdstdprcpId());
			}
		}

		return usptUdstdprcpDao.selectList(evlPlanId, null);
	}

	//이해관계자 엑셀업로드
	public List<UsptUdstdprcp> modifyEvlUdstdprcpByExcel(String evlPlanId, List<UsptUdstdprcp> usptUdstdprcpList) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		int evlIemCount = 0;

		//유효체크
		evlIemCount = 0;
		//TODO : (임병진) 이해관계자 등록시 유효체크 추가 확인 필요함-> 임의 처리함
		for(UsptUdstdprcp usptUdstdprcpInfo : usptUdstdprcpList) {
			evlIemCount ++;


			//1.입력값 체크
			String bizrno = usptUdstdprcpInfo.getBizrno().replaceAll("-", "");

			if(!CoreUtils.validation.isCompanyNumber(bizrno)) {
				throw new InvalidationException("올바르지 않은 사업자번호입니다.");
			}

//			if( !string.equals(usptUdstdprcpInfo.getEncMbtlnum().substring(0,2), "01") || !string.isNumeric(usptUdstdprcpInfo.getEncMbtlnum().replace("-","")) || usptUdstdprcpInfo.getEncMbtlnum().replace("-","").length() > 11  || usptUdstdprcpInfo.getEncMbtlnum().replace("-","").length() < 10 ){
//				throw new InvalidationException("휴대폰번호의 형식이 올바르지 않습니다.");
//			}

			if( !string.isNumeric(usptUdstdprcpInfo.getEncBrthdy().replace("-","")) || string.getNumberOnly(usptUdstdprcpInfo.getEncBrthdy()).length() != 8 ){
				throw new InvalidationException("생년월일의 형식이 올바르지 않습니다.");
			}


			//기존 등록여부 체크(이름, 생년월일, 성별, 휴대폰)
			String chkNm = usptUdstdprcpInfo.getNm();
			String chkBirth = usptUdstdprcpInfo.getEncBrthdy();
			String chkGender = usptUdstdprcpInfo.getGenderCd();
			String chkMbtlnum = usptUdstdprcpInfo.getEncMbtlnum();

			List<UsptUdstdprcp> existList =  usptUdstdprcpDao.selectList(evlPlanId, null);

			for(UsptUdstdprcp existInfo : existList) {
				log.debug("existInfo.getBrthdy() = " + usptUdstdprcpInfo.getBrthdy());
				log.debug("existInfo.getMbtlnum() = " + usptUdstdprcpInfo.getMbtlnum());
				String existNm = existInfo.getNm();
				String existBirth = existInfo.getBrthdy();
				String existGender = existInfo.getGenderCd();
				String existMbtlnum = existInfo.getMbtlnum();

				if( string.equals(chkNm, existNm) &&  string.equals(chkBirth, existBirth) && string.equals(chkGender, existGender) && string.equals(chkMbtlnum, existMbtlnum) ) {
					throw new InvalidationException(evlIemCount + "행의 정보는 이미 등록된 이해관계자입니다.");
				}
			}
		}

		List<UsptUdstdprcp> list = new ArrayList<>();

		for(UsptUdstdprcp usptUdstdprcpInfo : usptUdstdprcpList) {
			String bizrno = usptUdstdprcpInfo.getBizrno().replaceAll("-", "");
			usptUdstdprcpInfo.setEvlPlanId(evlPlanId);
			String insertKey = string.getNewId("evludstdprcp-");
			usptUdstdprcpInfo.setUdstdprcpId(insertKey);

			//생년월일 암호화
			usptUdstdprcpInfo.setEncBrthdy(CoreUtils.aes256.encrypt(usptUdstdprcpInfo.getEncBrthdy(), insertKey));
			//휴대폰 암호화
			usptUdstdprcpInfo.setEncMbtlnum(CoreUtils.aes256.encrypt(usptUdstdprcpInfo.getEncMbtlnum(), insertKey));

			usptUdstdprcpInfo.setCreatedDt(now);
			usptUdstdprcpInfo.setCreatorId(worker.getMemberId());
			usptUdstdprcpInfo.setUpdatedDt(now);
			usptUdstdprcpInfo.setUpdaterId(worker.getMemberId());
			usptUdstdprcpInfo.setBizrno(bizrno);

			list.add(usptUdstdprcpInfo);

		}

		usptUdstdprcpDao.insertList(list); //등록

		return usptUdstdprcpDao.selectList(evlPlanId, null);
	}

	//이해관계자 개별등록
	public UsptUdstdprcp addEvlUdstdprcp(UsptUdstdprcp usptUdstdprcp) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException exs = new InvalidationsException();

		//유효체크
		if (string.isBlank(usptUdstdprcp.getNm())) {
			exs.add("nm", "이름을 입력해주세요.");
		}

		if (string.isBlank(usptUdstdprcp.getEncBrthdy())) {
			exs.add("encBrthdy", "생년월일을 입력해주세요.");
		}

		if (exs.size() > 0) {
			throw exs;
		}

		if (string.isBlank(usptUdstdprcp.getEvlPlanId())) {
			throw new InvalidationException("평가계획 정보가 없습니다.");
		}

		UsptEvlPlan existChk = usptEvlPlanDao.select(usptUdstdprcp.getEvlPlanId());

		if (existChk == null) {
			throw new InvalidationException("평가계획 정보가 없습니다.");
		} else if (!string.equals(existChk.getEvlSttusCd(), Code.evlSttus.대기중)) {
			throw new InvalidationException("진행상태가 대기중인 건만 수정이 가능합니다.");
		}

		//이해관계자 등록 처리
		//1.입력값 체크
		String bizrno = usptUdstdprcp.getBizrno().replaceAll("-", "");

		if(!string.isBlank(bizrno)) {
			if(!CoreUtils.validation.isCompanyNumber(bizrno)) {
				throw new InvalidationException("올바르지 않은 사업자번호입니다.");
			}
		}

		if( !string.equals(usptUdstdprcp.getEncMbtlnum().substring(0,2), "01") || !string.isNumeric(usptUdstdprcp.getEncMbtlnum().replace("-","")) || usptUdstdprcp.getEncMbtlnum().replace("-","").length() > 11  || usptUdstdprcp.getEncMbtlnum().replace("-","").length() < 10 ){
			throw new InvalidationException("휴대폰번호의 형식이 올바르지 않습니다.");
		}

		if( !string.isNumeric(usptUdstdprcp.getEncBrthdy().replace("-","")) || string.getNumberOnly(usptUdstdprcp.getEncBrthdy()).length() != 8 ){
			throw new InvalidationException("생년월일의 형식이 올바르지 않습니다.");
		}

		//2.기존 등록여부 체크(이름, 생년월일, 성별, 휴대폰)
		String chkNm = usptUdstdprcp.getNm();
		String chkBirth = usptUdstdprcp.getEncBrthdy().replaceAll("-", "");
		String chkGender = usptUdstdprcp.getGenderCd();
		String chkMbtlnum = usptUdstdprcp.getEncMbtlnum().replaceAll("-", "");

		List<UsptUdstdprcp> existList =  usptUdstdprcpDao.selectList(usptUdstdprcp.getEvlPlanId(), null);

		for(UsptUdstdprcp existInfo : existList) {
			String existNm = existInfo.getNm();
			String existBirth = existInfo.getBrthdy();
			String existGender = existInfo.getGenderCd();
			String existMbtlnum = existInfo.getMbtlnum();

			if( string.equals(chkNm, existNm) &&  string.equals(chkBirth, existBirth) && string.equals(chkGender, existGender) && string.equals(chkMbtlnum, existMbtlnum) ) {
				throw new InvalidationException("입력하신 정보는 이미 등록된 이해관계자입니다.");
			}
		}

		String newUdstdprcpId = string.getNewId("evludstdprcp-");
		usptUdstdprcp.setUdstdprcpId(newUdstdprcpId);
		usptUdstdprcp.setEvlPlanId(usptUdstdprcp.getEvlPlanId());

		//생년월일 암호화
		usptUdstdprcp.setEncBrthdy(CoreUtils.aes256.encrypt(usptUdstdprcp.getEncBrthdy().replaceAll("-", ""), newUdstdprcpId));
		//휴대폰 암호화
		usptUdstdprcp.setEncMbtlnum(CoreUtils.aes256.encrypt(usptUdstdprcp.getEncMbtlnum().replaceAll("-", ""), newUdstdprcpId));

		usptUdstdprcp.setCreatedDt(now);
		usptUdstdprcp.setCreatorId(worker.getMemberId());
		usptUdstdprcp.setUpdatedDt(now);
		usptUdstdprcp.setUpdaterId(worker.getMemberId());
		usptUdstdprcp.setBizrno(bizrno);

		List<UsptUdstdprcp> list = new ArrayList<>();

		list.add(usptUdstdprcp);

		usptUdstdprcpDao.insertList(list); //등록

		return usptUdstdprcpDao.selectList(usptUdstdprcp.getEvlPlanId(), newUdstdprcpId).get(0);
	}


	//평가단계 평가자료 목록
	public List<UsptEvlCmitDta> getEvlCmitDtaList(String evlStepId) {

		List<UsptEvlCmitDta> resultList = new ArrayList<>();

		resultList = usptEvlCmitDao.selectCmitDtaList(evlStepId);

		for(UsptEvlCmitDta resultDto : resultList) {
			resultDto.setAttachmentList(attachmentService.getFileInfos_group(resultDto.getAttachmentGroupId()));
		}

		return resultList;

	}


	//평가계획 평가자료 저장
	public UsptEvlCmitDta modifyEvlCmitDta(String evlStepId, String evlTrgetId, List<MultipartFile> attachments) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		//유효체크 처리
		InvalidationsException exs = new InvalidationsException();

		if (exs.size() > 0) {
			throw exs;
		}

		//첨부파일
		//평가위원회 첨부파일이 이미 있었는지 확인하기 attachmentGroupId 조회하기 -> 재확인 필요
		String attachmentGroupId = usptEvlCmitDao.selectCmitDtaInfo(evlTrgetId);

		if (attachments != null && attachments.size() > 0) {
			String[] exts = {"txt","ppt","xlsx","pdf"};	//TODO : (임병진) 임시처리
			long size = 1000 * (1024 * 1024);	//TODO : (임병진) 임시처리

			if (string.isBlank(attachmentGroupId)) {
				CmmtAtchmnflGroup attGroup = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), attachments, exts, size);
				attachmentGroupId = attGroup.getAttachmentGroupId();
			} else {
				attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), attachmentGroupId, attachments, exts, size);
			}
		}

		//첨부파일 그룹ID 저장 처리
		UsptEvlCmitDta usptEvlCmitDta = new UsptEvlCmitDta();

		usptEvlCmitDta.setEvlTrgetId(evlTrgetId);//평가대상ID
		usptEvlCmitDta.setAttachmentGroupId(attachmentGroupId);//첨부파일그룹ID
		usptEvlCmitDta.setUpdaterId(worker.getMemberId());
		usptEvlCmitDta.setUpdatedDt(now);

		usptEvlCmitDao.updateCmitAttachInfo(usptEvlCmitDta);

		return usptEvlCmitDta;//TODO : (임병진) 리턴할 데이터를 추후 그룹ID만 넘기면 될지 확인 후 처리 예정
	}


	//평가계획 평가대상 분과 목록
	public List<UsptSect> getEvlSectList(String evlPlanId) {
		return usptEvlSectDao.selectList(evlPlanId);
	}

	//평가계획 평가대상 불러오기 목록
	public CorePagination<UsptEvlTrget> getEvlTargetist(UsptEvlTrget usptEvlTrgetParam, Long page) {
		if(page == null) {
			page = 1L;
		}

		if(usptEvlTrgetParam.getItemsPerPage() == null) {
			usptEvlTrgetParam.setItemsPerPage(ITEMS_PER_PAGE);;
		}

		if(string.isBlank(usptEvlTrgetParam.getPblancId())) {
			throw new InvalidationException("공고ID는 필수 입력입니다.");
		}

		if(string.isBlank(usptEvlTrgetParam.getEvlPlanId())) {
			throw new InvalidationException("평가계획ID는 필수 입력입니다.");
		}

		long totalItems = usptEvlTrgetDao.selectListCount(usptEvlTrgetParam);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, usptEvlTrgetParam.getItemsPerPage(), totalItems);

		usptEvlTrgetParam.setBeginRowNum(info.getBeginRowNum());

		List<UsptEvlTrget> list = usptEvlTrgetDao.selectList(usptEvlTrgetParam);

		//출력할 자료 생성 후 리턴
		CorePagination<UsptEvlTrget> pagination = new CorePagination<>(info, list);

		return pagination;

	}


	//평가계획 평가대상 분과별 대상목록
	public CorePagination<UsptEvlTrget> getEvlTargetBySectList(UsptEvlTrget usptEvlTrgetParam, Long page) {
		if(page == null) {
			page = 1L;
		}

		if(usptEvlTrgetParam.getItemsPerPage() == null) {
			usptEvlTrgetParam.setItemsPerPage(ITEMS_PER_PAGE);
		}

		long totalItems = usptEvlTrgetDao.selectListBySectCount(usptEvlTrgetParam);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, usptEvlTrgetParam.getItemsPerPage(), totalItems);

		usptEvlTrgetParam.setBeginRowNum(info.getBeginRowNum());
		List<UsptEvlTrget> list = usptEvlTrgetDao.selectListBySect(usptEvlTrgetParam);

		//출력할 자료 생성 후 리턴
		CorePagination<UsptEvlTrget> pagination = new CorePagination<>(info, list);

		return pagination;

	}

	//평가계획 평가대상 분과별 대상 저장
	public List<UsptEvlTrget> modifyEvlTargetBySectList(String evlPlanId, List<UsptEvlTrget> usptEvlTrgetList) {
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		InvalidationsException exs = new InvalidationsException();

		int evlIemCount = 0;
		String _flag = "";

		if(usptEvlTrgetList.size() < 1) {
			exs.add("변경할 정보가 없습니다.");
		}

		if(string.isBlank(evlPlanId)) {
			exs.add("변경할 평가계획 정보가 없습니다.");
		}

		UsptEvlPlan existInfo = usptEvlPlanDao.select(evlPlanId);

		if (existInfo == null) {
			throw new InvalidationException("존재하지 않는 평가계획 입니다.");
		} else if (!string.equals(existInfo.getEvlSttusCd(), Code.evlSttus.대기중)) {
			throw new InvalidationException("진행상태가 대기중인 건만 수정이 가능합니다.");
		}

		//대상 정보 유효체크
		for(UsptEvlTrget usptEvlTrgetListInfo : usptEvlTrgetList) {
			usptEvlTrgetListInfo.setEvlPlanId(evlPlanId);//평가계획ID 세팅
			_flag = usptEvlTrgetListInfo.getFlag();

			//if(usptEvlTrgetListInfo.getCheck()) {
			//삭제와 추가만 있음
			if (string.equals(_flag, "I") || string.equals(_flag, "D") ) {
				evlIemCount++;

				//등록인경우 필수 체크
				if (string.equals(_flag, "I") ) {
					if (string.isBlank(usptEvlTrgetListInfo.getApplyId())) {
						exs.add("applyId", evlIemCount + "번째 행의 신청정보가 없습니다.");
					}

					if (string.isBlank(usptEvlTrgetListInfo.getSectId())) {
						exs.add("sectId", evlIemCount + "번째 행의 분과정보가 없습니다.");
					}

					//이미 해당계획에 대상자로 등록된 신청자인지 확인
					if(usptEvlTrgetDao.selectExistChkByPlanId(usptEvlTrgetListInfo).size() > 0) {
						exs.add("applyId", evlIemCount + "번째 행 - 이미 대상자로 등록된 신청자입니다.");
					}

				}

				//삭제인경우 삭제대상 존재 여부 확인
				if (string.equals(_flag, "D") ) {
					if (string.isBlank(usptEvlTrgetListInfo.getEvlTrgetId())) {
						exs.add("evlTrgetId", evlIemCount + "번째 행의 삭제할 대상의 정보가 없습니다.");
					}

					long existCount = usptEvlTrgetDao.selectListBySectCount(usptEvlTrgetListInfo);

					if(existCount < 1) {
						exs.add("applyId", evlIemCount + "번째 행의 삭제할 대상의 정보가 존재하지 않습니다.");
					}
				}
			}
			//}//check
		}

		log.debug("evlIemCount = {}", evlIemCount);

		if(evlIemCount < 1) {
			exs.add("변경할 정보가 없습니다.");
		}

		if (exs.size() > 0) {
			throw exs;
		}

		//평가 대상 등록 처리
		for(UsptEvlTrget usptEvlTrgetListInfo : usptEvlTrgetList) {
			_flag =  usptEvlTrgetListInfo.getFlag();

			usptEvlTrgetListInfo.setCreatedDt(now);
			usptEvlTrgetListInfo.setCreatorId(worker.getMemberId());
			usptEvlTrgetListInfo.setUpdatedDt(now);
			usptEvlTrgetListInfo.setUpdaterId(worker.getMemberId());

			if (string.equals(_flag, "I")) {
				//해당 평가계획의 단계ID를 구해서 여러건 등록해야함.->
				//TODO : (임병진) 첫번째 단계에만 넣는것으로 수정, 추후 최종선정 완료시 다음단계에 대상자 넣어야함

				List<UsptEvlStep> usptEvlStepListChk = usptEvlStepDao.selectList(evlPlanId, "1");

				if(usptEvlStepListChk.size() < 1) {
					throw new InvalidationException("평가 단계 등록을 먼저 진행해주세요.");
				}

				for(UsptEvlStep usptEvlStepInfo : usptEvlStepListChk) {

					//기존등록 유무 체크
					UsptEvlTrget existChkParam = new UsptEvlTrget();

					existChkParam.setApplyId(usptEvlTrgetListInfo.getApplyId());
					existChkParam.setSectId(usptEvlTrgetListInfo.getSectId());
					existChkParam.setEvlStepId(usptEvlStepInfo.getEvlStepId());

					UsptEvlTrget existTargetInfo = usptEvlTrgetDao.selectExistChk(existChkParam);

					if(existTargetInfo == null) {
						usptEvlTrgetListInfo.setEvlPlanId(evlPlanId);
						usptEvlTrgetListInfo.setEvlTrgetId(string.getNewId("evltrget-"));
						usptEvlTrgetListInfo.setEvlStepId(usptEvlStepInfo.getEvlStepId());

						usptEvlTrgetDao.insert(usptEvlTrgetListInfo);
					}
				}

			}else if(string.equals(_flag, "D")) {
				//평가대상id에 해당하는 같은 분과의 평가단계의 대상 모두 삭제(multi)-> 해당하는 평가대상ID 직접삭제하면 됨으로 수정
				//usptEvlTrgetListInfo.setEvlPlanId(evlPlanId);

				if( string.isNotBlank(usptEvlTrgetListInfo.getEvlTrgetId()) ){
					usptEvlTrgetDao.delete(usptEvlTrgetListInfo);
				}
			}
		}

		return usptEvlTrgetList;
	}


	//공고 선택 팝업 차수 목록
	public CorePagination<UsptBsnsPblancRcept> getPblancRcepttist(UsptBsnsPblancRcept usptBsnsPblancRcept, Long page) {
		if(page == null) {
			page = 1L;
		}

		if(usptBsnsPblancRcept.getItemsPerPage() == null) {
			usptBsnsPblancRcept.setItemsPerPage(ITEMS_PER_PAGE);
		}

		long totalItems = usptBsnsPblancRceptDao.selectListCount(usptBsnsPblancRcept.getPblancId());

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, usptBsnsPblancRcept.getItemsPerPage(), totalItems);

		usptBsnsPblancRcept.setBeginRowNum(info.getBeginRowNum());
		List<UsptBsnsPblancRcept> list = usptBsnsPblancRceptDao.selectList(usptBsnsPblancRcept);

		//출력할 자료 생성 후 리턴
		CorePagination<UsptBsnsPblancRcept> pagination = new CorePagination<>(info, list);

		return pagination;
	}

	private void validChkPblanc(String pblancId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		BsnsPblancListParam bsnsPblancListParam = new BsnsPblancListParam();
		bsnsPblancListParam.setPblancId(pblancId);
		bsnsPblancListParam.setJobStepCd(Code.jobStep.평가선정);
		bsnsPblancListParam.setInsiderId(worker.getMemberId());

		bsnsPblancListParam.setIsExcel(false);
		long totalItems = usptBsnsPblancDao.selectListCount(bsnsPblancListParam);

		if(totalItems < 1 ) {
			throw new InvalidationException("해당 공고는 존재하지 않거나 평가를 진행할 수 없는 공고입니다. \n해당 사업의 업무단계를 확인해주세요");
		}

	}


	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param evlCmitId
	 * @param info
	 */
	public void downloadStepAtchmnfl(HttpServletResponse response, String evlStepId, CmmtAtchmnfl info) {
		SecurityUtils.checkWorkerIsInsider();

		UsptEvlStep paramStepChk = new UsptEvlStep();
		paramStepChk.setEvlStepId(evlStepId);

		List<UsptEvlStep> stepInfo = usptEvlStepDao.selectExistList(paramStepChk);

		if(stepInfo.size() < 1) {
			throw new InvalidationException("요청하신 정보에 해당하는 평가 단계정보가 없습니다.");
		}
		if(CoreUtils.string.isBlank(info.getAttachmentId())) {
			throw new InvalidationException("첨부파일ID를 입력해주세요");
		}

		attachmentService.downloadFile(response, config.getDir().getUpload(), info.getAttachmentId());
	}


	/**
	 * 첨부파일 일괄다운로드
	 * @param response
	 * @param evlCmitId
	 */
	public void downloadStepAllAtchmnfl(HttpServletResponse response, String evlStepId) {
		SecurityUtils.checkWorkerIsInsider();

		UsptEvlStep paramStepChk = new UsptEvlStep();
		paramStepChk.setEvlStepId(evlStepId);

		List<UsptEvlStep> stepInfo = usptEvlStepDao.selectExistList(paramStepChk);

		if(stepInfo.size() < 1) {
			throw new InvalidationException("요청하신 정보에 해당하는 평가 단계정보가 없습니다.");
		}

		List<UsptEvlCmitDta> resultList = new ArrayList<>();

		resultList = usptEvlCmitDao.selectCmitDtaList(evlStepId);


		if(resultList.size() < 1) {
			throw new InvalidationException("다운로드 가능한 첨부파일이 없습니다.");
		}

		int totalSize = 0;
		for(UsptEvlCmitDta resultDto : resultList) {
			String attachmentGroupId = resultDto.getAttachmentGroupId();

			List<CmmtAtchmnfl> allList = attachmentDao.selectList_notDeleted(attachmentGroupId);
			totalSize += allList.size();
		}

		String[] attachmentIds = new String[totalSize];

		for(UsptEvlCmitDta resultDto : resultList) {
			String attachmentGroupId = resultDto.getAttachmentGroupId();

			List<CmmtAtchmnfl> allList = attachmentDao.selectList_notDeleted(attachmentGroupId);
			int i = 0;
			for (CmmtAtchmnfl att : allList) {
				attachmentIds[i++] = att.getAttachmentId();
			}
		}

		attachmentService.downloadFiles(response, config.getDir().getUpload(), attachmentIds, "평가자료_첨부파일");

		//attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), evlCmit.getAttachmentGroupId(), "온라인평가_첨부파일");
	}

//	private void removeCmitInfo(String evlCmitId) {
//		//추출위원 상담 이력 삭제 USPT_CNSLT_HIST
//		usptCnsltHistDao.deleteByCmitId(evlCmitId);
//
//		//평가위원 삭제 USPT_EVL_MFCMM
//		usptEvlMfcmmDao.deleteByCmitId(evlCmitId);
//
//		//추출워원 삭제 USPT_EXTRC_MFCMM
//		usptExtrcMfcmmDao.deleteByCmitId(evlCmitId);
//
//		//제외위원 삭제 USPT_EXCL_MFCMM
//		usptExclMfcmmDao.deleteByCmitId(evlCmitId);
//
//		//평가위원추출 삭제 USPT_EVL_MFCMM_EXTRC
//		usptEvlMfcmmExtrcDao.deleteByCmitId(evlCmitId);
//
//		//평가위원회 삭제 USPT_EVL_CMIT
//		usptEvlCmitDao.deleteByCmitId(evlCmitId);
//
//	}

//	private void removeCmitTargetInfo(String sectId) {
//		//평가대상 삭제 USPT_EVL_TRGET - sectId 단위
//		usptEvlTrgetDao.deleteBySectId(sectId);
//
//		//평가단계 삭제 USPT_EVL_STEP -sectId 단위, sectId에 해당하는 평가단계 찾아서 삭제
//		usptEvlStepDao.deleteBySectId(sectId);
//
//		//평가분과 삭제 처리- sectId 단위
//		usptEvlSectDao.delete(sectId);
//
//		log.debug("강제 오류처리");
//		throw new InvalidationException("강제 오류처리");
//	}

}
