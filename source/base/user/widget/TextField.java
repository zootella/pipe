package base.user.widget;

import javax.swing.JTextField;

public class TextField {
	
	public TextField() {
		field = new JTextField();
		new ActionMenu(field);
	}
	
	public final JTextField field;
}
