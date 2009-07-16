package base.exception;

/**
 * Throw a DiskException when a file or folder on the disk or a network share breaks.
 * When you catch a DiskException, give up the operation that needs the disk.
 */
public class DiskException extends ProgramException {
	
	// Make
	
	public DiskException() {
		this.exception = null;
		this.message = null;
	}
	
	public DiskException(String message) {
		this.exception = null;
		this.message = message;
	}
	
	public DiskException(Exception exception) {
		this.exception = exception;
		this.message = null;
	}
	
	public DiskException(Exception exception, String message) {
		this.exception = exception;
		this.message = message;
	}
	
	// Look

	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
	/** A short text message that describes what happened, or null if none. */
	public final String message;
}
