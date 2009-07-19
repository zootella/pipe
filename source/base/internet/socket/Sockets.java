package base.internet.socket;

import base.exception.ProgramException;
import base.internet.name.Port;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

/** The program's Sockets object listens on a port to make new outgoing and accept new incoming TCP socket connections. */
public class Sockets extends Close {
	
	public Sockets(Update up, Port port) {

		listen = new ListenSocket(port);
		
		this.up = up;
		update = new Update(new MyReceive());
		update.send();
	}
	
	private final Update up;
	private final Update update;
	
	private final ListenSocket listen;

	/** The ProgramException that closed this Packets object, null if there isn't one. */
	public ProgramException exception() { return exception; }
	private ProgramException exception;

	@Override public void close() {
		if (already()) return;
		close(listen);
	}

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			try {

			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private Sockets me() { return this; }
	
	// Accept

}
