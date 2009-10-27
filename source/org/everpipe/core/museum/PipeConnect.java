package org.everpipe.core.museum;

import org.everpipe.main.Program;
import org.zootella.data.Data;
import org.zootella.data.Outline;
import org.zootella.exception.ProgramException;
import org.zootella.net.accept.AcceptReceive;
import org.zootella.net.connect.Connect;
import org.zootella.net.flow.SocketBay;
import org.zootella.net.name.IpPort;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.Receive;
import org.zootella.state.Update;
import org.zootella.time.Ago;
import org.zootella.time.Now;
import org.zootella.time.Pulse;

public class PipeConnect extends Close {
	
	/** Keep trying to upload hello to the peer at lan and internet, or let it connect to us, until it says hash back. */
	public PipeConnect(Program program, Update up, IpPort lan, IpPort internet, Data hello, Data hash) {
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
		close(pulse);
		try { program.core.accept.remove(acceptReceive); } catch (Throwable t) { Mistake.log(t); }
		up.send();
	}
	
	/** When done() call result() to get the SocketBay with the peer's valid greeting sitting in download(). */
	public SocketBay result() { return socket; }
	private SocketBay socket; // as soon as you get socket, you close

	private final MyReceive receive;
	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;

			// Connect to peer's LAN address
			if (no(lan) && lanAgo.enough()) {
				lan = new Connect(update, lanIp, hello, hash);
				attempts++;
			}
			if (done(lan)) {
				try {
					socket = lan.result(); // As soon as we have socket, close and return
					close(me());
					return;
				} catch (ProgramException e) { Mistake.ignore(e); }
				lan = null;
			}

			// Connect to peer's Internet address
			if (no(net) && netAgo.enough())
				net = new Connect(update, netIp, hello, hash);
			if (done(net)) {
				try {
					socket = net.result();
					close(me());
					return;
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
				if ((new Outline(s.download().data())).toData().hash().start(6).same(hash)) {
					socket = s;
					socket.upload().add(hello);
					close(me());
					return true;
				}
				
			} catch (ProgramException e) { Mistake.ignore(e); }
			return false;
		}
	}
	private PipeConnect me() { return this; }
	
	//tell the user about us
	
	private int attempts;
	public int attempts() { return attempts; }
	
	
	
	
}
