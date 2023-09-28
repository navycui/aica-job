package aicluster.pms.api.expertClfc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.expertClfc.dto.ExpertClChargerParam;
import aicluster.pms.api.expertClfc.dto.ExpertClfcParam;
import aicluster.pms.common.dao.CmmtCodeDao;
import aicluster.pms.common.dao.CmmtInsiderDao;
import aicluster.pms.common.dao.UsptExpertClChargerDao;
import aicluster.pms.common.dao.UsptExpertClDao;
import aicluster.pms.common.dao.UsptExpertClMapngDao;
import aicluster.pms.common.entity.CmmtCode;
import aicluster.pms.common.entity.CmmtInsider;
import aicluster.pms.common.entity.UsptExpertCl;
import aicluster.pms.common.entity.UsptExpertClCharger;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExpertClfcService {

	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private UsptExpertClDao usptExpertClDao;
	@Autowired
	private CmmtCodeDao cmmtCodeDao;
	@Autowired
	private UsptExpertClChargerDao usptExpertClChargerDao;		/*전문가분류담당자*/
	@Autowired
	private CmmtInsiderDao cmmtInsiderDao;		/*전문가분류담당자*/
	@Autowired
	private UsptExpertClMapngDao usptExpertClMapngDao;


	/**
	 * 전문가분류 등록(CODE에서)
	 * @return List<ExpertClDto>
	 */
	public void addExpertClfcFromCode() {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		Date now = new Date();

		/*코드테이블 조회*/
		String codeGroup = "DEPT_CD";		/*전문가분류*/
		String codeType = "DEPT_0100";	/*코드구분*/
		List<CmmtCode> codeList = cmmtCodeDao.selectList_enabled(codeGroup, codeType);
		/*전문가분류 등록된 0depth 코드 조회(root)*/
		String parntsExpertClId = usptExpertClDao.selectExpertClfcOneTree();
		/*전문가분류 등록된 1depth 코드 조회(팀)*/
		List<UsptExpertCl> expertClTwoList = usptExpertClDao.selectExpertClfcTwoTreeList();
		int sortOrderMaxNo = usptExpertClDao.selectExpertClfcSortOrderMax();

		/*
		 *  	분류코드가 동일하면 명만 수정 후 codeList에서 삭제
		 */
		UsptExpertCl inputParam = new UsptExpertCl();
		inputParam.setCreatedDt(now);
		inputParam.setCreatorId(worker.getMemberId());
		inputParam.setUpdatedDt(now);
		inputParam.setUpdaterId(worker.getMemberId());
		//수정
		if(expertClTwoList.size()>0) {
			for(int i = 0 ;  i< codeList.size() ; i++) {
				for(int j = 0 ;  j< expertClTwoList.size() ; j++) {
					if(string.equals(	codeList.get(i).getCode(), expertClTwoList.get(j).getExpertClId())) {
						inputParam.setExpertClId(codeList.get(i).getCode());
						inputParam.setExpertClNm(codeList.get(i).getCodeNm());
						usptExpertClDao.updateExpertClNm(inputParam);
						codeList.remove(i);
					}
				}
			}
		}
		//신규등록
		for(int i = 0 ;  i< codeList.size() ; i++) {
			inputParam.setParntsExpertClId(parntsExpertClId);
			inputParam.setExpertClId(codeList.get(i).getCode());
			inputParam.setExpertClNm(codeList.get(i).getCodeNm());
			inputParam.setSortOrdrNo(sortOrderMaxNo);
			inputParam.setEnabled(true);
			usptExpertClDao.insertExpertCl(inputParam);
			sortOrderMaxNo++;
		}
	}

	/**
	 * 전문가단 트리 내담당목록 조회
	 * @param
	 * @return List<ExpertClDto>
	 */
	public List<UsptExpertCl> getExpertClfcMyTreeList(ExpertClfcParam expertClfcParam) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String memberId = worker.getMemberId();
		String expertClId = expertClfcParam.getExpertClId();

		List<UsptExpertCl> treeList = new ArrayList<>();
		List<UsptExpertCl> returnTreeList = new ArrayList<>();

		//입력받은 분류 조회
		if(CoreUtils.string.equals(expertClId, "ALL")) {
			returnTreeList = usptExpertClDao.selectExpertClfcMyTreeList(null);	//전체
		}else if( CoreUtils.string.isNotBlank(expertClId)) {
			returnTreeList = usptExpertClDao.selectExpertClfcMyTreeList(expertClId);	//특정
		}else {
			//나의 목록 조회
			List<UsptExpertClCharger> selectResultList = usptExpertClChargerDao.selectListMyExpertCl(memberId);
			log.debug("#####	selectResultList : {}", selectResultList.toString());

			for(UsptExpertClCharger result : selectResultList) {
				log.debug("#####	result.getExpertClId : {}", result.getExpertClId());
				treeList =usptExpertClDao.selectExpertClfcMyTreeList(result.getExpertClId());
				returnTreeList.addAll(treeList);
			}
		}
		return returnTreeList;
	}

	/**
	 * 전문가단 목록 조회
	 * @param expertClId
	 * @return List<ExpertClDto>
	 */
	public List<UsptExpertCl> getNextDepthList(ExpertClfcParam expertClfcParam) {
		SecurityUtils.checkWorkerIsInsider();
		String expertClId = expertClfcParam.getExpertClId();
		return usptExpertClDao.selectExpertClIdList(expertClId);
	}

	/**
	 * 전문가분류 등록 ,수정
	 * @param List<ExpertClfcParam>
	 * @return
	 */
	public void modifyExpertClfc(List<UsptExpertCl> usptExpertClList ) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if(usptExpertClList.size() <= 0) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "AICA 전문가단"));
		}

		Date now = new Date();

		/** 전문가단 저장 */
		for( UsptExpertCl inputParam : usptExpertClList) {

			inputParam.setCreatedDt(now);
			inputParam.setCreatorId(worker.getMemberId());
			inputParam.setUpdatedDt(now);
			inputParam.setUpdaterId(worker.getMemberId());

			if(CoreUtils.string.equals(Code.flag.등록, inputParam.getFlag())){
				inputParam.setExpertClId(CoreUtils.string.getNewId(Code.prefix.전문가분류));
				usptExpertClDao.insertExpertCl(inputParam);
			} else if(CoreUtils.string.equals(Code.flag.수정, inputParam.getFlag())){
				usptExpertClDao.updateExpertCl(inputParam);
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}

	/**
	 * 전문가단 삭제
	 * @param List<ExpertClfcParam>
	 * @return
	 */
	public void deleteExpertClfc(List<UsptExpertCl> usptExpertClList ) {

		SecurityUtils.checkWorkerIsInsider();
		if(usptExpertClList.size() <= 0) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "AICA 전문가단"));
		}

		/** 전문가단 저장 */
		for( UsptExpertCl inputParam : usptExpertClList) {

			if(CoreUtils.string.equals(Code.flag.삭제, inputParam.getFlag())){

				int chenkMapngCnt =  usptExpertClMapngDao.selectCheckMapngCnt(inputParam.getExpertClId());
				if(chenkMapngCnt>0) {
					throw new InvalidationException("사용중인 전문가 분류로 삭제가 불가능 합니다.");
				}
				if(usptExpertClDao.deleteExpertCl(inputParam) != 1) {
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
				}
				usptExpertClDao.deleteExpertClParnts(inputParam);
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}

	/**
	 * 전문가단 담당자 목록 조회
	 * @param parentBsnsClId
	 * @return
	 */
	public List<UsptExpertClCharger> getExpertClChargerList(ExpertClfcParam expertClfcParam) {
		SecurityUtils.checkWorkerIsInsider();
		return usptExpertClChargerDao.selectList(expertClfcParam.getExpertClId());
	}

	/**
	 * 전문가단 담당자 등록
	 * @param List<ExpertClfcParam>
	 * @return
	 */
	public void addExpertClfcMapng(List<UsptExpertClCharger> usptExpertClChargerList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		Date now = new Date();

		if(usptExpertClChargerList.size() <= 0) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "AICA 전문가단"));
		}
		/** 전문가단 담당자 등록 */
		for( UsptExpertClCharger inputParam : usptExpertClChargerList) {
			inputParam.setCreatedDt(now);
			inputParam.setCreatorId(worker.getMemberId());
			inputParam.setUpdatedDt(now);
			inputParam.setUpdaterId(worker.getMemberId());

			if(CoreUtils.string.equals(Code.flag.등록, inputParam.getFlag())){
				usptExpertClChargerDao.insert(inputParam);
			}else{
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}

	/**
	 * 전문가단 담당자 삭제
	 * @param List<ExpertClfcParam>
	 * @return
	 */
	public void removeExpertClCharger(List<UsptExpertClCharger> usptExpertClChargerList) {
		SecurityUtils.checkWorkerIsInsider();

		if(usptExpertClChargerList.size() <= 0) {
			throw new InvalidationException(String.format(Code.validateMessage.입력없음, "AICA 전문가단"));
		}

		/** 전문가단 담당자 삭제 */
		for( UsptExpertClCharger inputParam : usptExpertClChargerList) {

			if(CoreUtils.string.equals(Code.flag.삭제, inputParam.getFlag())){
				if(usptExpertClChargerDao.delete(inputParam.getExpertClId(), inputParam.getMemberId()) != 1)
					throw new InvalidationException(String.format(Code.validateMessage.DB오류, "삭제"));
			} else {
				throw new InvalidationException(String.format(Code.validateMessage.입력없음, "변경 flag"));
			}
		}
	}


	/**
	 * 전문가단 담당자 추가 목록 조회(팝업)
	 * @param parentBsnsClId
	 * @return
	 */
	public CorePagination<UsptExpertClCharger> getExpertClChargerPopupList(ExpertClChargerParam inputParam, Long page) {
		SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(inputParam.getItemsPerPage() == null) {
			inputParam.setItemsPerPage(ITEMS_PER_PAGE);
		}
		Long itemsPerPage = inputParam.getItemsPerPage();

		String deptNm = inputParam.getDeptNm();
		String memberNm = inputParam.getMemberNm();
		// 전체 건수 확인
		int totalItems = cmmtInsiderDao.selectListCnt(deptNm, memberNm);

		// 조회할 페이지 구간 정보 확인
		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, Long.valueOf(totalItems));

		// 페이지 목록 조회
		List<CmmtInsider> list = cmmtInsiderDao.selectList(deptNm, memberNm,info.getBeginRowNum(), itemsPerPage);
		List<UsptExpertClCharger> returnList = new ArrayList<>();
		UsptExpertClCharger outParam = null;
		//셋팅
		for(CmmtInsider selectParam : list ) {
			outParam = new UsptExpertClCharger();
			outParam.setMemberId(selectParam.getMemberId());
			outParam.setMemberNm(selectParam.getMemberNm());
			outParam.setDeptNm(selectParam.getDeptNm());
			outParam.setClsfNm(selectParam.getPositionNm());
			returnList.add(outParam);
		}
		CorePagination<UsptExpertClCharger> pagination = new CorePagination<>(info, returnList);

		// 목록 조회
		return pagination;
	}

	/**
	 * 전문가단 트리 내담당목록 Popup조회
	 * 공통(COMMON), 내업무 조회
	 * @param
	 * @return List<ExpertClDto>
	 */
	public List<UsptExpertCl> getExpertClfcMyTreeListPopup() {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		String memberId = worker.getMemberId();

		List<UsptExpertCl> treeList = new ArrayList<>();
		List<UsptExpertCl> returnTreeList = new ArrayList<>();

		//나의 담당목록 조회
		int sameCommonCl = 0;
		List<UsptExpertClCharger> selectResultList = usptExpertClChargerDao.selectListMyExpertCl(memberId);
		for(UsptExpertClCharger searchCommon : selectResultList) {
			if(CoreUtils.string.equals(searchCommon.getExpertClId(), "COMMON")) {
				sameCommonCl++;
			}
		}
		//공통 분류를 추가 한다.
		if(sameCommonCl <= 0) {
			treeList =usptExpertClDao.selectExpertClfcMyTreeList("COMMON");
			returnTreeList.addAll(treeList);
		}
		//담당자의 담당부분만
		for(UsptExpertClCharger result : selectResultList) {
			treeList =usptExpertClDao.selectExpertClfcMyTreeList(result.getExpertClId());
			returnTreeList.addAll(treeList);
		}

		return returnTreeList;
	}
}