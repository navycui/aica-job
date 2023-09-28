package aicluster.pms.api.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.pms.api.dashboard.service.DashboardService;
import aicluster.pms.common.dto.DashboardDto;

/**
 * 대시보드 조회
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	/**
	 * 대시보드 조회
	 * @return
	 */
	@GetMapping("")
	public DashboardDto getList() {
		return dashboardService.get();
	}

}
