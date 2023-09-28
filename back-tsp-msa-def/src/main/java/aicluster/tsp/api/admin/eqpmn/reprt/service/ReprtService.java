package aicluster.tsp.api.admin.eqpmn.reprt.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.tsp.api.admin.eqpmn.reprt.param.ReprtParam;
import aicluster.tsp.api.admin.eqpmn.reprt.param.ReprtProcessParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.common.dao.TsptEqpmnReprtDao;
import aicluster.tsp.common.dto.ApplcntDto;
import aicluster.tsp.common.dto.EqpmnReprtDetailDto;
import aicluster.tsp.common.dto.EqpmnReprtDto;
import aicluster.tsp.common.entity.TsptEqpmnReprtHist;
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
public class ReprtService {

    public static final long ITEMS_PER_PAGE = 5;

    @Autowired
    private TsptEqpmnReprtDao tsptEqpmnReprtDao;

    @Autowired
    private CommonService commonService;
    @Autowired
    private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
    @Autowired
    private HttpServletRequest request;

    public CorePagination<EqpmnReprtDto> getEqpmnlReprtList(ReprtParam param, CorePaginationParam cpParam) {

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

        // 전체 건수 조회
        long totalItems = tsptEqpmnReprtDao.selectEqpmnReprtCount(param);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        List<EqpmnReprtDto> list = tsptEqpmnReprtDao.selectEqpmnReprtList(param, info.getBeginRowNum(), info.getItemsPerPage(), false);

        return new CorePagination<>(info, list);
    }

    public ModelAndView getListExcelDownload(ReprtParam param) {
        boolean isExcel = true;
        List<EqpmnReprtDto> list = tsptEqpmnReprtDao.selectEqpmnReprtList(param, 0L, 0L, isExcel);
        ExcelWorkbook wb = new ExcelWorkbook();
        wb.setFilename("결과보고서 현황");
        ExcelSheet<EqpmnReprtDto> sheet = new ExcelSheet<>();
        ExcelMergeRows mergeRegions = new ExcelMergeRows();
        sheet.setMergeRegions(mergeRegions);
        sheet.addRows(list);
        sheet.setHeaders("번호","처리상태", "구분", "사업자명/이름", "자산번호", "장비명(국문)", "사용시작일", "사용종료일", "제출일");
        sheet.setProperties("number","exReprtSttus", "exMberDiv", "entrprsNm", "assetsNo", "eqpmnNmKorean", "exUseBeginDt", "exUseEndDt", "exCreatDt");
        sheet.setTitle("결과보고서");
        sheet.setSheetName("결과보고서");
        wb.addSheet(sheet);
        return ExcelView.getView(wb);
    }

    public EqpmnReprtDetailDto getEqpmnReprtDetail(String reprtId){
        EqpmnReprtDetailDto data = tsptEqpmnReprtDao.selectEqpmnReprtDetail(reprtId);
        if(data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.조회결과없음, "결과보고서"));
        }

        ApplcntDto applcnt = commonService.getApplcnt(data.getApplcntId());

        data.setEmail(applcnt.getEmail());
        data.setMberDiv(applcnt.getMberDiv());
        data.setEntrprsNm(applcnt.getEntrprsNm());
        data.setOfcps(applcnt.getOfcps());
        data.setCttpc(applcnt.getCttpc());
        data.setPartcptnDiv(applcnt.getPartcptnDiv());

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
                        .workCn("결과보고서 상세정보")
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

    public void updateReprtProcess(ReprtProcessParam param) {
        if(CoreUtils.string.isBlank(param.getReprtId())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "신청ID"));
        }
        if(CoreUtils.string.isBlank(param.getReprtSttus())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "상태"));
        }
        ReprtProcessParam data = tsptEqpmnReprtDao.selectReqstProcess(param.getReprtId());

        if (data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
        }

        BnMember worker = TspUtils.getMember();
        param.setUpdusrId(worker.getMemberId());
        tsptEqpmnReprtDao.updateReprtProcess(param);

        // Hist 처리
        TsptEqpmnReprtHist hist = TsptEqpmnReprtHist.builder()
                .histId(CoreUtils.string.getNewId("reprtHist-"))
                .reprtId(param.getReprtId())
                .opetrId(worker.getLoginId())
                .processKnd(param.getReprtSttus())
                .processResn(param.getReprtSttus().equals(TspCode.processKnd.APPROVE.toString()) ? String.format(TspCode.histMessage.처리이력, TspCode.analsUseSttus.APPROVE.getTitle()) : param.getRsndqf())
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptEqpmnReprtDao.insertReprtHist(hist);
    }

    public CorePagination<TsptEqpmnReprtHist> getEqpmnReprtHistList(String reprtId, CorePaginationParam cpParam) {
        boolean isExcel = false;
        // 전체 건수 확인
        long totalItems = tsptEqpmnReprtDao.selectEqpmnReprtHistCount(reprtId);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<TsptEqpmnReprtHist> list = tsptEqpmnReprtDao.selectEqpmnReprtHistList(reprtId, isExcel, info.getBeginRowNum(), info.getItemsPerPage());
        CorePagination<TsptEqpmnReprtHist> pagination = new CorePagination<>(info, list);
        // 목록 조회
        return pagination;
    }
}