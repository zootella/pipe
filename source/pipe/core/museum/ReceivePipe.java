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
import base.file.Path;
import base.process.Mistake;
import base.state.Close;

public class ReceivePipe extends Close implements Pipe {

	// Object
	
	public ReceivePipe(Program program) {
		this.program = program;
		panel = new PipePanel(program, this);
		info = new PipeInfoFrame(program, this);

		hereHand = new Outline("hand", "receive");
		hereHand.add("unique", Data.unique());
		hereHand.add("i", program.core.here.internet().data());
		hereHand.add("l", program.core.here.lan().data());
		
		hereHi = new Outline("h");
		hereHi.add("i", program.core.here.internet().data());
		hereHi.add("l", program.core.here.lan().data());
		hereHi.add("h", hereHand.toData().hash().start(6)); // Just the first 6 bytes of the 20-byte SHA1 hash
	}
	
	private final Program program;
	
	private final PipePanel panel;
	private final PipeInfoFrame info;
	
	private Path folder;
	
	private Outline hereHi;
	private Outline hereHand;
	private Outline awayHi;
	private Outline awayHand;
	
	@Override public void close() {
		if (already()) return;
		close(info);
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
	
	@Override public void go() {}
}
