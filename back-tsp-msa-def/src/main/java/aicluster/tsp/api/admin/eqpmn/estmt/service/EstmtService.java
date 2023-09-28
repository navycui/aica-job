package aicluster.tsp.api.admin.eqpmn.estmt.service;

import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtListParam;
import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtOzReportParam;
import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtProcessParam;
import aicluster.tsp.api.admin.eqpmn.use.param.UseDscntParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.api.common.param.CommonCalcParam;
import aicluster.tsp.common.dao.TsptEqpmnDscntDao;
import aicluster.tsp.common.dao.TsptEqpmnEstmtDao;
import aicluster.tsp.common.dto.ApplcntDto;
import aicluster.tsp.common.dto.EqpmnEstmtDetailDto;
import aicluster.tsp.common.dto.EqpmnEstmtListDto;
import aicluster.tsp.common.dto.EqpmnUseDscntDto;
import aicluster.tsp.common.entity.TsptEqpmnEstmtReqstHist;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.excel.dto.ExcelMergeRows;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
public class EstmtService {

	public static final long ITEMS_PER_PAGE = 5;

	@Autowired
	private FwUserDao fwUserDao;

	@Autowired
	private TsptEqpmnEstmtDao tsptEqpmnEstmtDao;

	@Autowired
	private CommonService commonService;
	@Autowired
	private TsptEqpmnDscntDao dscntDao;

	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

	@Autowired
	private HttpServletRequest request;

	// 견적 목록 조회
	public CorePagination<EqpmnEstmtListDto> getEstmtList(EstmtListParam estmtListParam, CorePaginationParam cpParam) {

		// 검색 글자수 제한
		Integer searchString = 100;
		if (searchString > Integer.MAX_VALUE) {
			throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
		}
		if (CoreUtils.string.isNotBlank(estmtListParam.getUserNm()) && estmtListParam.getUserNm().length() > searchString){
			throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
		}
		if (CoreUtils.string.isNotBlank(estmtListParam.getEntrprsNm()) && estmtListParam.getEntrprsNm().length() > searchString){
			throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
		}
		if (CoreUtils.string.isNotBlank(estmtListParam.getEqpmnNmKorean()) && estmtListParam.getEqpmnNmKorean().length() > searchString){
			throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
		}
		if (CoreUtils.string.isNotBlank(estmtListParam.getAssetsNo()) && estmtListParam.getAssetsNo().length() > searchString){
			throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
		}
		if (CoreUtils.string.isNotBlank(estmtListParam.getRceptNo()) && estmtListParam.getRceptNo().length() > searchString){
			throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
		}

		boolean isExcel = false;
		// 전체 건수 확인
		long totalItems = tsptEqpmnEstmtDao.selectEqpmnEstmtCount(estmtListParam);

		// 조회할 페이지 구간 정보 확인
		CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

		// 페이지 목록 조회
		//List<EqpmnEstmtListDto> list = tsptEqpmnEstmtDao.selectEquipmentEstmtList(isExcel, info.getBeginRowNum(), itemsPerPage, estmtListParam);
		List<EqpmnEstmtListDto> list = tsptEqpmnEstmtDao.selectEqpmnEstmtList(isExcel, info.getBeginRowNum(), info.getItemsPerPage(), estmtListParam);
//		for(EqpmnEstmtListDto numberList : list)
//		{
//			Pattern pattern = Pattern.compile("^ER(0)*[0-9]$");
//			String receptNo = numberList.getRceptNo();
//			Matcher matcher = pattern.matcher(receptNo);
//			numberList.setNumber(Integer.parseInt(matcher.group(-1)));
//		}
		CorePagination<EqpmnEstmtListDto> pagination = new CorePagination<>(info, list);
		// 목록 조회
		return pagination;
	}
	// 견적 Excel 다운로드
	public ModelAndView getEstmtListExcelDownload(EstmtListParam estmtListParam) {
		boolean isExcel = true;
		List<EqpmnEstmtListDto> list = tsptEqpmnEstmtDao.selectEqpmnEstmtList(isExcel, 0L, 0L, estmtListParam);
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("견적요청 현황");
		ExcelSheet<EqpmnEstmtListDto> sheet = new ExcelSheet<>();
		ExcelMergeRows mergeRegions = new ExcelMergeRows();
		sheet.setMergeRegions(mergeRegions);
		sheet.addRows(list);
		sheet.setHeaders("번호", "신청상태", "구분", "사업자명/이름", "자산번호", "장비명(국문)", "사용시작일", "사용종료일", "지불방법",  "신청일시");
		sheet.setProperties("number","exReqstSttus", "exMberDiv", "entrprsNm", "assetsNo", "eqpmnNmKorean", "exUseBeginDt", "exUseEndDt", "exPymntMth",  "exCreatDt");
		sheet.setTitle("견적요청");
		sheet.setSheetName("견적요청");
        wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	// 견적요청 신청정보
	public EqpmnEstmtDetailDto getEstmtDetailInfo(String eqpmnEstmtId) {
		EqpmnEstmtDetailDto estmtDetailParam = tsptEqpmnEstmtDao.selectEqpmnEstmtDetail(eqpmnEstmtId);

		if(estmtDetailParam==null) {
			throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
		}

		ApplcntDto applcnt = commonService.getApplcnt(estmtDetailParam.getApplcntId());

		estmtDetailParam.setEmail(applcnt.getEmail());
		estmtDetailParam.setMberDiv(applcnt.getMberDiv());
		estmtDetailParam.setEntrprsNm(applcnt.getEntrprsNm());
		estmtDetailParam.setOfcps(applcnt.getOfcps());
		estmtDetailParam.setCttpc(applcnt.getCttpc());
		estmtDetailParam.setUserNm(applcnt.getUserNm());
		// 1일 가용시간 추가
		CommonCalcParam calc = commonService.getWorkingRntfee(estmtDetailParam.getEqpmnId(), estmtDetailParam.getUseBeginDt(), estmtDetailParam.getUseEndDt(), estmtDetailParam.getTkoutAt());
		estmtDetailParam.setUsefulHour(calc.getUsefulHour());
		// 할인 적용
		estmtDetailParam.setDscntList(dscntDao.selectEqpmnDscntList(estmtDetailParam.getEqpmnId()));

		// 첨부파일 정보
		if(!CoreUtils.string.isBlank(estmtDetailParam.getAtchmnflGroupId())){
			estmtDetailParam.setAttachmentList(commonService.getAttachmentGroup(estmtDetailParam.getAtchmnflGroupId()));
		}
		String reqstId = tsptEqpmnEstmtDao.selectReqstId(eqpmnEstmtId);
		if (CoreUtils.string.isNotBlank(reqstId)) {
			estmtDetailParam.setReqstId(reqstId);
		}

		// log 정보생성
		BnMember worker = TspUtils.getMember();
		if (!CoreUtils.string.equals(worker.getMemberId(), estmtDetailParam.getApplcntId())) {
			String memberIp = CoreUtils.webutils.getRemoteIp(request);
			if(!"0:0:0:0:0:0:0:1".equals(memberIp)) {
				LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
						.memberId(worker.getMemberId())
						.memberIp(memberIp)
						//.memberIp("127.0.0.1")
						.workTypeNm("조회")
						.workCn("장비 견적요청 상세정보")
						.trgterNm(applcnt.getUserNm())
						.trgterId(estmtDetailParam.getApplcntId())
						.email(applcnt.getEmail())
						//.birthday("응답값에 포함된 원문 생년월일")
						.mobileNo(applcnt.getCttpc())
						.build();
				indvdlInfSrchUtils.insert(logParam);
			}
		}

		return estmtDetailParam;
	}
	
	// 견적요청 신청정보 조정 사용금액 수정
	public void putEstmtDetailCalc(UseDscntParam param) {
		EqpmnUseDscntDto data = tsptEqpmnEstmtDao.selectEstmtDscnt(param.getReqstId(), param.getDscntId());
		// 예외처리? 검증?
		if (data == null) {
			throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
		}
		// 최종사용료
		Integer rntfee = 0;
		// 예상사용료
		Integer expectRntfee = 0;
		// 할인금액
		Integer dscntAmount = 0;
		// 사용시간(분)
		Integer usgtm = 0;
		// 예상사용시간(분)
		//Integer expectUsgtm = 0;

		CommonCalcParam calc = commonService.getWorkingRntfee(data.getEqpmnId(), data.getUseBeginDt(), data.getUseEndDt(), data.getTkoutAt());

		//expectUsgtm = calc.getTotalMin();
		// 할인만 적용(시간 계산)
		if (param.getUsgtm() == null|| param.getUsgtm() == 0){
			expectRntfee = calc.getRntfee();
			dscntAmount = (int)(data.getDscntRate() / 100.0 * expectRntfee);
			rntfee = expectRntfee - dscntAmount;
		}
		// 시간 적용
		else {
			usgtm = param.getUsgtm() * 60;
			expectRntfee = usgtm / 60 * data.getRntfeeHour();
			dscntAmount = (int)(data.getDscntRate() / 100.0 * expectRntfee);
			rntfee = expectRntfee - dscntAmount;
		}

		BnMember worker = TspUtils.getMember();

		tsptEqpmnEstmtDao.updateEstmtDscnt(param.getReqstId(), param.getDscntId(), worker.getMemberId(), rntfee, usgtm, dscntAmount);

		TsptEqpmnEstmtReqstHist hist = TsptEqpmnEstmtReqstHist.builder()
				.histId(CoreUtils.string.getNewId("reqstHist-"))
				.estmtId(param.getReqstId())
				.opetrId(worker.getLoginId())
				.processKnd(TspCode.processKnd.ACTL_USE_PAYMENT.toString())
				.processResn(String.format(TspCode.histMessage.처리이력, TspCode.processKnd.ACTL_USE_PAYMENT.getTitle()))
				.mberId(worker.getMemberId())
				.mberNm(worker.getMemberNm())
				.build();

		tsptEqpmnEstmtDao.insertEqpmnEstmtHist(hist);
	}

	public CorePagination<TsptEqpmnEstmtReqstHist> getEstmtHistList(String estmtId, CorePaginationParam cpParam) {
		boolean isExcel = false;
		// 전체 건수 확인
		long totalItems = tsptEqpmnEstmtDao.selectEqpmnEstmtHistCount(estmtId);

		// 조회할 페이지 구간 정보 확인
		CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

		// 페이지 목록 조회
		List<TsptEqpmnEstmtReqstHist> list = tsptEqpmnEstmtDao.selectEqpmnEstmtHistList(estmtId, isExcel, info.getBeginRowNum(), info.getItemsPerPage());
		CorePagination<TsptEqpmnEstmtReqstHist> pagination = new CorePagination<>(info, list);
		// 목록 조회
		return pagination;
	}

	public void estmtReqstProcess(EstmtProcessParam param)
	{
		if(CoreUtils.string.isBlank(param.getEstmtId())) {
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "신청ID"));
		}
		if(CoreUtils.string.isBlank(param.getReqstSttus())) {
			throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "상태"));
		}
		EstmtProcessParam data = tsptEqpmnEstmtDao.selectEqpmnEstmtProcess(param.getEstmtId());
		// 예외처리? 검증?
		if (data == null) {
			throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
		}

		BnMember worker = TspUtils.getMember();
		param.setUpdusrId(worker.getMemberId());

		tsptEqpmnEstmtDao.updateEqpmnEstmtProcess(param);

		String Resn = param.getRsndqf();
		if(Resn == null){
			Resn = String.format(TspCode.histMessage.처리이력, TspCode.processKnd.valueOf(param.getReqstSttus()).getTitle());
		}
		// Hist 처리
		TsptEqpmnEstmtReqstHist hist = TsptEqpmnEstmtReqstHist.builder()
				.histId(CoreUtils.string.getNewId("estmtHist-"))
				.estmtId(param.getEstmtId())
				.opetrId(worker.getLoginId())
				.processKnd(param.getReqstSttus())
				.processResn(Resn)
				.mberId(worker.getMemberId())
				.mberNm(worker.getMemberNm())
				.build();

		tsptEqpmnEstmtDao.insertEqpmnEstmtHist(hist);
	}

	public EstmtOzReportParam getOzEstmt(String estmtId) {
		TspUtils.getMember();
		EstmtOzReportParam param = tsptEqpmnEstmtDao.selectEstmtAdminInfo(estmtId);
		param.setCreatDt(tsptEqpmnEstmtDao.selectCreatDt(estmtId));
		param.setApplcnt(commonService.getApplcnt(tsptEqpmnEstmtDao.selectApplcntId(estmtId)));
		return param;
	}

}

