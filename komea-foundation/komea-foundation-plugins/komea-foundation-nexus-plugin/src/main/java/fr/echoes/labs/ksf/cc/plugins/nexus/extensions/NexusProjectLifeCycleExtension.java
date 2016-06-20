package fr.echoes.labs.ksf.cc.plugins.nexus.extensions;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.cc.plugins.nexus.services.NexusConfigurationService;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.NotifyResult;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;
import fr.echoes.labs.pluginfwk.api.plugin.Extension;

@Extension
public class NexusProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger			LOGGER	= LoggerFactory.getLogger(NexusProjectLifeCycleExtension.class);

	@Autowired
	private ApplicationContext			applicationContext;

	@Autowired
	private NexusErrorHandlingService	errorHandler;

	@Autowired
	private NexusConfigurationService	config;

	private ICurrentUserService			currentUserService;

	public void init() {
		if (this.currentUserService == null) {
			this.currentUserService = this.applicationContext.getBean(ICurrentUserService.class);
		}
	}

	@Override
	public NotifyResult notifyCreatedFeature(final ProjectDto project, final String featureId, final String featureSubject, final String username) {
		// Do nothing
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedProject(final ProjectDto project) {

		this.init();

		final String logginName = this.currentUserService.getCurrentUserLogin();
		// SecurityContextHolder.getContext().getAuthentication().getName();

		if (StringUtils.isEmpty(logginName)) {
			LOGGER.error("[nexus] No user found. Aborting project creation in Foreman module");
			return NotifyResult.CONTINUE;
		}

		LOGGER.info("[nexus] project {} creation detected [demanded by: {}]", project.getKey(), logginName);

		try {
			final String username = this.config.getUsername();
			final String password = this.config.getPassword();

			final Client client = ClientBuilder.newClient().register(new Authenticator(username, password));

			final String projectName = project.getName();

			final WebTarget target = client.target(this.config.getUrl()).path("service/local/repositories");
			final Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_XML);

			final RepositoryData data = new RepositoryData().setId(ProjectUtils.createIdentifier(projectName)).setName(projectName).setProvider(
					RepositoryData.PROVIDER_MAVEN2).setProviderRole(RepositoryData.PROVIDER_ROLE_REPOSITORY).setFormat(
							RepositoryData.FORMAT_MAVEN2).setRepoType(RepositoryData.REPO_TYPE_HOSTED).setExposed(true).setWritePolicy(
									RepositoryData.WRITE_POLICY_ALLOW_WRITE).setBrowseable(true).setIndexable(true).setNotFoundCacheTTL(1440).setRepoPolicy(
											RepositoryData.REPO_POLICY_RELEASE).setDownloadRemoteIndexes(false);

			final Repository repository = new Repository().setData(data);
			final Response response = invocationBuilder.post(Entity.entity(repository, MediaType.APPLICATION_XML));
			if (response.getStatus() != HttpStatus.SC_CREATED) {
				LOGGER.error("[nexus] failed to create Nexus repositories status : " + response.getStatus());
			}

		} catch (final Exception e) {
			LOGGER.error("[nexus] project creation failed", e);
			this.errorHandler.registerError("Unable to create Nexus repositories.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedRelease(final ProjectDto project, final String releaseVersion, final String username) {
		// Do nothing
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDeletedProject(final ProjectDto _project) {

		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDuplicatedProject(final ProjectDto _project) {
		// Do nothing
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedFeature(final ProjectDto projectDto, final String featureId, final String featureSubject) {
		// Do nothing
		return NotifyResult.CONTINUE;

	}

	@Override
	public NotifyResult notifyFinishedRelease(final ProjectDto project, final String releaseName) {
		// Do nothing
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyUpdatedProject(final ProjectDto _project) {
		// Do nothing
		return NotifyResult.CONTINUE;
	}

}
