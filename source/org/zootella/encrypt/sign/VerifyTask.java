package org.zootella.encrypt.sign;

import org.zootella.data.Data;
import org.zootella.exception.ProgramException;
import org.zootella.state.Close;
import org.zootella.state.Task;
import org.zootella.state.TaskBody;
import org.zootella.state.Update;

public class VerifyTask extends Close {

	public VerifyTask(Update up, Data data, Data signature, SignKey key) {
		this.up = up; // We'll tell update when we're done
		this.data = data;
		this.signature = signature;
		this.key = key;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final Data data;
	private final Data signature;
	private final SignKey key;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	public Boolean result() { check(exception, valid); return valid; }
	private ProgramException exception;
	private Boolean valid;
	
	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Boolean taskValid; // References thread() can safely set

		// A separate thread will call this method
		public void thread() {
			
			taskValid = Sign.verify(data, signature, key);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			valid = taskValid;
			close(VerifyTask.this); // We're done
		}
	}
}
