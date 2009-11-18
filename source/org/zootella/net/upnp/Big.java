package org.zootella.net.upnp;

import org.cybergarage.upnp.ControlPoint;
import org.zootella.net.upnp.name.IpResult;
import org.zootella.net.upnp.name.Map;
import org.zootella.net.upnp.name.MapResult;
import org.zootella.net.upnp.task.AddTask;
import org.zootella.net.upnp.task.IpTask;
import org.zootella.net.upnp.task.StartTask;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.Receive;
import org.zootella.state.Update;

public class Big extends Close {
	
	public Big(Update up, Map tcp, Map udp) {
		this.up = up;
		update = new Update(new MyReceive());

		tcpMap = tcp;
		udpMap = udp;

		listen = new Listen(update);
		startTask = new StartTask(update, listen.listen);
		log("start");
	}
	
	private final Map tcpMap;
	private final Map udpMap;
	
	private final Update up;
	private final Update update;
	
	private Listen listen;
	private StartTask startTask;
	private ControlPoint controlPoint;
	private Access access;
	
	private IpTask ipTask;
	private IpResult ipResult;
	
	private AddTask tcpTask;
	private MapResult tcpResult;
	
	private AddTask udpTask;
	private MapResult udpResult;

	@Override public void close() {
		if (already()) return;
		
		close(udpTask);
		close(tcpTask);
		close(ipTask);
		try { controlPoint.stop(); } catch (Throwable t) { Mistake.log(t); }
		close(startTask);
	}

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;

			if (controlPoint == null && done(startTask))
				controlPoint = startTask.result();
			if (access == null && listen.access() != null) {
				access = listen.access();
				up.send();
				log("access " + access.o.value("friendlyname"));
			}
			
			if (no(ipTask) && access != null)
				ipTask = new IpTask(update, listen.access());
			if (ipResult == null && done(ipTask)) {
				ipResult = ipTask.result();
				up.send();
				log("ip " + ipResult.ip.toString() + ipResult.duration.toString());
			}
			
			if (no(tcpTask) && access != null)
				tcpTask = new AddTask(update, access, tcpMap);
			if (tcpResult == null && done(tcpTask)) {
				tcpResult = tcpTask.result();
				up.send();
				log("tcp map made" + tcpResult.duration.toString());
			}
			
			if (no(udpTask) && access != null)
				udpTask = new AddTask(update, access, udpMap);
			if (udpResult == null && done(udpTask)) {
				udpResult = udpTask.result();
				up.send();
				log("udp map made" + udpResult.duration.toString());
			}
		}
	}
}
