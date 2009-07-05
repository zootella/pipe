package base.data;

import java.io.IOException;
import java.nio.ByteBuffer;

import base.exception.DiskException;
import base.file.File;
import base.size.Stripe;

// A Bay object holds data, growing to hold more you add to it.
// say one place in the program, probably in the javadoc or wiki page for bay
// this program holds the following conventions when using ByteBuffers
// position and limit clip around the data, not the space afterwards
// nothing ever moves them, if a java method is going to move them, our code copies the ByteBuffer and gives the java a copy
public class Bay {

	// Make
	
	/** Make a new empty Bay object that can hold data. */
	public Bay() {}
	/** Make a new empty Bay object prepared to hold size bytes of data. */
	public Bay(int size) { prepare(size); }

	/** Make a new Bay object with a copy of the given byte array in it. */
	public Bay(byte[] a) { add(a); }
	/** Make a new Bay object with the data of the given String in it. */
	public Bay(String s) { add(s); }
	/** Make a new Bay object with a copy of the data between b's position and limit in it, doesn't change b. */
	public Bay(ByteBuffer b) { add(b); }
	/** Make a new Bay object with a copy of d in it. */
	public Bay(Data d) { add(d); }

	// Add
	
	/** Add a single byte to end of the data this Bay object holds. */
	public void add(byte y) { add(new Data(y)); }
	/** Copy the given byte array into this Bay, adding it to the end of the data this Bay holds. */
	public void add(byte[] a) { add(new Data(a)); }
	/** Convert the given String into data, and add it to the end of the data this Bay holds. */
	public void add(String s) { add(new Data(s)); }
	/** Copy the data between b's position and limit into the end of this Bay, doesn't change b. */
	public void add(ByteBuffer b) { add(new Data(b)); }
	/** Copy the data d views into this Bay, adding it to the end of the data this Bay holds. */
	public void add(Data d) {
		if (d.isEmpty()) return;      // Nothing given to add
		prepare(d.size());            // Prepare our ByteBuffer to hold d's size
		buffer.put(d.toByteBuffer()); // Copy d in, moves buffer's position forward to still clip out the empty space at the end
	}

	/** Move all the data from the given Bin to the end of this Bay. */
	public void add(Bin b) {
		add(b.data());
		b.clear();
	}

	// Look

	/** true if this Bay is holding some data. */
	public boolean hasData() { return size() > 0; }
	/** How many bytes of data this Bay contains. */
	public int size() {
		if (buffer == null) return 0;
		return buffer.position() - offset;
	}
	
	/** Look at the data this Bay object holds. */
	public Data data() { return new Data(buffer()); }

	/**
	 * Make a read-only ByteBuffer with position and limit clipped around the data this Bay object holds.
	 * You can move the position without changing this Bay object.
	 */
	private ByteBuffer buffer() {
		if (buffer == null) return ByteBuffer.allocate(0); // If we don't have a ByteBuffer, return a new empty one
		
		// Make a new ByteBuffer clipped around our data, and return it
		ByteBuffer b = buffer.asReadOnlyBuffer(); // This doesn't copy the data
		b.position(offset); // Clip b's position and limit around our data
		b.limit(buffer.position());
		return b;
	}
	
	// Remove

	/** Remove size bytes from the start of the data this Bay object holds. */
	public void remove(int size) {

		// Check the given size
		if (size < 0) throw new IllegalArgumentException(); // Can't be negative
		if (size == 0) return; // Nothing to remove
		if (buffer == null || size > size()) throw new IndexOutOfBoundsException(); // We don't have that much data
		
		// Remove the data
		if (size == size()) clear(); // Remove everything we've got
		else offset += size; // Move our offset past the chunk of data to remove
	}
	
	/** Remove data from the start of this Bay, keeping only the last size bytes. */
	public void keep(int size) {
		remove(size() - size); // Remove everything but size bytes
	}

	/** Clear all the data this Bay object is holding. */
	public void clear() {
		if (buffer == null) return; // We don't have any data to clear

		// Mark our buffer as empty
		buffer.clear(); // Moves position to the start and limit to the end
		offset = 0;     // No data to skip over at the start
	}

	// Inside

	/**
	 * The ByteBuffer object that holds the data.
	 * The position and limit always clip around the empty space at the end of the buffer.
	 */
	private ByteBuffer buffer;

	/**
	 * The distance, in bytes, into the buffer where the data starts.
	 * There might be some removed data at the start, this offset will get you past it.
	 */
	private int offset;

	/** Prepare this Bay object to hold this many more bytes of data. */
	public void prepare(int more) {

		// Check the input
		if (more < 0) throw new IllegalArgumentException(); // Can't be negative
		if (more == 0) return; // No more space requested

		// We don't have a ByteBuffer to hold any data yet
		if (buffer == null) {

			// Make a new one exactly the right size
			buffer = allocate(more); // The position and limit clip out the whole empty thing

		// Our ByteBuffer isn't big enough to hold that much more data
		} else if (more > offset + buffer.remaining()) {

			// We'll make a new ByteBuffer, calculate how big it should be
			int size = ((size() + more) * 3) / 2; // It will be 2/3rds full
			if (size < 64) size = 64;             // At least 64 bytes

			// Replace our old ByteBuffer with a new, bigger one
			ByteBuffer b = allocate(size);
			b.put(buffer()); // Copy in all the data from the old one
			buffer = b;      // Point buffer at the new one, discarding our reference to the old one
			offset = 0;      // There's no removed data at the start of our new buffer

		// Our ByteBuffer will have enough space at the end once we shift the data to the start
		} else if (more > buffer.remaining()) {

			// Shift the data to the start of the ByteBuffer
			compact();
		}
	}

	/** Allocate a new empty Java ByteBuffer object with a capacity of size bytes. */
	private static ByteBuffer allocate(int size) {

		// If the size is 8 KB or bigger, allocate the memory outside of Java in the native process space 
		if (size < Bin.medium) return ByteBuffer.allocate(size);       // Back it with a Java byte array
		else                   return ByteBuffer.allocateDirect(size); // Get memory outside of Java
	}

	/** Shift the data this Bay object holds to the start of our ByteBuffer. */
	private void compact() {
		
		// We don't have any data, or it's already at the start
		if (size() == 0 || offset == 0) return;
		
		// Clip our buffer's position and limit around the data
		buffer.limit(buffer.position());
		buffer.position(offset);
		
		// Shift that data to the start
		buffer.compact(); // Also restores limit and position to again clip around the free space at the end
		
		// There's no data at the start to skip over any more
		offset = 0;
	}
	
	// File

	/** Add the given stripe of data from file to this Bay, or throw an IOException. */
	public void read(File file, Stripe stripe) {
		try {
			
			// Prepare enough room
			prepare((int)stripe.size);
			
			// Copy our buffer to read in the stripe
			ByteBuffer fill = buffer.duplicate(); // Copy buffer to move b's position and limit separately
			fill.limit(fill.position() + (int)stripe.size); // Clip position and limit around stripe size of space after our data
			
			// Copy data from the file to this Bay
			int did = file.file.getChannel().read(fill, stripe.i); // Read from file at stripe.i to fill between b's position and limit
			if (did != stripe.size) throw new DiskException("did " + did); // Make sure we got everything
			if (fill.hasRemaining()) throw new DiskException("remain");
			
			// Move buffer's position past the new data we wrote
			buffer.position(fill.position());

		} catch (IOException e) { throw new DiskException(e); }
	}
}
