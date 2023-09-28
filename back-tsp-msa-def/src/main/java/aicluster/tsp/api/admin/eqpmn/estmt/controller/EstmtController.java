package aicluster.tsp.api.admin.eqpmn.estmt.controller;

import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtListParam;
import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtOzReportParam;
import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtProcessParam;
import aicluster.tsp.api.admin.eqpmn.estmt.service.EstmtService;
import aicluster.tsp.api.admin.eqpmn.use.param.UseDscntParam;
import aicluster.tsp.common.dto.EqpmnEstmtDetailDto;
import aicluster.tsp.common.dto.EqpmnEstmtListDto;
import aicluster.tsp.common.entity.TsptEqpmnEstmtReqstHist;
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
@RequestMapping("/api/admin/eqpmns/estmts")
@RequiredArgsConstructor
@Api(tags = "사용관리 > 견적신청")
public class EstmtController {
	
	@Autowired
	private EstmtService estmtService;

	@ApiOperation(value = "견적신청 목록 조회", notes = "견적신청 목록 조회.")
	@GetMapping("")
	public CorePagination<EqpmnEstmtListDto> getEstmtList(EstmtListParam estmtListParam, CorePaginationParam cpParam) {
		return estmtService.getEstmtList(estmtListParam, cpParam);
	}

	@ApiOperation(value = "엑셀 다운로드", notes = "엑셀 다운로드 입니다.")
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDownload(EstmtListParam estmtListParam) {
	  	return estmtService.getEstmtListExcelDownload(estmtListParam);
	}
	  
	@ApiOperation(value = "견적요청 상세 > 신청정보", notes = "신청정보")
	@GetMapping("/{eqpmnEstmtId}")
	public EqpmnEstmtDetailDto getEstmtDetailInfo(@PathVariable("eqpmnEstmtId") String eqpmnEstmtId) {
		EqpmnEstmtDetailDto eqpmnEstmtDetailDto = estmtService.getEstmtDetailInfo(eqpmnEstmtId);
		return eqpmnEstmtDetailDto;
	}
	
	@ApiOperation(value = "견적요청 상세 > 신청정보 > 사용금액 재설정", notes = "신청정보 금액 재설정")
	@PutMapping("/dscnt")
	public void putEstmtDetailCalc(@RequestBody UseDscntParam param) {
		estmtService.putEstmtDetailCalc(param);
	}

	@ApiOperation(value = "견적신청 처리내역 조회", notes = "견적신청 처리내역 조회.")
	@GetMapping("/hist-list/{estmtId}")
	public CorePagination<TsptEqpmnEstmtReqstHist> getEstmtHistList(@PathVariable("estmtId") String estmtId,
																	CorePaginationParam cpParam) {
		return estmtService.getEstmtHistList(estmtId, cpParam);
	}

	@ApiOperation(value = "견적신청 처리", notes = "견적신청 처리")
	@PutMapping("/estmt-reqst-process")
	public void estmtReqstProcess(@RequestBody EstmtProcessParam param) {
		estmtService.estmtReqstProcess(param);
	}

	@ApiOperation(value = "견적서 발급", notes = "견적서 발급")
	@GetMapping("/ozreport/{estmtId}")
	public EstmtOzReportParam getOzEstmt(@PathVariable("estmtId") String estmtId) {
		return estmtService.getOzEstmt(estmtId);
	}

}
