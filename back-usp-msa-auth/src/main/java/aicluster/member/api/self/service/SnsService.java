package aicluster.member.api.self.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.login.config.GoogleConfig;
import aicluster.member.api.login.dto.GoogleLoginResponse;
import aicluster.member.api.login.dto.KakaoLoginParam;
import aicluster.member.api.login.dto.KakaoLoginResponse;
import aicluster.member.api.login.dto.NaverLoginResponse;
import aicluster.member.api.login.dto.SnsLoginParam;
import aicluster.member.api.login.service.LoginService;
import aicluster.member.api.self.dto.SnsConfigDto;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.util.CodeExt;
import bnet.library.exception.CommunicationException;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SnsService {

	@Autowired
	private CmmtMberInfoDao cmmtMemberDao;
	@Autowired
	private LoginService loginService;
	@Autowired
	private GoogleConfig googleConfig;


	public SnsConfigDto getConfig() {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		boolean google = string.isNotBlank(cmmtMember.getGoogleToken());
		boolean naver = string.isNotBlank(cmmtMember.getNaverToken());
		boolean kakao = string.isNotBlank(cmmtMember.getKakaoToken());

		SnsConfigDto dto = SnsConfigDto.builder()
				.google(google)
				.naver(naver)
				.kakao(kakao)
				.build();

		return dto;
	}

	public void google(SnsLoginParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		if (param == null || string.isBlank(param.getAccessToken())) {
			throw new InvalidationException("입력값이 올바르지 않습니다.");
		}

		/*
		 * Access token으로 Google 사용자 정보 조회
		 */
		Unirest.config().reset();
		Unirest.config().socketTimeout(1000 * 60 * 3);

		String url = CodeExt.snsUrl.GOOGLE_ME_URL;
		HttpResponse<GoogleLoginResponse> res = Unirest.get(url)
				.queryString("personFields", "birthdays")
				.queryString("key", googleConfig.getAppKey())
				.queryString("access_token", param.getAccessToken())
				.asObject(GoogleLoginResponse.class);

		if (res.getStatus() != 200) {
			GoogleLoginResponse gr = res.getBody();
			log.error(gr.toString());
			String msg = String.format("\"Google과 통신 중에 오류가 발생하여 작업을 중단하였습니다.(code=%d, message=%s)", gr.getCode(), gr.getMessage());
			throw new CommunicationException(msg);
		}

		GoogleLoginResponse gr = res.getBody();
		String googleId = gr.getPersonId();

		/*
		 * Goolgle Token 저장
		 */

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		CmmtMberInfo googleUser = cmmtMemberDao.selectByGoogleToken(googleId);
		if (googleUser != null) {
			if (!string.equals(cmmtMember.getMemberId(), googleUser.getMemberId())) {
				throw new InvalidationException("다른 계정에 연동되어 있어 사용할 수 없습니다.");
			}
		}

        Date now = new Date();
        cmmtMember.setGoogleToken(googleId);
        cmmtMember.setUpdatedDt(now);
        cmmtMember.setUpdaterId(worker.getMemberId());
        cmmtMemberDao.update(cmmtMember);
	}

	public void removeGoogle() {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		Date now = new Date();
		cmmtMember.setGoogleToken(null);
		cmmtMember.setUpdatedDt(now);
		cmmtMember.setUpdaterId(worker.getMemberId());
		cmmtMemberDao.update(cmmtMember);
	}

	public void naver(SnsLoginParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		if (param == null || string.isBlank(param.getAccessToken())) {
			throw new InvalidationException("입력값이 올바르지 않습니다.");
		}

		/*
		 * Access token으로 NAVER 사용자 정보 조회
		 */
		Unirest.config().reset();
		Unirest.config().socketTimeout(1000 * 60 * 3);

		String header = "Bearer " + param.getAccessToken();
		String url = CodeExt.snsUrl.NAVER_ME_URL;
		HttpResponse<NaverLoginResponse> res = Unirest.get(url)
				.header("Authorization", header)
				.asObject(NaverLoginResponse.class);

		if (res.getStatus() != 200) {
			log.error(res.getBody().toString());
			NaverLoginResponse nres = res.getBody();
			String msg = String.format("\"Naver와 통신 중에 오류가 발생하여 작업을 중단하였습니다.(code=%s, message=%s)", nres.getResultcode(), nres.getMessage());
			throw new CommunicationException(msg);
		}

		NaverLoginResponse nres = res.getBody();
		if (!string.equals(nres.getResultcode(), "00")) {
			String msg = String.format("\"Naver와 통신 중에 오류가 발생하여 작업을 중단하였습니다.(code=%s, message=%s)", nres.getResultcode(), nres.getMessage());
			throw new CommunicationException(msg);
		}

		String naverId = nres.getResponse().getId();

		/*
		 * Naver token 저장
		 */
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		CmmtMberInfo naverUser = cmmtMemberDao.selectByNaverToken(naverId);
		if (naverUser != null) {
			if (!string.equals(cmmtMember.getMemberId(), naverUser.getMemberId())) {
				throw new InvalidationException("다른 계정에 연동되어 있어 사용할 수 없습니다.");
			}
		}

        Date now = new Date();
        cmmtMember.setNaverToken(naverId);
        cmmtMember.setUpdatedDt(now);
        cmmtMember.setUpdaterId(worker.getMemberId());
        cmmtMemberDao.update(cmmtMember);
	}

	public void removeNaver() {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		Date now = new Date();
		cmmtMember.setNaverToken(null);
		cmmtMember.setUpdatedDt(now);
		cmmtMember.setUpdaterId(worker.getMemberId());
		cmmtMemberDao.update(cmmtMember);
	}

	public void kakao(KakaoLoginParam param) {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		if (param == null) {
			throw new InvalidationException("입력값이 올바르지 않습니다.");
		}

		boolean blankAccessToken = string.isBlank(param.getAccessToken());
		boolean blankCode = string.isBlank(param.getCode());
		boolean blankUri = string.isBlank(param.getUri());
		if (blankAccessToken && (blankCode || blankUri)) {
			throw new InvalidationException("입력값이 올바르지 않습니다.");
		}

		/*
		 * Kakao 사용자 정보 얻기
		 */
		KakaoLoginResponse nres = null;
		if (string.isNotBlank(param.getAccessToken())) {
			nres = loginService.getKakaoMemberByAccessToken(param.getAccessToken());
		}
		else {
			nres = loginService.getKakaoMemberByCode(param.getCode(), param.getUri());
		}

		String kakaoId = nres.getId();

		/*
		 * Kakao token 저장
		 */
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		CmmtMberInfo kakaoUser = cmmtMemberDao.selectByKakaoToken(kakaoId);
		if (kakaoUser != null) {
			if (!string.equals(cmmtMember.getMemberId(), kakaoUser.getMemberId())) {
				throw new InvalidationException("다른 계정에 연동되어 있어 사용할 수 없습니다.");
			}
		}

        Date now = new Date();
        cmmtMember.setKakaoToken(kakaoId);
        cmmtMember.setUpdatedDt(now);
        cmmtMember.setUpdaterId(worker.getMemberId());
        cmmtMemberDao.update(cmmtMember);
	}

	public void removeKakao() {
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		Date now = new Date();
		cmmtMember.setKakaoToken(null);
		cmmtMember.setUpdatedDt(now);
		cmmtMember.setUpdaterId(worker.getMemberId());
		cmmtMemberDao.update(cmmtMember);
	}

}
