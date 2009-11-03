package org.zootella.encrypt.pair;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.zootella.data.Data;
import org.zootella.exception.DataException;
import org.zootella.exception.PlatformException;

public class Pair {
	
	// Define

	/** RSA, the kind of public and private key pair encryption we use. */
	public static final String algorithm = "RSA";
	/** The transformation and padding we use. */
	private static final String transformation = "RSA/ECB/PKCS1PADDING";
	/** 1024 bit key size, ships with Java. */
	public static final int size = 1024;
	
	/** 117 byte message size, the largest message a 1024 bit key can encrypt. */
	public static final int messageSize = 117;
	
	// Key

	/** Make a new public and private key pair. */
	public static PairKeyData make() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
			generator.initialize(size);
			KeyPair key = generator.generateKeyPair();
			
			KeyFactory factory = KeyFactory.getInstance(algorithm);
			RSAPublicKeySpec publicSpec = factory.getKeySpec(key.getPublic(), RSAPublicKeySpec.class);
			RSAPrivateKeySpec privateSpec = factory.getKeySpec(key.getPrivate(), RSAPrivateKeySpec.class);

			return new PairKeyData(
				new Data(publicSpec.getModulus()),
				new Data(publicSpec.getPublicExponent()),
				new Data(privateSpec.getPrivateExponent()));
		}
		catch (NoSuchAlgorithmException e) { throw new PlatformException(e); }
		catch (InvalidKeySpecException e)  { throw new PlatformException(e); }
	}
	
	// Encrypt

	/** Use the given modulus and a peer's public exponent to encrypt data just for the peer. */
	public static Data encrypt(Data data, Data modulus, Data publicExponent) {
		try {
			KeyFactory factory = KeyFactory.getInstance(algorithm);
			PublicKey publicKey = factory.generatePublic(new RSAPublicKeySpec(modulus.toBigInteger(), publicExponent.toBigInteger()));

			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return new Data(cipher.doFinal(data.toByteArray()));
		}
		catch (NoSuchAlgorithmException e)  { throw new PlatformException(e); }
		catch (NoSuchPaddingException e)    { throw new PlatformException(e); }
		catch (NumberFormatException e)     { throw new DataException(e); }
		catch (InvalidKeySpecException e)   { throw new DataException(e); }
		catch (InvalidKeyException e)       { throw new DataException(e); }
		catch (IllegalBlockSizeException e) { throw new DataException(e); }
		catch (BadPaddingException e)       { throw new DataException(e); }
	}

	/** Use the modulus and our private exponent to decrypt data that was encrypted just for us. */
	public static Data decrypt(Data data, Data modulus, Data privateExponent) {
		try {
			KeyFactory factory = KeyFactory.getInstance(algorithm);
			PrivateKey privateKey = factory.generatePrivate(new RSAPrivateKeySpec(modulus.toBigInteger(), privateExponent.toBigInteger()));

			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new Data(cipher.doFinal(data.toByteArray()));
		}
		catch (NoSuchAlgorithmException e)  { throw new PlatformException(e); }
		catch (NoSuchPaddingException e)    { throw new PlatformException(e); }
		catch (NumberFormatException e)     { throw new DataException(e); }
		catch (InvalidKeySpecException e)   { throw new DataException(e); }
		catch (InvalidKeyException e)       { throw new DataException(e); }
		catch (IllegalBlockSizeException e) { throw new DataException(e); }
		catch (BadPaddingException e)       { throw new DataException(e); }
	}
}
