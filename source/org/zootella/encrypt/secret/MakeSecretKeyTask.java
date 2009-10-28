package org.zootella.encrypt.secret;

import org.zootella.exception.ProgramException;
import org.zootella.state.Close;
import org.zootella.state.Task;
import org.zootella.state.TaskBody;
import org.zootella.state.Update;

public class MakeSecretKeyTask extends Close {

	public MakeSecretKeyTask(Update up) {
		this.up = up; // We'll tell update when we're done
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	public KeySecret result() { check(exception, key); return key; }
	private ProgramException exception;
	private KeySecret key;
	
	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private KeySecret taskKey; // References thread() can safely set

		// A separate thread will call this method
		public void thread() {

			taskKey = Secret.make();
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			key = taskKey;
			close(me());          // We're done
		}
	}
	private MakeSecretKeyTask me() { return this; } // Give inner code a link to the outer object
}
