package base.exception;

/**
 * Throw a PeerException when a remote peer you're communicating with says something not allowed.
 * When you catch a PeerException, disconnect from the remote peer.
 */
public class PeerException extends RuntimeException {
	
	// Make
	
	public PeerException() {
		this.message = null;
		this.exception = null;
	}
	
	public PeerException(String message) {
		this.message = message;
		this.exception = null;
	}
	
	public PeerException(Exception exception) {
		this.message = null;
		this.exception = exception;
	}
	
	public PeerException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}
	
	// Look

	/** A short text message that describes what happened, or null if none. */
	public final String message;
	/** The Java exception that happened first and that we wrapped in this one, or null if none. */
	public final Exception exception;
}
