package studio.blockops.vyom.core.crypto.secp256k1;

import java.security.spec.ECPoint;

import javax.inject.Inject;

import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;

import studio.blockops.vyom.core.crypto.Curve;
import studio.blockops.vyom.core.crypto.Hashing;
import studio.blockops.vyom.core.crypto.Signature;
import studio.blockops.vyom.core.crypto.Verifier;

public class SecP256K1Verifier implements Verifier {
	
	private final Curve curve;
	
	@Inject
	private SecP256K1Verifier(final Curve curve) {
		this.curve = curve;
	}

	@Override
	public boolean verify(final byte[] data, final Signature signature) {
		if (!this.isCanonicalSignature(signature)) {
			return false;
		}

		final ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA3Digest(256)));
		final ECPoint point = curve.getParams().getCurve().decodePoint(this.keyPair.getPublicKey().getRaw());
		final ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(point, SecP256K1Curve.secp256k1().getParams());
		signer.init(false, publicKeyParameters);
		final byte[] hash = Hashing.sha3_256(data);
		return signer.verifySignature(hash, signature.getR(), signature.getS());
	}

	@Override
	public boolean isCanonicalSignature(final Signature signature) {
		return signature.getS().compareTo(SecP256K1Curve.secp256k1().getHalfGroupOrder()) <= 0;
	}

}
