package bnet.library.exception;

import java.util.ArrayList;
import java.util.List;

public class InvalidationsException extends NetException {
	private static final long serialVersionUID = -8714368702153951956L;

	private List<ExceptionMessage> exceptionMessages = new ArrayList<ExceptionMessage>();

	public List<ExceptionMessage> getExceptionMessages() {
		List<ExceptionMessage> exceptionMessages = new ArrayList<>();
		if(this.exceptionMessages != null) {
			exceptionMessages.addAll(this.exceptionMessages);
		}
		return exceptionMessages;
	}

	public void setExceptionMessages(List<ExceptionMessage> exceptionMessages) {
		this.exceptionMessages = new ArrayList<>();
		if(exceptionMessages != null) {
			this.exceptionMessages.addAll(exceptionMessages);
		}
	}

	public InvalidationsException add(String message) {
		exceptionMessages.add(ExceptionMessage.invalid(message));
		return this;
	}

	public InvalidationsException add(String field, String message) {
		exceptionMessages.add(ExceptionMessage.invalid(field, message));
		return this;
	}

	public InvalidationsException() {
		super();
	}

	public InvalidationsException(String message) {
		super(message);
		exceptionMessages.add(ExceptionMessage.invalid(message));
	}

	public InvalidationsException(String field, String message) {
		super(message);
		exceptionMessages.add(ExceptionMessage.invalid(field, message));
	}

	public int size() {
		return this.exceptionMessages.size();
	}
}
