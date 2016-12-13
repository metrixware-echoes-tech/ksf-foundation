package fr.echoes.labs.ksf.foreman.backup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public abstract class AbstractBackupStorageTest {

	protected static final String STORAGE_PATH = "build/tmp/dataset";
	protected static final String SOURCE_PATH = "src/test/resources/dataset/";
	
	protected BackupStorage backupStorage;
	
	public void setup() throws IOException {
		
		FileUtils.deleteDirectory(new File(STORAGE_PATH));
		new File(STORAGE_PATH).mkdirs();
		
		this.backupStorage = new BackupStorage(STORAGE_PATH);		
	}
	
	public void initDataset() throws IOException {
		FileUtils.copyDirectory(new File(SOURCE_PATH), new File(STORAGE_PATH));
	}
	
}
