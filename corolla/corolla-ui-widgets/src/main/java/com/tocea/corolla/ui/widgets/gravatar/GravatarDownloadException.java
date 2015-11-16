
package com.tocea.corolla.ui.widgets.gravatar;

/**
 * The Class GravatarDownloadException.
 */
public class GravatarDownloadException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new gravatar download exception.
	 *
	 * @param cause
	 *            the cause
	 */
	public GravatarDownloadException(final Throwable cause) {

		super("Gravatar could not be downloaded: " + cause.getMessage(), cause);
	}

}
