package studio.blockops.vyom.core.serialization;

public interface Encodable {

	/**
	 * Encodes this entity.
	 *
	 * @param encoder The {@link Encoder} to use
	 */
	void serialize(final Encoder encoder);

}
