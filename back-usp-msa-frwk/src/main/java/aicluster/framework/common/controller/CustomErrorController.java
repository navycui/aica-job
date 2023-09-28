package aicluster.framework.common.controller;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.exception.dto.ErrorResponse;
import aicluster.framework.exception.dto.ErrorResponseItem;

@RestController
public class CustomErrorController implements ErrorController {
	@RequestMapping("/error")
	public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			int statusCode = Integer.valueOf(status.toString());
			if (statusCode == HttpStatus.SC_NOT_FOUND) {
				final ErrorResponse error = ErrorResponse.builder()
						.error("Invalid")
						.status(statusCode)
						.message("지원하지 않는 기능입니다(CEC)")
						.errors(new ArrayList<ErrorResponseItem>())
						.build();
				return ResponseEntity.status(statusCode).body(error);
			}
			else if (statusCode == HttpStatus.SC_FORBIDDEN) {
				final ErrorResponse error = ErrorResponse.builder()
						.error("Forbidden")
						.status(statusCode)
						.message("권한이 없습니다(CEC)")
						.errors(new ArrayList<ErrorResponseItem>())
						.build();
				return ResponseEntity.status(statusCode).body(error);
			}
			else if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
				final ErrorResponse error = ErrorResponse.builder()
						.error("Forbidden")
						.status(statusCode)
						.message("로그인을 하세요(CEC)")
						.errors(new ArrayList<ErrorResponseItem>())
						.build();
				return ResponseEntity.status(statusCode).body(error);
			}
			else {
				final ErrorResponse error = ErrorResponse.builder()
						.error("Exception")
						.status(statusCode)
						.message("시스템 오류로 작업을 중단하였습니다(CEC)")
						.errors(new ArrayList<ErrorResponseItem>())
						.build();
				return ResponseEntity.status(statusCode).body(error);
			}
		}
		final ErrorResponse error = ErrorResponse.builder()
				.error("Exception")
				.status(500)
				.message("시스템 오류로 작업을 중단하였습니다(CEC)")
				.errors(new ArrayList<ErrorResponseItem>())
				.build();
		return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(error);
	}
}
