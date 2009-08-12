package base.data.encrypt;

import static org.junit.Assert.*;

import javax.crypto.Cipher;

import org.junit.Test;

import base.data.Bin;
import base.data.Data;
import base.encrypt.pair.KeyData;
import base.encrypt.pair.Pair;
import base.encrypt.secret.KeySecret;
import base.encrypt.secret.Secret;

public class EncryptTest {
	
	// pair

	@Test public void pair() {
		KeyData key = Pair.make();
		Data a = new Data("this is my short and very secret message");
		Data b = Pair.encrypt(a, key.modulus, key.publicExponent);
		Data c = Pair.decrypt(b, key.modulus, key.privateExponent);
		System.out.println(c.strike());
		assertEquals(a, c);
	}

	// secret

	@Test public void secret() {
		
		run(0);
		run(1);
		
		run(15);
		run(16);
		run(17);
		
		run(31);
		run(32);
		run(33);
		
		run(47);
		run(48);
		run(49);
		
		run(63);
		run(64);
		run(65);

		run(8159);
		run(8160);
		run(8161);

		run(8175);
		run(8176);
		run(8177);

		run(8191);
		run(8192);
	}
	
	public static void run(int size) {

		KeySecret key = Secret.make();
		
		Bin a = Bin.medium();
		Bin b = Bin.medium();
		Bin c = Bin.medium();
		
		a.add(Data.random(size));
//		a.add(Encrypt.padding(encrypt).toData());

		say(a, b, c);
		
		if (Secret.can(key.encrypt, Cipher.ENCRYPT_MODE, a, b)) {
			Secret.process(key.encrypt, Cipher.ENCRYPT_MODE, a, b);
			System.out.println("encrypted");
			say(a, b, c);
			
			if (Secret.can(key.decrypt, Cipher.DECRYPT_MODE, b, c)) {
				Secret.process(key.decrypt, Cipher.DECRYPT_MODE, b, c);
				System.out.println("decrypted");
				say(a, b, c);
				
				/*
				System.out.println(c.data().strike());
				System.out.println(c.data().base16());
				System.out.println("");
				*/
			}
		}
		System.out.println("");
	}
	
	public static void say(Bin a, Bin b, Bin c) {
		System.out.println(a.size() + "a " + b.size() + "b " + c.size() + "c");
	}
	
	
}
