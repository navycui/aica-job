package aicluster.member.api.self.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.member.api.module.dto.NiceIdResultParam;
import aicluster.member.api.self.dto.ModifyPasswdParam;
import aicluster.member.api.self.dto.PasswdCheckRes;
import aicluster.member.api.self.dto.SelfModifyParam;
import aicluster.member.api.self.service.SelfService;
import aicluster.member.common.dto.MemberSelfDto;

@RestController
@RequestMapping("/api/self")
public class SelfController {

	@Autowired
	private SelfService service;

	/**
	 * 비밀번호 확인
	 *
	 * @param passwd
	 * @return
	 */
	@PostMapping("/passwd-check")
	public PasswdCheckRes passwdCheck(String passwd) {
		return service.passwdCheck(passwd);
	}

	/**
	 * 개인정보 변경을 위한 본인정보 조회
	 *
	 * @param passwdCheckKey
	 * @return
	 */
	@GetMapping("/{passwdCheckKey}")
	public MemberSelfDto getMeForChkPasswd(@PathVariable String passwdCheckKey) {
		return service.getMeForChkPasswd(passwdCheckKey);
	}

	/**
	 * 개인정보 수정
	 *
	 * @param passwdCheckKey
	 * @param param
	 * @return
	 */
	@PutMapping("/{passwdCheckKey}")
	public MemberSelfDto modify(@PathVariable String passwdCheckKey, @RequestBody SelfModifyParam param) {
		return service.modify(passwdCheckKey, param);
	}

	/**
	 * 담당자 휴대폰 인증요청
	 *
	 * @param passwdCheckKey
	 * @param mobileNo
	 * @return
	 */
	@PostMapping("/{passwdCheckKey}/phone-cert-req")
	public SessionKeyDto phoneCertReq(@PathVariable String passwdCheckKey, String mobileNo) {
		return service.phoneCertReq(passwdCheckKey, mobileNo);
	}

	/**
	 * 담당자 휴대폰 인증결과 확인
	 *
	 * @param passwdCheckKey
	 * @param phoneCertKey
	 * @param certNo
	 * @return
	 */
	@PostMapping("/{passwdCheckKey}/phone-cert-check")
	public void phoneCertCheck(@PathVariable String passwdCheckKey, String phoneCertKey, String certNo) {
		service.phoneCertCheck(passwdCheckKey, phoneCertKey, certNo);
	}

	/**
	 * 담당자 휴대폰번호 수정
	 *
	 * @param passwdCheckKey 비밀번호확인 Key
	 * @param phoneCertKey 휴대폰번호인증 Key
	 */
	@PutMapping("/{passwdCheckKey}/phone")
	public void modifyMobileNo(@PathVariable String passwdCheckKey, String phoneCertKey) {
		service.modifyMobileNo(passwdCheckKey, phoneCertKey);
	}

	/**
	 * 이메일 인증번호 발송요청
	 *
	 * @param passwdCheckKey 비밀번호확인 Key
	 * @param email 이메일
	 * @return 이메일인증 Key
	 */
	@PostMapping("/{passwdCheckKey}/email-cert-req")
	public SessionKeyDto emailCertReq(@PathVariable String passwdCheckKey, String email) {
		return service.emailCertReq(passwdCheckKey, email);
	}

	/**
	 * 이메일 인증번호 확인
	 *
	 * @param passwdCheckKey 비밀번호확인 Key
	 * @param emailCertKey 이메일인증 Key
	 * @param certNo 인증번호
	 */
	@PostMapping("/{passwdCheckKey}/email-cert-check")
	public void emailCertCheck(@PathVariable String passwdCheckKey, String emailCertKey, String certNo) {
		service.emailCertCheck(passwdCheckKey, emailCertKey, certNo);
	}

	/**
	 * 이메일 변경
	 *
	 * @param passwdCheckKey 비밀번호확인 Key
	 * @param emailCertKey 이메일인증 Key
	 */
	@PutMapping("/{passwdCheckKey}/email")
	public void modifyEmail(@PathVariable String passwdCheckKey, String emailCertKey) {
		service.modifyEmail(passwdCheckKey, emailCertKey);
	}

	/**
	 * 비밀번호 변경
	 *
	 * @param param
	 */
	@PutMapping("/passwd")
	public void modifyPasswd(@RequestBody ModifyPasswdParam param) {
		service.modifyPasswd(param);
	}

	/**
	 * 본인정보 조회
	 *
	 * @return
	 */
	@GetMapping("/me")
	public MemberSelfDto getMe() {
		return service.getMe();
	}

	/**
	 * 휴대폰 본인인증 후 휴대폰 번호 변경
	 *
	 * @param passwdCheckKey
	 * @param param
	 */
	@PutMapping("/{passwdCheckKey}/mobile-cert")
	public void modifyMoblieCert(@PathVariable String passwdCheckKey, @RequestBody NiceIdResultParam param) {
		param.setSessionId(passwdCheckKey);
		service.modifyMobileCert(param);
	}
}
