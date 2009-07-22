package pipe.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import pipe.main.Main;
import pipe.main.Program;
import pipe.main.Snippet;
import base.process.Mistake;
import base.state.Close;
import base.user.skin.SkinButton;
import base.user.skin.SkinPanel;
import base.user.widget.Grip;

/** The toolbar at the top of the main window. */
public class ToolPanel {
	
	// Define
	
	public static final int height = 180;
	
	// Object
	
	public ToolPanel(MainFrame main) {
		this.program = main.program;

		closeAction = new CloseAction();
		makeAction = new MakeAction();
		menuAction = new MenuAction();
		snippetAction = new SnippetAction();
		preferencesAction = new PreferencesAction();
		informationAction = new InformationAction();
		aboutAction = new AboutAction();
		exitAction = new ExitAction();
		
		menu = new JPopupMenu();
		if (!Main.release)
			menu.add(new JMenuItem(snippetAction));
		menu.add(new JMenuItem(preferencesAction));
		menu.add(new JMenuItem(informationAction));
		menu.add(new JMenuItem(aboutAction));
		menu.addSeparator();
		menu.add(new JMenuItem(exitAction));

		Grip grip = new Grip(main.frame, new Rectangle(10, 10, 445, 25));
		SkinButton closeButton = new SkinButton(closeAction, new Rectangle(465, 10, 25, 25));
		SkinButton makeButton = new SkinButton(makeAction, new Rectangle(10, 45, 80, 25));
		SkinButton menuButton = new SkinButton(menuAction, new Rectangle(100, 45, 25, 25));
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(new Dimension(PipePanel.width, height));
		panel.setBackground(new Color(0xebebeb));
		
		panel.add(grip.label);
		panel.add(closeButton.button);
		panel.add(makeButton.button);
		panel.add(menuButton.button);
		
		
		SkinPanel skin = new SkinPanel();
		skin.setBounds(10, 100, 80, 25);
		skin.setBackground(Color.red);
		panel.add(skin);
		
		
		
		
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
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final MakeAction makeAction;
	private class MakeAction extends AbstractAction {
		public MakeAction() { super("New"); }
		public void actionPerformed(ActionEvent a) {
			try {

				program.core.pipes.make();
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final MenuAction menuAction;
	private class MenuAction extends AbstractAction {
		public MenuAction() { super("v"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				menu.show(panel, 100, 70);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final SnippetAction snippetAction;
	private class SnippetAction extends AbstractAction {
		public SnippetAction() { super("Snippet"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Snippet.snippet(program);

			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final PreferencesAction preferencesAction;
	private class PreferencesAction extends AbstractAction {
		public PreferencesAction() { super("Preferences"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				System.out.println("preferences action");

			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final InformationAction informationAction;
	private class InformationAction extends AbstractAction {
		public InformationAction() { super("Information"); }
		public void actionPerformed(ActionEvent a) {
			try {

				program.user.info.frame.setVisible(true);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final AboutAction aboutAction;
	private class AboutAction extends AbstractAction {
		public AboutAction() { super("About"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				System.out.println("about action");
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	private final ExitAction exitAction;
	private class ExitAction extends AbstractAction {
		public ExitAction() { super("Exit"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				Close.close(program);

			} catch (Exception e) { Mistake.stop(e); }
		}
	}
}
