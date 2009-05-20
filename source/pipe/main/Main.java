package pipe.main;

import javax.swing.SwingUtilities;

import base.exception.Error;
import base.setting.Store;

import pipe.user.Window;

public class Main {
	
	// Define
	
	/** The name of this program. */
	public static final String name = "Pipe";
	/** Text that describes the version of this program. */
	public static final String version = "2009 May 19";
	
	// Main

	// When the program runs, Java calls this main() method
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() { // Have the normal Swing thread call this run() method
        	public void run() {
        		try {

        			// Start the program
        			Store store = new Store();            // Read settings file from last time
        			Program program = new Program(store); // Make the program that does everything
        			new Window(program);                  // Show the program with a window on the screen

        		} catch (Exception e) { Error.error(e); } // Exception starting up
        	}
    	});
	}
}
