package base.internet.packet;

import base.data.Bin;
import base.size.PacketMove;
import base.state.Task;
import base.state.TaskBody;
import base.state.TaskClose;
import base.state.Update;

public class SendTask extends TaskClose {

	// Make

	/** Have listen send packet, don't look at packet after this. */
	public SendTask(Update update, ListenPacket listen, Packet packet) {
		this.update = update; // We'll tell above when we're done
		
		// Save the input
		this.listen = listen;
		this.packet = packet;

		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	/** Our bound UDP socket we use to send the packet. */
	private final ListenPacket listen;
	/** The Packet with the data and the IP address and port number we send it to. */
	private final Packet packet;

	// Result
	
	/** The empty Bin you can reuse, or throws the exception that made us give up. */
	public Bin result() throws Exception { return (Bin)check(bin); }
	private Bin bin;

	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private PacketMove taskMove; // References thread() can safely set

		// A separate thread will call this method
		public void thread() throws Exception {
				
			// Use listen to send bin's data to ipPort in a UDP packet
			taskMove = packet.bin.send(listen, packet.ipPort);
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(Exception e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			if (e == null) {      // No exception, save what thread() did
				
				bin = packet.bin;
				bin.clear();
			}
			close();              // We're done
			update.send();        // Tell update we've changed
		}
	}
}
