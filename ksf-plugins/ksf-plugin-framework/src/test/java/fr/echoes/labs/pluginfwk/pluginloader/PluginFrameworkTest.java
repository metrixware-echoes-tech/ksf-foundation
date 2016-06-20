package fr.echoes.labs.pluginfwk.pluginloader;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import fr.echoes.labs.pluginfwk.extensions.java.JavaClasspathPluginScanner;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFramework;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class PluginFrameworkTest {

	@Spy
	private PluginFrameworkConfiguration	pluginFrameworkConfiguration;
	@InjectMocks
	private PluginFramework					pluginFramework;

	@Test
	public void testDeclarePluginScanner() throws Exception {
		this.pluginFrameworkConfiguration.setExtraLibsFolder(new File("src/test/resources/extraLibs"));
		try (final PluginFramework pluginFramework = new PluginFramework(this.pluginFrameworkConfiguration)) {
			pluginFramework.declarePluginScanner(new JavaClasspathPluginScanner());
			pluginFramework.reloadPlugins();
		} finally {

		}

	}

}
