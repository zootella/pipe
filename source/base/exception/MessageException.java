package base.exception;

/**
 * Throw a MessageException when you're parsing a message and come to a mistake.
 * When you catch a MessageException, skip that message and try parsing the next one, or disconnect from the peer.
 */
public class MessageException extends RuntimeException {
	
	// Make
	
	public MessageException() {
		this.message = null;
		this.exception = null;
	}
	
	public MessageException(String message) {
		this.message = message;
		this.exception = null;
	}
	
	public MessageException(Exception exception) {
		this.message = null;
		this.exception = exception;
	}
	
	public MessageException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}
	
	// Look

	/** A short text message that describes what happened, or null if none. */
	public final String message;
	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
}
