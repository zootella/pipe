package base.exception;

/**
 * Throw a TimeException when you've waited too long for something to happen and need to give up.
 * When you catch a TimeException, try to make contact again.
 */
public class TimeException extends RuntimeException {
	
	// Make
	
	public TimeException() {
		this.exception = null;
		this.message = null;
	}
	
	public TimeException(String message) {
		this.exception = null;
		this.message = message;
	}
	
	public TimeException(Exception exception) {
		this.exception = exception;
		this.message = null;
	}
	
	public TimeException(Exception exception, String message) {
		this.exception = exception;
		this.message = message;
	}
	
	// Look

	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
	/** A short text message that describes what happened, or null if none. */
	public final String message;
}
