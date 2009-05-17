package main;

public class Program {
	
	// Defined
	
	/** The name of this program. */
	public static final String name = "Hello";
	/** Text that describes the version of this program. */
	public static final String versionText = "1.0";
	/** Text for the window title. */
	public static final String title = "Hello, World!";

	// Program

	/** Make the Program object, which represents the whole program and puts the window on the screen. */
	public Program() {
		core = new Core(); // Make Core first
		window = new Window(core); // Give window a link to core
	}
	
	/** The core of the program that does everything. */
	private final Core core;
	/** The window on the screen that shows the user what the core is doing. */
	private final Window window;
}
