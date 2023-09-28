package aicluster.batch.support;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
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
import aicluster.framework.security.TokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Transactional
@SpringBootTest
@ActiveProfiles("junit")
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

	@BeforeEach
	public void setup() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		String accessToken = getAccessToken();
		Authentication authentication = tokenProvider.getAuthentication(accessToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
    }

	private String getAccessToken() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

		BnMember member = BnMember.builder()
				.memberId("member-test")
				.loginId("test-user")
				.encPasswd("")
				.memberNm("테스터")
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
}
