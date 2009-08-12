package base.encrypt.pair;

import base.exception.ProgramException;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;

public class MakeKeyTask extends Close {

	public MakeKeyTask(Update up) {
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

	public KeyData result() { check(exception, key); return key; }
	private ProgramException exception;
	private KeyData key;
	
	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private KeyData taskKey; // References thread() can safely set

		// A separate thread will call this method
		public void thread() throws Exception {
			
			taskKey = Pair.make();
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			key = taskKey;
			close(me());          // We're done
		}
	}
	private MakeKeyTask me() { return this; } // Give inner code a link to the outer object
}
