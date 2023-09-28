package aicluster.tsp.api.front.mypage.estmt.controller;

import aicluster.tsp.api.front.mypage.estmt.param.MyEstmtDetailParam;
import aicluster.tsp.api.front.mypage.estmt.param.MyEstmtDwldInfoParam;
import aicluster.tsp.api.front.mypage.estmt.param.MyEstmtListSearchParam;
import aicluster.tsp.api.front.mypage.estmt.service.MyEstmtService;
import aicluster.tsp.common.dto.FrontMyEstmtListDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/front/mypage/estmts")
@RequiredArgsConstructor
@Api(tags = "Front > 마이페이지 > 견적요청")
public class MyEstmtController {

	@Autowired
	private MyEstmtService service;

	@ApiOperation(value = "견적, 사용신청 정보조회", notes = "견적, 사용신청 정보조회")
	@GetMapping("/tmp/{eqpmnId}")
	public MyEstmtDetailParam getEstmtDetailToUse(@PathVariable(value = "eqpmnId") String eqpmnId){
		return service.getEstmtDetailToUse(eqpmnId);
	}

	@ApiOperation(value = "견적요청 목록 조회", notes = "견적요청 목록 조회")
	@GetMapping("")
	public CorePagination<FrontMyEstmtListDto> getEstmtList(MyEstmtListSearchParam param, CorePaginationParam cpParam){
		return service.getEstmtList(param, cpParam);
	}

	@ApiOperation(value = "견적요청 상세정보 조회", notes = "견적요청 상세정보 조회")
	@GetMapping("/{estmtId}")
	public MyEstmtDetailParam getEstmtDetail(@PathVariable(value = "estmtId") String estmtId){
		return service.getEstmtDetail(estmtId);
	}

	@ApiOperation(value = "견적요청 상세정보 신청취소", notes = "견적요청 상세정보 신청취소")
	@PutMapping("/{estmtId}")
	public MyEstmtDetailParam putEstmtDetailCancel(@PathVariable(value = "estmtId") String estmtId){
		return service.putEstmtDetailCancel(estmtId);
	}

	@ApiOperation(value = "파일 한개 다운로드 - contentType", notes = "attachmentId 로 파일을 contentType 에 맞게 해서 다운로드 한다.")
	@GetMapping("/file-dwld-contentType/{attachmentId}")
	public void downloadFile_contentType(HttpServletResponse response, @PathVariable String attachmentId) {
		service.downloadFile_contentType(response, attachmentId);
	}

	@ApiOperation(value = "파일 그룹 다운로드", notes = "attachmentGroupId 로 해서 해당 파일들을 전부 다운로드 한다.")
	@GetMapping("/file-dwld-groupfiles/{attachmentGroupId}")
	public void fileDownload_groupfiles(HttpServletResponse response, @PathVariable String attachmentGroupId) {
		service.fileDownload_groupfiles(response, attachmentGroupId);
	}

	@ApiOperation(value = "견적서 다운로드 정보 조회", notes = "견적서 다운로드 담당자 정보조회")
	@GetMapping("/dwldInfo/{estmtId}")
	public MyEstmtDwldInfoParam getEstmtAdminInfo(@PathVariable(value = "estmtId") String estmtId){
		return service.getEstmtAdminInfo(estmtId);
	}
}
