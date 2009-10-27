package org.zootella.user.widget;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.Action;
import javax.swing.JButton;

import org.zootella.user.Face;

public class PlaceButton {
	
	public PlaceButton(Action action, Rectangle place, Color ink) {
		button = new JButton(action);
		button.setLayout(null);
		button.setBounds(place);
		button.setFont(Face.skin());
		button.setForeground(ink);
	}
	
	public final JButton button;
}


