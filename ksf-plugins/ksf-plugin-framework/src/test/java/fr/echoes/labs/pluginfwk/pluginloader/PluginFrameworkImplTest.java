package fr.echoes.labs.pluginfwk.pluginloader;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import fr.echoes.labs.pluginfwk.api.plugin.PluginFramework;
import fr.echoes.labs.pluginfwk.extensions.java.JavaClasspathPluginScanner;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkImpl;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class PluginFrameworkImplTest {

	@Spy
	private PluginFrameworkConfiguration	pluginFrameworkConfiguration;
	@InjectMocks
	private PluginFramework					pluginFrameworkImpl;

	@Test
	public void testDeclarePluginScanner() throws Exception {
		this.pluginFrameworkConfiguration.setExtraLibsFolder(new File("src/test/resources/extraLibs"));
		try (final PluginFramework pluginFrameworkImpl = new PluginFrameworkImpl(this.pluginFrameworkConfiguration)) {
			pluginFrameworkImpl.declarePluginScanner(new JavaClasspathPluginScanner());
			pluginFrameworkImpl.reloadPlugins();
		} finally {

		}

	}

}
