package org.zootella.encrypt.sign;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.zootella.data.Data;
import org.zootella.exception.DataException;
import org.zootella.exception.PlatformException;

public class Sign {
	
	// Define
	
	private static final String provider = "SUN";
	private static final String algorithm = "DSA";
	private static final String algorithmDetail = "SHA1withDSA";
	private static final String algorithmRandom = "SHA1PRNG";
	private static final int size = 1024;

	// Key

	public static SignKeyData make() {
		try {
			KeyPairGenerator g = KeyPairGenerator.getInstance(algorithm, provider);
			SecureRandom r = SecureRandom.getInstance(algorithmRandom, provider);
			g.initialize(size, r);
			
			KeyPair k = g.generateKeyPair();
			return new SignKeyData(
				new Data(k.getPublic().getEncoded()),
				new Data(k.getPrivate().getEncoded()));
		}
		catch (NoSuchAlgorithmException e) { throw new PlatformException(e); }
		catch (NoSuchProviderException e)  { throw new PlatformException(e); }
	}

	// Sign
	
	public static Data sign(Data message, Data privateKeyData) {
		try {
			X509EncodedKeySpec x = new X509EncodedKeySpec(privateKeyData.toByteArray());
			KeyFactory f = KeyFactory.getInstance(algorithm, provider);
			PrivateKey k = f.generatePrivate(x);

			Signature s = Signature.getInstance(algorithmDetail);
			s.initSign(k);
			s.update(message.toByteArray());
			return new Data(s.sign());
		}
		catch (NoSuchProviderException e)  { throw new PlatformException(e); }
		catch (NoSuchAlgorithmException e) { throw new PlatformException(e); }
		catch (InvalidKeySpecException e)  { throw new DataException(e); }
		catch (InvalidKeyException e)      { throw new DataException(e); }
		catch (SignatureException e)       { throw new DataException(e); }
	}
	
	public static boolean verify(Data message, Data signature, Data publicKeyData) {
		try {
			X509EncodedKeySpec x = new X509EncodedKeySpec(publicKeyData.toByteArray());
			KeyFactory f;
			f = KeyFactory.getInstance(algorithm, provider);
			PublicKey k = f.generatePublic(x);

			Signature s = Signature.getInstance(algorithmDetail);
			s.initVerify(k);
			s.update(message.toByteArray());
			return s.verify(signature.toByteArray());
			
			//TODO confirm if the message is corrupted, this returns false, it shouldn't throw an exception
		}
		catch (NoSuchProviderException e)  { throw new PlatformException(e); }
		catch (NoSuchAlgorithmException e) { throw new PlatformException(e); }
		catch (InvalidKeySpecException e)  { throw new DataException(e); }
		catch (InvalidKeyException e)      { throw new DataException(e); }
		catch (SignatureException e)       { throw new DataException(e); }
	}
	
	

}
