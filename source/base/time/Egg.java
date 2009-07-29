package base.time;

import base.exception.TimeException;
import base.state.Close;
import base.state.Receive;

/** Make and check an Egg timer to close with a TimeException when the disk or network made you wait for 4 seconds. */
public class Egg extends Close {
	
	public Egg(Receive receive) {
		start = new Now();
		pulse = new Pulse(receive);
	}
	
	public final Now start;
	private final Pulse pulse;

	@Override public void close() {
		if (already()) return;
		close(pulse);
	}

	public void check() {
		if (start.expired(Time.out)) throw new TimeException();
	}
}
