package aicluster.tsp.api.admin.eqpmn.use.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtProcessParam;
import aicluster.tsp.api.admin.eqpmn.estmt.service.EstmtService;
import aicluster.tsp.api.admin.eqpmn.use.param.EqpmnUseReqstParam;
import aicluster.tsp.api.admin.eqpmn.use.param.UseDscntParam;
import aicluster.tsp.api.admin.eqpmn.use.param.UseReqstProcessParam;
import aicluster.tsp.api.admin.eqpmn.use.param.UseReqstTkoutProcessParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.api.common.param.CommonCalcParam;
import aicluster.tsp.common.dao.TsptEqpmnDscntDao;
import aicluster.tsp.common.dao.TsptEqpmnUseReqstDao;
import aicluster.tsp.common.dto.ApplcntDto;
import aicluster.tsp.common.dto.EqpmnUseDscntDto;
import aicluster.tsp.common.dto.EqpmnUseReqstDetailDto;
import aicluster.tsp.common.dto.EqpmnUseReqstDto;
import aicluster.tsp.common.entity.TsptEqpmnUseReqst;
import aicluster.tsp.common.entity.TsptEqpmnUseReqstHist;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class UseReqstService {

    @Autowired
    private CommonService commonService;
    @Autowired
    private TsptEqpmnUseReqstDao tsptEqpmnUseReqstDao;
    @Autowired
    private TsptEqpmnDscntDao dscntDao;
    @Autowired
    private EstmtService estmtService;

    @Autowired
    private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
    @Autowired
    private HttpServletRequest request;

    public static final long ITEMS_PER_PAGE = 5;

    public CorePagination<EqpmnUseReqstDto> getEqpmnUseReqstList(EqpmnUseReqstParam eqpmnUseReqstParam, CorePaginationParam cpParam) {

        // 검색 글자수 제한
        Integer searchString = 100;
        if (searchString > Integer.MAX_VALUE) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
        }
        if (CoreUtils.string.isNotBlank(eqpmnUseReqstParam.getEntrprsNm()) && eqpmnUseReqstParam.getEntrprsNm().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (CoreUtils.string.isNotBlank(eqpmnUseReqstParam.getEqpmnNmKorean()) && eqpmnUseReqstParam.getEqpmnNmKorean().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (CoreUtils.string.isNotBlank(eqpmnUseReqstParam.getAssetsNo()) && eqpmnUseReqstParam.getAssetsNo().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (CoreUtils.string.isNotBlank(eqpmnUseReqstParam.getRceptNo()) && eqpmnUseReqstParam.getRceptNo().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }


        // 전체 건수 조회
        long totalItems = tsptEqpmnUseReqstDao.selectEqpmnReqstCount(eqpmnUseReqstParam);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        List<EqpmnUseReqstDto> list = tsptEqpmnUseReqstDao.selectEqpmnReqstList(eqpmnUseReqstParam, info.getBeginRowNum(), info.getItemsPerPage(), false);

        return new CorePagination<>(info, list);
    }

    public ModelAndView getListExcelDownload(EqpmnUseReqstParam param) {
        boolean isExcel = true;
        List<EqpmnUseReqstDto> list = tsptEqpmnUseReqstDao.selectEqpmnReqstList(param, 0L, 0L, isExcel);

        ExcelWorkbook wb = new ExcelWorkbook();
        wb.setFilename("장비사용 신청 현황");
        ExcelSheet<EqpmnUseReqstDto> sheet = new ExcelSheet<>();
        ExcelMergeRows mergeRegions = new ExcelMergeRows();
        sheet.setMergeRegions(mergeRegions);
        sheet.addRows(list);
        sheet.setHeaders("번호","신청상태", "구분", "사업자명/이름", "자산번호", "장비명(국문)", "사용시작일", "사용종료일", "지불방법", "신청일시");
        sheet.setProperties("number","exReqstSttus", "exMberDiv", "entrprsNm", "assetsNo", "eqpmnNmKorean", "exUseBeginDt", "exUseEndDt", "exPymntMth", "exCreatDt");
        sheet.setTitle("장비신청");
        sheet.setSheetName("장비신청");
        wb.addSheet(sheet);
        return ExcelView.getView(wb);
    }

    public EqpmnUseReqstDetailDto getEqpmnUseReqstDetail(String reqstId){
        EqpmnUseReqstDetailDto data = tsptEqpmnUseReqstDao.selectEqpmnUseReqstDetail(reqstId);
        if(data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.조회결과없음, "신청내역"));
        }

        ApplcntDto applcnt = commonService.getApplcnt(data.getApplcntId());

        data.setEmail(applcnt.getEmail());
        data.setMberDiv(applcnt.getMberDiv());
        data.setEntrprsNm(applcnt.getEntrprsNm());
        data.setOfcps(applcnt.getOfcps());
        data.setCttpc(applcnt.getCttpc());

        // 1일 가용시간 추가
        CommonCalcParam calc = commonService.getWorkingRntfee(data.getEqpmnId(), data.getUseBeginDt(), data.getUseEndDt(), data.getTkoutAt());
        data.setUsefulHour(calc.getUsefulHour());

        // 할인 정보
        data.setDscntList(dscntDao.selectEqpmnDscntList(data.getEqpmnId()));

        // 첨부파일 정보
        if(!CoreUtils.string.isBlank(data.getAtchmnflGroupId())){
            data.setAttachmentList(commonService.getAttachmentGroup(data.getAtchmnflGroupId()));
        }

        // log 정보생성
        BnMember worker = TspUtils.getMember();
        if (!CoreUtils.string.equals(worker.getMemberId(), data.getApplcntId())) {
            String memberIp = CoreUtils.webutils.getRemoteIp(request);
            if(!"0:0:0:0:0:0:0:1".equals(memberIp)) {
                LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
                        .memberId(worker.getMemberId())
                        .memberIp(memberIp)
                        //.memberIp("127.0.0.1")
                        .workTypeNm("조회")
                        .workCn("장비신청 상세정보")
                        .trgterNm(applcnt.getUserNm())
                        .trgterId(data.getApplcntId())
                        .email(applcnt.getEmail())
                        //.birthday("응답값에 포함된 원문 생년월일")
                        .mobileNo(applcnt.getCttpc())
                        .build();
                indvdlInfSrchUtils.insert(logParam);
            }
        }

        return data;
    }


    public CorePagination<TsptEqpmnUseReqstHist> getUseReqstHistList(String reqstId, CorePaginationParam cpParam) {
        boolean isExcel = false;
        // 전체 건수 확인
        long totalItems = tsptEqpmnUseReqstDao.selectUseReqstHistCount(reqstId);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<TsptEqpmnUseReqstHist> list = tsptEqpmnUseReqstDao.selectUseReqstHistList(reqstId, info.getBeginRowNum(), info.getItemsPerPage(), isExcel);
        CorePagination<TsptEqpmnUseReqstHist> pagination = new CorePagination<>(info, list);
        // 목록 조회
        return pagination;
    }


    public void updateUseReqstProcess(UseReqstProcessParam param) {
        if(CoreUtils.string.isBlank(param.getReqstId())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "신청ID"));
        }
        if(CoreUtils.string.isBlank(param.getReqstSttus())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "상태"));
        }
        UseReqstProcessParam data = tsptEqpmnUseReqstDao.selectUseReqstProcess(param.getReqstId());

        if (data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
        }

        BnMember worker = TspUtils.getMember();
        param.setUpdusrId(worker.getMemberId());
        tsptEqpmnUseReqstDao.updateUseReqstProcess(param);

        // Hist 처리
        TsptEqpmnUseReqstHist hist = TsptEqpmnUseReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(param.getReqstId())
                .opetrId(worker.getLoginId())
                .processKnd(param.getReqstSttus())
                .processResn(String.format(TspCode.histMessage.처리이력, TspCode.processKnd.valueOf(param.getReqstSttus()).getTitle()))
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();
        if(hist.getProcessKnd().equals("SPM_REQUEST"))
            hist.setProcessResn(param.getRsndqf());
        tsptEqpmnUseReqstDao.insertUseReqstHist(hist);
        if (param.getReqstSttus().equals(TspCode.reqstSttus.CANCEL.toString()) || param.getReqstSttus().equals(TspCode.reqstSttus.REJECT.toString())) {
            String estmtId = tsptEqpmnUseReqstDao.checkEstmtId(data.getApplcntId(), data.getEqpmnId(), data.getUseBeginDt(), data.getUseEndDt());
            if (CoreUtils.string.isNotBlank(estmtId)) {
                EstmtProcessParam estmtParam = EstmtProcessParam.builder()
                        .estmtId(estmtId)
                        .reqstSttus(param.getReqstSttus())
                        .build();
                estmtService.estmtReqstProcess(estmtParam);
            }
        }

    }

    public void updateUseReqstConsent(String reqstId) {
        if(CoreUtils.string.isBlank(reqstId)) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "신청ID"));
        }

        TsptEqpmnUseReqst data = tsptEqpmnUseReqstDao.selectUseReqstConsent(reqstId);

        if (data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
        }
//        if (data.getReqstSttus() != "신청"){
//            throw new InvalidationException("신청 상태가 아님");
//        }
        if ( (CoreUtils.string.equals(data.getReqstSttus(), TspCode.reqstSttus.REJECT.toString()) || (CoreUtils.string.equals(data.getReqstSttus(), TspCode.reqstSttus.CANCEL.toString()))) )
        {
            throw new InvalidationException("신청 상태가 아님");
        }

        BnMember worker = TspUtils.getMember();

        String useSttus = TspCode.reqUsage.USE.toString();
        String reqstSttus = TspCode.reqstSttus.APPROVE.toString();
        tsptEqpmnUseReqstDao.updateUseReqstConsent(reqstId, reqstSttus, useSttus, worker.getMemberId());

        // Hist 처리
        TsptEqpmnUseReqstHist hist = TsptEqpmnUseReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(reqstId)
                .opetrId(worker.getLoginId())
                .processKnd(useSttus)
                .processResn(String.format(TspCode.histMessage.처리이력, TspCode.processKnd.valueOf(useSttus).getTitle()))
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptEqpmnUseReqstDao.insertUseReqstHist(hist);
    }

    public EqpmnUseReqstDetailDto updateUseReqstTkoutProcess(UseReqstTkoutProcessParam param) {
        if(CoreUtils.string.isBlank(param.getReqstId())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "신청ID"));
        }
        if(CoreUtils.string.isBlank(param.getTkoutDlbrtResult())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "상태"));
        }
        UseReqstProcessParam data = tsptEqpmnUseReqstDao.selectUseReqstProcess(param.getReqstId());

        if (data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
        }

        BnMember worker = TspUtils.getMember();
        param.setUpdusrId(worker.getMemberId());
        tsptEqpmnUseReqstDao.updateUseReqstTkoutProcess(param);

        TsptEqpmnUseReqstHist hist = TsptEqpmnUseReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(param.getReqstId())
                .opetrId(worker.getLoginId())
                .processKnd(TspCode.eqpmnUseHist.TKOUT_DLBRT_RESULT.toString())
                .processResn(param.getTkoutDlbrtResult().equals(TspCode.processKnd.APPROVE.toString()) ? String.format(TspCode.histMessage.처리이력, TspCode.tkoutDlbrtResult.APPROVE.getTitle()) : param.getTkoutDlbrtCn())
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptEqpmnUseReqstDao.insertUseReqstHist(hist);
        return test(param.getReqstId(),param.getTkoutDlbrtResult(), worker.getMemberId());
    }
    public EqpmnUseReqstDetailDto test(String reqstId, String tkoutDlbrtResult, String memberId) {
        EqpmnUseReqstDetailDto dto = getEqpmnUseReqstDetail(reqstId);
        EqpmnUseDscntDto data = tsptEqpmnUseReqstDao.selectUseDscnt(reqstId, dto.getDscntId());
        data.setTkoutAt(tkoutDlbrtResult.equals("APPROVE"));
        CommonCalcParam calc = commonService.getWorkingRntfee(data.getEqpmnId(), data.getUseBeginDt(), data.getUseEndDt(), data.getTkoutAt());
        dto.setRntfee(calc.getRntfee());
        dto.setUsgtm(calc.getTotalHrs() * 60);
        dto.setDscntAmount(calc.getTotalHrs() * calc.getRntfeeHour() * data.getDscntRate() / 100);
        tsptEqpmnUseReqstDao.updateUseReqstDscnt(reqstId, dto.getDscntId(), memberId, dto.getRntfee(), dto.getUsgtm(), dto.getDscntAmount());
        return getEqpmnUseReqstDetail(reqstId);
    }

    public void updateUseReqstDscnt(UseDscntParam param) {
        EqpmnUseDscntDto data = tsptEqpmnUseReqstDao.selectUseDscnt(param.getReqstId(), param.getDscntId());
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
            expectRntfee = param.getUsgtm() * data.getRntfeeHour();
            dscntAmount = (int)(data.getDscntRate() / 100.0 * expectRntfee);
            rntfee = expectRntfee - dscntAmount;
        }

        BnMember worker = TspUtils.getMember();

        tsptEqpmnUseReqstDao.updateUseReqstDscnt(param.getReqstId(), param.getDscntId(), worker.getMemberId(), rntfee, usgtm, dscntAmount);

        TsptEqpmnUseReqstHist hist = TsptEqpmnUseReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(param.getReqstId())
                .opetrId(worker.getLoginId())
                .processKnd(TspCode.processKnd.ACTL_USE_PAYMENT.toString())
                .processResn(String.format(TspCode.histMessage.처리이력, TspCode.processKnd.ACTL_USE_PAYMENT.getTitle()))
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptEqpmnUseReqstDao.insertUseReqstHist(hist);
    }

}

