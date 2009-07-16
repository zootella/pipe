package base.exception;

/**
 * Throw a DataException when you're looking at some data or text and find a mistake.
 * When you catch a DataException, skip that message and try parsing the next one, or disconnect from the peer.
 */
public class DataException extends ProgramException {
	
	// Make
	
	public DataException() {
		this.exception = null;
		this.message = null;
	}
	
	public DataException(String message) {
		this.exception = null;
		this.message = message;
	}
	
	public DataException(Exception exception) {
		this.exception = exception;
		this.message = null;
	}
	
	public DataException(Exception exception, String message) {
		this.exception = exception;
		this.message = message;
	}
	
	// Look

	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
	/** A short text message that describes what happened, or null if none. */
	public final String message;
}
