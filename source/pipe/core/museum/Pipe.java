package pipe.core.museum;

import pipe.user.PipeInfoFrame;
import pipe.user.PipePanel;
import base.file.Path;

/** An object that implements Pipe represents our end of a pipe that transfers files with another computer. */
public interface Pipe {

	// User
	public PipePanel panel();
	public PipeInfoFrame info();
	
	// Look
	public String title();
	public String instruction();
	public String home();
	
	// Enter
	public String folder(Path p);
	public String away(String s);

	// Ready
	public boolean hasFolder();
	public boolean hasAway();

	// Command
	public void go();
}
