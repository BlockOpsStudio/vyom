package studio.blockops.vyom.serialization;

import java.math.BigInteger;

/**
 * Decoder interface
 */
public interface Decoder {

    /**
     * Decodes a 8-bit byte value.
     */
    byte decodeByte();

    /**
     * Decodes a 16-bit short value.
     */
    short decodeShort();

    /**
     * Decodes a 32-bit integer value.
     */
    int decodeInt();

    /**
     * Decodes a 64-bit long value.
     */
    long decodeLong();

    /**
     * Decodes a BigInteger value.
     */
    BigInteger decodeBigInteger();

    /**
     * Decodes a byte array value.
     */
    byte[] decodeBytes();

    /**
     * Decodes a String value.
     */
    String decodeString();

    /**
     * Decodes an object value.
     */
    void decodeObject();

    /**
     * Decodes multiple object values.
     */
    void decodeObjectArray();

}
