package base.net.packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;

import base.data.Bin;
import base.exception.NetException;
import base.net.name.Port;
import base.process.Mistake;
import base.state.Close;

/** A UDP socket bound to port that can send and receive packets. */
public class ListenPacket extends Close {

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
		}
		catch (SocketException e) { throw new NetException("already bound", e); }
		catch (IOException e) { throw new NetException(e); }
	}

	/** The port number this socket is bound to. */
	public final Port port;
	/** The Java DatagramChannel object that is this UDP socket. */
	public final DatagramChannel channel;

	/** Stop listening on port. */
	@Override public void close() {
		if (already()) return;
		try { channel.close(); } catch (IOException e) { Mistake.log(e); }
	}
}
