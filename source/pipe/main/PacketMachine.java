package pipe.main;

import java.util.ArrayList;
import java.util.List;

import base.data.Bin;
import base.data.Data;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.packet.ListenPacket;
import base.internet.packet.ReceiveTask;
import base.internet.packet.SendTask;
import base.internet.packet.Packet;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

// the program makes one of these to send and receive udp packets
public class PacketMachine extends Close {
	
	// Object
	
	public PacketMachine(Program program, Update up, Port port) {
		this.program = program;
		this.up = up;
		this.port = port;
		
		sendList = new ArrayList<Packet>();
		receiveList = new ArrayList<Packet>();
		bins = new ArrayList<Bin>();
		
		update = new Update(new MyReceive());
		update.send();
	}
	
	private final Program program;
	private final Update up;
	private final Update update;
	private final Port port;
	
	private ListenPacket listen;
	
	private SendTask sendTask;
	private ReceiveTask receiveTask;
	
	private List<Packet> sendList;
	private Packet sendPacket;
	private Packet receivePacket;
	private List<Packet> receiveList;
	private List<Packet> recycleList;

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
					sendTask.result();
					sendTask = null;
				}
				if (done(receiveTask)) {
					receiveList.add(receiveTask.result());
					receiveTask = null;
				}
				
				// Start
				if (!sendList.isEmpty() && no(sendTask)) {
					sendTask = new SendTask(update, listen, sendBin, p.ipPort);
				}
				if (no(receiveTask))
					receiveTask = new ReceiveTask(update, listen, receiveBin);

			} catch (Exception e) { exception = e; close(); }
		}
	}
	
	private Exception exception;
	public Exception exception() { return exception; }

	// Use
	
	public void send(Data data, IpPort ipPort) {
		sendList.add(packet);
		update.send();
	}
	
	/** The next packet we've received, or null no more right now. */
	public Packet receiveLook() {
		if (receiveList.isEmpty()) return null;
		
		return receiveList.get(0);
	}
	
	public void receiveDone() {
		bins.add(receiveList.remove(0).bin);
	}
}
