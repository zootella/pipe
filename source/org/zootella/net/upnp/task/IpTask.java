package org.zootella.net.upnp.task;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.zootella.exception.NetException;
import org.zootella.exception.ProgramException;
import org.zootella.net.name.Ip;
import org.zootella.net.upnp.Router;
import org.zootella.state.Close;
import org.zootella.state.Task;
import org.zootella.state.TaskBody;
import org.zootella.state.Update;

public class IpTask extends Close {
	
	// Make

	public IpTask(Update up, Router device) {
		this.up = up; // We'll tell above when we're done
		this.device = device;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final Router device;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	public Ip result() { check(exception, ip); return ip; }
	private ProgramException exception;
	private Ip ip;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Ip taskIp; // References thread() can safely set

		// A separate thread will call this method
		public void thread() {

			Action a = device.action("GetExternalIPAddress");
			if (a == null) throw new NetException("null action");
			if (!a.postControlAction()) throw new NetException("post false");

			Argument r = a.getOutputArgumentList().getArgument("NewExternalIPAddress");
			taskIp = new Ip(r.getValue());
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			ip = taskIp;
			close(IpTask.this); // We're done
		}
	}
}
