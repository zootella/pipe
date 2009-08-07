package pipe.main;

import base.process.Mistake;


public class Snippet {

	public static void snippet(Program program) {
		
		

		try {
			AES.go();
		} catch (Exception e) { Mistake.stop(e); }
		
		

	}
}
