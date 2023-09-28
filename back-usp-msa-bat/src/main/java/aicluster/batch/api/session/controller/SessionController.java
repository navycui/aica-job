package aicluster.batch.api.session.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.batch.api.session.service.SessionService;

@RestController
@RequestMapping("/api/session")
public class SessionController {

	@Autowired
	private SessionService service;

	@DeleteMapping("")
	public void removeExpired() {
		service.removeExpired();
	}
}
