package aicluster.mvn.api.resource.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.resource.dto.AlrsrcFoundInfParam;
import aicluster.mvn.common.dao.UsptResrceInvntryInfoDao;
import aicluster.mvn.common.dao.UsptResrceInvntryInfoHistDao;
import aicluster.mvn.common.dto.AlrsrcFninfRsrcDto;
import aicluster.mvn.common.dto.HistDtListItemDto;
import aicluster.mvn.common.entity.UsptResrceInvntryInfo;
import aicluster.mvn.common.entity.UsptResrceInvntryInfoHist;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.common.util.CodeExt.prefixId;
import aicluster.mvn.common.util.CodeExt.procType;
import aicluster.mvn.common.util.CodeExt.validateMessageExt;
import aicluster.mvn.common.util.MvnUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class AlrsrcFoundInfService {

	@Autowired
	UsptResrceInvntryInfoDao fninfDao;

	@Autowired
	UsptResrceInvntryInfoHistDao fninfHistDao;

	@Autowired
	MvnUtils mvnUtlis;

	/**
	 * 자원재고 목록 데이터에 자원ID(rsrc_id)가 동일한 건이 있는지 검증
	 *
	 * @param list : 자원재고 목록 데이터
	 * @param rsrcId : 자원ID
	 * @return true / false
	 */
	private boolean containsRsrcId(List<UsptResrceInvntryInfo> list, String rsrcId)
	{
		return list.stream().filter(obj -> obj.getRsrcId().equals(rsrcId)).findFirst().isPresent();
	}

	/**
	 * 자원재고 목록 데이터에 자원ID에 해당하는 자원재고정보의 처리유형코드(procTypeCd)를 변경한다.
	 *
	 * @param list : 자원재고 목록 데이터
	 * @param trgetFninf : 자원재고정보
	 */
	private void setFninfProcType(List<UsptResrceInvntryInfo> list, UsptResrceInvntryInfo trgetFninf)
	{
		UsptResrceInvntryInfo fninf =  list.stream().filter(obj -> obj.getRsrcId().equals(trgetFninf.getRsrcId())).findFirst().get();
		fninf.setProcTypeCd(trgetFninf.getProcTypeCd());
	}

	/**
	 * 처리유형코드별 이력 작업유형명 정의
	 *
	 * @param procTypeCd: 처리유형코드
	 * @return 작업유형명
	 */
	private String getWorkTypeNm(String procTypeCd)
	{
		String workTypeNm;

		if (string.isBlank(procTypeCd)) {
			return null;
		}

		switch (procTypeCd) {
			case CodeExt.procType.등록 :
				workTypeNm = CodeExt.workTypeNm.추가;
				break;
			case CodeExt.procType.수정 :
				workTypeNm = CodeExt.workTypeNm.재고변경;
				break;
			case CodeExt.procType.삭제 :
				workTypeNm = CodeExt.workTypeNm.삭제;
				break;
			default :
				workTypeNm = null;
		}

		return workTypeNm;
	}

	/**
	 * 자원재고이력정보 insert
	 *
	 * @param srcList : CUD 수행 전 자원재고정보 목록 데이터
	 * @param trgetList : CUD 대상 자원재고정보 목록 데이터
	 */
	private void insertHist(List<UsptResrceInvntryInfo> srcList, List<UsptResrceInvntryInfo> trgetList)
	{
		BnMember worker = SecurityUtils.getCurrentMember();

		Date now = new Date();

		// 이력데이터로 사용할 자원재고정보 목록 데이터 처리
		for (UsptResrceInvntryInfo trgetFninf : trgetList) {
			// 자원ID 동일건이 있으면 수정/삭제 처리유형코드로 정의하고, 신규는 목록 추가함.
			if (containsRsrcId(srcList, trgetFninf.getRsrcId())) {
				setFninfProcType(srcList, trgetFninf);
			}
			else {
				srcList.add(trgetFninf);
			}
		}

		// 이력 데이터 생성
		List<UsptResrceInvntryInfoHist> histList = new ArrayList<>();
		for (UsptResrceInvntryInfo fninf : srcList) {
			UsptResrceInvntryInfoHist hist = new UsptResrceInvntryInfoHist();
			property.copyProperties(hist, fninf);

			hist.setHistId(string.getNewId(prefixId.이력ID));
			hist.setHistDt(now);
			hist.setWorkTypeNm(getWorkTypeNm(fninf.getProcTypeCd()));
			hist.setWorkerId(worker.getMemberId());

			histList.add(hist);
		}

		// 자원재고이력정보 insert
		fninfHistDao.insertList(histList);
	}


	/**
	 * 자원할당재고정보 목록 조회
	 *
	 * @return 자원재고 목록
	 */
	public JsonList<UsptResrceInvntryInfo> getList()
	{
		SecurityUtils.checkWorkerIsInsider();

		List<UsptResrceInvntryInfo> list = fninfDao.selectList();

		return new JsonList<>(list);
	}

	/**
	 * 자원별 재고정보 조회
	 *
	 * @param rsrcId : 자원ID
	 * @return 자원재고정보
	 */
	public UsptResrceInvntryInfo get(String rsrcId)
	{
		SecurityUtils.checkWorkerIsInsider();

		return fninfDao.select(rsrcId);
	}

	/**
	 * 자원재고정보 일괄등록
	 *
	 * @param paramList : 자원재고정보 목록
	 */
	public void add(List<AlrsrcFoundInfParam> paramList)
	{
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if (paramList == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "입력할 자원재고정보"));
		}

		InvalidationsException errors = new InvalidationsException();

		// 입력 데이터 검증 및 수정대상 목록화
		for (AlrsrcFoundInfParam param : paramList) {
			if (!mvnUtlis.isValideCode(CodeExt.rsrcGroup.CODE_GROUP, param.getRsrcGroupCd())) {
				errors.add(String.format("[%s] " + validateMessageExt.입력오류, param.getRsrcTypeNm(), "자원그룹코드"));
				continue;
			}

			if (string.isNotBlank(param.getRsrcTypeUnitCd()) && !mvnUtlis.isValideCode(CodeExt.rsrcTypeUnit.CODE_GROUP, param.getRsrcTypeUnitCd())) {
				errors.add(String.format("[%s] " + validateMessageExt.입력오류, param.getRsrcTypeNm(), "자원타입단위코드"));
				continue;
			}
		}

		// 검증 오류 Exception
		if (errors.size() > 0) {
			throw errors;
		}

		// 자원재고정보 전체 목록 조회(CUD 작업 후 이력정보를 생성하기위해 미리 조회)
		List<UsptResrceInvntryInfo> fninfList = fninfDao.selectList();

		// 입력 VO 목록 생성
		Date now = new Date();
		List<UsptResrceInvntryInfo> insList = new ArrayList<>();
		for(AlrsrcFoundInfParam param : paramList) {
			UsptResrceInvntryInfo usptAlrsrcFninf = new UsptResrceInvntryInfo();
			property.copyProperties(usptAlrsrcFninf, param);

			usptAlrsrcFninf.setRsrcId(string.getNewId(prefixId.자원ID));
			usptAlrsrcFninf.setInvtQy(param.getInvtQy());
			usptAlrsrcFninf.setTotalQy(param.getInvtQy());  // 입력의 경우 전체수량은 재고수량과 동일하다.
			usptAlrsrcFninf.setCreatorId(worker.getMemberId());
			usptAlrsrcFninf.setCreatedDt(now);
			usptAlrsrcFninf.setUpdaterId(worker.getMemberId());
			usptAlrsrcFninf.setUpdatedDt(now);

			// 처리유형코드를 '등록'로 정의
			usptAlrsrcFninf.setProcTypeCd(procType.등록);

			// 입력 VO 목록 추가
			insList.add(usptAlrsrcFninf);
		}

		// 자원재고정보 insert
		fninfDao.insertList(insList);

		// 자원재고이력정보 insert
		insertHist(fninfList, insList);
	}

	/**
	 * 자원재고정보 일괄수정
	 *
	 * @param paramList : 자원재고정보 목록
	 */
	public void modify(List<AlrsrcFoundInfParam> paramList)
	{
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if (paramList == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "수정할 자원재고정보"));
		}

		InvalidationsException errors = new InvalidationsException();

		// 입력 데이터 검증 및 수정대상 목록화
		List<UsptResrceInvntryInfo> uptList = new ArrayList<>();
		for (AlrsrcFoundInfParam param : paramList) {
			UsptResrceInvntryInfo usptAlrsrcFninf = fninfDao.select(param.getRsrcId());
			if (usptAlrsrcFninf == null) {
				errors.add(String.format("[%s] " + validateMessageExt.조회결과없음, param.getRsrcTypeNm(), "자원재고정보"));
				continue;
			}

			if (!mvnUtlis.isValideCode(CodeExt.rsrcGroup.CODE_GROUP, param.getRsrcGroupCd())) {
				errors.add(String.format("[%s] " + validateMessageExt.입력오류, param.getRsrcTypeNm(), "자원그룹코드"));
				continue;
			}

			if (string.isNotBlank(param.getRsrcTypeUnitCd()) && !mvnUtlis.isValideCode(CodeExt.rsrcTypeUnit.CODE_GROUP, param.getRsrcTypeUnitCd())) {
				errors.add(String.format("[%s] " + validateMessageExt.입력오류, param.getRsrcTypeNm(), "자원타입단위코드"));
				continue;
			}

			uptList.add(usptAlrsrcFninf);
		}

		// 검증 오류 Exception
		if (errors.size() > 0) {
			throw errors;
		}

		// 자원재고정보 전체 목록 조회(CUD 작업 후 이력정보를 생성하기위해 미리 조회)
		List<UsptResrceInvntryInfo> fninfList = fninfDao.selectList();

		// 수정 데이터 VO 적용 및 이력 데이터 생성
		Date now = new Date();
		for (int i=0; i<paramList.size(); i++) {
			AlrsrcFoundInfParam param = paramList.get(i);
			UsptResrceInvntryInfo usptAlrsrcFninf = uptList.get(i);

			// 전체수량 계산(재고량 + 할당량)
			int totalQty = param.getInvtQy() + usptAlrsrcFninf.getDstbQy();

			// 수정내용 반영
			property.copyProperties(usptAlrsrcFninf, param);
			usptAlrsrcFninf.setInvtQy(param.getInvtQy());
			usptAlrsrcFninf.setTotalQy(totalQty);
			usptAlrsrcFninf.setUpdaterId(worker.getMemberId());
			usptAlrsrcFninf.setUpdatedDt(now);

			// 처리유형코드를 '수정'로 정의
			usptAlrsrcFninf.setProcTypeCd(CodeExt.procType.수정);
		}

		// 자원재고정보 update
		fninfDao.updateList(uptList);

		// 자원재고이력정보 insert
		insertHist(fninfList, uptList);
	}

	/**
	 * 자원재고정보 일괄삭제
	 *
	 * @param paramList : 자원ID 목록
	 */
	public void remove(List<AlrsrcFoundInfParam> paramList)
	{
		SecurityUtils.checkWorkerIsInsider();

		if (paramList == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "삭제할 자원재고정보"));
		}

		InvalidationsException errors = new InvalidationsException();

		// 입력 데이터 검증 및 삭제대상 목록화
		List<UsptResrceInvntryInfo> delList = new ArrayList<>();
		for (AlrsrcFoundInfParam param : paramList) {
			UsptResrceInvntryInfo usptAlrsrcFninf = fninfDao.select(param.getRsrcId());
			if (usptAlrsrcFninf == null) {
				errors.add(String.format("[%s] " + validateMessageExt.조회결과없음, param.getRsrcTypeNm(), "자원재고정보"));
				continue;
			}

			if (usptAlrsrcFninf.getDstbQy() > 0) {
				errors.add(String.format("[%s] 할당되어 이용 중인 업체가 존재합니다. 해당 자원에 이용 중인 업체가 없는 경우에만 삭제할 수 있습니다.", param.getRsrcTypeNm()));
				continue;
			}

			// 처리유형코드를 '삭제'로 정의
			usptAlrsrcFninf.setProcTypeCd(CodeExt.procType.삭제);

			delList.add(usptAlrsrcFninf);
		}

		// 검증 오류 Exception
		if (errors.size() > 0) {
			throw errors;
		}

		// 자원재고정보 전체 목록 조회(CUD 작업 후 이력정보를 생성하기위해 미리 조회)
		List<UsptResrceInvntryInfo> fninfList = fninfDao.selectList();

		// 데이터 삭제 및 이력 데이터 생성
		for (UsptResrceInvntryInfo usptAlrsrcFninf : delList) {
			// 자원재고정보 delete
			fninfDao.delete(usptAlrsrcFninf.getRsrcId());
		}

		// 자원재고이력정보 insert
		insertHist(fninfList, delList);
	}

	/**
	 * 자원재고정보 이력일자 목록 조회
	 *
	 * @param pageParam : 페이징 Parameter
	 * @return : 자원재고 이력일자 목록
	 */
	public CorePagination<HistDtListItemDto> getHistDays(CorePaginationParam pageParam)
	{
		SecurityUtils.checkWorkerIsInsider();

		long totalItems = fninfHistDao.selectHistDtGroupList_count();
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		List<HistDtListItemDto> list = fninfHistDao.selectHistDtGroupList(info.getBeginRowNum(), info.getItemsPerPage(), info.getTotalItems());

		return new CorePagination<>(info, list);
	}

	/**
	 * 자원재고 일자별 이력 목록 조회
	 *
	 * @param histDt : 이력일자
	 * @return : 자원재고정보 일자별 목록 조회
	 */
	public JsonList<UsptResrceInvntryInfoHist> getHist(String histDt)
	{
		SecurityUtils.checkWorkerIsInsider();

		List<UsptResrceInvntryInfoHist> list = fninfHistDao.selectList_histDt(histDt);

		return new JsonList<>(list);
	}

	/**
	 * 자원그룹별 자원 코드성 목록 조회
	 *
	 * @param rsrcGroupCd : 자원그룹코드
	 * @return 자원유형목록
	 */
	public JsonList<AlrsrcFninfRsrcDto> getCodeList(String rsrcGroupCd)
	{
		if (string.isBlank(rsrcGroupCd)) {
			throw new InvalidationException(String.format(validateMessageExt.선택없음, "자원그룹코드"));
		}

		mvnUtlis.validateCode("RSRC_GROUP", rsrcGroupCd, "자원그룹코드");

		List<AlrsrcFninfRsrcDto> list = fninfDao.selectRsrcCodeList(rsrcGroupCd);

		return new JsonList<>(list);
	}
}
