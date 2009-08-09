package base.net.packet;

import base.data.Bin;
import base.exception.ProgramException;
import base.size.move.PacketMove;
import base.state.Close;
import base.state.Task;
import base.state.TaskBody;
import base.state.Update;

public class ReceiveTask extends Close {

	// Make

	/** Wait on listen until a new Packet arrives, given the empty bin or null to have us make one. */
	public ReceiveTask(Update up, ListenPacket listen, Bin bin) {
		this.up = up; // We'll tell above when we're done
		this.listen = listen;
		this.bin = bin;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final ListenPacket listen;
	private final Bin bin;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	/** The Packet we received, or throws the exception that made us give up. */
	public Packet result() { check(exception, packet); return packet; }
	private ProgramException exception;
	private Packet packet;

	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private PacketMove taskMove; // References thread() can safely set

		// A separate thread will call this method
		public void thread() throws Exception {

			// Wait on listen until a new UDP packet arrives
			taskMove = bin.receive(listen);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			packet = new Packet(bin, taskMove);
			close(me());          // We're done
		}
	}
	private ReceiveTask me() { return this; } // Give inner code a link to the outer object
}
