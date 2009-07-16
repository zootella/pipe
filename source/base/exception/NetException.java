package base.exception;

/**
 * Throw a NetException when Internet communications are blocked or interrupted.
 * When you catch a NetException, try again with a new connection or message.
 */
public class NetException extends ProgramException {
	
	// Make
	
	public NetException() {
		this.exception = null;
		this.message = null;
	}
	
	public NetException(String message) {
		this.exception = null;
		this.message = message;
	}
	
	public NetException(Exception exception) {
		this.exception = exception;
		this.message = null;
	}
	
	public NetException(Exception exception, String message) {
		this.exception = exception;
		this.message = message;
	}
	
	// Look

	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
	/** A short text message that describes what happened, or null if none. */
	public final String message;
}
