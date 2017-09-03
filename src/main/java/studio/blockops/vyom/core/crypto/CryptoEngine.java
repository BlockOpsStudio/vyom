package studio.blockops.vyom.core.crypto;

import com.google.inject.assistedinject.Assisted;

/**
 * Represents a cryptographic engine that is a factory of crypto-providers.
 */
public interface CryptoEngine {

	/**
	 * Creates a signer.
	 *
	 * @param keyPair The key pair.
	 * @return The signer.
	 */
	Signer createSigner(final KeyPair keyPair);

	/**
	 * Creates a key generator.
	 *
	 * @return The key generator.
	 */
	KeyGenerator createKeyGenerator();

	/**
	 * Creates a block cipher.
	 *
	 * @param senderKeyPair The sender KeyPair. The sender's private key is required for encryption.
	 * @param recipientKeyPair The recipient KeyPair. The recipient's private key is required for decryption.
	 * @return The IES cipher.
	 */
	BlockCipher createBlockCipher(
			@Assisted("senderKeyPair") KeyPair senderKeyPair,
			@Assisted("recipientKeyPair") KeyPair recipientKeyPair);

	/**
	 * Creates a key analyzer.
	 *
	 * @return The key analyzer.
	 */
	KeyAnalyzer createKeyAnalyzer();
}
