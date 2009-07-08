package pipe.user;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import base.process.Mistake;

import pipe.core.museum.Pipe;
import pipe.main.Program;

/** A Status panel shows a pipe to the user. */
public class PipePanel {
	
	// Define
	
	public static final int width = 500;
	public static final int height = 200;
	
	public PipePanel(Program program, Pipe pipe) {
		this.program = program;
		this.pipe = pipe;
		
		
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(width, height);
		panel.setBackground(new Color(250, 250, 250));

		
		// buttons, info and terminate
		
		infoAction = new InfoAction();
		killAction = new KillAction();
		
		Button info = new Button(new InfoAction(), new Rectangle(320, 165, 80, 25));
		Button kill = new Button(new KillAction(), new Rectangle(410, 165, 80, 25));
		
		panel.add(info.button);
		panel.add(kill.button);
		
		
	}
	
	private final Program program;
	private final Pipe pipe;
	public final JPanel panel;

	// Action

	private final InfoAction infoAction;
	private class InfoAction extends AbstractAction {
		public InfoAction() { super("Info"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				pipe.info().frame.setVisible(true);

			} catch (Exception e) { Mistake.grab(e); }
		}
	}

	private final KillAction killAction;
	private class KillAction extends AbstractAction {
		public KillAction() { super("Kill"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				program.core.pipes.kill(pipe);

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	
	
}
