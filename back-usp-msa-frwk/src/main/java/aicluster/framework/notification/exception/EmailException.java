package aicluster.framework.notification.exception;

import bnet.library.exception.ExceptionMessage;
import bnet.library.exception.NetException;

public class EmailException extends NetException {
	private static final long serialVersionUID = -885675546754024737L;

	private ExceptionMessage exceptionMessage;

	public EmailException() {
		super();
	}

	public EmailException(String message) {
		super(message);
		this.exceptionMessage = ExceptionMessage.invalid(message);
	}

	public ExceptionMessage getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(ExceptionMessage exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
