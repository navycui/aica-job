package bnet.library.exception;

public class DataViolationException extends NetException {
	private static final long serialVersionUID = 3380290607604561395L;

	private ExceptionMessage exceptionMessage;

	public DataViolationException(Exception e) {
		super(e);
		this.exceptionMessage = ExceptionMessage.error("System error");
	}

	public DataViolationException(Throwable ex) {
		super(ex);
		this.exceptionMessage = ExceptionMessage.error("System error");
	}

	public DataViolationException(String message, Throwable e) {
		super(message, e);
		this.exceptionMessage = ExceptionMessage.error(message);
	}

	public DataViolationException(String sqlId) {
		super(sqlId);
		this.exceptionMessage = ExceptionMessage.invalid(sqlId);
	}

	public ExceptionMessage getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(ExceptionMessage exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
