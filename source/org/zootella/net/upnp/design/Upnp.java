package org.zootella.net.upnp.design;

import org.cybergarage.upnp.ControlPoint;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.Once;
import org.zootella.state.Receive;
import org.zootella.state.Update;

public class Upnp extends Close {
	
	public Upnp(Update up) {
		this.up = up;
		update = new Update(new MyReceive());
		
		device = new DeviceService(update);
		controlTask = new ControlTask(update, device.listener);
		
		once = new Once();
	}
	
	private final Update up;
	private final Update update;
	
	private final ControlTask controlTask;
	private ControlPoint controlPoint;
	private final DeviceService device;

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
			
			if (controlPoint == null && done(controlTask))
				controlPoint = controlTask.result();
			
			if (device.device() != null && once.once())
				System.out.println(device.name().toString());
			

		}
	}
	
	private final Once once;
	
	
	

}
