package org.zootella.encrypt.sign;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.KeySpec;

import org.junit.Assert;
import org.junit.Test;
import org.zootella.data.Data;

public class SignTest {
	
	@Test public void test() throws Exception {
		
		
		// try it for the first time:
		// make a key
		// sign something
		// validate the signature
		// corrupt the message
		// confirm validate returns false
		
		//TODO confirm if the message is corrupted, this returns false, it shouldn't throw an exception
		
		SignKey key = Sign.make();
		Data message = new Data("hello");
		Data signature = Sign.sign(message, key);
		boolean valid = Sign.verify(message, signature, key);
		Assert.assertTrue(valid);
		
		main2();
		
		
	}
	
	
	public static void main() throws Exception {
		String alg = "DSA";
		KeyPairGenerator kg = KeyPairGenerator.getInstance(alg);
		KeyPair keyPair = kg.genKeyPair();
		
		byte[] signature = performSigning("test", alg, keyPair);
		performVerification("test", alg, signature, keyPair.getPublic());
	}
	
	static byte[] performSigning(String s, String alg, KeyPair keyPair) throws Exception {
		Signature sign = Signature.getInstance(alg);
		PrivateKey privateKey = keyPair.getPrivate();
		sign.initSign(privateKey);
		sign.update(s.getBytes());
		return sign.sign();
	}
	
	static void performVerification(String s, String alg, byte[] signature, PublicKey publicKey)
	throws Exception {
		Signature sign = Signature.getInstance(alg);
		sign.initVerify(publicKey);
		sign.update(s.getBytes());
		System.out.println(sign.verify(signature));
	}	
	
	
	public static void main2() throws Exception {
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance(Sign.algorithm);
		generator.initialize(1024);
		KeyPair pair = generator.generateKeyPair();
		
		DSAPrivateKey privateKey = (DSAPrivateKey)pair.getPrivate();
		DSAPublicKey publicKey = (DSAPublicKey)pair.getPublic();
		
		BigInteger p = privateKey.getParams().getP();
		BigInteger q = privateKey.getParams().getQ();
		BigInteger g = privateKey.getParams().getG();
		
		BigInteger x = privateKey.getX();
		BigInteger y = publicKey.getY();
		
		PublicKey publicKey1 = KeyFactory.getInstance(Sign.algorithm).generatePublic(new DSAPublicKeySpec(y, p, q, g));
		PrivateKey privateKey1 = KeyFactory.getInstance(Sign.algorithm).generatePrivate(new DSAPrivateKeySpec(x, p, q, g));
		
		byte[] buffer = new byte[1024];
		
		Signature sig = Signature.getInstance(Sign.algorithm);
		sig.initSign(privateKey1);
		sig.update(buffer, 0, buffer.length);
		
		byte[] signature = sig.sign();
		
		sig = Signature.getInstance(publicKey1.getAlgorithm());
		sig.initVerify(publicKey1);
		sig.update(buffer, 0, buffer.length);
		boolean validity = sig.verify(signature);
		
		Assert.assertTrue(validity);
		
	}
	
	
}
