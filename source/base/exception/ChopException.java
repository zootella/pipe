package base.exception;

/**
 * Throw a ChopException when you're parsing data and run out because the end of the data has been chopped off.
 * When you catch a ChopException, try parsing into the data again later after more has arrived.
 * A ChopException is a special kind of DataException, it says the data is bad because it's not all here yet.
 */
public class ChopException extends DataException {
	
	// Make
	
	public ChopException() {}
	
	public ChopException(String message) { super()
		this.exception = null;
		this.message = message;
	}
	
	public ChopException(Exception exception) {
		this.exception = exception;
		this.message = null;
	}
	
	public ChopException(Exception exception, String message) {
		this.exception = exception;
		this.message = message;
	}
	
	// Look
}
