package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import base.exception.MessageException;
import base.exception.Mistake;
import base.file.Path;
import base.user.Cell;
import base.user.Dialog;
import base.user.Panel;
import base.user.TextMenu;
import pipe.core.Pipe;
import pipe.core.SendPipe;
import pipe.main.Main;
import pipe.main.Program;

public class ConfigureSendDialog {
	
	private final Program program;
	
	public ConfigureSendDialog(Program program) {
		this.program = program;
		
		browseAction = new BrowseAction();
		okAction = new OkAction();
		cancelAction = new CancelAction();
		
		folder = new JTextField();
		new TextMenu(folder);
		
		
		dialog = new JDialog(program.user.window.frame, "Send Pipe", true); // true to make a modal dialog
		
		Panel input = Panel.row();
		input.add(Cell.wrap(folder).fillWide());
		input.add(Cell.wrap(new JButton(browseAction)).upperRight());
		
		
		
		Panel buttons = Panel.row();
		buttons.add(Cell.wrap(new JButton(okAction)));
		buttons.add(Cell.wrap(new JButton(cancelAction)));
		
		
		
		Panel panel = Panel.column().border();
		panel.add(Cell.wrap(new JLabel("Choose the folder to send:")));
		panel.add(Cell.wrap(input.jpanel).fillWide().growTall());
		panel.add(Cell.wrap(buttons.jpanel).lowerRight());
		
		dialog.setContentPane(panel.jpanel); // Put everything we layed out in the dialog box

		Dialog.show(dialog, 600, 180);
		
	}

	public final JDialog dialog;
	private final JTextField folder;
	
	
	
	private final BrowseAction browseAction;
	private class BrowseAction extends AbstractAction {
		public BrowseAction() { super("Browse..."); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Dialog.chooseFolder(dialog, folder);
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}

	private final OkAction okAction;
	private class OkAction extends AbstractAction {
		public OkAction() { super("OK"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Path path = null;
				try {
					path = new Path(folder.getText());
				} catch (MessageException e) { path = null; }
				if (path != null && !path.existsFolder())
					path = null;
				
				if (path == null) {
					
					JOptionPane.showMessageDialog(program.user.window.frame, "That's not a path to a folder", Main.name, JOptionPane.PLAIN_MESSAGE);
					
				} else {
					
					dialog.dispose();
					Pipe pipe = new SendPipe(path);
					
					new ExchangeDialog(program, pipe);
					
					if (pipe.readyToStart()) {
						
						program.core.pipes.add(pipe);
						program.core.changed();
						
					} else {
						
						pipe.close();
					}
				}

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
