package studio.blockops.vyom.core.serialization.rlp;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import org.ethereum.util.ByteUtil;
import org.spongycastle.util.BigIntegers;

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
			write((byte) (OFFSET_SHORT_ITEM + 2), (byte) (s >> 8 & 0xFF),
					(byte) (s >> 0 & 0xFF));
		}
	}

	@Override
	public void encodeInt(int i) {
		if ((i & 0xFF) == i) {
			encodeByte((byte) i);
		} else if ((i & 0xFFFF) == i) {
			encodeShort((short) i);
		} else if ((i & 0xFFFFFF) == i) {
			write((byte) (OFFSET_SHORT_ITEM + 3), (byte) (i >>> 16),
					(byte) (i >>> 8), (byte) i);
		} else if ((i & 0xFFFFFFFF) == i) {
			write((byte) (OFFSET_SHORT_ITEM + 4), (byte) (i >>> 24),
					(byte) (i >>> 16), (byte) (i >>> 8), (byte) i);
		}

	}

	@Override
	public void encodeLong(long l) {
        if ((l & 0xFF) == l) {
            encodeByte((byte) l);
        } else if ((l & 0xFFFF) == l) {
            encodeShort((short) l);
        } else if (l >= 0 && l <= Integer.MAX_VALUE) {
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
		if (i.equals(BigInteger.ZERO))
			encodeByte((byte) 0);
		else
			encodeBytes(BigIntegers.asUnsignedByteArray(i));
	}

	@Override
	public void encodeBytes(byte[] bytes) {
		if (ByteUtil.isNullOrZeroArray(bytes)) {
			write(new byte[] { (byte) OFFSET_SHORT_ITEM });
		}

		else if (ByteUtil.isSingleZero(bytes)) {
			write(bytes);
		} else if (bytes.length == 1 && (bytes[0] & 0xFF) < 0x80) {
			write(bytes);
		} else if (bytes.length < SIZE_THRESHOLD) {
			// length = 8X
			byte length = (byte) (OFFSET_SHORT_ITEM + bytes.length);
			byte[] data = Arrays.copyOf(bytes, bytes.length + 1);
			System.arraycopy(data, 0, data, 1, bytes.length);
			data[0] = length;
			write(data);
		} else {
			// length of length = BX
			// prefix = [BX, [length]]
			int tmpLength = bytes.length;//303 2byte numbr
			byte byteNum = 0;
			while (tmpLength != 0) {
				++byteNum;
				tmpLength = tmpLength >> 8;
			}
			byte[] lenBytes = new byte[byteNum];
			for (int i = 0; i < byteNum; ++i) {
				lenBytes[byteNum - 1 - i] = (byte) ((bytes.length >> (8 * i)) & 0xFF); //lenby[1]= ; lenbyt[0]=
			}
			// first byte = F7 + bytes.length
			byte[] data = Arrays.copyOf(bytes, bytes.length + 1 + byteNum);
			System.arraycopy(data, 0, data, 1 + byteNum, bytes.length);
			data[0] = (byte) (OFFSET_LONG_ITEM + byteNum);//
			System.arraycopy(lenBytes, 0, data, 1, lenBytes.length);
			write(data);
		}
	}

	@Override
	public void encodeString(final String s) {
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
