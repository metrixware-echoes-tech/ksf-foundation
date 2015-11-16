package com.tocea.corolla.ui.widgets.gravatar;

/**
 * The Enum GravatarRating.
 */
public enum GravatarRating {
	
	GENERAL_AUDIENCES("g"),
	
	PARENTAL_GUIDANCE_SUGGESTED("pg"),
	
	RESTRICTED("r"),
	
	XPLICIT("x");
	
	private String code;
	
	private GravatarRating(final String code) {
		this.code = code;
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
	 * @param _code
	 *            the new code
	 */
	public void setCode(final String _code) {
		code = _code;
	}
	
}