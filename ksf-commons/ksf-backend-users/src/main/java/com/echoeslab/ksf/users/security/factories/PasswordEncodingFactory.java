/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.echoeslab.ksf.users.security.factories;



import com.echoeslab.ksf.users.security.config.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * This class defines Beans required to manipulate passwords inside the komea software factory.
 * @author sleroy
 *
 */
@Configuration
public class PasswordEncodingFactory {
	@Autowired
	private SecurityConfiguration		security;

	@Bean
	public PasswordEncoder getPasswordStrengthwordEncoderBean() {
		return new BCryptPasswordEncoder(this.security.getPasswordStrength());
	}

}
