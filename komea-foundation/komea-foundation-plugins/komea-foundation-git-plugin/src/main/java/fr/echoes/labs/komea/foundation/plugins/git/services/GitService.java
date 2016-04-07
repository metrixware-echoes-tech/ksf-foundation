package fr.echoes.labs.komea.foundation.plugins.git.services;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.komea.foundation.plugins.git.GitExtensionException;



/**
 * Spring Service for working with the foreman API.
 *
 * @author dcollard
 *
 */
@Service
public class GitService implements IGitService {

	private static final String RELEASE = "release";
	private static final String DEVELOP = "develop";

	private static final Logger LOGGER = LoggerFactory.getLogger(GitService.class);

	@Autowired
	private GitConfigurationService configuration;

	@Override
	public void createProject(String projectName) throws GitExtensionException {
		Objects.requireNonNull(projectName);

		final String gitProjectUri = this.configuration.getScmUrl() + '/' + projectName + ".git";

		File workingDirectory = null;
		Git git = null;
		try {
			workingDirectory = createCloneDestinationDirectory(projectName);

			// Clone project
			LOGGER.debug("Cloning the repository {} into {}", gitProjectUri, workingDirectory);

			// Clone the repository
			git = Git.cloneRepository().setURI(gitProjectUri).setDirectory(workingDirectory).call();

			// Create the master and develop branches.
			// Add the build and publish scripts and push the modification to origin
			LOGGER.debug("Initializing the repository");
			initRepo(workingDirectory, git);

			// Delete the working directory
			LOGGER.debug("Deleting the working directory: {}", workingDirectory);

		} catch (final Exception e) {
			throw new GitExtensionException(e);
		} finally {
			if (git != null) {
				git.close();
			}
			try {
				FileUtils.deleteDirectory(workingDirectory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private File createCloneDestinationDirectory(final String projectName)
			throws GitExtensionException, IOException {

		final File workingDirectory = new File(this.configuration.getGitWorkingdirectory(), projectName);

		if (workingDirectory.exists()) {
			if (!workingDirectory.isDirectory()) {
				LOGGER.error("Cannot use {} as a Git working directory as it is not a directory.", workingDirectory.getPath());
				throw new GitExtensionException("Failed to create Git working directory.");
			}
		} else {
			FileUtils.forceMkdir(workingDirectory);
		}

		return workingDirectory;
	}

	/**
     * Initializes the repository by adding the build and publish script and creating the develop branch.
	 */
	private void initRepo(File workingDirectory, Git git) throws NoFilepatternException, IOException, GitAPIException {

		// Create build script
		addScriptToRepo(workingDirectory, git, this.configuration.getBuildScript());

		// Create publish script
		addScriptToRepo(workingDirectory, git, this.configuration.getPublishScript());

		LOGGER.debug("Committing the files {} and {}", this.configuration.getBuildScript(), this.configuration.getPublishScript());
		git.commit().setMessage("Initial commit").call();

		LOGGER.debug("Pushing to master");
		git.push().call();

		// Create develop branch
		LOGGER.debug("Creating the develop branch");
		git.branchCreate().setName(DEVELOP).call();
		git.push().add(DEVELOP).call();
	}

	/**
	 * Creates a shell script and add it to the staging area.
	 */
	private File addScriptToRepo(final File workingDirectory, final Git git, String scriptName)
			throws IOException, GitAPIException, NoFilepatternException {
		LOGGER.debug("Creating the file {}", scriptName);
		final File buildScriptFile = new File(workingDirectory, scriptName);
		FileUtils.writeStringToFile(buildScriptFile, "#!/bin/bash");
		git.add().addFilepattern(buildScriptFile.getName()).call();
		return buildScriptFile;
	}


	@Override
	public void deleteProject(String projectName) throws GitExtensionException {
		Objects.requireNonNull(projectName);
	}

	@Override
	public void createRelease(String projectName) throws GitExtensionException {
		Objects.requireNonNull(projectName);

		final String gitProjectUri = this.configuration.getScmUrl() + '/' + projectName + ".git";

		File workingDirectory = null;
		Git git = null;

		try {
			workingDirectory = createCloneDestinationDirectory(projectName);

			// Clone project
			LOGGER.debug("Cloning the repository {} into {}", gitProjectUri, workingDirectory);
			git = Git.cloneRepository().setURI(gitProjectUri).setDirectory(workingDirectory).call();

			git.checkout()
					.setName(DEVELOP)
					.setCreateBranch(true)
					.setUpstreamMode(
							CreateBranchCommand.SetupUpstreamMode.TRACK)
					.setStartPoint(
							Constants.DEFAULT_REMOTE_NAME + "/" + DEVELOP)
					.call();
			git.branchCreate().setName(RELEASE).call();
			git.push().add(RELEASE).call();


		} catch (final Exception e) {
			throw new GitExtensionException(e);
		} finally {
			if (git != null) {
				git.close();
			}
			try {
				// Delete Git local repository
				FileUtils.deleteDirectory(workingDirectory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
