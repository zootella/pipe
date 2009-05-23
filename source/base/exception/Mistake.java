package base.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class Mistake {
	
	/** Notice an exception that we can ignore, and let the program keep going. */
	public static void ignore(Exception e) {
		
		// Write it to standard output
		System.out.println("Mistake.ignore() caught an exception --v--");
		System.out.print(describe(e));
		System.out.println("Mistake.ignore() caught an exception --^--");
	}
	
	/** Show and report an exception code didn't catch, and terminate the Java process. */
	public static void grab(Exception e) {
		
		// Write it to standard output
		System.out.println("Mistake.grab() caught an exception --v--");
		System.out.print(describe(e));
		System.out.println("Mistake.grab() caught an exception --^--");

		// Show it to the user
		JOptionPane.showMessageDialog(null, describe(e), "Error.error() caught an exception", JOptionPane.ERROR_MESSAGE);

		// Send it in a packet to the programmer TODO

		// Terminate the process without closing the program properly
		System.exit(0); // This never returns
	}

	// Help

	/** Get the stack trace from an exception. */
	private static String describe(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter, true);
		e.printStackTrace(printWriter);
		printWriter.flush();
		stringWriter.flush();
		return stringWriter.toString();
	}
}
