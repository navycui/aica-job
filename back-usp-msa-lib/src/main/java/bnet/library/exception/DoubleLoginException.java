package bnet.library.exception;

import java.util.Date;

public class DoubleLoginException extends NetException {

	/**
	 *
	 */
	private static final long serialVersionUID = 835764361006138157L;
	private ExceptionMessage exceptionMessage;
	private Date loginDt;
	private String loginIp;
	private String nextUrl;

	public DoubleLoginException() {
		super();
	}

	public DoubleLoginException(String message) {
		super(message);
		this.exceptionMessage = ExceptionMessage.invalid(message);
	}

	public DoubleLoginException(String field, String message) {
		super(message);
		this.exceptionMessage = ExceptionMessage.invalid(field, message);
	}

	public ExceptionMessage getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(ExceptionMessage exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public Date getLoginDt() {
		if (this.loginDt != null) {
			return new Date(this.loginDt.getTime());
		}
		return null;
	}
	
	public void setLoginDt(Date loginDt) {
		this.loginDt = null;
		if (loginDt != null) {
			this.loginDt = new Date(loginDt.getTime());
		}
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}
}
