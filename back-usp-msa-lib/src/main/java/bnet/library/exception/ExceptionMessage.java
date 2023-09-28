package bnet.library.exception;

import java.io.Serializable;

import org.apache.http.HttpStatus;

public class ExceptionMessage implements Serializable {
	private static final long serialVersionUID = 8535249061907775899L;
	private String field;
	private String message;
	private Integer status;

	public ExceptionMessage() {

	}

	public ExceptionMessage(Integer status, String field, String message) {
		this.status = status;
		this.field = field;
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}

    public static ExceptionMessage success(String message) {
        return new ExceptionMessage(HttpStatus.SC_OK, null, message);
    }
    public static ExceptionMessage invalid(String message) {
        return new ExceptionMessage(HttpStatus.SC_BAD_REQUEST, null, message);
    }
    public static ExceptionMessage invalid(String field, String message) {
        return new ExceptionMessage(HttpStatus.SC_BAD_REQUEST, field, message);
    }
    public static ExceptionMessage invalid(Integer status, String message) {
    	return new ExceptionMessage(status, null, message);
    }
    public static ExceptionMessage error(String message) {
        return new ExceptionMessage(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, message);
    }
    public static ExceptionMessage notfound(String message) {
        return new ExceptionMessage(HttpStatus.SC_NOT_FOUND, null, message);
    }
    public static ExceptionMessage unauthorized(String message) {
        return new ExceptionMessage(HttpStatus.SC_UNAUTHORIZED, null, message);
    }
    public static ExceptionMessage forbidden(String message) {
        return new ExceptionMessage(HttpStatus.SC_FORBIDDEN, null, message);
    }
    public static ExceptionMessage loginRequired(String message) {
    	return new ExceptionMessage(499, null, message);
    }

	@Override
	public String toString() {
		return "ExceptionMessage [field=" + field + ", message=" + message + ", status=" + status + "]";
	}
}
