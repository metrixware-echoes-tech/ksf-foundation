
package com.tocea.corolla.ui.widgets.gravatar;

/**
 * The Enum GravatarDefaultImage.
 */
public enum GravatarDefaultImage {

	GRAVATAR_ICON(""),

	HTTP_404("404"),

	IDENTICON("identicon"),

	MONSTERID("monsterid"),

	WAVATAR("wavatar");

	private String code;

	private GravatarDefaultImage(final String _code)

	{

		code = _code;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param _code the new code
	 */
	public void setCode(final String _code) {
		code = _code;
	}

}
