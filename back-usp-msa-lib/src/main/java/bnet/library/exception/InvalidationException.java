package bnet.library.exception;

public class InvalidationException extends NetException {
	private static final long serialVersionUID = -885675546754024737L;

	private ExceptionMessage exceptionMessage;

	public InvalidationException() {
		super();
	}

	public InvalidationException(String message) {
		super(message);
		this.exceptionMessage = ExceptionMessage.invalid(message);
	}

	public InvalidationException(Integer status, String message) {
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
