package fr.echoes.labs.ksf.foreman.backup;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class BackupStorageTest extends AbstractBackupStorageTest {
	
	@Before
	public void setup() throws IOException {
		super.setup();
	}
	
	@Test
	public void testReadHosts() throws IOException {
		
		// given
		initDataset();
		FileUtils.write(new File(STORAGE_PATH+"/hosts/"+"ignored_file.txt"), "should be ignored");
		
		// when
		final List<String> hosts = this.backupStorage.readHosts();
		
		// then
		Assert.assertEquals(11, hosts.size());
		Assert.assertTrue(hosts.containsAll(Lists.newArrayList(
				"dashboard.ksf.local",
				"foreman.ksf.local",
				"jasig.ksf.local"
		)));
	}
	
	@Test
	public void testReadEmptyHosts() {
		
		// when
		final List<String> hosts = this.backupStorage.readHosts();
				
		// then
		Assert.assertTrue(hosts.isEmpty());	
	}
	
	@Test
	public void testGetRoot() {
		
		Assert.assertEquals(STORAGE_PATH, this.backupStorage.getRoot());
		
	}
	
}
