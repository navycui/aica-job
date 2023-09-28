package aicluster.tsp.api.front.mypage.anals.controller;

import aicluster.tsp.api.front.mypage.anals.param.FrontMypageAnalsReqstParam;
import aicluster.tsp.api.front.mypage.anals.service.FrontMypageAnalsReqstService;
import aicluster.tsp.common.dto.FrontMyAnalsReqstDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/front/mypage/anals-reqst")
@RequiredArgsConstructor
@Api(tags = "Front > 마이페이지 > 분석도구")
public class FrontMypageAnalsReqstController {

	@Autowired
	private FrontMypageAnalsReqstService service;


	@ApiOperation(value = "사용신청 목록 조회", notes = "사용신청 목록 조회")
	@GetMapping("")
	public CorePagination<FrontMyAnalsReqstDto> getAnalsList(FrontMypageAnalsReqstParam param, CorePaginationParam cpParam){
		return service.getAnalsList(param, cpParam);
	}

	@ApiOperation(value = "상세 조회", notes = "상세 조회")
	@GetMapping("/{reqstId}")
	public FrontMyAnalsReqstDto getUseDetail(@PathVariable(value = "reqstId") String reqstId){
		return service.getAnalsDetail(reqstId);
	}

	@ApiOperation(value = "신청 취소", notes = "신청 취소")
	@PutMapping("/{reqstId}")
	public void putUseCancel(@PathVariable(value = "reqstId") String reqstId){
		service.putUseCancel(reqstId);
	}

}


