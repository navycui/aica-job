package aicluster.mvn.api.status.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.status.dto.MvnStatusListParam;
import aicluster.mvn.api.status.service.MvnStatusService;
import aicluster.mvn.common.dto.MvnFcStatsListItemDto;
import aicluster.mvn.common.dto.UserMvnCmpnyDto;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/status")
public class MvnStatusController {
	@Autowired
	private MvnStatusService service;

	/**
	 * 입주현황 조회
	 *
	 * @param param: 검색조건
	 * @param pageParam: 페이징
	 * @return 입주업체정보 목록
	 */
	@GetMapping("/movin-list")
	CorePagination<MvnFcStatsListItemDto> getList(MvnStatusListParam param, CorePaginationParam pageParam) {
		return service.getList(param, pageParam);
	}

	/**
	 * 사용자 입주현황 상세조회
	 *
	 * @param allInqireYn: 전제조회여부(true:모든기간 입주정보 중 최근 입주정보, false:현재 입주중에 해당하는 입주정보)
	 * @return 로그인 사용자의 입주정보
	 */
	@GetMapping("/movin-user")
	UserMvnCmpnyDto getUser(Boolean allInqireYn) {
		return service.getUser(allInqireYn);
	}
}