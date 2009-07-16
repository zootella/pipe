package base.exception;

/**
 * Throw a NetException when Internet communications are blocked or interrupted.
 * When you catch a NetException, try again with a new connection or message.
 */
public class NetException extends ProgramException {
	public NetException() {}
	public NetException(String message) { super(message); }
	public NetException(String message, Throwable cause) { super(message, cause); }
	public NetException(Throwable cause) { super(cause); }
}
