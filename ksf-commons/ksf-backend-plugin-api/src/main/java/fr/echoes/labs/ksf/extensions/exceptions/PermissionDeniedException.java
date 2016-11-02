package fr.echoes.labs.ksf.extensions.exceptions;

public class PermissionDeniedException extends Exception {

	private static final long serialVersionUID = -429040076332180377L;

	public PermissionDeniedException(final String message) {
		super(message);
	}
	
	public PermissionDeniedException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
}
