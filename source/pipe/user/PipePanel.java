package pipe.user;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;

import base.process.Mistake;
import base.user.skin.ButtonGuide;
import base.user.skin.Skin;
import base.user.skin.SkinButton;

import pipe.core.museum.Pipe;
import pipe.main.Program;
import pipe.user.skin.Guide;

/** A Status panel shows a pipe to the user. */
public class PipePanel {
	
	// Define
	
	public PipePanel(Program program, Pipe pipe) {
		this.program = program;
		this.pipe = pipe;
		
		
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(Guide.pipeSize);
		panel.setBackground(new Color(250, 250, 250));

		
		// buttons, info and terminate
		
		infoAction = new InfoAction();
		killAction = new KillAction();
		
		SkinButton info = new SkinButton(new InfoAction(), skin, Guide.skinPipeInfo, Guide.pipeInfo);
		SkinButton kill = new SkinButton(new KillAction(), skin, Guide.skinPipeKill, Guide.pipeKill);
		
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

			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final KillAction killAction;
	private class KillAction extends AbstractAction {
		public KillAction() { super("Kill"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				program.core.pipes.kill(pipe);

			} catch (Exception e) { Mistake.stop(e); }
		}
	}
	
	
	
}
