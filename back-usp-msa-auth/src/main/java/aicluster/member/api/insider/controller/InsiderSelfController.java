package aicluster.member.api.insider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.insider.dto.ChangePasswdParam;
import aicluster.member.api.insider.service.InsiderSelfService;

@RestController
@RequestMapping("/api/insiders/me")
public class InsiderSelfController {

	@Autowired
	private InsiderSelfService service;

	/**
	 * 내부사용자 비밀번호 변경
	 * @param param
	 */
	@PutMapping("/passwd")
	public void changePasswd(@RequestBody ChangePasswdParam param) {
		service.changePasswd(param);
	}
}
