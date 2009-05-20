package base.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class Error {
	
	/** Show and report an exception code didn't catch, and terminate the Java process. */
	public static void error(Exception e) {
		
		// Get the stack trace from the exception
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter, true);
		e.printStackTrace(printWriter);
		printWriter.flush();
		stringWriter.flush();
		String s = stringWriter.toString();

		// Write it to standard output
		System.out.println("Error.error() caught an exception --v--");
		System.out.print(s);
		System.out.println("Error.error() caught an exception --^--");

		// Show it to the user
		JOptionPane.showMessageDialog(null, s, "Error.error() caught an exception", JOptionPane.ERROR_MESSAGE);

		// Send it in a packet to the programmer TODO

		// Terminate Java, this never returns
		System.exit(0);
	}
}
