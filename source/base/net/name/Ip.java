package base.net.name;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import base.data.Bay;
import base.data.Data;
import base.data.Number;
import base.data.Text;
import base.exception.DataException;
import base.exception.PlatformException;

public class Ip implements Comparable<Ip> {
	
	// Look
	
	/** The 1st number in the IP address, n1.0.0.0. */
	public final int n1;
	/** The 2nd number in the IP address, 0.n2.0.0. */
	public final int n2;
	/** The 3rd number in the IP address, 0.0.n3.0. */
	public final int n3;
	/** The 4th number in the IP address, 0.0.0.n4. */
	public final int n4;
	
	// Convert

	/** Convert this Ip into a Java InetAddress object. */
	public InetAddress toInetAddress() {
		try {
			byte[] b = new byte[4]; // Convert our IP address into a byte[] to give to Java
			b[0] = (byte)n1;
			b[1] = (byte)n2;
			b[2] = (byte)n3;
			b[3] = (byte)n4;
			return InetAddress.getByAddress(b); // Java promises to not do any network activity
		} catch (UnknownHostException e) { throw new PlatformException(e); } // The array wasn't 4 bytes long
	}
	
	/** Make a new Ip with the IP address of the given Java InetAddress object. */
	public Ip(InetAddress a) {
		byte[] b = a.getAddress();
		n1 = b[0] & 0xff; // The & 0xff is necessary because we're casting a byte into an int, so a high 1 will get smeared
		n2 = b[1] & 0xff;
		n3 = b[2] & 0xff;
		n4 = b[3] & 0xff;
	}

	// Text
	
	/** Convert this Ip into text like "1.2.3.4". */
	public String toString() { return n1 + "." + n2 + "." + n3 + "." + n4; }

	/** Make a new Ip from a String like "1.2.3.4". */
	public Ip(String s) {
		List<String> list = Text.words(s, ".");
		if (list.size() != 4) throw new DataException(); // Make sure we got 4 parts
		n1 = Number.toInt(list.get(0), 0, 255); // Throws DataException if the text isn't numerals, or if a number isn't 0 through 255
		n2 = Number.toInt(list.get(1), 0, 255);
		n3 = Number.toInt(list.get(2), 0, 255);
		n4 = Number.toInt(list.get(3), 0, 255);
	}

	// Data

	/**
	 * "1234", the default pattern that describes how the IP address 1.2.3.4 is arranged in 4 bytes of data.
	 * There are 2 patterns you can use:
	 * 
	 * "1234" big endian, which is also called network byte order, this default
	 * "4321" little endian
	 */
	public static final String pattern = "1234";
	
	/** Convert this Ip into 4 bytes of data, 1.2.3.4 becomes 01 02 03 04. */
	public Data data() { return data(pattern); } // Use the default pattern
	/** Convert this Ip into 4 bytes of data using the pattern "1234" or "4321". */
	public Data data(String pattern) { Bay bay = new Bay(); toBay(bay, pattern); return bay.data(); }
	
	/** Convert this Ip into 4 bytes added to bay, 1.2.3.4 becomes 01 02 03 04. */
	public void toBay(Bay bay) { toBay(bay, pattern); }
	/** Convert this Ip into 4 bytes added to bay using the pattern "1234" or "4321". */
	public void toBay(Bay bay, String pattern) {
		if (pattern.charAt(0) == '1') { // pattern is "1234", use big endian network byte order
			Number.toBay(bay, 1, n1);   // Add each number in a single byte
			Number.toBay(bay, 1, n2);
			Number.toBay(bay, 1, n3);
			Number.toBay(bay, 1, n4);
		} else {                        // pattern is "4321", use little endian order
			Number.toBay(bay, 1, n4);
			Number.toBay(bay, 1, n3);
			Number.toBay(bay, 1, n2);
			Number.toBay(bay, 1, n1);
		}
	}

	/** Make a new Ip from d which must be 4 bytes, 01 02 03 04 becomes 1.2.3.4. */
	public Ip(Data d) { this(d, pattern); } // Use the default pattern
	/** Make a new Ip from d which must be 4 bytes, use the pattern "1234" or "4321". */
	public Ip(Data d, String pattern) {
		if (d.size() != 4) throw new DataException("size");
		if (pattern.charAt(0) == '1') {              // pattern is "1234", use big endian network byte order
			n1 = Number.toInt(d.clip(0, 1), 0, 255); // Turn each byte into a number
			n2 = Number.toInt(d.clip(1, 1), 0, 255);
			n3 = Number.toInt(d.clip(2, 1), 0, 255);
			n4 = Number.toInt(d.clip(3, 1), 0, 255);
		} else {                                     // pattern is "4321", use little endian order
			n4 = Number.toInt(d.clip(0, 1), 0, 255);
			n3 = Number.toInt(d.clip(1, 1), 0, 255);
			n2 = Number.toInt(d.clip(2, 1), 0, 255);
			n1 = Number.toInt(d.clip(3, 1), 0, 255);
		}
	}

	// Compare

	/**
	 * Compare this Ip to a given one to determine which should come first in sorted order.
	 * @return Negative to sort this first, positive if o is first, 0 if they're the same
	 */
	public int compareTo(Ip o) {
		if      (n1 != o.n1) return n1 - o.n1; // Compare each pair of numbers to return the first mismatch
		else if (n2 != o.n2) return n2 - o.n2;
		else if (n3 != o.n3) return n3 - o.n3;
		else                 return n4 - o.n4;
	}

	/** true if the given Ip is the same as this one. */
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Ip)) return false;
		return n1 == ((Ip)o).n1 && n2 == ((Ip)o).n2 && n3 == ((Ip)o).n3 && n4 == ((Ip)o).n4;
	}
}
