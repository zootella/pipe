package base.file;

import base.data.Bin;
import base.exception.ProgramException;
import base.size.Range;
import base.size.StripePattern;
import base.size.move.StripeMove;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;

public class ReadTask extends Close {
	
	// Make

	/** Read 1 or more bytes from stripe in file to bin, don't look at bin until this is closed. */
	public ReadTask(Update up, File file, Range range, Bin bin) {
		this.up = up; // We'll tell above when we're done
		this.file = file;
		this.pattern = file.pattern(); // Get the StripePattern that shows where file has data
		this.range = range;
		this.bin = bin;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final File file;
	private final StripePattern pattern;
	public final Range range;
	private final Bin bin;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	/** How much of stripe we read and how long it took, or throws the exception that made us give up. */
	public StripeMove result() { check(exception, move); return move; }
	private ProgramException exception;
	private StripeMove move;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private StripeMove taskMove; // References thread() can safely set

		// A separate thread will call this method
		public void thread() throws Exception {
				
			// Read 1 or more bytes from stripe in file to bin
			taskMove = bin.read(file, pattern, range);
		}
		
		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			move = taskMove;
			close(me());          // We're done
		}
	}
	private ReadTask me() { return this; } // Give inner code a link to the outer object
}
