package fr.echoes.labs.ksf.foreman.backup;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.echoes.labs.ksf.foreman.api.model.ForemanHost;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterWrapper;
import fr.echoes.labs.ksf.foreman.api.model.SmartVariableWrapper;

public class SmartVariableBackupService extends CsvBackupService<SmartVariableWrapper> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmartVariableBackupService.class);
	
	private static final String FILENAME_SUFFIX = "-variables.csv"; 
	private static final String FILENAME_GLOBAL_VALUES = "smart_variables.csv";

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
			super.write(values, HEADER, dirPath, FILENAME_GLOBAL_VALUES);
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
	
	public List<SmartVariableWrapper> read() throws IOException {
		
		final String dirPath = this.storage.getRoot()+"/";
		final String filePath = dirPath+FILENAME_GLOBAL_VALUES;
		
		if (new File(filePath).exists()) {
			return super.read(filePath, SmartVariableWrapper.class);
		}
		
		return Lists.newArrayList();
	}
	
	public Map<String, List<SmartVariableWrapper>> readHostGroupValues() throws IOException {
		
		final File hostGroupFolder = new File(this.storage.getHostGroupsFolder());
		final Map<String, List<SmartVariableWrapper>> results = Maps.newHashMap();
		
		if (hostGroupFolder.exists()) {
		
			final Pattern pattern = Pattern.compile("(.*)"+FILENAME_SUFFIX);
			
			for(final File file : hostGroupFolder.listFiles()) {
				final Matcher matcher = pattern.matcher(file.getName());
				if (matcher.find()) {
					final String hostGroup = decodeFileName(matcher.group(1));
					results.put(hostGroup, readHostGroupValues(hostGroup));
				}
			}
			
		}
		
		return results;
	}
	
	public List<SmartVariableWrapper> readHostGroupValues(final String hostGroup) throws IOException {
		
		final String dirPath = this.storage.getHostGroupsFolder();
		final String filePath = dirPath + hostGroup + FILENAME_SUFFIX;
		
		if (new File(filePath).exists()) {
			return super.read(filePath, SmartVariableWrapper.class);
		}
		
		return Lists.newArrayList();
	}
	
	public List<SmartVariableWrapper> readHostValues(final ForemanHost host) throws IOException {
		
		final String dirPath = this.storage.getHostFolder(host);
		final String filePath = dirPath + host.getName() + FILENAME_SUFFIX;
		
		if (new File(filePath).exists()) {
			return super.read(filePath, SmartVariableWrapper.class);
		}
		
		return Lists.newArrayList();
	}
	
}
