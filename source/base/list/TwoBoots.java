package base.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import base.time.Ago;

public class TwoBoots<T> {

	public TwoBoots(long halflife) {
		
		this.halflife = halflife;
		
		current = new HashSet<T>();
		archive = new HashSet<T>();
		
	}
	
	public final long half;
	
	private Set current;
	private Set archive;
	
	private Ago when;
	
	
	public void add(T o) {
		
	}
	
	

}
