package studio.blockops.vyom.core.crypto.secp256k1;

import java.math.BigInteger;

import javax.inject.Inject;

import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.nem.core.crypto.*;

import com.google.common.hash.Hashing;
import com.google.inject.assistedinject.Assisted;

import studio.blockops.vyom.core.crypto.Curve;
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
//		TODO: Reanalyze this condition
//		if (!this.keyPair.hasPrivateKey()) {
//			throw new CryptoException("cannot sign without private key");
//		}

		final ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA3Digest(256)));
		final ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(
				this.keyPair.getPrivateKey().getRaw(),
				this.curve.getParams());
		signer.init(true, privateKeyParameters);
		final byte[] hash = Hashing.sha256().hashBytes(data).asBytes();
		final BigInteger[] components = signer.generateSignature(hash);
		final Signature signature = new Signature(components[0], components[1]);
		return this.makeSignatureCanonical(signature);
	}

	@Override
	public Signature makeSignatureCanonical(final Signature signature) {
		return this.isCanonicalSignature(signature)
				? signature
				: new Signature(signature.getR(), SecP256K1Curve.secp256k1().getParams().getN().subtract(signature.getS()));
	}

	private 
}
