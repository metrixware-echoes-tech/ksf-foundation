package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dcollard
 *
 */
public class JenkinsBuildInfo {

	private final int number;
	private final String buildUrl;
	private final String time;

	JenkinsBuildInfo(int number, long timestamp, String buildUrl) {
		this.number = number;
		this.time = convert(timestamp);
		this.buildUrl = buildUrl;
	}

	private String convert(long timestamp) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
		return dateFormat.format(new Date(timestamp));
	}

	public int getNumber() {
		return this.number;
	}

	public String getTime() {
		return this.time;
	}

	public String getBuildUrl() {
		return this.buildUrl;
	}

}
