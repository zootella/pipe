package base.user.skin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pipe.user.skin.Guide;

import base.exception.DiskException;

public class Skin {
	
	public Skin(String name) {
		try {
			image = ImageIO.read(new File("name"));
			if (!Guide.skinSize.equals(new Dimension(image.getHeight(), image.getWidth())))
				throw new DiskException("wrong size");
		} catch (IOException e) { throw new DiskException(e); }
	}
	
	private final BufferedImage image;
	
	public Color color(Point p) {
		return new Color(image.getRGB(p.x, p.y));
	}
	
	public BufferedImage image(Rectangle r) {
		return image.getSubimage(r.x, r.y, r.width, r.height);
	}
}
