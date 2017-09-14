package studio.blockops.vyom.core.crypto;

import static org.ethereum.util.BIUtil.isLessThan;
import static org.ethereum.util.BIUtil.isMoreThan;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

import org.ethereum.config.Constants;
import org.ethereum.util.ByteUtil;
import org.spongycastle.util.encoders.Hex;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * ECDSA Signature
 */
public class Signature {

	private final BigInteger r;
	private final BigInteger s;
	private final byte v;

	/**
	 * Creates a new signature.
	 *
	 * @param r The r-part of the signature.
	 * @param s The s-part of the signature.
	 */
	public static Signature create(final BigInteger r, final BigInteger s) {
		validate(r, s, (byte) 0);
		return new Signature(r, s, (byte) 0);
	}

	/**
	 * Creates a new signature.
	 *
	 * @param bytes The binary representation of the signature.
	 */
	public static Signature create(final byte[] bytes) {
		Preconditions.checkNotNull(bytes);
		Preconditions.checkArgument(
				(64 != bytes.length),
				"binary signature representation must be 64 bytes");

		BigInteger r = new BigInteger(1, Arrays.copyOfRange(bytes, 0, 32));
		BigInteger s = new BigInteger(1, Arrays.copyOfRange(bytes, 32, 64));

		validate(r, s, (byte) 0);
		return new Signature(r, s, (byte) 0);
	}

	/**
	 * Creates a new signature.
	 *
	 * @param r The binary representation of r.
	 * @param s The binary representation of s.
	 */
	public static Signature create(final byte[] r, final byte[] s) {
		Preconditions.checkNotNull(r);
		Preconditions.checkNotNull(s);
		Preconditions.checkArgument(
				(32 != r.length || 32 != s.length),
				"binary signature representation of r and s must both have 32 bytes length");

		final BigInteger r1 = new BigInteger(1, r);
		final BigInteger s1 = new BigInteger(1, s);

		validate(r1, s1, (byte) 0);
		return new Signature(r1, s1, (byte) 0);
	}

	/**
	 * Creates a new signature.
	 *
	 * @param r The r-part of the signature.
	 * @param s The s-part of the signature.
	 */
	public static Signature create(final String r, final String s, final byte v) {
		final BigInteger r1 = new BigInteger(r);
		final BigInteger s1 = new BigInteger(s);
		validate(r1, s1, v);
		return new Signature(r1, s1, v);
	}

	/**
	 * Creates a new signature.
	 *
	 * @param r The r-part of the signature.
	 * @param s The s-part of the signature.
	 */
	public static Signature create(final BigInteger r, final BigInteger s, final byte v) {
		validate(r, s, v);
		return new Signature(r, s, v);
	}

	private Signature(final BigInteger r, final BigInteger s, final byte v) {
		this.r = r;
		this.s = s;
		this.v = v;
	}

	private static void validate(final BigInteger r, final BigInteger s, final byte v) {
		Preconditions.checkNotNull(r);
		Preconditions.checkNotNull(s);

		Preconditions.checkArgument(v == 0 || v == 27 || v == 28, "Not a valid value for v");

		Preconditions.checkArgument(isMoreThan(r, BigInteger.ONE), "r cannot be less than 1");
		Preconditions.checkArgument(isMoreThan(s, BigInteger.ONE), "s cannot be less than 1");

		Preconditions.checkArgument(isLessThan(r, Constants.getSECP256K1N()), "r cannot be more than max value" + Constants.getSECP256K1N());
		Preconditions.checkArgument(isLessThan(s, Constants.getSECP256K1N()), "s cannot be more than max value" + Constants.getSECP256K1N());
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
	 * Gets the v-part of the signature.
	 *
	 * @return The v-part of the signature.
	 */
	public byte getV() {
		return v;
	}

	/**
	 * Gets a big-endian 65-byte representation of the signature with recoverID.
	 *
	 * @return a big-endian 65-byte representation of the signature with recoverID.
	 */
	public byte[] getBytes() {
		final byte fixedV = (this.v>=27) ? (byte) (this.v - 27) : this.v;

		return ByteUtil.merge(
				ByteUtil.bigIntegerToBytes(this.r),
				ByteUtil.bigIntegerToBytes(this.s),
				new byte[]{fixedV});
	}

	/**
	 * Returns hex representation of this Signature
	 * @return Hex representation of this Signature
	 */
	public String toHexString() {
		return Hex.toHexString(getBytes());
	}

	@Override
	public int hashCode() {
		return Objects.hash(r, s, v);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Signature) {
			Signature that = (Signature) object;
			return Objects.equals(this.r, that.r) && Objects.equals(this.s, that.s) && Objects.equals(this.v, that.v);
		}
		return false;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("r", r)
				.add("s", s)
				.add("v", v)
				.toString();
	}
}