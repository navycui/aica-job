package aicluster.mvn.api.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.resource.dto.AlrsrcUserListParam;
import aicluster.mvn.api.resource.service.AlrsrcUserService;
import aicluster.mvn.common.dto.AlrsrcUserDto;
import aicluster.mvn.common.dto.AlrsrcUserListItemDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("api/resources/user")
public class AlrsrcUserController {

	@Autowired
	AlrsrcUserService service;

	/**
	 * 사용자 자원할당내역 목록조회
	 * @param param
	 * @param pageParam
	 * @return
	 */
	@GetMapping("")
	CorePagination<AlrsrcUserListItemDto> getList(AlrsrcUserListParam param, CorePaginationParam pageParam) {
		return service.getList(param, pageParam);
	}

	/**
	 * 사용자 자원할당내역 상세조회
	 * @param alrsrcId
	 * @return
	 */
	@GetMapping("/{alrsrcId}")
	AlrsrcUserDto get(@PathVariable String alrsrcId) {
		return service.get(alrsrcId);
	}
}
