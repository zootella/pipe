package base.internet.packet;

import java.util.ArrayList;
import java.util.List;

import base.data.Bin;
import base.data.BinBin;
import base.data.Data;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

/** A PacketMachine listens on a port to send and receive UDP packets. */
public class PacketMachine extends Close {

	/** Make a new PacketMachine that listens on the given port number. */
	public PacketMachine(Port port) {

		// Make inside
		packets = new ArrayList<Packet>();
		receivers = new ArrayList<PacketReceive>();
		bins = new BinBin();
		listen = new ListenPacket(port);

		// Start update
		update = new Update(new MyReceive());
		update.send();
	}
	
	/** Packets we're about to send. */
	private final List<Packet> packets;
	/** A List of PacketReceive objects above that we show each packet we receive. */
	private final List<PacketReceive> receivers;
	/** A stack of empty bins to recycle. */
	private final BinBin bins;
	/** Our datagram socket bound to port that we use to send and receive UDP packets. */
	private final ListenPacket listen;
	/** Our Update object that objects below tell when they've changed. */
	private final Update update;

	/** A SendTask that sends a UDP packet. */
	private SendTask send;
	/** A ReceiveTask that waits for a UDP packet to arrive, and then receives it. */
	private ReceiveTask receive;

	/** The Exception that closed this PacketMachine, null if there isn't one. */
	public Exception exception() { return exception; }
	private Exception exception;

	@Override public void close() {
		if (already()) return;
		close(listen);
		close(send);
		close(receive);
		receivers.clear(); // Stop bothering objects above
	}

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {

				// Send
				if (done(send)) { // Our SendTask finished sending a packet
					Bin bin = send.result();
					send = null;
					bins.add(bin); // Recycle the Bin it used
				}
				if (no(send) && !packets.isEmpty()) // We're not sending a packet right now and we've got one to send
					send = new SendTask(update, listen, packets.remove(0)); // Send it

				// Receive
				if (done(receive)) { // Our ReceiveTask finished waiting for and getting a packet
					Packet packet = receive.result(); // Get the packet
					receive = null;
					for (PacketReceive o : new ArrayList<PacketReceive>(receivers)) // Show it to each interested object above
						o.receive(packet);
					bins.add(packet.bin); // That's it for packet, recycle its Bin
				}
				if (no(receive)) // Wait for the next packet to arrive
					receive = new ReceiveTask(update, listen, bins.get());

			} catch (Exception e) { exception = e; close(); }
		}
	}
	
	// Send

	/** Send data to ipPort as the payload of a UDP packet. */
	public void send(Data data, IpPort ipPort) {
		open();
		Bin bin = bin();
		bin.add(data);
		send(bin, ipPort);
	}
	
	/** Get an empty Bin to fill with data and then send. */
	public Bin bin() {
		open();
		return bins.get();
	}

	/** Send the data in bin to ipPort as a new UDP packet. */
	public void send(Bin bin, IpPort ipPort) {
		open();
		packets.add(new Packet(bin, ipPort));
		update.send();
	}
	
	// Receive

	/** Add o to the list of objects this PacketMachine shows the packets it receives. */
	public void add(PacketReceive o) {
		open();
		if (!receivers.contains(o))
			receivers.add(o);
	}
	
	/** Remove o from the list of objects this PacketMachine bothers with arrived packets. */
	public void remove(PacketReceive o) {
		open();
		receivers.remove(o);
	}
}
