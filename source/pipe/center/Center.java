package pipe.center;

import javax.swing.SwingUtilities;

import base.data.Bin;
import base.data.Data;
import base.data.Number;
import base.data.Outline;
import base.data.Text;
import base.encode.Hash;
import base.net.name.Port;
import base.net.packet.Packet;
import base.net.packet.PacketReceive;
import base.net.packet.Packets;
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
				} catch (Exception e) { Mistake.stop(e); } // Stop the program for an Exception we didn't expect
			}
		});
	}
	
	public Center() {
		packets = new Packets(new Port(Number.toInt(Text.after(site, ":"))));
		packets.add(new MyPacketReceive());
		Alive.still();
	}

	public final Packets packets;

	@Override public void close() {
		if (already()) return;

		close(packets);
		
		Mistake.closeCheck();
	}
	
	private class MyPacketReceive implements PacketReceive {
		public void receive(Packet packet) {
			if (closed()) return;
			try {
				
				// Receive packets and send responses
				Outline q = new Outline(packet.bin.data()); // Parse the UDP payload into an Outline
				if (q.name.equals("aq")) { // Address request
					
					Data data = packet.move.ipPort.data();
					Outline p = new Outline("ap", data); // Address response
					p.add("hash", Hash.hash(data)); // Optional integrity check

					Bin bin = packets.bin();
					bin.add(p.toData());
					packets.send(bin, packet.move.ipPort);
				}

			} catch (Exception e) { Mistake.log(e); } // Log and drop unknown packets
		}
	}
}
