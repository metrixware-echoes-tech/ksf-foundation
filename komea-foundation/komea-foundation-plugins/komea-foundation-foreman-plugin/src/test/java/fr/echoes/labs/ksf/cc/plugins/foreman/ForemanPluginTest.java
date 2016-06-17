package fr.echoes.labs.ksf.cc.plugins.foreman;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanDAOExtension;
import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanProjectLifeCycleExtension;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;

@RunWith(MockitoJUnitRunner.class)
public class ForemanPluginTest {

	private final ForemanDAOExtension				foremanDAOExtension;
	private final ForemanProjectDashboardExtension	foremanProjectDashboardExtension;
	private final ForemanProjectLifeCycleExtension	foremanProjectLifeCycleExtension;
	private final ForemanPlugin						foremanPlugin	= new ForemanPlugin();

	@Test
	public void testGetExtensions() throws Exception {

		try {
			this.foremanPlugin.init();
			final IExtension[] extensions = this.foremanPlugin.getExtensions();
			System.out.println(extensions);
			assertEquals(2, extensions.length);
		} finally {
			this.foremanPlugin.destroy();
		}
	}

}
