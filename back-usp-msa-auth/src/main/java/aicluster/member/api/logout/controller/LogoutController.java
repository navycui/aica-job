package aicluster.member.api.logout.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.JwtUtils;
import aicluster.member.api.logout.dto.LogoutResponseDto;
import aicluster.member.api.logout.service.LogoutService;
import bnet.library.util.CoreUtils;

//@Slf4j
@RestController
@RequestMapping("/api/logout")
public class LogoutController {
	@Autowired
	private LogoutService logoutService;
	@Autowired
	private EnvConfig config;

	/**
	 * 회원 로그아웃
	 * @param request
	 * @param response
	 */
	@PostMapping("/member")
    public void logout_member(HttpServletRequest request, HttpServletResponse response) {
		String domain = config.getPortal().getDomain();
		String dxpUrl = domain + "/dxp/ssoLogout.do";
		String sazUrl = domain + "/saz/svc/sso/logout.do";
		logoutService.session_logout_post(dxpUrl);	//데이터유통 포털 로그아웃
		logoutService.session_logout_post(sazUrl);	//안심구역 포털 로그아웃
		
    	logoutService.logout_member(request);
    	Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.회원용);
    	if (cookie != null) {
    		cookie.setValue(null);
    		cookie.setMaxAge(0);
    		response.addCookie(cookie);
    	}

    	response.setHeader("Set-AccessToken", "removed");
		response.setHeader("Set-RefreshTokenExpiresIn", "0");
    }

    /**
     * 내부자 로그아웃
     * @param request
     * @param response
     */
    @PostMapping("/insider")
    public void logout_insider(HttpServletRequest request, HttpServletResponse response) {
    	logoutService.logout_insider(request);
    	Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.내부사용자용);
    	if (cookie != null) {
    		cookie.setValue(null);
    		cookie.setMaxAge(0);
    		response.addCookie(cookie);
    	}

    	response.setHeader("Set-AccessToken", "removed");
		response.setHeader("Set-RefreshTokenExpiresIn", "0");
    }

    /**
	 * 데이터유통 / 안심구역 회원 로그아웃
	 * @param request
	 * @param response
	 */
	@PostMapping("/member/ses")
    public LogoutResponseDto session_logout_member(HttpServletRequest request, HttpServletResponse response) {
		LogoutResponseDto result = logoutService.session_logout_member(request);
    	Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.회원용);
    	if (cookie != null) {
    		cookie.setValue(null);
    		cookie.setMaxAge(0);
    		response.addCookie(cookie);
    	}

    	response.setHeader("Set-AccessToken", "removed");
		response.setHeader("Set-RefreshTokenExpiresIn", "0");
		return result;
    }
}
