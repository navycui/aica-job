package aicluster.mvn.api.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.reservation.dto.MvnFcRsvtDto;
import aicluster.mvn.api.reservation.dto.MvnFcRsvtListParam;
import aicluster.mvn.api.reservation.dto.MvnFcRsvtModifyStateParam;
import aicluster.mvn.api.reservation.dto.UserRsvtListParam;
import aicluster.mvn.api.reservation.service.MvnFcRsvtService;
import aicluster.mvn.common.dto.MvnFcCutoffTimeDto;
import aicluster.mvn.common.dto.MvnFcRsvtListItemDto;
import aicluster.mvn.common.entity.UsptMvnFcltyResve;
import aicluster.mvn.common.entity.UsptMvnFcltyResveHist;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationParam;

@RestController
@RequestMapping("/api/reservation")
public class MvnFcRsvtController {

	@Autowired
	private MvnFcRsvtService service;

	/**
	 * 시설예약 목록조회
	 *
	 * @param param
	 * @param page
	 * @param itemsPerPage
	 * @return
	 */
	@GetMapping("/spaces")
	public CorePagination<MvnFcRsvtListItemDto> getRsvtSpaceList(MvnFcRsvtListParam param, CorePaginationParam pageParam) {
		return service.getRsvtSpaceList(param, pageParam);
	}

	/**
	 * 시설예약 상세조회
	 *
	 * @param reserveId
	 * @return
	 */
	@GetMapping("/spaces/{reserveId}")
	public MvnFcRsvtDto getRsvtSpace(@PathVariable String reserveId) {
		return service.getRsvtSpace(reserveId);
	}

	/**
	 * 시설예약 상태변경
	 *
	 * @param reserveId
	 * @param param
	 * @return
	 */
	@PutMapping("/spaces/{reserveId}/update-state")
	public UsptMvnFcltyResve modifyState(@PathVariable String reserveId, @RequestBody MvnFcRsvtModifyStateParam param) {
		return service.modifyState(reserveId, param);
	}

	/**
	 * 입주시설에 대한 일자별 예약불가시간대 조회
	 *
	 * @param mvnFcId: 입주시설ID
	 * @param ymd: 일자
	 * @return MvnFcCutoffTimeDto
	 */
	@GetMapping("/cutoff-time/{mvnFcId}")
	private MvnFcCutoffTimeDto getFacCutoffTimeList(@PathVariable String mvnFcId, String ymd) {
		return service.getFacCutoffTimeList(mvnFcId, ymd);
	}

	/**
	 * 사용자 시설예약 목록조회
	 * @param param
	 * @return
	 */
	@GetMapping("/user")
	public CorePagination<MvnFcRsvtListItemDto> getUserRsvtList(UserRsvtListParam param, CorePaginationParam pageParam) {
		return service.getUserRsvtList(param, pageParam);
	}

	/**
	 * 사용자 시설예약 상세조회
	 *
	 * @param reserveId
	 * @return
	 */
	@GetMapping("/user/{reserveId}")
	public MvnFcRsvtDto getUserRsvt(@PathVariable String reserveId) {
		return service.getUserRsvt(reserveId);
	}

	/**
	 * 처리이력 조회
	 *
	 * @param reserveId
	 * @return
	 */
	@GetMapping("/spaces/{reserveId}/hist")
	public JsonList<UsptMvnFcltyResveHist> getReserveHistList(@PathVariable String reserveId) {
		return service.getReserveHistList(reserveId);
	}
}
