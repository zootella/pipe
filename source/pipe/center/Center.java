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

	// When the program runs, Java calls this main() method
    public static void main(String[] arguments) {
    	SwingUtilities.invokeLater(new Runnable() { // Have the normal Swing thread call this run() method
        	public void run() {
        		try {
        			
        			// Start the program
        			new Center();
        			
        			// Keep it running
        			Alive.still();

        		} catch (Exception e) { Mistake.grab(e); } // Exception starting up
        	}
    	});
	}
	
	public Center() {
		update = new Update(new MyReceive());
		packet = new PacketMachine(update, new Port(9193));
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
				
				while (packet.receiveHas()) {
					Packet p = packet.receiveLook();
					System.out.println("Packet from " + p.ipPort.toString() + " data " + p.bin.data().base16());

					// Tell the peer what his IP address and port number looks like from the central server
					packet.send(new Data(p.ipPort.toString()), p.ipPort);
					//TODO have a sha1 hash followed by an outline message for send and receive

					packet.receiveDone();
				}

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
}
