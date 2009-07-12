package base.exception;

/**
 * Throw a DataException when you're looking at some data or text and find a mistake.
 * When you catch a DataException, skip that message and try parsing the next one, or disconnect from the peer.
 */
public class DataException extends ProgramException {
	
	// Make
	
	public DataException() {
		this.message = null;
		this.exception = null;
	}
	
	public DataException(String message) {
		this.message = message;
		this.exception = null;
	}
	
	public DataException(Exception exception) {
		this.message = null;
		this.exception = exception;
	}
	
	public DataException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}
	
	// Look

	/** A short text message that describes what happened, or null if none. */
	public final String message;
	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
}
