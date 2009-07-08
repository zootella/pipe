package pipe.core;

import java.net.InetAddress;
import java.util.Map;

import pipe.main.Main;
import pipe.main.Program;
import base.data.Number;
import base.data.Text;
import base.encode.Encode;
import base.internet.name.Ip;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.web.DomainTask;
import base.process.Mistake;
import base.state.Close;
import base.state.Model;
import base.state.Receive;
import base.state.Update;

public class Here extends Close {
	
	public Here(Program program) {
		this.program = program;
		update = new Update(new MyReceive());
		update.send();
		model = new MyModel();
		
		
		// choose our random port number or get it from settings
		port = new Port(1234);
	}
	
	private final Program program;
	
	private final Update update;
	
	public final Port port;
	
	private Ip lan;
	private Ip internet;

	public Ip lan() { return lan; }
	public Ip internet() { return internet; }
	
	public void refresh() {
		update.send();
		
	}
	
	
	private DomainTask domain;
	private IpPort central;
	
	
	
	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				// Look up the IP address of the central server
				if (no(domain))
					domain = new DomainTask(update, Text.before(Main.central, ":"));
				if (done(domain) && central == null)
					central = new IpPort(domain.result(), new Port(Number.toInt(Text.after(Main.central, ":"))));
				
				// Check our fake internal LAN IP address
				lan = new Ip(InetAddress.getLocalHost());

				if (central != null && internet == null)
					program.core.packet.send(Encode.fromBase16("0a"), new IpPort("127.0.0.1:9193"));

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	
	
	
	
	
	
	
	
	/** This object's Model gives View objects above what they need to show us to the user. */
	private Here me() { return this; } // Give the inner class a link to this outer object
	public final MyModel model;
	public class MyModel extends Model {
		@Override public Object out() { return me(); } // The outer object that made and contains this Model
		@Override public Map<String, String> view() { return null; }
		
		public String port()     { return text(port); }
		public String lan()      { return text(lan); }
		public String internet() { return text(internet); }
		public String age()      { return "not implmented yet"; }
	}
	
	
	public static String text(int i) {
		return i + "";
	}
	public static String text(long l) {
		return l + "";
	}
	public static String text(Object o) {
		if (o == null) return "";
		return o.toString();
	}
	

	@Override public void close() {
		if (already()) return;
		
		close(model);
	}

}