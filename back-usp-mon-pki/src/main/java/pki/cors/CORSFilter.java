package pki.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CORSFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		log.debug("CORSFilter HTTP Request: " + request.getMethod() + " ["+request.getRequestURI()+"]");

		if (string.endsWith(request.getRequestURI(), "/transkeyServlet")) {
			log.debug("키보드 보안 서블릿 CORS 처리");
			String origin = CoreUtils.string.removeWhitespace(request.getHeader("Origin"));
			((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", origin);
			((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Credentials", "true");
			((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
			((HttpServletResponse) servletResponse).addHeader("Access-Control-Max-Age", "3600");
		}
		else {
			log.debug("그외 페이지 CORS 처리");
			// Authorize (allow) all domains to consume the content
			((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
			((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Credentials", "false");
			((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
			((HttpServletResponse) servletResponse).addHeader("Access-Control-Max-Age", "10");
			((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,Accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
			((HttpServletResponse) servletResponse).addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Credentials");


			// For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS
			// 모바일 키보드 보안 솔루션에서 가상 키보드의 키 클릭 이벤트 발생 시 'OPTIONS' 메서드로 호출되어 아래 OPTIONS에 대한 CORS 적용 주석 처리
			if (request.getMethod().equals("OPTIONS")) {
				HttpServletResponse resp = (HttpServletResponse) servletResponse;
				resp.setStatus(HttpServletResponse.SC_ACCEPTED);
				return;
			}
		}

		// pass the request along the filter chain
		chain.doFilter(servletRequest, servletResponse);
	}

}
