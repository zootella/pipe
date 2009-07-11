package pipe.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

import pipe.center.Center;
import base.data.Bin;
import base.data.Data;
import base.data.Number;
import base.data.Outline;
import base.data.Text;
import base.encode.Hash;
import base.exception.DataException;
import base.exception.PlatformException;
import base.exception.TimeException;
import base.internet.name.Ip;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.packet.Packet;
import base.internet.packet.PacketMachine;
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
	
	public Here(Update up, Port port, PacketMachine packetMachine) {
		
		// Get information we can get right now
		now = new Now(); // When we did this
		try {
			lan = new IpPort(new Ip(InetAddress.getLocalHost()), port); // Our internal IP address on the LAN
		} catch (UnknownHostException e) { throw new PlatformException(e); }

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

	private final Update up;
	private final Update update;
	private final PacketMachine packetMachine;
	private final Pulse pulse;
	private DomainTask domain;
	private IpPort central;
	private Now sent;

	@Override public void close() {
		if (already()) return;
		
		packetMachine.remove(packetReceive);
		close(pulse);
		close(domain);
	}

	// Look
	
	/** When this Here was made and started collecting its information. */
	public final Now now;
	/** Our IP address and port number on the LAN. */
	public final IpPort lan;
	/** Our IP address and port number on the Internet, null before we know. */
	public IpPort internet() { return internet; }
	private IpPort internet;
	/** The Exception that made us give up, null if everything worked. */
	public Exception exception() { return exception; }
	private Exception exception;

	// Do

	private final MyReceive receive;
	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				// Look up the IP address of the central server
				if (no(domain))
					domain = new DomainTask(update, Text.before(Center.site, ":"));
				if (done(domain) && central == null)
					central = new IpPort(domain.result(), new Port(Number.toInt(Text.after(Center.site, ":"))));

				// Send the central server a UDP packet to find out what our IP address is
				if (central != null && internet == null && sent == null) {
					packetMachine.send((new Outline("aq")).toData(), central);
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
					internet = new IpPort(o.getData()); // Read
					up.send(); // It worked
				}

			}
			catch (DataException e) { Mistake.ignore(e); }
			catch (Exception e) { exception = e; close(); up.send(); }
		}
	}
}
