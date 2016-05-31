package fr.echoes.labs.ksf.cc.extensions.services.project;


/**
 * @author dcollard
 *
 */
public class ValidatorResult implements IValidatorResult {

	private final String message;
	private final ValidatorResultType type;


	public ValidatorResult(ValidatorResultType type, String message) {
		this.type = type;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public ValidatorResultType getType() {
		return this.type;
	}

}
