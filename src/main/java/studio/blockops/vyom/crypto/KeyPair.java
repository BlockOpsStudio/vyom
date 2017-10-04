package studio.blockops.vyom.crypto;

import com.google.common.base.Preconditions;

/**
 * Holds {@link PrivateKey} and {@link PublicKey}
 */
public class KeyPair {

    /**
     * The Private Key
     */
    private final PrivateKey privateKey;

    /**
     * The Public Key
     */
    private final PublicKey publicKey;

    /**
     * Factory method to create a {@link KeyPair}
     */
    public static KeyPair create(PrivateKey privateKey, PublicKey publicKey) {
        return new KeyPair(privateKey, publicKey);
    }

    private KeyPair(PrivateKey privateKey, PublicKey publicKey) {
        Preconditions.checkNotNull(privateKey);
        Preconditions.checkNotNull(publicKey);

        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    /**
     * Gets the private key.
     *
     * @return the privateKey
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Gets the public key.
     *
     * @return the publicKey
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

}
