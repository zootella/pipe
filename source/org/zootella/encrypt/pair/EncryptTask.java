package org.zootella.encrypt.pair;

import org.zootella.data.Data;
import org.zootella.exception.ProgramException;
import org.zootella.state.Close;
import org.zootella.state.Task;
import org.zootella.state.TaskBody;
import org.zootella.state.Update;

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
		public void thread() {
			
			taskEncrypted = Pair.encrypt(data, modulus, publicExponent);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			encrypted = taskEncrypted;
			close(EncryptTask.this); // We're done
		}
	}
}
