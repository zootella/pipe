package base.user.skin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import base.process.Mistake;
import base.user.Fonts;

public class SkinPanel extends JPanel {
	
	public SkinPanel() {
		label = new JLabel();
		label.setFont(Fonts.program());
		
		
		try {                
			image = ImageIO.read(new File("face.png"));
		} catch (IOException e) { Mistake.stop(e); }
		
		
		
		
	}
	
	public final JLabel label;
	
	
	private BufferedImage image;
	
	
	@Override public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
		
	}
	
	
	
	
}
