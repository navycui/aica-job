package bnet.library.exception;

public class CommunicationException extends NetException {
	private static final long serialVersionUID = -885675546754024737L;

	private ExceptionMessage exceptionMessage;

	public CommunicationException() {
		super();
	}

	public CommunicationException(String message) {
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
