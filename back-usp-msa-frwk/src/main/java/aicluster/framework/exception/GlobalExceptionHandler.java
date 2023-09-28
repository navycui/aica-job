package aicluster.framework.exception;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.exception.dto.ErrorResponse;
import aicluster.framework.exception.dto.ErrorResponseItem;
import aicluster.framework.log.LogUtils;
import aicluster.framework.log.vo.ErrorLog;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.CommunicationException;
import bnet.library.exception.DoubleLoginException;
import bnet.library.exception.ExceptionMessage;
import bnet.library.exception.ForbiddenException;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.exception.LoggableException;
import bnet.library.exception.UnauthorizedException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.webutils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Class<?>[] EXCEPT_THROWABLES = {
	        InvalidationException.class,
	        InvalidationsException.class,
	        SocketException.class,
	        DoubleLoginException.class,
	        HttpRequestMethodNotSupportedException.class,
	        UnauthorizedException.class,
	        ForbiddenException.class,
	        BadCredentialsException.class,
	        ClientAbortException.class,
	        MaxUploadSizeExceededException.class
	    };

	@Autowired
	private EnvConfig config;

	@Autowired
	private LogUtils logUtils;

	@ExceptionHandler(LoggableException.class)
	public ResponseEntity<ErrorResponse> handleLoggableException(LoggableException e, HttpServletRequest request) {
		writeLog(request, e);
		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(e.getExceptionMessage().getStatus())
				.message(e.getMessage())
				.errors(new ArrayList<ErrorResponseItem>())
				.build();
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidationException.class)
	public ResponseEntity<ErrorResponse> handleInvalidationException(InvalidationException e, HttpServletRequest request) {
		writeLog(request, e);
		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(e.getExceptionMessage().getStatus())
				.message(e.getMessage())
				.errors(new ArrayList<ErrorResponseItem>())
				.build();
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidationsException.class)
	public ResponseEntity<ErrorResponse> handleInvalidationsException(InvalidationsException e, HttpServletRequest request) {
		writeLog(request, e);

		List<ErrorResponseItem> list = new ArrayList<>();
		for (ExceptionMessage msg : e.getExceptionMessages()) {
			ErrorResponseItem item = ErrorResponseItem.builder()
					.field(msg.getField())
					.message(msg.getMessage())
					.build();
			list.add(item);
		}

		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(HttpStatus.SC_BAD_REQUEST)
				.message("errors")
				.errors(list)
				.build();
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(error);
	}

	@ExceptionHandler(CommunicationException.class)
	public ResponseEntity<ErrorResponse> handleBatchException(CommunicationException e, HttpServletRequest request) {
		writeLog(request, e);

		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(HttpStatus.SC_BAD_REQUEST)
				.message(e.getMessage())
				.errors(new ArrayList<ErrorResponseItem>())
				.build();
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(error);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(HttpStatus.SC_NOT_FOUND)
				.message("지원하지 않는 기능입니다.")
				.errors(new ArrayList<ErrorResponseItem>())
				.build();
		return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(error);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(UnauthorizedException e, HttpServletRequest request) {
		writeLog(request, e);

		final ErrorResponse error = ErrorResponse.builder()
				.error("Unauthorized")
				.status(HttpStatus.SC_UNAUTHORIZED)
				.message("로그인을 하세요.")
				.errors(new ArrayList<>())
				.build();
		return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e, HttpServletRequest request) {
		writeLog(request, e);

		final ErrorResponse error = ErrorResponse.builder()
				.error("Forbidden")
				.status(HttpStatus.SC_FORBIDDEN)
				.message("권한이 없습니다.")
				.errors(new ArrayList<>())
				.build();
		return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).body(error);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
		writeLog(request, e);

		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(HttpStatus.SC_BAD_REQUEST)
				.message("아이디 또는 비밀번호가 올바르지 않습니다.")
				.errors(new ArrayList<>())
				.build();
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exception(Exception e, HttpServletRequest request) throws IOException {
		writeLog(request, e);

		final ErrorResponse error = ErrorResponse.builder()
				.error("Exception")
				.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
				.message("시스템 오류로 작업을 중단하였습니다.")
				.errors(new ArrayList<>())
				.build();

		return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(error);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> MaxUploadSizeExceededException(Exception e, HttpServletRequest request) throws IOException {
		final ErrorResponse error = ErrorResponse.builder()
				.error("Exception")
				.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
				.message("파일 용량이 업로드 가능한 최대 파일 크기보다 커 업로드 불가합니다.")
				.errors(new ArrayList<>())
				.build();

		return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(error);
	}

    private void writeLog(HttpServletRequest request, Exception e) {
        String requestURI = request.getRequestURI();

        String errorTrace = CoreUtils.exception.getStackTraceString(e);
        log.error(errorTrace);

        if (logUtils == null) {
            return;
        }
        for (Class<?> cls: EXCEPT_THROWABLES) {
            if (cls.isAssignableFrom(e.getClass()) ) {
                return;
            }
        }

        ErrorLog log = new ErrorLog();
        log.setApiSystemId(config.getSystemId());
        log.setLogDt(new Date());
        log.setMemberIp(webutils.getRemoteIp(request));
        log.setErrorCode(e.getClass().getName());
        log.setErrorMsg(errorTrace);
        log.setUrl(requestURI);
        BnMember user = SecurityUtils.getCurrentMember();
        if (user == null) {
        	user = SecurityUtils.getAnonymousMember();
        }
        log.setMemberId(user.getMemberId());
        log.setLoginId(user.getLoginId());
        log.setMemberNm(user.getMemberNm());
        log.setMemberType(user.getMemberType());
        log.setGender(user.getGender());
        log.setAge(CoreUtils.date.getAge(user.getBirthday()));
        log.setPositionNm(user.getPositionNm());
        log.setDeptNm(user.getDeptNm());
        //log.setPstinstNm(user.getPstinstNm());

        logUtils.saveErrorLog(log);
    }

}
