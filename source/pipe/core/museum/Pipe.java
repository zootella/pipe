package pipe.core.museum;

import pipe.user.PipeInfoFrame;
import pipe.user.PipePanel;

/** An object that implements Pipe represents our end of a pipe that transfers files with another computer. */
public interface Pipe {

	// User
	public PipePanel userPanel();
	public PipeInfoFrame userInfo();
	
	// Folder
	public String folderTitle();
	public String folderInstruction();
	public String folder(String s);
	public boolean hasFolder();
	
	// Code
	public String homeCode();
	public void awayCode(String s);
	public boolean hasAwayCode();

	// Go
	public void go();
}
