package pipe.main;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import base.data.Bin;
import base.data.Data;
import base.exception.PlatformException;
import base.process.Mistake;
import base.time.Now;


public class Snippet {

	public static void snippet(Program program) {

		try {
			Bin.snippet();
		} catch (Exception e) { Mistake.stop(e); }
	}
	
	
}
