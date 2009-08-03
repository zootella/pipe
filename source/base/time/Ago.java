package base.time;

public class Ago {

	public Ago() { this.interval = Time.out; }
	
	public Ago(long interval) { this.interval = interval; }
	
	public final long interval;
	
	/** The time in milliseconds since January 1970 and when we were last set, 0 if we've never been set. */
	private long set;
	
	public boolean enough() {
		if (set + interval < Time.now()) {
			set = Time.now();
			return true;
		} else {
			return false;
		}
	}
}
