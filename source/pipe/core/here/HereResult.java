package pipe.core.here;

import base.exception.ProgramException;
import base.internet.name.IpPort;
import base.time.Now;

public class HereResult {
	
	public HereResult(IpPort lan, IpPort internet, ProgramException exception) {
		this.made = new Now();
		this.lan = lan;
		this.internet = internet;
		this.exception = exception;
	}
	
	public final Now made;
	public final IpPort lan;
	public final IpPort internet;
	public final ProgramException exception;
}
