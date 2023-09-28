package aicluster.batch.api.mvn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.batch.api.mvn.service.MvnFcRsvtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/mvn")
public class MvnFcRsvtController {

	@Autowired
	private MvnFcRsvtService service;

	@PutMapping("/reserve-close")
	void rsvtClose() {
		int cnt = service.updateRsvtClose();
		log.info(String.format("시설예약 이용종료 처리: %d", cnt));
	}
}
