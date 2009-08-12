package base.encrypt.pair;

import java.math.BigInteger;
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

public class Pair {

	/** RSA, the kind of public and private key pair encryption we use. */
	public static final String algorithm = "RSA";
	/** The transformation and padding we use. */
	private static final String transformation = "RSA/ECB/PKCS1PADDING";
	/** 1024 bit key size, ships with Java, will encrypt messages up to 177 bytes long. */
	public static final int size = 1024;

	/** Make a new public and private key pair. */
	public static KeyData make() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
			generator.initialize(size);
			KeyPair key = generator.generateKeyPair();
			
			KeyFactory factory = KeyFactory.getInstance(algorithm);
			RSAPublicKeySpec publicSpec = factory.getKeySpec(key.getPublic(), RSAPublicKeySpec.class);
			RSAPrivateKeySpec privateSpec = factory.getKeySpec(key.getPrivate(), RSAPrivateKeySpec.class);
			
			BigInteger modulus = publicSpec.getModulus();
			BigInteger publicExponent = publicSpec.getPublicExponent();
			BigInteger privateExponent = privateSpec.getPrivateExponent();
			
			Data modulusData = new Data(modulus.toByteArray());
			Data publicExponentData = new Data(publicExponent.toByteArray());
			Data privateExponentData = new Data(privateExponent.toByteArray());
			
			return new KeyData(modulusData, publicExponentData, privateExponentData);
		}
		catch (NoSuchAlgorithmException e) { throw new PlatformException(e); }
		catch (InvalidKeySpecException e)  { throw new PlatformException(e); }
	}

	/** Use the given modulus and a peer's public exponent to encrypt data just for the peer. */
	public static Data encrypt(Data data, Data modulus, Data publicExponent) {
		try {
			BigInteger modulusBig = new BigInteger(modulus.toByteArray());
			BigInteger publicExponentBig = new BigInteger(publicExponent.toByteArray());

			KeyFactory factory = KeyFactory.getInstance(algorithm);
			PublicKey publicKey = factory.generatePublic(new RSAPublicKeySpec(modulusBig, publicExponentBig));

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
			BigInteger modulusBig = new BigInteger(modulus.toByteArray());
			BigInteger privateExponentBig = new BigInteger(privateExponent.toByteArray());

			KeyFactory factory = KeyFactory.getInstance(algorithm);
			PrivateKey privateKey = factory.generatePrivate(new RSAPrivateKeySpec(modulusBig, privateExponentBig));

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
