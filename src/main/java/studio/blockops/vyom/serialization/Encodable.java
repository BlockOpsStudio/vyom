package studio.blockops.vyom.serialization;

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
    void encode(final Encoder encoder);

    /**
     * Returns encoded data
     *
     * @return returns encoded data in byte array form.
     */
    byte[] getEncoded();

}
