package org.everpipe.core.here.old;

import org.everpipe.center.Center;
import org.zootella.data.Number;
import org.zootella.data.Outline;
import org.zootella.data.Text;
import org.zootella.exception.DataException;
import org.zootella.exception.ProgramException;
import org.zootella.net.name.IpPort;
import org.zootella.net.name.Port;
import org.zootella.net.packet.Packet;
import org.zootella.net.packet.PacketReceive;
import org.zootella.net.packet.Packets;
import org.zootella.net.web.DomainTask;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.Once;
import org.zootella.state.Receive;
import org.zootella.state.Update;
import org.zootella.time.Egg;
import org.zootella.time.Now;

/** A Here figures out what our IP address is once and right now. */
public class HereTask extends Close {
	
	// Make
	
	public HereTask(Update up, Port port, Packets packets) {

		// Save and connect the given object that sends UDP packets
		this.packets = packets;
		packetReceive = new MyPacketReceive();
		packets.add(packetReceive);

		// Save and connect our Update objects
		this.up = up;
		receive = new MyReceive();
		egg = new Egg(receive);
		sent = new Once();
		result = new Once();
		update = new Update(receive);
		update.send();
	}

	private final Update up;
	private final Update update;
	private final Packets packets;
	private final Egg egg;
	private final Once sent;
	public final Once result;
	
	private DomainTask domain;
	private IpPort center;
	

	@Override public void close() {
		if (already()) return;
		
		packets.remove(packetReceive);
		close(egg);
		close(domain);
	}

	// Result
	
	public IpPort internet() { check(exception, internet); return internet; }
	public Now age() { return egg.start; }
	private ProgramException exception;
	private IpPort internet;

	// Do

	private final MyReceive receive;
	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {

				// Throw a TimeException if we've been trying to finish for more than 4 seconds
				egg.check();

				// Look up the IP address of the central server
				if (no(domain))
					domain = new DomainTask(update, Text.before(Center.site, ":"));
				if (done(domain) && center == null)
					center = new IpPort(domain.result(), new Port(Number.toInt(Text.after(Center.site, ":"))));

				// Send the central server a UDP packet to find out what our IP address is
				if (center != null && internet == null && sent.once())
					packets.send((new Outline("aq")).toData(), center);

			} catch (ProgramException e) { exception = e; close(HereTask.this); up.send(); }
		}
	}
	
	private final MyPacketReceive packetReceive;
	private class MyPacketReceive implements PacketReceive {
		public boolean receive(Packet packet) {
			if (closed()) return false;
			try {
				
				// Look for the packet response
				Outline o = new Outline(packet.bin.data()); // Parse the UDP payload into an Outline
				if (o.name.equals("ap")) { // Address response
					if (o.has("hash") && !o.value("hash").equals(o.value().hash())) // Hash check
						throw new DataException("received corrupted ap");
					internet = new IpPort(o.value()); // Read
					close(HereTask.this); // It worked, we're done
					up.send();
					return true;
				}
			}
			catch (DataException e) { Mistake.log(e); }
			catch (ProgramException e) { exception = e; close(HereTask.this); up.send(); }
			return false;
		}
	}
}
