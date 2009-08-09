package base.size.move;

import base.time.Duration;
import base.time.Now;

public class MorphMove {

	/** Document the result of a successful blocking data transformation that turned before bytes into after bytes, both must be 1 or more. */
	public MorphMove(Now start, long before, long after) {
		if (before < 1 || after < 1) throw new IndexOutOfBoundsException();
		this.duration = new Duration(start); // Record now as the stop time
		this.before = before;
		this.after = after;
	}

	/** How long the transformation took to complete, the time before and after the blocking call that did it. */
	public final Duration duration;
	/** How many bytes of input data we processed, 1 or more. */
	public final long before;
	/** How many bytes of output data we produced, 1 or more. */
	public final long after;
}
