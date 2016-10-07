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

public class JenkinsTemplateServiceTest {
	
	private static final String TMP_FOLDER = "build/tmp/templates/";

	@Mock
	private JenkinsConfigurationService configuration;
	
	private JenkinsTemplateService service;
	
	@Before
	public void setup() throws IOException {
		
		MockitoAnnotations.initMocks(this);
		this.service = new JenkinsTemplateService(this.configuration);
		
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
		
		final String defaultTemplate = getResourceContent("templates/job.xml");
		
		String content = service.getTemplate("job_demo.xml");
		Assert.assertEquals(getResourceContent("templates/job_demo.xml"), content);
		
		content = service.getTemplate("job.xml");
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
	
	private static String getResourceContent(final String filePath) throws IOException {
		
		return Resources.toString(Resources.getResource(filePath), Charsets.UTF_8);
	}
	
}
