package pipe.core;

import javax.swing.JPanel;

import base.file.Path;

/** An object that extends Pipe represents our end of a pipe that transfers files with another computer. */
public interface Pipe {
	
	public Path folder(String s);
	public Path folder();
	

	public JPanel panel();

	
	
	
	public boolean readyToStart();

}
