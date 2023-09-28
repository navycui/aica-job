package aicluster.member.api.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.account.dto.AccountParam;
import aicluster.member.api.account.dto.AccountUnlockParam;
import aicluster.member.api.account.service.AccountService;

@RestController
@RequestMapping("api/account")
public class AccountController {

	@Autowired
	private AccountService service;

	/**
	 * 로그인 계정 휴면 해제
	 * @param param
	 */
	@PutMapping("/undomant")
	void undomant(@RequestBody AccountParam param) {
		service.undomant(param);
	}

	/**
	 * 로그인 계정 잠금 해제
	 * @param param
	 */
	@PutMapping("/unlock")
	void unlock(@RequestBody AccountUnlockParam param) {
		service.unlock(param);
	}
}
