package pipe.main;

import javax.swing.SwingUtilities;

import base.process.Mistake;

public class Main {
	
	// Define
	
	/** The name of this program. */
	public static final String name = "Pipe";
	/** Text that describes the version of this program. */
	public static final String version = "2009 May 19";

	/** Domain name and port number of the central server. */
	public static final String central = "bootcloud.info:9193";
	
	// Main

	// When the program runs, Java calls this main() method
	public static void main(String[] arguments) {
		SwingUtilities.invokeLater(new Runnable() { // Have the normal Swing thread call this run() method
			public void run() {
				try {

					// Make and start the program
					new Program();

				} catch (Exception e) { Mistake.grab(e); } // Exception starting up
			}
		});
	}
}
