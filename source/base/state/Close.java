package base.state;

import java.util.HashSet;
import java.util.Set;

import base.exception.ProgramException;
import base.process.Mistake;
import base.time.Duration;
import base.time.Now;

/** Have your object extend Close so the program will notice if you forget to later call its close() method. */
public abstract class Close {

	// Core

	/**
	 * Count that the program has made another new object that needs to be closed.
	 * This automatically runs before execution enters the constructor of an object that extends Close.
	 */
	public Close() {
		whenMade = new Now();
		programOpen++; // Count the program has one more object open, this new one that extends Close
//		add(this);
	}
	
	/** true once this object that extends Close has been closed, and promises to not change again. */
	public boolean closed() { return objectClosed; }
	private boolean objectClosed; // Private so objects that extend Close can't get to this

	/** Close your objects inside, put away resources, and never change again. */
	public abstract void close(); // Your object that extends Close must have this method

	/**
	 * Mark this object that extends Close as closed, and only do this once.
	 * Start your close() method with the code if (already()) return;.
	 * The first time already() runs, it marks this object as closed and returns false.
	 * Try calling it again, and it will just return true.
	 */
	public boolean already() {
		if (objectClosed) return true; // We're already closed, return true to return from the close() method
		objectClosed = true;           // Mark this object that extends Close as now permanently closed
		whenClosed = new Duration(whenMade);
		programOpen--;                 // Count the program has one fewer object it needs to close
//		remove(this);
		return false;                  // Return false to run the contents of the close() method this first and only time
	}

	// Program
	
	/** The total number of objects the program has made that still need to be closed. */
	private static volatile int programOpen; // volatile because a Task thread may make an object that extends Close

	/** Before the program closes, make sure every object with a close() method had it run. */
	public static int checkAll() {
//		print();
		return programOpen;
	}
	
	// Check

	/** Make sure this object isn't closed before doing something that would change it. */
	public void open() { if (objectClosed) throw new IllegalStateException(); }

	/** Make sure this object is closed, throw e if given, and make sure o exists. */
	public void check(ProgramException e, Object o) {
		if (!objectClosed) throw new IllegalStateException();
		if (e != null) throw e;
		if (o == null) throw new NullPointerException();
	}

	// Help

	/** Close c ignoring null and exceptions. */
	public static void close(Close c) {
		if (c == null) return;
		try { c.close(); } catch (Exception e) { Mistake.log(e); } // Keep going to close the next object
	}

	/** true if c is null. */
	public static boolean no(Close c) { return c == null; }
	/** true if c exists. */
	public static boolean is(Close c) { return c != null; }
	/** true if c exists and is closed. */
	public static boolean done(Close c) { return c != null && c.closed(); }
	
	// See
	/*
	private static final Set<Close> list = new HashSet<Close>();
	private static synchronized void add(Close c) { list.add(c); }
	private static synchronized void remove(Close c) { list.remove(c); }
	public static synchronized void print() {
		if (list.size() != 0) {
			System.out.print(list.size() + " objects open:\n");
			for (Close c : list)
				System.out.print(c.toString() + "\n");
		}
	}
	*/
	
	// Time
	public Now whenMade() { return whenMade; }
	public Duration whenClosed() { open(); return whenClosed; }
	private final Now whenMade;
	private Duration whenClosed;
}
