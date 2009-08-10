package base.data.encrypt;

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

public class Encrypt {

	// Define
	
	/** Advanced Encryption Standard. */
	public static final String algorithm = "AES";
	/** 128 bit key size, ships with Java, strong enough for US classified Secret, and http://bit.ly/J3VBt Bruce Schneier recommends it over 256. */
	public static final int strength = 128;
	
	// Key

	/** Make a new key for encrypting and decrypting data. */
	public static Data key() {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(algorithm); // This blocks for a moment the first time the program calls it
			generator.init(strength);
			SecretKey key = generator.generateKey();
			return new Data(key.getEncoded());
		}
		catch (NoSuchAlgorithmException e)  { throw new PlatformException(e); } // Java doesn't have algorithm
		catch (InvalidParameterException e) { throw new PlatformException(e); } // Java doesn't allow strength
	}
	
	/** Turn the data of a key into a Cipher to Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE. */
	public static Cipher cipher(Data key, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(mode, new SecretKeySpec(key.toByteArray(), algorithm));
			return cipher;
		}
		catch (NoSuchAlgorithmException e)  { throw new PlatformException(e); } // Java doesn't have algorithm
		catch (NoSuchPaddingException e)    { throw new PlatformException(e); } // Java doesn't have padding
		catch (InvalidKeyException e)       { throw new DataException(e); }     // Unable to turn the key data into a working key
	}

	// Padding

	/** Throw a 2 block, 32 byte, Outline of padding in at the end to force data before through. */
	public static Outline padding(Cipher cipher) {
		final String name = "padding";
		int block = cipher.getBlockSize();
		int carry = (new Outline(name)).toData().size();
		return new Outline(name, Data.random((2 * block) - carry)); // 2 blocks will both get encrypted, and 1 decrypted on the far side
	}
	
	// Help
	
	/** The part of i that is whole multiples of block, any remainder removed. */
	private static int whole(int block, int i) { return i - (i % block); }
	
	// Can

	/** true if source has enough data and destination has enough space to encrypt. */
	public static boolean canEncrypt(Cipher cipher, Bin source, Bin destination) {
		try {
			askEncrypt(cipher, source, destination);
			return true;
		} catch (IndexOutOfBoundsException e) { return false; }
	}

	/** true if source has enough data and destination has enough space to decrypt. */
	public static boolean canDecrypt(Cipher cipher, Bin source, Bin destination) {
		try {
			askDecrypt(cipher, Cipher.ENCRYPT_MODE, source, destination);
			return true;
		} catch (IndexOutOfBoundsException e) { return false; }
	}
	
	// Encrypt

	/** How many bytes we expect the next call to encrypt, 1 or more, or IndexOutOfBoundsException. */
	private static int askEncrypt(Cipher cipher, Bin source, Bin destination) {
		int block = cipher.getBlockSize();
		
		int s = whole(block, source.size());
		int d = whole(block, destination.space()) - block; // Encrypt needs an extra block of space
		
		int ask = Math.min(s, d);
		if (ask < 1) throw new IndexOutOfBoundsException();
		
		return ask;
	}

	/** Encrypt 1 or more 16 byte block of source data into destination. */
	public static Move encrypt(Cipher cipher, Bin source, Bin destination) {
		try {
			int ask = askEncrypt(cipher, source, destination);
			
			ByteBuffer data = source.out(ask);
			ByteBuffer space = destination.in(ask);
			
			Now start = new Now();
			int did = cipher.update(data, space);
			if (did != ask) throw new IndexOutOfBoundsException("did"); // Make sure it did what we asked
			
			source.outCheck(did, data);
			destination.inCheck(did, space);
			source.outDone(data);
			destination.inDone(space);
			return new Move(start, did);
		}
		catch (ShortBufferException e)      { throw new DataException(e); } // If this happens, askEncrypt() is broken
		catch (IndexOutOfBoundsException e) { throw new DataException(e); }
	}
	
	// Decrypt

	/** How many bytes we expect the next call to decrypt, 1 or more, or IndexOutOfBoundsException. */
	private static int askDecrypt(Cipher cipher, int mode, Bin source, Bin destination) {
		int block = cipher.getBlockSize();
		
		int s = whole(block, source.size());
		
		if (mode == Cipher.DECRYPT_MODE) s -= block; // Decrypt will do one less block than we ask it to
		
		int d = whole(block, destination.space()) - block; // Decrypt needs an extra block of space
		
		int ask = Math.min(s, d);
		if (ask < 1) throw new IndexOutOfBoundsException();
		
		return ask;
	}
	
	/** Decrypt 1 or more 16 byte blocks of data, source needs at least 32 bytes because decrypt() always leaves the last block behind. */
	public static Move decrypt(Cipher cipher, int mode, Bin source, Bin destination) {
		try {
			int ask = askDecrypt(cipher, Cipher.DECRYPT_MODE, source, destination);

			int request = ask;
			if (mode == Cipher.DECRYPT_MODE) request += cipher.getBlockSize(); // Ask for an extra block because decrypt always does one less
			
			ByteBuffer data = source.out(request);
			ByteBuffer space = destination.in(ask);
			
			Now start = new Now();
			int did = cipher.update(data, space);
			if (did != ask) throw new IndexOutOfBoundsException("did");
			
			source.outCheck(did, data);
			destination.inCheck(did, space);
			source.outDone(data);
			destination.inDone(space);
			return new Move(start, did);
		}
		catch (ShortBufferException e)      { throw new DataException(e); } // If this happens, askEncrypt() is broken
		catch (IndexOutOfBoundsException e) { throw new DataException(e); }
	}
}
