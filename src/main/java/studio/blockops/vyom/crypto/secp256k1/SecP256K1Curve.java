package studio.blockops.vyom.crypto.secp256k1;

import java.math.BigInteger;

import org.spongycastle.crypto.params.ECDomainParameters;

import studio.blockops.vyom.crypto.Curve;

/**
 * Class that wraps the elliptic curve SECP256K1.
 */
public class SecP256K1Curve implements Curve {

	private final ECDomainParameters params;

	/**
	 * Constructor
	 * 
	 * @param params {@link ECDomainParameters}
	 */
	SecP256K1Curve(final ECDomainParameters params) {
		this.params = params;
	}

	@Override
	public String getName() {
		return "secp256k1";
	}

	@Override
	public BigInteger getGroupOrder() {
		return this.params.getN();
	}

	@Override
	public BigInteger getHalfGroupOrder() {
		return this.params.getN().shiftRight(1);
	}

	@Override
	public ECDomainParameters getParams() {
		return this.params;
	}
}
