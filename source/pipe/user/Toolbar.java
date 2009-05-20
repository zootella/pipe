package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import pipe.main.Program;
import pipe.main.Snippet;
import base.exception.Error;
import base.user.Cell;
import base.user.Panel;

/** The toolbar at the top of the main window. */
public class Toolbar {
	
	// Define
	
	public static final int height = 80;
	
	// Object
	
	public Toolbar(Program program) {
		this.program = program;

		newAction = new NewAction();
		infoAction = new InfoAction();
		snippetAction = new SnippetAction();
		
		panel = Panel.row();
		panel.add(Cell.wrap(new JButton(newAction)));
		panel.add(Cell.wrap(new JButton(infoAction)));
		panel.add(Cell.wrap(new JButton(snippetAction)));
		
		
		
	}
	
	
	private final Panel panel;

	private final Program program;
	
	// Actions

	private final NewAction newAction;
	private class NewAction extends AbstractAction {
		public NewAction() { super("New"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				
			} catch (Exception e) { Error.error(e); }
		}
	}

	private final InfoAction infoAction;
	private class InfoAction extends AbstractAction {
		public InfoAction() { super("Info"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				
			} catch (Exception e) { Error.error(e); }
		}
	}

	private final SnippetAction snippetAction;
	private class SnippetAction extends AbstractAction {
		public SnippetAction() { super("Snippet"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {
				
				Snippet.snippet();

			} catch (Exception e) { Error.error(e); }
		}
	}
	
	
	// Panel
	
	public JPanel panel() {
		return panel.jpanel;
	}

}
