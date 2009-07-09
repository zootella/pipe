package pipe.center;

import javax.swing.SwingUtilities;

import base.data.Bin;
import base.data.Data;
import base.internet.name.Port;
import base.internet.packet.Packet;
import base.internet.packet.PacketMachine;
import base.internet.packet.PacketReceive;
import base.process.Alive;
import base.process.Mistake;
import base.state.Close;

public class Center extends Close {
	
	/** The port we listen on. */
	public static final int port = 9193;
	
	public static void main(String[] arguments) {
		SwingUtilities.invokeLater(new Runnable() { // Have the normal Swing thread call this run() method
			public void run() {
				try {
					System.out.println("CENTER start");
					new Center(); // Make and start the program
				} catch (Exception e) { Mistake.grab(e); } // Exception starting up
			}
		});
	}
	
	public Center() {
		packetMachine = new PacketMachine(new Port(port));
		packetMachine.add(new MyPacketReceive());
		Alive.still();
	}

	public final PacketMachine packetMachine;

	@Override public void close() {
		if (already()) return;

		close(packetMachine);

		// Make sure every object with a close() method ran
		try { Close.checkAll(); } catch (Exception e) { Mistake.grab(e); }
	}
	
	private class MyPacketReceive implements PacketReceive {
		public void receive(Packet packet) {
			if (closed()) return;
			try {
				
				System.out.println("CENTER receive");
				Bin bin = packetMachine.get();
				bin.add(new Data("Your address is " + packet.move.ipPort.toString()));
				packetMachine.send(bin, packet.move.ipPort);

			} catch (Exception e) { Mistake.ignore(e); }
		}
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
}
