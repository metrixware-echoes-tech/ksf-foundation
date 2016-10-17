package fr.echoes.labs.ksf.foreman;

import org.kohsuke.args4j.Option;

public class CommandLineOptions {

	@Option(name = "-foremanUrl", aliases = { "--foremanUrl" }, usage = "Specify the Foreman URL", required=true)
	private String foremanUrl;
	
	@Option(name = "-foremanUser", aliases = {"--foremanUser"}, usage = "Specify the Foreman user", required=true)
	private String foremanUser;
	
	@Option(name = "-foremanPassword", aliases = {"--foremanPassword"}, usage = "Specify the Foreman user's password", required=true)
	private String foremanPassword;
	
	@Option(name = "-backupFolder", aliases= {"--backupFolder"}, usage = "Specify the path of the backup folder", required=true)
	private String backupFolder;
	
	@Option(name = "-mode", aliases= {"--mode"}, usage = "Specify the running mode: backup, install", required=true)
	private String mode;

	public String getForemanUrl() {
		return foremanUrl;
	}	

	public void setForemanUrl(String foremanUrl) {
		this.foremanUrl = foremanUrl;
	}	

	public String getForemanUser() {
		return foremanUser;
	}	

	public void setForemanUser(String foremanUser) {
		this.foremanUser = foremanUser;
	}	

	public String getForemanPassword() {
		return foremanPassword;
	}

	public void setForemanPassword(String foremanPassword) {
		this.foremanPassword = foremanPassword;
	}

	public String getBackupFolder() {
		return backupFolder;
	}

	public void setBackupFolder(String backupFolder) {
		this.backupFolder = backupFolder;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
