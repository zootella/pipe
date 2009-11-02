package org.zootella.encrypt.pair;

import org.junit.Assert;
import org.junit.Test;
import org.zootella.data.Data;
import org.zootella.exception.DataException;

public class PairTest {
	
	@Test public void test() throws Exception {
		PairKeyData key = Pair.make();
		
		roundTrip(key, Data.empty()); // No data
		
		roundTrip(key, new Data("hello")); // Short messages
		roundTrip(key, new Data("hello you"));
		
		roundTrip(key, Data.random(Pair.messageSize)); // Maximum message size
		
		try {
			roundTrip(key, Data.random(Pair.messageSize + 1)); // Message too big
			Assert.fail();
		} catch (DataException e) {}
	}
	
	private static void roundTrip(PairKeyData key, Data a) {
		Data b = Pair.encrypt(a, key.modulus, key.publicExponent);
		Data c = Pair.decrypt(b, key.modulus, key.privateExponent);
		Assert.assertEquals("before and after", a, c);
	}
}
