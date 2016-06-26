package fr.echoes.labs.pluginfwk.extensions.java;

import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Test;

import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkConfiguration;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkImpl;

public class JavaPluginScannerTest {

	@Test
	public void testLoadPLugins() {
		final PluginFrameworkConfiguration pluginFrameworkConfiguration = new PluginFrameworkConfiguration();
		pluginFrameworkConfiguration.setTemporaryPluginFolder(new File("build"));
		pluginFrameworkConfiguration.setJavaPluginFolder(new File("src/test/resources/plugins"));
		final PluginPropertyStorage pluginStorage = mock(PluginPropertyStorage.class);
		final PluginFrameworkImpl pluginFrameworkImpl = new PluginFrameworkImpl(pluginFrameworkConfiguration, pluginStorage);
		pluginFrameworkImpl.declarePluginScanner(new JavaPluginScanner(pluginFrameworkConfiguration));
		pluginFrameworkImpl.reloadPlugins();
	}

}
