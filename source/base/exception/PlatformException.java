package base.exception;

/**
 * Throw a PlatformException when something is wrong with the computer that the program can't fix.
 * When you catch a PlatformException, tell the user to upgrade Java and then close the program.
 */
public class PlatformException extends ProgramException {
	
	// Make
	
	public PlatformException() {
		this.exception = null;
		this.message = null;
	}
	
	public PlatformException(String message) {
		this.exception = null;
		this.message = message;
	}
	
	public PlatformException(Exception exception) {
		this.exception = exception;
		this.message = null;
	}
	
	public PlatformException(Exception exception, String message) {
		this.exception = exception;
		this.message = message;
	}
	
	// Look

	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
	/** A short text message that describes what happened, or null if none. */
	public final String message;
}
