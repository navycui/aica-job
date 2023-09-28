package aicluster.mvn.api.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.resource.dto.AlrsrcStatusParam;
import aicluster.mvn.api.resource.service.AlrsrcStatusService;
import aicluster.mvn.common.dto.AlrsrcStatusListItemDto;
import aicluster.mvn.common.dto.AlrsrcStatusSmmryDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("api/resources")
public class AlrsrcStatusController {

	@Autowired
	private AlrsrcStatusService service;

	/**
	 * 자원할당현황 요약정보조회
	 * @param param
	 * @return
	 */
	@GetMapping("/summary")
	AlrsrcStatusSmmryDto getSummary(AlrsrcStatusParam param) {
		return service.getSummary(param);
	}

	/**
	 * 자원할당현황 목록조회
	 * @param param
	 * @param pageParam
	 * @return
	 */
	@GetMapping("/situation")
	CorePagination<AlrsrcStatusListItemDto> getList(AlrsrcStatusParam param, CorePaginationParam pageParam) {
		return service.getList(param, pageParam);
	}
}
