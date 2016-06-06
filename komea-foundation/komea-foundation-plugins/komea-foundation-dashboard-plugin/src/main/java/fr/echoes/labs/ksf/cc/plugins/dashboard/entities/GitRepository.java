package fr.echoes.labs.ksf.cc.plugins.dashboard.entities;

import java.util.List;

public class GitRepository {

	private String name;
	private String remoteURL;
	private String username;
	private String password;
	
	private List<String> includedBranches;

	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}
	

	public String getRemoteURL() {
		return remoteURL;
	}
	

	public void setRemoteURL(String remoteURL) {
		this.remoteURL = remoteURL;
	}
	

	public String getUsername() {
		return username;
	}
	

	public void setUsername(String username) {
		this.username = username;
	}
	

	public String getPassword() {
		return password;
	}
	

	public void setPassword(String password) {
		this.password = password;
	}
	

	public List<String> getIncludedBranches() {
		return includedBranches;
	}
	

	public void setIncludedBranches(List<String> includedBranches) {
		this.includedBranches = includedBranches;
	}
	
}
