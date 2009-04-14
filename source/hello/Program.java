package hello;

import hello.user.Window;


import base.setting.Store;


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
		store = new Store();       // Open Store.txt from the last time the program ran
		
		window = new Window(this); // Making the Window object puts the program's window on the screen
	}

	/** The program's Store object keeps settings and data in Store.txt when it's not running. */
	public Store store;
	/** The program's Window object, which is the window on the screen. */
	public Window window;
	
	// -------- Pulse the program as it runs, and close it --------

	/** Close all the objects in the program that need to be closed before the program closes. */
	public void close() {

		// Close our objects that need to be closed
		window.close();   // Have the tabs save data to the Store object's Outline
		store.close();    // Save Store.txt for the next time the program runs
	}
}
