package org.zootella.user.skin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.zootella.exception.DiskException;
import org.zootella.file.Path;

/** A Skin object loads an image to show as the window background. */
public class Skin {
	
	/** Load the PNG file at path, and make sure it's size.width by size.height pixels big. */
	public Skin(Path path, Dimension size) {
		try {
			image = ImageIO.read(path.file);
			if (!size.equals(new Dimension(image.getWidth(), image.getHeight())))
				throw new DiskException("wrong size"); // Make sure the file we get is the right size
		} catch (IOException e) { throw new DiskException(e); }
	}
	
	/** The skin image, loaded into memory. */
	private final BufferedImage image;
	
	/** Eyedropper the Color at coordinates p.x, p.y in this Skin's image. */
	public Color color(Point p) {
		return new Color(image.getRGB(p.x, p.y));
	}
	
	/** Crop the given Rectangle from this Skin's image and return it. */
	public BufferedImage image(Rectangle r) {
		return image.getSubimage(r.x, r.y, r.width, r.height); // Doesn't copy image memory
	}
}
