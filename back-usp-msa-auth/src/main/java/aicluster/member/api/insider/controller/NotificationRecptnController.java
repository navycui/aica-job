package aicluster.member.api.insider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.insider.dto.NotificationRecptnParam;
import aicluster.member.api.insider.service.NotificationRecptnService;
import aicluster.member.common.entity.CmmtSysCharger;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("api/insiders/notification-reception")
public class NotificationRecptnController {
	@Autowired
	private NotificationRecptnService service;

	/**
	 * 오류알림 수신담당자 목록 조회
	 * @return
	 */
	@GetMapping("")
	JsonList<CmmtSysCharger> getList() {
		return service.getList();
	}

	/**
	 * 오류알림 수신담당자 일괄 저장
	 * @param param
	 * @return
	 */
	@PostMapping("")
	JsonList<CmmtSysCharger> saveAll(@RequestBody NotificationRecptnParam param) {
		return service.insertList(param);
	}
}
