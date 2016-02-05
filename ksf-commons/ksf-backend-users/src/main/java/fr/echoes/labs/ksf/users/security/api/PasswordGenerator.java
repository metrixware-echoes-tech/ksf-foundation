package fr.echoes.labs.ksf.users.security.api;

/**
 * This service provides methods to generate salt, key, passwords.
 * @author sleroy
 *
 */
public interface PasswordGenerator {
	/**
	 * Generates a new key of the given length.
	 *
	 * @param _length
	 *            the key length
	 * @return the key.
	 */
	String generateKey(int _length);
	
	/**
	 * Generates a new password
	 *
	 *
	 * @return the password. with default length
	 */
	String generatePassword();

	/**
	 * Generates a  new salt key
	 * @retur the salt with default length
	 */
	String generateSalt();
	
}
