package base.time;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Timer;

import base.process.Mistake;
import base.state.Close;
import base.state.Receive;

public class Delay extends Close {

	/** Make a Delay that will call receive() once shortly after the first of a bunch of send() calls. */
	public Delay(Receive receive) { this(receive, Time.delay); }
	/** Make a Delay that will call receive() once delay milliseconds after the first of a bunch of send() calls. */
	public Delay(Receive receive, long delay) {
		this.receive = receive;
		if (delay < Time.delay) delay = Time.delay; // Make sure delay isn't too fast
		timer = new Timer((int)delay, new MyActionListener());
		timer.setRepeats(false);
	}

	/** A link to the receive() method we call. */
	private final Receive receive;
	/** Our Timer that doesn't repeat. */
	private Timer timer;

	/** Close this Delay so it never calls receive() again. */
	public void close() {
		if (already()) return;
		timer.stop(); // Stop and discard timer, keeping it might prevent the program from closing
		timer = null;
	}

	/**
	 * Have this Update call the receive() method you gave it in a separate event after a short delay.
	 * Call send() several times in the delay time, and receive() will only happen once.
	 */
	public void send() {
		if (closed()) return; // Do nothing once closed
		if (set) return;      // We're already set to go off
		timer.start();        // Set the timer to go off once after its delay
		set = true;
	}
	
	/** true when we've set timer to go off, and it hasn't yet. */
	private boolean set;

	// When timer goes off, Java calls this method
	private class MyActionListener extends AbstractAction {
		public void actionPerformed(ActionEvent a) {
			try {
				if (closed()) return;                  // Don't let a closed Delay call receive()
				set = false;                           // Let the next call to send() go through
				receive.receive();                     // Call our given receive() method
			} catch (Exception e) { Mistake.stop(e); } // Stop the program for an Exception we didn't expect
		}
	}
}
