package base.user.widget;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import base.data.Text;
import base.desktop.Clipboard;
import base.process.Mistake;

public class TextMenu {
	
	/** Give component a right-click menu with Cut, Copy, Paste, Delete and Select All. */
	public TextMenu(JTextComponent component) {
		
		this.component = component;
		component.addMouseListener(new MyMouseListener()); // Find out when the user right-clicks component

		cutAction = new CutAction();
		copyAction = new CopyAction();
		pasteAction = new PasteAction();
		deleteAction = new DeleteAction();
		selectAction = new SelectAction();
		
		menu = new JPopupMenu();
		menu.add(cutAction);
		menu.add(copyAction);
		menu.add(pasteAction);
		menu.add(deleteAction);
		menu.addSeparator();
		menu.add(selectAction);
	}

	private final JTextComponent component;
	private final JPopupMenu menu;

	private class MyMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent m) { show(m); }
		public void mouseReleased(MouseEvent m) { show(m); }
	}

	private void show(MouseEvent m) {
		try {
			if (m.isPopupTrigger()) { // Only do something if this is the correct event
				
				// Disable everything, we'll enable those that can work next
				cutAction.setEnabled(false);
				copyAction.setEnabled(false);
				pasteAction.setEnabled(false);
				deleteAction.setEnabled(false);
				selectAction.setEnabled(false);
				
				// Find out if component is editable or read-only, and if it has some selected text
				boolean editable = component.isEditable();
				boolean selection = Text.hasText(component.getSelectedText());
				
				if (editable && selection) { // Editable with selection, enable Cut and Delete
					cutAction.setEnabled(true);
					deleteAction.setEnabled(true);
				}
				if (selection) // Selection, enable Copy
					copyAction.setEnabled(true);
				if (editable && Clipboard.hasText()) // Editable and clipboard has text, enable Paste
					pasteAction.setEnabled(true);
				if (Text.hasText(component.getText())) // Has text, enable Select All
					selectAction.setEnabled(true);
				
				// Show the menu to the user
				menu.show(m.getComponent(), m.getX(), m.getY());
			}
		} catch (Exception e) { Mistake.stop(e); }
	}

	private class Select {
		public Select() {
			
			// Get the selected text, and the text before and after it
			all = component.getText();
			i = component.getSelectionStart(); // The index where the selection starts
			size = component.getSelectionEnd() - i; // The number of selected characters
			
			before = Text.start(all, i);
			selected = Text.clip(all, i, size);
			after = Text.after(all, i + size);
		}
		
		public final String all;
		public final int i;
		public final int size;
		public final String before;
		public final String selected;
		public final String after;
	}
	
	private final CutAction cutAction;
	private class CutAction extends AbstractAction {
		public CutAction() { super("Cut"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Select s = new Select();
				Clipboard.copy(s.selected);
				component.setText(s.before + s.after); // Remove the selected text
				component.setCaretPosition(s.i);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final CopyAction copyAction;
	private class CopyAction extends AbstractAction {
		public CopyAction() { super("Copy"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Select s = new Select();
				Clipboard.copy(s.selected);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final PasteAction pasteAction;
	private class PasteAction extends AbstractAction {
		public PasteAction() { super("Paste"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Select s = new Select();
				String clipboard = Clipboard.paste(); // Insert text from the clipboard
				component.setText(s.before + clipboard + s.after);
				component.setCaretPosition(s.i + clipboard.length());
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final DeleteAction deleteAction;
	private class DeleteAction extends AbstractAction {
		public DeleteAction() { super("Delete"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Select s = new Select();
				component.setText(s.before + s.after); // Remove the selected text
				component.setCaretPosition(s.i);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final SelectAction selectAction;
	private class SelectAction extends AbstractAction {
		public SelectAction() { super("Select All"); }
		public void actionPerformed(ActionEvent a) {
			try {

				component.selectAll();
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}
}
