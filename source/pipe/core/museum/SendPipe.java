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
	
	public SendPipe(Program program) {
		this.program = program;
		panel = new PipePanel(program, this);
		info = new PipeInfoFrame(program, this);

		hereHand = new Outline("hand");
		hereHand.add("type", "send");
		hereHand.add("unique", Data.unique());
		hereHand.add("i", program.core.here.internet().data());
		hereHand.add("l", program.core.here.lan().data());
		
		hereHi = new Outline("h");
		hereHi.add("i", program.core.here.internet().data());
		hereHi.add("l", program.core.here.lan().data());
		hereHi.add("h", hereHand.toData().hash().start(6)); // Just the first 6 bytes of the 20-byte SHA1 hash
	}
	
	private final Program program;
	
	@Override public PipePanel panel() { return panel; }
	@Override public PipeInfoFrame info() { return info; }

	@Override public String title()       { return "Send Pipe"; }
	@Override public String instruction() { return "Choose the folder you want to send:"; }
	@Override public String home()        { return Main.flag + hereHi.toData().base62(); }
	
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

	@Override public boolean folder(Path p) {
		if (p == null) return false;
		this.folder = p;
		return true;
	}

	@Override public boolean away(String away) {
		if (away == null) return false;
		try {

			TextSplit split = Text.split(away, Main.flag);
			if (!split.found || Text.hasText(split.before)) return false;
			
			awayHi = new Outline(Encode.fromBase62(split.after));
			return true;
			
		} catch (DataException e) { return false; }
	}

	
	
	@Override public boolean ready() {
		return folder != null && awayHi != null;
	}

	@Override public void go() {
	}

}
