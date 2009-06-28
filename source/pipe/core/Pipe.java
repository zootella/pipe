package pipe.core;

import pipe.user.PipePanel;
import base.data.Outline;
import base.file.Path;

/** An object that extends Pipe represents our end of a pipe that transfers files with another computer. */
public interface Pipe {
	
	// Close

	/** Put resources away and never change again. */
	public void close();
	
	// Configure

	public Path folder(Path p);
	public Path folder();
	
	public Outline home();

	public Outline away(Outline o);
	public Outline away();

	/** true when this Pipe is configured and ready to start. */
	public boolean ready();

	// User

	/** The panel that shows this Pipe to the user. */
	public PipePanel panel();
	
	
	
	public String title();
	public String instruction();
	
	public void go();
}
