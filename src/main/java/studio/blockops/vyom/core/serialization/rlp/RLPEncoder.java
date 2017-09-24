package studio.blockops.vyom.core.serialization.rlp;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Collection;

import org.ethereum.util.ByteUtil;
import org.spongycastle.util.BigIntegers;

import com.google.common.base.Preconditions;

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
		if ((s & 0xFF) == s) {
			encodeByte((byte) s);
		} else {
			write((byte) (OFFSET_SHORT_ITEM + 2),
			        (byte) (s >> 8),
					(byte) s);
		}
	}

	@Override
	public void encodeInt(int i) {
        if ((i & 0xFF) == i) {
            encodeByte((byte) i);
        } else if ((i & 0xFFFF) == i) {
            encodeShort((short) i);
        } else if((i & 0xFFFFFFL) == i) {
            write((byte) (OFFSET_SHORT_ITEM + 3),
                    (byte) (i >>> 16),
                    (byte) (i >>> 8),
                    (byte) i);
        } else {
            write((byte) (OFFSET_SHORT_ITEM + 4),
                    (byte) (i >>> 24),
                    (byte) (i >>> 16),
                    (byte) (i >>> 8),
                    (byte) i);
        }
	}

    @Override
    public void encodeLong(long l) {
        if ((l & 0xFF) == l) {
            encodeByte((byte) l);
        } else if ((l & 0xFFFF) == l) {
            encodeShort((short) l);
        } else if((l & 0xFFFFFFFFL) == l) {
            encodeInt((int) l);
        } else if((l & 0xFFFFFFFFFFL) == l) {
            write((byte) (OFFSET_SHORT_ITEM + 5),
                    (byte) (l >>> 32),
                    (byte) (l >>> 24),
                    (byte) (l >>> 16),
                    (byte) (l >>> 8),
                    (byte) l);
        } else if((l & 0xFFFFFFFFFFFFL) == l) {
            write((byte) (OFFSET_SHORT_ITEM + 6),
                    (byte) (l >>> 40),
                    (byte) (l >>> 32),
                    (byte) (l >>> 24),
                    (byte) (l >>> 16),
                    (byte) (l >>> 8),
                    (byte) l);
        } else if((l & 0xFFFFFFFFFFFFFFL) == l) {
            write((byte) (OFFSET_SHORT_ITEM + 7),
                    (byte) (l >>> 48),
                    (byte) (l >>> 40),
                    (byte) (l >>> 32),
                    (byte) (l >>> 24),
                    (byte) (l >>> 16),
                    (byte) (l >>> 8),
                    (byte) l);
        } else {
            write((byte) (OFFSET_SHORT_ITEM + 8),
                    (byte) (l >>> 56),
                    (byte) (l >>> 48),
                    (byte) (l >>> 40),
                    (byte) (l >>> 32),
                    (byte) (l >>> 24),
                    (byte) (l >>> 16),
                    (byte) (l >>> 8),
                    (byte) l);
        }
    }

	@Override
	public void encodeBigInteger(BigInteger i) {
	    Preconditions.checkNotNull(i);
		if (i.equals(BigInteger.ZERO)) {
			encodeByte((byte) 0);
		} else {
			encodeBytes(BigIntegers.asUnsignedByteArray(i));
		}
	}

	@Override
	public void encodeBytes(byte[] bytes) {
		if (ByteUtil.isNullOrZeroArray(bytes)) {
			write((byte) OFFSET_SHORT_ITEM);
		} else if (bytes.length == 1 && (bytes[0] & 0xFF) < OFFSET_SHORT_ITEM) {
			write(bytes);
		} else if (bytes.length < SIZE_THRESHOLD) {
		    write((byte) (OFFSET_SHORT_ITEM + bytes.length));
		    write(bytes);
		} else {
			final int length = bytes.length;
			if (length <= 0xFF) {
			    write((byte) (OFFSET_LONG_ITEM + 1),
			            (byte) length);
			} else {
                write((byte) (OFFSET_LONG_ITEM + 2),
                        (byte) (length >> 8),
                        (byte) length);
			}
			write(bytes);
		}
	}

	@Override
	public void encodeString(String s) {
		encodeBytes(s.getBytes());
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
