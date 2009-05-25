package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pipe.main.Main;
import pipe.main.Program;
import base.data.Data;
import base.data.Outline;
import base.desktop.Clipboard;
import base.exception.ChopException;
import base.exception.MessageException;
import base.exception.Mistake;
import base.user.Cell;
import base.user.Dialog;
import base.user.Panel;
import base.user.TextMenu;

public class ExchangeDialog {
	
	private final Program program;
	
	public ExchangeDialog(Program program, Outline homeOutline) {
		this.program = program;

		home = new JTextArea();
		away = new JTextArea();
		new TextMenu(home);
		new TextMenu(away);
		home.setEditable(false);
		home.setLineWrap(true);
		away.setLineWrap(true);
		JScrollPane homeScroll = new JScrollPane(home, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane awayScroll = new JScrollPane(away, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		Panel row = Panel.row();
		row.add(Cell.wrap(new JButton(new OkAction())));
		row.add(Cell.wrap(new JButton(new CancelAction())));
		
		Panel panel = (new Panel()).border();
		panel.place(0, 0, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("Give this code to your friend:")));
		panel.place(1, 0, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("Enter your friend's code here:")));
		panel.place(0, 1, 1, 1, 1, 0, 0, 1, Cell.wrap(homeScroll).fill());
		panel.place(1, 1, 1, 1, 1, 0, 0, 0, Cell.wrap(awayScroll).fill());
		panel.place(0, 2, 1, 1, 1, 0, 0, 1, Cell.wrap(new JButton(new CopyAction())));
		panel.place(1, 2, 1, 1, 1, 0, 0, 0, Cell.wrap(new JButton(new PasteAction())));
		panel.place(0, 3, 2, 1, 1, 0, 0, 0, Cell.wrap(row.jpanel).lowerRight());
		
		home.setText(homeOutline.toString());

		dialog = new JDialog(program.user.window.frame, "Code Exchange", true); // true to make a modal dialog
		dialog.setContentPane(panel.jpanel);
		Dialog.show(dialog, 800, 400);
	}
	
	private final JDialog dialog;
	private final JTextArea home;
	private final JTextArea away;

	private Outline result;
	public Outline result() {
		return result;
	}
	
	// Action

	private class CopyAction extends AbstractAction {
		public CopyAction() { super("Copy"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Clipboard.copy(home.getText());
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	private class PasteAction extends AbstractAction {
		public PasteAction() { super("Paste"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				away.setText(Clipboard.paste());
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	private class OkAction extends AbstractAction {
		public OkAction() { super("OK"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				result = check(away.getText() + "\n");
				if (result == null)
					JOptionPane.showMessageDialog(program.user.window.frame, "Unable to parse code. Make sure you pasted it correctly, and try again.", Main.name, JOptionPane.PLAIN_MESSAGE);
				else
					dialog.dispose();

				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	private class CancelAction extends AbstractAction {
		public CancelAction() { super("Cancel"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	// Help
	
	private Outline check(String s) {
		try {
			return Outline.fromText(new Data(s));
		}
		catch (MessageException e) { return null; }
		catch (ChopException e) { return null; }
	}
	
	
	
	
	
	

}
