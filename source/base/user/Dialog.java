package base.user;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Dialog {
	
	// Define

	/** 8 pixels, the space between buttons and the width of the margin at the edge. */
	public static final int space = 8;
	/** 80 pixels, the width and height of a big button. */
	public static final int button = 80;

	// -------- Make --------

	/** Make a new non-modal JDialog object with the given title. */
	public static JDialog make(String title) {
		JFrame frame = null;
		return new JDialog(frame, title, false); // false to make a non-modal dialog
	}
	
	/** Make a new modal JDialog object with the given title. */
	public static JDialog modal(String title) {
		JFrame frame = null;
		return new JDialog(frame, title, true); // true to make a modal dialog
	}

	// -------- Standard dialog boxes --------
	
	/** Show the user the Open box to choose a file on the disk, and put the path in dialog's field. */
	public static void chooseFile(JDialog dialog, JTextField field) { choose(dialog, field, JFileChooser.FILES_ONLY); }
	/** Show the user the Open box to choose a folder on the disk, and put the path in dialog's field. */
	public static void chooseFolder(JDialog dialog, JTextField field) { choose(dialog, field, JFileChooser.DIRECTORIES_ONLY); }
	/** Show the user the Open box to choose a file or folder on the disk, and put the path in dialog's field. */
	public static void chooseFileOrFolder(JDialog dialog, JTextField field) { choose(dialog, field, JFileChooser.FILES_AND_DIRECTORIES); }

	/**
	 * Show the user the Open box to choose a file or folder on the disk, and put the path in dialog's field.
	 * 
	 * @param dialog The JDialog to show the Open box over
	 * @param field  The JTextField to fill with the path text the user chooses
	 * @param mode   Limit the choice to only files or only folders
	 */
	public static void choose(JDialog dialog, JTextField field, int mode) {
			
		// Make a Swing JFileChooser object
		JFileChooser chooser = new JFileChooser();
		
		chooser.setFileSelectionMode(mode); // Have it limit the choice to just files or just folders
		
		// Have our JFileChooser show the user the standard dialog box
		int result = chooser.showOpenDialog(dialog); // Control sticks here while the user is deciding
		if (result != JFileChooser.APPROVE_OPTION) return; // The user pressed Cancel
		
		// Get the Java File the user chose, turn it into a String, and put it in the text field
		field.setText(chooser.getSelectedFile().getAbsolutePath());
	}
}
