package studio.blockops.vyom.crypto.secp256k1;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.inject.Inject;

import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.math.ec.ECPoint;

import studio.blockops.vyom.core.Address;
import studio.blockops.vyom.crypto.Curve;
import studio.blockops.vyom.crypto.Hashing;
import studio.blockops.vyom.crypto.KeyGenerator;
import studio.blockops.vyom.crypto.KeyPair;
import studio.blockops.vyom.crypto.PrivateKey;
import studio.blockops.vyom.crypto.PublicKey;

/**
 * Implementation of the key generator for SECP256K1.
 */
public class SecP256K1KeyGenerator implements KeyGenerator {

	/**
	 * Random Number Generator
	 */
	private static final SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * A {@link SecP256K1Curve} instance
	 */
	private final Curve curve;

	@Inject
	private SecP256K1KeyGenerator(
			Curve curve) {
		this.curve = curve;
	}

	@Override
	public KeyPair generateKeyPair() {
		final ECKeyPairGenerator generator = new ECKeyPairGenerator();
		final ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(curve.getParams(), RANDOM);
		generator.init(keyGenParams);

		final AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();
		final ECPrivateKeyParameters privateKeyParams = (ECPrivateKeyParameters) keyPair.getPrivate();
		final PrivateKey privateKey = PrivateKey.create(privateKeyParams.getD());
		final PublicKey publicKey = derivePublicKey(privateKey);
		return KeyPair.create(privateKey, publicKey);
	}
	
	@Override	
	public PublicKey derivePublicKey(final PrivateKey privateKey) {
		final ECPoint point = this.curve.getParams().getG().multiply(privateKey.getRaw());
		return PublicKey.create(point.getEncoded(true));
	}
	
	@Override
	public Address computeAddress(PublicKey publicKey) {
		final ECPoint point = curve.getParams().getCurve().decodePoint(publicKey.getRaw());
		byte[] uncompressedPublicKey = point.getEncoded(false);
		byte[] address = Hashing.sha3omit12(Arrays.copyOfRange(uncompressedPublicKey, 1, uncompressedPublicKey.length));
		return Address.create(address);
	}
}
