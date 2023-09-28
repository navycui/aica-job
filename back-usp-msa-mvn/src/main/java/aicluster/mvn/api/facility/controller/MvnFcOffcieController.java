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
@RequestMapping("/api/office-facilities")
public class MvnFcOffcieController {

 	@Autowired
	private MvnFcService service;

 	/**
 	 * 사무실에 해당하는 입주시설 중 사용가능한 입주시설 목록조회
 	 *
 	 * @param bnoRoomNo: 건물 동호수
 	 * @param mvnFcCapacity: 입주시설 수용인연
 	 * @param mvnFcar: 입주시설 면적
 	 * @param page
 	 * @param itemsPerPage
 	 * @return CorePagination<UsptMvnFc>
 	 */
	@GetMapping("")
	CorePagination<UsptMvnFcltyInfo> getEnableList(String bnoRoomNo, Long mvnFcCapacity, String mvnFcar, CorePaginationParam pageParam)
	{
		return service.getEnableOfficeList( bnoRoomNo, mvnFcCapacity, mvnFcar, pageParam );
	}
}
