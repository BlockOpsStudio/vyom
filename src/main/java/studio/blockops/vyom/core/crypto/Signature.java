package studio.blockops.vyom.core.crypto;

import java.math.BigInteger;
import java.util.Arrays;

import org.ethereum.util.ByteUtil;
import org.spongycastle.util.encoders.Hex;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * Digital Signature Interface
 */
public class Signature {
	private static final BigInteger MAXIMUM_VALUE = BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE);

	private final BigInteger r;
	private final BigInteger s;
	private final byte v;

	/**
	 * Creates a new signature.
	 *
	 * @param r The r-part of the signature.
	 * @param s The s-part of the signature.
	 */
	public Signature(final BigInteger r, final BigInteger s) {
		Preconditions.checkNotNull(r);
		Preconditions.checkNotNull(s);
		Preconditions.checkArgument(r.compareTo(MAXIMUM_VALUE) < 0,	"r must fit into 32 bytes");
		Preconditions.checkArgument(s.compareTo(MAXIMUM_VALUE) < 0,	"s must fit into 32 bytes");

		this.r = r;
		this.s = s;
		this.v = 0;
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

		BigInteger r = new BigInteger(1, Arrays.copyOfRange(bytes, 0, 32));
		BigInteger s = new BigInteger(1, Arrays.copyOfRange(bytes, 32, 64));
		
		Preconditions.checkArgument(r.compareTo(MAXIMUM_VALUE) < 0,	"r must fit into 32 bytes");
		Preconditions.checkArgument(s.compareTo(MAXIMUM_VALUE) < 0,	"s must fit into 32 bytes");

		this.r = r;
		this.s = s;
		this.v = 0;
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

		this.r = new BigInteger(1, r);
		this.s = new BigInteger(1, s);
		this.v = 0;
	}

	/**
	 * Creates a new signature.
	 *
	 * @param r The r-part of the signature.
	 * @param s The s-part of the signature.
	 */
	public Signature(final BigInteger r, final BigInteger s, byte v) {
		this.r = r;
		this.s = s;
		this.v = v;
	}

	/**
	 * Gets the r-part of the signature.
	 *
	 * @return The r-part of the signature.
	 */
	public BigInteger getR() {
		return r;
	}

	/**
	 * Gets the r-part of the signature.
	 *
	 * @return The r-part of the signature.
	 */
	public byte[] getBinaryR() {
		return this.r.toByteArray();
	}

	/**
	 * Gets the s-part of the signature.
	 *
	 * @return The s-part of the signature.
	 */
	public BigInteger getS() {
		return s;
	}

	/**
	 * Gets the s-part of the signature.
	 *
	 * @return The s-part of the signature.
	 */
	public byte[] getBinaryS() {
		return this.s.toByteArray();
	}

	/**
	 * Gets a big-endian 64-byte representation of the signature.
	 *
	 * @return a big-endian 64-byte representation of the signature
	 */
	public byte[] getBytes() {
        final byte fixedV = this.v >= 27
                ? (byte) (this.v - 27)
                :this.v;

        return ByteUtil.merge(
                ByteUtil.bigIntegerToBytes(this.r),
                ByteUtil.bigIntegerToBytes(this.s),
                new byte[]{fixedV});
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(r, s);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Signature) {
			Signature that = (Signature) object;
			return Objects.equal(this.r, that.r) && Objects.equal(this.s, that.s);
		}
		return false;
	}

	@Override
	public String toString() {
		return Hex.toHexString(getBytes());
	}
}