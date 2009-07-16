package base.exception;

/** Custom exceptions thrown and caught by the program code extend ProgramException. */
public class ProgramException extends RuntimeException {
	public ProgramException() {}
	public ProgramException(String message) { super(message); }
	public ProgramException(String message, Throwable cause) { super(message, cause); }
	public ProgramException(Throwable cause) { super(cause); }
}
