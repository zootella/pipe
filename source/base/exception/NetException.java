package base.exception;

/**
 * Throw a NetException when Internet communications are blocked or interrupted.
 * When you catch a NetException, try again with a new connection or message.
 */
public class NetException extends ProgramException {
	
	// Make
	
	public NetException() {
		this.message = null;
		this.exception = null;
	}
	
	public NetException(String message) {
		this.message = message;
		this.exception = null;
	}
	
	public NetException(Exception exception) {
		this.message = null;
		this.exception = exception;
	}
	
	public NetException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}
	
	// Look

	/** A short text message that describes what happened, or null if none. */
	public final String message;
	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
}
