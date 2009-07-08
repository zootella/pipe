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

import pipe.main.Program;
import pipe.main.Snippet;

/** The toolbar at the top of the main window. */
public class ToolPanel {
	
	// Define
	
	public static final int height = 45;
	
	// Object
	
	public ToolPanel(Program program) {
		this.program = program;

		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(new Dimension(PipePanel.width, height));
		
		panel.setBackground(new Color(200, 200, 200));
		
		newAction = new NewAction();
		infoAction = new InfoAction();
		snippetAction = new SnippetAction();
		exitAction = new ExitAction();
		
		Button make = new Button(newAction, new Rectangle(10, 10, 80, 25));
		Button info = new Button(infoAction, new Rectangle(100, 10, 80, 25));
		Button snip = new Button(snippetAction, new Rectangle(190, 10, 80, 25));
		Button exit = new Button(exitAction, new Rectangle(280, 10, 80, 25));
		
		panel.add(make.button);
		panel.add(info.button);
		panel.add(snip.button);
		panel.add(exit.button);
	}

	public final JPanel panel;
	private final Program program;

	// Action

	private final NewAction newAction;
	private class NewAction extends AbstractAction {
		public NewAction() { super("New"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {

				program.core.pipes.make();
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}

	private final InfoAction infoAction;
	private class InfoAction extends AbstractAction {
		public InfoAction() { super("Info"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				program.user.info.frame.setVisible(true);
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}

	private final SnippetAction snippetAction;
	private class SnippetAction extends AbstractAction {
		public SnippetAction() { super("Snippet"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				Snippet.snippet(program);

			} catch (Exception e) { Mistake.grab(e); }
		}
	}

	private final ExitAction exitAction;
	private class ExitAction extends AbstractAction {
		public ExitAction() { super("Exit"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				program.close();

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
}
