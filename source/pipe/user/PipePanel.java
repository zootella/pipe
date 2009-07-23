package pipe.user;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import pipe.core.museum.Pipe;
import pipe.main.Program;
import pipe.user.skin.Guide;
import base.process.Mistake;
import base.user.skin.SkinButton;

/** A Status panel shows a pipe to the user. */
public class PipePanel {
	
	// Define
	
	public PipePanel(Program program, Pipe pipe) {
		this.program = program;
		this.pipe = pipe;
		
		
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(Guide.sizePipe);
		panel.setBackground(new Color(0xfafafa));

		
		// buttons, info and terminate
		
		infoAction = new InfoAction();
		killAction = new KillAction();
		
		SkinButton info = new SkinButton(infoAction, program.user.skin, Guide.skinPipeInfo, Guide.pipeInfo);
		SkinButton kill = new SkinButton(killAction, program.user.skin, Guide.skinPipeKill, Guide.pipeKill);
		
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
