package base.encrypt.secret;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import base.data.Bin;
import base.data.Data;
import base.data.Outline;
import base.exception.DataException;
import base.exception.PlatformException;
import base.size.move.Move;
import base.time.Now;

public class Secret {

	// Define
	
	/** Advanced Encryption Standard, the kind of symmetric shared secret encryption we use. */
	public static final String algorithm = "AES";
	/** 128 bit key size, ships with Java, strong enough for US classified Secret, and http://bit.ly/J3VBt Bruce Schneier recommends it over 256. */
	public static final int size = 128;
	
	// Key

	/** Make a new symmetric shared secret key for encrypting and decrypting data. */
	public static KeySecret make() {
		return parse(key());
	}
	
	/** Parse the given data into a symmetric shared secret key for encrypting and decrypting data. */
	public static KeySecret parse(Data key) {
		Cipher encrypt = cipher(key, Cipher.ENCRYPT_MODE);
		Cipher decrypt = cipher(key, Cipher.DECRYPT_MODE);
		return new KeySecret(key, encrypt, decrypt);
	}

	/** Make a new key for encrypting and decrypting data. */
	private static Data key() {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(algorithm); // This blocks for a moment the first time the program calls it
			generator.init(size);
			SecretKey key = generator.generateKey();
			return new Data(key.getEncoded());
		}
		catch (NoSuchAlgorithmException e)  { throw new PlatformException(e); } // Java doesn't have algorithm
		catch (InvalidParameterException e) { throw new PlatformException(e); } // Java doesn't allow strength
	}
	
	/** Turn the data of a key into a Cipher to Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE. */
	private static Cipher cipher(Data key, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(mode, new SecretKeySpec(key.toByteArray(), algorithm));
			return cipher;
		}
		catch (NoSuchAlgorithmException e) { throw new PlatformException(e); } // Java doesn't have algorithm
		catch (NoSuchPaddingException e)   { throw new PlatformException(e); } // Java doesn't have padding
		catch (InvalidKeyException e)      { throw new DataException(e); }     // Unable to turn the key data into a working key
	}

	// Data

	/** Throw a 2 block, 32 byte, Outline of padding in at the end to force data before through. */
	public static Outline padding(Cipher cipher) {
		final String name = "padding";
		int block = cipher.getBlockSize();
		int carry = (new Outline(name)).toData().size();
		return new Outline(name, Data.random((2 * block) - carry)); // 2 blocks will both get encrypted, and 1 decrypted on the far side
	}

	/** The part of i that is whole multiples of block, any remainder removed. */
	private static int whole(int block, int i) { return i - (i % block); }

	/** true if source has enough data and destination has enough space to encrypt or decrypt. */
	public static boolean can(Cipher cipher, int mode, Bin source, Bin destination) {
		try {
			expect(cipher, mode, source, destination);
			return true;
		} catch (IndexOutOfBoundsException e) { return false; }
	}

	/** How many bytes we expect the next call to encrypt or decrypt, 1 or more, or IndexOutOfBoundsException. */
	private static int expect(Cipher cipher, int mode, Bin source, Bin destination) {
		int block = cipher.getBlockSize();
		
		int s = whole(block, source.size());
		if (mode == Cipher.DECRYPT_MODE) s -= block; // Decrypt will do one less block than we ask it to
		int d = whole(block, destination.space()) - block; // Both encrypt and decrypt need an extra block of destination space
		
		int ask = Math.min(s, d);
		if (ask < 1) throw new IndexOutOfBoundsException();
		return ask;
	}

	/** Encrypt or decrypt 1 or more 16 byte blocks of data, decrypt needs 32 bytes in source because it always leaves a block behind. */
	public static Move process(Cipher cipher, int mode, Bin source, Bin destination) {
		try {
			int block = cipher.getBlockSize();
			int expect = expect(cipher, mode, source, destination);

			int ask = expect;
			if (mode == Cipher.DECRYPT_MODE) ask += block; // Ask for an extra block because decrypt always does one less
			
			ByteBuffer data = source.out(ask);
			ByteBuffer space = destination.in(expect + block); // Both encrypt and decrypt need an extra block of destination space
			
			Now start = new Now();
			int did = cipher.update(data, space);
			if (did != expect) throw new IndexOutOfBoundsException("did");
			if (mode == Cipher.DECRYPT_MODE) data.position(data.position() - block); // Decrypt says it does the last block it leaves behind
			
			source.outCheck(did, data);
			destination.inCheck(did, space);
			source.outDone(data);
			destination.inDone(space);
			return new Move(start, did);
		}
		catch (ShortBufferException e)      { throw new DataException(e); } // If this happens, ask() is coded incorrectly
		catch (IndexOutOfBoundsException e) { throw new DataException(e); }
	}
}
