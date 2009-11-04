package org.zootella.encrypt.sign;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;

import org.zootella.data.Data;
import org.zootella.exception.DataException;
import org.zootella.exception.PlatformException;

public class Sign {
	
	// Define
	
	/** DSA, the kind of digital signature we use. */
	public static final String algorithm = "DSA";
	/** 1024 bit key size. */
	public static final int size = 1024;

	// Key

	/** Make a new public and private key pair. */
	public static SignKey make() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
			generator.initialize(size);
			KeyPair key = generator.generateKeyPair();

			KeyFactory factory = KeyFactory.getInstance(algorithm);
			return new SignKey(
				factory.getKeySpec(key.getPublic(), DSAPublicKeySpec.class),
				factory.getKeySpec(key.getPrivate(), DSAPrivateKeySpec.class));
		}
		catch (NoSuchAlgorithmException e) { throw new PlatformException(e); }
		catch (InvalidKeySpecException e)  { throw new PlatformException(e); }
	}

	// Sign
	
	/** Sign data with the given private key. */
	public static Data sign(Data data, SignKey key) {
		try {
			PrivateKey privateKey = KeyFactory.getInstance(algorithm).generatePrivate(key.toPrivateSpec());
			Signature s = Signature.getInstance(algorithm);
			s.initSign(privateKey);
			s.update(data.toByteArray());
			return new Data(s.sign());
		}
		catch (NoSuchAlgorithmException e) { throw new PlatformException(e); }
		catch (InvalidKeySpecException e)  { throw new DataException(e); }
		catch (InvalidKeyException e)      { throw new DataException(e); }
		catch (SignatureException e)       { throw new DataException(e); }
	}

	/** true if signature proves that the given public key signed data, false data corrupted. */
	public static boolean verify(Data data, Data signature, SignKey key) {
		try {
			PublicKey publicKey = KeyFactory.getInstance(algorithm).generatePublic(key.toPublicSpec());
			Signature s = Signature.getInstance(algorithm);
			s.initVerify(publicKey);
			s.update(data.toByteArray());
			return s.verify(signature.toByteArray());
		}
		catch (NoSuchAlgorithmException e) { throw new PlatformException(e); }
		catch (InvalidKeySpecException e)  { throw new DataException(e); }
		catch (InvalidKeyException e)      { throw new DataException(e); }
		catch (SignatureException e)       { throw new DataException(e); }
	}
}
