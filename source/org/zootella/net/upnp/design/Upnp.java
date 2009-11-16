package org.zootella.net.upnp.design;

import org.cybergarage.upnp.ControlPoint;
import org.zootella.net.name.Ip;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.Receive;
import org.zootella.state.Update;
import org.zootella.time.Now;

public class Upnp extends Close {
	
	public Upnp(Update up) {
		this.up = up;
		update = new Update(new MyReceive());
		
		deviceService = new DeviceService(update);
		controlTask = new ControlTask(update, deviceService.listener);
		
		Now.say("start");
	}
	
	private final Update up;
	private final Update update;
	
	private final ControlTask controlTask;
	private ControlPoint controlPoint;
	private final DeviceService deviceService;
	private UpnpDevice upnpDevice;
	
	private IpTask ipTask;
	
	private Ip ip;

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
			
			if (upnpDevice == null && deviceService.device() != null) {
				upnpDevice = deviceService.device();
				Now.say(upnpDevice.o.value("friendlyname").toString());
			}
			
			if (no(ipTask) && upnpDevice != null)
				ipTask = new IpTask(update, deviceService.device());
			
			if (ip == null && done(ipTask)) {
				ip = ipTask.result();
				Now.say(ip.toString());
			}
			
			

		}
	}
	
	
	
	

}
