package pipe.main;

import base.data.Bin;
import base.process.Mistake;


public class Snippet {

	public static void snippet(Program program) {

		try {
			Bin.snippet();
		} catch (Exception e) { Mistake.stop(e); }
	}
	
	
}
