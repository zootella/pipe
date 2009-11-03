package org.zootella.encrypt.sign;

import org.zootella.data.Data;

public class SignKeyData {
	
	public SignKeyData(Data gBase, Data pPrime, Data qSub, Data xPrivate, Data yPublic) {
		this.gBase    = gBase;
		this.pPrime   = pPrime;
		this.qSub     = qSub;
		this.xPrivate = xPrivate;
		this.yPublic  = yPublic;
	}

	public final Data gBase;
	public final Data pPrime;
	public final Data qSub;
	public final Data xPrivate;
	public final Data yPublic;
}
