package studio.blockops.vyom.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

/**
 * Static class that exposes hash functions.
 */
public class Hashing {

    private static final String SHA3_256_ALGORITHM = "ETH-KECCAK-256";
    private static final String SHA3_512_ALGORITHM = "ETH-KECCAK-512";
    private static final String RIPEMD160_ALGORITHM = "RIPEMD160";

    /**
     * Performs a SHA3-256 hash of the concatenated inputs.
     *
     * @param inputs The byte arrays to concatenate and hash.
     * @return The hash of the concatenated inputs.
     * @throws CryptoException if the hash operation failed.
     */
    public static byte[] sha3_256(final byte[]... inputs) {
        return hash(SHA3_256_ALGORITHM, inputs);
    }

    /**
     * Performs a SHA3-512 hash of the concatenated inputs.
     *
     * @param inputs The byte arrays to concatenate and hash.
     * @return The hash of the concatenated inputs.
     * @throws CryptoException if the hash operation failed.
     */
    public static byte[] sha3_512(final byte[]... inputs) {
        return hash(SHA3_512_ALGORITHM, inputs);
    }

    /**
     * Calculates RIGTMOST160(SHA3(input)). This is used in address
     * calculations. *
     *
     * @param input
     *            - data
     * @return - 20 right bytes of the hash keccak of the data
     */
    public static byte[] sha3omit12(byte[] input) {
        byte[] hash = sha3_256(input);
        return Arrays.copyOfRange(hash, 12, hash.length);
    }

    /**
     * Performs a RIPEMD160 hash of the concatenated inputs.
     *
     * @param inputs The byte arrays to concatenate and hash.
     * @return The hash of the concatenated inputs.
     * @throws CryptoException if the hash operation failed.
     */
    public static byte[] ripemd160(final byte[]... inputs) {
        return hash(RIPEMD160_ALGORITHM, inputs);
    }

    private static byte[] hash(final String algorithm, final byte[]... inputs) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm, "SC");

            for (final byte[] input : inputs) {
                digest.update(input);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        } catch (NoSuchProviderException e) {
            throw new CryptoException(e);
        }

        return digest.digest();
    }
}
