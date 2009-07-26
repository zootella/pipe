package pipe.core.museum;

import pipe.user.PipeInfoFrame;
import pipe.user.PipePanel;
import base.data.Outline;
import base.file.Path;

public class MyPipe implements Pipe {

	@Override public Outline away(Outline o) {
		return null;
	}

	@Override public Outline away() {
		return null;
	}

	@Override public void close() {
	}

	@Override public Path folder(Path p) {
		return null;
	}

	@Override public Path folder() {
		return null;
	}

	@Override public void go() {
	}

	@Override public Outline home() {
		return null;
	}

	@Override public PipeInfoFrame info() {
		return null;
	}

	@Override public String instruction() {
		return null;
	}

	@Override public PipePanel panel() {
		return null;
	}

	@Override public boolean ready() {
		return false;
	}

	@Override public String title() {
		return null;
	}

}
