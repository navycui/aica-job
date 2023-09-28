package aicluster.member.api.login.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.common.entity.TokenDto;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.JwtUtils;
import aicluster.member.api.login.dto.EvlLoginParam;
import aicluster.member.api.login.dto.KakaoLoginParam;
import aicluster.member.api.login.dto.LoginParam;
import aicluster.member.api.login.dto.LoginResult;
import aicluster.member.api.login.dto.SnsLoginParam;
import aicluster.member.api.login.dto.SsoParam;
import aicluster.member.api.login.service.LoginService;
import aicluster.member.common.dao.CmmtEmpInfoDao;
import aicluster.member.common.dao.CmmtLoginAtptDao;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.entity.CmmtEmpInfo;
import aicluster.member.common.entity.CmmtLoginAtpt;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.util.CodeExt;
import aicluster.member.config.bruteforce.service.LoginAttemptService;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;

//@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {
	private static final int MAX_LOGIN_FAIL_CNT = 5;

	@Autowired
	private LoginService loginService;
    @Autowired
    private EnvConfig envConfig;
    @Autowired
    private CmmtLoginAtptDao cmmtLoginTryDao;
    @Autowired
    private CmmtMberInfoDao cmmtMemberDao;
    @Autowired
    private CmmtEmpInfoDao cmmtInsiderDao;
    @Autowired
    private LoginAttemptService loginAttemptService;

    @PostMapping("/sso")
	public LoginResult ssoLogin(@RequestBody SsoParam param, HttpServletRequest request, HttpServletResponse response) {
    	// 접속자 IP
    	String userIp = CoreUtils.webutils.getRemoteIp(request);

    	// 로그인 처리
    	LoginResult result = null;
    	try {
    		result = loginService.login_sso(param, userIp);
    	} catch (AuthenticationException ex) {
    		/*
    		 * 비밀번호 연속 오류 처리
    		 */
    		CmmtMberInfo cmmtMember = cmmtMemberDao.selectByLoginId(param.getLoginId());
    		if (cmmtMember != null) {
    			cmmtLoginTryDao.save(cmmtMember.getMemberId(), userIp);
    			CmmtLoginAtpt clt = cmmtLoginTryDao.select(cmmtMember.getMemberId(), userIp);
    			if (clt != null && clt.getTryCnt() >= MAX_LOGIN_FAIL_CNT) {
    				Date now = new Date();
    				cmmtMember.setMemberSt(CodeExt.memberSt.잠김);
    				cmmtMember.setMemberStDt(now);
    				cmmtMemberDao.update(cmmtMember);
    				throw new InvalidationException(String.format("비밀번호 %d회 연속 실패로 계정이 잠겼습니다.", MAX_LOGIN_FAIL_CNT));
    			}
    		}
    		throw ex;
    	}

        /*
         * Cookie 설정
         */
		Cookie cookie = new Cookie(JwtUtils.cookieRefreshToken.회원용, result.getRefreshToken());
    	cookie.setMaxAge(envConfig.getCookie().getMaxAge());
    	cookie.setSecure(envConfig.getCookie().getSecure());
    	cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
    	cookie.setDomain(envConfig.getCookie().getDomain());
    	cookie.setPath(envConfig.getCookie().getPath());
    	response.addCookie(cookie);

		response.setHeader("Set-AccessToken", result.getAccessToken());
		response.setHeader("Set-RefreshTokenExpiresIn", "" + result.getRefreshTokenExpiresIn());

		return result;
	}

    /**
     * 회원 로그인
     * @param param
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/member")
	public LoginResult memberLogin(@RequestBody LoginParam param, HttpServletRequest request, HttpServletResponse response) {
    	// 접속자 IP
    	String userIp = CoreUtils.webutils.getRemoteIp(request);

    	// 로그인 처리
    	LoginResult result = null;
    	try {
    		// 현재 login lock 걸린 상태라면,...
    		if (loginAttemptService.isLocked()) {
    			Date now = new Date();
    			Date unlockDt = loginAttemptService.getUnlockDt();
    			long min = date.getDiffMinutes(now, unlockDt);
    			int maxCnt = LoginAttemptService.MAX_ATTEMPT;
    			throw new InvalidationException(String.format("로그인 연속 %d회 실패하여, %d분후까지 로그인을 할 수 없습니다.", maxCnt, min));
    		}

    		// 로그인 시도
    		result = loginService.login_member(param, userIp);

    		// 로그인 성공처리
    		loginAttemptService.loginSucceeded();
    	} catch (AuthenticationException ex) {
    		/*
    		 * 비밀번호 연속 오류 처리
    		 */
//    		CmmtMberInfo cmmtMember = cmmtMemberDao.selectByLoginId(param.getLoginId());
//    		if (cmmtMember != null) {
//    			cmmtLoginTryDao.save(cmmtMember.getMemberId(), userIp);
//    			CmmtLoginAtpt clt = cmmtLoginTryDao.select(cmmtMember.getMemberId(), userIp);
//    			if (clt != null && clt.getTryCnt() >= MAX_LOGIN_FAIL_CNT) {
//    				Date now = new Date();
//    				cmmtMember.setMemberSt(CodeExt.memberSt.잠김);
//    				cmmtMember.setMemberStDt(now);
//    				cmmtMemberDao.update(cmmtMember);
//    				throw new InvalidationException(String.format("비밀번호 %d회 연속 실패로 계정이 잠겼습니다.", MAX_LOGIN_FAIL_CNT));
//    			}
//    		}
//    		throw ex;

    		// 로그인 실패 처리
    		loginAttemptService.loginFailed();
    		// 현재 로그인 시도 회수
    		int tryCnt = loginAttemptService.getTryCnt();
    		int maxCnt = LoginAttemptService.MAX_ATTEMPT;
    		String errorMsg = (maxCnt - tryCnt) > 0 ? String.format("아이디 또는 비밀번호가 올바르지 않습니다.(%d회 남음)", maxCnt - tryCnt) : "비밀번호 5회 연속 실패로 계정이 잠겼습니다.\n30분 후에 다시 시도해 주세요.";
    		throw new InvalidationException(errorMsg);
    	}

        /*
         * Cookie 설정
         */
		Cookie cookie = new Cookie(JwtUtils.cookieRefreshToken.회원용, result.getRefreshToken());
    	cookie.setMaxAge(envConfig.getCookie().getMaxAge());
    	cookie.setSecure(envConfig.getCookie().getSecure());
    	cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
    	cookie.setDomain(envConfig.getCookie().getDomain());
    	cookie.setPath(envConfig.getCookie().getPath());
    	response.addCookie(cookie);

		response.setHeader("Set-AccessToken", result.getAccessToken());
		response.setHeader("Set-RefreshTokenExpiresIn", "" + result.getRefreshTokenExpiresIn());

		return result;
	}

	/**
	 * 내부사용자 로그인
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/insider")
	public LoginResult insiderLogin(@RequestBody LoginParam param, HttpServletRequest request, HttpServletResponse response) {
		String userIp = CoreUtils.webutils.getRemoteIp(request);

    	LoginResult result = null;
    	try {
    		// 현재 login lock 걸린 상태라면,...
    		if (loginAttemptService.isLocked()) {
    			Date now = new Date();
    			Date unlockDt = loginAttemptService.getUnlockDt();
    			long min = date.getDiffMinutes(now, unlockDt);
    			int maxCnt = LoginAttemptService.MAX_ATTEMPT;
    			throw new InvalidationException(String.format("로그인 연속 %d회 실패하여, %d분후까지 로그인을 할 수 없습니다.", maxCnt, min));
    		}

    		// 로그인 시도
    		result = loginService.login_insider(param, userIp);

    		// 로그인 성공처리
    		loginAttemptService.loginSucceeded();
    	} catch (AuthenticationException ex) {
    		/*
    		 * 비밀번호 연속 오류 처리
    		 */
//    		CmmtEmpInfo cmmtInsider = cmmtInsiderDao.selectByLoginId(param.getLoginId());
//    		if (cmmtInsider != null) {
//    			cmmtLoginTryDao.save(cmmtInsider.getMemberId(), userIp);
//    			CmmtLoginAtpt clt = cmmtLoginTryDao.select(cmmtInsider.getMemberId(), userIp);
//    			if (clt != null && clt.getTryCnt() >= MAX_LOGIN_FAIL_CNT) {
//    				Date now = new Date();
//    				cmmtInsider.setMemberSt(CodeExt.memberSt.잠김);
//    				cmmtInsider.setMemberStDt(now);
//    				cmmtInsiderDao.update(cmmtInsider);
//    				throw new InvalidationException(String.format("비밀번호 %d회 연속 실패로 계정이 잠겼습니다.", MAX_LOGIN_FAIL_CNT));
//    			}
//    		}
//    		throw ex;
    		
    		// 로그인 실패 처리
    		loginAttemptService.loginFailed();
    		// 현재 로그인 시도 회수
    		int tryCnt = loginAttemptService.getTryCnt();
    		int maxCnt = LoginAttemptService.MAX_ATTEMPT;
    		String errorMsg = (maxCnt - tryCnt) > 0 ? String.format("아이디 또는 비밀번호가 올바르지 않습니다.(%d회 남음)", maxCnt - tryCnt) : "비밀번호 5회 연속 실패로 계정이 잠겼습니다.\n30분 후에 다시 시도해 주세요.";
    		throw new InvalidationException(errorMsg);
    	}

		Cookie cookie = new Cookie(JwtUtils.cookieRefreshToken.내부사용자용, result.getRefreshToken());
    	cookie.setMaxAge(envConfig.getCookie().getMaxAge());
    	cookie.setSecure(envConfig.getCookie().getSecure());
    	cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
    	cookie.setDomain(envConfig.getCookie().getDomain());
    	cookie.setPath(envConfig.getCookie().getPath());
    	response.addCookie(cookie);

		response.setHeader("Set-AccessToken", result.getAccessToken());
		response.setHeader("Set-RefreshTokenExpiresIn", "" + result.getRefreshTokenExpiresIn());

        return result;
	}

	/**
	 * 평가위원 로그인
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/evaluator")
	public LoginResult evaluatorLogin(@RequestBody EvlLoginParam param, HttpServletRequest request, HttpServletResponse response) {
    	String userIp = CoreUtils.webutils.getRemoteIp(request);
    	LoginResult result = loginService.login_evaluator(param, userIp);

		Cookie cookie = new Cookie(JwtUtils.cookieRefreshToken.회원용, result.getRefreshToken());
    	cookie.setMaxAge(envConfig.getCookie().getMaxAge());
    	cookie.setSecure(envConfig.getCookie().getSecure());
    	cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
    	cookie.setDomain(envConfig.getCookie().getDomain());
    	cookie.setPath(envConfig.getCookie().getPath());
    	response.addCookie(cookie);

		response.setHeader("Set-AccessToken", result.getAccessToken());
		response.setHeader("Set-RefreshTokenExpiresIn", "" + result.getRefreshTokenExpiresIn());

        return result;
	}

	/**
	 * 네이버 로그인
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/sns/naver")
	public LoginResult naverLogin(@RequestBody SnsLoginParam param, HttpServletRequest request, HttpServletResponse response) {
    	// 접속자 IP
    	String userIp = CoreUtils.webutils.getRemoteIp(request);

    	// 로그인 처리
    	LoginResult result = loginService.login_memberNaver(param, userIp);

        /*
         * Cookie 설정
         */
		Cookie cookie = new Cookie(JwtUtils.cookieRefreshToken.회원용, result.getRefreshToken());
    	cookie.setMaxAge(envConfig.getCookie().getMaxAge());
    	cookie.setSecure(envConfig.getCookie().getSecure());
    	cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
    	cookie.setDomain(envConfig.getCookie().getDomain());
    	cookie.setPath(envConfig.getCookie().getPath());
    	response.addCookie(cookie);

		response.setHeader("Set-AccessToken", result.getAccessToken());
		response.setHeader("Set-RefreshTokenExpiresIn", "" + result.getRefreshTokenExpiresIn());

		return result;
	}

	/**
	 * 구글 로그인
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/sns/google")
	public LoginResult googleLogin(@RequestBody SnsLoginParam param, HttpServletRequest request, HttpServletResponse response) {
    	// 접속자 IP
    	String userIp = CoreUtils.webutils.getRemoteIp(request);

    	// 로그인 처리
    	LoginResult result = loginService.login_memberGoogle(param, userIp);

        /*
         * Cookie 설정
         */
		Cookie cookie = new Cookie(JwtUtils.cookieRefreshToken.회원용, result.getRefreshToken());
    	cookie.setMaxAge(envConfig.getCookie().getMaxAge());
    	cookie.setSecure(envConfig.getCookie().getSecure());
    	cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
    	cookie.setDomain(envConfig.getCookie().getDomain());
    	cookie.setPath(envConfig.getCookie().getPath());
    	response.addCookie(cookie);

		response.setHeader("Set-AccessToken", result.getAccessToken());
		response.setHeader("Set-RefreshTokenExpiresIn", "" + result.getRefreshTokenExpiresIn());

		return result;
	}

	/**
	 * 카카오 로그인
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/sns/kakao")
	public LoginResult kakaoLogin(@RequestBody KakaoLoginParam param, HttpServletRequest request, HttpServletResponse response) {
    	// 접속자 IP
    	String userIp = CoreUtils.webutils.getRemoteIp(request);

    	// 로그인 처리
    	LoginResult result = loginService.login_memberKakao(param, userIp);

        /*
         * Cookie 설정
         */
		Cookie cookie = new Cookie(JwtUtils.cookieRefreshToken.회원용, result.getRefreshToken());
    	cookie.setMaxAge(envConfig.getCookie().getMaxAge());
    	cookie.setSecure(envConfig.getCookie().getSecure());
    	cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
    	cookie.setDomain(envConfig.getCookie().getDomain());
    	cookie.setPath(envConfig.getCookie().getPath());
    	response.addCookie(cookie);

		response.setHeader("Set-AccessToken", result.getAccessToken());
		response.setHeader("Set-RefreshTokenExpiresIn", "" + result.getRefreshTokenExpiresIn());

		return result;
	}

	/**
	 * 회원 refresh token
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/refresh-token/member")
    public TokenDto refreshTokenMember(HttpServletRequest request, HttpServletResponse response) {
		return loginService.refreshToken_member(request, response);
    }

	/**
	 * 내부자 refresh token
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/refresh-token/insider")
    public TokenDto refreshTokenInsider(HttpServletRequest request, HttpServletResponse response) {
		return loginService.refreshToken_insider(request, response);
    }

	@PostMapping("/refresh-token/evaluator")
	public TokenDto refreshTokenEvaluator(HttpServletRequest request, HttpServletResponse response) {
		return loginService.refreshToken_evaluator(request, response);
	}
}
