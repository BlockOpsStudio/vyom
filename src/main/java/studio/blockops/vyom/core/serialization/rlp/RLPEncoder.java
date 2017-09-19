package studio.blockops.vyom.core.serialization.rlp;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Collection;

import studio.blockops.vyom.core.serialization.Encodable;
import studio.blockops.vyom.core.serialization.Encoder;

public final class RLPEncoder implements Encoder, RLPParameters {

	private final ByteArrayOutputStream output;

	public RLPEncoder() {
		this.output = new ByteArrayOutputStream();
	}

    private final void write(final byte b) {
        output.write(b);
    }

    private final void write(final byte... b) {
        output.write(b, 0, b.length);
	}

	public final byte[] getEncoded() {
		return output.toByteArray();
	}

	@Override
	public void encodeByte(final byte b) {
	    if ((b & 0xFF) == 0) {
            write((byte) OFFSET_SHORT_ITEM);
        } else if ((b & 0xFF) <= 0x7F) {
            write(b);
        } else {
            write((byte) (OFFSET_SHORT_ITEM + 1), b);
        }
	}

	@Override
	public void encodeShort(final short s) {
        if ((s & 0xFF) == s)
            encodeByte((byte) s);
        else {
            write((byte) (OFFSET_SHORT_ITEM + 2),
                  (byte) (s >> 8 & 0xFF),
                  (byte) (s >> 0 & 0xFF));
        }
	}

	@Override
	public void encodeInt(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void encodeLong(long l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void encodeBigInteger(BigInteger i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void encodeBytes(byte[] bytes) {
		// TODO Auto-generated method stub

	}

	@Override
	public void encodeString(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void encodeObject(Encodable object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void encodeObjectArray(Encodable... objects) {
		// TODO Auto-generated method stub

	}

	@Override
	public void encodeObjectArray(Collection<? extends Encodable> objects) {
		// TODO Auto-generated method stub

	}

}
