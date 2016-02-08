package fr.echoes.labs.ksf.cc.plugins.foreman;


import fr.echoes.labs.foremanclient.Modules;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by jguidoux on 19/11/15.
 */
public class JsonMappingTest {

	@Test
	public void testMappginJson() throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		Modules modules = mapper.readValue(new File("src/test/resources/over.json"), Modules.class);

		Assert.assertNotNull(modules);
		Assert.assertEquals(13, modules.modules.size());
	}
	
}
