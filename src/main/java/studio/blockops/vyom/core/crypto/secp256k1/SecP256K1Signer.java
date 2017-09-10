package studio.blockops.vyom.core.crypto.secp256k1;

import java.math.BigInteger;
import java.util.Arrays;

import javax.inject.Inject;

import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.digests.SHA3Digest;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.crypto.signers.HMacDSAKCalculator;
import org.spongycastle.math.ec.ECPoint;

import com.google.inject.assistedinject.Assisted;

import studio.blockops.vyom.core.crypto.Curve;
import studio.blockops.vyom.core.crypto.ECKeyUtil;
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
				keyPair.getPrivateKey().getRaw(),
				curve.getParams());
		signer.init(true, privateKeyParameters);
		
		final byte[] messageHash = Hashing.sha3_256(data);
		final BigInteger[] components = signer.generateSignature(messageHash);
		
		final Signature signature = Signature.create(components[0], components[1]);
		final Signature canonicalSignature = makeSignatureCanonical(signature);
		return makeRecoverableSignature(canonicalSignature, messageHash);
	}
	
	private Signature makeRecoverableSignature(final Signature signature, final byte[] messageHash) {
		// Now we have to work backwards to figure out the recId needed to recover the signature.
        int recoverID = -1;

		final byte[] thisKey = getUncompressedPublicKey();
        for (int i = 0; i < 4; i++) {
            final byte[] k = ECKeyUtil.recoverPubBytesFromSignature(curve, i, signature, messageHash);
            if (k != null && Arrays.equals(k, thisKey)) {
                recoverID = i;
                break;
            }
        }
        
        if (recoverID == -1)
            throw new RuntimeException("Could not construct a recoverable key. This should never happen.");
        
        return Signature.create(signature.getR(), signature.getS(), (byte) (recoverID + 27));
	}
	
	private byte[] getUncompressedPublicKey() {
		final ECPoint point = curve.getParams().getG().multiply(keyPair.getPrivateKey().getRaw());
		return point.getEncoded(false);
	}

	@Override
	public Signature makeSignatureCanonical(final Signature signature) {
		if (isCanonicalSignature(signature)) {
			return signature;
		} else {
			return Signature.create(signature.getR(), curve.getParams().getN().subtract(signature.getS()));
		}
	}

	@Override
	public boolean verify(final byte[] data, final Signature signature) {
		if (!isCanonicalSignature(signature)) {
			return false;
		}

		final ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA3Digest(256)));
		final ECPoint point = curve.getParams().getCurve().decodePoint(keyPair.getPublicKey().getRaw());
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
