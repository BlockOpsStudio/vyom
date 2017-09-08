package studio.blockops.vyom.core.crypto;

import studio.blockops.vyom.core.Address;

/**
 * Interface for generating keys.
 */
public interface KeyGenerator {

	/**
	 * Creates a random key pair.
	 *
	 * @return The key pair.
	 */
	KeyPair generateKeyPair();

	/**
	 * Derives a public key from a private key.
	 *
	 * @param privateKey the private key.
	 * @return The public key.
	 */
	PublicKey derivePublicKey(final PrivateKey privateKey);

    /**
     * Compute an address from an encoded public key.
     *
     * @param publicKey the public key
     * @return An Address
     */
    Address computeAddress(final PublicKey publicKey);
}
