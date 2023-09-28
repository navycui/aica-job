package aicluster.batch.api.pms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.batch.api.pms.service.PmsService;

@RestController
@RequestMapping("/api/pms")
public class PmsController {

	@Autowired
	private PmsService pmsService;

	@GetMapping("/ntce")
	public void updateNtce() {
		pmsService.updateNtce();
	}


	@GetMapping("/rcept-closing")
	public void updateRceptClosing() {
		pmsService.updateRceptClosing();
	}


	@GetMapping("/lms-category")
	public void updateLmsCategory() {
		pmsService.updateLmsCategory();
	}
}
