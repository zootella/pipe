package base.net.accept;

import java.util.HashSet;
import java.util.Set;

import base.list.TwoBoots;
import base.net.flow.SocketBay;
import base.net.name.Port;
import base.state.Close;
import base.state.Receive;
import base.state.Update;
import base.time.Time;

/** The program's Accept object listens on a port to accept new incoming TCP socket connections. */
public class Accept extends Close {
	
	public Accept(Port port) {
		
		listenSocket = new ListenSocket(port);
		sockets = new TwoBoots<SocketBay>(Time.out);
		receivers = new HashSet<AcceptReceive>();

		update = new Update(new MyReceive());
		update.send();
	}
	
	private final Update update;
	
	private final ListenSocket listenSocket;
	private AcceptTask acceptTask;
	
	private final TwoBoots<SocketBay> sockets;
	private final Set<AcceptReceive> receivers;

	@Override public void close() {
		if (already()) return;
		close(listenSocket);
		close(acceptTask);
		close(sockets);
	}

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			
			// Wait for new sockets to connect
			if (done(acceptTask)) {
				sockets.add(new SocketBay(update, acceptTask.result()));
				acceptTask = null;
			}
			if (no(acceptTask))
				acceptTask = new AcceptTask(update, listenSocket);

			// Show each AcceptReceive object above each socket that has connected in
			for (AcceptReceive r : new HashSet<AcceptReceive>(receivers))
				for (SocketBay s : sockets.list())
					if (r.receive(s))
						sockets.remove(s); // r took s, remove s from our list
		}
	}
	
	/** Add o to the list of objects this Packets object shows the packets it receives. */
	public void add(AcceptReceive o) {
		open();
		if (!receivers.contains(o))
			receivers.add(o);
		update.send();
	}
	
	/** Remove o from the list of objects this Packets object bothers with arrived packets. */
	public void remove(AcceptReceive o) {
		open();
		receivers.remove(o);
	}
}
