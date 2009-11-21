package org.everpipe.core.here;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.zootella.exception.NetException;
import org.zootella.exception.ProgramException;
import org.zootella.net.name.Ip;
import org.zootella.state.Result;
import org.zootella.time.Duration;
import org.zootella.time.Now;

public class HereLan {
	
	public static Result<Ip> ip() {
		Ip ip = null;
		Now now = new Now();
		ProgramException exception = null;
		try {
			ip = new Ip(InetAddress.getLocalHost());
		} catch (UnknownHostException e) { exception = new NetException(e); }
		Duration duration = new Duration(now);
		return new Result<Ip>(ip, duration, exception);
	}
}
