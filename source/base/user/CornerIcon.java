package base.user;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;

import base.exception.PlatformException;
import base.process.Mistake;
import base.state.Close;

public class CornerIcon extends Close {
	
	public CornerIcon(String name, Image image, Action restoreAction, Action exitAction) {
		this.restoreAction = restoreAction;
		this.exitAction = exitAction;
		
		listen = new MyActionListener();
		mouse = new MyMouseListener();

		if (!SystemTray.isSupported()) throw new PlatformException("tray not supported");
		tray = SystemTray.getSystemTray();
		if (tray == null) throw new PlatformException("tray null");

		MenuItem restoreItem = new MenuItem((String)restoreAction.getValue(Action.NAME)); // This is an AWT MenuItem, not a Swing JMenuItem
		MenuItem exitItem = new MenuItem((String)exitAction.getValue(Action.NAME));

		restoreItem.setFont(Face.font());
		exitItem.setFont(Face.font());

		restoreItem.addActionListener(listen);
		exitItem.addActionListener(listen);

		PopupMenu menu = new PopupMenu();
		menu.add(restoreItem);
		menu.addSeparator();
		menu.add(exitItem);

		trayIcon = new TrayIcon(image, name, menu);
		trayIcon.addActionListener(listen);
		trayIcon.addMouseListener(mouse);
	}
	
	private final Action restoreAction;
	private final Action exitAction;
	
	private final SystemTray tray;
	private final TrayIcon trayIcon;

	@Override public void close() {
		if (already()) return;
		show(false);
	}

	public void show(boolean b) {
		if (show == b) return;
		show = b;
		
		if (show) {
			try {
				tray.add(trayIcon);
			} catch (AWTException e) { throw new PlatformException("cant add tray icon", e); }
		} else {
			tray.remove(trayIcon);
		}
	}
	private boolean show;

	private final MouseListener mouse;
	private class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent m) {
			try {
				
				if (m.getButton() == MouseEvent.BUTTON1)
					restoreAction.actionPerformed(null);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final ActionListener listen;
	private class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			try {
				if (a == null || a.getActionCommand() == null) return;
				
				if (a.getActionCommand().equals((String)restoreAction.getValue(Action.NAME)))
					restoreAction.actionPerformed(null);
				else if (a.getActionCommand().equals((String)exitAction.getValue(Action.NAME)))
					exitAction.actionPerformed(null);

			} catch (Exception e) { Mistake.stop(e); }
		}
	}
}
