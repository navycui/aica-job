package aicluster.tsp.api.admin.eqpmn.reprt.controller;

import aicluster.tsp.api.admin.eqpmn.reprt.param.ReprtParam;
import aicluster.tsp.api.admin.eqpmn.reprt.param.ReprtProcessParam;
import aicluster.tsp.api.admin.eqpmn.reprt.service.ReprtService;
import aicluster.tsp.common.dto.EqpmnReprtDetailDto;
import aicluster.tsp.common.dto.EqpmnReprtDto;
import aicluster.tsp.common.entity.TsptEqpmnReprtHist;
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
@RequestMapping("/api/admin/eqpmns/reprt")
@RequiredArgsConstructor
@Api(tags = "결과보고서 관리" )
public class ReprtController {

	@Autowired
	private ReprtService service;

	@ApiOperation(value = "결과보고서 조회", notes = "정보 조회")
	@GetMapping("")
	public CorePagination<EqpmnReprtDto> getEqpmnlReprtList(ReprtParam param,
															CorePaginationParam cpParam){
		return service.getEqpmnlReprtList(param, cpParam);
	}

	@ApiOperation(value = "엑셀 다운로드", notes = "엑셀 다운로드 입니다.")
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDownload(ReprtParam param) {
		return service.getListExcelDownload(param);
	}

	@ApiOperation(value = "결과보고서 상세정보 조회", notes = "상세 정보 조회")
	@GetMapping("/{reprtId}")
	public EqpmnReprtDetailDto getEqpmnReprtDetail(@PathVariable("reprtId") String reprtId){
		return service.getEqpmnReprtDetail(reprtId);
	}

	@ApiOperation(value = "결과보고서 처리", notes = "결과보고서 처리")
	@PutMapping("/process")
	public void UpdateReprtProcess(@RequestBody ReprtProcessParam param) {
		service.updateReprtProcess(param);
	}

	@ApiOperation(value = "결과보고서 처리내역 조회", notes = "결과보고서 처리내역 조회.")
	@GetMapping("/hist-list/{reprtId}")
	public CorePagination<TsptEqpmnReprtHist> getEqpmnReprtHistList(@PathVariable("reprtId") String reprtId,
																	CorePaginationParam cpParam) {
		return service.getEqpmnReprtHistList(reprtId, cpParam);
	}
}
