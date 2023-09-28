package aicluster.framework.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import aicluster.framework.common.dao.FwAuthorRoleDao;
import aicluster.framework.common.dao.FwEmpInfoDao;
import aicluster.framework.common.dao.FwMberInfoDao;
import aicluster.framework.common.dao.FwUsptEvlMfcmmDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAuthorRole;
import aicluster.framework.common.entity.CustomUserDetails;
import aicluster.framework.common.entity.TokenDto;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.TokenProvider.JwtValidationType;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtils {
	public static final String AUTHORIZATION_HEADER = "Auth";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.exceptAutoRefreshTokenUriPattern}")
    private String exceptAutoRefreshTokenUriPattern = null;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
    private EnvConfig envConfig;

	@Autowired
	private FwMberInfoDao fwMemberDao;

	@Autowired
	private FwEmpInfoDao fwInsiderDao;

	@Autowired
	private FwUsptEvlMfcmmDao fwUsptEvlMfcmmDao;

	@Autowired
	private FwAuthorRoleDao fwAuthorityRoleDao;

	public static class cookieRefreshToken {
		public static final String 회원용 = "member-refresh-token";
		public static final String 내부사용자용 = "insider-refresh-token";
	}

	public static class memberType {
		public static final String CODE_GROUP = "MEMBER_TYPE";

		public static final String 개인사용자 = "INDIVIDUAL";
		public static final String 개인사업자 = "SOLE";
		public static final String 법인사업자 = "CORPORATION";
		public static final String 대학 = "UNIVERSITY";
		public static final String 내부사용자 = "INSIDER";
		public static final String 평가위원 = "EVALUATOR";
	}

	public static class memberSt {
		public static final String CODE_GROUP = "MEMBER_ST";

		public static final String 정상 = "NORMAL";
		public static final String 불량회원 = "BLACK";
		public static final String 휴면 = "SLEEP";
		public static final String 탈퇴 = "SECESSION";
	}

	public TokenDto refreshToken(String accessToken, HttpServletRequest request, HttpServletResponse response) {
		if (string.isNotBlank(exceptAutoRefreshTokenUriPattern)) {
			String uri = request.getRequestURI();
			if (CoreUtils.antpath.match(exceptAutoRefreshTokenUriPattern, uri)) {
				return null;
			}
		}
		Authentication authentication = tokenProvider.getAuthentication(accessToken);
		BnMember user = (BnMember)authentication.getPrincipal();

		/*
		 * 내부 사용자인 경우
		 */
		if (string.equals(user.getMemberType(), JwtUtils.memberType.내부사용자)) {
	    	Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.내부사용자용);
	    	if (cookie == null) {
	    		return null;
	    	}
	    	String refreshToken = cookie.getValue();
	    	log.info(">>>>REFRESH-TOKEN:" + refreshToken);
	    	TokenDto dto = refreshToken_insider(refreshToken);
	    	cookie = new Cookie(JwtUtils.cookieRefreshToken.내부사용자용, dto.getRefreshToken());
	    	cookie.setMaxAge(envConfig.getCookie().getMaxAge());
	    	cookie.setSecure(envConfig.getCookie().getSecure());
	    	cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
	    	cookie.setDomain(envConfig.getCookie().getDomain());
	    	cookie.setPath(envConfig.getCookie().getPath());
	    	response.addCookie(cookie);

    		response.setHeader("Set-AccessToken", dto.getAccessToken());
    		response.setHeader("Set-RefreshTokenExpiresIn", "" + dto.getRefreshTokenExpiresIn());

	        return dto;
		}
		/*
		 * 평가위원인 경우
		 */
		else if (string.equals(user.getMemberType(), JwtUtils.memberType.평가위원)) {
			Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.회원용);
	    	if (cookie == null) {
	    		return null;
	    	}
	    	String refreshToken = cookie.getValue();
	    	log.info(">>>>REFRESH-TOKEN:" + refreshToken);
	    	TokenDto dto = refreshToken_evaluator(refreshToken);
	    	if (dto != null) {
	    		cookie = new Cookie(JwtUtils.cookieRefreshToken.회원용, dto.getRefreshToken());
	    		cookie.setMaxAge(envConfig.getCookie().getMaxAge());
	    		cookie.setSecure(envConfig.getCookie().getSecure());
	    		cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
	    		cookie.setDomain(envConfig.getCookie().getDomain());
	    		cookie.setPath(envConfig.getCookie().getPath());
	    		response.addCookie(cookie);

	    		response.setHeader("Set-AccessToken", dto.getAccessToken());
	    		response.setHeader("Set-RefreshTokenExpiresIn", "" + dto.getRefreshTokenExpiresIn());
	    	}

	        return dto;
		}
		/*
		 * 회원인 경우
		 */
		else {
			Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.회원용);
	    	if (cookie == null) {
	    		return null;
	    	}
	    	String refreshToken = cookie.getValue();
	    	log.info(">>>>REFRESH-TOKEN:" + refreshToken);
	    	TokenDto dto = refreshToken_member(refreshToken);
	    	if (dto != null) {
	    		cookie = new Cookie(JwtUtils.cookieRefreshToken.회원용, dto.getRefreshToken());
	    		cookie.setMaxAge(envConfig.getCookie().getMaxAge());
	    		cookie.setSecure(envConfig.getCookie().getSecure());
	    		cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
	    		cookie.setDomain(envConfig.getCookie().getDomain());
	    		cookie.setPath(envConfig.getCookie().getPath());
	    		response.addCookie(cookie);

	    		response.setHeader("Set-AccessToken", dto.getAccessToken());
	    		response.setHeader("Set-RefreshTokenExpiresIn", "" + dto.getRefreshTokenExpiresIn());
	    	}

	        return dto;
		}

	}

	private Authentication getAuthentication(BnMember member) {
    	List<GrantedAuthority> authorityList = new ArrayList<>();
    	List<CmmtAuthorRole> roleList = fwAuthorityRoleDao.selectList(member.getAuthorityId());
    	for (CmmtAuthorRole role : roleList) {
    		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleId());
    		authorityList.add(grantedAuthority);
    	}
    	CustomUserDetails details = new CustomUserDetails(member.getLoginId(), null, true, authorityList, member);
		return new UsernamePasswordAuthenticationToken(details, "", authorityList);
	}

    public TokenDto refreshToken_member(String refreshToken) {
        // Refresh Token 검증
        if (tokenProvider.validateToken(refreshToken) != JwtValidationType.NORMAL) {
            return null;
        }

        // 없으면 인증 오류
        BnMember member = fwMemberDao.selectBnMember_refreshToken(refreshToken);
        if (member == null || !string.equals(member.getMemberSt(), JwtUtils.memberSt.정상)) {
            return null;
        }

        // authentication 생성
        Authentication authentication = getAuthentication(member);

        // 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 저장소 정보 업데이트
        fwMemberDao.updateRefreshToken(member.getMemberId(), tokenDto.getRefreshToken());

        // 토큰 발급
        return tokenDto;
    }

    public TokenDto refreshToken_insider(String refreshToken) {
        // Refresh Token 검증
        if (tokenProvider.validateToken(refreshToken) != JwtValidationType.NORMAL) {
            return null;
        }

        BnMember member = fwInsiderDao.selectBnMember_refreshToken(refreshToken);
        if (member == null || !string.equals(member.getMemberSt(), JwtUtils.memberSt.정상)) {
            return null;
        }

        // authentication 생성
        Authentication authentication = getAuthentication(member);

        // 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        fwInsiderDao.updateRefreshToken(member.getMemberId(), tokenDto.getRefreshToken());

        // 토큰 발급
        return tokenDto;
    }

    /**
     * 평가위원 token refresh
     *
     * @param refreshToken refresh token
     * @return refresh token DTO
     */
    public TokenDto refreshToken_evaluator(String refreshToken) {
        // Refresh Token 검증
        if (tokenProvider.validateToken(refreshToken) != JwtValidationType.NORMAL) {
            return null;
        }

        BnMember member = fwUsptEvlMfcmmDao.selectBnMember_refreshToken(refreshToken);
        if (member == null || !string.equals(member.getMemberSt(), JwtUtils.memberSt.정상)) {
            return null;
        }

        // authentication 생성
        Authentication authentication = getAuthentication(member);

        // 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        fwUsptEvlMfcmmDao.updateRefreshToken(member.getMemberId(), tokenDto.getRefreshToken());

        // 토큰 발급
        return tokenDto;
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtUtils.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtUtils.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
