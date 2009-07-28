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
import base.file.Path;
import base.state.Close;

public class SendPipe extends Close implements Pipe {

	// Object

	public SendPipe(Program program) {
		this.program = program;
		panel = new PipePanel(program, this);
		info = new PipeInfoFrame(program, this);

		hereHand = new Outline("hand", "send");
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
	
	// User
	
	@Override public PipePanel userPanel()    { return panel; }
	@Override public PipeInfoFrame userInfo() { return info; }
	
	// Folder

	@Override public String folderTitle()       { return "Send Pipe"; }
	@Override public String folderInstruction() { return "Choose the folder you want to send:"; }

	@Override public String folder(String s) {
		
		Path p = null;
		try { p = new Path(s); } catch (DataException e) { return "That text can't be a path."; }

		if (!p.existsFolder()) return "Folder not found.";
		if (p.existsFolderEmpty()) return "That folder is empty.";

		folder = p;
		return null;
	}
	
	@Override public boolean hasFolder() { return folder != null; }

	
	
	
	@Override public String homeCode() { return Main.flag + hereHi.toData().base62(); }
	

	@Override public void close() {
		if (already()) return;
		close(info);
	}


	@Override public void awayCode(String s) {
		
		TextSplit split = Text.split(away, Main.flag);
		if (!split.found || Text.hasText(split.before)) return false;
		
		try {
		} catch (DataException e) { return false; }
		awayHi = new Outline(Encode.fromBase62(split.after));
		return true;
		
		
		

			
		} catch (DataException e) { return false; }
	}

	@Override public boolean hasAwayCode() { return awayHi != null; }
	
	

	@Override public void go() {
	}

}
