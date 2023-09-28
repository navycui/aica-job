package aicluster.batch.api.mvn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.batch.api.mvn.service.MvnCheckoutService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/mvn")
public class MvnCheckoutController {

	@Autowired
	private MvnCheckoutService service;

	@PutMapping("/checkout")
	void checkout() {
		int cnt = service.updateCheckout();
		log.info(String.format("입주업체 퇴실처리: %d", cnt));
	}
}
