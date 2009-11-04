package org.zootella.encrypt.pair;

import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import org.zootella.data.Data;
import org.zootella.data.Outline;

public class PairKey {
	
	// Make
	
	/** Hold the public and private parts of an encryption key. */
	public PairKey(Data modulus, Data publicExponent, Data privateExponent) {
		if (modulus.isEmpty() || publicExponent.isEmpty() || privateExponent.isEmpty()) throw new IllegalArgumentException();
		this.modulus = modulus;
		this.publicExponent = publicExponent;
		this.privateExponent = privateExponent;
	}

	/** Hold the public parts of an encryption key. */
	public PairKey(Data modulus, Data publicExponent) {
		if (modulus.isEmpty() || publicExponent.isEmpty()) throw new IllegalArgumentException();
		this.modulus = modulus;
		this.publicExponent = publicExponent;
		this.privateExponent = null;
	}
	
	// Look
	
	public final Data modulus;
	public final Data publicExponent;
	public final Data privateExponent;

	/** true if this PairKey has all the public and private parts, false public only. */
	public boolean hasPrivate() { return privateExponent != null; }

	// Java
	
	/** Get the data parts of a new encryption key Java has made. */
	public PairKey(RSAPublicKeySpec publicSpec, RSAPrivateKeySpec privateSpec) {
		this(
			new Data(publicSpec.getModulus()),
			new Data(publicSpec.getPublicExponent()),
			new Data(privateSpec.getPrivateExponent()));
	}

	/** Convert our data into a Java object. */
	public RSAPrivateKeySpec toPrivateSpec() {
		return new RSAPrivateKeySpec(modulus.toBigInteger(), privateExponent.toBigInteger()); 
	}

	/** Convert our data into a Java object. */
	public RSAPublicKeySpec toPublicSpec() {
		return new RSAPublicKeySpec(modulus.toBigInteger(), publicExponent.toBigInteger());
	}
	
	// Outline

	/** Turn this PairKey object into an Outline with the given name. */
	public Outline toOutline(String name) {
		Outline o = new Outline(name);
		o.add("m", modulus);
		o.add("u", publicExponent);
		if (privateExponent != null) o.add("r", privateExponent);
		return o;
	}

	/** Turn o back into a the PairKey object it was made from. */
	public static PairKey fromOutline(Outline o) {
		if (o.has("r"))
			return new PairKey(o.value("m"), o.value("u"), o.value("r"));
		else
			return new PairKey(o.value("m"), o.value("u"));
	}
}
