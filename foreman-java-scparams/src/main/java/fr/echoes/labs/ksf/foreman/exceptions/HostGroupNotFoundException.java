package fr.echoes.labs.ksf.foreman.exceptions;

public class HostGroupNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HostGroupNotFoundException(final String hostGroupName) {
		super("Cannot find host group with name "+hostGroupName);
	}
	
}
