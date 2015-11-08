/**
 *
 */
package com.echoeslab.ksf.cc.ui.security

import com.echoeslab.ksf.cc.ui.configuration.SecurityConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder



/**
 * @author sleroy
 *
 */
@Configuration
public class PasswordEncodingConfigurationBean {
	@Autowired
	private SecurityConfiguration		security

	@Bean
	public PasswordEncoder getPasswordEncoderBean() {
		return new BCryptPasswordEncoder(this.security.getPasswordStrength())
	}

}
