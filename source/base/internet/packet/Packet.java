package base.internet.packet;

import base.data.Bin;
import base.internet.name.IpPort;
import base.size.PacketMove;

/** A Packet object holds information about a UDP packet we send or receive, and gets used over and over again. */
public class Packet {
	
	// Make

	/** Make a new Packet to keep, send, or receive a UDP packet. */
	public Packet() {
		bin = Bin.big();
	}
	
	// Mark

	/** Blank this Packet to recycle it. */
	public void blank() {
		this.use = blank;
		this.bin.clear();
		this.move = null;
		this.ipPort = null;
	}

	/** Mark this Packet as in use by a Task thread that is sending it. */
	public void send(IpPort ipPort) {
		this.use = task;
		this.move = null;
		this.ipPort = ipPort;
	}

	/** Mark this Packet as in use by a Task thread that is waiting for a new UDP packet to arrive. */
	public void receiveWait() {
		this.use = task;
		this.bin.clear();
		this.move = null;
		this.ipPort = null;
	}

	/** This Packet holds information about a UDP packet that has arrived. */
	public void receiveArrived(PacketMove move) {
		this.use = arrived;
		this.move = move;
		this.ipPort = move.ipPort;
	}

	// Look

	/** What use this Packet currently has. */
	public int use() { return use; }
	/** Data to send, empty afterwards, or, empty to wait, then data received. */
	public Bin bin() { return bin; }
	/** How big the data is and how long it took to send, or how long we waited for it. */
	public PacketMove move() { return move; }
	/** The IP address and port number we sent this Packet to or received it from. */
	public IpPort ipPort() { return ipPort; }
	
	// Inside
	
	private int use;
	private final Bin bin;
	private PacketMove move;
	private IpPort ipPort;
	
	// Define

	/** This Packet is blank, you can fill it with data, address it, and send it. */
	public static final int blank = 0;
	/** A Task thread is using this Packet, do not touch its bin. */
	public static final int task = 1;
	/** This Packet has arrived, you can look at it and then blank() it. */
	public static final int arrived = 2;
}
