package aicluster.tsp.api.front.usereqst.estmt.controller;


import aicluster.tsp.api.common.param.CommonReturnList;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtRntfeeParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtSelectParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtUseDateParam;
import aicluster.tsp.api.front.usereqst.estmt.service.UseReqstEstmtService;
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
@RequestMapping("/api/front/use-reqst/estmts")
@RequiredArgsConstructor
@Api(tags = "Front > 견적신청")
public class UseReqstEstmtController {

	@Autowired
	private UseReqstEstmtService service;


	@ApiOperation(value = "견적신청 장비선택 리스트", notes = "견적신청")
	@GetMapping("")
	public CorePagination<FrontEqpmnSelectListDto> getEstmtEqpmn(UseReqstEstmtSelectParam param, CorePaginationParam cpParam){
		return service.getEstmtEqpmnList(param, cpParam);
	}

	@ApiOperation(value = "견적신청 장비선택", notes = "견적신청")
	@GetMapping("/{eqpmnId}")
	public FrontEqpmnSelectDto getEstmtEqpmnInfo(@PathVariable("eqpmnId") String eqpmnId){
		return service.getEstmtEqpmnInfo(eqpmnId);
	}

	@ApiOperation(value = "견적신청", notes = "견적신청")
	@PostMapping("")
	public void postEstmt(@RequestBody UseReqstEstmtParam param){
		service.postEstmt(param);
	}

//	@ApiOperation(value = "견적신청 수정", notes = "견적신청")
//	@PutMapping("")
//	public void PutEstmt(@RequestBody UseReqstEstmtParam param){
//		service.postEstmt(param);
//	}

	@ApiOperation(value = "견적신청 예상 사용금액", notes = "견적신청")
	@GetMapping("expect-rntfee")
	public UseReqstEstmtRntfeeParam getExpectRntfee(UseReqstEstmtParam param){
		return service.getExpectRntfee(param);
	}

	@ApiOperation(value = "사용기간 선택", notes = "견적신청")
	@GetMapping("eqpmn-use-date")
	public CommonReturnList<UseReqstEstmtUseDateParam> getEqpmnUseDateList(UseReqstEstmtUseDateParam param){
		return service.getEqpmnUseDateList(param);
	}


//	@ApiOperation(value = "견적신청", notes = "견적신청")
//	@PostMapping("/test")
//	public CommonReturnList<ArrayList> PostEstmtttt(UseReqstEstmtParam param){
//		//service.postEstmt(param, page, itemsPerPage);
//		ArrayList<String> a = new ArrayList<>();
//		a.add("aaa");
//		a.add("bbb");
//		CommonReturnList<ArrayList> aaa = new CommonReturnList(a);
//		return aaa;
//	}


}
