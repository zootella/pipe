package pipe.user.skin;

import java.awt.Dimension;
import java.awt.Point;

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
	
	//
	
	public static final Point skinInk = new Point(16, 16);
	
	public static final ButtonGuide skinButtonskinMakeButton = new ButtonGuide(16, 48, buttonWidth, buttonHeight);
	
	public static final ButtonGuide skinButtonToolInfo = new ButtonGuide(16, 48, buttonWidth, buttonHeight);
	public static final ButtonGuide skinButtonPipeKill = new ButtonGuide(16, 48, buttonWidth, buttonHeight);
	
	//skin, the whole image
	//skinTool, the whole background
	//skinPipe
	//skinInk
	//skinToolClose
	//skinToolMake
	//skinToolMenu
	
	public static final Dimension skin = new Dimension(x, y);

	// Tool

	//tool
	//toolGrip
	//toolClose
	//toolMake
	//toolMenu

	// Pipe
	
	//pipe
	//pipeInfo
	//pipeKill
	
	//skinPipeInfo
	//skinPipeKill
	
	
	

	
	
	
	
	
	
	
	
	
	
}
