package aicluster.member.api.self.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.login.dto.KakaoLoginParam;
import aicluster.member.api.login.dto.SnsLoginParam;
import aicluster.member.api.self.dto.SnsConfigDto;
import aicluster.member.api.self.service.SnsService;

@RestController
@RequestMapping("/api/self/sns")
public class SnsController {

	@Autowired
	private SnsService service;

	@GetMapping("")
	public SnsConfigDto getConfig() {
		return service.getConfig();
	}

	/**
	 * 회원본인 구글 SNS 연동 설정
	 * @param param
	 */
	@PostMapping("/google")
	public void google(@RequestBody SnsLoginParam param) {
		service.google(param);
	}

	/**
	 * 회원본인 구글 SNS 연동 해제
	 */
	@DeleteMapping("/google")
	public void removeGoogle() {
		service.removeGoogle();
	}

	/**
	 * 회원본인 네이버 SNS 연동 설정
	 * @param param
	 */
	@PostMapping("/naver")
	public void naver(@RequestBody SnsLoginParam param) {
		service.naver(param);
	}

	/**
	 * 회원본인 네이버 SNS 연동 해제
	 */
	@DeleteMapping("/naver")
	public void removeNaver() {
		service.removeNaver();
	}

	/**
	 * 회원본인 카카오 SNS 연동 설정
	 * @param param
	 */
	@PostMapping("/kakao")
	public void kakao(@RequestBody KakaoLoginParam param) {
		service.kakao(param);
	}

	/**
	 * 회원본인 카카오 SNS 연동 해제
	 */
	@DeleteMapping("/kakao")
	public void removeKakao() {
		service.removeKakao();
	}
}
