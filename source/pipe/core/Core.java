package pipe.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import pipe.core.museum.Pipe;
import pipe.main.Mistake;
import pipe.main.Program;
import pipe.user.ExchangeDialog;
import pipe.user.FolderDialog;
import pipe.user.MuseumDialog;

import base.data.Outline;
import base.encode.Encode;
import base.file.Path;
import base.internet.name.Ip;
import base.internet.name.Port;
import base.internet.packet.ListenPacket;
import base.internet.packet.ReceiveTask;
import base.internet.packet.SendTask;
import base.setting.Store;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

/** The core program beneath the window that does everything. */
public class Core extends Close {

	public Core(Program program) {
		this.program = program;
		
		update = new Update(new MyReceive());
		
		port = new Port(1234);
		pipes = new Pipes(program);
		packet = new PacketMachine(update, port);
		
		
		/*
		

		
		
		// what's my ip address, lan first
		try {
			InetAddress me = InetAddress.getLocalHost();
			Ip ip = new Ip(me);
			System.out.println(ip.toString());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		// ok, now for the internet
		// well, first let's try the time server thing
		time = new Time(program, update, "time-c.timefreq.bldrdoc.gov", new Port(37), Encode.fromBase16("00"));
		*/
		
		
		

	}

	private final Program program;
	private final Update update;
	public final Port port;
	public final Pipes pipes;
	public final PacketMachine packet;
	
//	private final Time time;

	@Override public void close() {
		if (already()) return;
		
		close(pipes);
		close(packet);
	}

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				/*
				if (time.answer != null)
					System.out.println("Answer: " + Encode.toBase16(time.answer));
					*/

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
}
