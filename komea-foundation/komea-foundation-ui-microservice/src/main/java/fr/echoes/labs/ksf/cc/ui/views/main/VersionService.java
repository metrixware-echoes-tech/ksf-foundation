package fr.echoes.labs.ksf.cc.ui.views.main;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "versionService")
public class VersionService {

	private static final Logger	LOGGER	= LoggerFactory.getLogger(VersionService.class);

	private Properties			properties;

	public String getBuildTimestamp() {
		return this.properties.getProperty("buildTimestamp");
	}

	public String getBuildVersion() {
		return this.properties.getProperty("this.buildVersion");
	}

	@PostConstruct
	public void loadVersionProperties() {
		this.properties = new Properties();
		try {
			this.properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("version.properties"));
		} catch (final IOException e) {
			LOGGER.error("Could not load the version of Komea FOundation.");
		}
	}

}
