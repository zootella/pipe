package org.zootella.state;

import javax.swing.SwingUtilities;

import org.zootella.exception.ProgramException;
import org.zootella.process.Mistake;

/** Make a Task to run some code in a separate thread. */
public class Task extends Close {
	
	// Make

	/** Make a Task to have a separate thread run the code in body now. */
	public Task(TaskBody body) {
		this.body = body;
		thread = new Thread(new ThreadRun(), "Task"); // Name thread "Task"
		thread.setDaemon(true); // Let the program close even if thread is still running
		thread.start(); // Have the new thread call run() below now
	}

	private final TaskBody body;
	private Thread thread;
	
	/** Interrupt our Task thread. */
	@Override public void close() {
		if (already()) return;
		if (thread != null) // If thread is running
			thread.interrupt(); // Stop it by making it throw an exception
	}
	
	// Run
	
	// When the constructor makes thread above, thread calls the run() method here
	private class ThreadRun implements Runnable {
		public void run() {
			try { body.thread(); } // Call the code we were given
			catch (ProgramException e) { programException = e; } // A ProgramException we expect and save
			catch (Throwable t) { throwable = t; } // An exception isn't expected, and stops the program
			SwingUtilities.invokeLater(new EventRun()); // We're done, send an event
		} // When thread exits run(), it closes
	}	
	
	private ProgramException programException;
	private Throwable throwable;

	// Soon after thread calls invokeLater() above, the normal event thread calls run() here
	private class EventRun implements Runnable {
		public void run() {
			if (closed()) return;                           // Do nothing once closed
			if (throwable != null) Mistake.stop(throwable); // An exception isn't expected, stop the program
			try {
				thread = null;                              // thread is done and exited, null our reference to it
				close(Task.this);                           // Mark this Task closed
				body.done(programException);                // Call the given done() method giving it the ProgramException we got
			} catch (Throwable t) { Mistake.stop(t); }      // Stop the program for an exception we didn't expect
		}
	}
}
