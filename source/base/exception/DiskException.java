package base.exception;

/**
 * Throw a DiskException when a file or folder on the disk or a network share breaks.
 * When you catch a DiskException, give up the operation that needs the disk.
 */
public class DiskException extends ProgramException {
	public DiskException() {}
	public DiskException(String message) { super(message); }
	public DiskException(String message, Throwable cause) { super(message, cause); }
	public DiskException(Throwable cause) { super(cause); }
}
