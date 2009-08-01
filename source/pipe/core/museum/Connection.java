package pipe.core.museum;

import pipe.main.Program;
import base.data.Data;
import base.exception.ProgramException;
import base.net.connect.ConnectTask;
import base.net.flow.SocketBay;
import base.net.name.IpPort;
import base.state.Close;
import base.state.Receive;

// a p2p connection that connects both ways
// later, add multiple tries and reconnect, and udp stream all right here
public class Connection extends Close {
	
	public Connection(Program program, IpPort lan, IpPort internet, Data signature) {
		this.program = program;
		this.lan = lan;
		this.internet = internet;
		this.signature = signature;
		
		//the goal of this object is to get whatever it needs, and then do whatever it needs, to open and keep open a single two way stream
	}
	
	private final Program program;
	private final IpPort lan;
	private final IpPort internet;
	private final Data signature;
	
	@Override public void close() {
		if (already()) return;
	}

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			try {
				
				/*
				if (awayHi != null && no(connect))
					connect = new ConnectTask(update, new IpPort(awayHi.value("l")));

				if (done(connect) && no(socket)) {
					socket = new SocketBay(update, connect.result());
					socket.upload().add(hereHello.toData());
				}
				*/

			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private Connection me() { return this; }
	
	public ProgramException exception() { return exception; }
	private ProgramException exception;
	
	
	
	
	
	
	
	
	
	
	
}
