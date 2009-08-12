package base.encrypt.pair;

import base.data.Data;
import base.exception.ProgramException;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;

public class EncryptTask extends Close {

	public EncryptTask(Update up, Data data, Data modulus, Data publicExponent) {
		this.up = up; // We'll tell update when we're done
		this.data = data;
		this.modulus = modulus;
		this.publicExponent = publicExponent;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final Data data;
	private final Data modulus;
	private final Data publicExponent;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	public Data result() { check(exception, encrypted); return encrypted; }
	private ProgramException exception;
	private Data encrypted;
	
	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Data taskEncrypted; // References thread() can safely set

		// A separate thread will call this method
		public void thread() throws Exception {
			
			taskEncrypted = Pair.encrypt(data, modulus, publicExponent);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			encrypted = taskEncrypted;
			close(me());          // We're done
		}
	}
	private EncryptTask me() { return this; } // Give inner code a link to the outer object
}
