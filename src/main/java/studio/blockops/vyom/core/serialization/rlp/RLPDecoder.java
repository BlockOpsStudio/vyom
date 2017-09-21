package studio.blockops.vyom.core.serialization.rlp;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import studio.blockops.vyom.core.serialization.Decoder;

public final class RLPDecoder implements Decoder, RLPParameters {

	private final ByteArrayInputStream input;

	public RLPDecoder(final byte[] data) {
		input = new ByteArrayInputStream(data);
	}

    @Override
    public byte decodeByte() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short decodeShort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int decodeInt() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long decodeLong() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public BigInteger decodeBigInteger() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] decodeBytes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String decodeString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void decodeObject() {
        // TODO Auto-generated method stub

    }

    @Override
    public void decodeObjectArray() {
        // TODO Auto-generated method stub

    }

}