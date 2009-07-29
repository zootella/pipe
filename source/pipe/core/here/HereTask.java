package pipe.core.here;

import pipe.center.Center;
import base.data.Number;
import base.data.Outline;
import base.data.Text;
import base.exception.DataException;
import base.exception.ProgramException;
import base.net.name.IpPort;
import base.net.name.Port;
import base.net.packet.Packet;
import base.net.packet.PacketReceive;
import base.net.packet.Packets;
import base.net.web.DomainTask;
import base.process.Mistake;
import base.state.Close;
import base.state.Egg;
import base.state.Once;
import base.state.Receive;
import base.state.Update;
import base.time.Now;

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
		public void receive() throws Exception {
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

			} catch (ProgramException e) { exception = e; close(me()); up.send(); }
		}
	}
	
	private final MyPacketReceive packetReceive;
	private class MyPacketReceive implements PacketReceive {
		public void receive(Packet packet) {
			if (closed()) return;
			try {
				
				// Look for the packet response
				Outline o = new Outline(packet.bin.data()); // Parse the UDP payload into an Outline
				if (o.name.equals("ap")) { // Address response
					if (o.has("hash") && !o.value("hash").equals(o.value().hash())) // Hash check
						throw new DataException("received corrupted ap");
					internet = new IpPort(o.value()); // Read
					close(me()); // It worked, we're done
					up.send();
				}
			}
			catch (DataException e) { Mistake.log(e); }
			catch (ProgramException e) { exception = e; close(me()); up.send(); }
		}
	}
	private HereTask me() { return this; }
}
