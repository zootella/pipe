package org.zootella.net.upnp.task;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.zootella.exception.ProgramException;
import org.zootella.net.upnp.Do;
import org.zootella.state.Close;
import org.zootella.state.Task;
import org.zootella.state.TaskBody;
import org.zootella.state.Update;

public class StartTask extends Close {
	
	// Make

	public StartTask(Update up, DeviceChangeListener listen) {
		this.up = up; // We'll tell above when we're done
		this.listen = listen;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final DeviceChangeListener listen;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	public ControlPoint result() { check(exception, control); return control; }
	private ProgramException exception;
	private ControlPoint control;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private ControlPoint taskControl; // References thread() can safely set

		// A separate thread will call this method
		public void thread() {

			taskControl = Do.start(listen);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			control = taskControl;
			close(StartTask.this); // We're done
		}
	}
}
