package org.everpipe.center;

import javax.swing.SwingUtilities;

import org.zootella.data.Bin;
import org.zootella.data.Data;
import org.zootella.data.Number;
import org.zootella.data.Outline;
import org.zootella.data.Text;
import org.zootella.encrypt.hash.Hash;
import org.zootella.exception.ProgramException;
import org.zootella.net.name.Port;
import org.zootella.net.packet.Packet;
import org.zootella.net.packet.PacketReceive;
import org.zootella.net.packet.Packets;
import org.zootella.process.Alive;
import org.zootella.process.Mistake;
import org.zootella.state.Close;

public class Center extends Close {

	/** Domain name and port number of the central server. */
	public static final String site = "bootcloud.info:9193";

	public static void main(String[] arguments) {
		SwingUtilities.invokeLater(new Runnable() { // Have the normal Swing thread call this run() method
			public void run() {
				try {
					new Center(); // Make and start the program
				} catch (Throwable t) { Mistake.stop(t); } // Stop the program for an exception we didn't expect
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
		public boolean receive(Packet packet) {
			if (closed()) return false;
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
					return true;
				}

			} catch (ProgramException e) { Mistake.log(e); } // Log and drop unknown packets
			return false;
		}
	}
}
