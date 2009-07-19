package base.state;

import base.exception.TimeException;
import base.time.Now;
import base.time.Time;

/** Make and check an Egg timer to close with a TimeException when the disk or Internet made you wait for 4 seconds. */
public class Egg extends Close {
	
	public Egg(Update up) {
		this.up = up;
		start = new Now();
		pulse = new Pulse(new MyReceive(), Time.second);
	}
	
	private final Update up;
	public final Now start;
	private final Pulse pulse;

	@Override public void close() {
		if (already()) return;
		close(pulse);
	}

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
				
			up.send();
		}
	}
	
	public void check() {
		if (start.expired(4 * Time.second)) throw new TimeException();
	}
}
