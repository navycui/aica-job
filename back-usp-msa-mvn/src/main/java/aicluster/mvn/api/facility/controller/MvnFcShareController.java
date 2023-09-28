package aicluster.mvn.api.facility.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.facility.service.MvnFcService;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/share-facilities")
public class MvnFcShareController {

	@Autowired
	private MvnFcService service;

	/**
	 * 공유실에 해당하는 입주시설 중 사용가능한 입주시설 목록조회
	 *
	 * @param mvnFcDtype: 입주시설 상세유형
	 * @param page
	 * @param itemsPerPage
	 * @return CorePagination<UsptMvnFc>
	 */
	@GetMapping("")
	CorePagination<UsptMvnFcltyInfo> getEnableList(String mvnFcDtype, CorePaginationParam pageParam)
	{
		return service.getEnableShareList( mvnFcDtype, pageParam );
	}
}
