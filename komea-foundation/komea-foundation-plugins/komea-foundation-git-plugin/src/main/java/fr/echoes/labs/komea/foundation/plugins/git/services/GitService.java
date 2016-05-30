package fr.echoes.labs.komea.foundation.plugins.git.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.CannotDeleteCurrentBranchException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidMergeHeadsException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.NotMergedException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
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

	private static final String DEVELOP = "develop";

	private static final Logger LOGGER = LoggerFactory.getLogger(GitService.class);

	@Autowired
	private GitConfigurationService configuration;

	@Override
	public void createProject(String projectName) throws GitExtensionException {
		Objects.requireNonNull(projectName);

		final String gitProjectUri = getProjectScmUrl(projectName);

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
			} catch (final IOException e) {
				LOGGER.warn("Failed to delete the directory " + workingDirectory.getName(), e);
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

	private void createBranch(String projectName, String branchName) throws GitExtensionException {
		Objects.requireNonNull(projectName);

		final String gitProjectUri = getProjectScmUrl(projectName);

		LOGGER.info("Creating branch {} for project {}", branchName, projectName);

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

			git.branchCreate().setName(branchName).call();
			git.push().add(branchName).call();


		} catch (final Exception e) {
			LOGGER.error("Failed to create branch " + branchName + " for project " + projectName, e);
			throw new GitExtensionException(e);
		} finally {
			if (git != null) {
				git.close();
			}
			try {
				// Delete Git local repository
				FileUtils.deleteDirectory(workingDirectory);
			} catch (final IOException e) {
				LOGGER.warn("Failed to delete the directory " + workingDirectory.getName(), e);
			}
		}
	}

	private MergeResult mergeBranches(Git git, String originBranch, String destinationBranch) throws IOException, NoHeadException, ConcurrentRefUpdateException, CheckoutConflictException, InvalidMergeHeadsException, WrongRepositoryStateException, NoMessageException, GitAPIException  {

		final MergeCommand mergeCommand = git.merge();
		final Ref ref = git.getRepository().exactRef(originBranch);
		mergeCommand.include(ref);
		return mergeCommand.call();

	}


	private void deleteBranche(Git git, String branchName) throws NotMergedException, CannotDeleteCurrentBranchException, GitAPIException {
		git.branchDelete().setBranchNames(branchName).call();
	}


	@Override
	public void createRelease(String projectName, String releaseVersion) throws GitExtensionException {
		final String branchName = getReleaseBranchName(projectName, releaseVersion);
		createBranch(projectName, branchName);
	}

	@Override
	public void createFeature(String projectName, String featureId, String featureSubject) throws GitExtensionException {
		final String branchName = getFeatureBranchName(projectName, featureId, featureSubject);
		createBranch(projectName, branchName);
	}

	@Override
	public void closeFeature(String projectName, String featureId,
			String featureSubject) throws GitExtensionException {

		final String gitProjectUri = getProjectScmUrl(projectName);
		final String branchName = getFeatureBranchName(projectName, featureId, featureSubject);

		File workingDirectory = null;
		Git git = null;

		try {
			workingDirectory = createCloneDestinationDirectory(projectName);

			// Clone project
			LOGGER.debug("Cloning the repository {} into {}", gitProjectUri, workingDirectory);
			git = Git.cloneRepository().setURI(gitProjectUri).setDirectory(workingDirectory).call();

			git.checkout()
			.setName(branchName)
			.setCreateBranch(true)
			.setUpstreamMode(
					CreateBranchCommand.SetupUpstreamMode.TRACK)
					.setStartPoint(
							Constants.DEFAULT_REMOTE_NAME + "/" + branchName)
							.call();

			final MergeResult mergeResult = mergeBranches(git, branchName, DEVELOP);
			if (mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING)) {

			} else {
				deleteBranche(git, branchName);
			}


		} catch (final Exception e) {
			throw new GitExtensionException(e);
		} finally {
			if (git != null) {
				git.close();
			}
			try {
				// Delete Git local repository
				FileUtils.deleteDirectory(workingDirectory);
			} catch (final IOException e) {
				LOGGER.warn("Failed to delete the directory " + workingDirectory.getName(), e);
			}
		}
	}

	private String getProjectScmUrl(String projectName) {
		final Map<String, String> variables = new HashMap<String, String>(2);
		variables.put("scmUrl", this.configuration.getScmUrl());
		variables.put("projectName", projectName);
		return replaceVariables(this.configuration.getProjectScmUrlPattern(), variables);
	}
	private String getReleaseBranchName(String projectName, String releaseVersion) {
		final Map<String, String> variables = new HashMap<String, String>(1);
		variables.put("releaseVersion", releaseVersion);
		return replaceVariables(this.configuration.getBranchReleasePattern(), variables);
	}

	private String getFeatureBranchName(String projectName, String featureId, String featureDescription) {
		final Map<String, String> variables = new HashMap<String, String>(2);
		variables.put("featureId", featureId);
		variables.put("featureDescription", featureDescription);
		return replaceVariables(this.configuration.getBranchFeaturePattern(), variables);
	}

	@Override
	public void closeRelease(String projectName, String releaseName)
			throws GitExtensionException {


	}

	private String replaceVariables(String str, Map<String, String> variables) {
		final StrSubstitutor sub = new StrSubstitutor(variables);
		sub.setVariablePrefix("%{");
		return sub.replace(str);
	}
}
