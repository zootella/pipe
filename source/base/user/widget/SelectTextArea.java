package base.user.widget;

import java.awt.Color;

import javax.swing.JTextArea;

import pipe.user.Skin;






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
		setFont(Skin.font());
		/*
		
		setSelectedTextColor(Skin.highInk());
		setSelectionColor(Skin.high());
		*/
		
		
		
		new TextMenu(this);
	}
}
