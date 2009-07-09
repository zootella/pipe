package base.time;

/** An Ago object records how long ago something most recently happened, if ever. */
public class Ago {

	// Set

	/** Remember the time right now. */
	public void set() {
		time = Time.now();
	}

	/**
	 * true if we're recording how long it's been since we were last set.
	 * false if we've never been set.
	 */
	public boolean isSet() {
		return time != 0; // If we haven't been set yet, time will still be 0
	}
	
	// Ago
	
	/**
	 * The number of milliseconds between January 1970 and when we were most recently set.
	 * 0 if we haven't been set yet.
	 */
	public long ago() {
		if (time == 0) throw new IllegalStateException(); // Make sure we were set
		return time;
	}
	private long time;

	/**
	 * Determine if a given amount of time has passed since we were most recently set.
	 * expired(0) always returns true because at least that much time as expired.
	 * 
	 * @param milliseconds The time that needs to have passed, as a number of milliseconds
	 * @param never        If this Time has never been set, return this boolean value
	 * @return             true if milliseconds have passed, false if they haven't yet, or never if we were never set
	 */
	public boolean expired(long milliseconds, boolean never) {
		if (milliseconds < 0) throw new IllegalArgumentException(); // Make sure milliseconds is 0 or more
		if (time == 0) return never; // If we were never set, return never instead of throwing an IllegalStateException
		return expired() >= milliseconds; // Return true if the requested number of milliseconds, or more, have passed 
	}
	
	/**
	 * Find out how many milliseconds have passed since we were last set.
	 * If you call this right after set() it will return 0 because no milliseconds have passed yet.
	 * 
	 * @throws IllegalStateException if this Time object has never been set
	 */
	public long expired() {
		if (time == 0) throw new IllegalStateException(); // Make sure we were set
		return Time.now() - time; // Compare the time now to what it was then
	}
}
