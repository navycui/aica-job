package aicluster.pms.api.bsns.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import aicluster.pms.api.bsns.dto.BsnsBasicParam;
import aicluster.pms.api.bsns.dto.BsnsListParam;
import aicluster.pms.api.bsns.dto.BsnsTaskDto;
import aicluster.pms.api.bsns.dto.Chklst;
import aicluster.pms.api.bsns.dto.IoeSetupParam;
import aicluster.pms.api.bsns.dto.RsltIdx;
import aicluster.pms.api.bsns.dto.RsltIdxDetailIem;
import aicluster.pms.api.bsns.dto.RsltIdxStdIem;
import aicluster.pms.common.dao.UsptBsnsApplyMberTypeDao;
import aicluster.pms.common.dao.UsptBsnsApplyRealmDao;
import aicluster.pms.common.dao.UsptBsnsAppnTaskDao;
import aicluster.pms.common.dao.UsptBsnsAtchmnflSetupDao;
import aicluster.pms.common.dao.UsptBsnsBhExmntDao;
import aicluster.pms.common.dao.UsptBsnsBhExmntIemDao;
import aicluster.pms.common.dao.UsptBsnsChargerDao;
import aicluster.pms.common.dao.UsptBsnsChklstDao;
import aicluster.pms.common.dao.UsptBsnsDao;
import aicluster.pms.common.dao.UsptBsnsIoeSetupDao;
import aicluster.pms.common.dao.UsptBsnsJobStepDao;
import aicluster.pms.common.dao.UsptBsnsRecomendClDao;
import aicluster.pms.common.dao.UsptBsnsRsltIdxDao;
import aicluster.pms.common.dao.UsptBsnsRsltIdxDetailIemDao;
import aicluster.pms.common.dao.UsptBsnsRsltIdxStdIemDao;
import aicluster.pms.common.dao.UsptBsnsTaxitmSetupDao;
import aicluster.pms.common.dao.UsptStdrApplyMberTypeDao;
import aicluster.pms.common.dao.UsptStdrApplyRealmDao;
import aicluster.pms.common.dao.UsptStdrAppnTaskDao;
import aicluster.pms.common.dao.UsptStdrAtchmnflSetupDao;
import aicluster.pms.common.dao.UsptStdrBhExmntDao;
import aicluster.pms.common.dao.UsptStdrChklstDao;
import aicluster.pms.common.dao.UsptStdrIoeSetupDao;
import aicluster.pms.common.dao.UsptStdrJobStepDao;
import aicluster.pms.common.dao.UsptStdrRsltIdxDao;
import aicluster.pms.common.dao.UsptStdrTaxitmSetupDao;
import aicluster.pms.common.dto.ApplyLimitDto;
import aicluster.pms.common.dto.BhExmntDto;
import aicluster.pms.common.dto.BsnsBasicDto;
import aicluster.pms.common.dto.BsnsListDto;
import aicluster.pms.common.dto.IoeSetupDto;
import aicluster.pms.common.dto.JobStepDto;
import aicluster.pms.common.entity.UsptBsns;
import aicluster.pms.common.entity.UsptBsnsApplyMberType;
import aicluster.pms.common.entity.UsptBsnsApplyRealm;
import aicluster.pms.common.entity.UsptBsnsAppnTask;
import aicluster.pms.common.entity.UsptBsnsAtchmnflSetup;
import aicluster.pms.common.entity.UsptBsnsBhExmnt;
import aicluster.pms.common.entity.UsptBsnsBhExmntIem;
import aicluster.pms.common.entity.UsptBsnsCharger;
import aicluster.pms.common.entity.UsptBsnsChklst;
import aicluster.pms.common.entity.UsptBsnsIoeSetup;
import aicluster.pms.common.entity.UsptBsnsJobStep;
import aicluster.pms.common.entity.UsptBsnsRecomendCl;
import aicluster.pms.common.entity.UsptBsnsRsltIdx;
import aicluster.pms.common.entity.UsptBsnsRsltIdxDetailIem;
import aicluster.pms.common.entity.UsptBsnsRsltIdxStdIem;
import aicluster.pms.common.entity.UsptBsnsTaxitmSetup;
import aicluster.pms.common.entity.UsptStdrApplyMberType;
import aicluster.pms.common.entity.UsptStdrApplyRealm;
import aicluster.pms.common.entity.UsptStdrAppnTask;
import aicluster.pms.common.entity.UsptStdrAtchmnflSetup;
import aicluster.pms.common.entity.UsptStdrIoeSetup;
import aicluster.pms.common.entity.UsptStdrJobStep;
import aicluster.pms.common.entity.UsptStdrRsltIdx;
import aicluster.pms.common.entity.UsptStdrTaxitmSetup;
import aicluster.pms.common.entity.UsptWctTaxitm;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class BsnsService {

	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private UsptBsnsDao usptBsnsDao;
	@Autowired
	private UsptBsnsChargerDao usptBsnsChargerDao;
	@Autowired
	private UsptBsnsRecomendClDao usptBsnsRecomendClDao;

	@Autowired
	private UsptBsnsAppnTaskDao usptBsnsAppnTaskDao;
	@Autowired
	private UsptBsnsApplyRealmDao usptBsnsApplyRealmDao;
	@Autowired
	private UsptBsnsJobStepDao usptBsnsJobStepDao;
	@Autowired
	private UsptBsnsApplyMberTypeDao usptBsnsApplyMberTypeDao;
	@Autowired
	private UsptBsnsChklstDao usptBsnsChklstDao;
	@Autowired
	private UsptBsnsBhExmntDao usptBsnsBhExmntDao;
	@Autowired
	private UsptBsnsBhExmntIemDao usptBsnsBhExmntIemDao;
	@Autowired
	private UsptBsnsIoeSetupDao usptBsnsIoeSetupDao;
	@Autowired
	private UsptBsnsTaxitmSetupDao usptBsnsTaxitmSetupDao;
	@Autowired
	private UsptBsnsRsltIdxDao usptBsnsRsltIdxDao;
	@Autowired
	private UsptBsnsRsltIdxDetailIemDao usptBsnsRsltIdxDetailIemDao;
	@Autowired
	private UsptBsnsRsltIdxStdIemDao usptBsnsRsltIdxStdIemDao;
	@Autowired
	private UsptBsnsAtchmnflSetupDao usptBsnsAtchmnflSetupDao;

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
	private UsptStdrIoeSetupDao usptStdrIoeSetupDao;
	@Autowired
	private UsptStdrTaxitmSetupDao usptStdrTaxitmSetupDao;
	@Autowired
	private UsptStdrRsltIdxDao usptStdrRsltIdxDao;
	@Autowired
	private UsptStdrAtchmnflSetupDao usptStdrAtchmnflSetupDao;

	@Autowired
	private EnvConfig config;
	@Autowired
	private AttachmentService attachmentService;


	/**
	 * 필수값 체크
	 * @param bsns
	 * @return
	 */
	protected InvalidationsException validateBasicInfo(BsnsBasicParam bsns) {
		InvalidationsException ies = new InvalidationsException();
		if(CoreUtils.string.isEmpty(bsns.getStdrBsnsCd())) {
			ies.add("stdrBsnsCd", String.format(Code.validateMessage.입력없음, "기준사업코드"));
		}
		if(CoreUtils.string.isEmpty(bsns.getBsnsNm())) {
			ies.add("bsnsNm", String.format(Code.validateMessage.입력없음, "사업명"));
		}
		if(CoreUtils.string.isEmpty(bsns.getBsnsYear())) {
			ies.add("bsnsYear", String.format(Code.validateMessage.입력없음, "사업연도"));
		}
		if(bsns.getTotBsnsPd() == 0) {
			ies.add("totBsnsPd", String.format(Code.validateMessage.입력없음, "총사업기간"));
		}
		if(CoreUtils.string.isEmpty(bsns.getChrgDeptNm())) {
			ies.add("chrgDeptNm", String.format(Code.validateMessage.입력없음, "담당부서"));
		}
		if(bsns.getBsnsRecomendClList() == null || bsns.getBsnsRecomendClList().isEmpty()) {
			ies.add("bsnsRecomendClList", String.format(Code.validateMessage.입력없음, "추천분류 정보"));
		}
		if(bsns.getBsnsChargerList() == null || bsns.getBsnsChargerList().isEmpty()) {
			ies.add("bsnsChargerList", String.format(Code.validateMessage.입력없음, "담당자 정보"));
		}
		return ies;
	}

	/**
	 * 사업 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<BsnsListDto> getList(BsnsListParam param, Long page){
		SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}

		param.setIsExcel(false);
		long totalItems = usptBsnsDao.selectListCount(param);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<BsnsListDto> list = usptBsnsDao.selectList(param);

		//출력할 자료 생성 후 리턴
		CorePagination<BsnsListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 지원사업공고 등록을 위한 사업 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<BsnsListDto> getListForPblanc(BsnsListParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}

		param.setInsiderId(worker.getMemberId());
		long totalItems = usptBsnsDao.selectListCountForPblanc(param);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<BsnsListDto> list = usptBsnsDao.selectListForPblanc(param);

		//출력할 자료 생성 후 리턴
		CorePagination<BsnsListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 사업관리 목록 엑셀 저장
	 * @param bsnsListParam
	 * @return
	 */
	public List<BsnsListDto> getListExcelDwld(BsnsListParam bsnsListParam){
		SecurityUtils.checkWorkerIsInsider();
		bsnsListParam.setIsExcel(true);
		return usptBsnsDao.selectList(bsnsListParam);
	}

	/**
	 * 사업 등록
	 * @param bsnsBasicParam
	 * @return
	 */
	public String add(BsnsBasicParam bsnsBasicParam) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if(bsnsBasicParam == null) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "사업 정보"));
		}

		InvalidationsException ies = validateBasicInfo(bsnsBasicParam);
		if (ies.size() > 0) {
			throw ies;
		}

		Date now = new Date();
		bsnsBasicParam.setCreatedDt(now);
		bsnsBasicParam.setCreatorId(worker.getMemberId());
		bsnsBasicParam.setUpdatedDt(now);
		bsnsBasicParam.setUpdaterId(worker.getMemberId());
		usptBsnsDao.insert(bsnsBasicParam);

		/* 사업추천분류 */
		List<UsptBsnsRecomendCl> bsnsRecomendClList = new ArrayList<>();
		for(UsptBsnsRecomendCl info : bsnsBasicParam.getBsnsRecomendClList()) {
			info.setBsnsCd(bsnsBasicParam.getBsnsCd());
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			bsnsRecomendClList.add(info);
		}
		if(bsnsRecomendClList.size() > 0)
			usptBsnsRecomendClDao.insertList(bsnsRecomendClList);

		/* 사업담당자 */
		List<UsptBsnsCharger> chkChargerList = bsnsBasicParam.getBsnsChargerList().stream().filter(x -> x.getReprsntCharger() != null && x.getReprsntCharger() == true).collect(Collectors.toList());
		if(chkChargerList == null || chkChargerList.size() != 1) {
			throw new InvalidationException("대표담당자는 필수이고 한명만 선택해주세요");
		}
		UsptBsnsCharger chkChargerInfo = chkChargerList.get(0);
		if(!CoreUtils.string.equals(chkChargerInfo.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("대표담당자는 실무담당자(수정권한)에만 설정 가능합니다.");
		}
		List<UsptBsnsCharger> bsnsChargerList = new ArrayList<>();
		for(UsptBsnsCharger info : bsnsBasicParam.getBsnsChargerList()) {
			info.setBsnsCd(bsnsBasicParam.getBsnsCd());
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			info.setUpdatedDt(now);
			info.setUpdaterId(worker.getMemberId());
			bsnsChargerList.add(info);
		}
		usptBsnsChargerDao.insertList(bsnsChargerList);


		/* 사업지정과제 */
		List<UsptStdrAppnTask> appnTaskList = usptStdrAppnTaskDao.selectList(bsnsBasicParam.getStdrBsnsCd());
		if(appnTaskList != null) {
			List<UsptBsnsAppnTask> bsnsAppnTaskList = new ArrayList<>();
			for(UsptStdrAppnTask selectInfo : appnTaskList) {
				UsptBsnsAppnTask regInfo = new UsptBsnsAppnTask();
				regInfo.setBsnsCd(bsnsBasicParam.getBsnsCd());
				regInfo.setAppnTaskId(CoreUtils.string.getNewId(Code.prefix.지정과제));
				regInfo.setAppnTaskNm(selectInfo.getAppnTaskNm());
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setUpdatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				bsnsAppnTaskList.add(regInfo);
			}
			if(bsnsAppnTaskList.size() > 0)
				usptBsnsAppnTaskDao.insertList(bsnsAppnTaskList);
		}
		/* 사업지원분야 */
		List<UsptStdrApplyRealm> applyRealmList = usptStdrApplyRealmDao.selectList(bsnsBasicParam.getStdrBsnsCd());
		if(applyRealmList != null) {
			List<UsptBsnsApplyRealm> bsnsApplyRealmList = new ArrayList<>();
			for(UsptStdrApplyRealm selectInfo : applyRealmList) {
				UsptBsnsApplyRealm regInfo = new UsptBsnsApplyRealm();
				regInfo.setBsnsCd(bsnsBasicParam.getBsnsCd());
				regInfo.setApplyRealmId(CoreUtils.string.getNewId(Code.prefix.지원분야));
				regInfo.setApplyRealmNm(selectInfo.getApplyRealmNm());
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setUpdatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				bsnsApplyRealmList.add(regInfo);
			}
			if(bsnsApplyRealmList.size() > 0)
				usptBsnsApplyRealmDao.insertList(bsnsApplyRealmList);
		}
		/* 업무단계 */
		List<UsptStdrJobStep> jobStepList = usptStdrJobStepDao.selectList(bsnsBasicParam.getStdrBsnsCd());
		if(jobStepList != null) {
			List<UsptBsnsJobStep> bsnsJobStepList = new ArrayList<>();
			for(UsptStdrJobStep selectInfo : jobStepList) {
				UsptBsnsJobStep regInfo = new UsptBsnsJobStep();
				regInfo.setBsnsCd(bsnsBasicParam.getBsnsCd());
				regInfo.setJobStepCd(selectInfo.getJobStepCd());
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());
				bsnsJobStepList.add(regInfo);
			}
			if(bsnsJobStepList.size() > 0)
				usptBsnsJobStepDao.insertList(bsnsJobStepList);
		}
		/* 사업 지원가능 회원유형 */
		List<UsptStdrApplyMberType> mberTypeList = usptStdrApplyMberTypeDao.selectList(bsnsBasicParam.getStdrBsnsCd());
		if(mberTypeList != null) {
			List<UsptBsnsApplyMberType> bsnsApplyMberTypeList = new ArrayList<>();
			for(UsptStdrApplyMberType selectInfo : mberTypeList) {
				UsptBsnsApplyMberType regInfo = new UsptBsnsApplyMberType();
				regInfo.setBsnsCd(bsnsBasicParam.getBsnsCd());
				regInfo.setApplyMberTypeCd(selectInfo.getApplyMberTypeCd());
				regInfo.setApplyMberTypeNm(selectInfo.getApplyMberTypeNm());
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());
				bsnsApplyMberTypeList.add(regInfo);
			}
			if(bsnsApplyMberTypeList.size() > 0)
				usptBsnsApplyMberTypeDao.insertList(bsnsApplyMberTypeList);
		}
		/* 사업체크리스트 */
		List<Chklst> chklstList = usptStdrChklstDao.selectList(bsnsBasicParam.getStdrBsnsCd());
		if(chklstList != null) {
			List<UsptBsnsChklst> bsnsChklstList = new ArrayList<>();
			for(Chklst selectInfo : chklstList) {
				UsptBsnsChklst regInfo = new UsptBsnsChklst();
				regInfo.setBsnsCd(bsnsBasicParam.getBsnsCd());
				regInfo.setChklstId(CoreUtils.string.getNewId(Code.prefix.체크리스트));
				regInfo.setChklstCn(selectInfo.getChklstCn());
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setUpdatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				bsnsChklstList.add(regInfo);
			}
			if(bsnsChklstList.size() > 0)
				usptBsnsChklstDao.insertList(bsnsChklstList);
		}
		/* 사업사전검토/사전검토항목 */
		List<BhExmntDto> bhExmntList = usptStdrBhExmntDao.selectList(bsnsBasicParam.getStdrBsnsCd());
		if(bhExmntList != null) {
			List<UsptBsnsBhExmnt> bsnsBhExmntList = new ArrayList<>();
			List<UsptBsnsBhExmntIem> bsnsBhExmntIemList = new ArrayList<>();
			for(BhExmntDto selectInfo : bhExmntList) {
				UsptBsnsBhExmnt regInfo = new UsptBsnsBhExmnt();
				regInfo.setBsnsCd(bsnsBasicParam.getBsnsCd());
				regInfo.setBhExmntId(CoreUtils.string.getNewId(Code.prefix.사전검토));
				regInfo.setBhExmntDivNm(selectInfo.getBhExmntDivNm());
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setUpdatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				bsnsBhExmntList.add(regInfo);

				if(selectInfo.getBhExmntIemList() != null) {
					for(BhExmntIem iemSelectInfo : selectInfo.getBhExmntIemList()) {
						UsptBsnsBhExmntIem iemRegInfo = new UsptBsnsBhExmntIem();
						iemRegInfo.setBhExmntId(regInfo.getBhExmntId());
						iemRegInfo.setBhExmntIemId(CoreUtils.string.getNewId(Code.prefix.사전검토항목));
						iemRegInfo.setBhExmntIemNm(iemSelectInfo.getBhExmntIemNm());
						iemRegInfo.setCreatedDt(now);
						iemRegInfo.setCreatorId(worker.getMemberId());
						iemRegInfo.setUpdatedDt(now);
						iemRegInfo.setUpdaterId(worker.getMemberId());
						bsnsBhExmntIemList.add(iemRegInfo);
					}
				}
			}
			if(bsnsBhExmntList.size() > 0) {
				usptBsnsBhExmntDao.insertList(bsnsBhExmntList);
				if(bsnsBhExmntIemList.size() > 0)
					usptBsnsBhExmntIemDao.insertList(bsnsBhExmntIemList);
			}
		}
		/* 사업비목 */
		List<UsptStdrIoeSetup> ioeList = usptStdrIoeSetupDao.selectList(bsnsBasicParam.getStdrBsnsCd());
		if(ioeList != null) {
			List<UsptBsnsIoeSetup> bsnsIoeSetupList = new ArrayList<>();
			for(UsptStdrIoeSetup selectInfo : ioeList) {
				UsptBsnsIoeSetup regInfo = new UsptBsnsIoeSetup();
				regInfo.setBsnsCd(bsnsBasicParam.getBsnsCd());
				regInfo.setWctIoeId(selectInfo.getWctIoeId());
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());
				bsnsIoeSetupList.add(regInfo);
			}
			if(bsnsIoeSetupList.size() > 0)
				usptBsnsIoeSetupDao.insertList(bsnsIoeSetupList);
		}
		/* 세목설정 */
		List<UsptStdrTaxitmSetup> taxitmList = usptStdrTaxitmSetupDao.selectList(bsnsBasicParam.getStdrBsnsCd());
		if(taxitmList != null) {
			List<UsptBsnsTaxitmSetup> bsnsTaxitmSetupList = new ArrayList<>();
			for(UsptStdrTaxitmSetup selectInfo : taxitmList) {
				UsptBsnsTaxitmSetup subRegInfo = new UsptBsnsTaxitmSetup();
				subRegInfo.setBsnsCd(bsnsBasicParam.getBsnsCd());
				subRegInfo.setWctIoeId(selectInfo.getWctIoeId());
				subRegInfo.setWctTaxitmId(selectInfo.getWctTaxitmId());
				subRegInfo.setCreatedDt(now);
				subRegInfo.setCreatorId(worker.getMemberId());
				bsnsTaxitmSetupList.add(subRegInfo);
			}
			if(bsnsTaxitmSetupList.size() > 0 )
				usptBsnsTaxitmSetupDao.insertList(bsnsTaxitmSetupList);
		}

		/*사업성과지표/세부항목/기준항목 */
		List<UsptStdrRsltIdx> rsltIdxList = usptStdrRsltIdxDao.selectList(bsnsBasicParam.getStdrBsnsCd());
		if(rsltIdxList != null) {
			List<UsptBsnsRsltIdx> bsnsRsltIdxList = new ArrayList<>();
			List<UsptBsnsRsltIdxDetailIem> bsnsRsltIdxDetailIemList = new ArrayList<>();
			List<UsptBsnsRsltIdxStdIem> bsnsRsltIdxStdIemList = new ArrayList<>();
			for(UsptStdrRsltIdx selectInfo : rsltIdxList) {
				UsptBsnsRsltIdx regInfo = new UsptBsnsRsltIdx();
				regInfo.setBsnsCd(bsnsBasicParam.getBsnsCd());
				regInfo.setRsltIdxId(CoreUtils.string.getNewId(Code.prefix.성과지표));
				regInfo.setRsltIdxNm(selectInfo.getRsltIdxNm());
				regInfo.setStdIdx(selectInfo.getStdIdx());
				regInfo.setRsltIdxTypeCd(selectInfo.getRsltIdxTypeCd());
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setUpdatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				bsnsRsltIdxList.add(regInfo);

				if(selectInfo.getDetailIemList() != null) {
					for(RsltIdxDetailIem dIemInfo : selectInfo.getDetailIemList()) {
						UsptBsnsRsltIdxDetailIem dIemRegInfo = new UsptBsnsRsltIdxDetailIem();
						dIemRegInfo.setRsltIdxId(regInfo.getRsltIdxId());
						dIemRegInfo.setRsltIdxDetailIemId(CoreUtils.string.getNewId(Code.prefix.성과지표세부항목));
						dIemRegInfo.setDetailIemNm(dIemInfo.getDetailIemNm());
						dIemRegInfo.setIemUnitCd(dIemInfo.getIemUnitCd());
						dIemRegInfo.setCreatedDt(now);
						dIemRegInfo.setCreatorId(worker.getMemberId());
						dIemRegInfo.setUpdatedDt(now);
						dIemRegInfo.setUpdaterId(worker.getMemberId());
						bsnsRsltIdxDetailIemList.add(dIemRegInfo);
					}
				}
				if(selectInfo.getStdIemList() != null) {
					for(RsltIdxStdIem stdIemInfo : selectInfo.getStdIemList()) {
						UsptBsnsRsltIdxStdIem sIemRegInfo = new UsptBsnsRsltIdxStdIem();
						sIemRegInfo.setRsltIdxId(regInfo.getRsltIdxId());
						sIemRegInfo.setRsltIdxStdIemId(CoreUtils.string.getNewId(Code.prefix.성과지표기준항목));
						sIemRegInfo.setStdIemNm(stdIemInfo.getStdIemNm());
						sIemRegInfo.setIemUnitCd(stdIemInfo.getIemUnitCd());
						sIemRegInfo.setCreatedDt(now);
						sIemRegInfo.setCreatorId(worker.getMemberId());
						sIemRegInfo.setUpdatedDt(now);
						sIemRegInfo.setUpdaterId(worker.getMemberId());
						bsnsRsltIdxStdIemList.add(sIemRegInfo);
					}
				}
			}
			if(bsnsRsltIdxList.size() > 0) {
				usptBsnsRsltIdxDao.insertList(bsnsRsltIdxList);
				if(bsnsRsltIdxDetailIemList.size() > 0)
					usptBsnsRsltIdxDetailIemDao.insertList(bsnsRsltIdxDetailIemList);
				if(bsnsRsltIdxStdIemList.size() > 0)
					usptBsnsRsltIdxStdIemDao.insertList(bsnsRsltIdxStdIemList);
			}
		}
		/* 사업첨부파일설정 */
		List<UsptStdrAtchmnflSetup> atchmnflList = usptStdrAtchmnflSetupDao.selectList(bsnsBasicParam.getStdrBsnsCd());
		if(atchmnflList != null) {
			List<UsptBsnsAtchmnflSetup> bsnsAtchmnflSetupList = new ArrayList<>();
			for(UsptStdrAtchmnflSetup selectInfo : atchmnflList) {
				UsptBsnsAtchmnflSetup regInfo = new UsptBsnsAtchmnflSetup();
				regInfo.setBsnsCd(bsnsBasicParam.getBsnsCd());
				regInfo.setAtchmnflSetupId(CoreUtils.string.getNewId(Code.prefix.첨부파일));
				regInfo.setFormatAttachmentId(selectInfo.getFormatAttachmentId());
				regInfo.setFileKndNm(selectInfo.getFileKndNm());
				regInfo.setEssntl(selectInfo.getEssntl());
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());
				regInfo.setUpdatedDt(now);
				regInfo.setUpdaterId(worker.getMemberId());
				bsnsAtchmnflSetupList.add(regInfo);
			}
			if(bsnsAtchmnflSetupList.size() > 0 )
				usptBsnsAtchmnflSetupDao.insertList(bsnsAtchmnflSetupList);
		}
		return bsnsBasicParam.getBsnsCd();
	}


	/**
	 * 기본정보 조회
	 * @param bsnsCd
	 * @return
	 */
	public BsnsBasicDto getBasicInfo(String bsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		BsnsBasicDto bsnsBasic = usptBsnsDao.select(bsnsCd);
		if(bsnsBasic == null) {
			throw new InvalidationException("요청하신 사업정보가 없습니다.");
		}
		bsnsBasic.setBsnsChargerList(usptBsnsChargerDao.selectList(bsnsCd));
		bsnsBasic.setBsnsRecomendClList(usptBsnsRecomendClDao.selectList(bsnsCd));
		return bsnsBasic;
	}

	/**
	 * 기본정보 수정
	 * @param bsnsBasicParam
	 */
	public void modifyBasicInfo(BsnsBasicParam bsnsBasicParam) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsDao.selectBsnsPblancCount(bsnsBasicParam.getBsnsCd()) > 0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n수정할 수 없습니다.");
		}

		InvalidationsException ies = validateBasicInfo(bsnsBasicParam);
		if (ies.size() > 0) {
			throw ies;
		}

		Date now = new Date();
		bsnsBasicParam.setUpdatedDt(now);
		bsnsBasicParam.setUpdaterId(worker.getMemberId());
		if(usptBsnsDao.update(bsnsBasicParam) != 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
		}

		/** 추천 분류 저장 */
		usptBsnsRecomendClDao.deleteAll(bsnsBasicParam.getBsnsCd());
		for(UsptBsnsRecomendCl info : bsnsBasicParam.getBsnsRecomendClList()) {
			info.setBsnsCd(bsnsBasicParam.getBsnsCd());
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			usptBsnsRecomendClDao.insert(info);
		}

		/** 담당자 저장 */
		List<UsptBsnsCharger> chkChargerList = bsnsBasicParam.getBsnsChargerList().stream().filter(x -> x.getReprsntCharger() != null && x.getReprsntCharger() == true).collect(Collectors.toList());
		if(chkChargerList == null || chkChargerList.size() != 1) {
			throw new InvalidationException("대표담당자는 필수이고 한명만 선택해주세요");
		}
		UsptBsnsCharger chkChargerInfo = chkChargerList.get(0);
		if(!CoreUtils.string.equals(chkChargerInfo.getChargerAuthorCd(), Code.사업담당자_수정권한)) {
			throw new InvalidationException("대표담당자는 실무담당자(수정권한)에만 설정 가능합니다.");
		}
		for(UsptBsnsCharger info : bsnsBasicParam.getBsnsChargerList()) {
			info.setBsnsCd(bsnsBasicParam.getBsnsCd());
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
				usptBsnsChargerDao.insert(info);
			} else if(CoreUtils.string.equals(Code.flag.수정, info.getFlag())){
				if(usptBsnsChargerDao.update(info) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else if(CoreUtils.string.equals(Code.flag.삭제, info.getFlag())){
				if(usptBsnsChargerDao.delete(info) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}


	/**
	 * 과제정보 조회
	 * @param bsnsCd
	 * @return
	 */
	public BsnsTaskDto getTaskInfo(String bsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		BsnsBasicDto bsnsBasic = usptBsnsDao.select(bsnsCd);
		if(bsnsBasic == null) {
			throw new InvalidationException("요청하신 사업정보가 없습니다.");
		}

		BsnsTaskDto bsnsTaskDto = new BsnsTaskDto();
		bsnsTaskDto.setBsnsCd(bsnsBasic.getBsnsCd());
		bsnsTaskDto.setTaskTypeCd(bsnsBasic.getTaskTypeCd());
		bsnsTaskDto.setBsnsApplyRealmList(usptBsnsApplyRealmDao.selectList(bsnsCd));
		bsnsTaskDto.setBsnsAppnTaskList(usptBsnsAppnTaskDao.selectList(bsnsCd));
		return bsnsTaskDto;
	}


	/**
	 * 과제정보 수정
	 * @param bsnsTaskInfo
	 */
	public void modifyTaskInfo(BsnsTaskDto bsnsTaskInfo) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsDao.selectBsnsPblancCount(bsnsTaskInfo.getBsnsCd()) >0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n수정할 수 없습니다.");
		}
		if(!CoreUtils.string.equals(Code.taskType.자유과제, bsnsTaskInfo.getTaskTypeCd())) {
			if(bsnsTaskInfo.getBsnsAppnTaskList() == null || bsnsTaskInfo.getBsnsAppnTaskList().isEmpty()) {
				throw new InvalidationException("지정과제 선택 시 지정과제정보를 입력해주세요!");
			}
		}

		Date now = new Date();
		bsnsTaskInfo.setUpdatedDt(now);
		bsnsTaskInfo.setUpdaterId(worker.getMemberId());
		usptBsnsDao.updateTaskType(bsnsTaskInfo);

		/** 지원분야 */
		if(bsnsTaskInfo.getBsnsApplyRealmList() != null)
			for(UsptBsnsApplyRealm info : bsnsTaskInfo.getBsnsApplyRealmList()) {
				if(CoreUtils.string.isNotBlank(info.getApplyRealmNm())) {
					info.setCreatedDt(now);
					info.setCreatorId(worker.getMemberId());
					info.setUpdatedDt(now);
					info.setUpdaterId(worker.getMemberId());
					info.setBsnsCd(bsnsTaskInfo.getBsnsCd());
					if(CoreUtils.string.isBlank(info.getApplyRealmNm())) {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "지원분야"));
					}
					if(CoreUtils.string.equals(Code.flag.등록, info.getFlag())) {
						info.setApplyRealmId(CoreUtils.string.getNewId(Code.prefix.지원분야));
						usptBsnsApplyRealmDao.insert(info);
					} else if(CoreUtils.string.equals(Code.flag.수정, info.getFlag())) {
						if(usptBsnsApplyRealmDao.update(info) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					} else if(CoreUtils.string.equals(Code.flag.삭제, info.getFlag())) {
						if(usptBsnsApplyRealmDao.delete(info) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					} else {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
					}
				}
			}

		/** 지정과제 */
		if(bsnsTaskInfo.getBsnsAppnTaskList() != null)
			for(UsptBsnsAppnTask info : bsnsTaskInfo.getBsnsAppnTaskList()) {
				if(CoreUtils.string.isNotBlank(info.getAppnTaskNm())) {
					info.setCreatedDt(now);
					info.setCreatorId(worker.getMemberId());
					info.setUpdatedDt(now);
					info.setUpdaterId(worker.getMemberId());
					info.setBsnsCd(bsnsTaskInfo.getBsnsCd());
					if(CoreUtils.string.isBlank(info.getAppnTaskNm())) {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "지정과제"));
					}
					if(CoreUtils.string.equals(Code.flag.등록, info.getFlag())) {
						info.setAppnTaskId(CoreUtils.string.getNewId(Code.prefix.지정과제));
						usptBsnsAppnTaskDao.insert(info);
					} else if(CoreUtils.string.equals(Code.flag.수정, info.getFlag())) {
						if(usptBsnsAppnTaskDao.update(info) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					} else if(CoreUtils.string.equals(Code.flag.삭제, info.getFlag())) {
						if(usptBsnsAppnTaskDao.delete(info) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					} else {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
					}
				}
			}
	}


	/**
	 * 업무단계 조회
	 * @param bsnsCd
	 * @return
	 */
	public JsonList<JobStepDto> getJobStepList(String bsnsCd){
		SecurityUtils.checkWorkerIsInsider();
		List<JobStepDto> list = usptBsnsJobStepDao.selectViewList(bsnsCd);
		return new JsonList<>(list);
	}

	/**
	 * 업무단계 수정
	 * @param bsnsCd
	 * @param jobStepList
	 */
	public void modifyJobStep(String bsnsCd, List<UsptBsnsJobStep> jobStepList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsDao.selectBsnsPblancCount(bsnsCd) >0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n수정할 수 없습니다.");
		}
		Date now = new Date();
		usptBsnsJobStepDao.deleteAll(bsnsCd);
		for(UsptBsnsJobStep info : jobStepList) {
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			info.setBsnsCd(bsnsCd);
			usptBsnsJobStepDao.insert(info);
		}
	}


	/**
	 * 신청제한 조회
	 * @param bsnsCd
	 * @return
	 */
	public ApplyLimitDto getApplyLimit(String bsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		ApplyLimitDto applyLimitDto = new ApplyLimitDto();
		applyLimitDto.setApplyMberTypeList(usptBsnsApplyMberTypeDao.selectList(bsnsCd));
		applyLimitDto.setChklstList(usptBsnsChklstDao.selectList(bsnsCd));
		return applyLimitDto;
	}

	/**
	 * 신청제한 수정
	 * @param bsnsCd
	 * @param applyLimitDto
	 */
	public void modifyApplyLimit(String bsnsCd, ApplyLimitDto applyLimitDto) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsDao.selectBsnsPblancCount(bsnsCd) >0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n수정할 수 없습니다.");
		}
		if(applyLimitDto.getApplyMberTypeList() == null || applyLimitDto.getApplyMberTypeList().isEmpty()) {
			throw new InvalidationException("신청가능 회원의 종류를 선택해주세요!");
		}

		Date now = new Date();
		usptBsnsApplyMberTypeDao.deleteAll(bsnsCd);

		for(ApplyMberType info : applyLimitDto.getApplyMberTypeList()) {
			UsptBsnsApplyMberType regInfo = new UsptBsnsApplyMberType();
			BeanUtils.copyProperties(info, regInfo);
			regInfo.setCreatedDt(now);
			regInfo.setCreatorId(worker.getMemberId());
			regInfo.setBsnsCd(bsnsCd);
			usptBsnsApplyMberTypeDao.insert(regInfo);
		}

		for(Chklst info : applyLimitDto.getChklstList()) {
			UsptBsnsChklst regInfo = new UsptBsnsChklst();
			BeanUtils.copyProperties(info, regInfo);
			regInfo.setCreatedDt(now);
			regInfo.setCreatorId(worker.getMemberId());
			regInfo.setUpdatedDt(now);
			regInfo.setUpdaterId(worker.getMemberId());
			regInfo.setBsnsCd(bsnsCd);
			if(CoreUtils.string.isBlank(regInfo.getChklstCn())) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "필수확인사항 내용"));
			}
			if(CoreUtils.string.equals(Code.flag.등록, regInfo.getFlag())) {
				regInfo.setChklstId(CoreUtils.string.getNewId(Code.prefix.체크리스트));
				usptBsnsChklstDao.insert(regInfo);
			} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
				if(usptBsnsChklstDao.update(regInfo) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
				if(usptBsnsChklstDao.delete(regInfo) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}


	/**
	 * 사전검토 조회
	 * @param bsnsCd
	 * @return
	 */
	public JsonList<BhExmntDto> getBhExmnt(String bsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptBsnsBhExmntDao.selectList(bsnsCd));
	}

	/**
	 * 사전검토 수정
	 * @param bsnsCd
	 * @param bhExmntList
	 */
	public void modifyBhExmnt(String bsnsCd, List<UsptBsnsBhExmnt> bhExmntList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsDao.selectBsnsPblancCount(bsnsCd) >0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n수정할 수 없습니다.");
		}

		Date now = new Date();
		for(UsptBsnsBhExmnt info : bhExmntList) {
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			info.setUpdatedDt(now);
			info.setUpdaterId(worker.getMemberId());
			info.setBsnsCd(bsnsCd);
			if(CoreUtils.string.isBlank(info.getBhExmntDivNm())) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "구분"));
			}
			if(CoreUtils.string.equals(Code.flag.등록,info.getFlag())) {
				info.setBhExmntId(CoreUtils.string.getNewId(Code.prefix.사전검토));
				usptBsnsBhExmntDao.insert(info);
			} else if(CoreUtils.string.equals(Code.flag.수정, info.getFlag())) {
				if(usptBsnsBhExmntDao.update(info) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else if(CoreUtils.string.equals(Code.flag.삭제, info.getFlag())) {
				usptBsnsBhExmntIemDao.deleteAll(info.getBhExmntId());
				if(usptBsnsBhExmntDao.delete(info) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}

			if(!CoreUtils.string.equals(Code.flag.삭제, info.getFlag())) {
				for(BhExmntIem subInfo : info.getBhExmntIemList()) {
					UsptBsnsBhExmntIem regInfo = new UsptBsnsBhExmntIem();
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
						usptBsnsBhExmntIemDao.insert(regInfo);
					} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
						if(usptBsnsBhExmntIemDao.update(regInfo) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
						if(usptBsnsBhExmntIemDao.delete(regInfo) != 1)
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
	 * @param bsnsCd
	 * @return
	 */
	public JsonList<IoeSetupDto> getIoe(String bsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptBsnsIoeSetupDao.selectList(bsnsCd));
	}

	/**
	 * 비목 수정
	 * @param bsnsCd
	 * @param ioeSetupParamList
	 */
	public void modifyIoe(String bsnsCd, List<IoeSetupParam> ioeSetupParamList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsDao.selectBsnsPblancCount(bsnsCd) >0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n수정할 수 없습니다.");
		}

		Date now = new Date();
		/** 비목/세목 전체 삭제 */
		usptBsnsTaxitmSetupDao.deleteAll(bsnsCd);
		usptBsnsIoeSetupDao.deleteAll(bsnsCd);
		/** 선택된 비목/세목 저장 */
		if(ioeSetupParamList != null && !ioeSetupParamList.isEmpty()) {
			for(IoeSetupParam paramInfo : ioeSetupParamList) {
				UsptBsnsIoeSetup info = new UsptBsnsIoeSetup();
				info.setBsnsCd(bsnsCd);
				info.setWctIoeId(paramInfo.getWctIoeId());
				info.setCreatedDt(now);
				info.setCreatorId(worker.getMemberId());
				usptBsnsIoeSetupDao.insert(info);

				if(paramInfo.getTaxitmList() != null && !paramInfo.getTaxitmList().isEmpty()) {
					for(UsptWctTaxitm taxitmInfo : paramInfo.getTaxitmList()) {
						UsptBsnsTaxitmSetup regInfo = new UsptBsnsTaxitmSetup();
						BeanUtils.copyProperties(taxitmInfo, regInfo);
						regInfo.setBsnsCd(bsnsCd);
						regInfo.setWctIoeId(info.getWctIoeId());
						regInfo.setCreatedDt(now);
						regInfo.setCreatorId(worker.getMemberId());
						usptBsnsTaxitmSetupDao.insert(regInfo);
					}
				}
			}
		}
	}


	/**
	 * 성과지표 조회
	 * @param bsnsCd
	 * @return
	 */
	public JsonList<UsptBsnsRsltIdx> getRsltIdx(String bsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptBsnsRsltIdxDao.selectList(bsnsCd));
	}

	/**
	 * 성과지표 수정
	 * @param bsnsCd
	 * @param rsltIdxList
	 */
	public void modifyRsltIdx(String bsnsCd, List<UsptBsnsRsltIdx> rsltIdxList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsDao.selectBsnsPblancCount(bsnsCd) >0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n수정할 수 없습니다.");
		}

		if(rsltIdxList == null || rsltIdxList.isEmpty()) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "성과지표 정보"));
		}

		Date now = new Date();
		for(UsptBsnsRsltIdx info : rsltIdxList) {
			info.setBsnsCd(bsnsCd);
			info.setCreatedDt(now);
			info.setCreatorId(worker.getMemberId());
			info.setUpdatedDt(now);
			info.setUpdaterId(worker.getMemberId());

			if(CoreUtils.string.isBlank(info.getRsltIdxTypeCd())) {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "성과지표유형코드"));
			}

			if(!CoreUtils.string.equals(info.getRsltIdxTypeCd(), "TABLE")) {
				info.setStdIdx(false);
			}
			if(CoreUtils.string.equals(Code.flag.등록, info.getFlag())) {
				info.setRsltIdxId(CoreUtils.string.getNewId(Code.prefix.성과지표));
				usptBsnsRsltIdxDao.insert(info);
			} else if(CoreUtils.string.equals(Code.flag.수정, info.getFlag())) {
				if(usptBsnsRsltIdxDao.update(info) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}

			if(info.getDetailIemList() != null) {
				for(RsltIdxDetailIem subIemInfo  : info.getDetailIemList()) {
					UsptBsnsRsltIdxDetailIem regInfo = new UsptBsnsRsltIdxDetailIem();
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
						usptBsnsRsltIdxDetailIemDao.insert(regInfo);
					} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
						if(usptBsnsRsltIdxDetailIemDao.update(regInfo) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
					} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
						if(usptBsnsRsltIdxDetailIemDao.delete(regInfo) != 1)
							throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
					} else {
						throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
					}
				}
			}

			if(info.getStdIdx()) {
				if(info.getStdIemList() != null) {
					for(RsltIdxStdIem subIemInfo  : info.getStdIemList()) {
						UsptBsnsRsltIdxStdIem regInfo = new UsptBsnsRsltIdxStdIem();
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
							usptBsnsRsltIdxStdIemDao.insert(regInfo);
						} else if(CoreUtils.string.equals(Code.flag.수정, regInfo.getFlag())) {
							if(usptBsnsRsltIdxStdIemDao.update(regInfo) != 1)
								throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
						} else if(CoreUtils.string.equals(Code.flag.삭제, regInfo.getFlag())) {
							if(usptBsnsRsltIdxStdIemDao.delete(regInfo) != 1)
								throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
						} else {
							throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
						}
					}
				}
			} else {
				if(CoreUtils.string.equals(Code.flag.수정, info.getFlag())) {
					usptBsnsRsltIdxStdIemDao.deleteAll(info.getRsltIdxId());
				}
			}
		}
	}

	/**
	 * 성과지표 삭제
	 * @param bsnsCd
	 * @param rsltIdxIdList
	 */
	public void removeRsltIdx(String bsnsCd, List<RsltIdx> rsltIdxIdList) {
		SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsDao.selectBsnsPblancCount(bsnsCd) >0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n수정할 수 없습니다.");
		}

		for(RsltIdx info : rsltIdxIdList) {
			usptBsnsRsltIdxDetailIemDao.deleteAll(info.getRsltIdxId());
			usptBsnsRsltIdxStdIemDao.deleteAll(info.getRsltIdxId());
			if(usptBsnsRsltIdxDao.delete(info.getRsltIdxId(), bsnsCd) != 1)
				throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
		}
	}


	/**
	 * 첨부파일설정 조회
	 * @param bsnsCd
	 * @return
	 */
	public JsonList<UsptBsnsAtchmnflSetup> getAtchmnfl(String bsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		return new JsonList<>(usptBsnsAtchmnflSetupDao.selectList(bsnsCd));
	}

	/**
	 * 첨부파일설정 수정
	 * @param bsnsCd
	 * @param setupList
	 * @param fileList
	 */
	public void modifyAtchmnfl(String bsnsCd, List<UsptBsnsAtchmnflSetup> setupList, List<MultipartFile> fileList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsDao.selectBsnsPblancCount(bsnsCd) >0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n수정할 수 없습니다.");
		}

		Date now = new Date();
		String[] exts = {"HWP", "DOCX", "PPT", "XLSX"};

		for(UsptBsnsAtchmnflSetup regInfo : setupList) {
			regInfo.setBsnsCd(bsnsCd);
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
				usptBsnsAtchmnflSetupDao.insert(regInfo);
			} else if(CoreUtils.string.equals(Code.flag.수정,regInfo.getFlag())) {
				if(usptBsnsAtchmnflSetupDao.update(regInfo) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "수정"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}

	/**
	 * 첨부파일설정 삭제
	 * @param bsnsCd
	 * @param atchmnflSetupIdList
	 */
	public void removeAtchmnfl(String bsnsCd, List<AtchmnflSetup> atchmnflSetupIdList) {
		SecurityUtils.checkWorkerIsInsider();
		if(usptBsnsDao.selectBsnsPblancCount(bsnsCd) >0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n수정할 수 없습니다.");
		}
		for(AtchmnflSetup setupInfo : atchmnflSetupIdList) {
			UsptBsnsAtchmnflSetup info = usptBsnsAtchmnflSetupDao.select(bsnsCd, setupInfo.getAtchmnflSetupId());
			if(usptBsnsAtchmnflSetupDao.delete(info) != 1)
				throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			attachmentService.removeFile_keepEmptyGroup(config.getDir().getUpload(), info.getFormatAttachmentId());
		}
	}


	/**
	 * 사업 삭제
	 * @param bsnsCd
	 */
	public void remove(String bsnsCd) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		UsptBsns info = usptBsnsDao.select(bsnsCd);
		if(info == null) {
			throw new InvalidationException("요청하신 사업에 대한 정보가 없습니다.");
		}

		if(usptBsnsDao.selectBsnsPblancCount(bsnsCd) > 0 ) {
			throw new InvalidationException("해당 사업이 사용중입니다. \n삭제할 수 없습니다.");
		}

		Date now = new Date();
		info.setUpdatedDt(now);
		info.setUpdaterId(worker.getMemberId());

		if(usptBsnsDao.updateUnable(info) != 1) {
			throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
		}
	}


	/**
	 * 과제분야 목록 조회
	 * @param bsnsCd
	 * @return
	 */
	public List<UsptBsnsApplyRealm> getApplyRealmList(String bsnsCd) {
		SecurityUtils.checkWorkerIsInsider();
		return usptBsnsApplyRealmDao.selectList(bsnsCd);
	}

}
