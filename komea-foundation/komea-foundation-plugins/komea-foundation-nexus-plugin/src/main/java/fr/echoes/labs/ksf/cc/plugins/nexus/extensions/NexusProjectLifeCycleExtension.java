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

import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.cc.plugins.nexus.services.NexusConfigurationService;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.NotifyResult;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;

@Extension
public class NexusProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final String MAVEN2 = "maven2";

	private static final Logger LOGGER = LoggerFactory.getLogger(NexusProjectLifeCycleExtension.class);

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private NexusErrorHandlingService errorHandler;

	@Autowired
	private NexusConfigurationService config;

	private ICurrentUserService currentUserService;

	public void init() {
		if (this.currentUserService == null) {
			this.currentUserService = this.applicationContext.getBean(ICurrentUserService.class);
		}
	}


	public static void main(String[] args) {
		try {
			final String username = "ksfuser";
			final String password = "490ebc83a2b4dc242a957551e082a1a6024259ef9676058e72fc8851ac1bf22b";

			final Client client = ClientBuilder.newClient()
                    .register(new Authenticator(username, password));



			final WebTarget target = client.target("https://ksf-demo.metrixware.local/nexus").path("service/local/repositories");
			final Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_XML);

			final RepositoryData data = new RepositoryData();
			data.setId("my-releases-xml5");
			data.setExposed(true);
			data.setFormat(MAVEN2);
			data.setName("MyReleasesXml 5");
			data.setProvider(MAVEN2);
			data.setProviderRole("org.sonatype.nexus.proxy.repository.Repository");
			data.setRepoPolicy("RELEASE");
			data.setRepoType("hosted");

			final Repository repository = new Repository();
			repository.setData(data);
			final Response response = invocationBuilder.post(Entity.entity(repository, MediaType.APPLICATION_XML));

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public NotifyResult notifyCreatedProject(ProjectDto project) {

		init();

		final String logginName = this.currentUserService.getCurrentUserLogin();
		//SecurityContextHolder.getContext().getAuthentication().getName();

		if (StringUtils.isEmpty(logginName)) {
			LOGGER.error("[nexus] No user found. Aborting project creation in Foreman module");
			return NotifyResult.CONTINUE;
		}

		LOGGER.info("[nexus] project {} creation detected [demanded by: {}]", project.getKey(), logginName);

		try {
			final String username = this.config.getUsername();
			final String password = this.config.getPassword();

			final Client client = ClientBuilder.newClient()
                    .register(new Authenticator(username, password));


			final String projectName = project.getName();

			final WebTarget target = client.target(this.config.getUrl()).path("service/local/repositories");
			final Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_XML);

			final RepositoryData data = new RepositoryData();
			data.setId(ProjectUtils.createIdentifier(projectName));
			data.setExposed(true);
			data.setFormat(MAVEN2);
			data.setProvider(MAVEN2);
			data.setName(projectName);
			data.setProviderRole("org.sonatype.nexus.proxy.repository.Repository");
			data.setRepoPolicy("RELEASE");
			data.setRepoType("hosted");

			final Repository repository = new Repository();
			repository.setData(data);
			final Response response = invocationBuilder.post(Entity.entity(repository, MediaType.APPLICATION_XML));
			final int status = response.getStatus();
			if (status != HttpStatus.SC_CREATED) {
				LOGGER.error("[nexus] failed to create Nexus repositories status : " + status);
			}

		} catch (final Exception e) {
			LOGGER.error("[nexus] project creation failed", e);
			this.errorHandler.registerError("Unable to create Nexus repositories.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDeletedProject(ProjectDto _project) {

        return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDuplicatedProject(ProjectDto _project) {
		// Do nothing
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyUpdatedProject(ProjectDto _project) {
		// Do nothing
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedRelease(ProjectDto project, String releaseVersion, String username) {
		// Do nothing
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedFeature(ProjectDto project, String featureId,
			String featureSubject, String username) {
		// Do nothing
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedRelease(ProjectDto project, String releaseName) {
		// Do nothing
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedFeature(ProjectDto projectDto, String featureId,
			String featureSubject) {
		// Do nothing
		return NotifyResult.CONTINUE;

	}

}
