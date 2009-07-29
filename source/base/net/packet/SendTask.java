package base.net.packet;

import base.data.Bin;
import base.exception.ProgramException;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;

public class SendTask extends Close {

	// Make

	/** Have listen send packet, don't look at packet after this. */
	public SendTask(Update up, ListenPacket listen, Packet packet) {
		this.up = up; // We'll tell above when we're done
		this.listen = listen;
		this.packet = packet;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final ListenPacket listen;
	private final Packet packet;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	/** The empty Bin you can reuse, or throws the exception that made us give up. */
	public Bin result() { check(exception, bin); return bin; }
	private ProgramException exception;
	private Bin bin;

	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {

		// A separate thread will call this method
		public void thread() throws Exception {
				
			// Use listen to send bin's data to ipPort in a UDP packet
			packet.bin.send(listen, packet.ipPort);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			bin = packet.bin;
			bin.clear();
			close(me());          // We're done
		}
	}
	private SendTask me() { return this; } // Give inner code a link to the outer object
}
