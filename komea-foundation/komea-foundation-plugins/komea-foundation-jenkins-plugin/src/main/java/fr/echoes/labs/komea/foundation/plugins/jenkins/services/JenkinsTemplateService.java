package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsConfigurationBean;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.foundation.utils.URLUtils;

@Service("jenkinsTemplates")
public class JenkinsTemplateService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsTemplateService.class);
	
	private static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
	private static final String RESOURCES_TEMPLATE_FOLDER = "jenkins_job_templates/";
	private static final String DEFAULT_TEMPLATE = "job.xml";
	
	private final JenkinsConfigurationService configuration;
	private final JenkinsNameResolver nameResolver;
	
	@Autowired
	public JenkinsTemplateService(final JenkinsConfigurationService configuration, final JenkinsNameResolver nameResolver) {
		this.configuration = configuration;
		this.nameResolver = nameResolver;
	}
	
	private String getTemplateFolder() {
		return this.configuration.getConfigurationBean().getTemplateFolder();
	}
	
	/**
	 * Retrieves the list of all available template files.
	 * @return the names of the template files.
	 */
	public Collection<String> getAvailableTemplates() {
		
		final Set<String> templates = Sets.newHashSet(DEFAULT_TEMPLATE);	
		final String templateFolder = getTemplateFolder();
		
		// retrieve the templates in the provided folder
		if (!StringUtils.isEmpty(templateFolder)) {
			templates.addAll(extractTemplateNames(new File(templateFolder)));
		}
		
		return templates;
	}
	
	private static List<String> extractTemplateNames(final File directory) {
		
		final List<String> templates = Lists.newArrayList();
		
		if (directory.exists() && directory.isDirectory()) {
			final FilenameFilter filter = new FilenameFilter() {				
				@Override
				public boolean accept(File dir, String name) {
					LOGGER.info("found file {} in {}", name, dir.getPath());
					return name.toLowerCase().endsWith(".xml");
				}
			};
			for (final File templateFile : directory.listFiles(filter)) {
				templates.add(templateFile.getName());
			}
		}else{
			LOGGER.error("The template folder {} does not exist.", directory.getAbsolutePath());
		}
		
		return templates;
	}
	
	/**
	 * Retrieves the job xml configuration template associated with the given template name.
	 * @return the content of the xml configuration template to use for creating a new job
	 */
	public String getTemplate(final String templateName) {

		String template;
		
		if (!StringUtils.isEmpty(templateName)) {
		
			// look for the template in the provided folder
			template = findTemplateInFolder(templateName);
			if (template != null) {
				return template;
			}
			
			// look for the template in the resources folder
			template = findTemplateInResources(templateName);
			if (template != null) {
				return template;
			}
		
		}
		
		// use default template 'job.xml'
		return findTemplateInResources(DEFAULT_TEMPLATE);
	}
	
	private String findTemplateInFolder(final String templateName) {
		
		String path = null;
		final String templateFolder = getTemplateFolder();
		
		if (!StringUtils.isEmpty(templateFolder)) {
			try {
				path = URLUtils.addPath(templateFolder, templateName);
				return FileUtils.readFileToString(new File(path));
			} catch (IOException ex) {
				LOGGER.error("Error while reading file "+path, ex);
			}
		}
	
		return null;
	}
	
	private String findTemplateInResources(final String templateName) {
		
		URL url = null;
		
		try {
			url = Resources.getResource(RESOURCES_TEMPLATE_FOLDER+templateName);
			LOGGER.info("Using Jenkins job template: {}", url.toString());
			return  Resources.toString(url, DEFAULT_CHARSET);
		} catch (final IllegalArgumentException ex) {
			LOGGER.error("Cannot find template "+templateName+" in resources folder.", ex);
		} catch (final IOException ex) {
			LOGGER.error("Cannot extract content of template "+url.getPath(), ex);
		}
	
		return null;
	}
	
	
	/**
	 * Builds the xml configuration of a Jenkins job.
	 * @param displayName the display name of the job
	 * @param scmUrl the url of a git repository
	 * @param branchName the branch of the git repository that will be build
	 * @return a String containing the xml configuration of the Jenkins job.
	 * @throws IOException if the xml configuration cannot be generated
	 */
	public String createConfigXml(final ProjectDto project, final String templateName, String branchName) throws IOException {

		final JenkinsConfigurationBean config = this.configuration.getConfigurationBean();
        final Map<String, String> variables = Maps.newHashMap();

        variables.put("scmUrl", this.nameResolver.getProjectScmUrl(project));
        variables.put("displayName", this.nameResolver.getDisplayName(project.getName(), branchName));
        variables.put("branchName", branchName);
        variables.put("buildScript", config.getBuildScript());
        variables.put("publishScript", config.getPublishScript());
        variables.put("scmRepositoryKey", this.nameResolver.getScmRepositoryName(project));
        variables.put("nexusRepositoryKey", this.nameResolver.getNexusRepositoryKey(project));

        return substituteText(getTemplate(templateName), variables);
    }

    /**
     * Replaces all the occurrences of variables with their matching values.
     * @param templateXml a String containing a XML template
     * @param variables the map with the variables' values, can be null.
     * @return a String containing the XML template with the replaced values.
     * @throws IOException
     */
    private static String substituteText(final String templateXml, final Map<String, String> variables) throws IOException {
        final StrSubstitutor sub = new StrSubstitutor(variables);
        final String resolvedXml = sub.replace(templateXml);
        return resolvedXml;
    }
	
}
