package org.zootella.user.widget;

import javax.swing.JTextArea;

import org.zootella.user.Face;

/** A single line, read-only text area that lets the user select and copy. */
public class TextLine {
	
	public TextLine() {
		this("");
	}

	public TextLine(String s) {
		area = new JTextArea(s);
		area.setLineWrap(false);
		area.setOpaque(false);
		area.setBorder(null);
		area.setEditable(false);
		area.setFont(Face.font());
		new TextMenu(area);
	}
	
	public final JTextArea area;
}
