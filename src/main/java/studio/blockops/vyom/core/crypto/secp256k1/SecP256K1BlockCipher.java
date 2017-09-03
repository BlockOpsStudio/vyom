package studio.blockops.vyom.core.crypto.secp256k1;

import javax.inject.Inject;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.IESParameters;
import org.ethereum.ConcatKDFBytesGenerator;
import org.ethereum.crypto.EthereumIESEngine;

import com.google.inject.assistedinject.Assisted;

import studio.blockops.vyom.core.crypto.BlockCipher;
import studio.blockops.vyom.core.crypto.CryptoException;
import studio.blockops.vyom.core.crypto.Curve;
import studio.blockops.vyom.core.crypto.KeyPair;
import studio.blockops.vyom.core.crypto.PrivateKey;
import studio.blockops.vyom.core.crypto.PublicKey;

public class SecP256K1BlockCipher implements BlockCipher {
	
	private static final byte[] d = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
	private static final byte[] e = new byte[] { 8, 7, 6, 5, 4, 3, 2, 1 };
	
	private final EthereumIESEngine encryptEngine;
	
	private final EthereumIESEngine decryptEngine;

	@Inject
	private SecP256K1BlockCipher(
			Curve curve,
			@Assisted("senderKeyPair") KeyPair senderKeyPair,
			@Assisted("recipientKeyPair") KeyPair recipentKeyPair) {		
		encryptEngine = createEngine(curve, true,  senderKeyPair.getPrivateKey(), 	recipentKeyPair.getPublicKey());
		decryptEngine = createEngine(curve, false, recipentKeyPair.getPrivateKey(), senderKeyPair.getPublicKey());
	}

	@Override
	public byte[] encrypt(byte[] input) {
		try {
			return this.encryptEngine.processBlock(input, 0, input.length);
		} catch (final InvalidCipherTextException e) {
			throw new CryptoException(e);
		}
	}

	@Override
	public byte[] decrypt(final byte[] input) {
		try {
			return this.decryptEngine.processBlock(input, 0, input.length);
		} catch (final InvalidCipherTextException e) {
			throw new CryptoException(e);
		}
	}
	
	private static final EthereumIESEngine createEngine(
			Curve curve,
			boolean forEncryption,
			PrivateKey privateKey, 
			PublicKey publicKey) {
        AESFastEngine aesFastEngine = new AESFastEngine();

        EthereumIESEngine iesEngine = new EthereumIESEngine(
                new ECDHBasicAgreement(),
                new ConcatKDFBytesGenerator(new SHA256Digest()),
                new HMac(new SHA256Digest()),
                new SHA256Digest(),
                new BufferedBlockCipher(new SICBlockCipher(aesFastEngine)));
        		
		IESParameters IES_PARAMETERS = new IESParameters(d, e, 64);
		
		iesEngine.init(
				forEncryption, 
				new ECPrivateKeyParameters(privateKey.getRaw(), curve.getParams()),
				new ECPrivateKeyParameters(privateKey.getRaw(), curve.getParams()),
				IES_PARAMETERS);
        
        return iesEngine;
	}

}
