package base.state;

import base.exception.TimeException;
import base.time.Now;
import base.time.Time;

/** Make and check an Egg timer to close with a TimeException when the disk or Internet made you wait for 4 seconds. */
public class Egg extends Close {
	
	public Egg(Receive receive) {
		start = new Now();
		pulse = new Pulse(receive, Time.second);
	}
	
	public final Now start;
	private final Pulse pulse;

	@Override public void close() {
		if (already()) return;
		close(pulse);
	}

	public void check() {
		if (start.expired(4 * Time.second)) throw new TimeException();
	}
}
