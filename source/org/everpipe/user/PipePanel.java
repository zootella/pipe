package org.everpipe.user;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import org.everpipe.core.museum.Pipe;
import org.everpipe.main.Program;
import org.zootella.process.Mistake;
import org.zootella.user.skin.PlainButton;

/** A Status panel shows a pipe to the user. */
public class PipePanel {
	
	// Define
	
	public PipePanel(Program program, Pipe pipe) {
		this.program = program;
		this.pipe = pipe;
		
		
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(Guide.sizePipe);
		panel.setBackground(new Color(0xdedede));
		
		// buttons, info and terminate
		
		infoAction = new InfoAction();
		killAction = new KillAction();
		
		PlainButton info = new PlainButton(infoAction, program.user.skin, Guide.skinPipeInfo, Guide.pipeInfo, Guide.ink);
		PlainButton kill = new PlainButton(killAction, program.user.skin, Guide.skinPipeKill, Guide.pipeKill, Guide.ink);
		
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
				
				pipe.userInfo().frame.setVisible(true);

			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final KillAction killAction;
	private class KillAction extends AbstractAction {
		public KillAction() { super("Kill"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				program.core.pipes.kill(pipe);

			} catch (Throwable t) { Mistake.stop(t); }
		}
	}
	
	
	
}
