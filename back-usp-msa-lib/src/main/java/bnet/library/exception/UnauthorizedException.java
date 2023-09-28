package bnet.library.exception;

public class UnauthorizedException extends NetException {
	private static final long serialVersionUID = -771564655800728520L;
	private ExceptionMessage exceptionMessage;

	public UnauthorizedException() {
		super();
	}

	public UnauthorizedException(String message) {
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
