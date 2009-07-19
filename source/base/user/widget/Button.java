package base.user.widget;

import java.awt.Rectangle;

import javax.swing.Action;
import javax.swing.JButton;

import pipe.user.Skin;

// A button that always stays in the same place to be skinned
// have it go dim if the action is disabled
// viewstate is normal, float, press, set, ghost
public class Button {
	
	public Button(Action action) {
		button = new JButton(action);
		/*
		button.setFont(Skin.font());
		button.setBackground(Skin.page());
		*/
	}
	
	public Button(Action action, Rectangle rectangle) {
		button = new JButton(action);
		/*
		button.setFont(Skin.font());
		button.setBackground(Skin.page());
		 */
		button.setLayout(null);
		button.setBounds(rectangle);
	}
	
	public final JButton button;

}
