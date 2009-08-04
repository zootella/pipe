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
import base.exception.DiskException;
import base.exception.ProgramException;
import base.file.Path;
import base.net.flow.SocketBay;
import base.net.name.IpPort;
import base.process.Mistake;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

public class ReceivePipe extends Close implements Pipe {

	// Object
	
	public ReceivePipe(Program program) {
		this.program = program;
		panel = new PipePanel(program, this);
		info = new PipeInfoFrame(program, this);

		hereHello = new Outline("hello", "receive");
		hereHello.add("unique", Data.unique());
		
		hereHi = new Outline("h");
		hereHi.add("i", program.core.here.internet().data());
		hereHi.add("l", program.core.here.lan().data());
		hereHi.add("h", hereHello.toData().hash().start(6)); // Just the first 6 bytes of the 20-byte SHA1 hash
		
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
	
	@Override public String folderTitle() { return "Receive Pipe"; }
	@Override public String folderInstruction() { return "Choose an empty folder to receive the incoming:"; }
	
	@Override public String folder(String s) {
		if (hasFolder()) throw new IllegalStateException();
		
		Path p = null;
		try { p = new Path(s); } catch (DataException e) { return "That text isn't a valid path."; }
		
		try { p.folder(); } catch (DiskException e) { return "Cannot find or make folder."; }
		try { p.folderWrite(); } catch (DiskException e) { return "Cannot write in folder."; }
		if (p.existsFolderFull()) return "Please choose an empty folder.";
		
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
				
				if (no(socket) && no(connect) && awayHi != null)
					connect = new PipeConnect(
						program,
						update,
						new IpPort(awayHi.value("l")),
						new IpPort(awayHi.value("i")),
						hereHello.toData(),
						awayHi.value("h"));

				if (no(socket) && done(connect)) {
					socket = connect.result();
					socket.up = update;
					
					System.out.println("receive pipe has socket");
				}

			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private ReceivePipe me() { return this; }
	
	public ProgramException exception() { return exception; }
	private ProgramException exception;
}
