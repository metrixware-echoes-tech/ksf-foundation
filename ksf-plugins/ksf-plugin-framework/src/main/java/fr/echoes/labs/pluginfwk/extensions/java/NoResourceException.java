package fr.echoes.labs.pluginfwk.extensions.java;

public class NoResourceException extends Exception {

	public NoResourceException(final String name) {
		super("Resource not found " + name);
	}

}
