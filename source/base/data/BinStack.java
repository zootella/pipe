package base.data;

import java.util.ArrayList;
import java.util.List;

public class BinStack {
	
	// Make
	
	public BinStack() {
		list = new ArrayList<Bin>();
	}
	
	// Inside
	
	private final List<Bin> list;
	
	// Use
	
	public void add(Bin bin) {
		if (list.size() > capacity)
			return;
		bin.clear();
		list.add(bin);
	}
	
	public Bin get() {
		if (list.isEmpty())
			return Bin.big();
		return list.remove(0);
	}
	
	// Define
	
	/** Maximum number of bins a BinStack can hold, 16 Bin.big 64 KB bins take up 1 MB of memory. */
	public static final int capacity = 16;
}
