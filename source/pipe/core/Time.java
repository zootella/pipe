package pipe.core;

import pipe.main.Program;
import base.data.Data;
import base.internet.name.Ip;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.packet.PacketMachine;
import base.internet.web.DomainTask;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

public class Time extends Close {
	
	public Time(Program program, Update up, String site, Port port, Data data) {
		this.program = program;
		this.up = up;
		
		this.data = data;
		this.site = site;
		this.port = port;
		
		update = new Update(new MyReceive());
		update.send();
	}
	
	private final Data data;
	private final String site;
	private final Port port;
	
	private final Program program;
	private final Update up;
	private final Update update;
	
	private PacketMachine machine;
	private DomainTask domain;
	private Ip ip;
	public Data answer;
	
	public Exception exception() { return exception; }
	private Exception exception;

	@Override public void close() {
		if (already()) return;
		
		close(machine);
	}

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				/*
				
				// Make
				if (no(machine))
					machine = new PacketMachine(port);

				// Turn domain name into IP address
				if (no(domain))
					domain = new DomainTask(update, site);
				if (done(domain) && ip == null) {
					ip = domain.result();
					machine.send(data, new IpPort(ip, port));
				}
				
				if (machine.has() && answer == null) {
					answer = machine.look().bin.data().copyData();
					machine.done();
					up.send();
				}
				*/

			} catch (Exception e) { exception = e; close(); }
		}
	}
	
	
	
	
	
}
