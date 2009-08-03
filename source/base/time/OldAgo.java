package base.time;

/** An Ago object records how long ago something most recently happened, if ever. */
public class OldAgo {
	
	// Inside
	
	/** The time in milliseconds since January 1970 and when we were last set, 0 if we've never been set. */
	private long time;

	// Set

	/** Remember the time right now. */
	public void set() {
		time = Time.now();
	}
	
	// Look

	/** true when this Ago has been set at least once. */
	public boolean isSet() {
		return time != 0; // If we haven't been set yet, time will still be 0
	}

	/** the time we were last set, IllegalStateException if we've never been set. */
	public long time() {
		if (time == 0) throw new IllegalStateException(); // Make sure we were set
		return time;
	}
	
	/** how long it's been since we were last set, IllegalStateException if we've never been set. */
	public long expired() {
		if (time == 0) throw new IllegalStateException(); // Make sure we were set
		return Time.now() - time; // Compare the time now to what it was then
	}

	/** true if it's been milliseconds or more since we've been most recently sent, never if we've never been set. */
	public boolean expired(long milliseconds, boolean never) {
		if (milliseconds < 0) throw new IllegalArgumentException(); // Make sure milliseconds is 0 or more
		if (time == 0) return never; // If we were never set, return never instead of throwing an IllegalStateException
		return expired() >= milliseconds; // Return true if the requested number of milliseconds, or more, have passed 
	}
}
