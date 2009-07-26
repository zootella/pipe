package pipe.core.here;

import java.net.InetAddress;
import java.net.UnknownHostException;

import base.exception.PlatformException;
import base.exception.ProgramException;
import base.internet.name.Ip;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.packet.Packets;
import base.state.Close;
import base.state.Model;
import base.state.Receive;
import base.state.Update;
import base.time.Now;
import base.time.Time;
import base.user.Describe;

public class Here extends Close {
	
	// Make
	
	public Here(Port port, Packets packets) {
		this.port = port;
		this.packets = packets;

		update = new Update(new MyReceive());
		refresh();
		
		model = new MyModel();
		model.pulse();
		model.changed();
	}
	
	private final Port port;
	private final Packets packets;
	private final Update update;
	
	private HereTask task; // The HereTask we used most recently to find internet at age
	private ProgramException exception; // The most recent exception from task
	private IpPort internet; // The most recent good Internet IP address we found for ourselves
	private Now age; // When we found internet

	@Override public void close() {
		if (already()) return;
		close(task);
		close(model);
	}

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			try {
				
				if (done(task) && task.result.once()) {
					internet = task.internet();
					age = task.age();
					model.changed();
				}

			} catch (ProgramException e) { exception = e; }
		}
	}
	
	// Do
	
	/** true if we've waited long enough to try to find our Internet IP address again. */
	public boolean canRefresh() {
		if (task == null) return true;
		if (!done(task)) return false;
		
		
		return task.age().expired(4 * Time.second);
		
		
		
	}
	
	/** Send a UDP packet to the central server to find out what our Internet IP address is. */
	public void refresh() {
		if (!canRefresh()) return;
		
		close(task);
		task = new HereTask(update, port, packets);
		
	}

	// Look

	/** Our internal IP address and listening port number on the LAN right now. */
	public IpPort lan() {
		try {
			return new IpPort(new Ip(InetAddress.getLocalHost()), port);
		} catch (UnknownHostException e) { throw new PlatformException(e); }
	}

	/** The most recent ProgramException that prevented us from finding out our Internet IP address. */
	public ProgramException exception() { return exception; }
	/** The most recent valid Internet IP address we've determined we have, null if we don't know yet. */
	public IpPort internet() { return internet; }
	/** When we found internet(). */
	public Now age() { return age; }
	
	// Model

	public final MyModel model;
	public class MyModel extends Model {
		
		public boolean canRefresh() { return me().canRefresh(); }
		
		public String lan()      { return Describe.object((me().lan())); }
		public String internet() { return Describe.object((me().internet())); }
		
		public String age() {
			if (me().age() == null) return "";
			return Describe.timeCoarse(me().age().age()) + " ago";
		}
		
		public String exception() { return Describe.object((me().exception())); }
	}
	private Here me() { return this; } // Give the inner class a link to this outer object
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
