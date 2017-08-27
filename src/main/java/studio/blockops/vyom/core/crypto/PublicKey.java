package studio.blockops.vyom.core.crypto;

import java.util.Arrays;
import java.util.Locale;

import com.google.common.base.Preconditions;

import studio.blockops.vyom.core.utils.BaseCodec;

/**
 * Represents a public key.
 */
public final class PublicKey {

	private final byte[] value;
	
	/**
	 * Creates a new public key.
	 * 
	 * @param value The raw public key value.
	 * @return The new public key.
	 */
	public static PublicKey create(final byte[] value) {
		Preconditions.checkNotNull(value);
		return new PublicKey(value);
	}
	
	/**
	 * Creates a new public key from a hex string.
	 * 
	 * @param value The hex string.
	 * @return The new public key.
	 */
	public static PublicKey createFromHexString(final String value) {
		try {
			Preconditions.checkNotNull(value);
			return new PublicKey(BaseCodec.decodeBase16(value.toLowerCase(Locale.US)));
		} catch (final IllegalArgumentException e) {
			throw new CryptoException(e);
		}
	}
	
	private PublicKey(final byte[] value) {
		this.value = value;
	}
	
	public byte[] getRaw() {
		return this.value;
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
		
		if (!(obj instanceof PublicKey)) {
			return false;
		}

		final PublicKey other = (PublicKey) obj;
		return Arrays.equals(this.value, other.value);
	}	
	
	@Override
	public String toString() {
		return BaseCodec.encodeBase16(this.value);
	}
	
}
