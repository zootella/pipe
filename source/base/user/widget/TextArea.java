package base.user.widget;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import pipe.user.Skin;

public class TextArea {
	
	public TextArea() {
		area = new JTextArea();
		area.setFont(Skin.font());
		
		
		
		new TextMenu(area);
	}
	
	public final JTextArea area;

}
