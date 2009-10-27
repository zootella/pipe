package org.zootella.user.skin;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Action;
import javax.swing.JButton;

import org.zootella.user.Face;


// A button that always stays in the same place to be skinned
public class PlainButton {

	public PlainButton(Action action, Skin skin, Rectangle guide, Rectangle place, Point ink) {
		button = new JButton(action);
		button.setLayout(null);
		button.setBounds(place);
		button.setFont(Face.skin());
		
		button.setForeground(skin.color(ink));
		
		//TODO pngs for ghost, normal, float, and press
	}
	
	public final JButton button;
}
