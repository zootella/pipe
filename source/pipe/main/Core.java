package pipe.main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import pipe.core.Pipe;
import pipe.user.ExchangeDialog;
import pipe.user.FolderDialog;
import pipe.user.MuseumDialog;

import base.data.Outline;
import base.encode.Encode;
import base.exception.Mistake;
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
	
	// Links
	
	private final Program program;

	// Object
	
	public Core(Program program) {
		this.program = program;
		store = new Store();
		pipes = new ArrayList<Pipe>();
		
		
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
		update = new Update(new MyReceive());
		time = new Time(program, update, "time-c.timefreq.bldrdoc.gov", new Port(37), Encode.fromBase16("00"));
		
		
		

	}
	
	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				if (time.answer != null)
					System.out.println("Answer: " + Encode.toBase16(time.answer));

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	
	private final Update update;
	private final Time time;
	
	private final Store store;
	
	/** The list of pipes the program is using to transfer files with other computers. */
	public final List<Pipe> pipes;
	

	@Override public void close() {
		if (already()) return;
		
		close(store);
		for (Pipe pipe : pipes)
			pipe.close();
	}
	
	// Make

	public void makePipe() {
		
		// Museum dialog
		Pipe pipe = MuseumDialog.show(program);
		if (pipe != null) {

			// Folder dialog
			Path folder = FolderDialog.show(program, pipe.title(), pipe.instruction());
			folder = pipe.folder(folder);
			if (folder != null) {

				// Exchange Dialog
				Outline away = ExchangeDialog.show(program, pipe.home());
				away = pipe.away(away);
				if (away != null) {
					
					// List, show, and start the new pipe
					pipes.add(pipe);
					program.user.main.fill();
					pipe.go();
				}
			}
		}

		// Close a pipe we made but then didn't add
		if (pipe != null && !pipes.contains(pipe))
			pipe.close();
	}
	
	public void killPipe(Pipe pipe) {
		if (pipe == null || !pipes.contains(pipe))
			throw new IllegalArgumentException();
		
		pipes.remove(pipe);
		pipe.close();
		
		program.user.main.fill();
	}
}
