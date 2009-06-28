package pipe.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import pipe.main.Program;
import pipe.main.Snippet;
import base.exception.Mistake;

/** The toolbar at the top of the main window. */
public class ToolPanel {
	
	// Define
	
	public static final int height = 45;
	
	// Object
	
	public ToolPanel(Program program) {
		this.program = program;

		newAction = new NewAction();
		infoAction = new InfoAction();
		snippetAction = new SnippetAction();
		exitAction = new ExitAction();
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(new Dimension(PipePanel.width, height));
		
		panel.setBackground(new Color(200, 200, 200));
		
		JButton newButton = new JButton(new NewAction());
		JButton infoButton = new JButton(new InfoAction());
		JButton snippetButton = new JButton(new SnippetAction());
		JButton exitButton = new JButton(new ExitAction());
		
		newButton.setLayout(null);
		infoButton.setLayout(null);
		snippetButton.setLayout(null);
		exitButton.setLayout(null);
		
		newButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		infoButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		snippetButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 11));

		newButton.setBounds(10, 10, 80, 25);
		infoButton.setBounds(100, 10, 80, 25);
		snippetButton.setBounds(190, 10, 80, 25);
		exitButton.setBounds(280, 10, 80, 25);

		panel.add(newButton);
		panel.add(infoButton);
		panel.add(snippetButton);
		panel.add(exitButton);
	}

	public final JPanel panel;
	private final Program program;

	// Action

	private final NewAction newAction;
	private class NewAction extends AbstractAction {
		public NewAction() { super("New"); } // Text for the button
		public void actionPerformed(ActionEvent a) {
			try {

				program.core.newPipe();
				
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
