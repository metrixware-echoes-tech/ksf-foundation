/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.labs.ksf.users.security.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.echoes.lab.ksf.users.security.config.SecurityConfiguration;
import fr.echoes.lab.ksf.users.security.utils.SecurityLoggers;

/**
 * This class defines Beans required to manipulate passwords inside the komea
 * software factory.
 *
 * @author sleroy
 *
 */
@Configuration
public class PasswordEncodingFactory {
	@Autowired
	private SecurityConfiguration security;
	

	@Bean
	public PasswordEncoder getPasswordStrengthwordEncoderBean() {
		SecurityLoggers.SECURITY_LOGGER.debug("Creation of the password encoder with the strength {}" ,security.getPasswordStrength());
		return new BCryptPasswordEncoder(security.getPasswordStrength());
	}

}
