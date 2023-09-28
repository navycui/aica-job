package bnet.library.exception;

public class LoggableException extends NetException {
	/**
	 *
	 */
	private static final long serialVersionUID = -8705035263144793138L;
	private ExceptionMessage exceptionMessage;

	public LoggableException() {
		super();
	}

	public LoggableException(String message) {
		super(message);
		this.exceptionMessage = ExceptionMessage.invalid(message);
	}

	public LoggableException(Integer status, String message) {
		super(message);
		this.exceptionMessage = ExceptionMessage.invalid(status, message);
	}

	public ExceptionMessage getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(ExceptionMessage exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
