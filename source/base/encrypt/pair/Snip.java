package base.encrypt.pair;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import base.data.Data;

public class Snip {

	// Define
	
	/** RSA, the kind of public and private key pair encryption we use. */
	public static final String algorithm = "RSA";
	/** The transformation and padding we use. */
	private static final String transformation = "RSA/ECB/PKCS1PADDING";
	/** 1024 bit key size, ships with Java, will encrypt messages up to 177 bytes long. */
	public static final int size = 1024;


	

	public static KeyPair generateRSAKeyPair() throws GeneralSecurityException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
		generator.initialize(size);
		return generator.generateKeyPair();
	}

	public static Data encrypt(Data data, PublicKey publicKey) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return new Data(cipher.doFinal(data.toByteArray()));
	}

	public static Data decrypt(Data data, PrivateKey privateKey) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new Data(cipher.doFinal(data.toByteArray()));
	}

	public static void snippet() throws Exception {
		
		KeyPair key = Snip.generateRSAKeyPair();
		
		Data a = new Data("A short secret message.");
		Data b = Snip.encrypt(a, key.getPublic());
		Data c = Snip.decrypt(b, key.getPrivate());
		
		System.out.println(c.strike());
	}

}