package org.zootella.encrypt.hash;

import org.junit.Assert;
import org.junit.Test;
import org.zootella.data.Data;

public class HashTest {
	
	private static final String empty = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
	private static final String hello = "aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d";
	private static final String helloYou = "ece6d7af388b73e477fdeb2bda09530dd87aa8a3";
	
	@Test public void testEmpty() throws Exception {

		Assert.assertEquals("empty", empty, Hash.hash(Data.empty()).base16());
	}
	
	@Test public void testShort() throws Exception {
	
		Assert.assertEquals("hello", hello, Hash.hash(new Data("hello")).base16());
		Assert.assertEquals("hello you", helloYou, Hash.hash(new Data("hello you")).base16());
	}
	
	@Test public void testParts() throws Exception {
		
		Hash hash = new Hash();
		hash.add(new Data("hello"));
		hash.add(new Data(" "));
		hash.add(new Data("you"));
		Assert.assertEquals("hello you in 3 parts", helloYou, hash.done().base16());
	}
	
	@Test public void testEmptyMiddle() throws Exception {
		
		Hash hash = new Hash();
		hash.add(new Data("hello"));
		hash.add(new Data(" "));
		hash.add(Data.empty()); // This won't mess it up
		hash.add(new Data("you"));
		Assert.assertEquals("hello you in 3 parts", helloYou, hash.done().base16());
	}
}
