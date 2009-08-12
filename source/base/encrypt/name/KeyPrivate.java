package base.encrypt.name;

import java.security.PrivateKey;

import javax.crypto.Cipher;

/** Private key and cipher for decrypting data bundled together. */
public class KeyPrivate {

	public KeyPrivate(PrivateKey key, Cipher decrypt) {
		this.key = key;
		this.decrypt = decrypt;
	}
	
	public final PrivateKey key;
	public final Cipher decrypt;
}
