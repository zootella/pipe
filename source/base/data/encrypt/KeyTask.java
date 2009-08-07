package base.data.encrypt;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import base.data.Data;
import base.exception.PlatformException;
import base.exception.ProgramException;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;

public class KeyTask extends Close {

	public KeyTask(Update up, String algorithm, int size) {
		this.up = up; // We'll tell update when we're done
		this.algorithm = algorithm;
		this.size = size;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final String algorithm;
	private final int size;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	/** How much we hashed when we're done, or throws the exception that made us give up. */
	public Data result() { check(exception, data); return data; }
	private ProgramException exception;
	private Data data;
	
	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Data taskData; // References thread() can safely set

		// A separate thread will call this method
		public void thread() throws Exception {

			try {
				KeyGenerator g = KeyGenerator.getInstance(algorithm); // Very slow the first time
				g.init(size);
				SecretKey k = g.generateKey();
				taskData = new Data(k.getEncoded());
			}
			catch (NoSuchAlgorithmException e) { throw new PlatformException(e); }
			catch (InvalidParameterException e) { throw new PlatformException(e); }
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			data = taskData;
			close(me());          // We're done
		}
	}
	private KeyTask me() { return this; } // Give inner code a link to the outer object
}
