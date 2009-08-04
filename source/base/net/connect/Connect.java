package base.net.connect;

import base.data.Data;
import base.data.Outline;
import base.exception.ChopException;
import base.exception.DataException;
import base.exception.ProgramException;
import base.net.flow.SocketBay;
import base.net.name.IpPort;
import base.process.Mistake;
import base.state.Close;
import base.state.Receive;
import base.state.Update;
import base.time.Egg;

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
		public void receive() throws Exception {
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
