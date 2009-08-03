package base.net.flow;

import base.data.Data;
import base.data.Outline;
import base.exception.ChopException;
import base.exception.DataException;
import base.exception.ProgramException;
import base.net.connect.ConnectTask;
import base.net.name.IpPort;
import base.process.Mistake;
import base.state.Close;
import base.state.Once;
import base.state.Receive;
import base.state.Update;
import base.time.Egg;

public class ConnectAndSend extends Close {

	/** Make a new socket connection to ipPort, send it uploadHello, and receive an Outline that hashes to downloadHash, all in 4 seconds. */
	public ConnectAndSend(Update up, IpPort ipPort, Data uploadHello, Data downloadHash) {
		this.up = up;
		this.ipPort = ipPort;
		this.uploadHello = uploadHello;
		this.downloadHash = downloadHash;
		
		receive = new MyReceive();
		update = new Update(receive);
		update.send();
		
		egg = new Egg(receive);
		uploadOnce = new Once();
	}
	
	private final IpPort ipPort;
	private final Data uploadHello;
	private final Data downloadHash;
	
	private final Update up;
	private final Update update;
	private final Egg egg;
	private final Once uploadOnce;
	
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
				
				// Connect and send
				if (no(connect))
					connect = new ConnectTask(update, ipPort);
				if (done(connect) && no(socket))
					socket = new SocketBay(update, connect.result());
				if (is(socket) && uploadOnce.once())
					socket.upload().add(uploadHello);

				// Download and check
				if (is(socket)) {
					try {
						Outline o = new Outline(socket.download().data());
						if (o.toData().hash().start(6).equals(downloadHash))
							close(me());
						else
							throw new DataException("downloaded outline with different hash");
					} catch (ChopException e) { Mistake.ignore(e); }
				}

			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private ConnectAndSend me() { return this; }
}
