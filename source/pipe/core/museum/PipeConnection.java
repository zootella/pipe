package pipe.core.museum;

import base.data.Data;
import base.data.Outline;
import base.exception.ProgramException;
import base.net.connect.ConnectTask;
import base.net.flow.SocketBay;
import base.net.name.IpPort;
import base.state.Close;
import base.state.Receive;
import base.state.Update;
import pipe.main.Program;

// tries to make the connection
// does the handshake
// later, tries to reconnect
// adds encryption
// listens for the remote handshake
// both SendPipe and ReceivePipe have a PipeConnection
public class PipeConnection extends Close {

	public PipeConnection(Program program, Outline hereHello, Outline awayHi) {
		this.program = program;
		this.hereHello = hereHello;
		this.awayHi = awayHi;

		hereHi = new Outline("h");
		hereHi.add("h", hereHello.toData().hash().start(6)); // Just the first 6 bytes of the 20-byte SHA1 hash
		hereHi.add("i", program.core.here.internet().data());
		hereHi.add("l", program.core.here.lan().data());
		
		update = new Update(new MyReceive());
	}
	
	private final Program program;
	private final Update update;
	
	private ConnectTask connect;
	private SocketBay socket;

	@Override public void close() {
		if (already()) return;
		close(connect);
		close(socket);
	}

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			try {
				
				if (awayHi != null && no(connect))
					connect = new ConnectTask(update, new IpPort(awayHi.value("l")));

				if (done(connect) && no(socket)) {
					socket = new SocketBay(update, connect.result());
					socket.upload().add(hereHello.toData());
				}

			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private PipeConnection me() { return this; }
	
	public ProgramException exception() { return exception; }
	private ProgramException exception;

	private Outline hereHi;
	private final Outline hereHello;
	private final Outline awayHi;
	private Outline awayHello;

	public String hereHi() {
		return null;
		
	}
	
	public boolean awayHi(String s) {
		return false;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
