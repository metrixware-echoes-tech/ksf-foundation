package fr.echoes.labs.ksf.cc.plugins.foreman;

import static org.junit.Assert.assertEquals;

import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanDAOExtension;
import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanProjectLifeCycleExtension;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;

@RunWith(MockitoJUnitRunner.class)
public class ForemanPluginTest {

	@Mock
	private ForemanDAOExtension					foremanDAOExtension;
	@Mock
	private ForemanProjectDashboardExtension	foremanProjectDashboardExtension;
	@Mock
	private ForemanProjectLifeCycleExtension	foremanProjectLifeCycleExtension;

	@Test
	public void testGetExtensions() throws Exception {
		final ForemanPlugin foremanPlugin = new ForemanPlugin(this.foremanDAOExtension, this.foremanProjectDashboardExtension,
				this.foremanProjectLifeCycleExtension);
		try {
			foremanPlugin.init();
			final IExtension[] extensions = foremanPlugin.getExtensions();
			System.out.println(extensions);
			assertEquals(2, extensions.length);
		} finally {
			foremanPlugin.destroy();
		}
	}

}
