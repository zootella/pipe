package pipe.core;

import java.net.InetAddress;

import pipe.center.Center;
import base.data.Number;
import base.data.Outline;
import base.data.Text;
import base.exception.DataException;
import base.exception.TimeException;
import base.internet.name.Ip;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.packet.Packet;
import base.internet.packet.Packets;
import base.internet.packet.PacketReceive;
import base.internet.web.DomainTask;
import base.process.Mistake;
import base.state.Close;
import base.state.Pulse;
import base.state.Receive;
import base.state.Update;
import base.time.Now;
import base.time.Time;

/** A Here figures out what our IP address is once and right now. */
public class Here extends Close {
	
	// Make
	
	public Here(Update up, Port port, Packets packetMachine) {
		
		this.port = port;

		// Save and connect the given object that sends UDP packets
		this.packetMachine = packetMachine;
		packetReceive = new MyPacketReceive();
		packetMachine.add(packetReceive);

		// Save and connect our Update objects
		this.up = up;
		receive = new MyReceive();
		pulse = new Pulse(receive, Time.second);
		update = new Update(receive);
		update.send();
	}

	private final Port port;
	private final Update up;
	private final Update update;
	private final Packets packetMachine;
	private final Pulse pulse;
	
	private DomainTask domain;
	private IpPort center;
	private Now sent;
	
	private IpPort lan;
	private IpPort net;
	private Exception exception;

	@Override public void close() {
		if (already()) return;
		
		packetMachine.remove(packetReceive);
		close(pulse);
		close(domain);
	}

	// Result
	
	public Result result() {
		return new Result(lan, net, exception);
	}
	
	public class Result {
		public Result(IpPort lan, IpPort internet, Exception exception) {
			this.age = new Now();
			this.lan = lan;
			this.net = internet;
			this.exception = exception;
		}
		public final Now age;
		public final IpPort lan;
		public final IpPort net;
		public final Exception exception;
	}

	// Do

	private final MyReceive receive;
	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				// Get our fake internal LAN IP address
				if (lan == null)
					lan = new IpPort(new Ip(InetAddress.getLocalHost()), port); // Our internal IP address on the LAN

				// Look up the IP address of the central server
				if (no(domain))
					domain = new DomainTask(update, Text.before(Center.site, ":"));
				if (done(domain) && center == null)
					center = new IpPort(domain.result(), new Port(Number.toInt(Text.after(Center.site, ":"))));

				// Send the central server a UDP packet to find out what our IP address is
				if (center != null && net == null && sent == null) {
					packetMachine.send((new Outline("aq")).toData(), center);
					sent = new Now();
				}

				// Don't wait too long for the response
				if (sent != null && sent.expired(4 * Time.second))
					throw new TimeException();

			} catch (Exception e) { exception = e; close(); up.send(); }
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
					close(); // It worked, we're done
					up.send();
				}
			}
			catch (DataException e) { Mistake.ignore(e); }
			catch (Exception e) { exception = e; close(); up.send(); }
		}
	}
}
