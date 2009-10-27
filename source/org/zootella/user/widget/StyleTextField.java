package org.zootella.user.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class StyleTextField {

	public StyleTextField(Color ink, Color background, Color selectInk, Color selectBackground, Font font, Rectangle place) {
		field = new JTextField();
		field.setLayout(null);
		field.setBounds(place);
		
		field.setForeground(ink);
		field.setBackground(background);
		field.setSelectedTextColor(selectInk);
		field.setSelectionColor(selectBackground);
		
		field.setFont(font);
		field.setBorder(BorderFactory.createEmptyBorder());
		

		new TextMenu(field);
	}
	
	public final JTextField field;
}
