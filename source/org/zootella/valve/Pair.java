package org.zootella.valve;

import java.util.ArrayList;
import java.util.List;

public class Pair<T> {
	
	// Pair
	
	/** A Pair looks at two neighboring objects in a List, a that comes right before b. */
	public Pair(T a, T b) {
		this.a = a;
		this.b = b;
	}

	/** The first object in this Pair. */
	public final T a;
	/** The second object in this Pair. */
	public final T b;
	
	// List of pairs

	/** Given a list of objects of type T, group them into pairs of T, last to first. */
	public static <T> List<Pair<T>> pairs(List<T> objects) {
		List<Pair<T>> pairs = new ArrayList<Pair<T>>();
		if (objects.size() < 2)
			return pairs; // Not enough objects for even one Pair, return an empty list
		for (int i = objects.size() - 1; i >= 1; i--) // Start with the last Pair
			pairs.add(new Pair<T>(objects.get(i - 1), objects.get(i))); // Add it to the list we'll return
		return pairs;
	}
}
