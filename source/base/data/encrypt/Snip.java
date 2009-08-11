package base.data.encrypt;


import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class Snip {

	/* A 1024-bit key will encrypt messages up to 117 bytes long. */
	private static final int KEY_SIZE = 1024;

	private static final String XFORM = "RSA/ECB/PKCS1PADDING"; // from poeticcode, works
	/*
	private static final String XFORM = "RSA/ECB/NoPadding"; // from poeticcode, no exception but bad data
	private static final String XFORM = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING"; //from stack overflow, works
	private static final String XFORM = "RSA"; // made up, works
	 */
	/*
	private static final String XFORM = "RSA/None/NoPadding"; // from javadoc, i think, throws exception
	private static final String XFORM = "RSA/NONE/NOPADDING"; // from javadoc, i think, throws exception
	private static final String XFORM = "RSA/NONE"; // from javadoc, i think, throws exception
	 */
	

	public static KeyPair generateRSAKeyPair() throws GeneralSecurityException {
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		gen.initialize(KEY_SIZE);
		return gen.generateKeyPair();
	}

	public static byte[] encrypt(byte[] plaintext, PublicKey pub) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(XFORM);
		cipher.init(Cipher.ENCRYPT_MODE, pub);
		return cipher.doFinal(plaintext);
	}

	public static byte[] decrypt(byte[] ciphertext, PrivateKey pvt) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(XFORM);
		cipher.init(Cipher.DECRYPT_MODE, pvt);
		return cipher.doFinal(ciphertext);
	}

	public static void snippet() throws Exception {
		KeyPair pair = Snip.generateRSAKeyPair();
		byte[] plaintext = "A short secret message.".getBytes("UTF-8");
		byte[] ciphertext = Snip.encrypt(plaintext, pair.getPublic());
		byte[] recovered = Snip.decrypt(ciphertext, pair.getPrivate());
		System.out.println(new String(recovered, "UTF-8"));
	}

}