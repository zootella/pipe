package org.zootella.encrypt.pair;

import org.zootella.data.Data;

public class PairKeyData {
	
	public PairKeyData(Data modulus, Data publicExponent, Data privateExponent) {
		this.modulus = modulus;
		this.publicExponent = publicExponent;
		this.privateExponent = privateExponent;
	}
	
	public final Data modulus;
	public final Data publicExponent;
	public final Data privateExponent;
}
