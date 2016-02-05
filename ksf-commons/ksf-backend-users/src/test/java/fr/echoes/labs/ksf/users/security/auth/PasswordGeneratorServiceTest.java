package fr.echoes.labs.ksf.users.security.auth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import fr.echoes.lab.ksf.users.security.config.SecurityConfiguration;



@RunWith(MockitoJUnitRunner.class)
public class PasswordGeneratorServiceTest {
	@Spy
	private final SecurityConfiguration	securityConfiguration = new SecurityConfiguration();
	@InjectMocks
	private PasswordGeneratorService	passwordGeneratorBean;

	@Test
	public final void testGenerateKey() throws Exception {
		final int strength = 5;
		assertEquals(strength*2, passwordGeneratorBean.generateKey(strength).length());
	}

	@Test
	public final void testGeneratePassword() throws Exception {
		final int strength = securityConfiguration.getDefaultPasswordLength();
		assertEquals(strength, passwordGeneratorBean.generatePassword().length());
	}

	@Test
	public final void testGenerateSalt() throws Exception {
		final int strength = securityConfiguration.getDefaultSaltLength();
		assertEquals(strength, passwordGeneratorBean.generateSalt().length());
	}


}
