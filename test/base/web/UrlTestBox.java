package base.web;

import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import base.exception.DataException;
import base.user.Dialog;
import base.user.Screen;
import base.user.panel.Cell;
import base.user.panel.Panel;
import base.user.widget.TextValue;
import base.user.widget.TextMenu;
import base.web.Url;

public class UrlTestBox {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new UrlTestBox();
            }
        });
    }

	private JDialog dialog;
	private JTextField in;
	private TextValue address, get, protocol, user, pass, site, port, path;
	private TextValue uriToString;
	private TextValue uriScheme, uriHost, uriPort;
	private TextValue uriSchemeSpecificPart, uriAuthority, uriUserInfo, uriPath, uriQuery, uriFragment;
	private TextValue rawSchemeSpecificPart, rawAuthority, rawUserInfo, rawPath, rawQuery, rawFragment;

	public UrlTestBox() {

		// Make controls
		in = new JTextField("https://user:pass@www.site.com:99/folder/folder/file.ext?parameters#bookmark");
		in.getDocument().addDocumentListener(new MyDocumentListener());
		new TextMenu(in);
		
		address  = new TextValue();
		get      = new TextValue();
		protocol = new TextValue();
		user     = new TextValue();
		pass     = new TextValue();
		site     = new TextValue();
		port     = new TextValue();
		path     = new TextValue();
		
		uriToString = new TextValue();
		
		uriScheme = new TextValue();
		uriHost   = new TextValue();
		uriPort   = new TextValue();
		
		uriSchemeSpecificPart = new TextValue();
		uriAuthority          = new TextValue();
		uriUserInfo           = new TextValue();
		uriPath               = new TextValue();
		uriQuery              = new TextValue();
		uriFragment           = new TextValue();
		
		rawSchemeSpecificPart = new TextValue();
		rawAuthority          = new TextValue();
		rawUserInfo           = new TextValue();
		rawPath               = new TextValue();
		rawQuery              = new TextValue();
		rawFragment           = new TextValue();

		// Lay out controls
		Panel panel = new Panel();
		panel.border();
		panel.place(1,  0, 1, 1, 0, 1, 0, 0, Cell.wrap(in).fillWide());
		
		panel.place(0,  1, 1, 1, 2, 0, 0, 0, Cell.wrap(new JLabel("address")));
		panel.place(0,  2, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("get")));
		panel.place(0,  3, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("protocol")));
		panel.place(0,  4, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("user")));
		panel.place(0,  5, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("pass")));
		panel.place(0,  6, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("site")));
		panel.place(0,  7, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("port")));
		panel.place(0,  8, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("path")));
		
		panel.place(0,  9, 1, 1, 2, 0, 0, 0, Cell.wrap(new JLabel("uri to string")));
		
		panel.place(0, 10, 1, 1, 2, 0, 0, 0, Cell.wrap(new JLabel("uri scheme")));
		panel.place(0, 11, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("uri host")));
		panel.place(0, 12, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("uri port")));

		panel.place(0, 13, 1, 1, 2, 0, 0, 0, Cell.wrap(new JLabel("uri scheme specific part")));
		panel.place(0, 14, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("uri authority")));
		panel.place(0, 15, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("uri user info")));
		panel.place(0, 16, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("uri path")));
		panel.place(0, 17, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("uri query")));
		panel.place(0, 18, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("uri fragment")));

		panel.place(0, 19, 1, 1, 2, 0, 0, 0, Cell.wrap(new JLabel("raw scheme specific part")));
		panel.place(0, 20, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("raw authority")));
		panel.place(0, 21, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("raw user info")));
		panel.place(0, 22, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("raw path")));
		panel.place(0, 23, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("raw query")));
		panel.place(0, 24, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("raw fragment")));

		panel.place(1,  1, 1, 1, 2, 1, 0, 0, Cell.wrap(address.area).fillWide());
		panel.place(1,  2, 1, 1, 0, 1, 0, 0, Cell.wrap(get.area).fillWide());
		panel.place(1,  3, 1, 1, 0, 1, 0, 0, Cell.wrap(protocol.area).fillWide());
		panel.place(1,  4, 1, 1, 0, 1, 0, 0, Cell.wrap(user.area).fillWide());
		panel.place(1,  5, 1, 1, 0, 1, 0, 0, Cell.wrap(pass.area).fillWide());
		panel.place(1,  6, 1, 1, 0, 1, 0, 0, Cell.wrap(site.area).fillWide());
		panel.place(1,  7, 1, 1, 0, 1, 0, 0, Cell.wrap(port.area).fillWide());
		panel.place(1,  8, 1, 1, 0, 1, 0, 0, Cell.wrap(path.area).fillWide());
		
		panel.place(1,  9, 1, 1, 2, 1, 0, 0, Cell.wrap(uriToString.area).fillWide());
		
		panel.place(1, 10, 1, 1, 2, 1, 0, 0, Cell.wrap(uriScheme.area).fillWide());
		panel.place(1, 11, 1, 1, 0, 1, 0, 0, Cell.wrap(uriHost.area).fillWide());
		panel.place(1, 12, 1, 1, 0, 1, 0, 0, Cell.wrap(uriPort.area).fillWide());

		panel.place(1, 13, 1, 1, 2, 1, 0, 0, Cell.wrap(uriSchemeSpecificPart.area).fillWide());
		panel.place(1, 14, 1, 1, 0, 1, 0, 0, Cell.wrap(uriAuthority.area).fillWide());
		panel.place(1, 15, 1, 1, 0, 1, 0, 0, Cell.wrap(uriUserInfo.area).fillWide());
		panel.place(1, 16, 1, 1, 0, 1, 0, 0, Cell.wrap(uriPath.area).fillWide());
		panel.place(1, 17, 1, 1, 0, 1, 0, 0, Cell.wrap(uriQuery.area).fillWide());
		panel.place(1, 18, 1, 1, 0, 1, 0, 0, Cell.wrap(uriFragment.area).fillWide());
		
		panel.place(1, 19, 1, 1, 2, 1, 0, 0, Cell.wrap(rawSchemeSpecificPart.area).fillWide());
		panel.place(1, 20, 1, 1, 0, 1, 0, 0, Cell.wrap(rawAuthority.area).fillWide());
		panel.place(1, 21, 1, 1, 0, 1, 0, 0, Cell.wrap(rawUserInfo.area).fillWide());
		panel.place(1, 22, 1, 1, 0, 1, 0, 0, Cell.wrap(rawPath.area).fillWide());
		panel.place(1, 23, 1, 1, 0, 1, 0, 0, Cell.wrap(rawQuery.area).fillWide());
		panel.place(1, 24, 1, 1, 0, 1, 0, 0, Cell.wrap(rawFragment.area).fillWide());

		// Make the dialog box and show it on the screen
		dialog = Dialog.make("Url Test");
		dialog.setContentPane(panel.panel); // Put everything we layed out in the dialog box
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Make closing the dialog close the program
		dialog.setBounds(Screen.positionSize(800, 600)); // Set the dialog size and pick a random location
		dialog.setVisible(true); // Show the dialog box on the screen
		update(); // Parse and show output for the default starting text
	}

	private class MyDocumentListener implements DocumentListener {
		public void insertUpdate(DocumentEvent e) { update(); }
		public void removeUpdate(DocumentEvent e) { update(); }
		public void changedUpdate(DocumentEvent e) {}
	}
	
	private void update() {

		String s = in.getText();

		Url url;
		try {
			url = new Url(s);
			
			address.area.setText(url.address);
			get.area.setText(url.get);
			protocol.area.setText(url.protocol);
			user.area.setText(url.user);
			pass.area.setText(url.pass);
			site.area.setText(url.site);
			port.area.setText(url.port + "");
			path.area.setText(url.path.toString());
			
		} catch (DataException e) {

			address.area.setText("(message exception)");
			get.area.setText("");
			protocol.area.setText("");
			user.area.setText("");
			pass.area.setText("");
			site.area.setText("");
			port.area.setText("");
			path.area.setText("");
		}
		
		URI uri;
		try {
			uri = new URI(s);
			
			uriToString.area.setText(say(uri.toString()));
			
			uriScheme.area.setText(say(uri.getScheme()));
			uriHost.area.setText(say(uri.getHost()));
			uriPort.area.setText(uri.getPort() + "");
			
			uriSchemeSpecificPart.area.setText(say(uri.getSchemeSpecificPart()));
			uriAuthority.area.setText(say(uri.getAuthority()));
			uriUserInfo.area.setText(say(uri.getUserInfo()));
			uriPath.area.setText(say(uri.getPath()));
			uriQuery.area.setText(say(uri.getQuery()));
			uriFragment.area.setText(say(uri.getFragment()));
			
			rawSchemeSpecificPart.area.setText(say(uri.getRawSchemeSpecificPart()));
			rawAuthority.area.setText(say(uri.getRawAuthority()));
			rawUserInfo.area.setText(say(uri.getRawUserInfo()));
			rawPath.area.setText(say(uri.getRawPath()));
			rawQuery.area.setText(say(uri.getRawQuery()));
			rawFragment.area.setText(say(uri.getRawFragment()));

		} catch (URISyntaxException e) {
			
			uriToString.area.setText("(uri syntax exception)");
			
			uriScheme.area.setText("");
			uriHost.area.setText("");
			uriPort.area.setText("");
			
			uriSchemeSpecificPart.area.setText("");
			uriAuthority.area.setText("");
			uriUserInfo.area.setText("");
			uriPath.area.setText("");
			uriQuery.area.setText("");
			uriFragment.area.setText("");
			
			rawSchemeSpecificPart.area.setText("");
			rawAuthority.area.setText("");
			rawUserInfo.area.setText("");
			rawPath.area.setText("");
			rawQuery.area.setText("");
			rawFragment.area.setText("");
		}
	}

	private String say(String s) {
		if (s == null) return "(null)";
		else           return s;
	}
}
