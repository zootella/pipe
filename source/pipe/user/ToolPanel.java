package pipe.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;


import base.process.Mistake;
import base.state.Close;
import base.user.Grip;

import pipe.main.Program;
import pipe.main.Snippet;

/** The toolbar at the top of the main window. */
public class ToolPanel {
	
	// Define
	
	public static final int height = 80;
	
	// Object
	
	public ToolPanel(MainFrame main) {
		this.program = main.program;

		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(new Dimension(PipePanel.width, height));
		
		panel.setBackground(new Color(0xdedede));
		
		closeAction = new CloseAction();
		makeAction = new MakeAction();
		infoAction = new InfoAction();
		snippetAction = new SnippetAction();
		exitAction = new ExitAction();

		Grip grip = new Grip(main.frame, new Rectangle(10, 10, 445, 25));
		Button close = new Button(closeAction, new Rectangle(465, 10, 25, 25));
		Button make = new Button(makeAction, new Rectangle(10, 45, 80, 25));
		Button info = new Button(infoAction, new Rectangle(100, 45, 80, 25));
		Button snippet = new Button(snippetAction, new Rectangle(190, 45, 80, 25));
		Button exit = new Button(exitAction, new Rectangle(280, 45, 80, 25));
		
		panel.add(grip.label);
		panel.add(close.button);
		panel.add(make.button);
		panel.add(info.button);
		panel.add(snippet.button);
		panel.add(exit.button);
	}

	public final JPanel panel;
	private final Program program;

	// Action

	private final CloseAction closeAction;
	private class CloseAction extends AbstractAction {
		public CloseAction() { super("X"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {

				program.user.show(false);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final MakeAction makeAction;
	private class MakeAction extends AbstractAction {
		public MakeAction() { super("New"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {

				program.core.pipes.make();
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final InfoAction infoAction;
	private class InfoAction extends AbstractAction {
		public InfoAction() { super("Info"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				program.user.info.frame.setVisible(true);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final SnippetAction snippetAction;
	private class SnippetAction extends AbstractAction {
		public SnippetAction() { super("Snippet"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				Snippet.snippet(program);

			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final ExitAction exitAction;
	private class ExitAction extends AbstractAction {
		public ExitAction() { super("Exit"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				Close.close(program);

			} catch (Exception e) { Mistake.stop(e); }
		}
	}
}
