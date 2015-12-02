package fr.echoes.lab.ksf.cc.plugins.foreman;

import org.springframework.stereotype.Service;

import com.tocea.corolla.utils.domain.ObjectValidation;


@Service("foremanValidation")
public class ForemanValidationConstants {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private ForemanValidationConstants () {
		// Do nothing
	}

	public static final int NAME_MIN_LENGTH = 3;

	public static final int NAME_MAX_LENGTH = 100;

	private static final String NAME_CANNOT_CONTAIN_SPECIAL_CHARACTERS = " name cannot contain special characters and must be between "+ NAME_MIN_LENGTH + " and " +  NAME_MAX_LENGTH + " characters in length.";

	public static final String ENVIRONMENT_NAME_PATTERN = ObjectValidation.HOSTNAME;

	public static final String TARGET_NAME_PATTERN = ObjectValidation.HOSTNAME;

	public static final String INCORRECT_ENVIRONMENT_NAME = "Environment" + NAME_CANNOT_CONTAIN_SPECIAL_CHARACTERS;

	public static final String INCORRECT_TARGET_NAME = "Target" + NAME_CANNOT_CONTAIN_SPECIAL_CHARACTERS;

}
