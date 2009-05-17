package main;

import javax.swing.SwingUtilities;

public class Main {

	// When the program runs, Java calls this main() method
    public static void main(String[] args) {

    	// Have the normal Swing thread call this run() method
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
            	// Make the Program object that is the whole program
            	new Program();
            }
        });
    }
}
