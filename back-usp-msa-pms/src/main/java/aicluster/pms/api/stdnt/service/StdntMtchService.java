package aicluster.pms.api.stdnt.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.stdnt.dto.StdntListParam;
import aicluster.pms.api.stdnt.dto.StdntMtchListParam;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntDao;
import aicluster.pms.common.dao.UsptBsnsPblancApplcntEntDao;
import aicluster.pms.common.dao.UsptStdntMtchDao;
import aicluster.pms.common.dto.MtchStdnListDto;
import aicluster.pms.common.dto.StdnListDto;
import aicluster.pms.common.dto.StdntMtchDto;
import aicluster.pms.common.dto.StdntMtchListDto;
import aicluster.pms.common.entity.UsptBsnsPblancApplcntEnt;
import aicluster.pms.common.entity.UsptStdntMtch;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;

@Service
public class StdntMtchService {

	public static final long ITEMS_PER_PAGE = 10L;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
	@Autowired
	private UsptBsnsPblancApplcntDao usptBsnsPblancApplcntDao;
	@Autowired
	private UsptBsnsPblancApplcntEntDao usptBsnsPblancApplcntEntDao;
	@Autowired
	private UsptStdntMtchDao usptStdntMtchDao;

	/**
	 * 목록 조회
	 * @param param
	 * @param page
	 * @param ordtmRcrit
	 * @return
	 */
	public CorePagination<StdntMtchListDto> getList(StdntMtchListParam param, Long page, Boolean ordtmRcrit){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}

		param.setInsiderId(worker.getMemberId());
		long totalItems = usptBsnsPblancApplcntDao.selectStdntMtchListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		List<StdntMtchListDto> list = usptBsnsPblancApplcntDao.selectStdntMtchList(param);
		CorePagination<StdntMtchListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 목록 엑셀 저장
	 * @param param
	 * @param ordtmRcrit
	 * @return
	 */
	public List<StdntMtchListDto> getListExcelDwld(StdntMtchListParam param, Boolean ordtmRcrit){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		param.setInsiderId(worker.getMemberId());
		long totalItems = usptBsnsPblancApplcntDao.selectStdntMtchListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		param.setBeginRowNum(info.getBeginRowNum());
		param.setItemsPerPage(totalItems);
		return usptBsnsPblancApplcntDao.selectStdntMtchList(param);
	}

	/**
	 * 상세 기업정보 조회
	 * @param request
	 * @param lastSlctnTrgetId
	 * @return
	 */
	public StdntMtchDto get(HttpServletRequest request, String lastSlctnTrgetId) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		StdntMtchDto dto = usptBsnsPblancApplcntDao.selectStdntMtch(lastSlctnTrgetId, worker.getMemberId());
		if(dto == null) {
			throw new InvalidationException("요청한 교육생매칭 업체정보가 없습니다.");
		}

		UsptBsnsPblancApplcntEnt entInfo = usptBsnsPblancApplcntEntDao.select(dto.getApplyId());
		if(entInfo != null) {
			entInfo.setFxnum(entInfo.getDecFxnum());
			entInfo.setCeoEmail(entInfo.getDecCeoEmail());
			entInfo.setCeoTelno(entInfo.getDecCeoTelno());
			entInfo.setReprsntTelno(entInfo.getDecReprsntTelno());
			dto.setApplcntEnt(entInfo);
		}

		Date bsnsBgndt = string.toDate(dto.getBsnsBgnde());
		Date bsnsEnddt = string.toDate(dto.getBsnsEndde());
		if (bsnsBgndt != null && bsnsEnddt != null ) {
			Date lastdt = string.toDate(date.getCurrentDate("yyyy") + "1231");
			dto.setBsnsPd("협약 체결일 ~ " + date.format(bsnsEnddt, "yyyy-MM-dd"));
			int allDiffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, bsnsEnddt);
			if(date.compareDay(bsnsEnddt, lastdt) == 1) {
				int diffMonths = CoreUtils.date.getDiffMonths(bsnsBgndt, lastdt);
				dto.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(lastdt, "yyyy-MM-dd") + "(" + diffMonths + "개월)");
			} else {
				dto.setBsnsPdYw(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
			}
			dto.setBsnsPdAll(date.format(bsnsBgndt, "yyyy-MM-dd") + " ~ " + date.format(bsnsEnddt, "yyyy-MM-dd") + "(" + allDiffMonths + "개월)");
		}

		// 이력 생성
		if(dto.getApplcntEnt() != null) {
			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
					.memberId(worker.getMemberId())
					.memberIp(CoreUtils.webutils.getRemoteIp(request))
					.workTypeNm("조회")
					.workCn("교육생매칭 상세조회")
					.trgterId(dto.getMemberId())
					.email(dto.getApplcntEnt().getCeoEmail())
					.birthday("")
					.mobileNo(dto.getApplcntEnt().getCeoTelno())
					.build();
			indvdlInfSrchUtils.insert(logParam);
		}

		return dto;
	}

	/**
	 * 상세 매칭교육생 목록 조회
	 * @param lastSlctnTrgetId
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	public CorePagination<MtchStdnListDto> getMtchStdntList(String lastSlctnTrgetId, Long page, Long itemsPerPage){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		StdntMtchDto dto = usptBsnsPblancApplcntDao.selectStdntMtch(lastSlctnTrgetId, worker.getMemberId());
		if(dto == null) {
			throw new InvalidationException("요청한 교육생매칭 업체정보가 없습니다.");
		}

		if(page == null) {
			page = 1L;
		}
		if(itemsPerPage == null) {
			itemsPerPage = ITEMS_PER_PAGE;
		}

		long totalItems = usptStdntMtchDao.selectMtchStdnListCount(lastSlctnTrgetId);
		CorePaginationInfo info = new CorePaginationInfo(page, itemsPerPage, totalItems);
		List<MtchStdnListDto> list = usptStdntMtchDao.selectMtchStdnList(lastSlctnTrgetId, itemsPerPage, info.getBeginRowNum());
		CorePagination<MtchStdnListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}

	/**
	 * 상세 매칭교육생 목록 엑셀 다운로드
	 * @param lastSlctnTrgetId
	 * @return
	 */
	public List<MtchStdnListDto> getMtchStdntListExcelDwld(String lastSlctnTrgetId){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		StdntMtchDto dto = usptBsnsPblancApplcntDao.selectStdntMtch(lastSlctnTrgetId, worker.getMemberId());
		if(dto == null) {
			throw new InvalidationException("요청한 교육생매칭 업체정보가 없습니다.");
		}

		long totalItems = usptStdntMtchDao.selectMtchStdnListCount(lastSlctnTrgetId);
		CorePaginationInfo info = new CorePaginationInfo(1L, totalItems, totalItems);
		return usptStdntMtchDao.selectMtchStdnList(lastSlctnTrgetId, info.getItemsPerPage(), info.getBeginRowNum());
	}


	/**
	 * 매칭교육생 삭제
	 * @param lastSlctnTrgetId
	 * @param deleteMtchStdntList
	 */
	public void removeMtchStdnt(String lastSlctnTrgetId, List<UsptStdntMtch> deleteMtchStdntList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		StdntMtchDto dto = usptBsnsPblancApplcntDao.selectStdntMtch(lastSlctnTrgetId, worker.getMemberId());
		if(dto == null) {
			throw new InvalidationException("요청한 교육생매칭 업체정보가 없습니다.");
		}
		if(deleteMtchStdntList == null || deleteMtchStdntList.size() == 0) {
			throw new InvalidationException("삭제 할 교육생 정보가 없습니다.");
		}
		for(UsptStdntMtch deleteInfo : deleteMtchStdntList) {
			if(CoreUtils.string.isBlank(deleteInfo.getApplyId())) {
				throw new InvalidationException("삭제 할 교육생 정보가 잘못되었습니다.");
			}
			deleteInfo.setLastSlctnTrgetId(lastSlctnTrgetId);

			if(usptStdntMtchDao.delete(deleteInfo) != 1) {
				throw new InvalidationException("매칭 교육생 삭제 중 오류가 발생했습니다.");
			}
		}
	}


	/**
	 * 교육생 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	public CorePagination<StdnListDto> getStdntList(StdntListParam param, Long page){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		StdntMtchDto dto = usptBsnsPblancApplcntDao.selectStdntMtch(param.getLastSlctnTrgetId(), worker.getMemberId());
		if(dto == null) {
			throw new InvalidationException("요청한 교육생매칭 업체정보가 없습니다.");
		}

		if(page == null) {
			page = 1L;
		}
		if(param.getItemsPerPage() == null) {
			param.setItemsPerPage(ITEMS_PER_PAGE);
		}

		Date rceptStartDt = string.toDate(param.getRceptStartDate());
		param.setRceptStartDate(date.format(rceptStartDt, "yyyyMMdd"));
		Date rceptEndDt = string.toDate(param.getRceptEndDate());
		param.setRceptEndDate(date.format(rceptEndDt, "yyyyMMdd"));

		long totalItems = usptStdntMtchDao.selectStdnListCount(param);
		CorePaginationInfo info = new CorePaginationInfo(page, param.getItemsPerPage(), totalItems);
		List<StdnListDto> list = usptStdntMtchDao.selectStdnList(param);
		CorePagination<StdnListDto> pagination = new CorePagination<>(info, list);
		return pagination;
	}


	/**
	 * 교육생 매칭 등록
	 * @param lastSlctnTrgetId
	 * @param mtchStdntList
	 */
	public void add(String lastSlctnTrgetId, List<UsptStdntMtch> mtchStdntList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		StdntMtchDto dto = usptBsnsPblancApplcntDao.selectStdntMtch(lastSlctnTrgetId, worker.getMemberId());
		if(dto == null) {
			throw new InvalidationException("요청한 교육생매칭 업체정보가 없습니다.");
		}

		if(!CoreUtils.string.equals(Code.사업담당자_수정권한, dto.getChargerAuthorCd())) {
			throw new InvalidationException("저장 권한이 없습니다.");
		}

		if(mtchStdntList == null || mtchStdntList.size() == 0) {
			throw new InvalidationException("등록 할 교육생 정보가 없습니다.");
		}

		Date now = new Date();
		for(UsptStdntMtch regInfo : mtchStdntList) {
			if(CoreUtils.string.isBlank(regInfo.getApplyId())) {
				throw new InvalidationException("등록 할 교육생 정보가 잘못되었습니다.");
			}
			regInfo.setLastSlctnTrgetId(lastSlctnTrgetId);
			regInfo.setCreatedDt(now);
			regInfo.setCreatorId(worker.getMemberId());
			usptStdntMtchDao.insert(regInfo);
		}
	}

}
