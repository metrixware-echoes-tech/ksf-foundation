package fr.echoes.labs.komea.foundation.plugins.git.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.DeleteBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.CannotDeleteCurrentBranchException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidMergeHeadsException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.NotMergedException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import fr.echoes.labs.komea.foundation.plugins.git.GitExtensionException;
import fr.echoes.labs.komea.foundation.plugins.git.GitExtensionMergeException;



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
			git = callCloneCommand(gitProjectUri, workingDirectory);

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
			git = callCloneCommand(gitProjectUri, workingDirectory);

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

	private Git callCloneCommand(final String gitProjectUri, File workingDirectory)
			throws GitAPIException, InvalidRemoteException, TransportException {


		final CloneCommand cloneCommand = Git.cloneRepository().setURI(gitProjectUri).setDirectory(workingDirectory);

//		configureSshConnection(cloneCommand);
//
//		final String username = this.configuration.getUsername();
//		final String password = this.configuration.getPassword();
//
//		if (!StringUtils.isBlank(username) && !StringUtils.isBlank(password)) {
//			final UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
//			cloneCommand.setCredentialsProvider(credentialsProvider);
//		}

		return cloneCommand.call();
	}

	private void configureSshConnection(final CloneCommand cloneCommand) {

		LOGGER.info("[git] configure SSH connection");

		final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {

			@Override
			protected JSch createDefaultJSch(FS fs) throws JSchException {
				final JSch defaultJSch = super.createDefaultJSch(fs);
				final String sshPrivateKeyPath = GitService.this.configuration.getSshPrivateKeyPath();

				if (!StringUtils.isBlank(sshPrivateKeyPath)) {
					LOGGER.info("[git] Use SSH private key : {}", sshPrivateKeyPath);
					defaultJSch.addIdentity(sshPrivateKeyPath);
				}

				return defaultJSch;
			}

			@Override
			protected void configure(Host hc, Session session) {
				if (!GitService.this.configuration.isStrictHostKeyChecking()) { // true/yes is the default value so we don't need to change the configuration
					LOGGER.info("[git] StrictHostKeyChecking : no");
					session.setConfig("StrictHostKeyChecking", "no");
				}

			}
		};
		
		if (!GitService.this.configuration.isStrictHostKeyChecking()) { // true/yes is the default value so we don't need to change the configuration
			
		}
		
		
		cloneCommand.setTransportConfigCallback(new TransportConfigCallback() {

			@Override
			public void configure(Transport transport) {

				final SshTransport sshTransport = ( SshTransport )transport;
				sshTransport.setSshSessionFactory( sshSessionFactory );
				LOGGER.info("[git] cloneCommand.setTransportConfigCallback");
			}
		});
	}

	private MergeResult mergeBranches(Git git, String branch, String destinationBranch) throws IOException, NoHeadException, ConcurrentRefUpdateException, CheckoutConflictException, InvalidMergeHeadsException, WrongRepositoryStateException, NoMessageException, GitAPIException  {
		git.checkout()
		.setName(destinationBranch)
		.setCreateBranch(true)
		.setUpstreamMode(
				CreateBranchCommand.SetupUpstreamMode.TRACK)
				.setStartPoint(
						Constants.DEFAULT_REMOTE_NAME + "/" + destinationBranch)
						.call();
		
		final MergeCommand mergeCommand = git.merge();
		//refs/remotes/origin/feat-448-Evo1
		Map<String, Ref> allRefs = git.getRepository().getAllRefs();
		final Ref ref = git.getRepository().findRef(Constants.DEFAULT_REMOTE_NAME + "/" + branch);
		mergeCommand.include(ref);
		return mergeCommand.call();

	}


	private void deleteBranche(Git git, String branchName) throws NotMergedException, CannotDeleteCurrentBranchException, GitAPIException {
		DeleteBranchCommand branchDelete = git.branchDelete().setBranchNames(Constants.DEFAULT_REMOTE_NAME + "/" + branchName);
		branchDelete.call();
		
		RefSpec refSpec = new RefSpec().setSource(null).setDestination(Constants.DEFAULT_REMOTE_NAME + "/" + branchName);
		git.push().setRefSpecs(refSpec).setRemote(Constants.DEFAULT_REMOTE_NAME).call();
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
			git = callCloneCommand(gitProjectUri, workingDirectory);

			final MergeResult mergeResult = mergeBranches(git, branchName, DEVELOP);
			if (mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING)) {
				throw new GitExtensionMergeException("Cannot merge '"+ branchName +"' into " + DEVELOP);
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
