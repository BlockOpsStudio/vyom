
package studio.blockops.vyom.core.crypto;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;


public class Key {
	private static final ECDomainParameters ecParams;

    private static final SecureRandom secureRandom;
	
	static {
        X9ECParameters params = SECNamedCurves.getByName("secp256k1");
        ecParams = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
        secureRandom = new SecureRandom();
    }
	
	private BigInteger privateKey;
    private byte[] publicKey;
	
	//private long creationTimeSeconds;//TO check performance
	
	// Transient because it's calculated on demand.
    transient private byte[] publicKeyHash;
	
	public Key() {
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(ecParams, secureRandom);
        generator.init(keygenParams);
        AsymmetricCipherKeyPair keypair = generator.generateKeyPair();
        ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) keypair.getPrivate();
        ECPublicKeyParameters pubParams = (ECPublicKeyParameters) keypair.getPublic();
        privateKey = privParams.getD();
        // The public key is an encoded point on the elliptic curve. It has no meaning independent of the curve.
        publicKey = pubParams.getQ().getEncoded();
        //creationTimeSeconds = getTime() / 1000;
    }
	
	private Key(BigInteger privKey, byte[] pubKey) {
        this.privateKey = privKey;
        this.publicKey = null;
        if (pubKey == null && privKey != null) {
            // Derive public from private.
            this.publicKey = publicKeyFromPrivate(privKey);
        } else if (pubKey != null) {
            // We expect the pubkey to be in regular encoded form, just as a BigInteger. Therefore the first byte is
            // a special marker byte.
            // TODO: This is probably not a useful API and may be confusing.
            this.publicKey = pubKey;
        }
    }
	
	public Key(BigInteger privKey) {
        this(privKey, (byte[])null);
    }
	
	public Key(BigInteger privKey, BigInteger pubKey) {
        this(privKey, Utils.bigIntegerToBytes(pubKey, 65));
    }
	
	public Key(byte[] privKeyBytes, byte[] pubKey) {
        this(privKeyBytes == null ? null : new BigInteger(1, privKeyBytes), pubKey);
    }
	
	public static byte[] publicKeyFromPrivate(BigInteger privKey) {
        return ecParams.getG().multiply(privKey).getEncoded();
    }
	
	public byte[] getPubKey() {
        return publicKey;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("public:").append(Utils.bytesToHexString(publicKey));
        
        return b.toString();
    }
	
	public String toStringWithPrivate() {
        StringBuffer b = new StringBuffer();
        b.append(toString());
        if (privateKey != null) {
            b.append(" private:").append(Utils.bytesToHexString(privateKey.toByteArray()));
        }
        return b.toString();
    }
	
	public byte[] sign(byte[] input) {
        if (privateKey == null)
            throw new IllegalStateException("This ECKey does not have the private key necessary for signing.");
        ECDSASigner signer = new ECDSASigner();
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateKey, ecParams);
        signer.init(true, privKey);
        BigInteger[] sigs = signer.generateSignature(input);
        
        try {
            
            ByteArrayOutputStream bos = new UnsafeByteArrayOutputStream(72);
            DERSequenceGenerator seq = new DERSequenceGenerator(bos);
            seq.addObject(new DERInteger(sigs[0]));
            seq.addObject(new DERInteger(sigs[1]));
            seq.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);  // Cannot happen.
        }
    }
	
	public static boolean verify(byte[] data, byte[] signature, byte[] pub) {
        ECDSASigner signer = new ECDSASigner();
        ECPublicKeyParameters params = new ECPublicKeyParameters(ecParams.getCurve().decodePoint(pub), ecParams);//check for pub
        signer.init(false, params);
        try {
            ASN1InputStream decoder = new ASN1InputStream(signature);
            DERSequence seq = (DERSequence) decoder.readObject();
            DERInteger r = (DERInteger) seq.getObjectAt(0);
            DERInteger s = (DERInteger) seq.getObjectAt(1);
            decoder.close();
            return signer.verifySignature(data, r.getValue(), s.getValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	public boolean verify(byte[] data, byte[] signature) {
        return Key.verify(data, signature, publicKey);
    }
	
	/**
     * Returns a 32 byte array containing the private key.
     */
    public byte[] getPrivKeyBytes() {
        return Utils.bigIntegerToBytes(privateKey, 32);
    }
}