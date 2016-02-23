package fr.echoes.labs.komea.foundation.plugins.git.services;

import java.io.File;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jgitflow.core.JGitFlow;

import fr.echoes.labs.komea.foundation.plugins.git.GitExtensionException;



/**
 * Spring Service for working with the foreman API.
 *
 * @author dcollard
 *
 */
@Service
public class GitService implements IGitService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GitService.class);

	@Autowired
	private GitConfigurationService configuration;

	private final String buildFilename = "build.sh";


	@Override
	public void createProject(String projectName) throws GitExtensionException {
		Objects.requireNonNull(projectName);


		final String gitProjectUri = this.configuration.getScmUrl() + '/' + projectName + projectName + ".git";
		final File workingDirectory = new File(this.configuration.getGitWorkingdirectory());
		try {
			FileUtils.forceMkdir(workingDirectory);

			// Clone project
			LOGGER.debug("Cloning the repository {} into {}", gitProjectUri, workingDirectory);
			Git.cloneRepository().setURI(gitProjectUri).setDirectory(workingDirectory).call();

			// Create branch develop
			LOGGER.debug("Initializing Git Flow repository structure");
			final JGitFlow flow = JGitFlow.getOrInit(workingDirectory);

			// Create build file
			LOGGER.debug("Creating the file {}", this.buildFilename);
			final File buildScriptFile = new File(workingDirectory, this.buildFilename);
			FileUtils.writeStringToFile(buildScriptFile, "#!/bin/bash");

			final Git git = flow.git();

			// Commit to master
			LOGGER.debug("Committing the file to master");
			git.add().addFilepattern(buildScriptFile.getName()).call();
			git.commit().setMessage("Initial commit");

			// Commit to develop
			LOGGER.debug("Committing the file to develop");
			git.checkout().setName("develop").call();
			git.add().addFilepattern(buildScriptFile.getName()).call();
			git.commit().setMessage("Initial commit");

		} catch (final Exception e) {
			throw new GitExtensionException(e);
		}
	}


	@Override
	public void deleteProject(String projectName) throws GitExtensionException {
		Objects.requireNonNull(projectName);
	}
}
