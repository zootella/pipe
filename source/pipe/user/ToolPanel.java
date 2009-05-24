package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import pipe.main.Program;
import pipe.main.Snippet;
import base.exception.Mistake;
import base.user.Cell;
import base.user.Panel;

/** The toolbar at the top of the main window. */
public class ToolPanel {
	
	// Define
	
	public static final int height = 80;
	
	// Object
	
	public ToolPanel(Program program) {
		this.program = program;

		newAction = new NewAction();
		infoAction = new InfoAction();
		snippetAction = new SnippetAction();
		exitAction = new ExitAction();
		
		panel = Panel.row();
		panel.add(Cell.wrap(new JButton(newAction)));
		panel.add(Cell.wrap(new JButton(infoAction)));
		panel.add(Cell.wrap(new JButton(snippetAction)));
		panel.add(Cell.wrap(new JButton(exitAction)));
		
		
		
	}
	
	
	private final Panel panel;

	private final Program program;
	
	public JPanel panel() {
		return panel.jpanel;
	}
	
	
	// Action

	private final NewAction newAction;
	private class NewAction extends AbstractAction {
		public NewAction() { super("New"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {

				program.user.newPipe();
				
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
