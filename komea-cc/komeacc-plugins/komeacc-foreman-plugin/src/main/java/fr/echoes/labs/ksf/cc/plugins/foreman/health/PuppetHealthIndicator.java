package fr.echoes.labs.ksf.cc.plugins.foreman.health;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import fr.echoes.labs.puppet.PuppetException;
import fr.echoes.labs.util.ExternalProcessLauncher;
import fr.echoes.labs.util.IProcessLaunchResult;

@Component
public class PuppetHealthIndicator extends AbstractHealthIndicator {

	private static final Logger LOGGER = LoggerFactory.getLogger(PuppetHealthIndicator.class);
	
	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		
		List<String> commandLine = Lists.newArrayList("puppet", "--version");
		
		final ExternalProcessLauncher processLauncher = new ExternalProcessLauncher(commandLine);
		
		try {
			
			final IProcessLaunchResult process = processLauncher.launchSync(true);
			
			if (process.getExitValue() != 0) {				
				throw new PuppetException("Puppet not installed");
			}
			
			builder.up();
			
		} catch (Exception ex) {
			
			LOGGER.error("[Puppet] Healt Indicator Error: ", ex);
			builder.down().withException(ex);
		}
		
	}

}
