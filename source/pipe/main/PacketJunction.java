package pipe.main;

import java.util.ArrayList;
import java.util.List;

import base.data.Bin;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.packet.ListenPacket;
import base.internet.packet.Packet;
import base.internet.packet.ReceiveTask;
import base.internet.packet.SendTask;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

// the program makes one of these to send and receive udp packets
public class PacketJunction extends Close {
	
	// Object
	
	public PacketJunction(Program program, Update up, Port port) {
		this.program = program;
		this.up = up;
		this.port = port;
		
		sendList = new ArrayList<Packet>();
		receiveList = new ArrayList<Packet>();
		sendBay = Bin.big();
		receiveBay = Bin.big();
		
		update = new Update(new MyReceive());
		update.send();
	}
	
	private final Program program;
	private final Update up;
	private final Update update;
	private final Port port;
	
	private ListenPacket listen;
	private final List<Packet> sendList;
	private final List<Packet> receiveList;
	private final Bin sendBay;
	private final Bin receiveBay;
	private SendTask sendTask;
	private ReceiveTask receiveTask;
	
	@Override public void close() {
		if (already()) return;
		
		close(listen);
	}

	// Receive

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				if (no(listen))
					listen = new ListenPacket(port);
				
				
				
				
			} catch (Exception e) { exception = e; close(); }
		}
	}
	
	private Exception exception;
	public Exception exception() { return exception; }

	// Use
	
	public void send(Packet packet, IpPort ipPort) {
		sendList.add(packet);
		
		
		update.send();
		
	}
	
	/** The next packet we've received, or null no more right now. */
	public Packet receive() {
		if (receiveList.isEmpty())
			return null;
		return receiveList.remove(0);
	}
}
