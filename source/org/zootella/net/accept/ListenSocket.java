package org.zootella.net.accept;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import org.zootella.exception.NetException;
import org.zootella.net.name.Port;
import org.zootella.process.Mistake;
import org.zootella.state.Close;

/** A TCP server socket bound to port that can listen for a new incoming connection. */
public class ListenSocket extends Close {

	// Open

	/** Bind a new TCP server socket to port. */
	public ListenSocket(Port port) {
		try {
			this.port = port;
			channel = ServerSocketChannel.open();
			channel.socket().bind(new InetSocketAddress(port.port));
		} catch (IOException e) { throw new NetException(e); }
	}

	// Look

	/** The port number this socket is bound to. */
	public final Port port;
	/** The Java ServerSocketChannel object that is this TCP server socket. */
	public final ServerSocketChannel channel;
	
	// Close

	/** Stop listening on port. */
	@Override public void close() {
		if (already()) return;
		try { channel.close(); } catch (Throwable t) { Mistake.log(t); } // May throw IOException, whatever it throws, we must keep going
	}
}
