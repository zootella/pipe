package base.internet.packet;

import base.data.Bin;
import base.size.PacketMove;
import base.state.Task;
import base.state.TaskBody;
import base.state.TaskClose;
import base.state.Update;

public class ReceiveTask extends TaskClose {

	// Make

	/** Wait on listen until a new Packet arrives, given the empty bin or null to have us make one. */
	public ReceiveTask(Update update, ListenPacket listen, Bin bin) {
		this.update = update; // We'll tell above when we're done
		
		// Make a bin if we weren't given one
		if (bin == null) bin = Bin.big();
		bin.clear();
		
		// Save the input
		this.listen = listen;
		this.bin = bin;

		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	/** Our bound UDP socket that receives the packet. */
	private final ListenPacket listen;
	/** The Bin that holds the data of the arrived UDP packet. */
	private final Bin bin;

	// Result
	
	/** The Packet we received, or throws the exception that made us give up. */
	public Packet result() throws Exception { return (Packet)check(packet); }
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
		public void done(Exception e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			if (e == null) {      // No exception, save what thread() did

				packet = new Packet(bin, taskMove);
			}
			close();              // We're done
			update.send();        // Tell update we've changed
		}
	}
}
