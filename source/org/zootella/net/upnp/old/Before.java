package org.zootella.net.upnp.old;

import org.cybergarage.upnp.ControlPoint;
import org.zootella.net.name.Ip;
import org.zootella.net.name.IpPort;
import org.zootella.net.name.Port;
import org.zootella.net.upnp.Access;
import org.zootella.net.upnp.Listen;
import org.zootella.net.upnp.name.Map;
import org.zootella.net.upnp.name.MapResult;
import org.zootella.net.upnp.task.StartTask;
import org.zootella.net.upnp.task.AddTask;
import org.zootella.net.upnp.task.IpTask;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.Receive;
import org.zootella.state.Update;
import org.zootella.time.Now;

public class Before extends Close {
	
	public Before(Update up) {
		this.up = up;
		update = new Update(new MyReceive());
		
		listen = new Listen(update);
		start = new StartTask(update, listen.listener);
		
		Now.say("start");
	}
	
	private final Update up;
	private final Update update;
	
	private final StartTask start;
	private ControlPoint controlPoint;
	private final Listen listen;
	private Access access;
	
	private IpTask ipTask;
	
	private Ip ip;
	
	private AddTask addTask;
	private MapResult mapResult;

	@Override public void close() {
		if (already()) return;

		close(start);

		try {
			controlPoint.stop();
		} catch (Throwable t) { Mistake.log(t); }
	}

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			
			if (controlPoint == null && done(start)) {
				controlPoint = start.result();
				Now.say("control point");
			}
			
			if (access == null && listen.access() != null) {
				access = listen.access();
				Now.say(access.o.value("friendlyname").toString());
			}
			
			if (no(ipTask) && access != null)
				ipTask = new IpTask(update, listen.access());
			
			if (ip == null && done(ipTask)) {
				ip = ipTask.result().ip;
				Now.say(ip.toString());
			}
			
			if (no(addTask) && access != null) {
				Map f = new Map(new Port(12345), new IpPort("192.168.1.100:12345"), "TCP", "PipeTest1");
				addTask = new AddTask(update, access, f);
				Now.say("made forward task");
			}
			
			if (mapResult == null && done(addTask)) {
				mapResult = addTask.result();
				Now.say("forward result " + mapResult.toString());
			}
			
			

		}
	}
	
	
	
	

}
