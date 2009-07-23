package pipe.user.skin;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import base.user.skin.ButtonGuide;

public class Guide {
	
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
	public static final Dimension sizeExchangeDialog = new Dimension(800, 400);
	public static final Dimension sizeInfoFrame = new Dimension(600, 200);
	public static final Dimension sizeMuseumDialog = new Dimension(600, 300);
	
	// Skin

	public static final Point skinInk = new Point(16, 16);
	
	public static final Rectangle skinTool = new Rectangle(0, 0, sizeTool.width, sizeTool.height);
	public static final Rectangle skinPipe = new Rectangle(0, 0, sizePipe.width, sizePipe.height);
	
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
