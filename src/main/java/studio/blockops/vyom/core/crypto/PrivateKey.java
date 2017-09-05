package studio.blockops.vyom.core.crypto;

import java.math.BigInteger;

import org.ethereum.util.ByteUtil;
import org.spongycastle.util.encoders.DecoderException;
import org.spongycastle.util.encoders.Hex;

import com.google.common.base.Preconditions;

/**
 * Represents a private key.
 */
public final class PrivateKey {

	private final BigInteger value;
	
	/**
	 * Creates a new private key.
	 * 
	 * @param value The raw private key value.
	 * @return The new private key.
	 */
	public static PrivateKey create(final BigInteger value) {
		Preconditions.checkNotNull(value);
		return new PrivateKey(value);
	}
	
	/**
	 * Creates a new private key from a decimal string.
	 * 
	 * @param value The decimal string.
	 * @return The new private key.
	 */
	public static PrivateKey createFromDecimalString(final String value) {
		Preconditions.checkNotNull(value);
		try {
			return new PrivateKey(new BigInteger(value, 10));
		} catch (final NumberFormatException e) {
			throw new CryptoException(e);
		}
	}
	
	/**
	 * Creates a new private key from a hex string.
	 * 
	 * @param value The hex string.
	 * @return The new private key.
	 */
	public static PrivateKey createFromHexString(final String value) {
		try {
			Preconditions.checkNotNull(value);
			return new PrivateKey(new BigInteger(1, Hex.decode(value)));
		} catch (final DecoderException e) {
			throw new CryptoException(e);
		}
	}
	
	private PrivateKey(final BigInteger value) {
		this.value = value;
	}
	
	public BigInteger getRaw() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (!(obj instanceof PrivateKey)) {
			return false;
		}

		final PrivateKey other = (PrivateKey) obj;
		return this.value.equals(other.value);
	}	
	
	@Override
	public String toString() {
		return Hex.toHexString(ByteUtil.bigIntegerToBytes(this.value));
	}
	
}
