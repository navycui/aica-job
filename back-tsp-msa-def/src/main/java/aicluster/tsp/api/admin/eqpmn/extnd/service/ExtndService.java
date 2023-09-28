package aicluster.tsp.api.admin.eqpmn.extnd.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.tsp.api.admin.eqpmn.extnd.param.ExtndDetailProcessParam;
import aicluster.tsp.api.admin.eqpmn.extnd.param.ExtndListParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.common.dao.TsptEqpmnExtndDao;
import aicluster.tsp.common.dto.ApplcntDto;
import aicluster.tsp.common.dto.EqpmnExtndDetailDto;
import aicluster.tsp.common.dto.EqpmnExtndListDto;
import aicluster.tsp.common.entity.TsptEqpmnExtnHist;
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
import java.util.Objects;

@Slf4j
@Service
public class ExtndService {

    @Autowired
    private TsptEqpmnExtndDao tsptEqpmnExtndDao;
    @Autowired
    private CommonService commonService;

    @Autowired
    private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
    @Autowired
    private HttpServletRequest request;

    // 기간연장신청 Excel 다운로드
    public ModelAndView getExtndListExcelDownload(ExtndListParam param) {
        boolean isExcel = true;
        List<EqpmnExtndListDto> list = tsptEqpmnExtndDao.selectEqpmnExtndList(isExcel, param);
        ExcelWorkbook wb = new ExcelWorkbook();
        wb.setFilename("기간연장신청 현황");
        ExcelSheet<EqpmnExtndListDto> sheet = new ExcelSheet<>();
        ExcelMergeRows mergeRegions = new ExcelMergeRows();
        sheet.setMergeRegions(mergeRegions);
        sheet.addRows(list);
        sheet.setHeaders("번호","신청상태", "구분", "사업자명", "이름", "자산번호", "장비명(국문)", "사용시작일", "사용종료일", "지불방법", "신청일시");
        sheet.setProperties("number", "exReqstSttus", "exMberDiv", "entrprsNm", "userNm", "assetsNo", "eqpmnNmKorean", "exUseBeginDt", "exUseEndDt", "exPymntMth", "exCreatDt");
        sheet.setTitle("기간연장신청 현황");
        sheet.setSheetName("기간연장신청 현황");
        wb.addSheet(sheet);
        return ExcelView.getView(wb);
    }

    // 기간연장신청 목록 조회
    public CorePagination<EqpmnExtndListDto> getExtndList(ExtndListParam param, CorePaginationParam cpParam) {

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

        boolean isExcel = false;
        // 전체 건수 확인
        long totalItems = tsptEqpmnExtndDao.selectEqpmnExtndCount(param);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<EqpmnExtndListDto> list = tsptEqpmnExtndDao.selectEqpmnExtndList(isExcel, info.getBeginRowNum(), cpParam.getItemsPerPage(), param);
        CorePagination<EqpmnExtndListDto> pagination = new CorePagination<>(info, list);
        // 목록 조회
        return pagination;
    }

    // 기간연장신청 상세정보 조회
    public EqpmnExtndDetailDto getExtndDetail(String etReqstId) {
        EqpmnExtndDetailDto dto = tsptEqpmnExtndDao.selectEqpmnExtndDetail(etReqstId);
        if (CoreUtils.string.isNotBlank(dto.getDscntId())) {
            dto.setDetailDscntParam(tsptEqpmnExtndDao.selectEqpmnExtndDetailDscnt(dto.getEqpmnId()));
        }
        dto.setUsgtm(Math.round(dto.getUsgtm()/60));
        dto.setExpectUsgtm(Math.round(dto.getExpectUsgtm()/60));

        ApplcntDto applcnt = commonService.getApplcnt(dto.getApplcntId());

        dto.setEmail(applcnt.getEmail());
        dto.setMberDiv(applcnt.getMberDiv());
        dto.setEntrprsNm(applcnt.getEntrprsNm());
        dto.setUserNm(applcnt.getUserNm());
        dto.setOfcps(applcnt.getOfcps());
        dto.setCttpc(applcnt.getCttpc());

        // log 정보생성
        BnMember worker = TspUtils.getMember();
        if (!CoreUtils.string.equals(worker.getMemberId(), dto.getApplcntId())) {
            String memberIp = CoreUtils.webutils.getRemoteIp(request);
            if(!"0:0:0:0:0:0:0:1".equals(memberIp)) {
                LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
                        .memberId(worker.getMemberId())
                        .memberIp(memberIp)
                        //.memberIp("127.0.0.1")
                        .workTypeNm("조회")
                        .workCn("장비 기간연장신청 상세정보")
                        .trgterNm(applcnt.getUserNm())
                        .trgterId(dto.getApplcntId())
                        .email(applcnt.getEmail())
                        //.birthday("응답값에 포함된 원문 생년월일")
                        .mobileNo(applcnt.getCttpc())
                        .build();
                indvdlInfSrchUtils.insert(logParam);
            }
        }

        return dto;
    }
    // 기간연장신청 상세정보 처리
    public EqpmnExtndDetailDto putExtndDetail(String etReqstId,  ExtndDetailProcessParam param) {
        EqpmnExtndDetailDto dto = getExtndDetail(etReqstId);

        if ( "APPROVE".equals(param.getReqstSttus()) ){
            if( "REJECT".equals(dto.getReqstSttus()) || "CANCEL".equals(dto.getReqstSttus()) ){
                throw new InvalidationException(String.format(TspCode.validateMessage.입력오류, "장비상태"));
            }
        }
        if (TspCode.processKnd.ACTL_USE_PAYMENT.toString().equals(param.getReqstSttus())) {
            param.setUsgtm(param.getUsgtm() * 60);
            param.setReqstSttus(dto.getReqstSttus());
        }
        // 기간연장신청 처리 업데이트
        tsptEqpmnExtndDao.updateEqpmnExtndDetail(etReqstId, param, dto.getPymntMth());
        // 기간연장신청 처리이력 등록
        if (Objects.equals(dto.getReqstSttus(), param.getReqstSttus()) && CoreUtils.string.isNotBlank(param.getDscntId())) {
            param.setReqstSttus(TspCode.processKnd.ACTL_USE_PAYMENT.toString());
        }
        insertExtndHist(etReqstId, param);
        // 최종 상세 페이지 호출
        dto = getExtndDetail(etReqstId);
        return dto;
    }

    // 처리이력 등록
    public void insertExtndHist(String etReqstId, ExtndDetailProcessParam param) {
        // 로그인 사용자 정보 추출
        BnMember worker = TspUtils.getMember();

        TsptEqpmnExtnHist hist = new TsptEqpmnExtnHist();
        hist.setHistId(CoreUtils.string.getNewId("extnHist-"));
        hist.setEtReqstId(etReqstId);
        hist.setMberId(worker.getMemberId());
        hist.setProcessKnd(param.getReqstSttus());
        if (CoreUtils.string.isBlank(param.getRsndqf())) {
            param.setRsndqf(String.format(TspCode.histMessage.처리이력, TspCode.processKnd.valueOf(param.getReqstSttus()).getTitle()));
        }
        hist.setProcessResn(param.getRsndqf());
        if (!param.getReqstSttus().equals(TspCode.processKnd.ACTL_USE_PAYMENT.toString())) {
            if (CoreUtils.string.isNotBlank(param.getNpyResn())) {
                hist.setProcessKnd(TspCode.extndSttus.NONPAYMENT.toString());
                hist.setProcessResn(String.format(TspCode.histMessage.처리이력, TspCode.extndSttus.NONPAYMENT.toString()));
            }
            if (CoreUtils.string.isNotBlank(param.getRcpmnyGdcc())) {
                hist.setProcessKnd(TspCode.extndSttus.DEPINFO.toString());
                hist.setProcessResn(param.getRcpmnyGdcc());
            }
        }

        tsptEqpmnExtndDao.insertExtndHist(hist);
    }

    // 기간연장 처리이력 조회
    public CorePagination<TsptEqpmnExtnHist> getExtndHist(String etReqstId, CorePaginationParam cpParam) {
        // 전체 건수 확인
        long totalItems = tsptEqpmnExtndDao.selectExtndHistCnt(etReqstId);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<TsptEqpmnExtnHist> list = tsptEqpmnExtndDao.selectExtndHistList(etReqstId, info.getBeginRowNum(), cpParam.getItemsPerPage());
        CorePagination<TsptEqpmnExtnHist> pagination = new CorePagination<>(info, list);

        // 목록 조회
        return pagination;
    }
}