package pipe.core;

import javax.swing.JPanel;

import base.state.Close;

import pipe.user.panel.PipePanel;

/** Our end of a pipe that transfers files with another computer. */
public class Pipe extends Close {
	
	public Pipe() {
		
	}

	private PipePanel status;
	
	public JPanel panel() {
		return new JPanel();
	}

	@Override public void close() {
		if (already()) return;
		
	}

}
