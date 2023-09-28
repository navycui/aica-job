package aicluster.tsp.api.admin.eqpmn.use.controller;

import aicluster.tsp.api.admin.eqpmn.use.param.EqpmnUseReqstParam;
import aicluster.tsp.api.admin.eqpmn.use.param.UseDscntParam;
import aicluster.tsp.api.admin.eqpmn.use.param.UseReqstProcessParam;
import aicluster.tsp.api.admin.eqpmn.use.param.UseReqstTkoutProcessParam;
import aicluster.tsp.api.admin.eqpmn.use.service.UseReqstService;
import aicluster.tsp.api.common.param.CommonRequestParam;
import aicluster.tsp.common.dto.EqpmnUseReqstDetailDto;
import aicluster.tsp.common.dto.EqpmnUseReqstDto;
import aicluster.tsp.common.entity.TsptEqpmnUseReqstHist;
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
@RequestMapping("/api/admin/eqpmns/use-reqst")
@RequiredArgsConstructor
@Api(tags = "장비신청 관리" )
public class UseReqstController {

	@Autowired
	private UseReqstService service;

	@ApiOperation(value = "장비사용신청 정보 조회", notes = "상세 정보 조회")
	@GetMapping("")
	public CorePagination<EqpmnUseReqstDto> getEqpmnUseReqstList(EqpmnUseReqstParam eqpmnUseReqstParam,
																 CorePaginationParam cpParam){
		return service.getEqpmnUseReqstList(eqpmnUseReqstParam, cpParam);
	}

	@ApiOperation(value = "엑셀 다운로드", notes = "엑셀 다운로드 입니다.")
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDownload(EqpmnUseReqstParam param) {
		return service.getListExcelDownload(param);
	}

	@ApiOperation(value = "장비신청 상세정보 조회", notes = "상세 정보 조회")
	@GetMapping("/{reqstId}")
	public EqpmnUseReqstDetailDto getEqpmnUseReqstDetail(@PathVariable("reqstId") String reqstId){
		return service.getEqpmnUseReqstDetail(reqstId);
	}


	@ApiOperation(value = "사용신청 처리내역 조회", notes = "사용신청 처리내역 조회.")
	@GetMapping("/hist-list/{reqstId}")
	public CorePagination<TsptEqpmnUseReqstHist> getUseReqstHistList(@PathVariable("reqstId") String reqstId,
																	 CorePaginationParam cpParam) {
		return service.getUseReqstHistList(reqstId, cpParam);
	}

	@ApiOperation(value = "사용신청 보완,반려 처리", notes = "사용신청 보완,반려 처리")
	@PutMapping("/process")
	public void updateUseReqstProcess(@RequestBody UseReqstProcessParam param) {
		service.updateUseReqstProcess(param);
	}

	@ApiOperation(value = "사용신청 승인 처리", notes = "사용신청 승인 처리")
	@PutMapping("/consent")
	public void updateUseReqstConsent(@RequestBody CommonRequestParam reqstId) {
		service.updateUseReqstConsent(reqstId.getParam());
	}

	@ApiOperation(value = "사용신청 반출심의 처리", notes = "사용신청 반출심의 처리")
	@PutMapping("/tkout")
	public EqpmnUseReqstDetailDto updateUseReqstTkoutProcess(@RequestBody UseReqstTkoutProcessParam param) {
		return service.updateUseReqstTkoutProcess(param);
	}

	@ApiOperation(value = "사용신청 할인 적용", notes = "사용신청 할인 적용")
	@PutMapping("/dscnt")
	public void updateUseReqstDscnt(@RequestBody UseDscntParam param) {
		service.updateUseReqstDscnt(param);
	}
}
