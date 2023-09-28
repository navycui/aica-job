package aicluster.member.support;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;

import aicluster.framework.common.entity.TokenDto;
import aicluster.member.api.login.dto.LoginParam;
import lombok.Builder;
import lombok.Data;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("junit")
public class TestControllerSupport {

	@Autowired
	Environment env;

	private EnvDto envDto;

	private static final String TEST_USERNAME = "aicaadmin";
	private static final String TEST_PASSWORD = "AB123!@#";
	private static final String LOGIN_URI = "/api/login/insider";

	protected HttpHeaders headers = new HttpHeaders();
	protected Cookie[] cookies = null;

	@Autowired
	protected MockMvc mvc;

	@Data
	@Builder
	private static class EnvDto
	{
		private String contextPath;

		private String getLoginUri() {
			return contextPath + LOGIN_URI;
		}
	}

	@BeforeEach
	public void setup() throws Exception
	{
		envDto = EnvDto.builder().contextPath(env.getProperty("server.servlet.context-path")).build();

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		LoginParam loginParam = new LoginParam();
		loginParam.setLoginId(TEST_USERNAME);
		loginParam.setPasswd(TEST_PASSWORD);

		Gson reqGson = new Gson();
		String jsonData = reqGson.toJson(loginParam);

		RequestBuilder builder = MockMvcRequestBuilders.post(envDto.getLoginUri())
				.contextPath(envDto.getContextPath())
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonData.getBytes());

		MvcResult result = this.mvc.perform(builder).andExpect(status().isOk()).andReturn();

		Gson resGson = new Gson();
		TokenDto token = resGson.fromJson(result.getResponse().getContentAsString(), TokenDto.class);

		cookies = result.getResponse().getCookies();
		headers.setBearerAuth(token.getAccessToken());
		//headers.set("X-Requested-With", "XMLHttpRequest");
		headers.set("Auth", "Bearer " + token.getAccessToken());
    }

	public MockHttpServletRequestBuilder getMethodBuilder(String url) {
		return MockMvcRequestBuilders.get(url).contextPath(envDto.getContextPath()).headers(headers).cookie(cookies);
	}

	public MockHttpServletRequestBuilder postMethodBuilder(String url) {
		return MockMvcRequestBuilders.post(url).contextPath(envDto.getContextPath()).headers(headers).cookie(cookies);
	}

	public MockHttpServletRequestBuilder putMethodBuilder(String url) {
		return MockMvcRequestBuilders.put(url).contextPath(envDto.getContextPath()).headers(headers).cookie(cookies);
	}

	public MockHttpServletRequestBuilder deleteMethodBuilder(String url) {
		return MockMvcRequestBuilders.delete(url).contextPath(envDto.getContextPath()).headers(headers).cookie(cookies);
	}

	public MockHttpServletRequestBuilder multipartMethodBuilder(String url, MockMultipartFile file) {
		return MockMvcRequestBuilders.multipart(url).file(file).contextPath(envDto.getContextPath()).headers(headers).cookie(cookies);
	}

	public MockHttpServletRequestBuilder putMultipartMethodBuilder(String url, MockMultipartFile file) {
		return MockMvcRequestBuilders.multipart(url).file(file).contextPath(envDto.getContextPath()).headers(headers).cookie(cookies).with(new RequestPostProcessor() {

			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
				return request;
			}
		});
	}
}
