package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;



/**
 * Spring Service for working with the foreman API.
 *
 * @author dcollard
 *
 */
@Service
public class JenkinsService implements IJenkinsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsService.class);

	@Autowired
	private JenkinsConfigurationService configurationService;

	@Override
	public void createProject(String projectName)
			throws JenkinsExtensionException {
		JenkinsHttpClient client;
		try {
			client = new JenkinsHttpClient(new URI(configurationService.getUrl()));

			final String resolvedXmlConfig = createXmlConfig(projectName);

			client.post_xml("/createItem?name=" + projectName, resolvedXmlConfig, false);
		} catch (final URISyntaxException e) {
			LOGGER.error("Failed to create Jenkins job", e);
		} catch (final IOException e) {
			LOGGER.error("Failed to create Jenkins job", e);
		}
	}

	private String createXmlConfig(String projectName) throws IOException {
		final URL url = com.google.common.io.Resources.getResource(this.configurationService.getTemplateName());

		final String templateXml = Resources.toString(url, Charsets.UTF_8);

		final Map<String, String> variables = new HashMap<String, String>();

		final String scmUrl = this.configurationService.getScmUrl() + '/' + projectName + ".git";

		variables.put("scmUrl", scmUrl);

		final StrSubstitutor sub = new StrSubstitutor(variables);
		final String resolvedXml = sub.replace(templateXml);
		return resolvedXml;
	}

	@Override
	public void deleteProject(String projectName)
			throws JenkinsExtensionException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<JenkinsBuildInfo> getBuildInfo(String projectName)
			throws JenkinsExtensionException {

		return null;
	}
}
