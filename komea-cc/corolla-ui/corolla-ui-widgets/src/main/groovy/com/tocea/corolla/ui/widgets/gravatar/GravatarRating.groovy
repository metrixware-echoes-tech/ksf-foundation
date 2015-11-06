package com.tocea.corolla.ui.widgets.gravatar

public enum GravatarRating {

	GENERAL_AUDIENCES("g"),

	PARENTAL_GUIDANCE_SUGGESTED("pg"),

	RESTRICTED("r"),

	XPLICIT("x")

	String code

	private GravatarRating(String code) {
		this.code = code
	}



}