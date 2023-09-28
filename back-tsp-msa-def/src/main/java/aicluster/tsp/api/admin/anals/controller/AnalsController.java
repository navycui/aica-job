package aicluster.tsp.api.admin.anals.controller;

import aicluster.tsp.api.admin.anals.param.AnalsReqstParam;
import aicluster.tsp.api.admin.anals.param.AnalsReqstProcessParam;
import aicluster.tsp.api.admin.anals.service.AnalsService;
import aicluster.tsp.common.dto.AnalsReqstDetailDto;
import aicluster.tsp.common.dto.AnalsReqstDto;
import aicluster.tsp.common.entity.TsptAnalsUntReqstHist;
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
@RequestMapping("/api/admin/anals")
@RequiredArgsConstructor
@Api(tags = "분석도구" )
public class AnalsController {

//	@Autowired
//	private AnalsService service;

	@Autowired
	private AnalsService service;

	@ApiOperation(value = "분석도구 사용신청 정보 조회", notes = "정보 조회")
	@GetMapping("")
	public CorePagination<AnalsReqstDto> getAnalsReqstList(AnalsReqstParam Param,
														   CorePaginationParam cpParam){
		return service.getAnalsReqstList(Param, cpParam);
	}

	@ApiOperation(value = "엑셀 다운로드", notes = "엑셀 다운로드 입니다.")
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDownload(AnalsReqstParam param) {
		return service.getListExcelDownload(param);
	}

	@ApiOperation(value = "분석도구 사용신청 상세정보 조회", notes = "상세 정보 조회")
	@GetMapping("/{reqstId}")
	public AnalsReqstDetailDto getAnalsReqstDetail(@PathVariable("reqstId") String reqstId){
		return service.getAnalsReqstDetail(reqstId);
	}

	@ApiOperation(value = "분석도구 사용신청 처리내역 조회", notes = "사용신청 처리내역 조회.")
	@GetMapping("/hist-list/{reqstId}")
	public CorePagination<TsptAnalsUntReqstHist> getAnalsReqstHistList(@PathVariable("reqstId") String reqstId,
																	   CorePaginationParam cpParam) {
		return service.getAnalsReqstHistList(reqstId, cpParam);
	}

	@ApiOperation(value = "사용신청 보완,반려 처리", notes = "사용신청 보완,반려 처리")
	@PutMapping("/process")
	public void updateAnalsReqstProcess(@RequestBody AnalsReqstProcessParam param) {
		service.updateAnalsReqstProcess(param);
	}
//
//	@ApiOperation(value = "사용신청 승인 처리", notes = "사용신청 승인 처리")
//	@PutMapping("/consent")
//	public void updateUseReqstConsent(@RequestBody CommonRequestParam reqstId) {
//		service.updateUseReqstConsent(reqstId.getParam());
//	}

//	@ApiOperation(value = "사용신청 반출심의 처리", notes = "사용신청 반출심의 처리")
//	@PutMapping("/tkout")
//	public void updateUseReqstTkoutProcess(@RequestBody UseReqstTkoutProcessParam param) {
//		service.updateUseReqstTkoutProcess(param);
//	}
//
//	@ApiOperation(value = "사용신청 할인 적용", notes = "사용신청 할인 적용")
//	@PutMapping("/dscnt")
//	public void updateUseReqstDscnt(@RequestBody UseDscntParam param) {
//		service.updateUseReqstDscnt(param);
//	}
}
