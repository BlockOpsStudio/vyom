package studio.blockops.vyom.serialization;

/**
 * An exception occurred during decoding
 */
public class DecoderException extends RuntimeException {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 4442311949959312323L;

	/**
	 * Creates a new decoder exception.
	 *
	 * @param message The exception message.
	 */
	public DecoderException(final String message) {
		super(message);
	}

	/**
	 * Creates a new decoder exception.
	 *
	 * @param cause The exception cause.
	 */
	public DecoderException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new decoder exception.
	 *
	 * @param message The exception message.
	 * @param cause The exception cause.
	 */
	public DecoderException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
