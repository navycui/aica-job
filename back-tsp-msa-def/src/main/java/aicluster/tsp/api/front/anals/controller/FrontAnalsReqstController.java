package aicluster.tsp.api.front.anals.controller;

import aicluster.tsp.api.common.param.CommonReturnList;
import aicluster.tsp.api.front.anals.param.FrontAnalsReqstParam;
import aicluster.tsp.api.front.anals.param.FrontAnalsTmplatParam;
import aicluster.tsp.api.front.anals.service.FrontAnalsReqstService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/front/anals-reqst")
@RequiredArgsConstructor
@Api(tags = "Front > 분석도구 사용신청")
public class FrontAnalsReqstController {

	@Autowired
	private FrontAnalsReqstService service;


	@ApiOperation(value = "분석도구 템플릿 리스트", notes = "분석도구 사용 가능 템플릿 리스트")
	@GetMapping("get-tmplat")
	public CommonReturnList<FrontAnalsTmplatParam> getAnalsTmplat(FrontAnalsTmplatParam param){
		return service.getAnalsTmplat(param);
	}
//
//	@ApiOperation(value = "사용신청 장비선택", notes = "사용신청")
//	@GetMapping("/{eqpmnId}")
//	public FrontEqpmnSelectDto getEstmtEqpmnInfo(@PathVariable("eqpmnId") String eqpmnId){
//		return service.getEstmtEqpmnInfo(eqpmnId);
//	}

	@ApiOperation(value = "사용신청", notes = "사용신청")
	@PostMapping("")
	public void postAnals(@RequestBody FrontAnalsReqstParam param){
		service.postAnals(param);
	}

}


