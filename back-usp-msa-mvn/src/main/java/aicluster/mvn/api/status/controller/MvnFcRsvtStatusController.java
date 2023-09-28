package aicluster.mvn.api.status.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.reservation.service.MvnFcRsvtService;
import aicluster.mvn.common.dto.MvnFcReserveStCountDto;

@RestController
@RequestMapping("api/reservation/status")
public class MvnFcRsvtStatusController {

	@Autowired
	private MvnFcRsvtService service;

	/**
	 * 시설예약 상태별 건수 조회
	 * @return
	 */
	@GetMapping("status-count")
	public MvnFcReserveStCountDto getStatusCount() {
		return service.getStatusCount();
	}
}
