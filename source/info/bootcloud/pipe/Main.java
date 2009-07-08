package info.bootcloud.pipe;

import javax.swing.SwingUtilities;

public class Main {

	// When the program runs, Java calls this main() method
    public static void main(String[] arguments) {
    	SwingUtilities.invokeLater(new Runnable() { // Have the normal Swing thread call this run() method
        	public void run() {
        		try {
        			
        			// Start the program
        			new Program();

        		} catch (Exception e) { Mistake.grab(e); } // Exception starting up
        	}
    	});
	}
}
