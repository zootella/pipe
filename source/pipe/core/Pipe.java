package pipe.core;

import javax.swing.JPanel;

import pipe.user.Status;

/** Our end of a pipe that transfers files with another computer. */
public class Pipe {
	
	public Pipe() {
		
	}

	private Status status;
	
	public JPanel panel() {
		return new JPanel();
	}

}
