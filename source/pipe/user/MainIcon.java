package pipe.user;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import pipe.main.Main;
import pipe.main.Program;
import base.exception.Mistake;
import base.exception.PlatformException;
import base.state.Close;

public class MainIcon extends Close {
	
	private final Program program;
	
	public MainIcon(final Program program) {
		this.program = program;
		
		listen = new MyActionListener();
		mouse = new MyMouseListener();

        if (!SystemTray.isSupported()) throw new PlatformException("tray not supported");
        tray = SystemTray.getSystemTray();
        if (tray == null) throw new PlatformException("tray null");
        
        
        Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("pipe/icon.gif"));

        MenuItem restoreItem = new MenuItem("Restore");
        MenuItem exitItem = new MenuItem("Exit");

        restoreItem.addActionListener(listen);
        exitItem.addActionListener(listen);
        
        PopupMenu menu = new PopupMenu();
        menu.add(restoreItem);
        menu.addSeparator();
        menu.add(exitItem);
        

        trayIcon = new TrayIcon(image, Main.name, menu);
        trayIcon.addActionListener(listen);
        trayIcon.addMouseListener(mouse);
	}

	
	private final MouseListener mouse;
    private class MyMouseListener extends MouseAdapter {
    	public void mouseClicked(MouseEvent m) {
    		try {
    			
    			if (m.getButton() == MouseEvent.BUTTON1)
    				program.user.show(true);
    			
    		} catch (Exception e) { Mistake.grab(e); }
    	}
    }
	

	private final ActionListener listen;
	private class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			try {
				if (a == null || a.getActionCommand() == null) return;
				
				if (a.getActionCommand().equals("Restore"))
					program.user.show(true);
				else if (a.getActionCommand().equals("Exit"))
					program.close();

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	
	private final SystemTray tray;
	private final TrayIcon trayIcon;
	
	private boolean show;
	
	public void show(boolean b) {
		if (show == b) return;
		
		if (b) {
	       	try {
				tray.add(trayIcon);
			} catch (AWTException e) { throw new PlatformException(e, "cant add tray icon"); }
		} else {
			tray.remove(trayIcon);
		}
		
		show = b;
	}

	@Override public void close() {
		if (already()) return;
		
		show(false);
	}
	
	

}
