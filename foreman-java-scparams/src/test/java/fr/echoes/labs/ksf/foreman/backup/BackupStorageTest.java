package fr.echoes.labs.ksf.foreman.backup;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class BackupStorageTest {

	private static final String STORAGE_PATH = "build/tmp/dataset";
	private static final String SOURCE_PATH = "src/test/resources/dataset/";
	
	private BackupStorage backupStorage;
	
	@Before
	public void setup() throws IOException {
		
		FileUtils.deleteDirectory(new File(STORAGE_PATH));
		new File(STORAGE_PATH).mkdirs();
		
		this.backupStorage = new BackupStorage(STORAGE_PATH);
	}
	
	
	private void initDataset() throws IOException {
		FileUtils.copyDirectory(new File(SOURCE_PATH), new File(STORAGE_PATH));
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
