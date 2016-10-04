package fr.echoes.labs.ksf.foreman.exceptions;

public class HostNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HostNotFoundException(final String hostName) {
		super("Cannot find host "+hostName);
	}
	
}
