package base.exception;

/**
 * Throw a TransferException when data transfer didn't work and you're not sure if it's the fault of the disk or the network.
 * When you catch a TransferException, try to figure out which one it was.
 */
public class TransferException extends MyException {
	
	// Make
	
	public TransferException() {
		this.message = null;
		this.exception = null;
	}
	
	public TransferException(String message) {
		this.message = message;
		this.exception = null;
	}
	
	public TransferException(Exception exception) {
		this.message = null;
		this.exception = exception;
	}
	
	public TransferException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}
	
	// Look

	/** A short text message that describes what happened, or null if none. */
	public final String message;
	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
}
