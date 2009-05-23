package base.exception;

/**
 * Throw a PlatformException when something is wrong with the computer that the program can't fix.
 * When you catch a PlatformException, tell the user to upgrade Java and then close the program.
 */
public class PlatformException extends RuntimeException {
	
	public final Exception exception;
	public final String message;
	
	
	public PlatformException() {
		exception = null;
		message = null;
	}
	
	public PlatformException(Exception e) {
		exception = e;
		message = null;
	}
	
	public PlatformException(Exception e, String m) {
		exception = e;
		message = m;
	}
	
	public PlatformException(String m) {
		exception = null;
		message = m;
	}
	
	
	
}
