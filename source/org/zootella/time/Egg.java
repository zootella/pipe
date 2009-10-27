package org.zootella.time;

import org.zootella.exception.TimeException;
import org.zootella.state.Close;
import org.zootella.state.Receive;

/** Make and check an Egg timer to close with a TimeException when the disk or network made you wait for 4 seconds. */
public class Egg extends Close {
	
	public Egg(Receive receive) { this(receive, Time.out); }
	public Egg(Receive receive, long delay) {
		start = new Now();
		this.delay = delay;
		pulse = new Pulse(receive);
	}
	
	public final Now start;
	public final long delay;
	private final Pulse pulse;

	@Override public void close() {
		if (already()) return;
		close(pulse);
	}

	public void check() {
		if (start.expired(delay)) throw new TimeException();
	}
}
