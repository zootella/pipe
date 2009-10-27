package org.zootella.user.widget;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JLabel;

public class WhiteLabel {
	
	public WhiteLabel(Rectangle place) {
		label = new JLabel();
		label.setLayout(null);
		label.setBounds(place);
		label.setBackground(Color.white);
		
		label.setOpaque(true);
		
	}

	public final JLabel label;
}
