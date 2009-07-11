package base.exception;

/**
 * Throw a TimeException when you've waited too long for something to happen and need to give up.
 * When you catch a TimeException, try to make contact again.
 */
public class TimeException extends RuntimeException {
	
	// Make
	
	public TimeException() {
		this.message = null;
		this.exception = null;
	}
	
	public TimeException(String message) {
		this.message = message;
		this.exception = null;
	}
	
	public TimeException(Exception exception) {
		this.message = null;
		this.exception = exception;
	}
	
	public TimeException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}
	
	// Look

	/** A short text message that describes what happened, or null if none. */
	public final String message;
	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
}
