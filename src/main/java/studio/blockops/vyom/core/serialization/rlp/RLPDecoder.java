package studio.blockops.vyom.core.serialization.rlp;

import java.io.ByteArrayInputStream;

import studio.blockops.vyom.core.serialization.Decoder;

public final class RLPDecoder implements Decoder, RLPParameters {
	
	private final ByteArrayInputStream input;
	
	public RLPDecoder(final byte[] data) {
		input = new ByteArrayInputStream(data);
	}

	@Override
	public void decodeByte() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decodeShort() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decodeInt() {
		// TODO Auto-generated method stub

	}

	@Override
	public void decodeLong() {
		// TODO Auto-generated method stub

	}

	@Override
	public void decodeBigInteger() {
		// TODO Auto-generated method stub

	}

	@Override
	public void decodeBytes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void decodeString() {
		// TODO Auto-generated method stub

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
