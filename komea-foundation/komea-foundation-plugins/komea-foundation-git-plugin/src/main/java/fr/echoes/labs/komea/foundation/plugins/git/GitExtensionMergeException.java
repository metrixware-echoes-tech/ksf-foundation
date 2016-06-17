package fr.echoes.labs.komea.foundation.plugins.git;

/**
 * @author dcollard
 *
 */
public class GitExtensionMergeException extends GitExtensionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5293445985568302339L;

	public GitExtensionMergeException(Exception e) {
		super(e);
	}
	
	public GitExtensionMergeException(String message) {
		super(message);
	}

	public GitExtensionMergeException(String message, Exception e) {
		super(message, e);
	}	


}
