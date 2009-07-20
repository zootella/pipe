package pipe.core.here;

import java.net.InetAddress;
import java.net.UnknownHostException;

import base.exception.PlatformException;
import base.internet.name.Ip;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.state.Close;

public class Here extends Close {
	
	// Make
	
	public Here(Port port) {
		this.port = port;
		
	}
	
	private final Port port;
	private IpPort internet;
	private HereTask task;

	@Override public void close() {
		if (already()) return;

	}
	
	// Look

	/** Our internal IP address and listening port number on the LAN right now. */
	public IpPort lan() {
		try {
			return new IpPort(new Ip(InetAddress.getLocalHost()), port);
		} catch (UnknownHostException e) { throw new PlatformException(e); }
	}
	
	public IpPort internet() {
		
	}
	
	public Now age() {
		
	}
	
	// Do
	
	public boolean canRefresh() {
		
		
	}
	
	public void refresh() {
		if (!canRefresh()) return;
		
	}
}
