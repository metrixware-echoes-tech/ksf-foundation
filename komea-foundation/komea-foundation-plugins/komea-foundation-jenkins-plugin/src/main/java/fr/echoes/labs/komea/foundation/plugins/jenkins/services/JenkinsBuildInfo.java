package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

/**
 * @author dcollard
 *
 */
public class JenkinsBuildInfo {

	private long time;
	private String id;

	JenkinsBuildInfo(String id, long time) {
		this.id = id;
		this.time = time;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
