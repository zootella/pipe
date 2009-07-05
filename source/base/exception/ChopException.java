package base.exception;

/**
 * Throw a ChopException when you're parsing data and run out because the end of the data has been chopped off.
 * When you catch a ChopException, try parsing into the data again later after more has arrived.
 */
public class ChopException extends MyException {
	
	// Make
	
	public ChopException() {
		this.message = null;
		this.exception = null;
	}
	
	public ChopException(String message) {
		this.message = message;
		this.exception = null;
	}
	
	public ChopException(Exception exception) {
		this.message = null;
		this.exception = exception;
	}
	
	public ChopException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}
	
	// Look

	/** A short text message that describes what happened, or null if none. */
	public final String message;
	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
}
