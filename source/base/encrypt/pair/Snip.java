package base.encrypt.pair;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
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

import base.data.Data;
import base.exception.DataException;
import base.exception.PlatformException;

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
		KeyPair key = generator.generateKeyPair();
		
		PublicKey publicKey = key.getPublic();
		PrivateKey privateKey = key.getPrivate();

		Data publicKeyData = new Data(publicKey.getEncoded());
		Data privateKeyData = new Data(privateKey.getEncoded());
		
		System.out.println("public  " + publicKeyData.size()  + " " + publicKeyData.base16());
		System.out.println("private " + privateKeyData.size() + " " + privateKeyData.base16());
		
		
		
		return key;
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

		//moar
		
		KeyFactory factory = KeyFactory.getInstance(algorithm);
		RSAPublicKeySpec publicSpec = factory.getKeySpec(key.getPublic(), RSAPublicKeySpec.class);
		RSAPrivateKeySpec privateSpec = factory.getKeySpec(key.getPrivate(), RSAPrivateKeySpec.class);

		BigInteger modulus = publicSpec.getModulus();
		BigInteger publicExponent = publicSpec.getPublicExponent();
		
		BigInteger modulusFromPrivate = privateSpec.getModulus();
		BigInteger privateExponent = privateSpec.getPrivateExponent();
		
		Data modulusData = new Data(modulus.toByteArray());
		
		BigInteger modulusRemade = new BigInteger(modulusData.toByteArray());
		
		
		
		KeyFactory fact = KeyFactory.getInstance(algorithm);
	    PublicKey pubKey = fact.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));		
		
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		
	}
	
	public static Data encrypt(Data data, Data modulus, Data exponent) {
		try {
			BigInteger modulusBig = new BigInteger(modulus.toByteArray());
			BigInteger exponentBig = new BigInteger(exponent.toByteArray());

			KeyFactory factory = KeyFactory.getInstance(algorithm);
			PublicKey publicKey = factory.generatePublic(new RSAPublicKeySpec(modulusBig, exponentBig));

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

	public static Data decrypt(Data data, Data modulus, Data exponent) {
		try {
			BigInteger modulusBig = new BigInteger(modulus.toByteArray());
			BigInteger exponentBig = new BigInteger(exponent.toByteArray());

			KeyFactory factory = KeyFactory.getInstance(algorithm);
			PrivateKey privateKey = factory.generatePrivate(new RSAPublicKeySpec(modulusBig, exponentBig));

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