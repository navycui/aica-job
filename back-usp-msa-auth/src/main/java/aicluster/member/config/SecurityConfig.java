package aicluster.member.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import aicluster.framework.config.EnvConfig;
import aicluster.framework.config.JwtSecurityConfig;
import aicluster.framework.security.JwtAccessDeniedHandler;
import aicluster.framework.security.JwtAuthenticationEntryPoint;
import aicluster.framework.security.TokenProvider;
import lombok.RequiredArgsConstructor;

/**
 * 보안 설정
 *
 * @author patrick
 *
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private EnvConfig envConfig;

    // Password encoding 방법 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 database 테스트가 원활하도록 관련 API 들은 전부 무시
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	// CSRF 설정 Disable
        	.csrf().disable()

            // exception handling 할 때 우리가 만든 클래스를 추가
            .exceptionHandling()
	            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
	            .accessDeniedHandler(jwtAccessDeniedHandler)

            // h2-console 을 위한 설정을 추가: 불필요한 듯한데...
//            .and()
//	            .headers()
//		            .frameOptions()
//		            .sameOrigin()

		    // CORS가 동작하려면 반드시 필요한 설정. WebConfig의 WebMvcConfigurer.addCorsMappings()와 함께 설정해 주어야 한다.
		    .and()
		    	.cors()
//		    		.configurationSource(corsConfigurationSource())

            // 시큐리티는 기본적으로 세션을 사용
            // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
            .and()
	            .sessionManagement()
	            	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
            .and()
            	.authorizeRequests()
            		.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
		            //.antMatchers("/member/api/auth/**").permitAll()
		            //.antMatchers("/member/api/members/join").permitAll()
		            //.antMatchers("/member/api/yym/**").permitAll()
		            .anyRequest().access("@authorizationChecker.check(request, authentication)")
		            //.anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요

            // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
            .and()
            	.apply(new JwtSecurityConfig(tokenProvider));
    }

    // CORS 허용 적용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        for (String origin: envConfig.getCors().getOrigins()) {
        	configuration.addAllowedOrigin(origin);
        }

        for (String header: envConfig.getCors().getHeaders()) {
        	configuration.addExposedHeader(header);
        }

        String[] defaultExposedHeaders = {"Set-AccessToken", "Set-RefreshTokenExpiresIn"};
        for (String header: defaultExposedHeaders) {
        	configuration.addExposedHeader(header);
        }

        configuration.addAllowedHeader("*");
        for (String method: envConfig.getCors().getMethods()) {
        	configuration.addAllowedMethod(method);
        }
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(envConfig.getCors().getPattern(), configuration);
        return source;
    }
}
