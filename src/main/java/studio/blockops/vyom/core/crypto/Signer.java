package studio.blockops.vyom.core.crypto;

/**
 * Interface that supports signing of arbitrarily sized message.
 */
public interface Signer {

	/**
	 * Signs the SHA3 hash of an arbitrarily sized message.
	 *
	 * @param data The message to sign.
	 * @return The generated signature.
	 */
	Signature sign(final byte[] data);

	/**
	 * Makes this signature canonical.
	 *
	 * @param signature The signature.
	 * @return Signature in canonical form.
	 */
	Signature makeSignatureCanonical(final Signature signature);
}
