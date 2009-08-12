package base.encrypt.pair;

import base.data.Data;
import base.exception.ProgramException;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;

public class DecryptTask extends Close {

	public DecryptTask(Update up, Data data, Data modulus, Data privateExponent) {
		this.up = up; // We'll tell update when we're done
		this.data = data;
		this.modulus = modulus;
		this.privateExponent = privateExponent;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final Data data;
	private final Data modulus;
	private final Data privateExponent;
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
		public void thread() throws Exception {
			
			taskDecrypted = Pair.decrypt(data, modulus, privateExponent);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			decrypted = taskDecrypted;
			close(me());          // We're done
		}
	}
	private DecryptTask me() { return this; } // Give inner code a link to the outer object
}
