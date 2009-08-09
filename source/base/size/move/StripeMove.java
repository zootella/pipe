package base.size.move;

import base.size.Stripe;
import base.time.Duration;
import base.time.Now;

public class StripeMove {

	/** Document the result of a successful blocking data transfer of 1 or more bytes at index i. */
	public StripeMove(Now start, long i, long size) {
		this.duration = new Duration(start); // Record now as the stop time
		this.stripe = new Stripe(i, size);
	}

	/** How long the transfer took to complete, the time before and after the blocking call that did it. */
	public final Duration duration;
	/** The Stripe we read, or wrote, index 0 start of file, size 1 or more. */
	public final Stripe stripe;
}
