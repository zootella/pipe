package pipe.core.museum;

import pipe.main.Main;
import pipe.main.Program;
import pipe.user.PipeInfoFrame;
import pipe.user.PipePanel;
import base.data.Data;
import base.data.Outline;
import base.data.Text;
import base.data.TextSplit;
import base.encode.Encode;
import base.exception.DataException;
import base.exception.ProgramException;
import base.file.Path;
import base.net.connect.ConnectTask;
import base.net.flow.SocketBay;
import base.net.name.IpPort;
import base.net.socket.Socket;
import base.process.Mistake;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

public class SendPipe extends Close implements Pipe {

	// Object

	public SendPipe(Program program) {
		this.program = program;
		panel = new PipePanel(program, this);
		info = new PipeInfoFrame(program, this);

		hereHello = new Outline("hello", "send");
		hereHello.add("unique", Data.unique());
		hereHello.add("i", program.core.here.internet().data());
		hereHello.add("l", program.core.here.lan().data());
		
		hereHi = new Outline("h");
		hereHi.add("h", hereHello.toData().hash().start(6)); // Just the first 6 bytes of the 20-byte SHA1 hash
		hereHi.add("i", program.core.here.internet().data());
		hereHi.add("l", program.core.here.lan().data());
		
		update = new Update(new MyReceive());
	}
	
	private final Program program;
	private final Update update;
	
	private final PipePanel panel;
	private final PipeInfoFrame info;
	
	private Path folder;

	private Outline hereHi;
	private Outline hereHello;
	private Outline awayHi;
	private Outline awayHello;
	
	private PipeConnect connect;
	private SocketBay socket;

	@Override public void close() {
		if (already()) return;
		close(info);
		close(connect);
		close(socket);
	}
	
	// User
	
	@Override public PipePanel userPanel() { return panel; }
	@Override public PipeInfoFrame userInfo() { return info; }

	// Folder

	@Override public String folderTitle() { return "Send Pipe"; }
	@Override public String folderInstruction() { return "Choose the folder you want to send:"; }

	@Override public String folder(String s) {
		if (hasFolder()) throw new IllegalStateException();
		
		Path p = null;
		try { p = new Path(s); } catch (DataException e) { return "That text isn't a valid path."; }

		if (!p.existsFolder()) return "Folder not found.";
		if (p.existsFolderEmpty()) return "That folder is empty.";

		folder = p;
		return null;
	}
	
	@Override public boolean hasFolder() { return folder != null; }

	// Code

	@Override public String homeCode() { return Main.flag + hereHi.toData().base62(); }

	@Override public void awayCode(String s) {
		if (hasAwayCode()) throw new IllegalStateException();

		TextSplit split = Text.split(s, Main.flag);
		if (!split.found || Text.hasText(split.before)) return;
		
		try {
			awayHi = new Outline(Encode.fromBase62(split.after));
		} catch (DataException e) { Mistake.ignore(e); }
	}

	@Override public boolean hasAwayCode() { return awayHi != null; }

	// Go
	
	@Override public void go() {
		update.send();
		
		
		
		
		
	}
	

	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			try {
				
				/*
				if (no(socket) && no(connect) && awayHi != null)
					connect = new PipeConnect(
						program,
						update,
						new IpPort(awayHi.value("l")),
						new IpPort(awayHi.value("i")),
						hereHello.toData(),
						awayHi.value("h"));

				if (no(socket) && done(connect)) {
					socket = new SocketBay(update, connect.result());
					socket.upload().add(hereHello.toData());
				}
				*/
			

			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private SendPipe me() { return this; }
	
	public ProgramException exception() { return exception; }
	private ProgramException exception;

		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
