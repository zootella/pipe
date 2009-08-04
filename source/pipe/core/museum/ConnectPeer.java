package pipe.core.museum;

import pipe.main.Program;
import base.data.Data;
import base.data.Outline;
import base.exception.ProgramException;
import base.net.accept.AcceptReceive;
import base.net.connect.Connect;
import base.net.flow.SocketBay;
import base.net.name.IpPort;
import base.process.Mistake;
import base.state.Close;
import base.state.Receive;
import base.state.Update;
import base.time.Ago;
import base.time.Pulse;

// make a p2p connection that can connect either way
// tries to connect to lan and internet every 4 seconds
// listens for an incoming connection
// says hello and makes sure the peer says an outline that hashes to hash
// doesn't time out
public class ConnectPeer extends Close {
	
	/** Upload hello to the peer at lan and internet, and check that it says hash back, or let it connect to us. */
	public ConnectPeer(Program program, Update up, IpPort lan, IpPort internet, Data hello, Data hash) {
		this.program = program;
		this.up = up;
		this.lanIp = lan;
		this.netIp = internet;
		this.hello = hello;
		this.hash = hash;
		
		acceptReceive = new MyAcceptReceive();
		program.core.accept.add(acceptReceive);

		receive = new MyReceive();
		update = new Update(receive);
		update.send();
		
		lanAgo = new Ago();
		netAgo = new Ago();
		pulse = new Pulse(receive);
	}
	
	private final Program program;
	private final Update up;
	private final Update update;
	
	private final IpPort lanIp; // remote peer's lan ip address
	private final IpPort netIp; // remote peer's internet ip address
	private final Data hello; // our greeting we'll send the remote peer
	private final Data hash; // the first outline the remote peer send us must hash to this value
	
	private Connect lan;
	private Connect net;
	
	private final Ago lanAgo;
	private final Ago netAgo;
	private final Pulse pulse;
	
	
	@Override public void close() {
		if (already()) return;
		
		close(lan);
		close(net);
		close(socket);
		close(pulse);
		
		try { program.core.accept.remove(acceptReceive); } catch (Exception e) { Mistake.log(e); }
		
		up.send();
	}
	
	public SocketBay socket() { return socket; }
	private SocketBay socket;
	
	//TODO write this like a task, sure it doesn't time out, but it ends in victory or exception with result()

	private final MyReceive receive;
	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			
			// Check
			if (done(socket))
				socket = null;

			// Connect to peer's LAN address
			if (no(socket) && no(lan) && lanAgo.enough())
				lan = new Connect(update, lanIp, hello, hash);
			if (done(lan)) {
				try {
					if (no(socket)) {
						socket = lan.result();
						up.send();
					} else {
						close(lan.result());
					}
				} catch (ProgramException e) { Mistake.ignore(e); }
				lan = null;
			}

			// Connect to peer's Internet address
			if (no(net) && no(socket) && netAgo.enough())
				net = new Connect(update, netIp, hello, hash);
			if (done(net)) {
				try {
					if (no(socket)) {
						socket = net.result();
						up.send();
					} else {
						close(net.result());
					}
				} catch (ProgramException e) { Mistake.ignore(e); }
				net = null;
			}
		}
	}
	
	private final MyAcceptReceive acceptReceive;
	private class MyAcceptReceive implements AcceptReceive {
		public boolean receive(SocketBay s) {
			if (closed()) return false;
			try {
				
				// See if the peer has connected to us
				if (no(socket) && (new Outline(s.download().data())).toData().hash().start(6).same(hash)) {
					socket = s;
					up.send();
					return true;
				}
				
			} catch (ProgramException e) { Mistake.ignore(e); }
			return false;
		}
	}
}
