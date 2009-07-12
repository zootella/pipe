package base.process;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class Mistake {
	
	/** Notice an exception that we can ignore, and let the program keep going. */
	public static void ignore(Exception e) {
		
		// Write it to standard output
		System.out.print("Mistake.ignore() caught an exception --v--\n");
		System.out.print(describe(e));
		System.out.print("Mistake.ignore() caught an exception --^--\n");
	}
	
	/** Show and report an exception code didn't catch, and terminate the Java process. */
	public static void grab(Exception e) {
		
		// Write it to standard output
		System.out.print("Mistake.grab() caught an exception --v--\n");
		System.out.print(describe(e));
		System.out.print("Mistake.grab() caught an exception --^--\n");

		// Show it to the user
		try {
			JOptionPane.showMessageDialog(null, describe(e), "Mistake.grab() caught an exception", JOptionPane.ERROR_MESSAGE);
		} catch (Exception i) { Mistake.ignore(i); }

		// Send it in a packet to the programmer TODO

		// Terminate the process without closing the program properly
		System.exit(0); // This never returns
	}

	// Help

	/** Get the stack trace from an exception. */
	private static String describe(Exception e) {
		StringWriter s = new StringWriter();
		PrintWriter p = new PrintWriter(s, true);
		e.printStackTrace(p);
		p.flush();
		s.flush();
		return s.toString();
	}
}
