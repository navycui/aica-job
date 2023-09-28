package aicluster.member.api.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.member.api.account.service.AccountCertService;
import aicluster.member.api.member.dto.PkiCertParam;
import aicluster.member.api.module.dto.NiceIdResultParam;

@RestController
@RequestMapping("api/account-cert")
public class AccountCertController {

	@Autowired
	private AccountCertService service;

	/**
	 * 개인회원 계정 인증 처리
	 *
	 * @param param: 휴대폰 본인인증 결과 정보
	 * @return 계정 인증 결과정보 세션ID
	 */
	@PostMapping("/individual")
	SessionKeyDto certForIndividual(@RequestBody NiceIdResultParam param) {
		param.setSessionId(null);
		return service.certForIndividual(param);
	}

	/**
	 * 사업자회원 계정 인증 처리
	 *
	 * @param param: 공동인증서 인증 결과 정보
	 * @return 계정 인증 결과정보 세션ID
	 */
	@PostMapping("/bzmn")
	SessionKeyDto certForBzmn(@RequestBody PkiCertParam param) {
		param.setTermsConsentSessionId(null);
		return service.certForBzmn(param);
	}
}
