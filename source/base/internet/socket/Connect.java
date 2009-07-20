package base.internet.socket;

import base.exception.ProgramException;
import base.internet.name.IpPort;
import base.internet.packet.Packets;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

public class Connect extends Close {

	/** Make a new outgoing socket connection to ipPort in 4 seconds. */
	public Connect(Update up, IpPort ipPort) {
		this.ipPort = ipPort;
		
		this.up = up;
		update = new Update(new MyReceive());
		
	}
	
	private final IpPort ipPort;
	private final Update up;
	private final Update update;
	
	/** The socket we connected, its yours to use and then close, or throws the exception that made us give up. */
	public Socket result() { check(exception, socket); return socket; }
	private ProgramException exception;
	private Socket socket;
	
	
	@Override public void close() {
		if (already()) return;
		
	}

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			try {
	
			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private Connect me() { return this; }
	
	
}
