package pipe.main;

import javax.swing.SwingUtilities;

import base.process.Mistake;

public class Main {
	
	// Define
	
	/** true to build to release to users, false to include debugging information. */
	public static final boolean release = false;
	
	/** The name of this program. */
	public static final String name = "Pipe";
	/** Text that describes the version of this program. */
	public static final String version = "2009 May 19";
	/** Text prefix for exchange codes. */
	public static final String flag = "PIPE";
	
	// Main

	// When the program runs, Java calls this main() method
	public static void main(String[] arguments) {
		SwingUtilities.invokeLater(new Runnable() { // Have the normal Swing thread call this run() method
			public void run() {
				try {

					// Make and start the program
					new Program();

				} catch (Exception e) { Mistake.stop(e); } // Stop the program for an Exception we didn't expect
			}
		});
	}
}
