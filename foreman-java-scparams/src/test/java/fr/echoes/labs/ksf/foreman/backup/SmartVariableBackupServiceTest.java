package fr.echoes.labs.ksf.foreman.backup;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.echoes.labs.ksf.foreman.api.model.ForemanHost;
import fr.echoes.labs.ksf.foreman.api.model.SmartVariableWrapper;

public class SmartVariableBackupServiceTest extends AbstractBackupStorageTest {

	private SmartVariableBackupService service;
	
	@Before
	public void setup() throws IOException {			
		super.setup();
		this.service = new SmartVariableBackupService(this.backupStorage);
	}
	
	@Test
	public void testReadGlobalValues() throws IOException {
		
		// given
		initDataset();
		
		// when
		final List<SmartVariableWrapper> values = service.read();
		
		// then
		Assert.assertNotNull(values);
		Assert.assertEquals(5, values.size());
		
		// then
		final SmartVariableWrapper var = values.get(0);
		Assert.assertEquals("/home/ksfuser/.ssh/config", var.getVariable()); 
		Assert.assertEquals("string", var.getType());
	}
	
	@Test
	public void testReadHostGroupValues() throws IOException {
		
		// given
		initDataset();
		
		// when
		final List<SmartVariableWrapper> values = service.readHostGroupValues("Provision from foreman.ksf.local");
		
		// then
		Assert.assertNotNull(values);
		Assert.assertEquals(3, values.size());
		
		// then
		final SmartVariableWrapper var = values.get(0);
		Assert.assertEquals("/home/ksfuser/.ssh/config", var.getVariable());
		Assert.assertEquals("string", var.getType());
	}
	
	@Test
	public void testReadHostValues() throws IOException {
		
		// given
		initDataset();
		
		// and
		final ForemanHost host = new ForemanHost("1", "dashboard.ksf.local");
		
		// when
		final List<SmartVariableWrapper> values = service.readHostValues(host);
		
		// then
		Assert.assertNotNull(values);
		Assert.assertEquals(2, values.size());	
	}
}
