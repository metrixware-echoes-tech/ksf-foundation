package fr.echoes.labs.ksf.foreman.exceptions;

public class SmartClassParameterNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SmartClassParameterNotFoundException(final String parameter, final String puppetClass) {
		super("Cannot find parameter "+parameter+" in puppet class "+puppetClass);
	}
	
}
