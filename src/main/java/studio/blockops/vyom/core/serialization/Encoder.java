package studio.blockops.vyom.core.serialization;

import java.math.BigInteger;
import java.util.Collection;

public interface Encoder {

	/**
	 * Encodes a 32-bit integer value.
	 *
	 * @param i The value.
	 */
	void encodeInt(final int i);

	/**
	 * Encodes a 64-bit long value.
	 *
	 * @param l The value.
	 */
	void encodeLong(final long l);

	/**
	 * Encodes a 64-bit double value.
	 *
	 * @param d The value.
	 */
	void encodeDouble(final double d);

	/**
	 * Encodes a BigInteger value.
	 *
	 * @param i The value.
	 */
	void encodeBigInteger(final BigInteger i);

	/**
	 * Encodes a byte array value.
	 *
	 * @param bytes The value.
	 */
	void encodeBytes(final byte[] bytes);

	/**
	 * Encodes a String value.
	 *
	 * @param s The value.
	 */
	void encodeString(final String s);

	/**
	 * Encodes an object value.
	 *
	 * @param object The value.
	 */
	void encodeObject(final Encodable object);

	/**
	 * Encodes multiple object values.
	 *
	 * @param objects objects varargs
	 */
	void encodeObjectArray(final Encodable... objects);

	/**
	 * Encodes an array of object values.
	 *
	 * @param objects The array.
	 */
	void encodeObjectArray(final Collection<? extends Encodable> objects);

}
