package fr.echoes.labs.util;


public class ExternalProcessLauncherException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 481602542906094098L;

	public ExternalProcessLauncherException(String message, Object ...args) {
		super(String.format(message, args));
	}


	public ExternalProcessLauncherException(Throwable tr) {
		super(tr);
	}
}
