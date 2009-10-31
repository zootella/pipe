package org.zootella.desktop;

public class Desktop {

	/** true if we're running on a Mac, false for Windows or Linux. */
	public static boolean isMac() {
		return System.getProperty("mrj.version") != null; // Ask Java for a property that only exists on Mac
	}
}
