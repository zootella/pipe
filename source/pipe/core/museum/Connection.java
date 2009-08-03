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
	
	public Connection(Program program, Update up, IpPort lan, IpPort internet, Data hello, Data hash) {
		this.program = program;
		this.up = up;
		this.lan = lan;
		this.internet = internet;
		this.hello = hello;
		this.hash = hash;
		
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
	private final Data hello;
	private final Data hash; // hash of the remote peer's hello outline
	
	private ConnectTask connectLan;
	private ConnectTask connectInternet;
	
	private SocketBay socketLan;
	private SocketBay socketInternet;
	private SocketBay socket; // what connected to us, or, what we connected and it took
	
	@Override public void close() {
		if (already()) return;
		
		close(connectLan);
		close(connectInternet);
		close(socketLan);
		close(socketInternet);
		close(socket);
		
		try { program.core.accept.remove(acceptReceive); } catch (Exception e) { Mistake.log(e); }
	}
	
	public SocketBay socket() { return socket; }

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			clear();
			if (open(socket)) return;
			
			if (no(socketLan)) {
				if (no(connectLan))
					connectLan = new ConnectTask(update, lan);
				if (done(connectLan)) {
					try {
						socketLan = new SocketBay(update, connectLan.result());
						up.send();
					} catch (ProgramException e) { Mistake.ignore(e); }
					connectLan = null;
				}
			}
			

			
			
		}
	}
	
	private final MyAcceptReceive acceptReceive;
	private class MyAcceptReceive implements AcceptReceive {
		public boolean receive(SocketBay s) {
			if (closed()) return false;
			clear();
			if (open(socket)) return false;
			try {
				if ((new Outline(s.download().data())).toData().hash().start(6).same(hash)) {
					socket = s;
					up.send();
					return true;
				}
			} catch (ProgramException e) { Mistake.ignore(e); }
			return false;
		}
	}
	
	
	
	private void clear() {
		if (done(socket))
			socket = null;
		if (done(socketLan))
			socketLan = null;
		if (done(socketInternet))
			socketInternet = null;
	}
	
	
	
	
	
	
	
	
	
	
	public ProgramException exception() { return exception; }
	private ProgramException exception;
	
	
	
	
	
	
	
	
	
	
	
}
