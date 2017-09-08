package studio.blockops.vyom.core;

import java.util.Arrays;

import org.spongycastle.util.encoders.DecoderException;
import org.spongycastle.util.encoders.Hex;

import com.google.common.base.Preconditions;

import studio.blockops.vyom.core.crypto.CryptoException;

public class Address {

	private final byte[] value;
	
	/**
	 * Creates a new address
	 * 
	 * @param value The raw address value.
	 * @return The address
	 */
	public static Address create(final byte[] value) {
		return new Address(value);
	}
	
	/**
	 * Creates a new address from a hex string.
	 * 
	 * @param value The hex string.
	 * @return The address
	 */
	public static Address createFromHexString(final String value) {
		try {
			Preconditions.checkNotNull(value);
			return new Address(Hex.decode(value));
		} catch (final DecoderException e) {
			throw new CryptoException(e);
		}
	}
	
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
