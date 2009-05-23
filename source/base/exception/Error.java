package base.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class Error {
	
	/** Show and report an exception code didn't catch, and terminate the Java process. */
	public static void error(Exception e) {
		
		// Write it to standard output
		System.out.println("Error.error() caught an exception --v--");
		System.out.print(describe(e));
		System.out.println("Error.error() caught an exception --^--");

		// Show it to the user
		JOptionPane.showMessageDialog(null, describe(e), "Error.error() caught an exception", JOptionPane.ERROR_MESSAGE);

		// Send it in a packet to the programmer TODO

		// Terminate Java, this never returns
		System.exit(0);
	}

	// Help

	/** Get the stack trace from an exception. */
	public static String describe(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter, true);
		e.printStackTrace(printWriter);
		printWriter.flush();
		stringWriter.flush();
		return stringWriter.toString();
	}
}
