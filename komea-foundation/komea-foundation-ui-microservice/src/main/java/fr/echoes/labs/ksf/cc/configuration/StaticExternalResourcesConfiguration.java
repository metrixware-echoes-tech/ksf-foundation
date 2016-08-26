package fr.echoes.labs.ksf.cc.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

/**
 * This configuration class allows to serve static content located in external directories.
 *
 * @author dcollard
 *
 */
@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class StaticExternalResourcesConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticExternalResourcesConfiguration.class);

	@Value("${ksf.resources.staticLocations:file:/opt/echoes/foundation/resources/}")
	private String[] resourcesStaticLocations;


	@Value("${ksf.resources.staticLocationsPathPatterns:/externalResources/**}")
	private String[] resourcesStaticLocationsPathPatterns;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		final String[] resourcesStaticLocations = this.resourcesStaticLocations;
		final String[] resourcesStaticLocationsPathPatterns = this.resourcesStaticLocationsPathPatterns;

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Resources static locations='{}'", String.join(",",resourcesStaticLocations));
			LOGGER.debug("Resources static locations path patterns='{}'", String.join(",",resourcesStaticLocationsPathPatterns));
		}

		registry.addResourceHandler(resourcesStaticLocationsPathPatterns).addResourceLocations(resourcesStaticLocations);

		super.addResourceHandlers(registry);
	}

}