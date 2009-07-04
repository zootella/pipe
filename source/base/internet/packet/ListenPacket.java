package base.internet.packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;


import base.data.Bin;
import base.exception.NetException;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.state.Close;

/** A UDP socket bound to port that can send and receive packets. */
public class ListenPacket extends Close {

	// Open

	/** Bind a new UDP socket to port. */
	public ListenPacket(Port port) {
		try {
			this.port = port;
			channel = DatagramChannel.open();
			if (channel.socket().getSendBufferSize() < Bin.big) // Handle 64 KB UDP packets, the default is just 8 KB
				channel.socket().setSendBufferSize(Bin.big);
			if (channel.socket().getReceiveBufferSize() < Bin.big)
				channel.socket().setReceiveBufferSize(Bin.big);
			channel.socket().bind(new InetSocketAddress(port.port));
		} catch (IOException e) { throw new NetException(e); }
	}
	
	// Use
	
	
	
	public void send(Packet packet) {
		if (closed()) throw new NetException("closed");
		
		
		
		this.outgoing = true; // Send
		this.move = bin.send(this, ipPort);
		this.ipPort = ipPort;
		this.bin = bin;
	}
	
	
	public void receive(Packet packet) {
		if (closed()) throw new NetException("closed");
		
		this.outgoing = false; // Receive
		this.move = bin.receive(listen);
		this.ipPort = move.ipPort;
		this.bin = bin;
	}
	
	

	// Look

	/** The port number this socket is bound to. */
	public final Port port;
	/** The Java DatagramChannel object that is this UDP socket. */
	public final DatagramChannel channel;

	// Close

	/** Stop listening on port. */
	@Override public void close() {
		if (already()) return;
		try { channel.close(); } catch (IOException e) {}
	}
}
