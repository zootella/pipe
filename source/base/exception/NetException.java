package base.exception;

/**
 * Throw a NetException when Internet communications are blocked or interrupted.
 * When you catch a NetException, try again with a new connection or message.
 */
public class NetException extends RuntimeException {
	
	public NetException() {
		message = null;
		exception = null;
	}
	
	public NetException(String s) {
		message = s;
		exception = null;
	}

	public NetException(Exception e) {
		message = null;
		exception = e;
	}
	
	public final String message;
	public final Exception exception;
	//TODO cant you make and extend BaseException to put message and exception in there?
}
