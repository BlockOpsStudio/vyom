package studio.blockops.vyom.core.crypto;

/**
 * Interface that supports verification of arbitrarily sized message.
 */
public interface Verifier {

	/**
	 * Verifies that the signature is valid.
	 *
	 * @param data The original message.
	 * @param signature The generated signature.
	 * @return true if the signature is valid.
	 */
	boolean verify(final byte[] data, final Signature signature);

	/**
	 * Determines if the signature is canonical.
	 *
	 * @param signature The signature.
	 * @return true if the signature is canonical.
	 */
	boolean isCanonicalSignature(final Signature signature);
}
