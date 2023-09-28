package aicluster.member.api.help.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.help.dto.BzmnPasswdParam;
import aicluster.member.api.help.dto.CertCheckParam;
import aicluster.member.api.help.dto.ChangePasswdParam;
import aicluster.member.api.help.dto.EmailCertReqParam;
import aicluster.member.api.help.dto.FindBzmnIdParam;
import aicluster.member.api.help.dto.FindIndividualIdParam;
import aicluster.member.api.help.dto.IndividualPasswdParam;
import aicluster.member.api.help.dto.LoginIdDto;
import aicluster.member.api.help.dto.PasswdDto;
import aicluster.member.api.help.dto.PhoneCertReqParam;
import aicluster.member.api.help.service.FindService;

@RestController
@RequestMapping("/api/help/find")
public class FindController {

	@Autowired
	private FindService service;

	/**
	 * 개인회원 로그인 아이디 찾기
	 *
	 * @param param Json
	 * @return
	 */
	@PostMapping("/individual-id")
	public LoginIdDto findIndividualId(@RequestBody FindIndividualIdParam param) {
		return service.findIndividualLoginId( param );
	}

	/**
	 * 사업자 로그인 아이디 찾기
	 *
	 * @param param
	 * @return
	 */
	@PostMapping("/bzmn-id")
	public LoginIdDto findBzmnId(@RequestBody FindBzmnIdParam param) {
		return service.findBzmnLoginId( param );
	}

	/**
	 * 개인회원 비밀번호 찾기 1단계
	 *
	 * @param param
	 * @return
	 */
	@PostMapping("/individual-passwd")
	public PasswdDto individualPasswd(@RequestBody IndividualPasswdParam param) {
		return service.individualPasswd(param);
	}

	/**
	 * 사업자회원 비밀번호 찾기 1단계
	 *
	 * @param param
	 * @return
	 */
	@PostMapping("/bzmn-passwd")
	public PasswdDto bzmnPasswd(@RequestBody BzmnPasswdParam param) {
		return service.bzmnPasswd( param );
	}

	/**
	 * 비밀번호 찾기용 휴대폰 인증번호 발송
	 *
	 * @param param 이 key값을 "/phone-cert-check"의 파라미터로 사용한다.
	 * @return
	 */
	@PostMapping("/phone-cert-req")
	public void phoneCertReq(@RequestBody PhoneCertReqParam param) {
		service.phoneCertReq( param );
	}

	/**
	 * 인증번호 확인
	 *
	 * @param param
	 * @return
	 */
	@PostMapping("/phone-cert-check")
	public void phoneCertCheck(@RequestBody CertCheckParam param) {
		service.certCheck( param );
	}

	/**
	 * 비밀번호 찾기 이메일 인증 요청
	 * @param param
	 */
	@PostMapping("/email-cert-req")
	public void emailCertReq(@RequestBody EmailCertReqParam param) {
		service.emailCertReq( param );
	}

	/**
	 * 비밀번호 찾기 이메일 인증 확인
	 * @param param
	 */
	@PostMapping("/email-cert-check")
	public void emailCertCheck(@RequestBody CertCheckParam param) {
		service.certCheck( param );
	}

	/**
	 * 비밀번호 변경
	 *
	 * @param param
	 */
	@PostMapping("/change-passwd")
	public void changePasswd(@RequestBody ChangePasswdParam param) {
		service.changePasswd( param );
	}
}
