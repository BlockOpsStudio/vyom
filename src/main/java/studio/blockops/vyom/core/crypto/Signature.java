package studio.blockops.vyom.core.crypto;

import java.math.BigInteger;
import java.util.Arrays;

import com.google.common.base.Preconditions;

import studio.blockops.vyom.core.utils.BaseCodec;

/**
 * Digital Signature Interface
 */
public class Signature {
	private static final BigInteger MAXIMUM_VALUE = BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE);

	private final byte[] r;
	private final byte[] s;

	/**
	 * Creates a new signature.
	 *
	 * @param r The r-part of the signature.
	 * @param s The s-part of the signature.
	 */
	public Signature(final BigInteger r, final BigInteger s) {
		Preconditions.checkNotNull(r);
		Preconditions.checkNotNull(s);
		Preconditions.checkArgument(
				(0 < r.compareTo(MAXIMUM_VALUE) || 0 < s.compareTo(MAXIMUM_VALUE)),
				"r and s must fit into 32 bytes");

		this.r = r.toByteArray();
		this.s = s.toByteArray();
	}

	/**
	 * Creates a new signature.
	 *
	 * @param bytes The binary representation of the signature.
	 */
	public Signature(final byte[] bytes) {
		Preconditions.checkNotNull(bytes);
		Preconditions.checkArgument(
				(64 != bytes.length),
				"binary signature representation must be 64 bytes");

		this.r = Arrays.copyOfRange(bytes, 0, 32);
		this.s = Arrays.copyOfRange(bytes, 32, 64);
	}

	/**
	 * Creates a new signature.
	 *
	 * @param r The binary representation of r.
	 * @param s The binary representation of s.
	 */
	public Signature(final byte[] r, final byte[] s) {
		Preconditions.checkNotNull(r);
		Preconditions.checkNotNull(s);
		Preconditions.checkArgument(
				(32 != r.length || 32 != s.length),
				"binary signature representation of r and s must both have 32 bytes length");

		this.r = r;
		this.s = s;
	}

	/**
	 * Gets the r-part of the signature.
	 *
	 * @return The r-part of the signature.
	 */
	public BigInteger getR() {
		return new BigInteger(1, r);
	}

	/**
	 * Gets the r-part of the signature.
	 *
	 * @return The r-part of the signature.
	 */
	public byte[] getBinaryR() {
		return this.r;
	}

	/**
	 * Gets the s-part of the signature.
	 *
	 * @return The s-part of the signature.
	 */
	public BigInteger getS() {
		return new BigInteger(1, s);
	}

	/**
	 * Gets the s-part of the signature.
	 *
	 * @return The s-part of the signature.
	 */
	public byte[] getBinaryS() {
		return this.s;
	}

	/**
	 * Gets a big-endian 64-byte representation of the signature.
	 *
	 * @return a big-endian 64-byte representation of the signature
	 */
	public byte[] getBytes() {
		byte[] c = new byte[64];
		System.arraycopy(r, 0, c, 0, 32);
		System.arraycopy(s, 0, c, 32, 32);
		return c;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.r) ^ Arrays.hashCode(this.s);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Signature)) {
			return false;
		}

		final Signature other = (Signature) obj;

		return Arrays.equals(this.r, other.r) && Arrays.equals(this.s, other.s);
	}

	@Override
	public String toString() {
		return BaseCodec.encodeBase16(this.getBytes());
	}
}