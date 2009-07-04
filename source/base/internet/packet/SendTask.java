package base.internet.packet;

import base.state.Task;
import base.state.TaskBody;
import base.state.TaskClose;
import base.state.Update;

public class SendTask extends TaskClose {

	// Make

	/** Send bin's data to ipPort in a UDP packet, don't look at bin after this. */
	public SendTask(Update update, ListenPacket listen, Packet packet) {
		this.update = update; // We'll tell above when we're done
		
		// Save the input
		this.listen = listen;
		this.packet = packet;
		
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	/** Our bound UDP socket we use to send the packet. */
	private final ListenPacket listen;

	// Result
	
	/** A Packet that tells when it was sent and has an empty bin to reuse, or throws the exception that made us give up. */
	public Packet result() throws Exception { return (Packet)check(packet); }
	private final Packet packet;

	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {

		// A separate thread will call this method
		public void thread() throws Exception {
				
			// Use listen to send bin's data to ipPort in a UDP packet
			packet.move = packet.bin.send(listen, packet.ipPort);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(Exception e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			close();              // We're done
			update.send();        // Tell update we've changed
		}
	}
}
