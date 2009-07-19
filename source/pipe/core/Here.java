package pipe.core;

import java.net.InetAddress;

import pipe.center.Center;
import base.data.Number;
import base.data.Outline;
import base.data.Text;
import base.exception.DataException;
import base.exception.ProgramException;
import base.internet.name.Ip;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.packet.Packet;
import base.internet.packet.PacketReceive;
import base.internet.packet.Packets;
import base.internet.web.DomainTask;
import base.process.Mistake;
import base.state.Close;
import base.state.Egg;
import base.state.Once;
import base.state.Receive;
import base.state.Update;
import base.time.Now;

/** A Here figures out what our IP address is once and right now. */
public class Here extends Close {
	
	// Make
	
	public Here(Update up, Port port, Packets packets) {
		
		this.port = port;

		// Save and connect the given object that sends UDP packets
		this.packets = packets;
		packetReceive = new MyPacketReceive();
		packets.add(packetReceive);

		// Save and connect our Update objects
		this.up = up;
		receive = new MyReceive();
		egg = new Egg(receive);
		sent = new Once();
		update = new Update(receive);
		update.send();
	}

	private final Port port;
	private final Update up;
	private final Update update;
	private final Packets packets;
	private final Egg egg;
	private final Once sent;
	
	private DomainTask domain;
	private IpPort center;
	
	private IpPort lan;
	private IpPort net;
	private ProgramException exception;

	@Override public void close() {
		if (already()) return;
		
		packets.remove(packetReceive);
		close(egg);
		close(domain);
	}

	// Result
	
	public Result result() {
		return new Result(lan, net, exception);
	}
	
	public class Result {
		public Result(IpPort lan, IpPort internet, ProgramException exception) {
			this.age = new Now();
			this.lan = lan;
			this.net = internet;
			this.exception = exception;
		}
		public final Now age;
		public final IpPort lan;
		public final IpPort net;
		public final ProgramException exception;
	}

	// Do

	private final MyReceive receive;
	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			try {

				// Throw a TimeException if we've been trying to finish for more than 4 seconds
				egg.check();
				
				// Get our fake internal LAN IP address
				if (lan == null)
					lan = new IpPort(new Ip(InetAddress.getLocalHost()), port); // Our internal IP address on the LAN

				// Look up the IP address of the central server
				if (no(domain))
					domain = new DomainTask(update, Text.before(Center.site, ":"));
				if (done(domain) && center == null)
					center = new IpPort(domain.result(), new Port(Number.toInt(Text.after(Center.site, ":"))));

				// Send the central server a UDP packet to find out what our IP address is
				if (center != null && net == null && sent.once())
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
					if (o.has("hash") && !o.o("hash").getData().equals(o.getData().hash())) // Hash check
						throw new DataException("received corrupted ap");
					net = new IpPort(o.getData()); // Read
					close(me()); // It worked, we're done
					up.send();
				}
			}
			catch (DataException e) { Mistake.ignore(e); }
			catch (ProgramException e) { exception = e; close(me()); up.send(); }
		}
	}
	
	private Here me() { return this; }
}
