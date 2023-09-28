package aicluster.tsp.api.admin.eqpmn.use.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.tsp.api.admin.eqpmn.extnd.param.ExtndDetailProcessParam;
import aicluster.tsp.api.admin.eqpmn.extnd.service.ExtndService;
import aicluster.tsp.api.admin.eqpmn.use.param.*;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.api.common.param.CommonCalcParam;
import aicluster.tsp.common.dao.TsptEqpmnDscntDao;
import aicluster.tsp.common.dao.TsptEqpmnUseDao;
import aicluster.tsp.common.dao.TsptEqpmnUseReqstDao;
import aicluster.tsp.common.dao.TsptFrontMyUseDao;
import aicluster.tsp.common.dto.*;
import aicluster.tsp.common.entity.TsptEqpmnExtn;
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
import java.util.HashMap;
import java.util.List;

@Service
public class UseService {

    @Autowired
    private CommonService commonService;
    @Autowired
    private ExtndService extndService;
    @Autowired
    private TsptFrontMyUseDao tsptFrontMyUseDao;
    @Autowired
    private TsptEqpmnUseDao tsptEqpmnUseDao;
    @Autowired
    private TsptEqpmnUseReqstDao tsptEqpmnUseReqstDao;
    @Autowired
    private TsptEqpmnDscntDao dscntDao;
    @Autowired
    private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
    @Autowired
    private HttpServletRequest request;
    public CorePagination<EqpmnUseDto> getEqpmnlUseList(EqpmnUseParam param, CorePaginationParam cpParam) {

        // 검색 글자수 제한
        Integer searchString = 100;
        if (searchString > Integer.MAX_VALUE) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
        }
        if (CoreUtils.string.isNotBlank(param.getEntrprsNm()) && param.getEntrprsNm().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (CoreUtils.string.isNotBlank(param.getEqpmnNmKorean()) && param.getEqpmnNmKorean().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (CoreUtils.string.isNotBlank(param.getAssetsNo()) && param.getAssetsNo().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (CoreUtils.string.isNotBlank(param.getRceptNo()) && param.getRceptNo().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }

        // 전체 건수 조회
        long totalItems = tsptEqpmnUseDao.selectEqpmnUseCount(param);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        List<EqpmnUseDto> list = tsptEqpmnUseDao.selectEqpmnUseList(param, info.getBeginRowNum(), info.getItemsPerPage(), false);

        return new CorePagination<>(info, list);
    }

    public EqpmnUseDetailDto getEqpmnUseDetail(String reqstId){
        EqpmnUseDetailDto data = tsptEqpmnUseDao.selectEqpmnUseDetail(reqstId);
        if(data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.조회결과없음, "신청내역"));
        }

        ApplcntDto applcnt = commonService.getApplcnt(data.getApplcntId());

        data.setEmail(applcnt.getEmail());
        data.setMberDiv(applcnt.getMberDiv());
        data.setEntrprsNm(applcnt.getEntrprsNm());
        data.setOfcps(applcnt.getOfcps());
        data.setCttpc(applcnt.getCttpc());

        data.setDscntList(dscntDao.selectEqpmnDscntList(data.getEqpmnId()));

        // 1일 가용시간 추가
        CommonCalcParam calc = commonService.getWorkingRntfee(data.getEqpmnId(), data.getUseBeginDt(), data.getUseEndDt(), data.getTkoutAt());
        data.setUsefulHour(calc.getUsefulHour());

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
                        .workCn("장비사용 상세정보")
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

    public ModelAndView getListExcelDownload(EqpmnUseParam param) {
        boolean isExcel = true;
        List<EqpmnUseDto> list = tsptEqpmnUseDao.selectEqpmnUseList(param, 0L, 0L, isExcel);
        ExcelWorkbook wb = new ExcelWorkbook();
        wb.setFilename("장비사용 현황");
        ExcelSheet<EqpmnUseDto> sheet = new ExcelSheet<>();
        ExcelMergeRows mergeRegions = new ExcelMergeRows();
        sheet.setMergeRegions(mergeRegions);
        sheet.addRows(list);
        sheet.setHeaders("번호","사용상태", "구분", "사업자명/이름", "자산번호", "장비명(국문)", "사용시작일", "사용종료일", "반출여부", "반입여부", "지불방법");
        sheet.setProperties("number","exUseSttus", "exMberDiv", "entrprsNm", "assetsNo", "eqpmnNmKorean", "exUseBeginDt", "exUseEndDt", "exTkoutAt", "exTkinAt", "exPymntMth");
        sheet.setTitle("장비사용");
        sheet.setSheetName("장비사용");
        wb.addSheet(sheet);
        return ExcelView.getView(wb);
    }

    public void updateUseProcess(UseReqstProcessParam param) {
        if(CoreUtils.string.isBlank(param.getReqstId())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "신청ID"));
        }
        if(CoreUtils.string.isBlank(param.getReqstSttus())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "상태"));
        }
        UseReqstProcessParam data = tsptEqpmnUseDao.selectUseReqstProcess(param.getReqstId());

        if (data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
        }

        BnMember worker = TspUtils.getMember();
        param.setUpdusrId(worker.getMemberId());

        tsptEqpmnUseDao.updateUseProcess(param);

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

        tsptEqpmnUseDao.insertUseHist(hist);
    }

    public void updateUseNpyProcess(UseNpyProcessParam param) {
        if(CoreUtils.string.isBlank(param.getReqstId())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "신청ID"));
        }
//        if(CoreUtils.string.isBlank(param.getNpyResn())) {
//            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "상태"));
//        }
        UseNpyProcessParam data = tsptEqpmnUseDao.selectUseNpyProcess(param.getReqstId());

        if (data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
        }

        BnMember worker = TspUtils.getMember();
        param.setUpdusrId(worker.getMemberId());
        tsptEqpmnUseDao.updateUseNpyProcess(param);

        String resn = param.getNpyResn();
        String knd = TspCode.processKnd.NONPAYMENT.toString();
        if(resn == null){
            knd = "미납취소";
        }

        TsptEqpmnUseReqstHist hist = TsptEqpmnUseReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(param.getReqstId())
                .opetrId(worker.getLoginId())
                .processKnd(knd)
                .processResn(resn)
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptEqpmnUseDao.insertUseHist(hist);
    }

    public void updateUseRcpmnyGdcc(UseRcpmnyGdccParam param) {
        if(CoreUtils.string.isBlank(param.getReqstId())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "신청ID"));
        }
        if(CoreUtils.string.isBlank(param.getRcpmnyGdcc())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "상태"));
        }

        String applcntId = tsptEqpmnUseDao.selectUseRcpmnyGdcc(param.getReqstId());

        if (CoreUtils.string.isBlank(applcntId)) {
            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
        }

        ApplcntDto applcnt = commonService.getApplcnt(applcntId);

        // 입금 인내문 메일 발송
        commonService.sendEmail("입금 부탁드립니다.", param.getRcpmnyGdcc(), applcnt, new HashMap<>());

        BnMember worker = TspUtils.getMember();

        param.setUpdusrId(worker.getMemberId());
        tsptEqpmnUseDao.updateUseRcpmnyGdcc(param);

        TsptEqpmnUseReqstHist hist = TsptEqpmnUseReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(param.getReqstId())
                .opetrId(worker.getLoginId())
                .processKnd(TspCode.processKnd.DEPINFO.toString())
                .processResn(param.getRcpmnyGdcc())
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptEqpmnUseDao.insertUseHist(hist);
    }

    public void updateUseAditRntfee(UseAditRntfeeParam param) {
        if(CoreUtils.string.isBlank(param.getReqstId())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "신청ID"));
        }
        if(param.getAditRntfee() == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "상태"));
        }
        UseAditRntfeeParam data = tsptEqpmnUseDao.selectUseAditRntfee(param.getReqstId());

        if (data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
        }

        BnMember worker = TspUtils.getMember();
        param.setUpdusrId(worker.getMemberId());
        tsptEqpmnUseDao.updateUseAditRntfee(param);

        // Hist 처리 반출처리도 들어가나??
        TsptEqpmnUseReqstHist hist = TsptEqpmnUseReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(param.getReqstId())
                .opetrId(worker.getLoginId())
                .processKnd(TspCode.processKnd.ADD_PAYMENT.toString())
                .processResn(param.getRqestResn())
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptEqpmnUseDao.insertUseHist(hist);
    }

    public void updateTkin(String reqstId) {
        Integer row = tsptEqpmnUseDao.selectUseTkin(reqstId);
        if(row == 0) {
            throw new InvalidationException(String.format(TspCode.validateMessage.조회결과없음, "신청내역"));
        }

        BnMember worker = TspUtils.getMember();

        tsptEqpmnUseDao.updateUseTkin(reqstId, worker.getMemberId());

        // Hist 처리 반출처리도 들어가나??
        TsptEqpmnUseReqstHist hist = TsptEqpmnUseReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(reqstId)
                .opetrId(worker.getLoginId())
                .processKnd(TspCode.processKnd.TKIN.toString())
                .processResn(String.format(TspCode.histMessage.처리이력, TspCode.processKnd.TKIN.getTitle()))
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptEqpmnUseDao.insertUseHist(hist);
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

    public List<EqpmnUseRntfeeHistDto> getRntfeeList(String reqstId) {
        // 전체 건수 조회
        List<EqpmnUseRntfeeHistDto> data = tsptEqpmnUseDao.selectRntfeeList(reqstId);

        return data;
    }

    public CorePagination<TsptEqpmnUseReqstHist> getUseHistList(String reqstId, CorePaginationParam cpParam) {
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

    public CorePagination<EqpmnUseExtndDto> getExtndList(String reqstId, CorePaginationParam cpParam) {
        boolean isExcel = false;
        // 전체 건수 확인
        long totalItems = tsptEqpmnUseDao.selectUseExtndCount(reqstId);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<EqpmnUseExtndDto> list = tsptEqpmnUseDao.selectUseExtndList(reqstId, info.getBeginRowNum(), info.getItemsPerPage(), isExcel);
        CorePagination<EqpmnUseExtndDto> pagination = new CorePagination<>(info, list);
        // 목록 조회
        return pagination;
    }
    public void putExtnd(UseExtndParam param) {
        EqpmnUseExtndDto data = tsptEqpmnUseDao.selectUseExtnd(param.getReqstId());
        if(data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.조회결과없음, "신청내역"));
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
        Integer expectUsgtm = 0;

        CommonCalcParam calc = commonService.getWorkingRntfee(data.getEqpmnId(), param.getUseBeginDt(), param.getUseEndDt(), data.getTkoutAt());

        expectUsgtm = calc.getTotalMin();
        // 할인만 적용(시간 계산)
        if (param.getUsgtm() == null || param.getUsgtm() == 0){
            expectRntfee = calc.getRntfee();
            dscntAmount = (int)(data.getDscntRate() / 100.0 * expectRntfee);
            rntfee = expectRntfee - dscntAmount;
        }
        // 시간 적용
        else {
            usgtm = param.getUsgtm() * 60;
            expectRntfee = usgtm * calc.getRntfeeHour();
            dscntAmount = (int)(data.getDscntRate() / 100.0 * expectRntfee);
            rntfee = expectRntfee - dscntAmount;
        }

        BnMember worker = TspUtils.getMember();

        param.setExpectRntfee(expectRntfee);
        param.setRntfee(rntfee);
        param.setExpectUsgtm(expectUsgtm);
        param.setDscntAmount(dscntAmount);
        param.setUpdusrId(worker.getMemberId());
        //tsptEqpmnUseDao.updateUseExtnd(param);

        TsptEqpmnExtn tsptEqpmnExtn = TsptEqpmnExtn.builder()
                .etReqstId(CoreUtils.string.getNewId("extnReqst-"))
                .reqstId(param.getReqstId())
                .reqstSttus(TspCode.extndSttus.APPROVE.toString())
                .useBeginDt(param.getUseBeginDt())
                .useEndDt(param.getUseEndDt())
                .rntfee(param.getRntfee())
                .dscntId(data.getDscntId())
                .usgtm(usgtm)
                .expectUsgtm(param.getExpectUsgtm())
                .dscntAmount(param.getDscntAmount())
                .expectRntfee(param.getExpectRntfee())
                .creatrId(worker.getMemberId())
                .updusrId(worker.getMemberId())
                .dscntId(data.getDscntId())
                .build();
        tsptFrontMyUseDao.postUseExtndReqst(tsptEqpmnExtn);

        ExtndDetailProcessParam extndParam = new ExtndDetailProcessParam();
        extndParam.setReqstSttus(TspCode.extndSttus.APPLY.toString());
        extndParam.setRsndqf(param.getRsndqf());

        extndService.insertExtndHist(tsptEqpmnExtn.getEtReqstId(), extndParam);
    }

    public void updateUseReqstCancel(String reqstId) {
        Integer row = tsptEqpmnUseDao.selectUseTkin(reqstId);
        if(row == 0) {
            throw new InvalidationException(String.format(TspCode.validateMessage.조회결과없음, "신청내역"));
        }

        BnMember worker = TspUtils.getMember();

        tsptEqpmnUseDao.updateUseReqstCancel(reqstId, worker.getMemberId());

        TsptEqpmnUseReqstHist hist = TsptEqpmnUseReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(reqstId)
                .opetrId(worker.getLoginId())
                .processKnd(TspCode.processKnd.CANCEL.toString())
                .processResn(String.format(TspCode.histMessage.처리이력, TspCode.processKnd.CANCEL.getTitle()))
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptEqpmnUseDao.insertUseHist(hist);
    }


}


