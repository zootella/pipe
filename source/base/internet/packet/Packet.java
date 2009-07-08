package base.internet.packet;

import base.data.Bin;
import base.internet.name.IpPort;
import base.size.PacketMove;

/** A Packet object holds information about a UDP packet we send or receive, and can be cleared and used again. */
public class Packet {
	
	// State
	
	/** This Packet is blank, you can fill it with data, address it, and send it. */
	public static final int blank = 0;
	/** A Task thread is sending this Packet, do not touch its bin. */
	public static final int send = 1;
	/** This Packet has arrived, you can look at it and then clear() it blank. */
	public static final int receive = 2;

	// Object
	
	/** Make a new Packet to keep, send, or receive a UDP packet. */
	public Packet() {
		bin = Bin.big();
	}

	/** Clear this Packet to use it again. */
	public void clear() {
		use = blank;
		bin.clear();
		move = null;
		ipPort = null;
	}

	/** What this Packet is doing right now. */
	public int use;
	/** Data to send, empty afterwards, or empty to wait, then data received. */
	public final Bin bin;
	/** How big the data is and how long it took to send, or how long we waited for it. */
	public PacketMove move;
	/** The IP address and port number we sent this Packet to or received it from. */
	public IpPort ipPort;
}
