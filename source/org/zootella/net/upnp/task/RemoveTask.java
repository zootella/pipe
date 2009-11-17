package org.zootella.net.upnp.task;

import org.cybergarage.upnp.Action;
import org.zootella.exception.NetException;
import org.zootella.exception.ProgramException;
import org.zootella.net.upnp.Map;
import org.zootella.net.upnp.Router;
import org.zootella.state.Close;
import org.zootella.state.Task;
import org.zootella.state.TaskBody;
import org.zootella.state.Update;

public class RemoveTask extends Close {
	
	// Make

	public RemoveTask(Update up, Router router, Map forward) {
		this.up = up; // We'll tell above when we're done
		this.router = router;
		this.forward = forward;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final Router router;
	private final Map forward;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	public Boolean result() { check(exception, result); return result; }
	private ProgramException exception;
	private Boolean result;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Boolean taskResult; // References thread() can safely set

		// A separate thread will call this method
		public void thread() {

			Action a = router.action("DeletePortMapping");
			if (a == null) throw new NetException("null action");

			a.setArgumentValue("NewRemoteHost",   forward.outsideIp);        // String
			a.setArgumentValue("NewExternalPort", forward.outsidePort.port); // int
			a.setArgumentValue("NewProtocol",     forward.protocol);         // String

			taskResult = new Boolean(a.postControlAction());
			
			//TODO have a synchronous remove for close()
			//factor the contents of all the tasks into static methods that can be called synchronously
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			result = taskResult;
			close(RemoveTask.this); // We're done
		}
	}
}
