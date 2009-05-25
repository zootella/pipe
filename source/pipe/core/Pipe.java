package pipe.core;

import javax.swing.JPanel;

import base.file.Path;
import base.state.Close;

import pipe.user.PipePanel;

/** An object that extends Pipe represents our end of a pipe that transfers files with another computer. */
public interface Pipe {
	
	public Path folder(String s);
	

	public JPanel panel();

	
	
	
	public boolean readyToStart();

}
