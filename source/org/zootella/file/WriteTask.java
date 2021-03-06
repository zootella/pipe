package org.zootella.file;

import org.zootella.data.Bin;
import org.zootella.exception.ProgramException;
import org.zootella.size.Range;
import org.zootella.size.move.StripeMove;
import org.zootella.state.Close;
import org.zootella.state.Task;
import org.zootella.state.TaskBody;
import org.zootella.state.Update;

public class WriteTask extends Close {
	
	// Make

	/** Write 1 or more bytes from bin to range in file, don't look at bin until this is closed. */
	public WriteTask(Update up, File file, Range range, Bin bin) {
		this.up = up; // We'll tell above when we're done
		this.file = file;
		this.range = range;
		this.bin = bin;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final File file;
	public final Range range;
	private final Bin bin;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	/** How much of stripe we wrote and how long it took, or throws the exception that made us give up. */
	public StripeMove result() { check(exception, move); return move; }
	private ProgramException exception;
	private StripeMove move;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private StripeMove taskMove; // References thread() can safely set

		// A separate thread will call this method
		public void thread() {
				
			// Read 1 or more bytes from stripe in file to bin
			taskMove = bin.write(file, range);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return;  // Don't let anything change if we're already closed
			exception = e;         // Get the exception our code above threw
			move = taskMove;
			file.add(move.stripe); // Record the Stripe of data we wrote in file's StripePattern
			close(WriteTask.this); // We're done
		}
	}
}
