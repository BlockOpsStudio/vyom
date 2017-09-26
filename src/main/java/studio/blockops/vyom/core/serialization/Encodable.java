package studio.blockops.vyom.core.serialization;

/**
 * This interface imposes the concrete class to define
 * encoding logic.
 */
public interface Encodable {

	/**
	 * Encodes this entity.
	 *
	 * @param encoder The {@link Encoder} to use
	 */
	void serialize(final Encoder encoder);

}
