package base.net.flow;

import base.data.Bin;
import base.exception.ProgramException;
import base.net.socket.Socket;
import base.size.Range;
import base.size.move.Move;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;

public class DownloadTask extends Close {
	
	// Make

	/** Download 1 or more bytes from socket to bin limited by range, don't look at bin until this is closed. */
	public DownloadTask(Update up, Socket socket, Range range, Bin bin) {
		this.up = up; // We'll tell above when we're done
		this.socket = socket;
		this.range = range;
		this.bin = bin;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final Socket socket;
	private final Range range;
	private final Bin bin;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	/** How much data we downloaded and how long it took, or throws the exception that made us give up. */
	public Move result() { check(exception, move); return move; }
	private ProgramException exception;
	private Move move;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Move taskMove; // References thread() can safely set

		// A separate thread will call this method
		public void thread() throws Exception {
				
			// Download 1 or more bytes from socket to bin
			taskMove = bin.download(socket, range);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			move = taskMove;
			close(me());          // We're done
		}
	}
	private DownloadTask me() { return this; } // Give inner code a link to the outer object
}
