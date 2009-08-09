package base.size.move;

import base.net.name.IpPort;
import base.time.Duration;
import base.time.Now;

public class PacketMove {

	/** Document we successfully transferred a UDP packet with ipPort with a data payload of 0 or more bytes. */
	public PacketMove(Now start, int size, IpPort ipPort) {
		if (size < 0) throw new IndexOutOfBoundsException();
		this.duration = new Duration(start); // Record now as the stop time
		this.size = size;
		this.ipPort = ipPort;
	}

	/** How long the transfer took to complete, the time before and after the blocking call that did it. */
	public final Duration duration;
	/** The size of the UDP packet's data payload, 0 or more bytes. */
	public final int size;
	/** The IP address and port number we sent the UDP packet to or received it from. */
	public final IpPort ipPort;
}
