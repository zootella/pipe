package org.zootella.net.upnp;

import org.cybergarage.upnp.ControlPoint;
import org.zootella.data.Outline;
import org.zootella.exception.ProgramException;
import org.zootella.exception.TimeException;
import org.zootella.net.name.Ip;
import org.zootella.net.upnp.name.Map;
import org.zootella.net.upnp.task.AddTask;
import org.zootella.net.upnp.task.IpTask;
import org.zootella.net.upnp.task.StartTask;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.Receive;
import org.zootella.state.Result;
import org.zootella.state.Update;
import org.zootella.time.Egg;
import org.zootella.time.Time;

public class Router extends Close {
	
	public Router(Update up, Map tcp, Map udp) {
		this.up = up;
		
		Receive receive = new MyReceive();
		update = new Update(receive);
		egg = new Egg(receive, 4 * Time.second);//TODO change to 20 seconds

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
	private final Egg egg;
	
	private Listen listen;
	private StartTask startTask;
	private ControlPoint control;
	private Access access;
	
	private Result<Outline> nameResult;
	public Result<Outline> name() { return nameResult; }
	public boolean hasName() { return nameResult != null; }
	
	private IpTask ipTask;
	private Result<Ip> ipResult;
	public Result<Ip> ip() { return ipResult; }
	public boolean hasIp() { return ipResult != null; }
	
	private AddTask tcpTask;
	private Result<Map> tcpResult;
	public Result<Map> tcp() { return tcpResult; }
	public boolean hasTcp() { return tcpResult != null; }
	
	private AddTask udpTask;
	private Result<Map> udpResult;
	public Result<Map> udp() { return udpResult; }
	public boolean hasUdp() { return udpResult != null; }

	@Override public void close() {
		if (already()) return;
		
		close(egg);
		close(udpTask);
		close(tcpTask);
		close(ipTask);
		try { control.stop(); } catch (Throwable t) { Mistake.log(t); }
		close(startTask);
		
		up.send();
	}

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				egg.check();
				
				if (control == null && done(startTask))
					control = startTask.result();
				if (access == null && listen.access() != null) {
					access = listen.access();
					nameResult = new Result<Outline>(access.o, whenMade());
					up.send();
					log("access " + access.o.value("friendlyname"));
				}
				
				if (no(ipTask) && access != null)
					ipTask = new IpTask(update, listen.access());
				if (ipResult == null && done(ipTask)) {
					ipResult = ipTask.result();
					up.send();
					log("ip " + ipResult.result().toString() + " " + ipResult.duration.toString());
				}
				
				if (no(tcpTask) && access != null)
					tcpTask = new AddTask(update, access, tcpMap);
				if (tcpResult == null && done(tcpTask)) {
					tcpResult = tcpTask.result();
					up.send();
					log("tcp " + " " + tcpResult.duration.toString());
				}
				
				if (no(udpTask) && access != null)
					udpTask = new AddTask(update, access, udpMap);
				if (udpResult == null && done(udpTask)) {
					udpResult = udpTask.result();
					up.send();
					log("udp " + " " + udpResult.duration.toString());
				}

			} catch (ProgramException e) { exception(e); close(Router.this); return; }
		}
	}
	
	private void exception(ProgramException e) {
		if (nameResult == null)
			nameResult = new Result<Outline>(null, whenMade(), e);
		if (ipResult == null)
			ipResult = new Result<Ip>(null, whenMade(), e);
		if (tcpResult == null)
			tcpResult = new Result<Map>(null, whenMade(), e);
		if (udpResult == null)
			udpResult = new Result<Map>(null, whenMade(), e);
	}
}
