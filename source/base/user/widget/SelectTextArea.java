package base.user.widget;

import javax.swing.JTextArea;

import base.user.Graphics;



/** A wrapping, read-only text area that lets the user select and copy. */
public class SelectTextArea extends JTextArea {
	
	public SelectTextArea() {
		this("");
	}

	public SelectTextArea(String s) {
		super(s);
		setLineWrap(true);
		setOpaque(false);
		setBorder(null);
		setEditable(false);
		setFont(Graphics.font());
		new TextMenu(this);
	}
}
