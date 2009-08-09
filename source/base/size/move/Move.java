package base.size.move;

import base.time.Duration;
import base.time.Now;

public class Move {

	/** Document the result of a successful blocking data transfer of 1 or more bytes. */
	public Move(Now start, long size) {
		if (size < 1) throw new IndexOutOfBoundsException();
		this.duration = new Duration(start); // Record now as the stop time
		this.size = size;
	}

	/** How long the transfer took to complete, the time before and after the blocking call that did it. */
	public final Duration duration;
	/** How many bytes we uploaded or downloaded, 1 or more. */
	public final long size;
}
