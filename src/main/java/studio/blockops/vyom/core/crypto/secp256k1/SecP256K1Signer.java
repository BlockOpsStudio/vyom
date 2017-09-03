package studio.blockops.vyom.core.crypto.secp256k1;

import java.math.BigInteger;

import javax.inject.Inject;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.math.ec.ECPoint;

import com.google.inject.assistedinject.Assisted;

import studio.blockops.vyom.core.crypto.Curve;
import studio.blockops.vyom.core.crypto.Hashing;
import studio.blockops.vyom.core.crypto.KeyPair;
import studio.blockops.vyom.core.crypto.Signature;
import studio.blockops.vyom.core.crypto.Signer;

/**
 * Implementation of the DSA signer for SECP256K1.
 */
public class SecP256K1Signer implements Signer {
	
	private final Curve curve;
	
	private final KeyPair keyPair;

	@Inject
	private SecP256K1Signer(
			Curve curve,
			@Assisted KeyPair keyPair) {
		this.curve = curve;
		this.keyPair = keyPair;
	}

	@Override
	public Signature sign(final byte[] data) {
		final ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
		final ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(
				this.keyPair.getPrivateKey().getRaw(),
				this.curve.getParams());
		signer.init(true, privateKeyParameters);
		final byte[] hash = Hashing.sha3_256(data);
		final BigInteger[] components = signer.generateSignature(hash);
		final Signature signature = new Signature(components[0], components[1]);
		return this.makeSignatureCanonical(signature);
	}

	@Override
	public Signature makeSignatureCanonical(final Signature signature) {
		if (isCanonicalSignature(signature)) {
			return signature;
		} else {
			return new Signature(signature.getR(), curve.getParams().getN().subtract(signature.getS()));
		}
	}

	@Override
	public boolean verify(final byte[] data, final Signature signature) {
		if (!this.isCanonicalSignature(signature)) {
			return false;
		}

		final ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA3Digest(256)));
		final ECPoint point = curve.getParams().getCurve().decodePoint(this.keyPair.getPublicKey().getRaw());
		final ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(point, curve.getParams());
		signer.init(false, publicKeyParameters);
		final byte[] hash = Hashing.sha3_256(data);
		return signer.verifySignature(hash, signature.getR(), signature.getS());
	}

	@Override
	public boolean isCanonicalSignature(final Signature signature) {
		return signature.getS().compareTo(curve.getHalfGroupOrder()) <= 0;
	}
}
