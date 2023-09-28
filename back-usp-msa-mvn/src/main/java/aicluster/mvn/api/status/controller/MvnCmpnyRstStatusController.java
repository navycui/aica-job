package aicluster.mvn.api.status.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.mvn.api.company.service.MvnCmpnyRstService;
import aicluster.mvn.common.dto.MvnCmpnyRsltSttusCdCountDto;

@RestController
@RequestMapping("api/company-result/status")
public class MvnCmpnyRstStatusController {

	@Autowired
	private MvnCmpnyRstService service;

	/**
	 * 입주업체성과 상태별 건수 조회
	 * @return
	 */
	@GetMapping("status-count")
	public MvnCmpnyRsltSttusCdCountDto getStatusCount() {
		return service.getStatusCount();
	}

}
