package org.zootella.encode;

import org.junit.Assert;

import org.junit.Test;
import org.zootella.data.Data;
import org.zootella.data.Encode;

public class EncodeTest {
	
	@Test public void test() throws Exception {
		
		test("");
		test("a");
		test("ab");
		test("[");
		test("]");
		test("\0");
		test("\t");
		test("a[b]c\r\n");
		test("[hello]\0\t\r\n");

		test(Data.random(16));
	}

	public void test(String a) throws Exception {
			
		String b = Encode.box(new Data(a));
		String c = Encode.unbox(b).toString();
		Assert.assertTrue(a.equals(c));
	}

	public void test(Data a) throws Exception {
			
		String b = Encode.box(a);
		Data c = Encode.unbox(b);
		Assert.assertTrue(a.equals(c));
	}
}
