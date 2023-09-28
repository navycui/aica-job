package aicluster.mvn.api.status.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.request.service.MvnExtService;
import aicluster.mvn.common.dto.MvnEtReqStCountDto;

@RestController
@RequestMapping("api/extension/status")
public class MvnExtStatusController {

	@Autowired
	private MvnExtService service;

	/**
	 * 입주연장신청 상태별 건수 조회
	 * @return
	 */
	@GetMapping("status-count")
	public MvnEtReqStCountDto getStatusCount() {
		return service.getStatusCount();
	}
}
