package fr.echoes.labs.ksf.cc.plugins.foreman;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import fr.echoes.labs.pluginfwk.api.extension.Extension;

public class ForemanPluginTest {

	@Test
	public void testGetExtensions() throws Exception {
		final ForemanPlugin foremanPlugin = new ForemanPlugin();
		final List<Extension> extensions = Lists.newArrayList(foremanPlugin.getExtensions());
		System.out.println(extensions);
		assertEquals(2, extensions.size());
	}

}
