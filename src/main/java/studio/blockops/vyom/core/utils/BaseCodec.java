package studio.blockops.vyom.core.utils;

import com.google.common.io.BaseEncoding;

/**
 * Static Utility class to encode/decode data for different bases.
 */
public class BaseCodec {
	
	/**
	 * Encodes a byte array to a hex string.
	 * 
	 * @param bytes The input byte array.
	 * @return The output hex string.
	 */
	public static String encodeBase16(final byte[] bytes) {
		return BaseEncoding.base16().lowerCase().encode(bytes);
	}
	
	/**
	 * Decodes a hex string to a byte array.
	 * 
	 * @param value The input hex string.
	 * @return The output byte array.
	 */
	public static byte[] decodeBase16(final String value) {
		final String hexString = (value.length() % 2 == 0) ? value : "0" + value;
		return BaseEncoding.base16().lowerCase().decode(hexString);
	}
}
