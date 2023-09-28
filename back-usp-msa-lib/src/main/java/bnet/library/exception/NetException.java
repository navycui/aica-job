package bnet.library.exception;

import java.util.Collection;
import java.util.stream.Collectors;

public class NetException extends RuntimeException {

	private static final long serialVersionUID = 6611053398247736679L;

    public NetException() {
        super();
    }

	public NetException(Exception e) {
		super(e);
	}

	public NetException(String msg) {
		super(msg);
	}

	public NetException(Throwable ex) {
		super(ex);
	}

	public NetException(String message, Throwable e) {
		super(message, e);
	}

	public NetException(Collection<Exception> ex) {
		super(ex.stream().map(e -> e.getClass().getName() + " " + e.getMessage()).collect(Collectors.joining("\n")));
	}
}
