package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

import pipe.main.Program;
import base.exception.Mistake;
import base.user.Cell;
import base.user.Dialog;
import base.user.Panel;

public class MuseumDialog {
	
	private final Program program;
	
	public MuseumDialog(Program program) {
		this.program = program;
		
		
		sendAction = new SendAction();
		receiveAction = new ReceiveAction();
		
		
		dialog = new JDialog(program.user.window.frame, "What kind of pipe do you want? (Science Museum Music)", true); // true to make a modal dialog
		
		dialog.setResizable(false);
		
		Panel panel = Panel.row();
		panel.add(Cell.wrap(new JButton(sendAction)));
		panel.add(Cell.wrap(new JButton(receiveAction)));
		

		
		dialog.setContentPane(panel.jpanel); // Put everything we layed out in the dialog box

		Dialog.show(dialog, 600, 300);
		
	}
	
	private final JDialog dialog;
	
	
	
	private final SendAction sendAction;
	private class SendAction extends AbstractAction {
		public SendAction() { super("Send Pipe"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				new ConfigureSendDialog(program);
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	private final ReceiveAction receiveAction;
	private class ReceiveAction extends AbstractAction {
		public ReceiveAction() { super("Receive Pipe"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	
	
	
	
	

}
