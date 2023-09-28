package aicluster.batch.api.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.batch.api.notification.service.NotificationService;

@RestController
@RequestMapping("/api/noti")
public class NotificationController {

	@Autowired
	private NotificationService service;

	@GetMapping("/error")
	public void sendErrorEmail() {
		service.notifyError();
	}
}
