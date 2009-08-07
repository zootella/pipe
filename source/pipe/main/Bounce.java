package pipe.main;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.agreement.DHBasicAgreement;
import org.bouncycastle.crypto.generators.DHBasicKeyPairGenerator;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;

import junit.framework.*;

public class Bounce extends TestCase {
	/** We use this algorithm for computing the shared key. It is 
hard-coded for 
simpler use of this class. */
	private static final String KEYAGREEMENT_ALGORITHM = "DiffieHellman";

	/** The 1024 bit Diffie-Hellman modulus values used by SKIP */
	private static final byte skip1024ModulusBytes[] = { (byte) 0xF4,
		(byte) 0x88, (byte) 0xFD, (byte) 0x58, (byte) 0x4E, 
		(byte) 0x49,
		(byte) 0xDB, (byte) 0xCD, (byte) 0x20, (byte) 0xB4, 
		(byte) 0x9D,
		(byte) 0xE4, (byte) 0x91, (byte) 0x07, (byte) 0x36, 
		(byte) 0x6B,
		(byte) 0x33, (byte) 0x6C, (byte) 0x38, (byte) 0x0D, 
		(byte) 0x45,
		(byte) 0x1D, (byte) 0x0F, (byte) 0x7C, (byte) 0x88, 
		(byte) 0xB3,
		(byte) 0x1C, (byte) 0x7C, (byte) 0x5B, (byte) 0x2D, 
		(byte) 0x8E,
		(byte) 0xF6, (byte) 0xF3, (byte) 0xC9, (byte) 0x23, 
		(byte) 0xC0,
		(byte) 0x43, (byte) 0xF0, (byte) 0xA5, (byte) 0x5B, 
		(byte) 0x18,
		(byte) 0x8D, (byte) 0x8E, (byte) 0xBB, (byte) 0x55, 
		(byte) 0x8C,
		(byte) 0xB8, (byte) 0x5D, (byte) 0x38, (byte) 0xD3, 
		(byte) 0x34,
		(byte) 0xFD, (byte) 0x7C, (byte) 0x17, (byte) 0x57, 
		(byte) 0x43,
		(byte) 0xA3, (byte) 0x1D, (byte) 0x18, (byte) 0x6C, 
		(byte) 0xDE,
		(byte) 0x33, (byte) 0x21, (byte) 0x2C, (byte) 0xB5, 
		(byte) 0x2A,
		(byte) 0xFF, (byte) 0x3C, (byte) 0xE1, (byte) 0xB1, 
		(byte) 0x29,
		(byte) 0x40, (byte) 0x18, (byte) 0x11, (byte) 0x8D, 
		(byte) 0x7C,
		(byte) 0x84, (byte) 0xA7, (byte) 0x0A, (byte) 0x72, 
		(byte) 0xD6,
		(byte) 0x86, (byte) 0xC4, (byte) 0x03, (byte) 0x19, 
		(byte) 0xC8,
		(byte) 0x07, (byte) 0x29, (byte) 0x7A, (byte) 0xCA, 
		(byte) 0x95,
		(byte) 0x0C, (byte) 0xD9, (byte) 0x96, (byte) 0x9F, 
		(byte) 0xAB,
		(byte) 0xD0, (byte) 0x0A, (byte) 0x50, (byte) 0x9B, 
		(byte) 0x02,
		(byte) 0x46, (byte) 0xD3, (byte) 0x08, (byte) 0x3D, 
		(byte) 0x66,
		(byte) 0xA4, (byte) 0x5D, (byte) 0x41, (byte) 0x9F, 
		(byte) 0x9C,
		(byte) 0x7C, (byte) 0xBD, (byte) 0x89, (byte) 0x4B, 
		(byte) 0x22,
		(byte) 0x19, (byte) 0x26, (byte) 0xBA, (byte) 0xAB, 
		(byte) 0xA2,
		(byte) 0x5E, (byte) 0xC3, (byte) 0x55, (byte) 0xE9, 
		(byte) 0x2F,
		(byte) 0x78, (byte) 0xC7 };

	/** The SKIP 1024 bit modulus. This is only a BigInterger 
representation of 
skip1024ModulusBytes, but kept for performance reasons. */
	public static final BigInteger skip1024Modulus = new BigInteger(1,
			skip1024ModulusBytes);

	/** The base used with the SKIP 1024 bit modulus */
	public static final BigInteger skip1024Base = BigInteger.valueOf(2);

	public void testDHAgreement() throws Exception {
		// the error only happens with some probability, so try more often
		for (int i=0; i<10; i++) {
			System.out.println("Try " + i);

			// DH intialization with JCE
			KeyAgreement dh1 = javax.crypto.KeyAgreement.getInstance(KEYAGREEMENT_ALGORITHM);
			KeyPairGenerator kg1 = KeyPairGenerator.getInstance(KEYAGREEMENT_ALGORITHM);
			DHParameterSpec ps = new 
			DHParameterSpec(skip1024Modulus, skip1024Base);
			kg1.initialize(ps);
			KeyPair myKeypair1 = kg1.generateKeyPair();
			dh1.init(myKeypair1.getPrivate());

			// DH initialization with BC
			DHBasicAgreement dh2 = new org.bouncycastle.crypto.agreement.DHBasicAgreement();
			DHBasicKeyPairGenerator kg2 = new DHBasicKeyPairGenerator();
			kg2.init(new DHKeyGenerationParameters(new 
					SecureRandom(), new 
					DHParameters(skip1024Modulus, skip1024Base)));
			AsymmetricCipherKeyPair myKeypair2 = kg2.generateKeyPair();
			dh2.init(myKeypair2.getPrivate());

			// get JCE side public key
			byte[] publicKey1 = ((DHPublicKey) 
					myKeypair1.getPublic()).getY().toByteArray();

			// get BC side public key
			byte[] publicKey2 = ((DHPublicKeyParameters) 
					myKeypair2.getPublic()) .getY().toByteArray();

			// add other public key to JCE side
			DHPublicKey remotePublicKey1 = (DHPublicKey) 
			java.security.KeyFactory.getInstance(
					KEYAGREEMENT_ALGORITHM).generatePublic(
							new DHPublicKeySpec(new 
									BigInteger(publicKey2), skip1024Modulus, 
									skip1024Base));
			dh1.doPhase(remotePublicKey1, true);
			byte[] sharedKey1 = dh1.generateSecret();

			// add other public key to BC side
			DHPublicKeyParameters remotePublicKey2 = new 
			DHPublicKeyParameters(new 
					BigInteger(publicKey1), 
					new DHParameters(skip1024Modulus, skip1024Base));
			byte[] sharedKey2 = dh2.calculateAgreement(remotePublicKey2).toByteArray();

			// and compare - must be equal
			Assert.assertTrue("Shared keys to not match", compareByteArrays(sharedKey1, sharedKey2));
		}
	}

	private boolean compareByteArrays(byte[] b1, byte[] b2) {
		if (b1.length != b2.length)
			return false;
		for (int i=0; i<b1.length; i++)
			if (b1[i] != b2[i])
				return false;
		return true;
	}
}