package pipe.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import pipe.center.Center;
import base.data.Data;
import base.data.Number;
import base.data.Outline;
import base.data.Text;
import base.exception.PlatformException;
import base.internet.name.Ip;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.packet.Packet;
import base.internet.packet.PacketMachine;
import base.internet.packet.PacketReceive;
import base.internet.web.DomainTask;
import base.process.Mistake;
import base.state.Close;
import base.state.Model;
import base.state.Receive;
import base.state.Update;
import base.time.Ago;
import base.time.Now;
import base.time.Time;

/** A Here figures out what our IP address is once and right now. */
public class Here extends Close {
	
	// Make
	
	public Here(Update up, Port port, PacketMachine packetMachine) {
		
		// Get information we can get right now
		now = new Now(); // When we did this
		this.port = port; // The given port number we're listening on
		try {
			lan = new Ip(InetAddress.getLocalHost()); // Our internal IP address on the LAN
		} catch (UnknownHostException e) { throw new PlatformException(e); }

		// Save and connect the given object that sends UDP packets
		this.packetMachine = packetMachine;
		packetReceive = new MyPacketReceive();
		packetMachine.add(packetReceive);

		// Save and connect our Update objects
		this.up = up;
		update = new Update(new MyReceive());
		update.send();
	}

	private final Update up;
	private final Update update;
	private final PacketMachine packetMachine;
	private DomainTask domain;
	private IpPort central;
	private Now sent;

	@Override public void close() {
		if (already()) return;
		
		packetMachine.remove(packetReceive);
		close(domain);
	}

	// Look
	
	/** When this Here was made and collected its information. */
	public final Now now;
	/** The local port number we're listening on. */
	public final Port port;
	/** Our IP address on the LAN. */
	public final Ip lan;
	/** Our IP address on the Internet, null before we know. */
	public Ip internet() { return internet; }
	private Ip internet;
	/** The Exception that made us give up, null if everything worked. */
	public Exception exception() { return exception; }
	private Exception exception;

	// Do

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

			} catch (Exception e) { exception = e; close(); up.send(); }
		}
	}
	
	private final MyPacketReceive packetReceive;
	private class MyPacketReceive implements PacketReceive {
		public void receive(Packet packet) {
			if (closed()) return;
			try {
				
				
				
				
				
				
				
				
			} catch (Exception e) { exception = e; close(); up.send(); }
		}
	}
	

	// have this thing work within 4 seconds or fail with a WaitException exception
	// have a little separate 4 second timer with its own receive that races the normal thing and does this
	// no, it can use the same MyReceive
	
	
	
	
	

}
