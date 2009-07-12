package base.internet.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import base.data.Bin;
import base.exception.NetException;
import base.internet.name.IpPort;
import base.process.Mistake;
import base.state.Close;
import base.time.Duration;
import base.time.Now;

/** An open TCP socket connection. */
public class Socket extends Close {

	// Open

	/** Open a new TCP socket connection to the given IP address and port number or throw a NetException. */
	public Socket(IpPort ipPort) {
		try {
			Now start = new Now();
			outgoing = true;
			channel = SocketChannel.open();
			if (!channel.connect(ipPort.toInetSocketAddress())) throw new NetException("connect false");
			this.ipPort = ipPort;
			size();
			connect = new Duration(start);
		} catch (IOException e) { throw new NetException(e); }
	}
	
	/** Make a new Socket for the given SocketChannel that just connected in to us. */
	public Socket(SocketChannel channel) {
		try {
			if (!channel.isConnected()) throw new NetException("not connected"); // Make sure the given channel is connected
			outgoing = false;
			this.channel = channel;
			ipPort = new IpPort((InetSocketAddress)channel.socket().getRemoteSocketAddress());
			size();
			connect = null;
		} catch (IOException e) { throw new NetException(e); }
	}
	
	/** Increase the socket buffer size if necessary. */
	private void size() throws IOException {
		if (channel.socket().getSendBufferSize() < Bin.medium)
			channel.socket().setSendBufferSize(Bin.medium);
		if (channel.socket().getReceiveBufferSize() < Bin.medium)
			channel.socket().setReceiveBufferSize(Bin.medium);
	}
	
	// Look

	/** The Java SocketChannel object that is this TCP socket connection. */
	public final SocketChannel channel;
	/** The IP address and port number of the peer on the far end of this connection. */
	public final IpPort ipPort;
	/** true if we connected out to the peer, false if the peer connected in to us. */
	public final boolean outgoing;
	/** How long we took to connect, null if the peer connected in to us. */
	public final Duration connect;
	
	// Close

	/** Disconnect this TCP socket connection. */
	@Override public void close() {
		if (already()) return;
		try { channel.close(); } catch (IOException e) { Mistake.ignore(e); }
	}
}
