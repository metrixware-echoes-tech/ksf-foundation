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

}
