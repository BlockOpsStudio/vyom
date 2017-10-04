package studio.blockops.vyom.serialization;

/**
 * An exception occurred during encoding
 */
public class EncoderException extends RuntimeException {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 4442311949959312323L;

	/**
	 * Creates a new encoder exception.
	 *
	 * @param message The exception message.
	 */
	public EncoderException(final String message) {
		super(message);
	}

	/**
	 * Creates a new encoder exception.
	 *
	 * @param cause The exception cause.
	 */
	public EncoderException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new encoder exception.
	 *
	 * @param message The exception message.
	 * @param cause The exception cause.
	 */
	public EncoderException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
