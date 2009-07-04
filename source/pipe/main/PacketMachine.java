package pipe.main;

import java.util.ArrayList;
import java.util.List;

import base.data.Data;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.packet.ListenPacket;
import base.internet.packet.Packet;
import base.internet.packet.ReceiveTask;
import base.internet.packet.SendTask;
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
					receiveTask = new ReceiveTask(update, listen, reuse());

			} catch (Exception e) { exception = e; close(); }
		}
	}
	
	private Exception exception;
	public Exception exception() { return exception; }

	// Use
	
	public void send(Data data, IpPort ipPort) {
		
		Packet packet = reuse();
		packet.bin.add(data);
		packet.ipPort = ipPort;
		
		send.add(packet);
		update.send();
	}
	
	public boolean receiveHas() {
		return !receive.isEmpty();
	}
	
	public Packet receiveLook() {
		if (receive.isEmpty())
			return null;
		return receive.get(0);
	}
	
	public void receiveDone() {
		recycle.add(receive.get(0));
	}
	
	// Inside
	
	private Packet reuse() {
		
		if (recycle.isEmpty())
			return new Packet();
		
		Packet packet = recycle.remove(0);
		packet.clear();
		return packet;
	}
}
