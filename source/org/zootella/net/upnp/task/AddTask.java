package org.zootella.net.upnp.task;

import org.zootella.exception.ProgramException;
import org.zootella.net.upnp.Access;
import org.zootella.net.upnp.Do;
import org.zootella.net.upnp.name.Map;
import org.zootella.net.upnp.name.MapResult;
import org.zootella.state.Close;
import org.zootella.state.Task;
import org.zootella.state.TaskBody;
import org.zootella.state.Update;

public class AddTask extends Close {
	
	// Make

	public AddTask(Update up, Access router, Map forward) {
		this.up = up; // We'll tell above when we're done
		this.router = router;
		this.forward = forward;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final Access router;
	private final Map forward;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	public MapResult result() { check(exception, result); return result; }
	private ProgramException exception;
	private MapResult result;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private MapResult taskResult; // References thread() can safely set

		// A separate thread will call this method
		public void thread() {
			
			taskResult = Do.add(router, forward);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			result = taskResult;
			close(AddTask.this); // We're done
		}
	}
}
