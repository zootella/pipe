package base.state;

import javax.swing.SwingUtilities;

import base.process.Mistake;

/** Make a Task to run some code in a separate thread. */
public class Task extends Close {
	
	// Make

	/** Make a Task to have a separate thread run the code in body now. */
	public Task(TaskBody body) {
		this.body = body;
		thread = new Thread(new ThreadRun(), "Task"); // Name thread "Task"
		thread.setDaemon(true); // Let the program close even if thread is still running
		thread.start(); // Have thread call ThreadRun.run() below now
	}

	private final TaskBody body;
	private Thread thread;
	
	@Override public void close() {
		if (already()) return;
		if (thread != null) // If thread is running, have it throw an Exception
			thread.interrupt();
	}
	
	// Run
	
	// When the constructor makes thread above, thread calls the run() method here
	private class ThreadRun implements Runnable {
		public void run() {
			try {
				body.thread(); // Call the code we were given
			} catch (Exception e) { exception = e; } // Catch and save any exception it throws
			SwingUtilities.invokeLater(new EventRun()); // We're done, send an event
		} // When thread exits run(), it closes
	}	
	
	private Exception exception;

	// Soon after thread calls invokeLater() above, the normal event thread calls run() here
	private class EventRun implements Runnable {
		public void run() {
			if (closed()) return;     // Do nothing once closed
			try {
				thread = null;        // thread is done and exited, null our reference to it
				close(me());          // Mark this Task closed
				body.done(exception); // Call the given done() method with the ProgramException we got
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	private final Task me() { return this; } // Give inner code a link to this outer object
}
