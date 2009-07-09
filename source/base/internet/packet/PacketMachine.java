package base.internet.packet;

import java.util.ArrayList;
import java.util.List;

import base.data.Bin;
import base.data.BinStack;
import base.data.Data;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

public class PacketMachine extends Close {
	
	// Object
	
	public PacketMachine(Port port) {
		this.port = port;
		
		send = new ArrayList<Packet>();
		receive = new ArrayList<PacketReceive>();
		stack = new BinStack();
		
		update = new Update(new MyReceive());
		update.send();
	}
	
	private final Update update;
	private final Port port;
	
	private ListenPacket listen;
	
	private SendTask sendTask;
	private ReceiveTask receiveTask;
	
	private final List<Packet> send;
	
	private final List<PacketReceive> receive;
	
	private final BinStack stack;
	
	private Exception exception;
	public Exception exception() { return exception; }

	@Override public void close() {
		if (already()) return;
		
		close(listen);
		close(sendTask);
		close(receiveTask);
		
		receive.clear();
	}

	// Receive

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				// Make
				if (no(listen))
					listen = new ListenPacket(port);
				
				// Send
				if (done(sendTask)) {
					Bin bin = sendTask.result();
					sendTask = null;
					stack.add(bin);
				}
				if (no(sendTask) && !send.isEmpty())
					sendTask = new SendTask(update, listen, send.remove(0));

				// Receive
				if (done(receiveTask)) {
					Packet packet = receiveTask.result();
					receiveTask = null;
					for (PacketReceive o : receive)
						o.receive(packet);
					stack.add(packet.bin);
				}
				if (no(receiveTask))
					receiveTask = new ReceiveTask(update, listen, stack.get());

			} catch (Exception e) { exception = e; close(); }
		}
	}
	
	
	
	// make the interface like this
	// send(data, ipPort), and you never hear anything back
	// you register to find out when packets arrive
	// when a packet arrives, you get called and have it handed to you, you have this one chance to do something with it
	// arrived(packet)
	
	// packet.bin
	// packet.ipPort
	// packet.move
	// only used in receive, send doesn't use it at all
	// if sending a udp packet throws an exception, it breaks the machine, not the sender
	
	// no bin cache for now
	// later, you could have machine cache 8 bins or whatever
	

	// Send
	
	/** Send data to ipPort as a new UDP packet. */
	public void send(Data data, IpPort ipPort) {
		open();
		Bin bin = get();
		bin.add(data);
		send(bin, ipPort);
	}
	
	/** Get an empty Bin to fill with the data of a new UDP packet you want to send. */
	public Bin get() {
		open();
		return stack.get();
	}

	/** Send the data in bin to ipPort as a new UDP packet. */
	public void send(Bin bin, IpPort ipPort) {
		open();
		send.add(new Packet(bin, ipPort));
		update.send();
	}
	
	// Receive

	public void add(PacketReceive o) {
		open();
		receive.add(o);
	}
	public void remove(PacketReceive o) {
		open();
		receive.remove(o);
	}
	


}
