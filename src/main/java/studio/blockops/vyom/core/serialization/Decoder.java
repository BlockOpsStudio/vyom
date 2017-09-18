package studio.blockops.vyom.core.serialization;

import java.io.IOException;

public interface Decoder {

	/**
	 * Decodes a 8-bit byte value.
	 */
	void decodeByte(int index) throws IOException;

	/**
	 * Decodes a 16-bit short value.
	 */
	void decodeShort();

	/**
	 * Decodes a 32-bit integer value.
	 */
	void decodeInt();

	/**
	 * Decodes a 64-bit long value.
	 */
	void decodeLong();

	/**
	 * Decodes a BigInteger value.
	 */
	void decodeBigInteger();

	/**
	 * Decodes a byte array value.
	 */
	void decodeBytes();

	/**
	 * Decodes a String value.
	 */
	void decodeString();

	/**
	 * Decodes an object value.
	 */
	void decodeObject();

	/**
	 * Decodes multiple object values.
	 */
	void decodeObjectArray();

}
