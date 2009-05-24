package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;

import pipe.main.Program;
import base.exception.Mistake;
import base.user.Cell;
import base.user.Dialog;
import base.user.Panel;

public class MuseumDialog {
	
	// Object
	
	public MuseumDialog(Program program) {
		
		Panel panel = Panel.row();
		panel.add(Cell.wrap(new JButton(new SendAction())));
		panel.add(Cell.wrap(new JButton(new ReceiveAction())));

		dialog = new JDialog(program.user.window.frame, "What kind of pipe do you want? (Science Museum Music)", true); // true to make a modal dialog
		dialog.setResizable(false);
		dialog.setContentPane(panel.jpanel); // Put everything we layed out in the dialog box
		Dialog.show(dialog, 600, 300);
	}
	
	private final JDialog dialog;
	
	public String result() { return result; }
	private String result;
	
	// Action
	
	private class SendAction extends AbstractAction {
		public SendAction() { super("Send Pipe"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				result = "send";
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	private class ReceiveAction extends AbstractAction {
		public ReceiveAction() { super("Receive Pipe"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				result = "receive";
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
}
