package studio.blockops.vyom.core.utils;

import java.util.Arrays;
import java.util.List;

// import studio.blockops.vyom.core.crypto.HashUtil;
//import org.bouncycastle.util.encoders.Hex;
//
//import java.math.BigInteger;
//
//import java.util.Arrays;
//import java.util.List;

/**
 * Class to encapsulate an object and provide utilities for conversion
 */
public class Value {

	private Object value;
	private byte[] rlp;

	public static Value fromRlpEncoded(byte[] data) {

		if (data != null && data.length != 0) {
			Value v = new Value();
			v.init(data);
			return v;
		}
		return null;
	}

	public Value() {
	}

	public void init(byte[] rlp) {
		this.rlp = rlp;
	}

	public Value(Object obj) {

		if (obj == null)
			return;

		if (true) {
			this.value = obj;
		}
	}

	public List<Object> asList() {
		Object[] valueArray = (Object[]) value;
		return Arrays.asList(valueArray);
	}


	public boolean isList() {
		return value != null && value.getClass().isArray()
				&& !value.getClass().getComponentType().isPrimitive();
	}

}
