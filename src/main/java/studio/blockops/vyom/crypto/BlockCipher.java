package studio.blockops.vyom.crypto;

/**
 * Interface for encryption and decryption of data.
 */
public interface BlockCipher {

    /**
     * Encrypts an arbitrarily-sized message.
     *
     * @param senderPrivateKey The sender {@link PrivateKey}.
     * @param recipientPublicKey The recipient {@link PublicKey}.
     * @param input The message to encrypt.
     * @return The encrypted message.
     */
    byte[] encrypt(final PrivateKey senderPrivateKey, final PublicKey recipientPublicKey, byte[] input);

    /**
     * Decrypts an arbitrarily-sized message.
     *
     * @param recipientPrivateKey The recipient {@link PrivateKey}.
     * @param cipher The message to decrypt.
     * @return The decrypted message or null if decryption failed.
     */
    byte[] decrypt(final PrivateKey recipientPrivateKey, final byte[] cipher);
}
