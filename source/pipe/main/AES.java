package pipe.main;

import java.security.InvalidParameterException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import base.data.Data;
import base.exception.PlatformException;

public class AES {

	public static void go() throws Exception {
		
		
		Security.addProvider(new BouncyCastleProvider());

		KeyGenerator generate = KeyGenerator.getInstance("AES", "BC");//this is really slow, you actually have to do it in a task, have a global static
		try {
			generate.init(256); // Choices are 128, 192 and 256
		} catch (InvalidParameterException e) { throw new PlatformException(e); }

		SecretKey key = generate.generateKey();
		byte[] raw = key.getEncoded();
		System.out.println("raw " + new Data(raw).base16());
		
		
		SecretKeySpec key2 = new SecretKeySpec(raw, "AES");

		Cipher cipher = Cipher.getInstance("AES", "BC");
		cipher.init(Cipher.ENCRYPT_MODE, key2);

		byte[] encrypted = cipher.doFinal("This is just an example".getBytes());
		System.out.println("encrypted string: " + new Data(encrypted).base16());

		cipher.init(Cipher.DECRYPT_MODE, key2);
		byte[] original = cipher.doFinal(encrypted);
		String originalString = new String(original);
		System.out.println("Original string: " + originalString + " " + new Data(original).base16());
	}
}