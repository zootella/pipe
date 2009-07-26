package base.user.skin;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

public class SkinLabel {
	
	public SkinLabel(Skin skin, Rectangle guide, Rectangle place) {
		this.skin = skin;
		this.guide = guide;
		label = new MyLabel();
		label.setLayout(null);
		label.setBounds(place);
		
	}
	
	private final Skin skin;
	private final Rectangle guide;
	public final MyLabel label;
	
	private class MyLabel extends JLabel {
		@Override public void paintComponent(Graphics g) {
			g.drawImage(skin.image(guide), 0, 0, null);
		}
	}


}
