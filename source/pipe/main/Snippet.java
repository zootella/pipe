package pipe.main;

import javax.crypto.Cipher;

import base.data.Bin;
import base.data.Data;
import base.data.encrypt.Encrypt;

public class Snippet {

	public static void snippet(Program program) {

		Data key = Encrypt.key();
		Cipher encrypt = Encrypt.cipher(key, Cipher.ENCRYPT_MODE);
		Cipher decrypt = Encrypt.cipher(key, Cipher.DECRYPT_MODE);

		Bin a = Bin.medium();
		Bin b = Bin.medium();
		Bin c = Bin.medium();
		
		a.add(new Data("h"));
		a.add(Encrypt.padding(encrypt).toData());
		
		run(encrypt, decrypt, a, b, c);
		
		

		
		
		
		
		
		

	}
	
	public static void run(Cipher encrypt, Cipher decrypt, Bin a, Bin b, Bin c) {
		System.out.println("start");
		say(a, b, c);
		System.out.println("");
		
		if (Encrypt.can(encrypt, Cipher.ENCRYPT_MODE, a, b)) {
			Encrypt.process(encrypt, Cipher.ENCRYPT_MODE, a, b);
			System.out.println("encrypted");
			say(a, b, c);
			System.out.println("");
			
			if (Encrypt.can(decrypt, Cipher.DECRYPT_MODE, b, c)) {
				Encrypt.process(decrypt, Cipher.DECRYPT_MODE, b, c);
				System.out.println("decrypted");
				say(a, b, c);
				System.out.println("");
				
				System.out.println(c.data().strike());
				System.out.println(c.data().base16());
				System.out.println("");
			}
		}
	}
	
	public static void say(Bin a, Bin b, Bin c) {
		System.out.println(a.size() + "a " + b.size() + "b " + c.size() + "c");
	}
}
