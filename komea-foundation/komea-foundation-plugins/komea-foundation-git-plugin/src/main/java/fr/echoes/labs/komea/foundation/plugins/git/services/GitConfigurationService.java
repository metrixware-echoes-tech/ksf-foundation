package fr.echoes.labs.komea.foundation.plugins.git.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author dcollard
 *
 */
@Service("gitConfiguration")
public class GitConfigurationService {

	@Value("${ksf.scmUrl}")
	private String scmUrl;

	@Value("${ksf.git.workingDirectory}")
	private String gitWorkingdirectory;

	@Value("${ksf.buildSystem.defaultScript}")
	private String buildScript;

	@Value("${ksf.artifacts.publishScript}")
	private String publishScript;

	@Value("${ksf.git.branch.releasePattern}")
	private String branchReleasePattern;

	@Value("${ksf.git.branch.featurePattern}")
	private String branchFeaturePattern;

	@Value("${ksf.projectScmUrlPattern}")
	private String projectScmUrlPattern;

	@Value("${ksf.git.username:}")
	private String username;

	@Value("${ksf.git.password:}")
	private String password;

	@Value("${ksf.git.strictHostKeyChecking:true}")
	private boolean strictHostKeyChecking;

	@Value("${ksf.git.sshPrivateKeyPath:}")
	protected String sshPrivateKeyPath;

	@Value("${ksf.git.displayedUri:}")
	private String displayedUri;

	public String getScmUrl() {
		return this.scmUrl;
	}

	public String getGitWorkingdirectory() {
		return this.gitWorkingdirectory;
	}

	public String getBuildScript() {
		return this.buildScript;
	}

	public String getPublishScript() {
		return this.publishScript;
	}

	public String getBranchReleasePattern() {
		return this.branchReleasePattern;
	}

	public String getBranchFeaturePattern() {
		return this.branchFeaturePattern;
	}

	public String getProjectScmUrlPattern() {
		return this.projectScmUrlPattern;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @return the strictHostKeyChecking
	 */
	public boolean isStrictHostKeyChecking() {
		return this.strictHostKeyChecking;
	}

	/**
	 * @return the sshPrivateKeyPath
	 */
	public String getSshPrivateKeyPath() {
		return this.sshPrivateKeyPath;
	}

	/**
	 * @return the displayedUri
	 */
	public String getDisplayedUri() {
		return StringUtils.isBlank(this.displayedUri) ? getScmUrl() : this.displayedUri;
	}


}
