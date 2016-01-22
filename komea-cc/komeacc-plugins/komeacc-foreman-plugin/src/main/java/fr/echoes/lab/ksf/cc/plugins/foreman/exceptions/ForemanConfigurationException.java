package fr.echoes.lab.ksf.cc.plugins.foreman.exceptions;

public class ForemanConfigurationException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = "[Foreman] Configuration Error: ";
	
	public ForemanConfigurationException(String message) {
		super(DEFAULT_MESSAGE+message);
	}
	
}
