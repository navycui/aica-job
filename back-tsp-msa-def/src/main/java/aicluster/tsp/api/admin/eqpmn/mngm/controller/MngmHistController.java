package aicluster.tsp.api.admin.eqpmn.mngm.controller;

import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmMgtHistParam;
import aicluster.tsp.api.admin.eqpmn.mngm.service.MngmHistService;
import aicluster.tsp.common.entity.TsptEqpmnProcessHist;
import bnet.library.util.CoreUtils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@RestController
@RequestMapping("/api/admin/eqpmns/mngms")
@RequiredArgsConstructor
@Api(tags = "장비관리 > 이력조회" )
public class MngmHistController {

	@Autowired
	private MngmHistService service;

	@ApiOperation(value = "장비관리이력조회", notes = "장비관리이력조회")
	@GetMapping("/mngmhistlist/{eqpmnId}")
	public CorePagination<MngmMgtHistParam> getMgtList(@PathVariable("eqpmnId") String eqpmnId, @RequestParam(required = false) String manageDiv, CorePaginationParam cpParam){
		return service.getMgtList(eqpmnId, manageDiv, cpParam);
	}

	@ApiOperation(value = "엑셀 다운로드", notes = "엑셀 다운로드 입니다.")
	@GetMapping("/mngmhistList/excel-dwld/{eqpmnId}")
	public ModelAndView getMgtListExcelDwld(@PathVariable("eqpmnId") String eqpmnId, @RequestParam(required = false) String manageDiv) {
		return service.getMgtListExcelDwld(eqpmnId, manageDiv);
	}

	@ApiOperation(value = "장비처리이력조회", notes = "장비처리이력조회")
	@GetMapping("/prohistlist/{eqpmnId}")
	public CorePagination<TsptEqpmnProcessHist> getProcessHistList(@PathVariable("eqpmnId") String eqpmnId, CorePaginationParam cpParam){
		return service.getProHistList(eqpmnId, cpParam);
	}
}
