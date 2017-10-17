package studio.blockops.vyom.serialization.rlp;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import studio.blockops.vyom.serialization.Decoder;
import studio.blockops.vyom.serialization.DecoderException;

/**
 * Defines decoding methods for common types.
 * <p>
 * Decodes data from an enclosed {@link ByteArrayInputStream} object,
 * which is initialized by the provided byte array.
 */
public final class RLPDecoder implements Decoder, RLPParameters {

    /**
     * Byte buffer to store encoded data
     */
    private final ByteArrayInputStream input;

    /**
     * Creates a {@link RLPDecoder} instance initialized with {@link ByteArrayInputStream} object.
     *
     * @param data RLP encoded data as byte array
     */
    public RLPDecoder(final byte[] data) {
        input = new ByteArrayInputStream(data);
    }

    @Override
    public byte decodeByte() {
        final int b = read();
        final int masked = b & 0xFF;

        if (masked < OFFSET_SHORT_ITEM) {
            return (byte) b;
        } else if (masked == OFFSET_SHORT_ITEM + 1) {
            return (byte) read();
        } else if (masked == OFFSET_SHORT_ITEM) {
            return 0;
        } else {
            throw new DecoderException("Invalid byte decoding with first byte value: 0x" + Integer.toHexString(b));
        }
    }

    @Override
    public short decodeShort() {
        final int b = read();
        final int masked = b & 0xFF;

        if (masked < OFFSET_SHORT_ITEM) {
            return (short) b;
        } else if (masked > OFFSET_SHORT_ITEM && masked <= OFFSET_SHORT_ITEM + 2) {
            int length = b - OFFSET_SHORT_ITEM;
            return (short) decodeAsBigEndianLong(length);
        } else if (masked == OFFSET_SHORT_ITEM) {
            return 0;
        } else {
            throw new DecoderException("Invalid short decoding with first byte value: 0x" + Integer.toHexString(b));
        }
    }

    @Override
    public int decodeInt() {
        final int b = read();
        final int masked = b & 0xFF;

        if (masked < OFFSET_SHORT_ITEM) {
            return (int) b;
        } else if (masked > OFFSET_SHORT_ITEM && masked <= OFFSET_SHORT_ITEM + 4) {
            int length = b - OFFSET_SHORT_ITEM;
            return (int) decodeAsBigEndianLong(length);
        } else if (masked == OFFSET_SHORT_ITEM) {
            return 0;
        } else {
            throw new DecoderException("Invalid int decoding with first byte value: 0x" + Integer.toHexString(b));
        }
    }

    @Override
    public long decodeLong() {
        final int b = read();
        final int masked = b & 0xFF;

        if (masked < OFFSET_SHORT_ITEM) {
            return (long) b;
        } else if (masked > OFFSET_SHORT_ITEM && masked <= OFFSET_SHORT_ITEM + 8) {
            int length = b - OFFSET_SHORT_ITEM;
            return decodeAsBigEndianLong(length);
        } else if (masked == OFFSET_SHORT_ITEM) {
            return 0;
        } else {
            throw new DecoderException("Invalid long decoding with first byte value: 0x" + Integer.toHexString(b));
        }
    }

    @Override
    public BigInteger decodeBigInteger() {
        final int b = read();
        final int masked = b & 0xFF;

        if (masked > OFFSET_SHORT_ITEM && masked < OFFSET_SHORT_LIST) {
            final int length = getLength(b);
            return new BigInteger(1, read(length));
        } else if (masked < OFFSET_SHORT_ITEM) {
            return new BigInteger(1, new byte[]{(byte) b});
        } else if (masked == OFFSET_SHORT_ITEM) {
            return BigInteger.ZERO;
        } else {
            throw new DecoderException("Invalid BigInteger decoding with first byte value: 0x" + Integer.toHexString(b));
        }
    }

    @Override
    public byte[] decodeBytes() {
        final int b = read();
        final int masked = b & 0xFF;

        if (masked > OFFSET_SHORT_ITEM && masked < OFFSET_SHORT_LIST) {
            final int length = getLength(b);
            return read(length);
        } else if (masked < OFFSET_SHORT_ITEM) {
            return new byte[]{(byte) b};
        } else if (masked == OFFSET_SHORT_ITEM) {
            return new byte[]{};
        } else {
            throw new DecoderException("Invalid Byte Array decoding with first byte value: 0x" + Integer.toHexString(b));
        }
    }

    @Override
    public String decodeString() {
        return new String(decodeBytes());
    }

    @Override
    public byte[] decodeList() {
        final int b = read();
        final int masked = b & 0xFF;

        if (masked > OFFSET_SHORT_LIST) {
            final int length = getLength(b);
            return read(length);
        } else if (masked == OFFSET_SHORT_LIST) {
            return new byte[]{};
        } else {
            throw new DecoderException("Invalid list decoding with first byte value: 0x" + Integer.toHexString(b));
        }
    }

    private final int read() {
        final int b = input.read();
        if (b == -1) {
            throw new DecoderException("End of stream reached");
        }
        return b;
    }

    private final byte[] read(final int numberOfBytes) {
        final byte[] b = new byte[numberOfBytes];
        final int availableBytes = input.available();
        if (availableBytes < numberOfBytes) {
            throw new DecoderException(
                    new StringBuilder("End of stream reached. ")
                        .append("Bytes Available [").append(availableBytes)
                        .append("], Requested [").append(numberOfBytes).append("]")
                        .toString());
        }
        input.read(b, 0, numberOfBytes);
        return b;
    }

    private final long decodeAsBigEndianLong(int length) {
        long value = 0;
        while (length != 0) {
            value = (long) ((value <<  8) | (read() & 0xff));
            --length;
        }
        return value;
    }

    private final int getLength(final int b) {
        final int masked = b & 0xFF;

        if (masked > OFFSET_LONG_ITEM && masked < OFFSET_SHORT_LIST) {
            final int lengthOfLength = b - OFFSET_LONG_ITEM;
            return (int) decodeAsBigEndianLong(lengthOfLength);
        } else if (masked > OFFSET_SHORT_ITEM && masked <= OFFSET_LONG_ITEM) {
            return b - OFFSET_SHORT_ITEM;
        } else if (masked > OFFSET_LONG_LIST) {
            final int lengthOfLength = b - OFFSET_LONG_LIST;
            return (int) decodeAsBigEndianLong(lengthOfLength);
        } else if (masked > OFFSET_SHORT_LIST && masked <= OFFSET_LONG_LIST) {
            return b - OFFSET_SHORT_LIST;
        } else {
            throw new DecoderException("Invalid length decoding with first byte value: 0x" + Integer.toHexString(b));
        }
    }

}
