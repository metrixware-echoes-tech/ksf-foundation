package fr.echoes.labs.ksf.foreman.backup;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.ksf.foreman.api.model.ForemanHost;
import fr.echoes.labs.ksf.foreman.api.model.SmartVariableWrapper;

public class SmartVariableBackupService extends CsvBackupService<SmartVariableWrapper> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmartVariableBackupService.class);
	
	private static final String FILENAME_SUFFIX = "-variables.csv"; 

	private static final String[] HEADER = new String[] {
			"variable",
			"puppetModule",
			"puppetClass",
			"type",
			"value"
	};
	
	private final BackupStorage storage;
	
	public SmartVariableBackupService(final BackupStorage storage) {
		this.storage = storage;
	}
	
	public void write(final List<SmartVariableWrapper> values) throws IOException {
		
		final String dirPath = this.storage.getRoot()+"/";
		
		if (values.isEmpty()) {
			LOGGER.info("No global smart variable found.");
		}else{
			LOGGER.info("Writing {} smart variable into {}", values.size(), dirPath);
			super.write(values, HEADER, dirPath, "smart_variables.csv");
		}
	}
	
	public void write(final ForemanHost host, final List<SmartVariableWrapper> values) throws IOException {
		
		final String folderPath = storage.getHostFolder(host);
		
		if (values.isEmpty()) {
			LOGGER.info("No smart variables found for host {}", host.getName());
		}else{
			
			LOGGER.info("Writing {} smart variables of host {} into {}", values.size(), host.getName(), folderPath);
			super.write(values, HEADER, folderPath, host.getName()+FILENAME_SUFFIX);
		}	
	}
	
	public void writeHostGroupValues(final String hostGroup, final List<SmartVariableWrapper> values) throws IOException {
		
		final String hostGroupFolderPath = this.storage.getHostGroupsFolder();
		
		if (values.isEmpty()) {
			LOGGER.info("No smart variables found for host group {}", hostGroup);
		}else{
			LOGGER.info("Writing {} smart variables of host group {} into {}", values.size(), hostGroup, hostGroupFolderPath);
			super.write(values, HEADER, hostGroupFolderPath, hostGroup+FILENAME_SUFFIX);
		}
	}
	
}
