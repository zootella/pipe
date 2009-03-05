package org.pipe.all;

import org.base.internet.old.OldInternet;
import org.base.setting.Store;
import org.base.state.old.OldPulse;
import org.base.web.Web;
import org.pipe.all.user.Window;


// this is the program object
// it will have members for all the objects the program needs as it runs
public class Program {
	
	// -------- Factory settings --------
	
	/** "Hello", the name of this program. */
	public static final String name = "Hello";
	/** "1.0", text that describes the version of this program. */
	public static final String versionText = "1.0";
	/** "Hello, World!", text for the window title. */
	public static final String title = "Hello, World!";

	// -------- The Program object, and the objects inside it --------

	/** Make the Program object, which represents the whole program and puts the window on the screen. */
	public Program() {

		// Make the objects that are a part of this new Program object
		pulse = new OldPulse(this);   // The Pulse constructor needs a link back up to this Program object
		store = new Store();       // Open Store.txt from the last time the program ran
		internet = new OldInternet();
		web = new Web(internet);   // The Web constructor needs a link to the Internet object
		
		window = new Window(this); // Making the Window object puts the program's window on the screen
	}

	/** The program's Pulse object calls the pulse() method as the program runs. */
	public OldPulse pulse;
	/** The program's Store object keeps settings and data in Store.txt when it's not running. */
	public Store store;
	/** The program's Internet object makes TCP socket connections and sends UDP packets. */
	public OldInternet internet;
	/** The program's Web object can download files from Web sites. */
	public Web web;
	/** The program's Window object, which is the window on the screen. */
	public Window window;
	
	// -------- Pulse the program as it runs, and close it --------

	/** Pulse all the objects in the program that need to get pulsed to notice what's changed. */
	public void pulse() {

		// Pulse our objects that need pulsing
		if (web != null) web.pulse();    // Pulse the Web object so it notices when its downloads are closer to done
	}

	/** Close all the objects in the program that need to be closed before the program closes. */
	public void close() {

		// Close our objects that need to be closed
		pulse.close();    // Close the Pulse object so it will stop its timer
		internet.close(); // Close the Internet object so it will disconnect its connections
		window.close();   // Have the tabs save data to the Store object's Outline
		store.close();    // Save Store.txt for the next time the program runs
	}
}
