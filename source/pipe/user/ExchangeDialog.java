package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JTextArea;

import pipe.core.Pipe;
import pipe.main.Program;
import base.exception.Mistake;

public class ExchangeDialog {
	
	private final Program program;
	
	public ExchangeDialog(Program program, Pipe pipe) {
		this.program = program;
		
		okAction = new OkAction();
		cancelAction = new CancelAction();
		
		dialog = new JDialog(program.user.window.frame, "Code Exchange", true); // true to make a modal dialog
		
		
	}
	
	public final JDialog dialog;
	private final JTextArea home;
	private final JTextArea away;

	
	
	private final OkAction okAction;
	private class OkAction extends AbstractAction {
		public OkAction() { super("OK"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	private final CancelAction cancelAction;
	private class CancelAction extends AbstractAction {
		public CancelAction() { super("Cancel"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	
	
	
	
	

}
