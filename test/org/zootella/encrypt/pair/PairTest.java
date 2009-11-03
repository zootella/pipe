package org.zootella.encrypt.pair;

import org.junit.Assert;
import org.junit.Test;
import org.zootella.data.Data;
import org.zootella.exception.DataException;

public class PairTest {
	
	@Test public void test() throws Exception {
		PairKey key = Pair.make();
		
		roundTrip(key, Data.empty()); // No data
		
		roundTrip(key, new Data("hello")); // Short messages
		roundTrip(key, new Data("hello you"));
		
		roundTrip(key, Data.random(Pair.messageSize)); // Maximum message size
		
		try {
			roundTrip(key, Data.random(Pair.messageSize + 1)); // Message too big
			Assert.fail();
		} catch (DataException e) {}
	}
	
	private static void roundTrip(PairKey key, Data a) {
		Data b = Pair.encrypt(a, key);
		Data c = Pair.decrypt(b, key);
		Assert.assertEquals("before and after", a, c);
	}
}
