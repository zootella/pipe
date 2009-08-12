package base.encrypt.name;

import java.security.PublicKey;

import javax.crypto.Cipher;

/** Public key and cipher for encrypting data bundled together. */
public class KeyPublic {

	public KeyPublic(PublicKey key, Cipher encrypt) {
		this.key = key;
		this.encrypt = encrypt;
	}
	
	public final PublicKey key;
	public final Cipher encrypt;
}
