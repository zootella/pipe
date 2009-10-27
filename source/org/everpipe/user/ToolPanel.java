package org.everpipe.user;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.everpipe.main.Main;
import org.everpipe.main.Program;
import org.everpipe.main.Snippet;
import org.zootella.process.Mistake;
import org.zootella.user.skin.PlainButton;
import org.zootella.user.widget.Grip;

/** The toolbar at the top of the main window. */
public class ToolPanel {
	
	// Object
	
	public ToolPanel(User user, MainFrame main) {
		this.program = main.program;

		closeAction = new CloseAction();
		makeAction = new MakeAction();
		menuAction = new MenuAction();
		snippetAction = new SnippetAction();
		preferencesAction = new PreferencesAction();
		informationAction = new InformationAction();
		aboutAction = new AboutAction();
		
		menu = new JPopupMenu();
		if (!Main.release)
			menu.add(new JMenuItem(snippetAction));
		menu.add(new JMenuItem(preferencesAction));
		menu.add(new JMenuItem(informationAction));
		menu.add(new JMenuItem(aboutAction));
		menu.addSeparator();
		menu.add(new JMenuItem(user.exitAction));
		
		PlainButton closeButton = new PlainButton(closeAction, user.skin, Guide.skinToolClose, Guide.toolClose, Guide.ink);
		PlainButton makeButton = new PlainButton(makeAction, user.skin, Guide.skinToolMake, Guide.toolMake, Guide.ink);
		PlainButton menuButton = new PlainButton(menuAction, user.skin, Guide.skinToolMenu, Guide.toolMenu, Guide.ink);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(Guide.sizeTool);
		panel.setBackground(new Color(0xebebeb));
		
		panel.add(closeButton.button);
		panel.add(makeButton.button);
		panel.add(menuButton.button);
		new Grip(main.frame, panel);
		
		/*
		SkinPanel skin = new SkinPanel();
		skin.setBounds(10, 100, 80, 25);
		skin.setBackground(Color.red);
		panel.add(skin);
		*/
		
		
		
		
	}
	
	private final Program program;
	private final JPopupMenu menu;

	public final JPanel panel;

	// Action

	private final CloseAction closeAction;
	private class CloseAction extends AbstractAction {
		public CloseAction() { super("x"); }
		public void actionPerformed(ActionEvent a) {
			try {

				program.user.show(false);
				
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final MakeAction makeAction;
	private class MakeAction extends AbstractAction {
		public MakeAction() { super("New"); }
		public void actionPerformed(ActionEvent a) {
			try {

				program.core.pipes.make();
				
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final MenuAction menuAction;
	private class MenuAction extends AbstractAction {
		public MenuAction() { super("v"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				menu.show(panel, Guide.toolMenu.x, Guide.toolMenu.y + Guide.toolMenu.height);
				
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final SnippetAction snippetAction;
	private class SnippetAction extends AbstractAction {
		public SnippetAction() { super("Snippet"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Snippet.snippet(program);

			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final PreferencesAction preferencesAction;
	private class PreferencesAction extends AbstractAction {
		public PreferencesAction() { super("Preferences"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				System.out.println("preferences action");

			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final InformationAction informationAction;
	private class InformationAction extends AbstractAction {
		public InformationAction() { super("Information"); }
		public void actionPerformed(ActionEvent a) {
			try {

				program.user.info.frame.setVisible(true);
				
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final AboutAction aboutAction;
	private class AboutAction extends AbstractAction {
		public AboutAction() { super("About"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				System.out.println("about action");
				
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}
}
