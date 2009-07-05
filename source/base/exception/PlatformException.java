package base.exception;

/**
 * Throw a PlatformException when something is wrong with the computer that the program can't fix.
 * When you catch a PlatformException, tell the user to upgrade Java and then close the program.
 */
public class PlatformException extends MyException {
	
	// Make
	
	public PlatformException() {
		this.message = null;
		this.exception = null;
	}
	
	public PlatformException(String message) {
		this.message = message;
		this.exception = null;
	}
	
	public PlatformException(Exception exception) {
		this.message = null;
		this.exception = exception;
	}
	
	public PlatformException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}
	
	// Look

	/** A short text message that describes what happened, or null if none. */
	public final String message;
	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
}
