package aicluster.member.api.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.member.api.member.dto.CertResultForJoinDto;
import aicluster.member.api.member.dto.JoinParam;
import aicluster.member.api.member.dto.PkiCertParam;
import aicluster.member.api.member.service.JoinService;
import aicluster.member.api.module.dto.NiceIdResultParam;
import aicluster.member.common.dto.MemberSelfDto;
import aicluster.member.common.dto.VerifyLoginIdResultDto;

@RestController
@RequestMapping("/api/join")
public class JoinController {

	@Autowired
	private JoinService service;

	/**
	 * 회원가입 이메일 인증요청
	 *
	 * @param email	이메일주소
	 * @return SessionKeyDto	이메일인증번호 저장한 Session ID
	 */
	@PostMapping("/email/cert-req")
	public SessionKeyDto emailCertReq(String email) {
		return service.emailCertReq(email);
	}

	/**
	 * 회원가입 이메일 인증확인
	 *
	 * @param emailCertKey	이메일인증번호 저장한 Session ID
	 * @param certNo	인증번호
	 */
	@PostMapping("/email/cert-check")
	public void emailCertCheck(String emailCertKey, String certNo) {
		service.emailCertCheck(emailCertKey, certNo);
	}

	/**
	 * 회원가입 휴대폰 인증번호 요청
	 *
	 * @param sessionId	회원가입 인증 Session ID
	 * @param mobileNo	휴대폰 번호
	 * @return SessionKeyDto	휴대폰 인증번호 저장한 Session ID
	 */
	@PostMapping("/phone/cert-req")
	public SessionKeyDto phoneCertReq(String sessionId, String mobileNo) {
		return service.phoneCertReq(sessionId, mobileNo);
	}

	/**
	 * 회원가입 휴대폰 인증번호 확인
	 *
	 * @param sessionId	회원가입 인증 Session ID
	 * @param phoneCertKey	휴대폰 인증번호 저장한 Session ID
	 * @param certNo	인증번호
	 */
	@PostMapping("/phone/cert-check")
	public void phoneCertCheck(String sessionId, String phoneCertKey, String certNo) {
		service.phoneCertCheck(sessionId, phoneCertKey, certNo);
	}

	/**
	 * 로그인 ID 중복 검증
	 *
	 * @param loginId	로그인ID
	 * @return 로그인ID중복검증 결과
	 */
	@PostMapping("/verify/login-id")
	public VerifyLoginIdResultDto verifyLoginId(String loginId) {
		return service.verifyLoginId(loginId);
	}

	/**
	 * 휴대폰 본인인증(nice) 결과을 기반으로 회원가입 여부 검증
	 *
	 * @param param	본인인증결과
	 * @return 회원가입여부 검증결과
	 */
	@PostMapping("/mobile")
	public CertResultForJoinDto mobile(@RequestBody NiceIdResultParam param) {
		return service.verifyMobileCert(param);
	}

	/**
	 * 공동인증서(MagicLine4Web) 인증결과를 기반으로 회원가입 여부 검증
	 *
	 * @param param	약관동의 및 공동인증서 인증결과 세션ID 모음
	 * @return	회원가입여부 검증결과
	 */
	@PostMapping("/certification")
	public CertResultForJoinDto pkiCert(@RequestBody PkiCertParam param) {
		return service.verifyPkiCert(param);
	}

	/**
	 * 회원가입
	 *
	 * @param param	회원가입정보
	 */
	@PostMapping("")
	public MemberSelfDto join(@RequestBody JoinParam param) {
		return service.join(param);
	}

	/**
	 * 탈퇴회원 정상전환
	 *
	 * @param sessionId	휴대폰 본인인증 또는 공동인증서 인증결과 Session ID
	 */
	@PostMapping("/unsecession")
	public void unsecession(String sessionId) {
		service.unsecession(sessionId);
	}
}
