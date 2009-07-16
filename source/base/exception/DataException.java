package base.exception;

/**
 * Throw a DataException when you're looking at some data or text and find a mistake.
 * When you catch a DataException, skip that message and try parsing the next one, or disconnect from the peer.
 */
public class DataException extends ProgramException {
	public DataException() {}
	public DataException(String message) { super(message); }
	public DataException(String message, Throwable cause) { super(message, cause); }
	public DataException(Throwable cause) { super(cause); }
}
