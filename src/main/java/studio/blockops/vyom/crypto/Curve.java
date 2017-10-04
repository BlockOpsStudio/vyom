package studio.blockops.vyom.crypto;

import java.math.BigInteger;

import org.spongycastle.crypto.params.ECDomainParameters;

/**
 * Interface for getting information for a curve.
 */
public interface Curve {

	/**
	 * Gets the name of the curve.
	 *
	 * @return The name of the curve.
	 */
	String getName();

	/**
	 * Gets the group order.
	 *
	 * @return The group order.
	 */
	BigInteger getGroupOrder();

	/**
	 * Gets the group order / 2.
	 *
	 * @return The group order / 2.
	 */
	BigInteger getHalfGroupOrder();

	/**
	 * Gets the curve parameters.
	 *
	 * @return The curve parameters.
	 */
	ECDomainParameters getParams();
}
