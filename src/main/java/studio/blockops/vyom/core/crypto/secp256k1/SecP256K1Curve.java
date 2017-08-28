package studio.blockops.vyom.core.crypto.secp256k1;

import java.math.BigInteger;

import org.bouncycastle.crypto.params.ECDomainParameters;

import studio.blockops.vyom.core.crypto.Curve;

/**
 * Class that wraps the elliptic curve SECP256K1.
 */
public class SecP256K1Curve implements Curve {

	private final ECDomainParameters params;
	private final BigInteger halfGroupOrder;

	SecP256K1Curve(final ECDomainParameters params, final BigInteger halfGroupOrder) {
		this.params = params;
		this.halfGroupOrder = halfGroupOrder;
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
		return this.halfGroupOrder;
	}

	@Override
	public ECDomainParameters getParams() {
		return this.params;
	}
}
