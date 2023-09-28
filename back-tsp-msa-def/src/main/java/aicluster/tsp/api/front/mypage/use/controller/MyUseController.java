package aicluster.tsp.api.front.mypage.use.controller;

import aicluster.tsp.api.common.param.CommonReturnList;
import aicluster.tsp.api.front.mypage.use.param.*;
import aicluster.tsp.api.front.mypage.use.service.MyUseService;
import aicluster.tsp.common.dto.*;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/front/mypage/use")
@RequiredArgsConstructor
@Api(tags = "Front > 마이페이지 > 장비사용")
public class MyUseController {

	@Autowired
	private MyUseService service;

	@ApiOperation(value = "목록 조회", notes = "목록 조회")
	@GetMapping("")
	public CorePagination<FrontMyUseListDto> getUseList(MyUseListSearchParam param, CorePaginationParam cpParam){
		return service.getUseList(param, cpParam);
	}

	@ApiOperation(value = "상세(승인전, 후) 조회", notes = "상세(승인전, 후) 조회")
	@GetMapping("/{reqstId}")
	public MyUseDetailParam getUseDetail(@PathVariable(value = "reqstId") String reqstId){
		return service.getUseDetail(reqstId);
	}

	@ApiOperation(value = "상세 신청취소", notes = "상세 신청취소")
	@PutMapping("/{reqstId}/cancel")
	public void putUseCancel(@PathVariable(value = "reqstId") String reqstId){
		service.putUseCancel(reqstId);
	}

	@ApiOperation(value = "상세 사용종료", notes = "상세 사용종료")
	@PutMapping("/{reqstId}/enduse")
	public void putUseEnduse(@PathVariable(value = "reqstId") String reqstId){
		service.putUseEnduse(reqstId);
	}

	@ApiOperation(value = "상세 기간연장내역", notes = "상세 기간연장내역")
	@GetMapping("/{reqstId}/extnd")
	public CommonReturnList<FrontMyUseExtndListDto> getUseExtndList(@PathVariable(value = "reqstId") String reqstId){
		return new CommonReturnList<>(service.getUseExtndList(reqstId));
	}

	@ApiOperation(value = "상세 기간연장 조회", notes = "상세 기간연장 조회")
	@GetMapping("/{reqstId}/extnd/cal")
	public FrontMyUseExtndCalenDto getUseExtndReqst(@PathVariable(value = "reqstId") String reqstId, @RequestParam(required = false, value = "searchMonth") String searchMonth){
		return service.getUseExtndReqst(reqstId, searchMonth);
	}

	@ApiOperation(value = "상세 기간연장 신청 전 조회", notes = "상세 기간연장 신청 전 조회")
	@GetMapping("/{reqstId}/extnd/reqst")
	public MyUseExtndParam getUseExtndReqst(@PathVariable(value = "reqstId") String reqstId, MyUseExtndReqstParam myUseExtndReqstParam){
		return service.getUseExtndReqst(reqstId, myUseExtndReqstParam);
	}

	@ApiOperation(value = "상세 기간연장 신청", notes = "상세 기간연장 신청")
	@PostMapping("/{reqstId}/extnd/reqst")
	public void postUseExtndReqst(@PathVariable(value = "reqstId") String reqstId, @RequestBody MyUseExtndParam myUseExtndParam){
		service.postUseExtndReqst(reqstId, myUseExtndParam);
	}

	@ApiOperation(value = "상세 사용료부과내역", notes = "상세 사용료부과내역")
	@GetMapping("/{reqstId}/rntfee")
	public FrontMyUseExtndRntfeeDto getUseRntfee(@PathVariable(value = "reqstId") String reqstId){
		return service.getUseRntfee(reqstId);
	}

	@ApiOperation(value = "상세 결과보고서 조회", notes = "상세 결과보고서 조회")
	@GetMapping("/{reqstId}/reprt")
	public FrontMyUseReprtDto getUseReprt(@PathVariable(value = "reqstId") String reqstId){
		return service.getUseReprt(reqstId);
	}

	@ApiOperation(value = "상세 결과보고서 임시저장/제출", notes = "상세 결과보고서 임시저장/제출")
	@PostMapping("/{reqstId}/reprt/{reprtSttus}")
	public FrontMyUseReprtDto postUseReprt(@PathVariable(value = "reqstId") String reqstId, @RequestBody FrontMyUseReprtDto.MyUseReprt myUseReprt, @PathVariable(value = "reprtSttus") String reprtSttus){
		return service.postUseReprt(reqstId, myUseReprt, reprtSttus);
	}

}
