package aicluster.tsp.api.front.usereqst.controller;

import aicluster.tsp.api.common.param.CommonReturnList;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtRntfeeParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtSelectParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtUseDateParam;
import aicluster.tsp.api.front.usereqst.service.FrontUseReqstService;
import aicluster.tsp.common.dto.FrontEqpmnSelectDto;
import aicluster.tsp.common.dto.FrontEqpmnSelectListDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/front/use-reqst")
@RequiredArgsConstructor
@Api(tags = "Front > 장비 사용신청")
public class FrontUseReqstController {

	@Autowired
	private FrontUseReqstService service;


	@ApiOperation(value = "사용신청 장비선택 리스트", notes = "사용신청")
	@GetMapping("")
	public CorePagination<FrontEqpmnSelectListDto> getEstmtEqpmn(UseReqstEstmtSelectParam param, CorePaginationParam cpParam){
		return service.getEstmtEqpmnList(param, cpParam);
	}

	@ApiOperation(value = "사용신청 장비선택", notes = "사용신청")
	@GetMapping("/{eqpmnId}")
	public FrontEqpmnSelectDto getEstmtEqpmnInfo(@PathVariable("eqpmnId") String eqpmnId){
		return service.getEstmtEqpmnInfo(eqpmnId);
	}

	@ApiOperation(value = "사용신청", notes = "사용신청")
	@PostMapping("")
	public void postUseReqst(@RequestBody UseReqstEstmtParam param){
		service.postUseReqst(param);
	}

	@ApiOperation(value = "사용신청 예상 사용금액", notes = "사용신청")
	@GetMapping("expect-rntfee")
	public UseReqstEstmtRntfeeParam getExpectRntfee(UseReqstEstmtParam param){
		return service.getExpectRntfee(param);
	}

	@ApiOperation(value = "사용기간 선택", notes = "사용신청")
	@GetMapping("eqpmn-use-date")
	public CommonReturnList<UseReqstEstmtUseDateParam> getEqpmnUseDateList(UseReqstEstmtUseDateParam param){
		return service.getEqpmnUseDateList(param);
	}



}
