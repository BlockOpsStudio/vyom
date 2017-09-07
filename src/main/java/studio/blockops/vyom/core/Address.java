package studio.blockops.vyom.core;

import java.util.Arrays;

import org.spongycastle.util.encoders.Hex;

public class Address {

	private final byte[] value;
	
	private Address(final byte[] value) {
		this.value = value;
	}

	public byte[] getRaw() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(this.value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (!(obj instanceof Address)) {
			return false;
		}

		final Address other = (Address) obj;
		return Arrays.equals(this.value, other.value);
	}	
	
	@Override
	public String toString() {
		return Hex.toHexString(this.value);
	}

}
