package org.zootella.net.connect;

import org.zootella.data.Data;
import org.zootella.data.Outline;
import org.zootella.exception.ChopException;
import org.zootella.exception.DataException;
import org.zootella.exception.ProgramException;
import org.zootella.net.flow.SocketBay;
import org.zootella.net.name.IpPort;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.Receive;
import org.zootella.state.Update;
import org.zootella.time.Egg;

public class Connect extends Close {
	
	/** Make a new TCP socket connection to ipPort, say hello and get hash response, in 4 seconds or less. */
	public Connect(Update up, IpPort ipPort, Data hello, Data hash) {
		this.up = up;
		this.ipPort = ipPort;
		this.hello = hello;
		this.hash = hash;
		
		receive = new MyReceive();
		egg = new Egg(receive);
		update = new Update(receive);
		update.send();
	}
	
	private final Update up;
	private final Update update;
	private final IpPort ipPort;
	private final Data hello;
	private final Data hash;
	private final Egg egg;
	private ConnectTask connect;

	@Override public void close() {
		if (already()) return;
		close(egg);
		close(connect);
		if (exception != null)
			close(socket);
		up.send();
	}
	
	public SocketBay result() { check(exception, socket); return socket; }
	private ProgramException exception;
	private SocketBay socket;

	private final MyReceive receive;
	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				egg.check();
				
				// Connect and upload hello
				if (no(connect))
					connect = new ConnectTask(update, ipPort);
				if (done(connect) && no(socket)) {
					socket = new SocketBay(update, connect.result());
					socket.upload().add(hello);
				}

				// Download and check the peer's response
				if (is(socket)) {
					try {
						Outline o = new Outline(socket.download().data());
						if (o.toData().hash().start(6).equals(hash))
							close(me());
						else
							throw new DataException("bad response");
					} catch (ChopException e) { Mistake.ignore(e); }
				}

			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private Connect me() { return this; }
}
