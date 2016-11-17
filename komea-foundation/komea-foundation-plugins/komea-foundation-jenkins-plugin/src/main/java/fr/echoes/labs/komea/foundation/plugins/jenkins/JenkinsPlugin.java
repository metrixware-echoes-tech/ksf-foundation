package fr.echoes.labs.komea.foundation.plugins.jenkins;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.domain.ProjectCustomField;

import fr.echoes.labs.komea.foundation.plugins.jenkins.extensions.JenkinsProjectDashboardExtension;
import fr.echoes.labs.komea.foundation.plugins.jenkins.extensions.JenkinsProjectLifeCycleExtension;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsTemplateService;
import fr.echoes.labs.ksf.cc.extensions.services.ProjectCustomFieldService;
import fr.echoes.labs.ksf.extensions.annotations.Plugin;
import fr.echoes.labs.pluginfwk.api.extension.Extension;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

@Plugin
public class JenkinsPlugin implements PluginDefinition {

	public static final String ID = "jenkins";
	
	@Autowired
	private JenkinsProjectDashboardExtension projectDashboardExtension;
	
	@Autowired
	private JenkinsProjectLifeCycleExtension projectLifeCycleExtension;
	
	@Autowired
	private ProjectCustomFieldService projectCustomFieldService;
	
	@Autowired
	private JenkinsTemplateService templateService;
	
	@Autowired
    private MessageSource messageResource;
	
	@Override
	public String getDescription() {
		return "Jenkins plugin provides an integration of Jenkins with Komea Foundation";
	}

	@Override
	public Extension[] getExtensions() {
		return new Extension[] { this.projectDashboardExtension, this.projectLifeCycleExtension };
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getName() {
		return "Jenkins Plugin";
	}

	@Override
	public Object getPluginProperties() {
		return new JenkinsConfigurationBean();
	}

	@Override
	public void destroy() throws PluginException {
		// Nothing to do.
	}

	@Override
	public void init(final PluginPropertyStorage propertyStorage) throws PluginException {
		
		final ProjectCustomField templateField = new ProjectCustomField();
		templateField.setName("jobTemplate");
		templateField.setTitle(new MessageSourceAccessor(this.messageResource).getMessage("foundation.projects.newProjectForm.jobTemplate"));
		templateField.setType(String.class);
		for (final String template : this.templateService.getAvailableTemplates()) {
			templateField.addListValue(template);
		}
		
		// register the custom field
		this.projectCustomFieldService.register(templateField);
	}
	
}
