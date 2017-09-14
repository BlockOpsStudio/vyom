package studio.blockops.vyom.core.utils;

import static java.util.Arrays.copyOfRange;
import static org.bouncycastle.util.Arrays.concatenate;
import static org.bouncycastle.util.BigIntegers.asUnsignedByteArray;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.util.encoders.Hex;


/**
 * @author Sarvesh Yadav, Bhaskar Singh
 */
public class RLP {

	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	/**
	 * [0x80] If a string is 0-55 bytes long, the RLP encoding consists of a
	 * single byte with value 0x80 plus the length of the string followed by the
	 * string. The range of the first byte is thus [0x80, 0xb7].
	 */
	private static final int OFFSET_SHORT_LIST = 0xc0;

	private static final int SIZE_THRESHOLD = 56;
	
	/**
     * [0xf7]
     * If the total payload of a list is more than 55 bytes long, the RLP
     * encoding consists of a single byte with value 0xf7 plus the length of the
     * length of the list in binary form, followed by the length of the list,
     * followed by the concatenation of the RLP encodings of the items. The
     * range of the first byte is thus [0xf8, 0xff].
     */
    private static final int OFFSET_LONG_LIST = 0xf7;
    
    
	private static final double MAX_ITEM_LENGTH = Math.pow(256, 8);

	
	
	/**
     * [0xb7]
     * If a string is more than 55 bytes long, the RLP encoding consists of a
     * single byte with value 0xb7 plus the length of the length of the string
     * in binary form, followed by the length of the string, followed by the
     * string. For example, a length-1024 string would be encoded as
     * \xb9\x04\x00 followed by the string. The range of the first byte is thus
     * [0xb8, 0xbf].
     */
    private static final int OFFSET_LONG_ITEM = 0xb7;

	/**
	 * [0x80] If a string is 0-55 bytes long, the RLP encoding consists of a
	 * single byte with value 0x80 plus the length of the string followed by the
	 * string. The range of the first byte is thus [0x80, 0xb7].
	 */
	private static final int OFFSET_SHORT_ITEM = 0x80;

	/*
	 * *****************************************************
	 * ****************** ENCODING *************************
	 * *****************************************************
	 */

	/**
	 * Turn Object into its RLP encoded equivalent of a byte-array Support for
	 * String, Integer, BigInteger and Lists of any of these types.
	 * 
	 * @param input
	 *            as object or List of objects
	 * @return byte[] RLP encoded
	 */
	public static byte[] encode(Object input) {
		Value val = new Value(input);
		if (val.isList()) {
			List<Object> inputArray = val.asList();

			if (inputArray.isEmpty()) {
				return encodeLength(inputArray.size(), OFFSET_SHORT_LIST);
			}
			byte[] output = EMPTY_BYTE_ARRAY;
			for (Object object : inputArray) {
				output = concatenate(output, encode(object));
			}
			byte[] prefix = encodeLength(output.length, OFFSET_SHORT_LIST);
			return concatenate(prefix, output);
		} else {
			byte[] inputAsBytes = toBytes(input);
			if (inputAsBytes.length == 1 && (inputAsBytes[0] & 0xff) <= 0x80) {
				return inputAsBytes;
			} else {
				byte[] firstByte = encodeLength(inputAsBytes.length, OFFSET_SHORT_ITEM);// 0x80+
																						// length
																						// +input
																						// data
				return concatenate(firstByte, inputAsBytes);
			}
		}
	}
	
	/*
	 * *****************************************************
	 * ****************** DECODING *************************
	 * *****************************************************
	 */
	
	 /**
     * Reads any RLP encoded byte-array and returns all objects as byte-array or list of byte-arrays
     *
     * @param data RLP encoded byte-array
     * @param pos  position in the array to start reading
     * @return DecodeResult encapsulates the decoded items as a single Object and the final read position
     */
    public static DecodeResult decode(byte[] data, int pos) {
        if (data == null || data.length < 1) {
            return null;
        }
        int prefix = data[pos] & 0xFF;
        if (prefix == OFFSET_SHORT_ITEM) {  // 0x80
            return new DecodeResult(pos + 1, ""); // means no length or 0
        } else if (prefix < OFFSET_SHORT_ITEM) {  // [0x00, 0x7f]
            return new DecodeResult(pos + 1, new byte[]{data[pos]}); // byte is its own RLP encoding
        } else if (prefix <= OFFSET_LONG_ITEM) {  // [0x81, 0xb7]
            int len = prefix - OFFSET_SHORT_ITEM; // length of the encoded bytes
            return new DecodeResult(pos + 1 + len, copyOfRange(data, pos + 1, pos + 1 + len));
        } else if (prefix < OFFSET_SHORT_LIST) {  // [0xb8, 0xbf]
            int lenlen = prefix - OFFSET_LONG_ITEM; // length of length the encoded bytes
            int lenbytes = byteArrayToInt(copyOfRange(data, pos + 1, pos + 1 + lenlen)); // length of encoded bytes
            return new DecodeResult(pos + 1 + lenlen + lenbytes, copyOfRange(data, pos + 1 + lenlen, pos + 1 + lenlen
                    + lenbytes));
        } else if (prefix <= OFFSET_LONG_LIST) {  // [0xc0, 0xf7]
            int len = prefix - OFFSET_SHORT_LIST; // length of the encoded list
            int prevPos = pos;
            pos++;
            return decodeList(data, pos, prevPos, len);
        } else if (prefix <= 0xFF) {  // [0xf8, 0xff]
            int lenlen = prefix - OFFSET_LONG_LIST; // length of length the encoded list
            int lenlist = byteArrayToInt(copyOfRange(data, pos + 1, pos + 1 + lenlen)); // length of encoded bytes
            pos = pos + lenlen + 1; // start at position of first element in list
            int prevPos = lenlist;
            return decodeList(data, pos, prevPos, lenlist);
        } else {
            throw new RuntimeException("Only byte values between 0x00 and 0xFF are supported, but got: " + prefix);
        }
    }
    
    // Code from: http://stackoverflow.com/a/4785776/459349
    public static String bytesToAscii(byte[] b) {
        String hex = Hex.toHexString(b);
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }
    
    private static DecodeResult decodeList(byte[] data, int pos, int prevPos, int len) {
        List<Object> slice = new ArrayList<Object>();
        for (int i = 0; i < len; ) {
            // Get the next item in the data list and append it
            DecodeResult result = decode(data, pos);
            slice.add(result.getDecoded());
            // Increment pos by the amount bytes in the previous read
            prevPos = result.getPos();
            i += (prevPos - pos);
            pos = prevPos;
        }
        return new DecodeResult(pos, slice.toArray());
    }
    
    /**
     * Cast hex encoded value from byte[] to int
     *
     * Limited to Integer.MAX_VALUE: 2^32-1 (4 bytes)
     *
     * @param b array contains the values
     * @return unsigned positive int value.
     */
    public static int byteArrayToInt(byte[] b) {
        if (b == null || b.length == 0)
            return 0;
        return new BigInteger(1, b).intValue();
    }

	/**
	 * Integer limitation goes up to 2^31-1 so length can never be bigger than
	 * MAX_ITEM_LENGTH
	 */
	public static byte[] encodeLength(int length, int offset) {// 0x80+length
		if (length < SIZE_THRESHOLD) {
			byte firstByte = (byte) (length + offset); //
			return new byte[] { firstByte };
		} else if (length < MAX_ITEM_LENGTH) {
			byte[] binaryLength;
			if (length > 0xFF)
				binaryLength = intToBytesNoLeadZeroes(length);
			else
				binaryLength = new byte[] { (byte) length };
			byte firstByte = (byte) (binaryLength.length + offset + SIZE_THRESHOLD - 1);
			return concatenate(new byte[] { firstByte }, binaryLength);
		} else {
			throw new RuntimeException("Input too long");
		}
	}

	public static String byteAryToHex(byte[] byteAry) {
		final StringBuilder builder = new StringBuilder();
		for (byte b : byteAry) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

	public static byte[] intToBytesNoLeadZeroes(int val) {

		long v = System.nanoTime();
		if (val == 0)
			return EMPTY_BYTE_ARRAY;

		int lenght = 0;

		int tmpVal = val;
		while (tmpVal != 0) {
			tmpVal = tmpVal >>> 8;
			++lenght;
		}

		byte[] result = new byte[lenght];

		int index = result.length - 1;
		while (val != 0) {

			result[index] = (byte) (val & 0xFF);
			val = val >>> 8;
			index -= 1;
		}
		System.out.println(System.nanoTime() - v);
		return result;
	}

	/*
	 * Utility function to convert Objects into byte arrays
	 */
	private static byte[] toBytes(Object input) {
		if (input instanceof byte[]) {
			return (byte[]) input;
		} else if (input instanceof String) {
			String inputString = (String) input;
			return inputString.getBytes();
		} else if (input instanceof Long) {
			Long inputLong = (Long) input;
			return (inputLong == 0) ? EMPTY_BYTE_ARRAY : asUnsignedByteArray(BigInteger.valueOf(inputLong));
		} else if (input instanceof Integer) {
			Integer inputInt = (Integer) input;
			return (inputInt == 0) ? EMPTY_BYTE_ARRAY : asUnsignedByteArray(BigInteger.valueOf(inputInt));
		} else if (input instanceof BigInteger) {
			BigInteger inputBigInt = (BigInteger) input;
			return (inputBigInt.equals(BigInteger.ZERO)) ? EMPTY_BYTE_ARRAY : asUnsignedByteArray(inputBigInt);
		}
		// else if (input instanceof Value) {
		// Value val = (Value) input;
		// return toBytes(val.asObj());
		// }
		throw new RuntimeException("Unsupported type: Only accepting String, Integer and BigInteger for now");
	}

}
