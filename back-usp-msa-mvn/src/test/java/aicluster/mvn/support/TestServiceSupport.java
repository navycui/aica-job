package aicluster.mvn.support;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.Code;
import aicluster.framework.security.TokenProvider;
import bnet.library.util.CoreUtils.string;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;

@Transactional
@SpringBootTest
@ActiveProfiles("junit")
@TestInstance(Lifecycle.PER_CLASS)
public class TestServiceSupport {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String PRINCIPAL_KEY = "principal";
    @Value("${jwt.accessTokenExpireTime}")
    private long accessTokenExpireTime = 600000;
    @Value("${jwt.refreshTokenExpireTime}")
    private long refreshTokenExpireTime = 1800000;
    @Value("${jwt.secret}")
    private String secretKey;

	@Autowired
	private TokenProvider tokenProvider;

	private tokenMemberVo memberVo;

	@Data
	private static class tokenMemberVo {
		private String tokenMemberId;
		private String tokenMemberType;

		public String getTokenMemberId() {
			if (string.isBlank(this.tokenMemberId)) {
				return "member-test";
			}
			return this.tokenMemberId;
		}
		public String getTokenMemberType() {
			if (string.isBlank(this.tokenMemberType)) {
				return Code.memberType.개인;
			}
			return this.tokenMemberType;
		}
	}

	@BeforeAll
	public void start() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@BeforeEach
	public void setup() throws Exception {
		resetAccessToken();
    }

	private String getAccessToken() {
		if (memberVo == null)  memberVo = new tokenMemberVo();

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

		BnMember member = BnMember.builder()
				.memberId(memberVo.getTokenMemberId())
				.loginId("test-user")
				.encPasswd("")
				.memberNm("테스터")
				.memberType(memberVo.getTokenMemberType())
				.authorityId("")
				.enabled(true)
				.build();

		long now = (new Date()).getTime();
		Date accessTokenExpiresIn = new Date(now + 600000);
        String accessToken = Jwts.builder()
                .setSubject("test-user")
                .claim(AUTHORITIES_KEY, "ROLE_USER,ROLE_ADMIN")
                .claim(PRINCIPAL_KEY, member)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return accessToken;
	}

	private void resetAccessToken() {
		String accessToken = getAccessToken();
		Authentication authentication = tokenProvider.getAuthentication(accessToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public void resetAccessToken(String memberId, String memberType) {
		if (memberVo == null)  memberVo = new tokenMemberVo();
		memberVo.setTokenMemberId(memberId);
		memberVo.setTokenMemberType(memberType);

		resetAccessToken();
	}

	public void setTokenMemberType(String memberType) {
		if (memberVo == null)  memberVo = new tokenMemberVo();
		memberVo.setTokenMemberType(memberType);
	}
}
