package main;


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import status.user.StatusTab;


import base.user.Dialog;
import bridge.user.BridgeTab;

public class Window {

	// -------- The program's Window, and its parts --------

	/**
	 * Make the Window object, which is the program's window on the screen.
	 * Calling this constructor shows the window on the screen.
	 * 
	 * @param program A link up to the Program object this new Window object is a part of
	 */
	public Window(Core core) {
		
		// Save the link back up to the Program object
		this.core = core;

		// Make the objects that represent the tabs in the window
		bridge = new BridgeTab();
		status = new StatusTab();

		// Make a row of tabs, and add the tabs to it
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Bridge", bridge.panel.jpanel);
		tabs.addTab("Status", status.panel.jpanel);

		// Choose how big the window will be, and where it will appear on the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();          // Screen resolution in pixels
		Dimension size = new Dimension(screen.width * 3 / 4, screen.height / 2); // Window size
		Point location = Dialog.position(size);                                  // Random location

		// Make the program's window, configure it, and show it
		frame = new JFrame();                                    // Make the Swing JFrame object which is the program's main window on the screen
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Make closing the window close the program
		frame.addWindowListener(new MyWindowListener());         // Have Java tell us when the user closes the window
		frame.setTitle(Program.title);                           // Set the text in the window's title bar
		frame.setSize(size);                                     // Set the window's size and location from what we calculated above
		frame.setLocation(location);
		frame.setContentPane(tabs);                              // Put the tabs in the window
		frame.setVisible(true);                                  // Show the window on the screen
	}

	private final Core core;

	/** The Swing JFrame object which is the main window on the screen. */
	public JFrame frame;
	/** The Status tab in the window. */
	public StatusTab status;
	public BridgeTab bridge;

	// -------- Close --------

	// When the user clicks the main window's corner X, Java closes the program and calls this windowClosing() method
	private class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {

			// Close the core
			core.close();
		}
	}

	
	//TODO Move this to Dialog
	// -------- Factory settings --------
}
