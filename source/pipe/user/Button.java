package pipe.user;

import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.Action;
import javax.swing.JButton;

// A button that always stays in the same place to be skinned
// have it go dim if the action is disabled
// viewstate is normal, float, press, set, ghost
public class Button {
	
	public Button(Action action, Rectangle rectangle) {
		button = new JButton(action);
		button.setLayout(null);
		button.setFont(new Font("Tahoma", Font.PLAIN, 11));
		button.setBounds(rectangle);
	}
	
	public final JButton button;

}
