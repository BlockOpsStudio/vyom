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
	public void encodeByte(byte b) {
		// TODO Auto-generated method stub
		
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
