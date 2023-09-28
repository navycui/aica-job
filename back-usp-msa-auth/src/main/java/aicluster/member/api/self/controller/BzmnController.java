package aicluster.member.api.self.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.member.api.member.dto.PkiCertParam;
import aicluster.member.api.self.dto.ChangeParam;
import aicluster.member.api.self.service.BzmnService;

@RestController
@RequestMapping("/api/self/bzmn")
public class BzmnController {

	@Autowired
	private BzmnService service;

	/**
	 * 사업자전환 - 공동인증서 인증
	 *
	 * @param param	약관동의 및 공동인증서 인증결과 세션ID 모음
	 * @return 공동인증서 인증결과 신규저장 세션ID
	 */
	@PostMapping("/bizrno-cert")
	public SessionKeyDto bizrnoCert(@RequestBody PkiCertParam param) {
		return service.bizrnoCert(param);
	}

	/**
	 * 휴대폰 인증 요청
	 *
	 * @param sessionId
	 * @param mobileNo
	 * @return
	 */
	@PostMapping("/phone-cert-req")
	public SessionKeyDto phoneCertReq(String sessionId, String mobileNo) {
		return service.phoneCertReq(sessionId, mobileNo);
	}

	/**
	 * 휴대폰 인증번호 확인
	 *
	 * @param sessionId
	 * @param certNo
	 * @return 없음
	 */
	@PostMapping("/phone-cert-check")
	public void phoneCertCheck(String sessionId, String certNo) {
		service.phoneCertCheck(sessionId, certNo);
	}

	/**
	 * 이메일 인증 요청
	 *
	 * @param sessionId
	 * @param email
	 * @return emailCertKey
	 */
	@PostMapping("/email-cert-req")
	public SessionKeyDto emailCertReq(String sessionId, String email) {
		return service.emailCertReq( sessionId, email );
	}

	/**
	 * 이메일 인증번호 확인
	 *
	 * @param sessionId
	 * @param certNo
	 * @return
	 */
	@PostMapping("/email-cert-check")
	public void emailCertCheck(String sessionId, String certNo) {
		service.emailCertCheck(sessionId, certNo);
	}

	/**
	 * 사업자전환
	 *
	 * @param param	사업자전환 입력정보
	 */
	@PostMapping("/change")
	public void change(@RequestBody ChangeParam param) {
		service.change(param);
	}
}
