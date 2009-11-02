package org.zootella.encrypt.sign;

import org.junit.Assert;
import org.junit.Test;

public class SignTest {
	
	@Test public void test() throws Exception {
		
		
		// try it for the first time:
		// make a key
		// sign something
		// validate the signature
		// corrupt the message
		// confirm validate returns false
		
		SignKeyData key = Sign.make();
		
		
		

		round();
		round();
		round();
		round();
		round();
		
	}
	
	private static void round() {
		SignKeyData key = Sign.make();
		System.out.println(key.publicKey.size()  + " bytes: " + key.publicKey.base16());//443 or 444
		System.out.println(key.privateKey.size() + " bytes: " + key.privateKey.base16());//335
		System.out.println("");
		
	}
}
