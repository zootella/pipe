package base.data.encrypt;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import base.data.Bin;
import base.data.Data;
import base.exception.ChopException;
import base.exception.DataException;
import base.exception.PlatformException;

public class All {
	
	public static final String algorithm = "AES";
	public static final int size = 128; // Comes with Java and good enough for US classified Secret

	public static Data key(String algorithm, int size) {
		try {
			KeyGenerator g = KeyGenerator.getInstance(algorithm);
			g.init(size);
			SecretKey k = g.generateKey();
			return new Data(k.getEncoded());
		}
		catch (NoSuchAlgorithmException e)  { throw new PlatformException(e); }
		catch (InvalidParameterException e) { throw new PlatformException(e); }
	}
	
	public static Cipher cipher(String algorithm, Data key, int mode) {
		try {
			Cipher c = Cipher.getInstance(algorithm);
			c.init(mode, new SecretKeySpec(key.toByteArray(), algorithm));
			return c;
		}
		catch (NoSuchAlgorithmException e)  { throw new PlatformException(e); }
		catch (NoSuchPaddingException e)    { throw new PlatformException(e); }
		catch (InvalidKeyException e)       { throw new DataException(e); }
	}
	
	public static void encrypt(Cipher cipher, Bin source, Bin destination) {
		byte[] encrypted = cipher.update(source.data().toByteArray());
	}
	
	public static void decrypt(Cipher cipher, Bin source, Bin destination) {
		byte[] original = cipher.update(source.data().toByteArray());
	}

	
	
	
}
