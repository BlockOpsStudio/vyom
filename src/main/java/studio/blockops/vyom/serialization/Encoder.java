package studio.blockops.vyom.serialization;

import java.math.BigInteger;
import java.util.Collection;

/**
 * Encoder interface
 */
public interface Encoder {

    /**
     * Encodes a 8-bit byte value.
     *
     * @param b The value.
     */
    void encodeByte(final byte b);

    /**
     * Encodes a 16-bit short value.
     *
     * @param s The value.
     */
    void encodeShort(final short s);

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
