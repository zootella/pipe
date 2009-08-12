package base.encrypt.pair;

import base.data.Data;

public class KeyData {
	
	public KeyData(Data modulus, Data publicExponent, Data privateExponent) {
		this.modulus = modulus;
		this.publicExponent = publicExponent;
		this.privateExponent = privateExponent;
	}
	
	public final Data modulus;
	public final Data publicExponent;
	public final Data privateExponent;
}
