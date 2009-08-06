package base.time;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Timer;

import base.process.Mistake;
import base.state.Close;
import base.state.Update;

public class CenterPulse extends Close {
	//TODO this is a bad idea and doesn't match the rest of the program
	//or, add these features
	//give it a little object which has the Update, the interval in units of fifth of a second, repeats or not, and right now or not

	/*
	public class Instruction {
		public Instruction(Update update, boolean repeat, long interval) {
			this.update = update;
			this.repeat = repeat;
			this.interval = interval;
			
		}
		public final Update update;
		public final boolean now;
		public final boolean repeat;
		public final long interval;
	}
	no, all of this is coded in java already
	*/
	
	
	public CenterPulse(Update u) {
		update = u;
		synchronized (o) {
			if (list.isEmpty()) {
				timer = new Timer((int)Time.delay, new MyActionListener());
				timer.setRepeats(true);
				timer.start();
			}
			list.add(u);
		}
	}
	
	private final Update update;
	
	@Override public void close() {
		if (already()) return;
		try {
			synchronized (o) {
				list.remove(update);
				if (list.isEmpty()) {
					timer.stop(); // Stop and discard timer, keeping it might prevent the program from closing
					timer = null;
				}
			}
		} catch (Exception e) { Mistake.log(e); }
	}
	
	private class MyActionListener extends AbstractAction {
		public void actionPerformed(ActionEvent a) {
			try {
				for (Update u : list)
					u.send();
			} catch (Exception e) { Mistake.stop(e); } // Stop the program for an Exception we didn't expect
		}
	}
	
	private static final Object o = new Object();
	private static final Set<Update> list = new HashSet<Update>();
	private static Timer timer;
}
