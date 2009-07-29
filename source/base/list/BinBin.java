package base.list;

import java.util.ArrayList;
import java.util.List;

import base.data.Bin;

/** A recycling bin of Bin objects ready for quick reuse without memory allocation. */
public class BinBin {

	/** Make a recycling bin of Bin objects to keep and reuse them quickly. */
	public BinBin() {
		list = new ArrayList<Bin>();
	}

	/** Our internal List of Bin objects. */
	private final List<Bin> list;

	/** Finished with bin, add it to this recycling BinBin. */
	public void add(Bin bin) {
		if (bin.capacity() != Bin.big) throw new IllegalArgumentException(); // Check the size
		if (list.size() > capacity)
			return; // We're full
		bin.clear(); // Clear and keep it
		list.add(bin);
	}

	/** Quickly get a Bin from this recycling BinBin. */
	public Bin get() {
		if (list.isEmpty())
			return Bin.big(); // Fresh out, allocate a new one
		return list.remove(0); // Remove one from our list
	}

	/** Maximum number of bins we can hold, 16 Bin.big 64 KB bins take up 1 MB of memory. */
	public static final int capacity = 16;
}
