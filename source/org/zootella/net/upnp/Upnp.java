package org.zootella.net.upnp;

import org.cybergarage.upnp.ControlPoint;
import org.zootella.net.name.Ip;
import org.zootella.net.name.Port;
import org.zootella.net.upnp.task.StartTask;
import org.zootella.net.upnp.task.AddTask;
import org.zootella.net.upnp.task.IpTask;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.Receive;
import org.zootella.state.Update;
import org.zootella.time.Now;

public class Upnp extends Close {
	
	public Upnp(Update up) {
		this.up = up;
		update = new Update(new MyReceive());
		
		deviceService = new Listen(update);
		controlTask = new StartTask(update, deviceService.listener);
		
		Now.say("start");
	}
	
	private final Update up;
	private final Update update;
	
	private final StartTask controlTask;
	private ControlPoint controlPoint;
	private final Listen deviceService;
	private Router router;
	
	private IpTask ipTask;
	
	private Ip ip;
	
	private AddTask forwardTask;
	private Boolean forwardResult;

	@Override public void close() {
		if (already()) return;

		close(controlTask);

		try {
			controlPoint.stop();
		} catch (Throwable t) { Mistake.log(t); }
	}

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			
			if (controlPoint == null && done(controlTask)) {
				controlPoint = controlTask.result();
				Now.say("control point");
			}
			
			if (router == null && deviceService.router() != null) {
				router = deviceService.router();
				Now.say(router.o.value("friendlyname").toString());
			}
			
			if (no(ipTask) && router != null)
				ipTask = new IpTask(update, deviceService.router());
			
			if (ip == null && done(ipTask)) {
				ip = ipTask.result();
				Now.say(ip.toString());
			}
			
			if (no(forwardTask) && router != null) {
				Map f = new Map("", new Port(12345), "192.168.1.100", new Port(12345), "TCP", "PipeTest1");
				forwardTask = new AddTask(update, router, f);
				Now.say("made forward task");
			}
			
			if (forwardResult == null && done(forwardTask)) {
				forwardResult = forwardTask.result();
				Now.say("forward result " + forwardResult.toString());
			}
			
			

		}
	}
	
	
	
	

}
