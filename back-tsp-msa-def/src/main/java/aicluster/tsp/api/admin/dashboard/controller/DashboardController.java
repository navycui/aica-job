package aicluster.tsp.api.admin.dashboard.controller;

import aicluster.tsp.api.admin.dashboard.service.DashboardService;
import aicluster.tsp.common.dto.DashboardDto;
import aicluster.tsp.common.entity.CmmtBoardArticle;
import bnet.library.util.pagination.CorePagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@Api(tags="대시보드")
public class DashboardController {
	/*
	* 장비 분류
	* */

	@Autowired
	private DashboardService service;

	/**
	 * 조회 : 클릭 후 나오는 카테고리 리스트
	 */

	@ApiOperation(value = "대쉬보드 리스트 조회",
			notes = "" +
					"대시보드 리스트를 조회<br/>" +
					"page : long - 페이지수<br/>" +
					"itemsPerPage : long - 한페이지당 나오는 개수")
	@GetMapping("/list")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "페이지", required = true, dataType = "Long", paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "itemsPerPage", value = "한 페이지에 보여질 개수", required = true, dataType = "Long", paramType = "")
	})
	public CorePagination<CmmtBoardArticle> getList(Long page, Long itemsPerPage){
		return service.selectDashboardList(page, itemsPerPage);
	}

	/**
	 * 조회 : 클릭 후 나오는 카테고리 리스트
	 */
	@GetMapping("/count")
	public DashboardDto getCount(){
		return service.selectCount();
	}
}
