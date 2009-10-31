package org.zootella.process;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import org.zootella.state.Close;

public class Mistake {

	/** Ignore t. */
	public static void ignore(Throwable t) {} // Actually ignore it
	
	/** Log t, but let the program keep running. */
	public static void log(Throwable t) {
		String title = "Mistake log:"; // Compose
		String body = describe(t);
		
		log(title, body); // Report
		send(title + "\n" + body);
	}
	
	/** Log t and stop the program. */
	public static void stop(Throwable t) {
		String title = "Mistake stop:"; // Compose
		String body = describe(t);
		
		log(title, body); // Report
		send(title + "\n" + body);
		show(title, body);
		System.exit(0); // Terminate the process right here without closing the program properly
	}

	/** Make sure the program closed all the objects that needed to be closed. */
	public static void closeCheck() {
		int open = Close.checkAll();
		if (open == 0) return; // Check
		String title = "Mistake close:"; // Compose
		String body = open + " objects still open\n";

		log(title, body); // Report
		send(title + "\n" + body);
		show(title, body);
		System.exit(0); // Terminate the process right here without closing the program properly
	}

	// Help

	/** Get the stack trace from an exception. */
	private static String describe(Throwable t) {
		StringWriter s = new StringWriter();
		PrintWriter p = new PrintWriter(s, true);
		t.printStackTrace(p);
		p.flush();
		s.flush();
		return s.toString();
	}
	
	/** Make a note in the local debugging log. */
	private static void log(String title, String body) {
		System.out.print(title + "\n");
		System.out.print(body);
	}
	
	/** Send the error in a packet to the programmer. */
	private static void send(String body) {
		try {
			//TODO
		} catch (Throwable t) { Mistake.ignore(t); } // Ignore an exception and keep going
	}
	
	/** Show the error to the user. */
	private static void show(String title, String body) {
		try {
			JOptionPane.showMessageDialog(null, body, title, JOptionPane.ERROR_MESSAGE);
		} catch (Throwable t) { Mistake.ignore(t); } // Ignore an exception and keep going
	}
}
