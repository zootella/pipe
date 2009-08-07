package pipe.main;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import base.process.Mistake;

public class Snippet {

	public static void snippet(Program program) {

		try {
			AES.go();
		} catch (Exception e) { Mistake.stop(e); }
		
		

		/*
		StandardPBEStringEncryptor mySecondEncryptor = new StandardPBEStringEncryptor();
		mySecondEncryptor.setProviderName("BC");
		mySecondEncryptor.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
		mySecondEncryptor.setPassword(myPassword);

		String mySecondEncryptedText = mySecondEncryptor.encrypt(myText);		
		*/

	}
}
