package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import pipe.core.museum.Pipe;
import pipe.core.museum.ReceivePipe;
import pipe.core.museum.SendPipe;
import pipe.main.Program;
import base.process.Mistake;
import base.user.Screen;
import base.user.panel.Cell;
import base.user.panel.Panel;
import base.user.skin.PlainButton;

public class MuseumDialog {
	
	// Object
	
	public MuseumDialog(Program program) {
		this.program = program;
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(Guide.sizeMuseumDialog);
		
		panel.add(new PlainButton(new SendAction(), program.user.skin, null, Guide.museumSend).button);
		panel.add(new PlainButton(new ReceiveAction(), program.user.skin, null, Guide.museumReceive).button);

		dialog = new JDialog(program.user.main.frame, "What kind of pipe do you want? (Science Museum Music)", true); // true to make a modal dialog
		dialog.setResizable(false);
		dialog.setLayout(null);
		dialog.setContentPane(panel); // Put everything we layed out in the dialog box
		dialog.setBounds(Screen.positionSize(Guide.sizeMuseumDialog)); // Set the dialog size and pick a random location
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
