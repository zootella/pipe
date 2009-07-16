package base.exception;

/**
 * Throw a PlatformException when something is wrong with the computer that the program can't fix.
 * When you catch a PlatformException, tell the user to upgrade Java and then close the program.
 */
public class PlatformException extends ProgramException {
	public PlatformException() {}
	public PlatformException(String message) { super(message); }
	public PlatformException(String message, Throwable cause) { super(message, cause); }
	public PlatformException(Throwable cause) { super(cause); }
}
