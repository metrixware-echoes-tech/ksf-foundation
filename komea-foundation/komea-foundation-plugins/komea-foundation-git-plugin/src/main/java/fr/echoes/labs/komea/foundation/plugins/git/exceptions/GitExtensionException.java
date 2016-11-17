package fr.echoes.labs.komea.foundation.plugins.git.exceptions;

/**
 * @author dcollard
 *
 */
public class GitExtensionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6364259683604481173L;

	public GitExtensionException(Exception e) {
		super(e);
	}

	public GitExtensionException(String message) {
		super(message);
	}

	public GitExtensionException(String message, Exception e) {
		super(message, e);
	}
}
