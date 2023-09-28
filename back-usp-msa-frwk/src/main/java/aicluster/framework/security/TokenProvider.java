package aicluster.framework.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CustomUserDetails;
import aicluster.framework.common.entity.TokenDto;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.json;
import bnet.library.util.CoreUtils.string;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String PRINCIPAL_KEY = "principal";
    private static final String BEARER_TYPE = "Bearer";
    @Value("${jwt.accessTokenExpireTime}")
    private long accessTokenExpireTime = 600000;
    @Value("${jwt.refreshTokenExpireTime}")
    private long refreshTokenExpireTime = 1800000;

    private final Key key;

    public enum JwtValidationType {
    	INVALID, EXPIRED, NORMAL
    }

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @PostConstruct
    public void init() {
    	log.info("===============================================");
    	log.info("ACCESS TOKEN EXPIRE TIME:" + this.accessTokenExpireTime + " ms");
    	log.info("REFRESH TOKEN EXPIRE TIME:" + this.refreshTokenExpireTime + " ms");
    	log.info("===============================================");
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date nowDt = new Date();
        long now = nowDt.getTime();
        CustomUserDetails details = (CustomUserDetails)authentication.getPrincipal();
        BnMember member = (BnMember)details.getMember();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + accessTokenExpireTime);
        String accessToken = Jwts.builder()
        		.setIssuedAt(nowDt)
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .claim(PRINCIPAL_KEY, member)
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
        		.setIssuedAt(nowDt)
        		.setSubject(member.getMemberId())
                .setExpiration(new Date(now + refreshTokenExpireTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpireTime)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
        	throw new InvalidationException("인증정보에 권한정보가 없어 작업을 중단하였습니다.");
        }

        String strAuthorities = claims.get(AUTHORITIES_KEY).toString();
        if (string.isBlank(strAuthorities)) {
        	throw new InvalidationException("인증정보에 권한정보가 없어 작업을 중단하였습니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        Object principal = claims.get(PRINCIPAL_KEY);
        if (principal == null) {
        	throw new InvalidationException("인증정보에 사용자 정보가 없어 작업을 중단하였습니다.");
        }

        @SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>)claims.get(PRINCIPAL_KEY);
        BnMember member = json.mapToObject(map, BnMember.class);

        // UserDetails 객체를 만들어서 Authentication 리턴
        return new UsernamePasswordAuthenticationToken(member, "", authorities);
    }

    public JwtValidationType validateToken(String token) {
    	if (string.isBlank(token)) {
    		return JwtValidationType.INVALID;
    	}
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return JwtValidationType.NORMAL;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            return JwtValidationType.INVALID;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return JwtValidationType.EXPIRED;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            return JwtValidationType.INVALID;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            return JwtValidationType.INVALID;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
