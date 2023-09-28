package aicluster.tsp.api.admin.eqpmn.mngm.controller;

import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmStatusParam;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmSearchParam;
import aicluster.tsp.api.admin.eqpmn.mngm.service.MngmStatusService;
import aicluster.tsp.common.util.TspCode;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@RestController
@RequestMapping("/api/admin/eqpmns/mngms")
@RequiredArgsConstructor
@Api(tags = "장비정보 > 사용현황")
public class MngmStatusController {

  @Autowired
  private MngmStatusService service;

  @ApiOperation(value = "장비 사용 현황 목록 조회", notes = "장비 사용중인 목록을 조회합니다.")
  @GetMapping("/status")
  public CorePagination<MngmStatusParam> getList(
          CorePaginationParam corePaginationParam,
          MngmSearchParam search) {
    return service.getList(TspCode.eqpmnUsage.USE.toString(), search, corePaginationParam);
  }

  @ApiOperation(value = "엑셀 다운로드", notes = "엑셀 다운로드 입니다.")
  @GetMapping("/status/excel-dwld")
  public ModelAndView getListExcelDownload(MngmSearchParam search) {
    return service.getListExcelDownload(TspCode.eqpmnUsage.USE.toString(), search);
  }
}
