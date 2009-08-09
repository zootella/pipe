package base.net.packet;

import base.data.Bin;
import base.net.name.IpPort;
import base.size.move.PacketMove;

public class Packet {

	/** Make a Packet to send the data in bin to ipPort. */
	public Packet(Bin bin, IpPort ipPort) {
		this.bin = bin;
		this.move = null;
		this.ipPort = ipPort;
	}

	/** Make a Packet we've received. */
	public Packet(Bin bin, PacketMove move) {
		this.bin = bin;
		this.move = move;
		this.ipPort = null;
	}

	/** The UDP Packet payload data we send or the data we received. */
	public final Bin bin;
	/** For a Packet we received, when it arrived and where it came from. */
	public final PacketMove move;
	/** For a Packet we send, the IP address and port number we're sending it to. */
	public final IpPort ipPort;
}
