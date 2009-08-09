package base.data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import base.exception.DiskException;
import base.exception.NetException;
import base.file.File;
import base.net.name.IpPort;
import base.net.packet.ListenPacket;
import base.net.socket.Socket;
import base.size.Range;
import base.size.Size;
import base.size.Stripe;
import base.size.StripePattern;
import base.size.move.Move;
import base.size.move.PacketMove;
import base.size.move.StripeMove;
import base.time.Ago;
import base.time.Now;
import base.time.Stopwatch;

public class Bin {
	
	// Define
	
	/** 8 KB in bytes, the capacity of a normal Bin, our buffer size for TCP sockets. */
	public static final int medium = 8 * (int)Size.kilobyte;
	/** 64 KB in bytes, the capacity of a big Bin, our buffer size for UDP packets. */
	public static final int big = 64 * (int)Size.kilobyte;

	// Make
	
	/** Get a new empty 8 KB Bin. */
	public static Bin medium() {
		return new Bin(medium);
	}
	
	/** Get a new empty 64 KB Bin. */
	public static Bin big() {
		return new Bin(big);
	}

	/** Make a new Bin with the given ByteBuffer inside. */
	private Bin(int size) {
		this.buffer = ByteBuffer.allocateDirect(size);
	}
	
	// Look

	/** Look at the Data in this Bin. */
	public Data data() {
		ByteBuffer b = buffer.asReadOnlyBuffer(); // This doesn't copy the data
		b.flip(); // Clip b's position and limit around our data
		return new Data(b); // Make it into a new Data object
	}

	/** The number of bytes of data in this Bin, 0 if empty. */
	public int size() {
		return buffer.position();
	}
	
	/** The total number of bytes this Bin is capable of holding. */
	public int capacity() {
		return buffer.capacity();
	}

	/** The amount of free space in this Bin, 0 if totally full. */
	public int space() {
		return capacity() - size();
	}
	
	/** true if this Bin has at least 1 byte of data. */
	public boolean hasData() { return size() != 0; }
	/** true if this Bin has no data, not even 1 byte. */
	public boolean isEmpty() { return size() == 0; }
	/** true if this Bin has at least 1 byte of space. */
	public boolean hasSpace() { return size() != capacity(); }
	/** true if this Bin is completely full of data, with no space for even 1 more byte. */
	public boolean isFull() { return size() == capacity(); }
	
	// Change

	/** Move data from source to destination, do nothing if either are null. */
	public static void move(Bin source, Bin destination) {
		if (source == null || destination == null) return;
		destination.add(source); // Move data from the source Bin to the destination Bin
	}

	/** Move as much data as fits from bin to this one. */
	public void add(Bin bin) {
		if (bin.isEmpty() || isFull()) { // Nothing given or no space here
		} else if (isEmpty() && capacity() == bin.capacity()) { // We're empty and have the same capacity
			ByteBuffer b = bin.buffer; // Switch buffers
			bin.buffer = buffer;
			buffer = b;
		} else { // Move some data in
			Data data = bin.data();
			add(data);
			bin.keep(data.size()); // Have bin keep only what add didn't take
		}
	}

	/** Move as much data as fits from bay to this Bin, removing what we take from bay. */
	public void add(Bay bay) {
		Data data = bay.data();
		add(data);
		bay.keep(data.size()); // Have bay keep only what add didn't take
	}

	/** Move as much data as fits from data to this Bin, removing what we take from data. */
	public void add(Data data) {
		if (data.isEmpty() || isFull()) return;   // Nothing given or no space here
		int did = Math.min(data.size(), space()); // Figure out how many bytes we can move
		Data d = data.start(did);                 // Clip d around that size
		buffer.put(d.toByteBuffer());             // Copy in the data
		data.remove(did);                         // Remove what we took from the given Data object
	}

	/** Remove size bytes from the start of the data in this Bin. */
	public void remove(int size) {
		if (size < 0 || size > size()) throw new IndexOutOfBoundsException(); // Can't be negative or more data than we have
		if (size == 0) return; // Nothing to remove
		buffer.limit(buffer.position()); // Clip around the data we're going to keep
		buffer.position(size);
		buffer.compact(); // Slide that data to the start and clip position and limit around the space at the end
	}
	
	/** Remove data from the start of this Bin, keeping only the last size bytes. */
	public void keep(int size) {
		remove(size() - size); // Remove everything but size bytes
	}

	/** Remove all the data this Bin is holding, leaving it empty. */
	public void clear() {
		buffer.position(0); // Move position back to the start to clip out the whole thing as empty
	}

	// Hold

	/**
	 * Our ByteBuffer object that holds the data.
	 * The position and limit always clip around the empty space at the end of the buffer.
	 * The data always starts at the start.
	 */
	private ByteBuffer buffer;

	// Help

	/** Copy our buffer clipped around space bytes of space for moving data in. */
	private ByteBuffer in(int space) {
		if (space > space()) throw new IndexOutOfBoundsException();
		ByteBuffer b = buffer.duplicate();
		b.limit(b.position() + space);
		return b;
	}
	
	/** Make sure we did at least 1 byte and position moved forward correctly. */
	private void inCheck(int did, ByteBuffer space) throws IOException {
		if (did < 1) throw new IOException("did " + did); // Hit the end got nothing
		if (buffer.position() + did != space.position()) throw new IOException("position"); // Moved position forward incorrectly
	}
	
	/** Save our buffer after moving data in. */
	private void inDone(ByteBuffer space) {
		space.limit(space.capacity());
		buffer = space;
	}

	/** Copy our buffer clipped around size bytes of data at the start for moving data out. */
	private ByteBuffer out(int size) {
		if (size > size()) throw new IndexOutOfBoundsException();
		ByteBuffer b = buffer.duplicate();
		b.position(0);
		b.limit(size);
		return b;
	}
	
	/** Make sure we did at least 1 byte and position moved forward correctly. */
	private void outCheck(int did, ByteBuffer data) throws IOException {
		if (did < 1) throw new IOException("did " + did); // Error or wrote nothing
		if (did != data.position()) throw new IOException("position"); // Moved position forward incorrectly
	}
	
	/** Save our buffer after moving data out. */
	private void outDone(ByteBuffer data) {
		data.limit(buffer.position());
		data.compact(); // Slide the data to the start and clip position and limit around the space after it
		buffer = data;
	}

	// File
	
	/** Read 1 byte or more from file to this Bin. */
	public StripeMove read(File file, StripePattern pattern, Range range) {
		try {
			int ask = range.ask(space()); // Don't try to read more bytes than we have space for
			ByteBuffer space = in(ask);
			if (!pattern.is(true, new Stripe(range.i, ask))) throw new DiskException("hole"); // Make sure the file has data where we will read it
			Now start = new Now();
			int did = file.file.getChannel().read(space, range.i); // Read from the file at i and move space.position forward
			inCheck(did, space);
			inDone(space);
			return new StripeMove(start, range.i, did);
		} catch (IOException e) { throw new DiskException(e); }
	}

	/** Write 1 byte or more from this Bin to file. */
	public StripeMove write(File file, Range range) {
		try {
			ByteBuffer data = out(range.ask(size())); // Don't try to write more bytes than we have
			Now start = new Now();
			int did = file.file.getChannel().write(data, range.i); // Write to the file at trip.at and move data.position forward
			outCheck(did, data);
			outDone(data);
			return new StripeMove(start, range.i, did);
		} catch (IOException e) { throw new DiskException(e); }
	}

	// Socket
	
	/** Download 1 byte or more from socket, adding it to this Bin. */
	public Move download(Socket socket, Range range) {
		try {
			ByteBuffer space = in(range.ask(space()));
			Now start = new Now();
			int did = socket.channel.read(space); // Download data and move position forward
			inCheck(did, space);
			inDone(space);
			return new Move(start, did);
		} catch (IOException e) { throw new NetException(e); }
	}

	/** Upload 1 byte or more from this Bin into socket. */
	public Move upload(Socket socket, Range range) {
		try {
			ByteBuffer data = out(range.ask(size()));
			Now start = new Now();
			int did = socket.channel.write(data); // Upload data and move position forward
			outCheck(did, data);
			outDone(data);
			return new Move(start, did);
		} catch (IOException e) { throw new NetException(e); }
	}

	// Packet
	
	/** Receive the data of a single UDP packet from listen, 0 or more bytes, putting it in this empty Bin. */
	public PacketMove receive(ListenPacket listen) {
		try {
			ByteBuffer space = in(capacity()); // Make sure we start out empty
			Now start = new Now();
			InetSocketAddress a = (InetSocketAddress)listen.channel.receive(space); // Receive a packet and move position forward
			if (a == null) throw new NetException("null");
			inDone(space);
			return new PacketMove(start, size(), new IpPort(a));
		} catch (IOException e) { throw new NetException(e); }
	}
	
	/** Use listen to send the data in this Bin, 0 or more bytes, as a UDP packet to p. */
	public PacketMove send(ListenPacket listen, IpPort p) {
		try {
			ByteBuffer data = out(size()); // We might send a UDP packet with no data payload
			Now start = new Now();
			int did = listen.channel.send(data, p.toInetSocketAddress()); // Send a packet and move position forward
			if (did != size())         throw new NetException("behind");
			if (data.remaining() != 0) throw new NetException("position");
			outDone(data);
			return new PacketMove(start, did, p);
		} catch (IOException e) { throw new NetException(e); }
	}
	
	// Encrypt
	
	/** Throw a padding Outline into the stream at the end to force everything before through. */
	public static Outline padding(Cipher cipher) {
		final String name = "padding";
		int block = cipher.getBlockSize();
		int carry = (new Outline(name)).toData().size();
		return new Outline(name, Data.random((2 * block) - carry)); // 2 blocks will both get encrypted, and 1 decrypted on the far side
	}
	
	/** The part of i that is whole multiples of block, any remainder removed. */
	private static int whole(int block, int i) { return i - (i % block); }

	/** true if source has enough data and destination has enough space to encrypt. */
	public static boolean canEncrypt(Cipher cipher, Bin source, Bin destination) {
		try { askEncrypt(cipher, source, destination); return true; }
		catch (IndexOutOfBoundsException e) { return false; }
	}

	/** true if source has enough data and destination has enough space to decrypt. */
	public static boolean canDecrypt(Cipher cipher, Bin source, Bin destination) {
		try { askDecrypt(cipher, source, destination); return true; }
		catch (IndexOutOfBoundsException e) { return false; }
	}

	/** How many bytes we expect the next call to encrypt, 1 or more, or IndexOutOfBoundsException. */
	private static int askEncrypt(Cipher cipher, Bin source, Bin destination) {
		int block = cipher.getBlockSize();
		int s = whole(block, source.size());
		int d = whole(block, destination.space()) - block; // Encrypt needs an extra block of space
		int ask = Math.min(s, d);
		if (ask < 1) throw new IndexOutOfBoundsException();
		return ask;
	}
	
	/** Encrypt 16 bytes or more blocks of source data into destination. */
	public static Move encrypt(Cipher cipher, Bin source, Bin destination) {
		try {
			int ask = askEncrypt(cipher, source, destination);
			ByteBuffer s = source.buffer.duplicate();
			s.position(0); // Clip s position and limit around the source data we ask to encrypt
			s.limit(ask);
			Now start = new Now();
			int did = cipher.update(s, destination.buffer); // Moves destination position forward
			if (did != ask) throw new IndexOutOfBoundsException("did");
			source.remove(did); // Remove what we encrypted from the given source Bin
			return new Move(start, did);
		} catch (ShortBufferException e) { throw new IndexOutOfBoundsException(e.toString()); } // If this happens, askEncrypt() is broken
	}

	/** How many bytes we expect the next call to decrypt, 1 or more, or IndexOutOfBoundsException. */
	private static int askDecrypt(Cipher cipher, Bin source, Bin destination) {
		int block = cipher.getBlockSize();
		int s = whole(block, source.size()) - block; // Decrypt will do one less block than we ask it to
		int d = whole(block, destination.space()) - block; // Decrypt needs an extra block of space
		int ask = Math.min(s, d);
		if (ask < 1) throw new IndexOutOfBoundsException();
		return ask;
	}

	/** Decrypt 32 bytes or more of source data into destination, always leaves the last 16 byte block behind. */
	public static Move decrypt(Cipher cipher, Bin source, Bin destination) {
		try {
			int ask = askDecrypt(cipher, source, destination);
			ByteBuffer s = source.buffer.duplicate();
			s.position(0);
			s.limit(ask + cipher.getBlockSize()); // Ask for an extra block, decrypt will do one less
			Now start = new Now();
			int did = cipher.update(s, destination.buffer);
			if (did != ask) throw new IndexOutOfBoundsException("did");
			source.remove(did);
			return new Move(start, did);
		} catch (ShortBufferException e) { throw new IndexOutOfBoundsException(e.toString()); }
	}
	

	public static void snippet() throws Exception {
		

		test(0);
		
		
		
	}
	
	
	public static void test(int z) throws Exception {

		final String algorithm = "AES";
		final int size = 128;

		Stopwatch stopwatch = new Stopwatch();
		KeyGenerator g = KeyGenerator.getInstance(algorithm);
		g.init(size);
		SecretKey k = g.generateKey();
		Data d = new Data(k.getEncoded());
		stopwatch.stop();
		System.out.println("made key in " + stopwatch.toString());

		Cipher encrypt = Cipher.getInstance(algorithm);
		Cipher decrypt = Cipher.getInstance(algorithm);
		encrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(d.toByteArray(), algorithm));
		decrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(d.toByteArray(), algorithm));

		Bin a = Bin.medium();
		Bin b = Bin.medium();
		Bin c = Bin.medium();

		a.add(new Data("[^^all my secret information is safe now thanks to this awesome computer program and the good people who made it^^]"));
		a.add(padding(encrypt).toData());
		System.out.println("start     " + a.size() + "a " + b.size() + "b " + c.size() + "c");
		
		if (canEncrypt(encrypt, a, b)) {
			encrypt(encrypt, a, b);
			System.out.println("encrypted " + a.size() + "a " + b.size() + "b " + c.size() + "c");
			System.out.println(b.data().strike());
			System.out.println(b.data().base16());
			
			if (canDecrypt(decrypt, b, c)) {
				decrypt(decrypt, b, c);
				System.out.println("decrypted " + a.size() + "a " + b.size() + "b " + c.size() + "c");
				System.out.println(c.data().strike());
				System.out.println(c.data().base16());
			}
		}
		
		System.out.println("");
		
		
		
		// all you have to do is feed it different amounts of data, and show that it never chokes
		// encrypt must be able to take 1 byte or more, and must always generate 16 bytes or more
		// decrypt must be able to take 16 bytes or more
		// confirm it acts this way
		
		/*

		i = source.size(); // 37
		
		ByteBuffer s = source.buffer.duplicate();
		s.flip();
		ByteBuffer d = destination.buffer.duplicate();
		
		System.out.println("before " + s.position() + ":" + s.limit() + " source, " + d.position() + ":" + d.limit() + " destination ");
		int did = cipher.update(s, d);
		System.out.println("did " + did);
		System.out.println("after " + s.position() + ":" + s.limit() + " source, " + d.position() + ":" + d.limit() + " destination ");
		
		
		//solution to trailing end problem
		// if you've got less than block do final, otherwise update
		
		//solution to overflow problem
		// ask it to decrypt at most destination space minus block

		/*
		ByteBuffer s = source.
		
		cipher.update(input, output);
		
		if (source.size()      < cipher.getBlockSize()) throw new IllegalArgumentException("not enough data");
		if (destination.size() < cipher.getBlockSize()) throw new IllegalArgumentException("not enough space");
			
		*/
		
	}
	
	
	
	
	
	
	
	
}
