package fr.echoes.labs.komea.foundation.plugins.git.services;

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

}
