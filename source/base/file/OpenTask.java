package base.file;

import base.exception.ProgramException;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;

public class OpenTask extends Close {
	
	// Make

	/** Open a file on the disk. */
	public OpenTask(Update up, Open open) {
		this.up = up; // We'll tell above when we're done
		this.open = open;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	public final Open open;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	/** The File we opened, or throws the exception that made us give up. */
	public File result() { check(exception, file); return file; }
	private ProgramException exception;
	private File file;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private File taskFile; // References thread() can safely set

		// A separate thread will call this method
		public void thread() throws Exception {
				
			// Open the file
			taskFile = new File(open);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			file = taskFile;
			close(me());          // We're done
		}
	}
	private OpenTask me() { return this; } // Give inner code a link to the outer object
}
