package base.exception;

/**
 * Throw a TimeException when you've waited too long for something to happen and need to give up.
 * When you catch a TimeException, try to make contact again.
 */
public class TimeException extends ProgramException {
	public TimeException() {}
	public TimeException(String message) { super(message); }
	public TimeException(String message, Throwable cause) { super(message, cause); }
	public TimeException(Throwable cause) { super(cause); }
}
