package base.exception;

/**
 * Throw a NetException when Internet communications are blocked or interrupted.
 * When you catch a NetException, try again with a new connection or message.
 */
public class NetException extends RuntimeException {
	
	public NetException() {
		exception = null;
	}

	public NetException(Exception e) {
		exception = e;
	}
	
	/** The Exception from Java that caused us to throw this NetException. */
	public final Exception exception;
}
