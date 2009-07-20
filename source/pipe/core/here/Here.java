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
import base.state.Receive;
import base.state.Update;
import base.time.Now;
import base.time.Time;

public class Here extends Close {
	
	// Make
	
	public Here(Update up, Port port, Packets packets) {
		this.up = up;
		this.port = port;
		this.packets = packets;
		update = new Update(new MyReceive());
		refresh();
	}
	
	private final Port port;
	private final Packets packets;
	private final Update up;
	private final Update update;
	
	private HereTask task; // The HereTask we used most recently to find internet at age
	private ProgramException exception; // The most recent exception from task
	private IpPort internet; // The most recent good Internet IP address we found for ourselves
	private Now age; // When we found internet

	@Override public void close() {
		if (already()) return;
		close(task);
	}

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			try {
				
				if (done(task) && task.result.once()) {
					internet = task.internet();
					age = task.age();
					up.send();
				}

			} catch (ProgramException e) { exception = e; }
		}
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
}
