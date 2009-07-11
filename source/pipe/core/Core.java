package pipe.core;

import java.util.Map;

import pipe.main.Program;
import base.internet.name.Port;
import base.internet.packet.PacketMachine;
import base.process.Mistake;
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
		
		port = new Port(1234);

		pipes = new Pipes(program);
		
		packetMachine = new PacketMachine(port);
		
		
		
		model = new MyModel();
		
		
		receive = new MyReceive();
		pulse = new Pulse(receive, Time.second);
		update = new Update(receive);
		
		refreshHere();
	}

	private final Port port;
	private final Update update;
	private final Program program;
	public final Pipes pipes;
	public final PacketMachine packetMachine;

	private Here here;
	public Here.Result hereResult;
	
	private final Pulse pulse;
	
	
	public boolean canRefreshHere() {
		if (here != null) return false; // Can't refresh because we've got a Here working right now
		if (hereResult == null) return true; // No Here result at all yet, so yes, we can refresh
		return hereResult.age.expired(5 * Time.second); // Allow refresh if our result is more than 5 seconds old
	}

	public void refreshHere() {
		if (!canRefreshHere()) return;
		
		here = new Here(update, port, packetMachine);
		model.changed();
		
		
		
	}
	

	@Override public void close() {
		if (already()) return;
		
		close(pipes);
		close(packetMachine);
		close(here);
		close(model);
		close(pulse);
	}
	
	private final MyReceive receive;
	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				if (done(here)) {
					hereResult = here.result();
					here = null;
					model.changed();
				}
				
				model.changed();

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	

	
	/** This object's Model gives View objects above what they need to show us to the user. */
	private Core me() { return this; } // Give the inner class a link to this outer object
	public final MyModel model;
	public class MyModel extends Model {
		@Override public Object out() { return me(); } // The outer object that made and contains this Model
		@Override public Map<String, String> view() { return null; }
		
		public boolean canRefresh() {
			return canRefreshHere();
		}
		
		public String lan() {
			if (hereResult == null || hereResult.lan == null)
				return "";
			return hereResult.lan.toString();
		}
		public String net() {
			if (hereResult == null || hereResult.net == null)
				return "";
			return hereResult.net.toString();
		}
		public String age() {
			if (hereResult == null || hereResult.age == null)
				return "";
			return Describe.timeCoarse(hereResult.age.age()) + " ago";
		}
	}
	
	

}
