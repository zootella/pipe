package pipe.core;

import java.util.Map;

import pipe.main.Program;
import base.internet.name.Port;
import base.internet.packet.PacketMachine;
import base.state.Close;
import base.state.Model;

/** The core program beneath the window that does everything. */
public class Core extends Close {

	public Core(Program program) {
		this.program = program;
		
		Port port = new Port(1234);

		pipes = new Pipes(program);
		
		packetMachine = new PacketMachine(port);
		
		
		
		/*
		
		model = new MyModel();
		
		update = new Update(new MyReceive());
		update.send();
		here = new Here(update, port, packetMachine);
		*/

	}

	private final Program program;
	public final Pipes pipes;
	public final PacketMachine packetMachine;

	public Here here;
	private boolean refreshHere;
	

	@Override public void close() {
		if (already()) return;
		
		close(pipes);
		close(packetMachine);
		close(here);
	}
	
	/*
	
	/** This object's Model gives View objects above what they need to show us to the user. *
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
	
	*/
	

}
