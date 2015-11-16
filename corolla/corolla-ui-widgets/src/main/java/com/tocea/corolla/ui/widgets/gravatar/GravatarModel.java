
package com.tocea.corolla.ui.widgets.gravatar;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * Gravatar model to convert email into url for gravatar
 *
 * @author sleroy
 */
public class GravatarModel {

	private static final String GRAVATAR_URL = "http://www.gravatar.com/avatar/";

	String	email;
	String	gravatarKey;
	int		hsize;

	/**
	 * Instantiates a new gravatar model.
	 *
	 * @param model the model
	 * @param _hSize the _h size
	 */
	public GravatarModel(final String model, final int _hSize) {

		email = model;
		gravatarKey = new Md5PasswordEncoder().encodePassword(email, null);
		hsize = _hSize;
	}

	/**
	 * Gets the object.
	 *
	 * @return the object
	 */
	public String getObject() {

		final StringBuilder sb = new StringBuilder();
		sb.append(GRAVATAR_URL);
		sb.append(gravatarKey);
		sb.append("?s=");
		sb.append(hsize);
		return sb.toString();
	}
}
