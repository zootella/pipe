package org.everpipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.everpipe.core.museum.Pipe;
import org.everpipe.main.Main;
import org.everpipe.main.Program;
import org.zootella.process.Mistake;
import org.zootella.user.Dialog;
import org.zootella.user.Screen;
import org.zootella.user.panel.Cell;
import org.zootella.user.panel.Panel;
import org.zootella.user.widget.TextField;

public class FolderDialog {

	// Object
	
	public FolderDialog(Program program, Pipe pipe) {
		this.program = program;
		this.pipe = pipe;
		
		browseAction = new BrowseAction();
		okAction = new OkAction();
		cancelAction = new CancelAction();
		
		folder = new TextField();

		dialog = new JDialog(program.user.main.frame, pipe.folderTitle(), true); // true to make a modal dialog
		
		Panel input = Panel.row();
		input.add(Cell.wrap(folder.field).fillWide());
		input.add(Cell.wrap(new JButton(browseAction)).upperRight());

		Panel buttons = Panel.row();
		buttons.add(Cell.wrap(new JButton(okAction)));
		buttons.add(Cell.wrap(new JButton(cancelAction)));

		Panel panel = Panel.column().border();
		panel.add(Cell.wrap(new JLabel(pipe.folderInstruction())));
		panel.add(Cell.wrap(input.panel).fillWide().growTall());
		panel.add(Cell.wrap(buttons.panel).lowerRight());
		
		dialog.setContentPane(panel.panel); // Put everything we layed out in the dialog box

		dialog.setBounds(Screen.positionSize(Guide.sizeFolderDialog)); // Set the dialog size and pick a random location
		dialog.setVisible(true); // Show the dialog box on the screen
	}

	private final Program program;
	private final Pipe pipe;
	private final JDialog dialog;
	private final TextField folder;
	
	// Action

	private final BrowseAction browseAction;
	private class BrowseAction extends AbstractAction {
		public BrowseAction() { super("Browse..."); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Dialog.chooseFolder(dialog, folder.field);
				
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final OkAction okAction;
	private class OkAction extends AbstractAction {
		public OkAction() { super("OK"); }
		public void actionPerformed(ActionEvent a) {
			try {

				String s = pipe.folder(folder.field.getText());
				if (s != null)
					JOptionPane.showMessageDialog(program.user.main.frame, s, Main.name, JOptionPane.WARNING_MESSAGE);
				else
					dialog.dispose();

			} catch (Throwable t) { Mistake.stop(t); }
		}
	}
	
	private final CancelAction cancelAction;
	private class CancelAction extends AbstractAction {
		public CancelAction() { super("Cancel"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				dialog.dispose();
				
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}
}
