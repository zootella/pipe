package base.size.move;

import base.time.Duration;
import base.time.Now;

public class MorphMove {

	/** Document the result of a successful blocking data transformation that turned consumed bytes into produced bytes, both must be 1 or more. */
	public MorphMove(Now start, long consumed, long produced) {
		if (consumed < 1 || produced < 1) throw new IndexOutOfBoundsException();
		this.duration = new Duration(start); // Record now as the stop time
		this.consumed = consumed;
		this.produced = produced;
	}

	/** How long the transformation took to complete, the time before and after the blocking call that did it. */
	public final Duration duration;
	/** How many bytes of input data we consumed, 1 or more. */
	public final long consumed;
	/** How many bytes of output data we produced, 1 or more. */
	public final long produced;
}
