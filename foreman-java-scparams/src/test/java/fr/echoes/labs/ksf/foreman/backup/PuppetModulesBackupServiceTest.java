package fr.echoes.labs.ksf.foreman.backup;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.foreman.api.model.ForemanHost;
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;

public class PuppetModulesBackupServiceTest {

	private static final String STORAGE_PATH = "build/tmp/dataset";
	private static final String SOURCE_PATH = "src/test/resources/dataset/";
	
	private BackupStorage backupStorage;
	private PuppetModulesBackupService backupService;
	
	@Before
	public void setup() {

		new File(STORAGE_PATH).delete();
		new File(STORAGE_PATH).mkdirs();
		
		this.backupStorage = new BackupStorage(STORAGE_PATH);
		this.backupService = new PuppetModulesBackupService(this.backupStorage);
	}
	
	private void initDataset() throws IOException {
		FileUtils.copyDirectory(new File(SOURCE_PATH), new File(STORAGE_PATH));
	}
	
	@Test
	public void testReadHostGroups() throws IOException {
		
		// given
		initDataset();
		
		// when
		final Map<String, List<PuppetClass>> results = this.backupService.readHostGroups();
		
		// then
		Assert.assertEquals(1, results.size());
		
		// then
		Entry<String, List<PuppetClass>> entry = results.entrySet().iterator().next();
		Assert.assertEquals("Provision from foreman.ksf.local", entry.getKey());
		Assert.assertEquals(15, entry.getValue().size());
		
	}
	
	@Test
	public void testReadHostClasses() throws IOException {
		
		// given
		final ForemanHost host = new ForemanHost("1", "dashboard.ksf.local");
		
		initDataset();
				
		// when
		final List<PuppetClass> results = this.backupService.readHostClasses(host);
		
		// then
		Assert.assertEquals(6, results.size());
		
	}
	
	@Test
	public void testWriteHostGroups() throws IOException {
		
		// given
		final String hostGroup = "myHostGroup";
		final List<PuppetClass> puppetClasses = Lists.newArrayList(
				new PuppetClass(1, "module1:class1", "module1"),
				new PuppetClass(2, "module1:class2", "module1"),
				new PuppetClass(3, "module2:class1", "module")
		);
		
		// when
		this.backupService.writeHostGroupClasses(hostGroup, puppetClasses);
		
		// then
		Assert.assertTrue(new File(STORAGE_PATH+"/hostgroups").exists());
		Assert.assertTrue(new File(STORAGE_PATH+"/hostgroups/"+hostGroup+"-classes.csv").exists());		
	}
	
	@Test
	public void testWriteEmptyHostGroups() throws IOException {
		
		// given
		final String hostGroup = "myEmptyHostGroup";
		final List<PuppetClass> puppetClasses = Lists.newArrayList();
		
		// when
		this.backupService.writeHostGroupClasses(hostGroup, puppetClasses);
		
		// then
		Assert.assertFalse(new File(STORAGE_PATH+"/hostgroups/"+hostGroup+"-classes.csv").exists());	
		
	}
	
	@Test
	public void testWriteHost() throws IOException {
		
		// given
		final ForemanHost host = new ForemanHost("1", "my.host.local");
		
		// given
		final List<PuppetClass> puppetClasses = Lists.newArrayList(
				new PuppetClass(1, "module1:class1", "module1"),
				new PuppetClass(2, "module1:class2", "module1"),
				new PuppetClass(3, "module2:class1", "module")
		);
		
		// when
		this.backupService.writeHostClasses(host, puppetClasses);
		
		// then
		Assert.assertTrue(new File(STORAGE_PATH+"/hosts").exists());
		Assert.assertTrue(new File(STORAGE_PATH+"/hosts/"+host.getName()).exists());
		Assert.assertTrue(new File(STORAGE_PATH+"/hosts/"+host.getName()+"/"+host.getName()+"-classes.csv").exists());
	}
	
	@Test
	public void testWriteEmptyHost() throws IOException {
		
		// given
		final ForemanHost host = new ForemanHost("1", "my.empty.host.local");
		
		// given
		final List<PuppetClass> puppetClasses = Lists.newArrayList();
				
		// when
		this.backupService.writeHostClasses(host, puppetClasses);
		
		// then
		Assert.assertFalse(new File(STORAGE_PATH+"/hosts/"+host.getName()).exists());
		Assert.assertFalse(new File(STORAGE_PATH+"/hosts/"+host.getName()+"/"+host.getName()+"-classes.csv").exists());
		
	}
	
}
