package aicluster.mvn.api.status.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.request.service.MvnCheckoutService;
import aicluster.mvn.common.dto.CheckoutReqStCountDto;

@RestController
@RequestMapping("api/checkouts/status")
public class MvnCheckoutStatusController {

	@Autowired
	private MvnCheckoutService service;

	/**
	 * 퇴실신청 상태별 건수 조회
	 * @return
	 */
	@GetMapping("status-count")
	public CheckoutReqStCountDto getStatusCount() {
		return service.getStatusCount();
	}
}
