package pipe.main;

import base.data.Bin;
import base.data.Data;
import base.encode.Encode;
import base.encode.HashValve;
import base.file.Open;
import base.file.OpenTask;
import base.file.Path;
import base.file.ReadValve;
import base.internet.name.Ip;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.packet.ListenPacket;
import base.internet.packet.Packet;
import base.internet.packet.ReceiveTask;
import base.internet.packet.SendTask;
import base.internet.web.DomainTask;
import base.state.Close;
import base.state.Receive;
import base.state.Update;
import base.valve.Flow;

public class Time extends Close {
	
	public Time(Program program, Update up, String site, Port port) {
		this.program = program;
		this.up = up;
		
		this.site = site;
		this.port = port;
		
		sendBin = Bin.big();
		sendBin.add(Encode.fromBase16("00"));
		receiveBin = Bin.big();
		
		update = new Update(new MyReceive());
		update.send();
	}
	
	private final String site;
	private final Port port;
	
	private final Program program;
	private final Update up;
	private final Update update;
	
	private ListenPacket listen;
	private SendTask sendTask;
	private final Bin sendBin;
	private final Bin receiveBin;
	private Ip siteIp;
	private DomainTask domain;
	private ReceiveTask receive;
	
	private Packet arrived;
	
	public Exception exception() { return exception; }
	private Exception exception;

	@Override public void close() {
		if (already()) return;

		
	}
	
	
	// Receive

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				// Make
				if (no(listen))
					listen = new ListenPacket(new Port(1234));

				// Receive
				if (no(receive))
					receive = new ReceiveTask(update, listen, receiveBin);
				if (done(receive))
					arrived = receive.result();

				// Send
				if (no(domain))
					domain = new DomainTask(update, site);
				if (done(domain))
					siteIp = domain.result();
				if (no(sendTask) && siteIp != null)
					sendTask = new SendTask(update, listen, sendBin, new IpPort(siteIp, port));
				
				
				

			} catch (Exception e) { exception = e; close(); }
		}
	}
	
	// in addition to doing it that way, do it as a single Task
	// between these two, you can be flexible for everything
	// later, split this into two objects, one that just talks on udp,
	// the other that uses that one to contact the time server
	
	
	
	
}
