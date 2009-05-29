package pipe.core;

import javax.swing.JPanel;

import base.data.Outline;
import base.file.Path;

/** An object that extends Pipe represents our end of a pipe that transfers files with another computer. */
public interface Pipe {
	
	// Close

	/** Put resources away and never change again. */
	public void close();
	
	// Configure

	/** Tell this Pipe what folder to use, returns Path if good or null not good. */
	public Path folder(String s);
	/** The folder this Pipe will use. */
	public Path folder();
	
	
	public Outline home();
	
	public Outline away(String s);
	public Outline away();

	/** true when this Pipe is configured and ready to start. */
	public boolean ready();

	// User

	/** Make a panel to show this pipe to the user. */
	public JPanel panel();
}
