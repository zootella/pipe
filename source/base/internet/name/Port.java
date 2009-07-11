package base.internet.name;

import base.exception.DataException;

public class Port {
	
	// Define
	
	/** 0, the minimum possible port number. */
	public static final int minimum = 0;
	/** 65535, 0xffff in 2 bytes, the maximum possible port number. */
	public static final int maximum = 65535;
	
	// Make

	/** Make sure port is 0 through 65535 or throw a DataException. */
	public Port(int port) {
		if (port < minimum || port > maximum) throw new DataException();
		this.port = port;
	}
	
	// Look
	
	/** The port number, 0 through 65535. */
	public final int port;
	
	// Text

	@Override public String toString() {
		return port + "";
	}
}
