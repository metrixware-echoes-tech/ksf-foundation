package fr.echoes.lab.puppet;

public class PuppetException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -3998075153319348174L;


	public PuppetException(String message, Object ...args) {
		super(String.format(message, args));
	}


	public PuppetException(Throwable tr) {
		super(tr);
	}
}
