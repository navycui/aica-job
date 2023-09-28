package bnet.library.exception;

public class ForbiddenException extends NetException {
	private static final long serialVersionUID = -771564655800728520L;
	private ExceptionMessage exceptionMessage;

	public ForbiddenException() {
		super();
	}

	public ForbiddenException(String message) {
		super(message);
		this.exceptionMessage = ExceptionMessage.forbidden(message);
	}

	public ExceptionMessage getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(ExceptionMessage exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
