package aicluster.pms.api.bsns.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.bsns.dto.ApplyMberType;
import aicluster.pms.api.bsns.dto.AtchmnflSetup;
import aicluster.pms.api.bsns.dto.BhExmntIem;
import aicluster.pms.api.bsns.dto.Chklst;
import aicluster.pms.api.bsns.dto.IoeSetupParam;
import aicluster.pms.api.bsns.dto.RsltIdx;
import aicluster.pms.api.bsns.dto.RsltIdxDetailIem;
import aicluster.pms.api.bsns.dto.RsltIdxStdIem;
import aicluster.pms.api.bsns.dto.StdrBsnsBasicDto;
import aicluster.pms.api.bsns.dto.StdrBsnsBasicParam;
import aicluster.pms.api.bsns.dto.StdrBsnsListParam;
import aicluster.pms.api.bsns.dto.StdrBsnsTaskDto;
import aicluster.pms.common.dao.UsptBsnsClDao;
import aicluster.pms.common.dao.UsptStdrApplyMberTypeDao;
import aicluster.pms.common.dao.UsptStdrApplyRealmDao;
import aicluster.pms.common.dao.UsptStdrAppnTaskDao;
import aicluster.pms.common.dao.UsptStdrAtchmnflSetupDao;
import aicluster.pms.common.dao.UsptStdrBhExmntDao;
import aicluster.pms.common.dao.UsptStdrBhExmntIemDao;
import aicluster.pms.common.dao.UsptStdrBsnsDao;
import aicluster.pms.common.dao.UsptStdrChargerDao;
import aicluster.pms.common.dao.UsptStdrChklstDao;
import aicluster.pms.common.dao.UsptStdrIoeSetupDao;
import aicluster.pms.common.dao.UsptStdrJobStepDao;
import aicluster.pms.common.dao.UsptStdrRecomendClDao;
import aicluster.pms.common.dao.UsptStdrRsltIdxDao;
import aicluster.pms.common.dao.UsptStdrRsltIdxDetailIemDao;
import aicluster.pms.common.dao.UsptStdrRsltIdxStdIemDao;
import aicluster.pms.common.dao.UsptStdrTaxitmSetupDao;
import aicluster.pms.common.dto.ApplyLimitDto;
import aicluster.pms.common.dto.BhExmntDto;
import aicluster.pms.common.dto.IoeSetupDto;
import aicluster.pms.common.dto.JobStepDto;
import aicluster.pms.common.dto.StdrBsnsListDto;
import aicluster.pms.common.entity.UsptStdrApplyMberType;
import aicluster.pms.common.entity.UsptStdrApplyRealm;
import aicluster.pms.common.entity.UsptStdrAppnTask;
import aicluster.pms.common.entity.UsptStdrAtchmnflSetup;
import aicluster.pms.common.entity.UsptStdrBhExmnt;
import aicluster.pms.common.entity.UsptStdrBhExmntIem;
import aicluster.pms.common.entity.UsptStdrBsns;
import aicluster.pms.common.entity.UsptStdrCharger;
import aicluster.pms.common.entity.UsptStdrChklst;
import aicluster.pms.common.entity.UsptStdrIoeSetup;
import aicluster.pms.common.entity.UsptStdrJobStep;
import aicluster.pms.common.entity.UsptStdrRecomendCl;
import aicluster.pms.common.entity.UsptStdrRsltIdx;
import aicluster.pms.common.entity.UsptStdrRsltIdxDetailIem;
import aicluster.pms.common.entity.UsptStdrRsltIdxStdIem;
import aicluster.pms.common.entity.UsptStdrTaxitmSetup;
import aicluster.pms.common.entity.UsptWctTaxitm;
import aicluster.pms.common.util.Code;
import aicluster.pms.common.util.Code.taskType;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class StdrBsnsService {

	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private UsptStdrBsnsDao usptStdrBsnsDao;
	@Autowired
	private UsptStdrChargerDao usptStdrChargerDao;
	@Autowired
	private UsptStdrRecomendClDao usptStdrRecomendClDao;
	@Autowired
	private UsptStdrAppnTaskDao usptStdrAppnTaskDao;
	@Autowired
	private UsptStdrApplyRealmDao usptStdrApplyRealmDao;
	@Autowired
	private UsptStdrJobStepDao usptStdrJobStepDao;
	@Autowired
	private UsptStdrApplyMberTypeDao usptStdrApplyMberTypeDao;
	@Autowired
	private UsptStdrChklstDao usptStdrChklstDao;
	@Autowired
	private UsptStdrBhExmntDao usptStdrBhExmntDao;
	@Autowired
	private UsptStdrBhExmntIemDao usptStdrBhExmntIemDao;
	@Autowired
	private UsptStdrIoeSetupDao usptStdrIoeSetupDao;
	@Autowired
	private UsptStdrTaxitmSetupDao usptStdrTaxitmSetupDao;
	@Autowired
	private UsptStdrRsltIdxDao usptStdrRsltIdxDao;
	@Autowired
	private UsptStdrRsltIdxDetailIemDao usptStdrRsltIdxDetailIemDao;
	@Autowired
	private UsptStdrRsltIdxStdIemDao usptStdrRsltIdxStdIemDao;
	@Autowired
	private UsptStdrAtchmnflSetupDao usptStdrAtchmnflSetupDao;
	@Autowired
	private UsptBsnsClDao usptBsnsClDao;

	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;


	/**
	 *
	 * @param stdrBsns
	 * @return
	 */
	protected InvalidationsException validateBasicInfo(StdrBsnsBasicParam stdrBsns) {
		InvalidationsException ies = new InvalidationsException();
		if(CoreUtils.string.isEmpty(stdrBsns.getBsnsClId())) {
			ies.add("bsnsClId", String.format(Code.validateMessage.입력없음, "사업분류"));
		}
		if(CoreUtils.string.isEmpty(stdrBsns.getStdrBsnsNm())) {
			ies.add("bsnsNm", String.format(Code.validateMessage.입력없음, "기준사업명"));
		}
		if(CoreUtils.string.isEmpty(stdrBsns.getBeginYear())) {
			ies.add("beginYear", String.format(Code.validateMessage.입력없음, "시작년도"));
		}
		if(CoreUtils.string.isEmpty(stdrBsns.getBsnsTypeCd())) {
			ies.add("bsnsTypeCd", String.format(Code.validateMessage.입력없음, "사업유형"));
		}
		if(stdrBsns.getTotBsnsPd() == 0) {
			ies.add("totBsnsPd", String.format(Code.validateMessage.입력없음, "총사업기간"));
		}
		if(CoreUtils.string.isEmpty(stdrBsns.getChrgDeptNm())) {
			ies.add("chrgDeptNm", String.format(Code.validateMessage.입력없음, "담당부서"));
		}
		if(stdrBsns.getStdrRecomendClList() == null || stdrBsns.getStdrRecomendClList().isEmpty()) {
			ies.add("stdrRecomendClList", String.format(Code.validateMessage.입력없음, "추천분류 정보"));
		}
		if(stdrBsns.getStdrChargerList() == null || stdrBsns.getStdrChargerList().isEmpty()) {
			ies.add("stdrChargerList", String.format(Code.validateMessage.입력없음, "담당자 정보"));
		}
		return ies;
	}

	/**
	 * 기준사업 목록 조회
	 * @param stdrBsnsListParam
	 * @param page
	 * @return
	 */
	public CorePagination<StdrBsnsListDto> getList(StdrBsnsListParam stdrBsnsListParam, Long page){
		SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(stdrBsnsListParam.getItemsPerPage() == null) {
			stdrBsnsListParam.setItemsPerPage(ITEMS_PER_PAGE);
		}

		stdrBsnsListParam.setIsExcel(false);
		long totalItems = usptStdrBsnsDao.selectListCount(stdrBsnsListParam);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, stdrBsnsListParam.getItemsPerPage(), totalItems);
		stdrBsnsListParam.setBeginRowNum(info.getBeginRowNum());
		List<StdrBsnsListDto> list = usptStdrBsnsDao.selectList(stdrBsnsListParam);

		//출력할 자료 생성 후 리턴
		CorePagination<StdrBsnsListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 사업 등록을 위한 기준사업 목록 조회
	 * @param stdrBsnsListParam
	 * @param page
	 * @return
	 */
	public CorePagination<StdrBsnsListDto> getListForBsns(StdrBsnsListParam stdrBsnsListParam, Long page){
		SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(stdrBsnsListParam.getItemsPerPage() == null) {
			stdrBsnsListParam.setItemsPerPage(ITEMS_PER_PAGE);
		}

		stdrBsnsListParam.setIsExcel(false);
		long totalItems = usptStdrBsnsDao.selectListCountForBsns(stdrBsnsListParam);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, stdrBsnsListParam.getItemsPerPage(), totalItems);
		stdrBsnsListParam.setBeginRowNum(info.getBeginRowNum());
		List<StdrBsnsListDto> list = usptStdrBsnsDao.selectListForBsns(stdrBsnsListParam);

		//출력할 자료 생성 후 리턴
		CorePagination<StdrBsnsListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 기준사업관리 목록 엑셀 저장
	 * @param stdrBsnsListParam
	 * @return
	 */
	public List<StdrBsnsListDto> getListExcelDwld(StdrBsnsListParam stdrBsnsListParam){
		SecurityUtils.checkWorkerIsInsider();
		stdrBsnsListParam.setIsExcel(true);
		return usptStdrBsnsDao.selectList(stdrBsnsListParam);
	}


	/**
	 * 기준사업 저장
	 * @param stdrBsns
	 * @return
	 */
	public String add(StdrBsnsBasicParam stdrBsns) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(stdrBsns == null) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "기준사업 정보"));
		}

		InvalidationsException ies = validateBasicInfo(stdrBsns);
		if (ies.size() > 0) {
			throw ies;
		}

		Date now = new Date();
		stdrBsns.setCreatedDt(now);
		stdrBsns.setCreatorId(worker.getMemberId());
		stdrBsns.setUpdatedDt(now);
		stdrBsns.setUpdaterId(worker.getMemberId());
		/** 과제유형을 기본값으로 저장 */
		stdrBsns.setTaskTypeCd(taskType.자유과제);
		usptStdrBsnsDao.insert(stdrBsns);

		for(UsptStdrRecomendCl info : stdrBsns.getStdrRecomendClList()) {
			info.setStdrBsnsCd(stdrBsns.getStdrBsnsCd());
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			usptStdrRecomendClDao.insert(info);
		}

		List<UsptStdrCharger> chkChargerList = stdrBsns.getStdrChargerList().stream().filter(x -> x.getReprsntCharger() != null && x.getReprsntCharger() == true).collect(Collectors.toList());
		if(chkChargerList == null || chkChargerList.size() != 1) {
			throw new InvalidationException("대표담당자는 필수이고 한명만 선택해주세요");
		}
		UsptStdrCharger chargerInfo = chkChargerList.get(0);
		if(!CoreUtils.string.equals(chargerInfo.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("대표담당자는 실무담당자(수정권한)에만 설정 가능합니다.");
		}

		for(UsptStdrCharger info : stdrBsns.getStdrChargerList()) {
			info.setStdrBsnsCd(stdrBsns.getStdrBsnsCd());
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			info.setUpdatedDt(now);
			info.setUpdaterId(worker.getMemberId());
			if(info.getReprsntCharger() == null)
				info.setReprsntCharger(false);
			usptStdrChargerDao.insert(info);
		}
		return stdrBsns.getStdrBsnsCd();
	}


	/**
	 * 기본정보 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	public StdrBsnsBasicDto getBasicInfo(String stdrBsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		StdrBsnsBasicDto basicDto = new StdrBsnsBasicDto();
		UsptStdrBsns stdrBsnsInfo = usptStdrBsnsDao.select(stdrBsnsCd);
		if(stdrBsnsInfo == null) {
			throw new InvalidationException("요청하신 기준사업 정보가 없습니다.");
		}
		BeanUtils.copyProperties(stdrBsnsInfo, basicDto);
		basicDto.setBsnsClNm(usptBsnsClDao.selectBsnsClNm(stdrBsnsInfo.getBsnsClId()));
		basicDto.setStdrChargerList(usptStdrChargerDao.selectList(stdrBsnsCd));
		basicDto.setStdrRecomendClList(usptStdrRecomendClDao.selectList(stdrBsnsCd));
		return basicDto;
	}


	/**
	 * 기본정보 수정
	 * @param stdrBsnsCd
	 * @param stdrBsnsBasicParam
	 */
	public void modifyBasicInfo(String stdrBsnsCd, StdrBsnsBasicParam stdrBsnsBasicParam) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(stdrBsnsBasicParam == null) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "기준사업 정보"));
		}

		InvalidationsException ies = validateBasicInfo(stdrBsnsBasicParam);
		if (ies.size() > 0) {
			throw ies;
		}

		Date now = new Date();
		stdrBsnsBasicParam.setStdrBsnsCd(stdrBsnsCd);
		stdrBsnsBasicParam.setUpdatedDt(now);
		stdrBsnsBasicParam.setUpdaterId(worker.getMemberId());
		usptStdrBsnsDao.update(stdrBsnsBasicParam);

		/** 추천 분류 저장 */
		usptStdrRecomendClDao.deleteAll(stdrBsnsCd);
		for(UsptStdrRecomendCl info : stdrBsnsBasicParam.getStdrRecomendClList()) {
			info.setStdrBsnsCd(stdrBsnsBasicParam.getStdrBsnsCd());
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			usptStdrRecomendClDao.insert(info);
		}

		/** 담당자 저장 */
		List<UsptStdrCharger> chkChargerList = stdrBsnsBasicParam.getStdrChargerList().stream().filter(x -> x.getReprsntCharger() != null && x.getReprsntCharger() == true).collect(Collectors.toList());
		if(chkChargerList == null || chkChargerList.size() != 1) {
			throw new InvalidationException("대표담당자는 필수이고 한명만 선택해주세요");
		}
		UsptStdrCharger chkChargerInfo = chkChargerList.get(0);
		if(!CoreUtils.string.equals(chkChargerInfo.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("대표담당자는 실무담당자(수정권한)에만 설정 가능합니다.");
		}

		for(UsptStdrCharger info : stdrBsnsBasicParam.getStdrChargerList()) {
			info.setStdrBsnsCd(stdrBsnsBasicParam.getStdrBsnsCd());
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			info.setUpdatedDt(now);
			info.setUpdaterId(worker.getMemberId());

			if(CoreUtils.string.equals(chkChargerInfo.getInsiderId(), info.getInsiderId())) {
				info.setReprsntCharger(true);
			} else {
				info.setReprsntCharger(false);
			}

			if(CoreUtils.string.equals(Code.flag.등록, info.getFlag())){
				usptStdrChargerDao.insert(info);
			} else if(CoreUtils.string.equals(Code.flag.수정, info.getFlag())){
				if(usptStdrChargerDao.update(info) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else if(CoreUtils.string.equals(Code.flag.삭제, info.getFlag())){
				if(usptStdrChargerDao.delete(info) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}


	/**
	 * 과제정보 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	public StdrBsnsTaskDto getTaskInfo(String stdrBsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		UsptStdrBsns usptStdrBsns = usptStdrBsnsDao.select(stdrBsnsCd);
		if(usptStdrBsns == null) {
			throw new InvalidationException("요청하신 기준사업 정보가 없습니다.");
		}
		StdrBsnsTaskDto taskDto = new StdrBsnsTaskDto();
		taskDto.setStdrBsnsCd(usptStdrBsns.getStdrBsnsCd());
		taskDto.setTaskTypeCd(usptStdrBsns.getTaskTypeCd());
		taskDto.setStdrApplyRealmList(usptStdrApplyRealmDao.selectList(stdrBsnsCd));
		taskDto.setStdrAppnTaskList(usptStdrAppnTaskDao.selectList(stdrBsnsCd));
		return taskDto;
	}


	/**
	 * 과제정보 수정
	 * @param stdrBsnsCd
	 * @param taskInfo
	 */
	public void modifyTaskInfo(String stdrBsnsCd, StdrBsnsTaskDto taskInfo) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(!CoreUtils.string.equals(Code.taskType.자유과제, taskInfo.getTaskTypeCd())) {
			if(taskInfo.getStdrAppnTaskList() == null || taskInfo.getStdrAppnTaskList().isEmpty()) {
				throw new InvalidationException("지정과제 선택 시 지정과제정보를 입력해주세요!");
			}
		}

		Date now = new Date();
		taskInfo.setStdrBsnsCd(stdrBsnsCd);
		taskInfo.setUpdatedDt(now);
		taskInfo.setUpdaterId(worker.getMemberId());
		usptStdrBsnsDao.updateTaskType(taskInfo);

		if(taskInfo.getStdrApplyRealmList() != null)
			for(UsptStdrApplyRealm info : taskInfo.getStdrApplyRealmList()) {
				if(!CoreUtils.string.isBlank(info.getApplyRealmNm())){
					info.setCreatedDt(now);
					info.setCreatorId(worker.getMemberId());
					info.setUpdatedDt(now);
					info.setUpdaterId(worker.getMemberId());
					info.setStdrBsnsCd(stdrBsnsCd);
					if(CoreUtils.string.isBlank(info.getApplyRealmNm())) {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "지원분야"));
					}
					if(CoreUtils.string.equals(Code.flag.등록,info.getFlag())) {
						info.setApplyRealmId(CoreUtils.string.getNewId(Code.prefix.지원분야));
						usptStdrApplyRealmDao.insert(info);
					} else if(CoreUtils.string.equals(Code.flag.수정,info.getFlag())) {
						if(usptStdrApplyRealmDao.update(info) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					} else if(CoreUtils.string.equals(Code.flag.삭제,info.getFlag())) {
						if(usptStdrApplyRealmDao.delete(info) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					} else {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
					}
				}
			}

		if(taskInfo.getStdrAppnTaskList() != null)
			for(UsptStdrAppnTask info : taskInfo.getStdrAppnTaskList()) {
				if(!CoreUtils.string.isBlank(info.getAppnTaskNm())){
					info.setCreatedDt(now);
					info.setCreatorId(worker.getMemberId());
					info.setUpdatedDt(now);
					info.setUpdaterId(worker.getMemberId());
					info.setStdrBsnsCd(stdrBsnsCd);
					if(CoreUtils.string.isBlank(info.getAppnTaskNm())) {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "지정과제"));
					}
					if(CoreUtils.string.equals(Code.flag.등록,info.getFlag())) {
						info.setAppnTaskId(CoreUtils.string.getNewId(Code.prefix.지정과제));
						usptStdrAppnTaskDao.insert(info);
					} else if(CoreUtils.string.equals(Code.flag.수정,info.getFlag())) {
						if(usptStdrAppnTaskDao.update(info) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					} else if(CoreUtils.string.equals(Code.flag.삭제,info.getFlag())) {
						if(usptStdrAppnTaskDao.delete(info) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					} else {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
					}
				}
			}
	}


	/**
	 * 업무단계 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	public JsonList<JobStepDto> getJobStepList(String stdrBsnsCd){
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptStdrJobStepDao.selectViewList(stdrBsnsCd));
	}


	/**
	 * 업무단계 수정
	 * @param stdrBsnsCd
	 * @param jobStepList
	 */
	public void modifyJobStep(String stdrBsnsCd, List<UsptStdrJobStep> jobStepList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		Optional<UsptStdrJobStep> stepChk = jobStepList.stream().filter(x->x.getJobStepCd().equals(Code.jobStep.공고접수)).findFirst();
		if(stepChk == null || !stepChk.isPresent()) {
			throw new InvalidationException("업무단계에서 공고/접수는 필수입니다.");
		}

		Date now = new Date();
		usptStdrJobStepDao.deleteAll(stdrBsnsCd);
		for(UsptStdrJobStep info : jobStepList) {
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			info.setStdrBsnsCd(stdrBsnsCd);
			usptStdrJobStepDao.insert(info);
		}
	}


	/**
	 * 신청제한 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	public ApplyLimitDto getApplyLimit(String stdrBsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		ApplyLimitDto applyLimitDto = new ApplyLimitDto();
		applyLimitDto.setApplyMberTypeList(usptStdrApplyMberTypeDao.selectViewList(stdrBsnsCd));
		applyLimitDto.setChklstList(usptStdrChklstDao.selectList(stdrBsnsCd));
		return applyLimitDto;
	}


	/**
	 * 신청제한 수정
	 * @param stdrBsnsCd
	 * @param applyLimitDto
	 */
	public void modifyApplyLimit(String stdrBsnsCd, ApplyLimitDto applyLimitDto) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(applyLimitDto.getApplyMberTypeList() == null || applyLimitDto.getApplyMberTypeList().isEmpty()) {
			throw new InvalidationException("신청가능 회원의 종류를 선택해주세요!");
		}

		Date now = new Date();
		usptStdrApplyMberTypeDao.deleteAll(stdrBsnsCd);
		for(ApplyMberType info : applyLimitDto.getApplyMberTypeList()) {
			UsptStdrApplyMberType regInfo = new UsptStdrApplyMberType ();
			BeanUtils.copyProperties(info, regInfo);
			regInfo.setCreatedDt(now);
			regInfo.setCreatorId(worker.getMemberId());
			regInfo.setStdrBsnsCd(stdrBsnsCd);
			usptStdrApplyMberTypeDao.insert(regInfo);
		}

		for(Chklst info : applyLimitDto.getChklstList()) {
			UsptStdrChklst regInfo = new UsptStdrChklst();
			BeanUtils.copyProperties(info, regInfo);
			regInfo.setCreatedDt(now);
			regInfo.setCreatorId(worker.getMemberId());
			regInfo.setUpdatedDt(now);
			regInfo.setUpdaterId(worker.getMemberId());
			regInfo.setStdrBsnsCd(stdrBsnsCd);
			if(CoreUtils.string.isBlank(regInfo.getChklstCn())) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "필수확인사항 내용"));
			}
			if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
				regInfo.setChklstId(CoreUtils.string.getNewId(Code.prefix.체크리스트));
				usptStdrChklstDao.insert(regInfo);
			} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
				if(usptStdrChklstDao.update(regInfo) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
				if(usptStdrChklstDao.delete(regInfo) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}


	/**
	 * 사전검토 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	public JsonList<BhExmntDto> getBhExmnt(String stdrBsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptStdrBhExmntDao.selectList(stdrBsnsCd));
	}


	/**
	 * 사전검토 수정
	 * @param stdrBsnsCd
	 * @param bhExmntList
	 */
	public void modifyBhExmnt(String stdrBsnsCd, List<UsptStdrBhExmnt> bhExmntList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		Date now = new Date();
		for(UsptStdrBhExmnt info : bhExmntList) {
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			info.setUpdatedDt(now);
			info.setUpdaterId(worker.getMemberId());
			info.setStdrBsnsCd(stdrBsnsCd);
			if(CoreUtils.string.isBlank(info.getBhExmntDivNm())) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "구분"));
			}
			if(CoreUtils.string.equals(Code.flag.등록,info.getFlag())) {
				info.setBhExmntId(CoreUtils.string.getNewId(Code.prefix.사전검토));
				usptStdrBhExmntDao.insert(info);
			} else if(CoreUtils.string.equals(Code.flag.수정,info.getFlag())) {
				if(usptStdrBhExmntDao.update(info) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else if(CoreUtils.string.equals(Code.flag.삭제,info.getFlag())) {
				usptStdrBhExmntIemDao.deleteAll(info.getBhExmntId());
				if(usptStdrBhExmntDao.delete(info) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}

			if(!CoreUtils.string.equals(Code.flag.삭제,info.getFlag())) {
				for(BhExmntIem subInfo : info.getBhExmntIemList()) {
					UsptStdrBhExmntIem regInfo = new UsptStdrBhExmntIem();
					BeanUtils.copyProperties(subInfo, regInfo);
					regInfo.setCreatedDt(now);
					regInfo.setCreatorId(worker.getMemberId());
					regInfo.setUpdatedDt(now);
					regInfo.setUpdaterId(worker.getMemberId());
					regInfo.setBhExmntId(info.getBhExmntId());
					if(CoreUtils.string.isBlank(regInfo.getBhExmntIemNm())) {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "확인항목"));
					}
					if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
						regInfo.setBhExmntIemId(CoreUtils.string.getNewId(Code.prefix.사전검토항목));
						usptStdrBhExmntIemDao.insert(regInfo);
					} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
						if(usptStdrBhExmntIemDao.update(regInfo) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
						if(usptStdrBhExmntIemDao.delete(regInfo) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					} else {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
					}
				}
			}
		}
	}


	/**
	 * 비목 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	public JsonList<IoeSetupDto> getIoe(String stdrBsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptStdrIoeSetupDao.selectViewList(stdrBsnsCd));
	}

	/**
	 * 비목 수정
	 * @param stdrBsnsCd
	 * @param ioeSetupParamList
	 */
	public void modifyIoe(String stdrBsnsCd, List<IoeSetupParam> ioeSetupParamList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		Date now = new Date();
		/** 비목/세목 전체 삭제 */
		usptStdrTaxitmSetupDao.deleteAll(stdrBsnsCd);
		usptStdrIoeSetupDao.deleteAll(stdrBsnsCd);

		/** 선택된 비목/세목 저장 */
		if(ioeSetupParamList != null && !ioeSetupParamList.isEmpty()) {
			for(IoeSetupParam paramInfo : ioeSetupParamList) {
				UsptStdrIoeSetup info = new UsptStdrIoeSetup();
				info.setStdrBsnsCd(stdrBsnsCd);
				info.setWctIoeId(paramInfo.getWctIoeId());
				info.setCreatedDt(now);
				info.setCreatorId(worker.getMemberId());
				usptStdrIoeSetupDao.insert(info);

				if(paramInfo.getTaxitmList() != null && !paramInfo.getTaxitmList().isEmpty()) {
					for(UsptWctTaxitm taxitmInfo : paramInfo.getTaxitmList()) {
						UsptStdrTaxitmSetup regInfo = new UsptStdrTaxitmSetup();
						BeanUtils.copyProperties(taxitmInfo, regInfo);
						regInfo.setStdrBsnsCd(stdrBsnsCd);
						regInfo.setWctIoeId(info.getWctIoeId());
						regInfo.setCreatedDt(now);
						regInfo.setCreatorId(worker.getMemberId());
						usptStdrTaxitmSetupDao.insert(regInfo);
					}
				}
			}
		}
	}


	/**
	 * 성과지표 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	public JsonList<UsptStdrRsltIdx> getRsltIdx(String stdrBsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptStdrRsltIdxDao.selectList(stdrBsnsCd));
	}

	/**
	 * 성과지표 수정
	 * @param stdrBsnsCd
	 * @param rsltIdxList
	 */
	public void modifyRsltIdx(String stdrBsnsCd, List<UsptStdrRsltIdx> rsltIdxList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if(rsltIdxList == null || rsltIdxList.isEmpty()) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "성과지표 정보"));
		}

		Date now = new Date();
		for(UsptStdrRsltIdx info : rsltIdxList) {
			info.setStdrBsnsCd(stdrBsnsCd);
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			info.setUpdatedDt(now);
			info.setUpdaterId(worker.getMemberId());

			if(CoreUtils.string.isBlank(info.getRsltIdxTypeCd())) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "성과지표유형코드"));
			}
			if(info.getStdIdx() == null) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "기준지표여부"));
			}
			if(!CoreUtils.string.equals(info.getRsltIdxTypeCd(), "TABLE")) {
				info.setStdIdx(false);
			}

			if(CoreUtils.string.equals(Code.flag.등록,info.getFlag())) {
				info.setRsltIdxId(CoreUtils.string.getNewId(Code.prefix.성과지표));
				usptStdrRsltIdxDao.insert(info);
			} else if(CoreUtils.string.equals(Code.flag.수정,info.getFlag())) {
				if(usptStdrRsltIdxDao.update(info) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}

			if(info.getDetailIemList() != null) {
				for(RsltIdxDetailIem subIemInfo  : info.getDetailIemList()) {
					UsptStdrRsltIdxDetailIem regInfo = new UsptStdrRsltIdxDetailIem();
					BeanUtils.copyProperties(subIemInfo, regInfo);
					regInfo.setRsltIdxId(info.getRsltIdxId());
					regInfo.setCreatedDt(now);
					regInfo.setCreatorId(worker.getMemberId());
					regInfo.setUpdatedDt(now);
					regInfo.setUpdaterId(worker.getMemberId());

					if(!info.getStdIdx()) {
						if(CoreUtils.string.isBlank(subIemInfo.getIemUnitCd())) {
							throw new InvalidationException(String.format(Code.validateMessage.입력없음, "항목단위코드"));
						}
					}

					if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
						regInfo.setRsltIdxDetailIemId(CoreUtils.string.getNewId(Code.prefix.성과지표세부항목));
						usptStdrRsltIdxDetailIemDao.insert(regInfo);
					} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
						if(usptStdrRsltIdxDetailIemDao.update(regInfo) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
						if(usptStdrRsltIdxDetailIemDao.delete(regInfo) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					} else {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
					}
				}
			}

			if(info.getStdIdx()) {
				if(info.getStdIemList() != null) {
					for(RsltIdxStdIem subIemInfo  : info.getStdIemList()) {
						UsptStdrRsltIdxStdIem regInfo = new UsptStdrRsltIdxStdIem();
						BeanUtils.copyProperties(subIemInfo, regInfo);
						regInfo.setRsltIdxId(info.getRsltIdxId());
						regInfo.setCreatedDt(now);
						regInfo.setCreatorId(worker.getMemberId());
						regInfo.setUpdatedDt(now);
						regInfo.setUpdaterId(worker.getMemberId());

						if(CoreUtils.string.isBlank(subIemInfo.getIemUnitCd())) {
							throw new InvalidationException(String.format(Code.validateMessage.입력없음, "항목단위코드"));
						}

						if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
							regInfo.setRsltIdxStdIemId(CoreUtils.string.getNewId(Code.prefix.성과지표기준항목));
							usptStdrRsltIdxStdIemDao.insert(regInfo);
						} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
							if(usptStdrRsltIdxStdIemDao.update(regInfo) != 1)
								throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
						} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
							if(usptStdrRsltIdxStdIemDao.delete(regInfo) != 1)
								throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
						} else {
							throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
						}
					}
				}
			} else {
				if(CoreUtils.string.equals(Code.flag.수정, info.getFlag())) {
					usptStdrRsltIdxStdIemDao.deleteAll(info.getRsltIdxId());
				}
			}
		}
	}

	/**
	 * 성과지표 삭제
	 * @param stdrBsnsCd
	 * @param rsltIdxIdList
	 */
	public void removeRsltIdx(String stdrBsnsCd, List<RsltIdx> rsltIdxIdList) {
		SecurityUtils.checkWorkerIsInsider();
		for(RsltIdx info : rsltIdxIdList) {
			usptStdrRsltIdxDetailIemDao.deleteAll(info.getRsltIdxId());
			usptStdrRsltIdxStdIemDao.deleteAll(info.getRsltIdxId());
			if(usptStdrRsltIdxDao.delete(info.getRsltIdxId(), stdrBsnsCd) != 1)
				throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
		}
	}


	/**
	 * 첨부파일 설정 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	public JsonList<UsptStdrAtchmnflSetup> getAtchmnfl(String stdrBsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptStdrAtchmnflSetupDao.selectList(stdrBsnsCd));
	}

	/**
	 * 첨부파일 설정 수정
	 * @param stdrBsnsCd
	 * @param setupList
	 * @param fileList
	 */
	public void modifyAtchmnfl(String stdrBsnsCd, List<UsptStdrAtchmnflSetup> setupList, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		Date now = new Date();
		String[] exts = {"HWP", "DOCX", "PPT", "XLSX"};

		for(UsptStdrAtchmnflSetup regInfo : setupList) {
			regInfo.setStdrBsnsCd(stdrBsnsCd);
			regInfo.setCreatedDt(now);
			regInfo.setCreatorId(worker.getMemberId());
			regInfo.setUpdatedDt(now);
			regInfo.setUpdaterId(worker.getMemberId());

			if(regInfo.getFormatFileOrder() != -1) {
				if(fileList != null) {
					MultipartFile fileInfo = fileList.get(regInfo.getFormatFileOrder());
					CmmtAtchmnfl att = attachmentService.uploadFile_noGroup(config.getDir().getUpload(), fileInfo, exts, 0);
					regInfo.setFormatAttachmentId(att.getAttachmentId());
				}
			}
			if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
				regInfo.setAtchmnflSetupId(CoreUtils.string.getNewId(Code.prefix.첨부파일));
				usptStdrAtchmnflSetupDao.insert(regInfo);
			} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
				if(usptStdrAtchmnflSetupDao.update(regInfo) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}

	/**
	 * 첨부파일 설정 삭제
	 * @param stdrBsnsCd
	 * @param atchmnflSetupIdList
	 */
	public void removeAtchmnfl(String stdrBsnsCd, List<AtchmnflSetup> atchmnflSetupIdList) {
		SecurityUtils.checkWorkerIsInsider();
		for(AtchmnflSetup setupInfo : atchmnflSetupIdList) {
			UsptStdrAtchmnflSetup info = usptStdrAtchmnflSetupDao.select(stdrBsnsCd, setupInfo.getAtchmnflSetupId());
			if(usptStdrAtchmnflSetupDao.delete(info) != 1)
				throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			attachmentService.removeFile_keepEmptyGroup(config.getDir().getUpload(), info.getFormatAttachmentId());
		}
	}

	/**
	 *  삭제
	 * @param stdrBsnsCd
	 */
	public void remove(String stdrBsnsCd) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptStdrBsns info = usptStdrBsnsDao.select(stdrBsnsCd);
		if(info == null) {
			throw new InvalidationException("요청하신 기준사업에 대한 정보가 없습니다.");
		}

		Integer cnt = usptStdrBsnsDao.selectBsnsCount(stdrBsnsCd);
		if(cnt != 0) {
			throw new InvalidationException("해당 기준사업이 사용중입니다. \n삭제할 수 없습니다.");
		}

		Date now = new Date();
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());

		if(usptStdrBsnsDao.updateUnable(info) != 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
		}
	}

}
