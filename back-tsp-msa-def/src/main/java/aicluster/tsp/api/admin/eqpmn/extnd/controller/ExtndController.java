package aicluster.tsp.api.admin.eqpmn.extnd.controller;

import aicluster.tsp.api.admin.eqpmn.extnd.param.ExtndDetailProcessParam;
import aicluster.tsp.api.admin.eqpmn.extnd.param.ExtndListParam;
import aicluster.tsp.api.admin.eqpmn.extnd.service.ExtndService;
import aicluster.tsp.common.dto.EqpmnExtndDetailDto;
import aicluster.tsp.common.dto.EqpmnExtndListDto;
import aicluster.tsp.common.entity.TsptEqpmnExtnHist;
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
@RequestMapping("/api/admin/eqpmns/extnd")
@RequiredArgsConstructor
@Api(tags = "사용관리 > 기간연장신청")
public class ExtndController {

	@Autowired
	private ExtndService extndService;

	@ApiOperation(value = "기간연장신청 목록 조회", notes = "기간연장신청 목록 조회.")
	@GetMapping("")
	public CorePagination<EqpmnExtndListDto> getExtndList(ExtndListParam param, CorePaginationParam cpParam) {
		return extndService.getExtndList(param, cpParam);
	}
	@ApiOperation(value = "엑셀 다운로드", notes = "엑셀 다운로드 입니다.")
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDownload(ExtndListParam param) {
		return extndService.getExtndListExcelDownload(param);
	}
	@ApiOperation(value = "기간연장신청 상세 조회", notes = "기간연장신청 상세 조회.")
	@GetMapping("/{etReqstId}")
	public EqpmnExtndDetailDto getExtndDetail(@PathVariable("etReqstId") String etReqstId) {
		return extndService.getExtndDetail(etReqstId);
	}
	@ApiOperation(value = "기간연장신청 상세 처리", notes = "기간연장신청 상세 처리.")
	@PutMapping("/{etReqstId}")
	public EqpmnExtndDetailDto putExtndDetail(@PathVariable("etReqstId") String etReqstId, @RequestBody(required = false) ExtndDetailProcessParam param) {
		return extndService.putExtndDetail(etReqstId, param);
	}
	@ApiOperation(value = "기간연장신청 처리이력 조회", notes = "기간연장신청 처리이력 조회.")
	@GetMapping("/hist/{etReqstId}")
	public CorePagination<TsptEqpmnExtnHist> getExtndHist(@PathVariable("etReqstId") String etReqstId, CorePaginationParam cpParam) {
		return extndService.getExtndHist(etReqstId, cpParam);
	}
}