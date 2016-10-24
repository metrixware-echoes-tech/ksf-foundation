package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

public class JenkinsTemplateServiceTest {
	
	private static final String TMP_FOLDER = "build/tmp/templates/";
	private static final String DEFAULT_TEMPLATE = "jenkins_job_templates/job.xml";

	@Mock
	private JenkinsConfigurationService configuration;
	
	@Mock
	private JenkinsNameResolver nameResolver;
	
	private JenkinsTemplateService service;
	
	@Before
	public void setup() throws IOException {
		
		MockitoAnnotations.initMocks(this);
		this.service = new JenkinsTemplateService(this.configuration, this.nameResolver);
		
		final File tmpFolder = new File(TMP_FOLDER);
		FileUtils.deleteDirectory(tmpFolder);
		tmpFolder.mkdirs();
		FileUtils.writeStringToFile(new File(TMP_FOLDER+"job_test.xml"), "test");
		FileUtils.writeStringToFile(new File(TMP_FOLDER+"job.xml"), "test doublon");
		FileUtils.writeStringToFile(new File(TMP_FOLDER+"not_a_template"), "Nope");
	}
	
	@Test
	public void testGetAvailableTemplates() {
		
		// given
		final Collection<String> expected = Lists.newArrayList(
				"job.xml", "job_test.xml"
		);
		
		// mock
		Mockito.when(configuration.getTemplateFolder()).thenReturn(TMP_FOLDER);
		
		// when
		final Collection<String> templates = service.getAvailableTemplates();
		
		// then
		Assert.assertEquals(expected.size(), templates.size());
		Assert.assertTrue(templates.containsAll(expected));

	}
	
	@Test
	public void testGetTemplate() throws IOException {
		
		final String defaultTemplate = getResourceContent(DEFAULT_TEMPLATE);
		
		String content = service.getTemplate("job.xml");
		Assert.assertEquals(defaultTemplate, content);
		
		content = service.getTemplate("job_test.xml");
		Assert.assertEquals(defaultTemplate, content);
		
		Mockito.when(configuration.getTemplateFolder()).thenReturn(TMP_FOLDER);
		
		content = service.getTemplate("job.xml");
		Assert.assertEquals(FileUtils.readFileToString(new File(TMP_FOLDER+"job.xml")), content);
		
		content = service.getTemplate("job_test.xml");
		Assert.assertEquals(FileUtils.readFileToString(new File(TMP_FOLDER+"job_test.xml")), content);
		
		content = service.getTemplate("nope.xml");
		Assert.assertEquals(defaultTemplate, content);
		
		content = service.getTemplate(null);
		Assert.assertEquals(defaultTemplate, content);
		
	}
	
	@Test
	public void testCreateConfigXml() throws IOException {
		
		// given
		final ProjectDto project =  new ProjectDto();
		final String templateName = "job_demo.xml";
		final String branchName = "develop";
		
		// and
		final String scmURL = "ssh://git@localhost:6969/ksf-foundation.git";
		final String nexusRepo = "myNexusRepo";
		final String buildScript = "myBuildScript";
		final String publishScript = "myPublishScript";
		
		// mock
		Mockito.when(nameResolver.getProjectScmUrl(project)).thenReturn(scmURL);
		Mockito.when(nameResolver.getNexusRepositoryKey(project)).thenReturn(nexusRepo);
		Mockito.when(configuration.getBuildScript()).thenReturn(buildScript);
		Mockito.when(configuration.getPublishScript()).thenReturn(publishScript);
		
		// when
		final String result = service.createConfigXml(project, templateName, branchName);
		
		// then
		Assert.assertTrue(result.contains("<url>"+scmURL+"</url>"));
		Assert.assertTrue(result.contains("<name>"+branchName+"</name>"));
		Assert.assertTrue(result.contains("<version>"+branchName+"</version>"));
		Assert.assertTrue(result.contains("<repository>"+nexusRepo+"</repository>"));
		Assert.assertTrue(result.contains("./"+buildScript+"</command>"));
		Assert.assertTrue(result.contains("<filePath>"+publishScript+"</filePath>"));
	}
	
	private static String getResourceContent(final String filePath) throws IOException {
		
		return Resources.toString(Resources.getResource(filePath), Charsets.UTF_8);
	}
	
}
