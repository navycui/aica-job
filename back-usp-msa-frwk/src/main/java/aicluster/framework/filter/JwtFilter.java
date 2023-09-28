package aicluster.framework.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.TokenDto;
import aicluster.framework.security.JwtUtils;
import aicluster.framework.security.TokenProvider;
import aicluster.framework.security.TokenProvider.JwtValidationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    private JwtUtils jwtUtils;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

    	/*
    	 * JwtUtils injection
    	 */
    	if (jwtUtils == null) {
	    	ServletContext context = request.getServletContext();
	        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	        jwtUtils = ctx.getBean(JwtUtils.class);
    	}

        // 1. Request Header 에서 토큰을 꺼냄
        String accessToken = jwtUtils.resolveAccessToken(request);

        // 2. validateToken 으로 토큰 유효성 검사
        JwtValidationType vt = tokenProvider.validateToken(accessToken);

        // 3. expired되었다면, 재 생성
        if (vt == JwtValidationType.EXPIRED) {
        	TokenDto tokenDto = jwtUtils.refreshToken(accessToken, request, response);
        	if (tokenDto != null) {
        		accessToken = tokenDto.getAccessToken();
        		vt = JwtValidationType.NORMAL;
        	}
        }

        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
        if (StringUtils.hasText(accessToken) && vt == JwtValidationType.NORMAL) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            BnMember user = (BnMember)authentication.getPrincipal();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> authorityList = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
            	authorityList.add(authority.getAuthority());
            }
            user.setRoles(authorityList);

            log.info("============================================");
            log.info("login-id:" + user.getLoginId());
            log.info("authority:" + authorityList.toString());
            log.info("============================================");
        }
        else {
        	log.info("============================================");
        	log.info("Not authenticated");
        	log.info("============================================");
        }

        filterChain.doFilter(request, response);
    }
}
