package org.zootella.time;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Timer;

import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.Receive;
import org.zootella.state.Update;

public class Pulse extends Close {

	/** Make a Pulse that will call the given receive() method 5 times a second. */
	public Pulse(Receive receive) { this(receive, Time.delay); }
	/** Make a Pulse that will call the given receive() method every delay milliseconds. */
	public Pulse(Receive receive, long delay) {
		this.receive = receive;
		if (delay < Time.delay) delay = Time.delay; // Make sure delay isn't too fast
		timer = new Timer((int)delay, new MyActionListener());
		timer.setRepeats(true);
		timer.start();
		
		update = new Update(receive);
	}
	
	/** A link to the receive() method we call. */
	private final Receive receive;
	/** Our Timer set to repeat. */
	private Timer timer;

	/** Close this Pulse so it never calls receive() again. */
	public void close() {
		if (already()) return;
		timer.stop(); // Stop and discard timer, keeping it might prevent the program from closing
		timer = null;
	}

	// When timer goes off, Java calls this method
	private class MyActionListener extends AbstractAction {
		public void actionPerformed(ActionEvent a) {
			try {
				if (closed()) return;                  // Don't let a closed Pulse call receive()
				receive.receive();                     // Call our given receive() method
			} catch (Throwable t) { Mistake.stop(t); } // Stop the program for an exception we didn't expect
		}
	}
	
	

	//TODO added now
	private final Update update;
	public void now() {
		update.send();
	}
}
