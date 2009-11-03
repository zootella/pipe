package org.zootella.encrypt.pair;

import org.zootella.data.Data;
import org.zootella.exception.ProgramException;
import org.zootella.state.Close;
import org.zootella.state.Task;
import org.zootella.state.TaskBody;
import org.zootella.state.Update;

public class DecryptTask extends Close {

	public DecryptTask(Update up, Data data, PairKey key) {
		this.up = up; // We'll tell update when we're done
		this.data = data;
		this.key = key;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final Data data;
	private final PairKey key;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	public Data result() { check(exception, decrypted); return decrypted; }
	private ProgramException exception;
	private Data decrypted;
	
	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Data taskDecrypted; // References thread() can safely set

		// A separate thread will call this method
		public void thread() {
			
			taskDecrypted = Pair.decrypt(data, key);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			decrypted = taskDecrypted;
			close(DecryptTask.this); // We're done
		}
	}
}
