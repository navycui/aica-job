package aicluster.tsp.api.admin.resrce.controller;

import aicluster.tsp.api.admin.resrce.param.ResrceDetailParam;
import aicluster.tsp.api.admin.resrce.param.ResrceHistParam;
import aicluster.tsp.api.admin.resrce.param.ResrceListParam;
import aicluster.tsp.api.admin.resrce.service.ResrceService;
import aicluster.tsp.api.common.param.CommonRequestParam;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.common.dto.ResrceUseReqstDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/admin/resrce")
@RequiredArgsConstructor
@Api(tags = "실증 자원")

public class ResrceController {

    @Autowired
    private ResrceService service;
    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "실증자원신청 목록", notes = "실증 자원신청에 대한 목록을 조회합니다.")
    @GetMapping("")
    public CorePagination<ResrceUseReqstDto> getResrceList(
            CorePaginationParam corePaginationParam,
            ResrceListParam search) {
        return service.getResrceList(search, corePaginationParam);
    }

    @ApiOperation(value = "실증자원신청정보 상세신청정보", notes = "실증 자원신청정보에 대한 상세목록을 조회합니다.")
    @GetMapping("/{reqstId}")
    public ResrceDetailParam getResrceDetail(@PathVariable("reqstId") String reqstId) {
        return service.getResrceDetail(reqstId);
    }

    @ApiOperation(value = "실증자원신청 상세처리이력", notes = "실증 자원신청처리이력에 대한 상세목록을 조회합니다.")
    @GetMapping("/{reqstId}/hist")
    public CorePagination<ResrceHistParam> getResrceHist(@PathVariable("reqstId") String reqstId, CorePaginationParam corePaginationParam)
    {
        return service.getResrceHist(reqstId, corePaginationParam) ;
    }

    @ApiOperation(value = "승인 팝업", notes = "실증 자원신청 승인")
    @PutMapping("/{reqstId}/approve")
    public ResrceDetailParam putApprove(@PathVariable("reqstId") String reqstId) {
        return service.putApprove(reqstId);
    }

    @ApiOperation(value = "보완 팝업", notes = "실증 자원신청 보완")
    @PutMapping("/{reqstId}/spm-request")
    public ResrceDetailParam putSpmRequest(@PathVariable("reqstId") String reqstId, @RequestBody CommonRequestParam rsndqf) {
        return service.putSpmRequest(reqstId, rsndqf.getParam());
    }

    @ApiOperation(value = "반려 팝업", notes = "실증 자원신청 반려")
    @PutMapping("/{reqstId}/reject")
    public ResrceDetailParam putReject(@PathVariable("reqstId") String reqstId) {
        return service.putReject(reqstId);
    }

    @ApiOperation(value = "실증 자원신청 엑셀 다운로드", notes = "실증 자원신청 엑셀 다운로드 입니다.")
    @GetMapping("/excel-dwld")
    public ModelAndView getListExcelDownload(ResrceListParam search) {
        return service.getListExcelDownload(search);
    }

    @ApiOperation(value = "실증 자원사용 목록")
    @GetMapping("/resrce-use")
    public CorePagination<ResrceUseReqstDto> getResrceUseList(
            CorePaginationParam corePaginationParam,ResrceListParam search){

        return service.getResrceUseList(corePaginationParam, search);
    }

    @ApiOperation(value = "실증 자원사용 상세 신청정보")
    @GetMapping("/resrce-use/{reqstId}")
    public ResrceDetailParam getResrceUseDetail(@PathVariable("reqstId") String reqstId) {
        return service.getResrceUseDetail(reqstId);
    }

    @ApiOperation(value = "승인취소")
    @PutMapping("/resrce-use/{reqstId}/cancel")
    public ResrceDetailParam putCancel(@PathVariable("reqstId") String reqstId) {
        return service.putCancel(reqstId);
    }

    @ApiOperation(value = "반환요청")
    @PutMapping("/resrce-use/{reqstId}/req-return")
    public ResrceDetailParam putReqreturn(@PathVariable("reqstId") String reqstId) {
        return service.putReqreturn(reqstId);
    }

    @ApiOperation(value = "반환완료")
    @PutMapping("/resrce-use/{reqstId}/return")
    public ResrceDetailParam putReturn(@PathVariable("reqstId") String reqstId) {
        return service.putReturn(reqstId);
    }

    @ApiOperation(value = "실증자원사용 처리이력", notes = "실증 자원사용 처리이력에 대한 상세목록을 조회합니다.")
    @GetMapping("/resrce-use/{reqstId}/hist")
    public CorePagination<ResrceHistParam> getResrceUseHist(@PathVariable("reqstId") String reqstId, CorePaginationParam corePaginationParam)
    {
        return service.getResrceUseHist(reqstId, corePaginationParam);
    }

    @ApiOperation(value = "실증 자원신청 엑셀 다운로드", notes = "실증 자원신청 엑셀 다운로드 입니다.")
    @GetMapping("/resrce-use/excel-dwld")
    public ModelAndView getResrceListExcelDownload(ResrceListParam search) {
        return service.getResrceListExcelDownload(search);
    }

    @ApiOperation(value = "첨부 파일 한개 다운로드 - contentType", notes = "attachmentId 로 파일을 contentType 에 맞게 해서 다운로드 한다.")
    @GetMapping("/file-dwld-contentType/{attachmentId}")
    public void downloadFile_contentType(HttpServletResponse response, @PathVariable String attachmentId) {
        commonService.downloadFile_contentType(response, attachmentId);
    }
}
