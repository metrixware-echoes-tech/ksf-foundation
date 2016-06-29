package fr.echoes.labs.ksf.cc.pluginmanager;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;
import fr.echoes.labs.pluginfwk.extensions.java.KSFURLClassLoader;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkConfiguration;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkImpl;

@RunWith(MockitoJUnitRunner.class)
public class SpringPluginScannerTest {

	@Mock
	private ApplicationContext applicationContext;

	@Test
	public void testLoadPLugins() {

		final PluginFrameworkConfiguration pluginFrameworkConfiguration = this.initConfiguration();

		final PluginPropertyStorage pluginPropertyStorage = mock(PluginPropertyStorage.class);
		final PluginFrameworkImpl pluginFrameworkImpl = new PluginFrameworkImpl(pluginFrameworkConfiguration, pluginPropertyStorage);
		pluginFrameworkImpl.declarePluginScanner(new SpringPluginScanner(pluginFrameworkConfiguration, this.applicationContext));
		pluginFrameworkImpl.reloadPlugins();
	}

	@Test
	public void testScanner() {
		final SpringPluginScanner springPluginScanner = new SpringPluginScanner(this.initConfiguration(), this.applicationContext);
		try {
			springPluginScanner.initSpringEnv(new KSFURLClassLoader(Thread.currentThread().getContextClassLoader(), new URL[] {}));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

	}

	private PluginFrameworkConfiguration initConfiguration() {
		final PluginFrameworkConfiguration pluginFrameworkConfiguration = new PluginFrameworkConfiguration();
		pluginFrameworkConfiguration.setTemporaryPluginFolder(new File("build"));
		pluginFrameworkConfiguration.setSpringPluginFolder(new File("src/test/resources/springplugins"));
		return pluginFrameworkConfiguration;
	}
}
