package org.zootella.time;

public class Duration {
	
	//TODO rename Life, with birth and death

	/** Make a new Duration that will record the given start time to right now. */
	public Duration(Now start) {
		this(start, new Now()); // Call the next constructor
	}

	/** Make a new Duration to record the given start and stop times. */
	public Duration(Now start, Now stop) {
		if (stop.time < start.time) throw new IllegalArgumentException(); // Make sure stop is at or after start
		this.start = start;
		this.stop = stop;
	}

	/** The time when this Duration started. */
	public final Now start;
	/** The time when this Duration stopped, the same as start or afterwards. */
	public final Now stop;
	
	/** The length of this Duration in milliseconds, 1 or more. */
	public long timeSafe() {
		long time = time();
		if (time < 1) time = 1; // A 0 might end up on the bottom of a speed fraction
		return time;
	}

	/** The length of this Duration in milliseconds, 0 or more. */
	public long time() {
		return stop.time - start.time;
	}
	
	/** When this duration ended and how long it took, like "Wed 1:39p 58.023s in 278ms". */
	@Override public String toString() {
		return stop.toString() + " in " + time() + "ms";
	}
}
