package studio.blockops.vyom.core.crypto;

import com.google.common.base.Preconditions;

public class KeyPair {
	
	private final PrivateKey privateKey;
	private final PublicKey publicKey;
	
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
