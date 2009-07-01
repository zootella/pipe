package base.user;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

public class Screen {
	
	// Library

	/** Given a width and height like 3/4 and 1/2 of the screen size, choose a random position on the screen. */
	public static Rectangle positionPortion(float w, float h) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return positionPixel((int)(w * screen.width), (int)(h * screen.height));
	}
	
	/** Given a width and height like 500 and 300 pixels, choose a random position on the screen. */
	public static Rectangle positionPixel(int w, int h) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return new Rectangle(position(screen.width, w), position(screen.height, h), w, h);
	}
	
	// Inside

	/** Given a screen and window width or height, choose a random position for the window. */
	private static int position(int screen, int window) {
		if (window > screen)                               // Window larger than screen
			return 0;                                      // Position on the left or top edge
		else if ((2 * window) > screen)                    // Window larger than screen half
			return random((screen - window));              // Position randomly on screen
		else                                               // Window screen half or smaller
			return (screen / 2) - window + random(window); // Position randomly in center
	}
	
	/** Generate a random number 0 through size, clustered around size / 2. */
	private static int random(int size) {

		// Make the random number generator if we don't already have it
		if (random == null) random = new Random();
		
		// Calculate the midpoint into size our results will cluster around
		int half = size / 2;
		
		// Calculate the upper bound
		int bound = half; // Start at half
		while (random.nextBoolean()) bound /= 2; // Make it smaller
		if (bound < 1) return half; // To small, reached center

		// Pick a random number within that bound
		int i = random.nextInt(bound);
		if (i > half) i = half; // Too big somehow

		// Go that distance to the left or right of the midpoint
		if (random.nextBoolean()) return half + i;
		else                      return half - i;
	}

	/** A random number generator random() makes the first time it runs. */
	private static Random random;
}
