package fr.echoes.labs.ksf.cc.extensions.services.project;

/**
 * @author dcollard
 *
 */
public interface IValidatorResult {

	/**
	 * @return the message.
	 */
	public String getMessage();

	/**
	 * @return the type of result.
	 */
	public ValidatorResultType getType();

}
