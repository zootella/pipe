package base.internet.packet;

import java.util.ArrayList;
import java.util.List;

import base.internet.name.Port;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

public class PacketMachine extends Close {
	
	// Object
	
	public PacketMachine(Update up, Port port) {
		this.up = up;
		this.port = port;
		
		send = new ArrayList<Packet>();
		receive = new ArrayList<Packet>();
		recycle = new ArrayList<Packet>();
		
		update = new Update(new MyReceive());
		update.send();
	}
	
	private final Update up;
	private final Update update;
	private final Port port;
	
	private ListenPacket listen;
	
	private SendTask sendTask;
	private ReceiveTask receiveTask;
	
	private final List<Packet> send;
	private final List<Packet> receive;
	private final List<Packet> recycle;

	@Override public void close() {
		if (already()) return;
		
		close(listen);
		close(sendTask);
		close(receiveTask);
	}

	// Receive

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				// Make
				if (no(listen))
					listen = new ListenPacket(port);

				// Done
				if (done(sendTask)) {
					recycle.add(sendTask.result());
					sendTask = null;
				}
				if (done(receiveTask)) {
					receive.add(receiveTask.result());
					receiveTask = null;
					up.send();
				}
				
				// Start
				if (!send.isEmpty() && no(sendTask))
					sendTask = new SendTask(update, listen, send.remove(0));
				if (no(receiveTask))
					receiveTask = new ReceiveTask(update, listen, get());

			} catch (Exception e) { exception = e; close(); }
		}
	}
	
	private Exception exception;
	public Exception exception() { return exception; }

	// Send
	
	/** Get a fresh empty Packet to fill with data, address, and then send(). */
	public Packet get() {
		open();
		if (closed()) throw new IllegalStateException();
		if (recycle.isEmpty())
			return new Packet();
		Packet packet = recycle.remove(0);
		packet.clear();
		return packet;
	}

	/** Send packet to the address written on it, don't look at packet after calling send(). */
	public void send(Packet packet) {
		open();
		send.add(packet);
		update.send();
	}
	
	// Receive

	/** true if this PacketMachine has received a Packet. */
	public boolean has() { open(); return !receive.isEmpty(); }
	/** Look at a Packet this PacketMachine has received. */
	public Packet look() { open(); return receive.get(0); }
	/** Tell this PacketMachine you're done looking at the Packet receiveLook() gave you. */
	public void done() { open(); recycle.add(receive.remove(0)); }
}
