package org.zootella.encrypt.sign;

import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;

import org.zootella.data.Data;
import org.zootella.data.Outline;

public class SignKey {
	
	// Make

	/** Hold the public and private parts of a signature key. */
	public SignKey(Data g, Data p, Data q, Data x, Data y) {
		if (g.isEmpty() || p.isEmpty() || q.isEmpty() || x.isEmpty() || y.isEmpty()) throw new IllegalArgumentException();
		this.g = g;
		this.p = p;
		this.q = q;
		this.x = x;
		this.y = y;
	}

	/** Hold the public parts of a signature key. */
	public SignKey(Data g, Data p, Data q, Data y) {
		if (g.isEmpty() || p.isEmpty() || q.isEmpty() || y.isEmpty()) throw new IllegalArgumentException();
		this.g = g;
		this.p = p;
		this.q = q;
		this.x = null;
		this.y = y;
	}
	
	// Look

	public final Data g;
	public final Data p;
	public final Data q;
	public final Data x;
	public final Data y;

	/** true if this SignKey has all the public and private parts, false public only. */
	public boolean hasPrivate() { return x != null; }
	
	// Java
	
	/** Get the data parts of a new signature key Java has made for you. */
	public SignKey(DSAPublicKeySpec publicSpec, DSAPrivateKeySpec privateSpec) {
		this(
			new Data(publicSpec.getG()),
			new Data(publicSpec.getP()),
			new Data(publicSpec.getQ()),
			new Data(publicSpec.getY()),
			new Data(privateSpec.getX()));
	}

	/** Convert our data into a Java object. */
	public DSAPrivateKeySpec toPrivateSpec() {
		return new DSAPrivateKeySpec(x.toBigInteger(), p.toBigInteger(), q.toBigInteger(), g.toBigInteger()); 
	}

	/** Convert our data into a Java object. */
	public DSAPublicKeySpec toPublicSpec() {
		return new DSAPublicKeySpec(y.toBigInteger(), p.toBigInteger(), q.toBigInteger(), g.toBigInteger());
	}
	
	// Outline

	/** Turn this SignKey object into an Outline with the given name. */
	public Outline toOutline(String name) {
		Outline o = new Outline(name);
		o.add("g", g);
		o.add("p", p);
		o.add("q", q);
		if (x != null) o.add("x", x);
		o.add("y", y);
		return o;
	}

	/** Turn o back into a the SignKey object it was made from. */
	public static SignKey fromOutline(Outline o) {
		if (o.has("x"))
			return new SignKey(o.value("g"), o.value("p"), o.value("q"), o.value("x"), o.value("y"));
		else
			return new SignKey(o.value("g"), o.value("p"), o.value("q"), o.value("y"));
	}
}
