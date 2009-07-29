package base.net.web;

import java.net.InetAddress;
import java.net.UnknownHostException;

import base.exception.NetException;
import base.exception.ProgramException;
import base.net.name.Ip;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;

public class DomainTask extends Close {
	
	// Make

	/** Use DNS to resolve site like "www.site.com" to an IP address. */
	public DomainTask(Update up, String site) {
		this.up = up; // We'll tell above when we're done
		this.site = site;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	public final String site;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	/** The IP address our DNS lookup found, or throws the exception that made this give up. */
	public Ip result() { check(exception, ip); return ip; }
	private ProgramException exception;
	private Ip ip;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Ip taskIp; // References thread() can safely set

		// A separate thread will call this method
		public void thread() throws Exception {

			// Look up the domain name in DNS to get its IP address
			try {
				taskIp = new Ip(InetAddress.getByName(site));
			} catch (UnknownHostException e) { throw new NetException(e); }
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			ip = taskIp;
			close(me());          // We're done
		}
	}
	private DomainTask me() { return this; } // Give inner code a link to the outer object
}
