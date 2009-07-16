package base.process;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import base.state.Close;

public class Mistake {
	
	/** Notice an exception that we can ignore, and let the program keep going. */
	public static void ignore(Exception e) {
		String title = "Mistake.ignore() caught an exception"; // Compose
		String body = describe(e);
		
		log(title, body); // Report
		send(title + "\n" + body);
	}
	
	/** Show and report an exception code didn't catch, and terminate the Java process. */
	public static void stop(Exception e) {
		String title = "Mistake.stop() caught an exception"; // Compose
		String body = describe(e);
		
		log(title, body); // Report
		send(title + "\n" + body);
		show(title, body);
		System.exit(0); // Terminate the process right here without closing the program properly
	}

	/** Make sure the program closed all the objects that needed to be closed. */
	public static void closeCheck() {
		if (Close.checkAll() == 0) return; // Check
		String title = "Mistake.close() found open objects"; // Compose
		String body = Close.checkAll() + " objects still open\n";

		log(title, body); // Report
		send(title + "\n" + body);
		show(title, body);
		System.exit(0); // Terminate the process right here without closing the program properly
	}

	// Help

	/** Get the stack trace from an exception. */
	private static String describe(Exception e) {
		StringWriter s = new StringWriter();
		PrintWriter p = new PrintWriter(s, true);
		e.printStackTrace(p);
		p.flush();
		s.flush();
		
		String body = e.toString() + "\n" + s.toString();
		
		return body;
	}
	
	/** Make a note in the local debugging log. */
	private static void log(String title, String body) {
		System.out.print(title + " --v--\n");
		System.out.print(body);
		System.out.print(title + " --^--\n");
	}
	
	/** Send the error in a packet to the programmer. */
	private static void send(String body) {
		try {
			//TODO
		} catch (Exception e) {} // Ignore an exception and keep going
	}
	
	/** Show the error to the user. */
	private static void show(String title, String body) {
		try {
			JOptionPane.showMessageDialog(null, body, title, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {} // Ignore an exception and keep going
	}
}
