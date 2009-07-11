package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import pipe.main.Main;
import pipe.main.Program;
import base.exception.DiskException;
import base.exception.DataException;
import base.file.Path;
import base.process.Mistake;
import base.user.Cell;
import base.user.Dialog;
import base.user.Panel;
import base.user.Screen;
import base.user.TextMenu;

public class FolderDialog {
	
	// Library

	public static Path show(Program program, String title, String instruction) {
		return (new FolderDialog(program, title, instruction)).path;
	}

	// Object
	
	private FolderDialog(Program program, String title, String instruction) {
		this.program = program;
		
		browseAction = new BrowseAction();
		okAction = new OkAction();
		cancelAction = new CancelAction();
		
		folder = new JTextField();
		new TextMenu(folder);

		dialog = new JDialog(program.user.main.frame, title, true); // true to make a modal dialog
		
		Panel input = Panel.row();
		input.add(Cell.wrap(folder).fillWide());
		input.add(Cell.wrap(new JButton(browseAction)).upperRight());

		Panel buttons = Panel.row();
		buttons.add(Cell.wrap(new JButton(okAction)));
		buttons.add(Cell.wrap(new JButton(cancelAction)));

		Panel panel = Panel.column().border();
		panel.add(Cell.wrap(new JLabel(instruction)));
		panel.add(Cell.wrap(input.panel).fillWide().growTall());
		panel.add(Cell.wrap(buttons.panel).lowerRight());
		
		dialog.setContentPane(panel.panel); // Put everything we layed out in the dialog box

		dialog.setBounds(Screen.positionSize(600, 180)); // Set the dialog size and pick a random location
		dialog.setVisible(true); // Show the dialog box on the screen
	}

	private final Program program;
	private final JDialog dialog;
	private final JTextField folder;
	private Path path;
	
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
				
				path = check(folder.getText());
				if (path == null)
					JOptionPane.showMessageDialog(program.user.main.frame, "That's not a path to a folder", Main.name, JOptionPane.PLAIN_MESSAGE);
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
		catch (DataException e) { return null; }
		catch (DiskException e) { return null; }
	}
}
