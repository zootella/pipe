package pipe.core.museum;

import pipe.user.PipeInfoFrame;
import pipe.user.PipePanel;
import base.file.Path;

/** An object that implements Pipe represents our end of a pipe that transfers files with another computer. */
public interface Pipe {

	// On the screen
	public PipePanel panel();
	public PipeInfoFrame info();
	
	// Pipe produces
	public String title();
	public String instruction();
	public String home();
	
	// User enters
	public boolean folder(Path p);
	public boolean away(String s);

	// Command
	public boolean ready();
	public void go();
}
