package aicluster.tsp.api.admin.eqpmn.use.controller;

import aicluster.tsp.api.admin.eqpmn.use.param.*;
import aicluster.tsp.api.admin.eqpmn.use.service.UseService;
import aicluster.tsp.api.common.param.CommonRequestParam;
import aicluster.tsp.api.common.param.CommonReturnList;
import aicluster.tsp.common.dto.EqpmnUseDetailDto;
import aicluster.tsp.common.dto.EqpmnUseDto;
import aicluster.tsp.common.dto.EqpmnUseExtndDto;
import aicluster.tsp.common.dto.EqpmnUseRntfeeHistDto;
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
@RequestMapping("/api/admin/eqpmns/use")
@RequiredArgsConstructor
@Api(tags = "장비 사용 관리" )
public class UseController {

	@Autowired
	private UseService service;

//	@ApiOperation(value = "장비 사용 관리 리스트 조회", notes = "장비 사용관리 리스트를 page형식으로 조회한다.")
//	@GetMapping("")
//	public CorePagination<EqpmnUseDto> getEquipmentRentalUsageList(
//																				EqpmnUseParam eqpmnUseParam,
//																				@RequestParam(required = false) Long page,
//																				@RequestParam(required = false) Long itemsPerPage){
//		return service.getEquipmentRentalUsageList(eqpmnUseParam, page, itemsPerPage);
//	}
//
//	@ApiOperation(value = "장비 사용 관리 조회", notes = "eqpmnRentalId로 장비 사용관리를 조회한다.")
//	@GetMapping("/{eqpmnRentalId}")
//	public EqpmnUseDto getEquipmentRentalUsage(@PathVariable("eqpmnRentalId") String eqpmnRentalId){
//		return service.getEquipmentRentalUsage(eqpmnRentalId);
//	}

	@ApiOperation(value = "장비사용 정보 조회", notes = "상세 정보 조회")
	@GetMapping("")
	public CorePagination<EqpmnUseDto> getEqpmnlUseList(EqpmnUseParam param, CorePaginationParam cpParam){
		return service.getEqpmnlUseList(param, cpParam);
	}

	@ApiOperation(value = "엑셀 다운로드", notes = "엑셀 다운로드 입니다.")
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDownload(EqpmnUseParam param) {
		return service.getListExcelDownload(param);
	}

	@ApiOperation(value = "장비사용 상세정보 조회", notes = "상세 정보 조회")
	@GetMapping("/{reqstId}")
	public EqpmnUseDetailDto getEqpmnUseDetail(@PathVariable("reqstId") String reqstId){
		return service.getEqpmnUseDetail(reqstId);
	}

	@ApiOperation(value = "장비사용 처리", notes = "장비사용 처리")
	@PutMapping("/process")
	public void UpdateUseProcess(@RequestBody UseReqstProcessParam param) {
		service.updateUseProcess(param);
	}


	@ApiOperation(value = "미납 처리", notes = "미납 처리")
	@PutMapping("/npy-process")
	public void UpdateUseNpyProcess(@RequestBody UseNpyProcessParam param) {
		service.updateUseNpyProcess(param);
	}

	@ApiOperation(value = "입금 안내문 발송", notes = "입금 안내")
	@PutMapping("/rcpmny-gdcc")
	public void UpdateUseRcpmnyGdcc(@RequestBody UseRcpmnyGdccParam param) {
		service.updateUseRcpmnyGdcc(param);
	}

	@ApiOperation(value = "추가 금액등록", notes = "추가금액 등록")
	@PutMapping("/adit-rntfee")
	public void UpdateUseAditRntfee(@RequestBody UseAditRntfeeParam param) {
		service.updateUseAditRntfee(param);
	}

	@ApiOperation(value = "반입 완료처리", notes = "반입 완료처리")
	@PutMapping("/tkin")
	public void UpdateUseTkin(@RequestBody CommonRequestParam reqstId) {
		service.updateTkin(reqstId.getParam());
	}

	@ApiOperation(value = "사용료 부과내역", notes = "사용료 부과내역")
	@GetMapping("/rntfee/{reqstId}")
	public CommonReturnList<EqpmnUseRntfeeHistDto> getRntfeeList(@PathVariable("reqstId") String reqstId){
		return new CommonReturnList<>(service.getRntfeeList(reqstId));
	}

	@ApiOperation(value = "장비사용 할인 적용", notes = "장비사용 할인 적용")
	@PutMapping("/dscnt")
	public void updateUseReqstDscnt(@RequestBody UseDscntParam param) {
		service.updateUseReqstDscnt(param);
	}

	@ApiOperation(value = "사용 처리내역 조회", notes = "사용신청 처리내역 조회.")
	@GetMapping("/hist-list/{reqstId}")
	public CorePagination<TsptEqpmnUseReqstHist> getUseHistList(@PathVariable("reqstId") String reqstId, CorePaginationParam cpParam) {
		return service.getUseHistList(reqstId, cpParam);
	}

	@ApiOperation(value = "사용 기간연장관리 조회", notes = "사용신청 기간연장관리 조회.")
	@GetMapping("/extnd/{reqstId}")
	public CorePagination<EqpmnUseExtndDto> getExtndList(@PathVariable("reqstId") String reqstId, CorePaginationParam cpParam) {
		return service.getExtndList(reqstId, cpParam);
	}

	@ApiOperation(value = "사용 기간연장", notes = "사용신청 기간연장관리.")
	@PutMapping("/extnd")
	public void putExtnd(@RequestBody UseExtndParam param) {
		service.putExtnd(param);
	}

	@ApiOperation(value = "사용신청 취소", notes = "사용신청 취소.")
	@PutMapping("/reqst-cancel/{reqstId}")
	public void reqstCancel(@PathVariable("reqstId") String reqstId) {
		service.updateUseReqstCancel(reqstId);
	}

	//@ApiOperation(value = "반입 완료처리", notes = "반입 완료처리")
	//@PutMapping("/Test")
	//public String timeTest(String eqpmnId, Date startDt, Date endDt) {
		//return "총 가격 : " + service.getWorkingRntfee(eqpmnId, startDt, endDt);
		//service.updateTkin(reqstId);
	//}
}
