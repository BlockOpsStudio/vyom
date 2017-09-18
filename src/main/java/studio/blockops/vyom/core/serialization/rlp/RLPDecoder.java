package studio.blockops.vyom.core.serialization.rlp;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import studio.blockops.vyom.core.serialization.Decoder;

public final class RLPDecoder implements Decoder, RLPParameters {
	
	private final ByteArrayInputStream input;
	private byte [] data ; 
	
	public RLPDecoder(final byte[] data) {
		input = new ByteArrayInputStream(data);
		this.data = data;
	}

	@Override
	public void decodeByte(int index) throws IOException {
		 // null item
        if ((this.data[index] & 0xFF) == OFFSET_SHORT_ITEM) {
        	input.read(new byte[]  {(byte) (this.data[index] - OFFSET_SHORT_ITEM)});
        }
        // single byte item
        if ((this.data[index] & 0xFF) < OFFSET_SHORT_ITEM) {
        	input.read(new byte[]  {(byte) (this.data[index])});
        }
        // single byte item
        if ((this.data[index] & 0xFF) == OFFSET_SHORT_ITEM + 1) {
        	input.read(new byte[]  {(byte) (this.data[index +1])});
        }
		
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
