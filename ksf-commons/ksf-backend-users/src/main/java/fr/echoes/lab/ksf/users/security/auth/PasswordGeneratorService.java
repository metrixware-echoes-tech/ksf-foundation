package fr.echoes.lab.ksf.users.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import fr.echoes.lab.ksf.users.security.api.PasswordGenerator;
import fr.echoes.lab.ksf.users.security.config.SecurityConfiguration;

/**
 * This service provides a method generate keys, salt and passwords.
 *
 * @author sleroy
 *
 */
@Service
public class PasswordGeneratorService implements PasswordGenerator {

	private final SecurityConfiguration securityConfiguration;

	@Autowired
	public PasswordGeneratorService(final SecurityConfiguration _securityConfiguration) {
		super();
		securityConfiguration = _securityConfiguration;
	}

	@Override
	public String generateKey(final int _length) {
		return new String(Hex.encode(KeyGenerators.secureRandom(_length).generateKey()));
	}

	@Override
	public String generatePassword() {
		return generateKey(securityConfiguration.getDefaultPasswordLength()/2);
	}

	@Override
	public String generateSalt() {
		return generateKey(securityConfiguration.getDefaultSaltLength()/2);
	}

}
