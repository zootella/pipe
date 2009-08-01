package pipe.core.museum;

import pipe.main.Program;
import base.data.Data;
import base.data.Outline;
import base.exception.DataException;
import base.exception.ProgramException;
import base.net.accept.AcceptReceive;
import base.net.connect.ConnectTask;
import base.net.flow.SocketBay;
import base.net.name.IpPort;
import base.process.Mistake;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

// a p2p connection that connects both ways
// later, add multiple tries and reconnect, and udp stream all right here
// the goal of this object is to get whatever it needs, and then do whatever it needs, to open and keep open a single two way stream
public class Connection extends Close {
	
	public Connection(Program program, Update up, IpPort lan, IpPort internet, Data signature) {
		this.program = program;
		this.up = up;
		this.lan = lan;
		this.internet = internet;
		this.signature = signature;
		
		acceptReceive = new MyAcceptReceive();
		program.core.accept.add(acceptReceive);

		update = new Update(new MyReceive());
		update.send();
		
	}
	
	private final Program program;
	private final Update up;
	private final Update update;
	
	private final IpPort lan; // remote peer's lan ip address
	private final IpPort internet; // remote peer's internet ip address
	private final Data signature; // hash of what the remote peer will tell us
	
	private ConnectTask connectLan;
	private ConnectTask connectInternet;
	private SocketBay socketConnectLan;
	private SocketBay socketConnectInternet;
	private SocketBay socketAccept;
	
	@Override public void close() {
		if (already()) return;
		
		close(connectLan);
		close(connectInternet);
		close(socketConnectLan);
		close(socketConnectInternet);
		close(socketAccept);
		
		try { program.core.accept.remove(acceptReceive); } catch (Exception e) { Mistake.log(e); }
	}

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			try {
				
				/*
				if (no(connect))
					connect = new ConnectTask(update, lan);

				if (done(connect) && no(socket)) {
					socket = new SocketBay(update, connect.result());
					socket.upload().add("hai");
				}
				*/

			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private Connection me() { return this; }
	
	private final MyAcceptReceive acceptReceive;
	private class MyAcceptReceive implements AcceptReceive {
		public boolean receive(SocketBay socket) {

			try {
				Data d = socket.download().data();
				Outline o = new Outline(d);
				
				
				
				
				socket.download().keep(d.size());
				return true;
			} catch (DataException e) { Mistake.ignore(e); }
			
			
			
			return false;
		}
	}
	
	public ProgramException exception() { return exception; }
	private ProgramException exception;
	
	
	
	
	
	
	
	
	
	
	
}
