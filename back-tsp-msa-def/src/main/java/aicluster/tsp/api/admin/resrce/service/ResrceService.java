package aicluster.tsp.api.admin.resrce.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.tsp.api.admin.resrce.param.ResrceDetailParam;
import aicluster.tsp.api.admin.resrce.param.ResrceHistParam;
import aicluster.tsp.api.admin.resrce.param.ResrceListParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.common.dao.TsptResrceUseReqstDao;
import aicluster.tsp.common.dao.TsptResrceUseReqstHistDao;
import aicluster.tsp.common.dto.ApplcntDto;
import aicluster.tsp.common.dto.ResrceUseReqstDto;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.excel.dto.ExcelMergeRows;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
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
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ResrceService {

    public static final long ITEMS_PER_PAGE = 5;

    @Autowired
    private TsptResrceUseReqstDao tsptResrceUseReqstDao;

    @Autowired
    private TsptResrceUseReqstHistDao tsptResrceUseReqstHistDao;

    @Autowired
    private CommonService commonService;

    @Autowired
    private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
    @Autowired
    private HttpServletRequest request;

    /** 실증 자원 신청 조회 */
    public CorePagination<ResrceUseReqstDto> getResrceList(ResrceListParam search, CorePaginationParam corePaginationParam){

        // 검색 글자수 제한
        Integer searchString = 100;
        if (searchString > Integer.MAX_VALUE) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
        }
        if (CoreUtils.string.isNotBlank(search.getEntrprsNm()) && search.getEntrprsNm().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (CoreUtils.string.isNotBlank(search.getRceptNo()) && search.getRceptNo().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }

        if (CoreUtils.string.isBlank(corePaginationParam.getPage().toString()) || corePaginationParam.getPage() < 1)
            corePaginationParam.setPage(1L);
        if (CoreUtils.string.isBlank(corePaginationParam.getItemsPerPage().toString()) || corePaginationParam.getItemsPerPage() < 1)
            corePaginationParam.setItemsPerPage(ITEMS_PER_PAGE);

        boolean isExcel = false;

        long totalItems = tsptResrceUseReqstDao.selectResrceApplyCount(search);

        CorePaginationInfo info = new CorePaginationInfo(corePaginationParam.getPage(), corePaginationParam.getItemsPerPage(), totalItems);
        Long beginRowNum = info.getBeginRowNum();

        List<ResrceUseReqstDto> list = tsptResrceUseReqstDao.selectResrceApplyList(search, beginRowNum, corePaginationParam.getItemsPerPage(), false);
        CorePagination<ResrceUseReqstDto> pagination = new CorePagination<>(info, list);

        return pagination;
    }

    /** 실증 자원 상세정보 조회 */
    public ResrceDetailParam getResrceDetail(String reqstId) {
        ResrceDetailParam resrceDetailParam = tsptResrceUseReqstDao.selectResrceDetail(reqstId);
        ApplcntDto applcntdto = commonService.getApplcnt(resrceDetailParam.getApplcntId());
        resrceDetailParam.setMberDiv(applcntdto.getMberDiv());
        resrceDetailParam.setEntrprsNm(applcntdto.getEntrprsNm());
        resrceDetailParam.setUserNm(applcntdto.getUserNm());
        resrceDetailParam.setCttpc(applcntdto.getCttpc());
        resrceDetailParam.setEmail(applcntdto.getEmail());

        // log 정보생성
        BnMember worker = TspUtils.getMember();
        if (!CoreUtils.string.equals(worker.getMemberId(), resrceDetailParam.getApplcntId())) {
            String memberIp = CoreUtils.webutils.getRemoteIp(request);
            if(!"0:0:0:0:0:0:0:1".equals(memberIp)) {
                LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
                        .memberId(worker.getMemberId())
                        .memberIp(memberIp)
                        //.memberIp("127.0.0.1")
                        .workTypeNm("조회")
                        .workCn("실증 자원신청 상세정보")
                        .trgterNm(resrceDetailParam.getUserNm())
                        .trgterId(resrceDetailParam.getApplcntId())
                        .email(resrceDetailParam.getEmail())
                        //.birthday("응답값에 포함된 원문 생년월일")
                        .mobileNo(resrceDetailParam.getCttpc())
                        .build();
                indvdlInfSrchUtils.insert(logParam);
            }
        }

        return resrceDetailParam;
    }

    /** 실증자원신청 승인 */
    public ResrceDetailParam putApprove(String reqstId) {
        BnMember worker = TspUtils.getMember();
        InvalidationsException inputValidateErrs = new InvalidationsException();
        ResrceDetailParam param = tsptResrceUseReqstDao.selectResrceDetail(reqstId);
        if(CoreUtils.string.isNotBlank(param.getReqstSttus())&&param.getReqstSttus().equals(TspCode.reqstSttus.APPLY.toString())||param.getReqstSttus().equals(TspCode.reqstSttus.SPM_REQUEST.toString())) {
            tsptResrceUseReqstDao.putApprove(reqstId);
            ResrceHistParam resrceHistParam = new ResrceHistParam();
            resrceHistParam = ResrceHistParam.builder()
                    .histId(CoreUtils.string.getNewId("hist-"))
                    .reqstId(reqstId)
                    .opetrId(worker.getLoginId())
                    .processKnd("APPROVE")
                    .processResn("승인 처리되었습니다.")
                    .mberId(worker.getMemberId())
                    .mberNm(worker.getMemberNm())
                    .build();
            tsptResrceUseReqstHistDao.putResrceHist(resrceHistParam);

        }
        else inputValidateErrs.add("APPROVE",String.format(TspCode.validateMessage.허용불가,"신청 혹은 보완상태 아님"));
        return tsptResrceUseReqstDao.selectResrceDetail(reqstId);
    }

    public ResrceDetailParam putSpmRequest(String reqstId, String rsndqf) {
        BnMember worker = TspUtils.getMember();
        InvalidationsException inputValidateErrs = new InvalidationsException();
        ResrceDetailParam param = tsptResrceUseReqstDao.selectResrceDetail(reqstId);
        if(CoreUtils.string.isNotBlank(param.getReqstSttus())&&param.getReqstSttus().equals(TspCode.reqstSttus.APPLY.toString())||param.getReqstSttus().equals(TspCode.reqstSttus.SPM_REQUEST.toString())){
            if(rsndqf != null) {
                tsptResrceUseReqstDao.putSpmRequest(reqstId, rsndqf);
                ResrceHistParam resrceHistParam = new ResrceHistParam();
                resrceHistParam = ResrceHistParam.builder()
                        .histId(CoreUtils.string.getNewId("hist-"))
                        .reqstId(reqstId)
                        .opetrId(worker.getLoginId())
                        .processKnd("SPM_REQUEST")
                        .processResn(rsndqf)
                        .mberId(worker.getMemberId())
                        .mberNm(worker.getMemberNm())
                        .build();
                tsptResrceUseReqstHistDao.putResrceHist(resrceHistParam);
            }
            else inputValidateErrs.add("사유",String.format(TspCode.validateMessage.입력없음,"사유"));
        }else inputValidateErrs.add("SPM_REQUEST",String.format(TspCode.validateMessage.허용불가,"신청상태 아님"));
        return tsptResrceUseReqstDao.selectResrceDetail(reqstId);
    }
    
    public ResrceDetailParam putReject(String reqstId) {
        BnMember worker = TspUtils.getMember();
        InvalidationsException inputValidateErrs = new InvalidationsException();

        ResrceDetailParam param = tsptResrceUseReqstDao.selectResrceDetail(reqstId);
        if(CoreUtils.string.isNotBlank(param.getReqstSttus())&&param.getReqstSttus().equals(TspCode.reqstSttus.APPLY.toString())||param.getReqstSttus().equals(TspCode.reqstSttus.SPM_REQUEST.toString())){
            tsptResrceUseReqstDao.putReject(reqstId);
            ResrceHistParam resrceHistParam = new ResrceHistParam();
            resrceHistParam = ResrceHistParam.builder()
                    .histId(CoreUtils.string.getNewId("hist-"))
                    .reqstId(reqstId)
                    .opetrId(worker.getLoginId())
                    .processKnd("REJECT")
                    .processResn("반려 처리되었습니다.")
                    .mberId(worker.getMemberId())
                    .mberNm(worker.getMemberNm())
                    .build();
            tsptResrceUseReqstHistDao.putResrceHist(resrceHistParam);
        }else inputValidateErrs.add("SPM_REQUEST",String.format(TspCode.validateMessage.허용불가,"신청상태, 보완상태 아님"));
        return tsptResrceUseReqstDao.selectResrceDetail(reqstId);
    }

    /** 처리이력 */
    public CorePagination<ResrceHistParam> getResrceHist(String reqstId, CorePaginationParam corePaginationParam)
    {
        if (CoreUtils.string.isBlank(corePaginationParam.getPage().toString()) || corePaginationParam.getPage() < 1)
            corePaginationParam.setPage(1L);
        if (CoreUtils.string.isBlank(corePaginationParam.getItemsPerPage().toString()) || corePaginationParam.getItemsPerPage() < 1)
            corePaginationParam.setItemsPerPage(ITEMS_PER_PAGE);

        long totalItems = tsptResrceUseReqstHistDao.getResrceHistCount(reqstId);
        CorePaginationInfo info = new CorePaginationInfo(corePaginationParam.getPage(), corePaginationParam.getItemsPerPage(), totalItems);

        List<ResrceHistParam> list = tsptResrceUseReqstHistDao.getResrceHist(reqstId, info.getBeginRowNum(), info.getItemsPerPage());
        CorePagination<ResrceHistParam> pagination = new CorePagination<>(info, list);

        return pagination;
    }

    public ModelAndView getListExcelDownload(ResrceListParam search) {
        boolean isExcel = true;

        ExcelWorkbook wb = new ExcelWorkbook();
        // 페이지 목록 조회
        List<ResrceUseReqstDto> list = tsptResrceUseReqstDao.selectResrceApplyList(search, isExcel);
        wb.setFilename("실증자원신청 현황");
        ExcelSheet<ResrceUseReqstDto> sheet = new ExcelSheet<>();
        ExcelMergeRows mergeRegions = new ExcelMergeRows();
        sheet.setMergeRegions(mergeRegions);
        sheet.addRows(list);
        sheet.setHeaders("번호","신청상태", "구분", "사업자명", "이름",
                "사용 시작일", "사용 종료일", "신청 일시");
        sheet.setProperties("number","exReqstSttus", "exMberDiv", "entrprsNm", "userNm", "exUseBeginDt",
                "exUseEndDt","exCreatDt");
        sheet.setTitle("실증자원신청 조회");
        sheet.setSheetName("실증자원신청 조회");
        wb.addSheet(sheet);

        return ExcelView.getView(wb);
    }

    /** 실증자원사용 목록 조회 */
    public CorePagination<ResrceUseReqstDto> getResrceUseList(CorePaginationParam corePaginationParam, ResrceListParam search){

        // 검색 글자수 제한
        Integer searchString = 100;
        if (searchString > Integer.MAX_VALUE) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
        }
        if (CoreUtils.string.isNotBlank(search.getUserNm()) && search.getUserNm().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (CoreUtils.string.isNotBlank(search.getUserNm()) && search.getUserNm().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }
        if (CoreUtils.string.isNotBlank(search.getRceptNo()) && search.getRceptNo().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }

        if (CoreUtils.string.isBlank(corePaginationParam.getPage().toString()) || corePaginationParam.getPage() < 1)
            corePaginationParam.setPage(1L);
        if (CoreUtils.string.isBlank(corePaginationParam.getItemsPerPage().toString()) || corePaginationParam.getItemsPerPage() < 1)
            corePaginationParam.setItemsPerPage(ITEMS_PER_PAGE);

        boolean isExcel = false;

        long totalItems = tsptResrceUseReqstDao.selectResrceUseCount(search);
        CorePaginationInfo info = new CorePaginationInfo(corePaginationParam.getPage(), corePaginationParam.getItemsPerPage(), totalItems);
        Long beginRowNum = info.getBeginRowNum();

        List<ResrceUseReqstDto> list = tsptResrceUseReqstDao.selectResrceUseList(search, beginRowNum, corePaginationParam.getItemsPerPage(), false);
        CorePagination<ResrceUseReqstDto> pagination = new CorePagination<>(info, list);


        return pagination;
    }

    /** 실증 자원사용 상세 신청정보 */
    public ResrceDetailParam getResrceUseDetail(String reqstId) {
        ResrceDetailParam resrceDetailParam = tsptResrceUseReqstDao.selectResrceUseDetail(reqstId);
        ApplcntDto applcntdto = commonService.getApplcnt(resrceDetailParam.getApplcntId());
        resrceDetailParam.setMberDiv(applcntdto.getMberDiv());
        resrceDetailParam.setEntrprsNm(applcntdto.getEntrprsNm());
        resrceDetailParam.setUserNm(applcntdto.getUserNm());
        resrceDetailParam.setCttpc(applcntdto.getCttpc());
        resrceDetailParam.setEmail(applcntdto.getEmail());

        // log 정보생성
        BnMember worker = TspUtils.getMember();
        if (!CoreUtils.string.equals(worker.getMemberId(), resrceDetailParam.getApplcntId())) {
            String memberIp = CoreUtils.webutils.getRemoteIp(request);
            if(!"0:0:0:0:0:0:0:1".equals(memberIp)) {
                LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
                        .memberId(worker.getMemberId())
                        .memberIp(memberIp)
                        //.memberIp("127.0.0.1")
                        .workTypeNm("조회")
                        .workCn("실증 자원사용 상세정보")
                        .trgterNm(resrceDetailParam.getUserNm())
                        .trgterId(resrceDetailParam.getApplcntId())
                        .email(resrceDetailParam.getEmail())
                        //.birthday("응답값에 포함된 원문 생년월일")
                        .mobileNo(resrceDetailParam.getCttpc())
                        .build();
                indvdlInfSrchUtils.insert(logParam);
            }
        }

        return resrceDetailParam;
    }

    /** 승인 취소 */
    public ResrceDetailParam putCancel(String reqstId)
    {
        BnMember worker = TspUtils.getMember();
        InvalidationsException inputValidateErrs = new InvalidationsException();
        ResrceDetailParam param = tsptResrceUseReqstDao.selectResrceUseDetail(reqstId);
        if(CoreUtils.string.isNotBlank(param.getUseSttus())&&param.getUseSttus().equals(TspCode.reqUsage.WAITING.toString()))
        {
            tsptResrceUseReqstDao.putCancel(reqstId);
            ResrceHistParam resrceHistParam = new ResrceHistParam();
            resrceHistParam = ResrceHistParam.builder()
                    .histId(CoreUtils.string.getNewId("hist-"))
                    .reqstId(reqstId)
                    .opetrId(worker.getLoginId())
                    .processKnd("APPROVE_CANCEL")
                    .processResn("승인취소 처리되었습니다.")
                    .mberId(worker.getMemberId())
                    .mberNm(worker.getMemberNm())
                    .build();
            tsptResrceUseReqstHistDao.putResrceHist(resrceHistParam);
        }else
            throw new InvalidationException("대기중 상태가 아닙니다.");
        return tsptResrceUseReqstDao.selectResrceUseDetail(reqstId);
    }

    /**반환 요청 */
    public ResrceDetailParam putReqreturn(String reqstId) {
        BnMember worker = TspUtils.getMember();
        InvalidationsException inputValidateErrs = new InvalidationsException();
        ResrceDetailParam param = tsptResrceUseReqstDao.selectResrceUseDetail(reqstId);
        ApplcntDto applcnt = commonService.getApplcnt(param.getApplcntId());
        if(CoreUtils.string.isNotBlank(param.getUseSttus())&&param.getUseSttus().equals(TspCode.reqUsage.USE.toString()))
        {
            tsptResrceUseReqstDao.putReqreturn(reqstId);
            ResrceHistParam resrceHistParam = new ResrceHistParam();
            resrceHistParam = ResrceHistParam.builder()
                    .histId(CoreUtils.string.getNewId("hist-"))
                    .reqstId(reqstId)
                    .opetrId(worker.getLoginId())
                    .processKnd("REQ_RETURN")
                    .processResn("반환요청 처리되었습니다.")
                    .mberId(worker.getMemberId())
                    .mberNm(worker.getMemberNm())
                    .build();
            tsptResrceUseReqstHistDao.putResrceHist(resrceHistParam);
            commonService.sendEmail("실증자원 반환 부탁드립니다.","실증자원 반환 부탁드립니다." , applcnt, new HashMap<>());
        }else
            throw new InvalidationException("사용중 상태가 아닙니다.");
        return tsptResrceUseReqstDao.selectResrceUseDetail(reqstId);
    }

    /**반환 완료 */
    public ResrceDetailParam putReturn(String reqstId)
    {
        BnMember worker = TspUtils.getMember();
        InvalidationsException inputValidateErrs = new InvalidationsException();
        ResrceDetailParam param = tsptResrceUseReqstDao.selectResrceUseDetail(reqstId);
        if(CoreUtils.string.isNotBlank(param.getUseSttus())&&param.getUseSttus().equals(TspCode.reqUsage.USE.toString())||param.getUseSttus().equals(TspCode.reqUsage.REQ_RETURN.toString())){
            tsptResrceUseReqstDao.putReturn(reqstId);
            ResrceHistParam resrceHistParam = new ResrceHistParam();
            resrceHistParam = ResrceHistParam.builder()
                    .histId(CoreUtils.string.getNewId("hist-"))
                    .reqstId(reqstId)
                    .opetrId(worker.getLoginId())
                    .processKnd("REQ_COMPLETE")
                    .processResn("반환완료 처리되었습니다.")
                    .mberId(worker.getMemberId())
                    .mberNm(worker.getMemberNm())
                    .build();
            tsptResrceUseReqstHistDao.putResrceHist(resrceHistParam);
        }else
            throw new InvalidationException("사용중 혹은 반환요청 상태가 아닙니다.");
        return tsptResrceUseReqstDao.selectResrceUseDetail(reqstId);
    }

    /**실증자원사용 처리이력 */
    public CorePagination<ResrceHistParam> getResrceUseHist(String reqstId, CorePaginationParam corePaginationParam)
    {
        if (CoreUtils.string.isBlank(corePaginationParam.getPage().toString()) || corePaginationParam.getPage() < 1)
            corePaginationParam.setPage(1L);
        if (CoreUtils.string.isBlank(corePaginationParam.getItemsPerPage().toString()) || corePaginationParam.getItemsPerPage() < 1)
            corePaginationParam.setItemsPerPage(ITEMS_PER_PAGE);

        long totalItems = tsptResrceUseReqstHistDao.getResrceHistUseCount(reqstId);
        CorePaginationInfo info = new CorePaginationInfo(corePaginationParam.getPage(), corePaginationParam.getItemsPerPage(), totalItems);

        List<ResrceHistParam> list = tsptResrceUseReqstHistDao.getResrceUseHist(reqstId, info.getBeginRowNum(), info.getItemsPerPage());
        CorePagination<ResrceHistParam> pagination = new CorePagination<>(info, list);

        return pagination;
    }

    /**실증자원사용 엑셀 다운로드 */
    public ModelAndView getResrceListExcelDownload(ResrceListParam search) {
        boolean isExcel = true;

        ExcelWorkbook wb = new ExcelWorkbook();
        // 페이지 목록 조회
        List<ResrceUseReqstDto> list = tsptResrceUseReqstDao.selectResrceUseList(search, isExcel);
        wb.setFilename("실증자원사용 현황");
        ExcelSheet<ResrceUseReqstDto> sheet = new ExcelSheet<>();
        ExcelMergeRows mergeRegions = new ExcelMergeRows();
        sheet.setMergeRegions(mergeRegions);
        sheet.addRows(list);
        sheet.setHeaders("번호","사용상태", "구분", "사업자명", "이름",
                "사용 시작일", "사용 종료일");
        sheet.setProperties("number","exUseSttus", "exMberDiv", "entrprsNm", "userNm", "exUseBeginDt",
                "exUseEndDt");
        sheet.setTitle("실증자원사용 조회");
        sheet.setSheetName("실증자원사용 조회");
        wb.addSheet(sheet);

        return ExcelView.getView(wb);
    }
}
