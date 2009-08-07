package pipe.main;

import java.security.InvalidParameterException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import base.data.Data;
import base.exception.PlatformException;

public class AES {

	public static void go() throws Exception {

		KeyGenerator generate = KeyGenerator.getInstance("AES");//this is really slow, you actually have to do it in a task, have a global static
		try {
			generate.init(192); // Choices are 128, 192 and 256
		} catch (InvalidParameterException e) { throw new PlatformException(e); }

		SecretKey key = generate.generateKey();
		byte[] raw = key.getEncoded();
		System.out.println("raw " + new Data(raw).base16());

		SecretKeySpec key2 = new SecretKeySpec(raw, "AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key2);

		byte[] encrypted = cipher.doFinal("This is just an example".getBytes());
		System.out.println("encrypted string: " + new Data(encrypted).base16());

		cipher.init(Cipher.DECRYPT_MODE, key2);
		byte[] original = cipher.doFinal(encrypted);
		String originalString = new String(original);
		System.out.println("Original string: " + originalString + " " + new Data(original).base16());
	}
}