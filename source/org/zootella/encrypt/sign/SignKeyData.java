package org.zootella.encrypt.sign;

import org.zootella.data.Data;

public class SignKeyData {
	
	public SignKeyData(Data gBase, Data pPrime, Data qSub, Data yPublic, Data xPrivate) {
		this.gBase    = gBase;
		this.pPrime   = pPrime;
		this.qSub     = qSub;
		this.yPublic  = yPublic;
		this.xPrivate = xPrivate;
	}

	public final Data gBase;
	public final Data pPrime;
	public final Data qSub;
	public final Data yPublic;
	public final Data xPrivate;
}
