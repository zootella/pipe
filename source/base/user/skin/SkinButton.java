package base.user.skin;

import java.awt.Rectangle;

import javax.swing.Action;
import javax.swing.JButton;

import base.user.Fonts;

// A button that always stays in the same place to be skinned
public class SkinButton {

	public SkinButton(Action action, Rectangle rectangle) {
		button = new JButton(action);
		button.setLayout(null);
		button.setBounds(rectangle);
		button.setFont(Fonts.program());
		
		//TODO pngs for ghost, normal, float, and press
	}
	
	public final JButton button;
}
