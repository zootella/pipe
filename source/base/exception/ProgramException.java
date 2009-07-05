package base.exception;

/**
 * Throw a ProgramException when something has gone wrong in the program that restarting the program might fix.
 * When you catch a ProgramException, restart the program.
 */
public class ProgramException extends MyException {
	
	// Make
	
	public ProgramException() {
		this.message = null;
		this.exception = null;
	}
	
	public ProgramException(String message) {
		this.message = message;
		this.exception = null;
	}
	
	public ProgramException(Exception exception) {
		this.message = null;
		this.exception = exception;
	}
	
	public ProgramException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}
	
	// Look

	/** A short text message that describes what happened, or null if none. */
	public final String message;
	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
}
