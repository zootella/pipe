package pipe.center;

import javax.swing.SwingUtilities;

import base.data.Data;
import base.internet.name.Port;
import base.internet.packet.Packet;
import base.internet.packet.PacketMachine;
import base.process.Alive;
import base.process.Mistake;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

public class Center extends Close {
	
	/** The port we listen on. */
	public static final int port = 9193;
	
	public static void main(String[] arguments) {
		SwingUtilities.invokeLater(new Runnable() { // Have the normal Swing thread call this run() method
			public void run() {
				try {
					new Center(); // Make and start the program
				} catch (Exception e) { Mistake.grab(e); } // Exception starting up
			}
		});
	}
	
	public Center() {
		update = new Update(new MyReceive());
		packet = new PacketMachine(update, new Port(port));
		Alive.still(); // Keep the program running
	}

	private final Update update;
	public final PacketMachine packet;

	@Override public void close() {
		if (already()) return;

		close(packet);

		// Make sure every object with a close() method ran
		try { Close.checkAll(); } catch (Exception e) { Mistake.grab(e); }
	}

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {

				while (packet.has()) {
					Packet r = packet.look();
					
					Packet s = reply(r);
					
					packet.send(s);

					packet.done();
				}

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	private Packet reply(Packet r) {
		Packet s = packet.get();
		s.bin.add(new Data("hello"));
		s.ipPort = r.ipPort;
		return s;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
