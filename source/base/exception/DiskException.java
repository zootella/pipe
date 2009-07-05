package base.exception;

/**
 * Throw a DiskException when a file or folder on the disk or a network share breaks.
 * When you catch a DiskException, give up the operation that needs the disk.
 */
public class DiskException extends MyException {
	
	// Make
	
	public DiskException() {
		this.message = null;
		this.exception = null;
	}
	
	public DiskException(String message) {
		this.message = message;
		this.exception = null;
	}
	
	public DiskException(Exception exception) {
		this.message = null;
		this.exception = exception;
	}
	
	public DiskException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}
	
	// Look

	/** A short text message that describes what happened, or null if none. */
	public final String message;
	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
}
