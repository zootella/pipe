package pipe.user;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import pipe.main.Main;
import pipe.main.Program;
import base.exception.DiskException;
import base.exception.MessageException;
import base.exception.Mistake;
import base.file.Path;
import base.user.Cell;
import base.user.Dialog;
import base.user.Panel;
import base.user.TextMenu;

public class FolderDialog {

	// Link
	
	private final Program program;
	
	// Object
	
	public FolderDialog(Program program, String title, String instruction) {
		this.program = program;
		
		browseAction = new BrowseAction();
		okAction = new OkAction();
		cancelAction = new CancelAction();
		
		folder = new JTextField();
		new TextMenu(folder);

		dialog = new JDialog(program.user.window.frame, title, true); // true to make a modal dialog
		
		Panel input = Panel.row();
		input.add(Cell.wrap(folder).fillWide());
		input.add(Cell.wrap(new JButton(browseAction)).upperRight());

		Panel buttons = Panel.row();
		buttons.add(Cell.wrap(new JButton(okAction)));
		buttons.add(Cell.wrap(new JButton(cancelAction)));

		Panel panel = Panel.column().border();
		panel.add(Cell.wrap(new JLabel(instruction)));
		panel.add(Cell.wrap(input.jpanel).fillWide().growTall());
		panel.add(Cell.wrap(buttons.jpanel).lowerRight());
		
		dialog.setContentPane(panel.jpanel); // Put everything we layed out in the dialog box

		Dialog.show(dialog, 600, 180);
	}

	private final JDialog dialog;
	private final JTextField folder;

	public Path result() { return result; }
	private Path result;
	
	// Action

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
				
				result = check(folder.getText());
				if (result == null)
					JOptionPane.showMessageDialog(program.user.window.frame, "That's not a path to a folder", Main.name, JOptionPane.PLAIN_MESSAGE);
				else
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
	
	// Help

	/** Make sure s is a Path to a folder on the disk, or return null. */
	private static Path check(String s) {
		try {
			
			// Parse s into a Path
			Path path = new Path(s);
			if (path == null)
				return null; // Given text is not a valid Path
			
			// See if a folder is there
			if (path.existsFolder())
				return path;
			
			// Try to make the folder
			path.folder();
			
			// See if the folder is there now
			if (path.existsFolder())
				return path;
			return null;
		}
		catch (MessageException e) { return null; }
		catch (DiskException e) { return null; }
	}
}
