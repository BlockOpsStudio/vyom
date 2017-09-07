package studio.blockops.vyom.core.crypto.secp256k1;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.inject.Inject;

import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.math.ec.ECPoint;

import studio.blockops.vyom.core.crypto.Curve;
import studio.blockops.vyom.core.crypto.Hashing;
import studio.blockops.vyom.core.crypto.KeyGenerator;
import studio.blockops.vyom.core.crypto.KeyPair;
import studio.blockops.vyom.core.crypto.PrivateKey;
import studio.blockops.vyom.core.crypto.PublicKey;

/**
 * Implementation of the key generator for SECP256K1.
 */
public class SecP256K1KeyGenerator implements KeyGenerator {

	private static final SecureRandom RANDOM = new SecureRandom();
	
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
	public byte[] computeAddress(PublicKey publicKey) {
		final ECPoint point = curve.getParams().getCurve().decodePoint(publicKey.getRaw());
		byte[] uncompressedPublicKey = point.getEncoded(false);
		return Hashing.sha3omit12(Arrays.copyOfRange(uncompressedPublicKey, 1, uncompressedPublicKey.length));		
	}
}
