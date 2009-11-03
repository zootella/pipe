package org.zootella.encrypt.sign;

import org.junit.Assert;
import org.junit.Test;
import org.zootella.data.Data;

public class SignTest {
	
	@Test public void test() throws Exception {
		
		
		// try it for the first time:
		// make a key
		// sign something
		// validate the signature
		// corrupt the message
		// confirm validate returns false
		
		//TODO confirm if the message is corrupted, this returns false, it shouldn't throw an exception
		
		
		SignKeyData key = Sign.make();
		
		Data message = new Data("hello");
		Data signature = Sign.sign(message, key.privateKey);
		boolean valid = Sign.verify(message, signature, key.publicKey);
		
		Assert.assertTrue(valid);
		
		
	}
	
}
