package org.zootella.state;

import javax.swing.SwingUtilities;

import org.zootella.process.Mistake;

public class Update {
	
	// Make

	/** Make an Update that will have a separate event call receive() once soon after several send() calls. */
	public Update(Receive receive) {
		this.receive = receive;
		spin = new Spin();
	}
	
	/** A link to the receive() method we call. */
	private final Receive receive;
	/** Our Spin object that detects if we go too fast. */
	private final Spin spin;
	
	// Send and receive

	/**
	 * Have this Update call the receive() method you gave it in a separate event.
	 * Call send() several times in a row, and receive() will only happen once.
	 */
	public void send() {
		if (set) return; // We're already set to go off
		SwingUtilities.invokeLater(new MyRunnable()); // Have Java call run() below separately and soon
		set = true;
	}
	
	/** true when we've set Java to call run(), and it hasn't yet. */
	private boolean set;

	// Soon after send() above calls SwingUtilities.invokeLater(), Java calls this run() method
	private class MyRunnable implements Runnable {
		public void run() {
			try {
				set = false;                           // Let the next call to send() go through
				spin.count();                          // Make sure we haven't been doing this too frequently
				receive.receive();                     // Call our given receive() method
			} catch (Throwable t) { Mistake.stop(t); } // Stop the program for an exception we didn't expect
		}
	}

	// Thread

	/** Only the normal event thread can call send(), if you're in a Task thread or something else, use sendFromThread(). */
	public void sendFromThread() {
		SwingUtilities.invokeLater(new Runnable() { // Have the normal Swing thread call this run() method
			public void run() {
				try {
					send();
				} catch (Throwable t) { Mistake.stop(t); } // Stop the program for an exception we didn't expect
			}
		});
	}
}
