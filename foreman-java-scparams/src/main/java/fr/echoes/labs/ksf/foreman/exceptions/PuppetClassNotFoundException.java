package fr.echoes.labs.ksf.foreman.exceptions;

public class PuppetClassNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PuppetClassNotFoundException(final String puppetModule, final String puppetClass) {
		super("Cannot find Puppet class "+puppetClass+" of module "+puppetModule);
	}
	
}
