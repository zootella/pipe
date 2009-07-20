package pipe.core;

import java.util.Map;

import pipe.core.here.Here;
import pipe.main.Program;
import base.internet.name.Port;
import base.internet.packet.Packets;
import base.state.Close;
import base.state.Model;
import base.state.Pulse;
import base.state.Receive;
import base.state.Update;
import base.time.Time;
import base.user.Describe;

/** The core program beneath the window that does everything. */
public class Core extends Close {

	public Core(Program program) {
		this.program = program;
		
		
		port = Port.random();

		pipes = new Pipes(program);
		
		packets = new Packets(port);
		
		
		
		model = new MyModel();
		
		
		receive = new MyReceive();
		pulse = new Pulse(receive, Time.second / 5); //shouldn't here call model.changed when it 
		update = new Update(receive);
		
		here = new Here(update, port, packets);
		model.changed();
	}

	private final Port port;
	private final Update update;
	private final Program program;
	public final Pipes pipes;
	public final Packets packets;

	public final Here here;
	
	private final Pulse pulse;
	
	@Override public void close() {
		if (already()) return;
		
		close(pipes);
		close(packets);
		close(here);
		close(model);
		close(pulse);
	}
	
	private final MyReceive receive;
	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			
			model.changed();
		}
	}
	

	
	/** This object's Model gives View objects above what they need to show us to the user. */
	private Core me() { return this; } // Give the inner class a link to this outer object
	public final MyModel model;
	public class MyModel extends Model {
		@Override public Object out() { return me(); } // The outer object that made and contains this Model
		@Override public Map<String, String> view() { return null; }
		
		public boolean canRefresh() {
			return here.canRefresh();
		}
		
		public String lan() {
			return here.lan().toString();
		}
		public String internet() {
			if (here.internet() == null) return "";
			return here.internet().toString();
		}
		public String age() {
			if (here.age() == null) return "";
			return Describe.timeCoarse(here.age().age()) + " ago";
		}
		public String exception() {
			if (here.exception() == null) return "";
			return here.exception().toString();
		}
	}
	
	

}
