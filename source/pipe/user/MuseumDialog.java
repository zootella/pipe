package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;

import pipe.core.museum.Pipe;
import pipe.core.museum.ReceivePipe;
import pipe.core.museum.SendPipe;
import pipe.main.Program;
import base.process.Mistake;
import base.user.Cell;
import base.user.Dialog;
import base.user.Panel;
import base.user.Screen;

public class MuseumDialog {
	
	// Library
	
	public static Pipe show(Program program) {
		return (new MuseumDialog(program)).pipe;
	}
	
	// Object
	
	private MuseumDialog(Program program) {
		this.program = program;
		
		Panel panel = Panel.row();
		panel.add(Cell.wrap(new JButton(new SendAction())));
		panel.add(Cell.wrap(new JButton(new ReceiveAction())));

		dialog = new JDialog(program.user.main.frame, "What kind of pipe do you want? (Science Museum Music)", true); // true to make a modal dialog
		dialog.setResizable(false);
		dialog.setContentPane(panel.panel); // Put everything we layed out in the dialog box
		dialog.setBounds(Screen.positionSize(600, 300)); // Set the dialog size and pick a random location
		dialog.setVisible(true); // Show the dialog box on the screen
	}
	
	private final Program program;
	private final JDialog dialog;
	private Pipe pipe;
	
	// Action
	
	private class SendAction extends AbstractAction {
		public SendAction() { super("Send Pipe"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				pipe = new SendPipe(program);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}
	
	private class ReceiveAction extends AbstractAction {
		public ReceiveAction() { super("Receive Pipe"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				pipe = new ReceivePipe();
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}
}
