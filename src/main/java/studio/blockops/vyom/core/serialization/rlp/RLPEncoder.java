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

	public final byte[] getEncoded() {
		return output.toByteArray();
	}

	@Override
	public void encodeByte(byte singleByte) {
		if ((singleByte & 0xFF) == 0) {
			byte[] bytes = new byte[] { (byte) RLPParameters.OFFSET_SHORT_ITEM };
			this.output.write(bytes, 0, bytes.length);
		} else if ((singleByte & 0xFF) <= 0x7F) {
			byte[] bytes1 = new byte[] { singleByte };
			this.output.write(bytes1, 0, bytes1.length);

		} else {
			byte[] bytes2 = new byte[] {
					(byte) (RLPParameters.OFFSET_SHORT_ITEM + 1), singleByte };
			this.output.write(bytes2, 0, bytes2.length);
		}
	}

	@Override
	public void encodeShort(short s) {
		// TODO Auto-generated method stub

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
