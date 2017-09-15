package studio.blockops.vyom.core.crypto.secp256k1;

import java.security.SecureRandom;

import javax.inject.Inject;

import org.ethereum.ConcatKDFBytesGenerator;
import org.ethereum.crypto.EthereumIESEngine;
import org.spongycastle.crypto.BufferedBlockCipher;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.agreement.ECDHBasicAgreement;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.engines.AESFastEngine;
import org.spongycastle.crypto.macs.HMac;
import org.spongycastle.crypto.modes.SICBlockCipher;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.params.IESParameters;
import org.spongycastle.crypto.params.IESWithCipherParameters;
import org.spongycastle.crypto.params.ParametersWithIV;
import org.spongycastle.math.ec.ECPoint;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import studio.blockops.vyom.core.crypto.BlockCipher;
import studio.blockops.vyom.core.crypto.CryptoException;
import studio.blockops.vyom.core.crypto.Curve;
import studio.blockops.vyom.core.crypto.PrivateKey;
import studio.blockops.vyom.core.crypto.PublicKey;

/**
 * SecP256K1 implementation of {@link BlockCipher}
 */
public class SecP256K1BlockCipher implements BlockCipher {
	
	private static final byte[] d = new byte[] { };
	private static final byte[] e = new byte[] { };
	private static final int KEY_SIZE = 128;
	private static final IESParameters IES_PARAMETERS = new IESWithCipherParameters(d, e, KEY_SIZE, KEY_SIZE);

	/**
	 * A random number generator
	 */
	private static final SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * A {@link SecP256K1Curve} instance
	 */
	private final Curve curve;
	
	/**
	 * Ethereum IES Engine
	 */
	private final EthereumIESEngine iesEngine;

	@Inject
	private SecP256K1BlockCipher(Curve curve) {
		this.curve = curve;
		iesEngine = createEngine();
	}

	@Override
	public byte[] encrypt(final PrivateKey senderPrivateKey, final PublicKey recipientPublicKey, byte[] input) {
		Preconditions.checkNotNull(senderPrivateKey);
		Preconditions.checkNotNull(recipientPublicKey);
		Preconditions.checkNotNull(input);
		
		// Public Key Point
		final ECPoint publicKeyPoint = curve.getParams().getCurve().decodePoint(recipientPublicKey.getRaw());
		
		// IV
		final byte[] IV = new byte[KEY_SIZE/8];
		RANDOM.nextBytes(IV);
		final ParametersWithIV parametersWithIV = new ParametersWithIV(IES_PARAMETERS, IV);
		
		iesEngine.init(true,
				new ECPrivateKeyParameters(senderPrivateKey.getRaw(), curve.getParams()),
				new ECPublicKeyParameters(publicKeyPoint, curve.getParams()),
				parametersWithIV);
		
		try {
			final byte[] cipher = this.iesEngine.processBlock(input, 0, input.length);
			final ByteArrayDataOutput output = ByteStreams.newDataOutput();
			output.write(publicKeyPoint.getEncoded(false));
			output.write(IV);
			output.write(cipher);
			return output.toByteArray();
		} catch (final InvalidCipherTextException e) {
			throw new CryptoException(e);
		}
	}

	@Override
	public byte[] decrypt(final PrivateKey recipientPrivateKey, final byte[] cipher) {
		Preconditions.checkNotNull(recipientPrivateKey);
		Preconditions.checkNotNull(cipher);
		
		final ByteArrayDataInput input = ByteStreams.newDataInput(cipher);
		
		// Public Key Point
		final byte[] rawPublicKey = new byte[2*((curve.getParams().getCurve().getFieldSize()+7)/8) + 1];
		input.readFully(rawPublicKey);
		final ECPoint publicKeyPoint = curve.getParams().getCurve().decodePoint(rawPublicKey);
		
		// IV
		final byte[] IV = new byte[KEY_SIZE/8];
		input.readFully(IV);
        final ParametersWithIV parametersWithIV = new ParametersWithIV(IES_PARAMETERS, IV);
		
		// Cipher Body
		final byte[] cipherBody = new byte[cipher.length - rawPublicKey.length - IV.length];
		input.readFully(cipherBody);		
		
		iesEngine.init(false,
				new ECPrivateKeyParameters(recipientPrivateKey.getRaw(), curve.getParams()),
				new ECPublicKeyParameters(publicKeyPoint, curve.getParams()),
				parametersWithIV);
		
		try {
			return iesEngine.processBlock(cipherBody, 0, cipherBody.length);
		} catch (final InvalidCipherTextException e) {
			throw new CryptoException(e);
		}
	}
	
	private static final EthereumIESEngine createEngine() {
        final AESFastEngine aesFastEngine = new AESFastEngine();

        final EthereumIESEngine iesEngine = new EthereumIESEngine(
                new ECDHBasicAgreement(),
                new ConcatKDFBytesGenerator(new SHA256Digest()),
                new HMac(new SHA256Digest()),
                new SHA256Digest(),
                new BufferedBlockCipher(new SICBlockCipher(aesFastEngine)));
        
        return iesEngine;
	}

}
