package pipe.user.skin;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import base.user.skin.ButtonGuide;

public class Guide {
	
	// Define

	public static final int pipeWidth = 500;
	
	public static final int toolHeight = 180;
	public static final int pipeHeight = 200;

	public static final int buttonWidth = 80;
	public static final int buttonHeight = 25;
	
	// Size
	
	public static final Dimension toolSize = new Dimension(pipeWidth, toolHeight);
	public static final Dimension pipeSize = new Dimension(pipeWidth, pipeHeight);
	public static final Dimension skinSize = new Dimension(424, 667); //TODO set this for real
	
	// Skin

	public static final Point skinInk = new Point(16, 16);
	
	public static final Rectangle skinTool = new Rectangle(0, 0, toolSize.width, toolSize.height);
	public static final Rectangle skinPipe = new Rectangle(0, 0, pipeSize.width, pipeSize.height);
	
	public static final ButtonGuide skinToolClose = new ButtonGuide(0, 0, buttonWidth, buttonHeight);
	public static final ButtonGuide skinToolMake = new ButtonGuide(0, 0, buttonWidth, buttonHeight);
	public static final ButtonGuide skinToolMenu = new ButtonGuide(0, 0, buttonWidth, buttonHeight);
	
	public static final ButtonGuide skinPipeInfo = new ButtonGuide(0, 0, buttonWidth, buttonHeight);
	public static final ButtonGuide skinPipeKill = new ButtonGuide(0, 0, buttonWidth, buttonHeight);

	// Tool

	public static final Rectangle toolGrip = new Rectangle(10, 10, 445, buttonHeight);
	
	public static final Rectangle toolClose = new Rectangle(465, 10, buttonHeight, buttonHeight);
	public static final Rectangle toolMake = new Rectangle(10, 45, buttonWidth, buttonHeight);
	public static final Rectangle toolMenu = new Rectangle(100, 45, buttonHeight, buttonHeight);

	// Pipe

	public static final Rectangle pipeInfo = new Rectangle(320, 165, buttonWidth, buttonHeight);
	public static final Rectangle pipeKill = new Rectangle(410, 165, buttonWidth, buttonHeight);
	
	
	
	

	
	
	
	
	
	
	
	
	
	
}
