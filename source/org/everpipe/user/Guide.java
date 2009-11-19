package org.everpipe.user;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

public class Guide {

	// File

	/** Filename "skin.png" in the present working directory. */
	public static final String skin = "skin.png";
	
	/** Package path to "icon.gif" in the jar or source code. */
	public static final String icon = "org/everpipe/resource/icon.gif";
	
	public static final Dimension skinSize = new Dimension(424, 667);
	
	// Define

	public static final int pipeWidth = 500;
	
	public static final int toolHeight = 80;
	public static final int pipeHeight = 200;

	public static final int buttonWidth = 80;
	public static final int buttonHeight = 25;
	
	// Size
	
	public static final Dimension sizeTool = new Dimension(pipeWidth, toolHeight);
	public static final Dimension sizePipe = new Dimension(pipeWidth, pipeHeight);
	public static final Dimension sizeSkin = new Dimension(424, 667); //TODO set this for real
	
	public static final Dimension sizeFolderDialog = new Dimension(600, 180);
	public static final Dimension sizeExchangeDialog = new Dimension(900, 200);
	public static final Dimension sizeInfoFrame = new Dimension(800, 400);
	public static final Dimension sizeMuseumDialog = new Dimension(600, 300);
	
	// Skin

	public static final Point skinInk = new Point(16, 16);
	
	public static final Rectangle skinTool = new Rectangle(0, 0, sizeTool.width, sizeTool.height);
	public static final Rectangle skinPipe = new Rectangle(0, 0, sizePipe.width, sizePipe.height);
	
	public static final Rectangle skinToolClose = new Rectangle(0, 0, buttonWidth, buttonHeight);
	public static final Rectangle skinToolMake = new Rectangle(0, 0, buttonWidth, buttonHeight);
	public static final Rectangle skinToolMenu = new Rectangle(0, 0, buttonWidth, buttonHeight);
	
	public static final Rectangle skinPipeInfo = new Rectangle(0, 0, buttonWidth, buttonHeight);
	public static final Rectangle skinPipeKill = new Rectangle(0, 0, buttonWidth, buttonHeight);

	// Tool

	public static final Rectangle toolGrip = new Rectangle(10, 10, 445, buttonHeight);
	
	public static final Rectangle toolClose = new Rectangle(465, 10, buttonHeight, buttonHeight);
	public static final Rectangle toolMake = new Rectangle(10, 45, buttonWidth, buttonHeight);
	public static final Rectangle toolMenu = new Rectangle(100, 45, buttonHeight, buttonHeight);

	// Pipe

	public static final Rectangle pipeInfo = new Rectangle(320, 165, buttonWidth, buttonHeight);
	public static final Rectangle pipeKill = new Rectangle(410, 165, buttonWidth, buttonHeight);
	
	// Museum
	
	public static final Rectangle museumSend = new Rectangle(10, 10, 200, 200);
	public static final Rectangle museumReceive = new Rectangle(220, 10, 200, 200);

	// Icon
	
	/** Where the 16 pixel square icon is pictured in skin.png. */
	//TODO use this, or remove it
//	public static final Rectangle icon = new Rectangle(50, 50, 16, 16);
	
	// Color
	
	/** The coordinates to the pixel in skin.png you can eyedropper to get the font ink color. */
	public static final Point ink = new Point(0, 0);

	// Font
	
	public static final Font font = new Font("Tahoma", Font.PLAIN, 11);
	public static final Font bigFont = new Font("Arial", Font.PLAIN, 24); // Loads Helvetica on Mac, which is good
}
