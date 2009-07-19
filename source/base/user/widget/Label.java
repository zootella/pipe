package base.user.widget;

import javax.swing.JLabel;

import pipe.user.Skin;




public class Label {
	
	public Label(String s) {
		label = new JLabel(s);
		/*
		label.setFont(Skin.font());
//		label.setForeground(Skin.ink());
 * 
 */
	}
	
	
	public JLabel label;

}
