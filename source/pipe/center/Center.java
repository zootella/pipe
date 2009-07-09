package pipe.center;

import javax.swing.SwingUtilities;

import base.data.Bin;
import base.data.Data;
import base.data.Number;
import base.data.Text;
import base.internet.name.Port;
import base.internet.packet.Packet;
import base.internet.packet.PacketMachine;
import base.internet.packet.PacketReceive;
import base.process.Alive;
import base.process.Mistake;
import base.state.Close;

public class Center extends Close {

	/** Domain name and port number of the central server. */
	public static final String site = "bootcloud.info:9193";

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
		packetMachine = new PacketMachine(new Port(Number.toInt(Text.after(site, ":"))));
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
				
			Bin bin = packetMachine.bin();
			bin.add(new Data("Your address is " + packet.move.ipPort.toString()));
			packetMachine.send(bin, packet.move.ipPort);
		}
	}
}
