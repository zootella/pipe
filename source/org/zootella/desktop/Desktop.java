package org.zootella.desktop;

public class Desktop {

	/** true if the desktop we're on has a Windows or Linux system tray, false a Mac dock. */
	public static boolean hasTray() {
		return !hasDock();
	}
	
	/** true if the desktop we're on has a Mac dock, false a Windows or Linux system tray. */
	public static boolean hasDock() {
		return System.getProperty("mrj.version") != null;
	}
}
