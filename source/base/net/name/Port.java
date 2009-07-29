package base.net.name;

import base.data.Number;
import base.exception.DataException;

public class Port {
	
	// Define
	
	/** 0, the minimum possible port number. */
	public static final int minimum = 0;
	/** 65535, 0xffff in 2 bytes, the maximum possible port number. */
	public static final int maximum = 65535;
	
	// Make
	
	/** Make sure s is "0" through "65535" or throw a DataException. */
	public Port(String s) {
		this(Number.toInt(s));
	}

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
	
	// Random

	/** Pick a random port number to listen on. */
	public static Port random() {
		return new Port(Number.random().nextInt(50000) + 2000); // 2000 through 51999
	}
}
