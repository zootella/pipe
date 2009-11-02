package org.zootella.encrypt.sign;

import org.zootella.data.Data;

public class SignKeyData {
	
	public SignKeyData(Data publicKey, Data privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	public final Data publicKey;
	public final Data privateKey;
}
