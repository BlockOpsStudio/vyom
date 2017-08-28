package studio.blockops.vyom.core.crypto;

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

}
