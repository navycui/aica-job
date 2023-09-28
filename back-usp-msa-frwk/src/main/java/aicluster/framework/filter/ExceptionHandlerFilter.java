package aicluster.framework.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import aicluster.framework.exception.dto.ErrorResponse;
import aicluster.framework.exception.dto.ErrorResponseItem;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception ex) {
			String error = CoreUtils.exception.getStackTraceString(ex);
			log.error(error);
			setErrorResponse(response, ex);
		}
	}

	public void setErrorResponse(HttpServletResponse response, Throwable ex) {
		log.error(ex.getMessage());

		int status = 0;
		String message = null;
		String errorCode = null;
		if (ex instanceof InvalidationException) {
			status = HttpStatus.SC_BAD_REQUEST;
			message = ex.getMessage();
			errorCode = "Invalid";
		}
		else {
			status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
			message = "시스템 오류로 작업을 중단하였습니다.";
			errorCode = "Exception";
		}
		response.setStatus(status);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		final ErrorResponse error = ErrorResponse.builder()
				.error(errorCode)
				.status(status)
				.message(message)
				.errors(new ArrayList<ErrorResponseItem>())
				.build();

		try {
			String json = CoreUtils.json.toString(error);
			System.out.println(json);
			response.getWriter().write(json);
		} catch (IOException e) {
			String msg = CoreUtils.exception.getStackTraceString(e);
			log.error(msg);
		}
	}
}
