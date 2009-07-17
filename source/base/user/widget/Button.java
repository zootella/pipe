package base.user.widget;

import java.awt.Rectangle;

import javax.swing.Action;
import javax.swing.JButton;

import base.user.Graphics;


// A button that always stays in the same place to be skinned
// have it go dim if the action is disabled
// viewstate is normal, float, press, set, ghost
public class Button {
	
	public Button(Action action) {
		button = new JButton(action);
		button.setFont(Graphics.font());
		button.setForeground(Graphics.foreground());
	}
	
	public Button(Action action, Rectangle rectangle) {
		button = new JButton(action);
		button.setFont(Graphics.font());
		button.setForeground(Graphics.foreground());
		button.setLayout(null);
		button.setBounds(rectangle);
	}
	
	public final JButton button;

}
