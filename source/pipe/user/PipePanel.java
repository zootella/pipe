package pipe.user;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import base.user.Cell;
import base.user.Panel;

import pipe.core.Pipe;
import pipe.main.Program;

/** A Status panel shows a pipe to the user. */
public class PipePanel {
	
	// Define
	
	public static final int width = 500;
	
	public static final int height = 200;
	
	public PipePanel(Program program, Pipe pipe) {
		this.program = program;
		this.pipe = pipe;
		
		
		
		panel = new Panel();
		panel.add(Cell.wrap(new JLabel("Hello Pipe")));
		
		panel.jpanel.setPreferredSize(new Dimension(width, height));
		
		
	}
	
	private final Program program;
	private final Pipe pipe;

	public final Panel panel;

}
