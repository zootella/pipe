package base.internet.socket;

import java.util.ArrayList;
import java.util.List;

import base.internet.name.Port;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

/** The program's Sockets object listens on a port to accept new incoming TCP socket connections. */
public class Sockets extends Close {
	
	public Sockets(Update up, Port port) {

		listen = new ListenSocket(port);
		receivers = new ArrayList<AcceptReceive>();
		
		this.up = up;
		update = new Update(new MyReceive());
		update.send();
	}
	
	private final Update up;
	private final Update update;
	
	private final ListenSocket listen;
	private final List<AcceptReceive> receivers;

	@Override public void close() {
		if (already()) return;
		close(listen);
	}

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;

		}
	}
	
	// Accept

	/** Add o to the list of objects this Packets object shows the packets it receives. */
	public void add(AcceptReceive o) {
		open();
		if (!receivers.contains(o))
			receivers.add(o);
	}
	
	/** Remove o from the list of objects this Packets object bothers with arrived packets. */
	public void remove(AcceptReceive o) {
		open();
		receivers.remove(o);
	}
	

}
