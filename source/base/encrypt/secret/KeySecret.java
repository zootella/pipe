package base.encrypt.secret;

import javax.crypto.Cipher;

import base.data.Data;

/** Secret key and ciphers for encrypting and decrypting data bundeled together. */
public class KeySecret {
	
	public KeySecret(Data key, Cipher encrypt, Cipher decrypt) {
		this.key = key;
		this.encrypt = encrypt;
		this.decrypt = decrypt;
	}
	
	public final Data key;
	public final Cipher encrypt;
	public final Cipher decrypt;
}