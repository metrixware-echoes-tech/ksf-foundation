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

	public String getScmUrl() {
		return this.scmUrl;
	}

	public String getGitWorkingdirectory() {
		return this.gitWorkingdirectory;
	}
}
