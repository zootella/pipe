package base.encrypt.hash;

import base.data.Bin;
import base.exception.ProgramException;
import base.size.Range;
import base.size.move.Move;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;
import base.time.Now;

public class HashTask extends Close {

	/** SHA1 hash and clear bin's data with the given Hash object, don't look at hash or bin until this is closed. */
	public HashTask(Update up, Hash hash, Bin bin, Range range) {
		this.up = up; // We'll tell update when we're done
		this.hash = hash;
		this.bin = bin;
		this.range = range;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final Hash hash;
	private final Bin bin;
	private final Range range;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	/** How much we hashed when we're done, or throws the exception that made us give up. */
	public Move result() { check(exception, move); return move; }
	private ProgramException exception;
	private Move move;

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Move taskMove; // References thread() can safely set

		// A separate thread will call this method
		public void thread() throws Exception {
			
			// Hash data from bin and remove it
			Now start = new Now();
			int ask = range.ask(bin.size());
			hash.add(bin.data().begin(ask));
			bin.remove(ask);
			taskMove = new Move(start, ask);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			move = taskMove;
			close(me());          // We're done
		}
	}
	private HashTask me() { return this; } // Give inner code a link to the outer object
}
