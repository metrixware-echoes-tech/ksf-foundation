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
	private String jobName;
	private String result;
	private int duration;

	JenkinsBuildInfo(int number, String jobName, long timestamp, int duration, String buildUrl, String result) {
		this.number = number;
		this.jobName = jobName;
		this.time = convert(timestamp);
		this.buildUrl = buildUrl;
		this.duration = duration;
		this.result = result;
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

	public String getJobName() {
		return this.jobName;
	}

	public String getResult() {
		return result;
	}

	public int getDuration() {
		return duration;
	}

}
