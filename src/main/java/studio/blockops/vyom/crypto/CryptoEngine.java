package studio.blockops.vyom.crypto;

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
     * @return The IES cipher.
     */
    BlockCipher createBlockCipher();
}
