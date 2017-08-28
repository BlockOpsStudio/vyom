package studio.blockops.vyom.core.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Static class that exposes hash functions.
 */
public class Hashing {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * Performs a SHA3-256 hash of the concatenated inputs.
	 *
	 * @param inputs The byte arrays to concatenate and hash.
	 * @return The hash of the concatenated inputs.
	 * @throws CryptoException if the hash operation failed.
	 */
	public static byte[] sha3_256(final byte[]... inputs) {
		return hash("SHA3-256", inputs);
	}

	/**
	 * Performs a SHA3-512 hash of the concatenated inputs.
	 *
	 * @param inputs The byte arrays to concatenate and hash.
	 * @return The hash of the concatenated inputs.
	 * @throws CryptoException if the hash operation failed.
	 */
	public static byte[] sha3_512(final byte[]... inputs) {
		return hash("SHA3-512", inputs);
	}

	/**
	 * Performs a RIPEMD160 hash of the concatenated inputs.
	 *
	 * @param inputs The byte arrays to concatenate and hash.
	 * @return The hash of the concatenated inputs.
	 * @throws CryptoException if the hash operation failed.
	 */
	public static byte[] ripemd160(final byte[]... inputs) {
		return hash("RIPEMD160", inputs);
	}

	private static byte[] hash(final String algorithm, final byte[]... inputs) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(algorithm, "BC");

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
