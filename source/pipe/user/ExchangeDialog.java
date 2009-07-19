package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import pipe.main.Main;
import pipe.main.Program;
import base.data.Data;
import base.data.Outline;
import base.desktop.Clipboard;
import base.exception.DataException;
import base.process.Mistake;
import base.user.Screen;
import base.user.panel.Cell;
import base.user.panel.Panel;
import base.user.widget.TextArea;

public class ExchangeDialog {
	
	// Library

	public static Outline show(Program program, Outline home) {
		return (new ExchangeDialog(program, home)).result;
	}
	
	// Object

	private ExchangeDialog(Program program, Outline homeOutline) {
		this.program = program;

		home = new TextArea();
		away = new TextArea();
		home.area.setEditable(false);
		home.area.setLineWrap(true);
		away.area.setLineWrap(true);
		JScrollPane homeScroll = new JScrollPane(home.area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane awayScroll = new JScrollPane(away.area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		Panel row = Panel.row();
		row.add(Cell.wrap(new JButton(new OkAction())));//TODO still using JButton, i see?
		row.add(Cell.wrap(new JButton(new CancelAction())));
		
		Panel panel = (new Panel()).border();
		panel.place(0, 0, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("Give this code to your friend:")));
		panel.place(1, 0, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("Enter your friend's code here:")));
		panel.place(0, 1, 1, 1, 1, 0, 0, 1, Cell.wrap(homeScroll).fill());
		panel.place(1, 1, 1, 1, 1, 0, 0, 0, Cell.wrap(awayScroll).fill());
		panel.place(0, 2, 1, 1, 1, 0, 0, 1, Cell.wrap(new JButton(new CopyAction())));
		panel.place(1, 2, 1, 1, 1, 0, 0, 0, Cell.wrap(new JButton(new PasteAction())));
		panel.place(0, 3, 2, 1, 1, 0, 0, 0, Cell.wrap(row.panel).lowerRight());
		
		home.area.setText(homeOutline.toString());

		dialog = new JDialog(program.user.main.frame, "Code Exchange", true); // true to make a modal dialog
		dialog.setContentPane(panel.panel);
		dialog.setBounds(Screen.positionSize(800, 400)); // Set the dialog size and pick a random location
		dialog.setVisible(true); // Show the dialog box on the screen
	}
	
	private final Program program;
	private final JDialog dialog;
	private final TextArea home;
	private final TextArea away;
	private Outline result;
	
	// Action

	private class CopyAction extends AbstractAction {
		public CopyAction() { super("Copy"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Clipboard.copy(home.area.getText());
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}
	
	private class PasteAction extends AbstractAction {
		public PasteAction() { super("Paste"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				away.area.setText(Clipboard.paste());
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}
	
	private class OkAction extends AbstractAction {
		public OkAction() { super("OK"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				result = check(away.area.getText() + "\n");
				if (result == null)
					JOptionPane.showMessageDialog(program.user.main.frame, "Unable to parse code. Make sure you pasted it correctly, and try again.", Main.name, JOptionPane.PLAIN_MESSAGE);
				else
					dialog.dispose();

				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}
	
	private class CancelAction extends AbstractAction {
		public CancelAction() { super("Cancel"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}
	
	// Help
	
	private Outline check(String s) {
		try {
			return Outline.fromText(new Data(s));
		}
		catch (DataException e) { return null; }
	}
}
