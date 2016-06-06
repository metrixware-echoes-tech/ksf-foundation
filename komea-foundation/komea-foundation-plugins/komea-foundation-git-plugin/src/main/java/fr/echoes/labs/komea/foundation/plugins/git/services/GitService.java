package fr.echoes.labs.komea.foundation.plugins.git.services;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.DeleteBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.CannotDeleteCurrentBranchException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidMergeHeadsException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.NotMergedException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.jcraft.jsch.Session;

import fr.echoes.labs.komea.foundation.plugins.git.GitExtensionException;
import fr.echoes.labs.komea.foundation.plugins.git.GitExtensionMergeException;
import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;



/**
 * Spring Service for working with the foreman API.
 *
 * @author dcollard
 *
 */
@Service
public class GitService implements IGitService {

	private static final String DEVELOP = "develop";
	private static final String MASTER = "master";

	private static final Logger LOGGER = LoggerFactory.getLogger(GitService.class);

	@Autowired
	private GitConfigurationService configuration;

	@Override
	public void createProject(ProjectDto project) throws GitExtensionException {
		
		String projectName = project.getName();
		
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
			
			// Insert Git data in the Project object
			project.getOtherAttributes().put(ProjectExtensionConstants.GIT_URL, gitProjectUri);
			project.getOtherAttributes().put(ProjectExtensionConstants.ANALYZED_BRANCHES, Lists.newArrayList(DEVELOP));

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

		configureSshConnection(cloneCommand);
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

		if (!GitService.this.configuration.isStrictHostKeyChecking()) { // true/yes is the default value so we don't need to change the configuration

            SshSessionFactory. setInstance(new JschConfigSessionFactory() {


            	@Override
            	protected void configure(Host hc, Session session) {
            		LOGGER.info("[git] set StrictHostKeyChecking to 'no'");
            		session.setConfig("StrictHostKeyChecking" , "no");
            	}
            });
		}

//		final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
//
//			@Override
//			protected JSch createDefaultJSch(FS fs) throws JSchException {
//				final JSch defaultJSch = super.createDefaultJSch(fs);
//				final String sshPrivateKeyPath = GitService.this.configuration.getSshPrivateKeyPath();
//
//				if (!StringUtils.isBlank(sshPrivateKeyPath)) {
//					LOGGER.info("[git] Use SSH private key : {}", sshPrivateKeyPath);
//					defaultJSch.addIdentity(sshPrivateKeyPath);
//				}
//
//				return defaultJSch;
//			}
//
//			@Override
//			protected void configure(Host hc, Session session) {
//				if (!GitService.this.configuration.isStrictHostKeyChecking()) { // true/yes is the default value so we don't need to change the configuration
//					LOGGER.info("[git] StrictHostKeyChecking : no");
//					session.setConfig("StrictHostKeyChecking", "no");
//				}
//
//			}
//		};
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
		final Ref ref = git.getRepository().findRef(Constants.DEFAULT_REMOTE_NAME + "/" + branch);
		mergeCommand.include(ref);
		return mergeCommand.call();

	}


	private void deleteBranche(Git git, String branchName) throws NotMergedException, CannotDeleteCurrentBranchException, GitAPIException {

		// Delete local branch
		final DeleteBranchCommand branchDelete = git.branchDelete().setBranchNames(Constants.DEFAULT_REMOTE_NAME + "/" + branchName);
		branchDelete.call();

		// Delete remote branch
        final RefSpec deleteSpec = new RefSpec().setSource(null).setDestination(Constants.R_HEADS + branchName);
        git.push().setRemote(Constants.DEFAULT_REMOTE_NAME).setRefSpecs(deleteSpec).call();
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
		return createIdentifier(replaceVariables(this.configuration.getBranchReleasePattern(), variables));
	}

	private String getFeatureBranchName(String projectName, String featureId, String featureDescription) {
		final Map<String, String> variables = new HashMap<String, String>(2);
		variables.put("featureId", featureId);
		variables.put("featureDescription", featureDescription);
		return createIdentifier(replaceVariables(this.configuration.getBranchFeaturePattern(), variables));
	}

	@Override
	public void closeRelease(String projectName, String releaseName)
			throws GitExtensionException {
		final String gitProjectUri = getProjectScmUrl(projectName);
		final String branchName = getReleaseBranchName(projectName, releaseName);

		File workingDirectory = null;
		Git git = null;

		try {
			workingDirectory = createCloneDestinationDirectory(projectName);

			// Clone project
			LOGGER.debug("Cloning the repository {} into {}", gitProjectUri, workingDirectory);
			git = callCloneCommand(gitProjectUri, workingDirectory);

			tagBranch(git, branchName, releaseName);

			final MergeResult mergeIntoDevlopResult = mergeBranches(git, branchName, DEVELOP);
			if (mergeIntoDevlopResult.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING)) {
				throw new GitExtensionMergeException("Cannot merge '"+ branchName +"' into " + DEVELOP);
			}

			final MergeResult mergeIntoMasterResult = mergeBranches(git, branchName, MASTER);
			if (mergeIntoMasterResult.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING)) {
				throw new GitExtensionMergeException("Cannot merge '"+ branchName +"' into " + DEVELOP);
			} else {
				deleteBranche(git, branchName); // Both merges have succeeded we can delete the release branch
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

	private void checkout(Git git, String branchName) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		git.checkout().
        setCreateBranch(true).
        setName(branchName).
        setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
        setStartPoint("origin/" + branchName).
        call();
	}

	private void tagBranch(Git git, String branchName, String tagName) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		checkout(git, branchName);
       git.tag().setName(tagName).call();
       git.push().setPushTags().call();
	}

	private String createIdentifier(String projectName) {
		return  Normalizer.normalize(projectName, Normalizer.Form.NFD).replaceAll("[^\\dA-Za-z\\-]", "").replaceAll("\\s+","-" ).toLowerCase();
	}

	private String replaceVariables(String str, Map<String, String> variables) {
		final StrSubstitutor sub = new StrSubstitutor(variables);
		sub.setVariablePrefix("%{");
		return sub.replace(str);
	}

}
