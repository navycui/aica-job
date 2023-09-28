package aicluster.tsp.api.admin.anals.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.tsp.api.admin.anals.param.AnalsReqstParam;
import aicluster.tsp.api.admin.anals.param.AnalsReqstProcessParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.common.dao.TsptAnalsDao;
import aicluster.tsp.common.dto.*;
import aicluster.tsp.common.entity.TsptAnalsUntReqstHist;
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
public class AnalsService {

    @Autowired
    private CommonService commonService;
    @Autowired
    private TsptAnalsDao tsptAnalsDao;

    @Autowired
    private LogIndvdlInfSrchUtils indvdlInfSrchUtils;
    @Autowired
    private HttpServletRequest request;

    public static final long ITEMS_PER_PAGE = 5;

    public CorePagination<AnalsReqstDto> getAnalsReqstList(AnalsReqstParam Param, CorePaginationParam cpParam) {
        // 전체 건수 조회
        long totalItems = tsptAnalsDao.selectAnalsReqstCount(Param);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        List<AnalsReqstDto> list = tsptAnalsDao.selectAnalsReqstList(Param, info.getBeginRowNum(), info.getItemsPerPage(), false);

        return new CorePagination<>(info, list);
    }

    public ModelAndView getListExcelDownload(AnalsReqstParam param) {
        boolean isExcel = true;
        List<AnalsReqstDto> list = tsptAnalsDao.selectAnalsReqstList(param, 0L, 0L, isExcel);

        ExcelWorkbook wb = new ExcelWorkbook();
        wb.setFilename("분석도구 신청 현황");
        ExcelSheet<AnalsReqstDto> sheet = new ExcelSheet<>();
        ExcelMergeRows mergeRegions = new ExcelMergeRows();
        sheet.setMergeRegions(mergeRegions);
        sheet.addRows(list);
        sheet.setHeaders("번호","신청상태", "구분", "사업자명/이름", "사용시작일", "사용종료일", "신청일시");
        sheet.setProperties("number","exUseSttus", "exMberDiv", "entrprsNm", "exUseBeginDt", "exUseEndDt", "exCreatDt");
        sheet.setTitle("분석도구 신청");
        sheet.setSheetName("분석도구 신청");
        wb.addSheet(sheet);
        return ExcelView.getView(wb);
    }

    public AnalsReqstDetailDto getAnalsReqstDetail(String reqstId){
        AnalsReqstDetailDto data = tsptAnalsDao.selectAnalsReqstDetail(reqstId);
        if(data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.조회결과없음, "신청내역"));
        }

        ApplcntDto applcnt = commonService.getApplcnt(data.getCreatrId());

        data.setEmail(applcnt.getEmail());
        data.setMberDiv(applcnt.getMberDiv());
        data.setEntrprsNm(applcnt.getEntrprsNm());
        data.setOfcps(applcnt.getOfcps());
        data.setCttpc(applcnt.getCttpc());
        data.setPartcptnDiv(applcnt.getPartcptnDiv());

        // log 정보생성
        BnMember worker = TspUtils.getMember();
        if (!CoreUtils.string.equals(worker.getMemberId(), data.getCreatrId())) {
            String memberIp = CoreUtils.webutils.getRemoteIp(request);
            if(!"0:0:0:0:0:0:0:1".equals(memberIp)) {
                LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
                        .memberId(worker.getMemberId())
                        .memberIp(memberIp)
                        //.memberIp("127.0.0.1")
                        .workTypeNm("조회")
                        .workCn("분석환경 사용신청 상세정보")
                        .trgterNm(applcnt.getUserNm())
                        .trgterId(data.getCreatrId())
                        .email(applcnt.getEmail())
                        //.birthday("응답값에 포함된 원문 생년월일")
                        .mobileNo(applcnt.getCttpc())
                        .build();
                indvdlInfSrchUtils.insert(logParam);
            }
        }

        return data;
    }

    public CorePagination<TsptAnalsUntReqstHist> getAnalsReqstHistList(String reqstId, CorePaginationParam cpParam) {
        boolean isExcel = false;
        // 전체 건수 확인
        long totalItems = tsptAnalsDao.selectAnalsReqstHistCount(reqstId);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<TsptAnalsUntReqstHist> list = tsptAnalsDao.selectAnalsReqstHistList(reqstId, info.getBeginRowNum(), info.getItemsPerPage(), isExcel);
        CorePagination<TsptAnalsUntReqstHist> pagination = new CorePagination<>(info, list);
        // 목록 조회
        return pagination;
    }

    public void updateAnalsReqstProcess(AnalsReqstProcessParam param) {
        if(CoreUtils.string.isBlank(param.getReqstId())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "신청ID"));
        }
        if(CoreUtils.string.isBlank(param.getUseSttus())) {
            throw new InvalidationException(String.format(TspCode.validateMessage.입력없음, "상태"));
        }
        AnalsReqstProcessParam data = tsptAnalsDao.selectAnalsReqstProcess(param.getReqstId());

        if (data == null) {
            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
        }

        BnMember worker = TspUtils.getMember();
        if(worker == null ){
            worker = new BnMember();
            worker.setMemberId("member-20186b1226b444b88dee46e5e356de6e");
            worker.setMemberNm("이충혁");
        }
        param.setUpdusrId(worker.getMemberId());
        tsptAnalsDao.updateAnalsReqstProcess(param);

        // Hist 처리
        TsptAnalsUntReqstHist hist = TsptAnalsUntReqstHist.builder()
                .histId(CoreUtils.string.getNewId("reqstHist-"))
                .reqstId(param.getReqstId())
                .opetrId("노출될 ID")
                .processKnd(param.getUseSttus())
                .processResn(String.format(TspCode.histMessage.처리이력, TspCode.processKnd.valueOf(param.getUseSttus()).getTitle()))
                .mberId(worker.getMemberId())
                .mberNm(worker.getMemberNm())
                .build();

        tsptAnalsDao.insertAnalsReqstHist(hist);
    }

}

