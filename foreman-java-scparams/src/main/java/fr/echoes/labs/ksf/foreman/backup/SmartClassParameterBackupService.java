package fr.echoes.labs.ksf.foreman.backup;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
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
import fr.echoes.labs.ksf.foreman.api.utils.Comparators;

public class SmartClassParameterBackupService extends CsvBackupService<SmartClassParameterWrapper> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmartClassParameterBackupService.class);

	private static final String[] HEADER = new String[] {
			"puppetModule",
			"puppetClass",
			"parameter",
			"value",
			"usePuppetDefault"
	};
	
	private final BackupStorage storage;
	
	public SmartClassParameterBackupService(final BackupStorage storage) {
		
		this.storage = storage;
	}

	public void write(final ForemanHost host, final List<SmartClassParameterWrapper> values) throws IOException {
		
		final String folderPath = storage.getHostFolder(host);
		
		if (values.isEmpty()) {
			LOGGER.info("No override values found for host {}", host.getName());
		}else{
			
			LOGGER.info("Writing {} override values of host {} into {}", values.size(), host.getName(), folderPath);
			sortThenWrite(values, folderPath, host.getName()+"-parameters.csv");
		}
	}
	
	public List<SmartClassParameterWrapper> read(final ForemanHost host) throws IOException {
		
		final String folderPath = storage.getHostFolder(host);
		
		if (new File(folderPath).exists()) {
			return super.read(folderPath+host.getName()+"-parameters.csv", SmartClassParameterWrapper.class);
		}
		
		return Lists.newArrayList();
	}
	 
	public void write(final List<SmartClassParameterWrapper> values) throws IOException {
		
		final String dirPath = this.storage.getRoot()+"/";
		
		if (values.isEmpty()) {
			LOGGER.info("No global override values found for host");
		}else{
			LOGGER.info("Writing {} override values into {}", values.size(), dirPath);
			sortThenWrite(values, dirPath, "puppet_classes_parameters.csv");
		}
	}
	
	public List<SmartClassParameterWrapper> read() throws IOException {
		
		final String dirPath = this.storage.getRoot()+"/";
		final String filePath = dirPath+"puppet_classes_parameters.csv";
		
		if (new File(filePath).exists()) {
			return super.read(filePath, SmartClassParameterWrapper.class);
		}
		
		return Lists.newArrayList();
	}
	
	public void writeHostGroupValues(final String hostGroup, final List<SmartClassParameterWrapper> values) throws IOException {
		
		final String hostGroupFolderPath = this.storage.getHostGroupsFolder();
		
		if (values.isEmpty()) {
			LOGGER.info("No override values found for host group {}", hostGroup);
		}else{
			LOGGER.info("Writing {} override values of host group {} into {}", values.size(), hostGroup, hostGroupFolderPath);
			sortThenWrite(values, hostGroupFolderPath, hostGroup+"-parameters.csv");
		}
	}
	
	public Map<String, List<SmartClassParameterWrapper>> readHostGroupValues() throws IOException {
		
		final File hostGroupFolder = new File(this.storage.getHostGroupsFolder());
		final Map<String, List<SmartClassParameterWrapper>> results = Maps.newHashMap();
		
		if (hostGroupFolder.exists()) {
		
			final Pattern pattern = Pattern.compile("(.*)-parameters.csv");
			
			for(final File file : hostGroupFolder.listFiles()) {
				final Matcher matcher = pattern.matcher(file.getName());
				if (matcher.find()) {
					final String hostGroup = matcher.group(1);
					final List<SmartClassParameterWrapper> values = super.read(file.getPath(), SmartClassParameterWrapper.class);
					results.put(hostGroup, values);
				}
			}
		
		}
		
		return results;
	}
	
	public void writeOsValues(final String os, final List<SmartClassParameterWrapper> values) throws IOException {
		
		final String folderPath = this.storage.getOsFolder();
		
		if (values.isEmpty()) {
			LOGGER.info("No override values found for os {}", os);
		}else{
			LOGGER.info("Writing {} override values of os {} into {}", values.size(), os, folderPath);
			sortThenWrite(values, folderPath, os+".csv");
		}
	}
	
	public void writeDomainValues(final String domain, final List<SmartClassParameterWrapper> values) throws IOException {
		
		final String folderPath = this.storage.getDomainFolder();
		
		if (values.isEmpty()) {
			LOGGER.info("No override values found for domain {}", domain);
		}else{
			LOGGER.info("Writing {} override values of domain {} into {}", values.size(), domain, folderPath);
			sortThenWrite(values, folderPath, domain+".csv");
		}
	}
	
	private void sortThenWrite(final List<SmartClassParameterWrapper> values, final String folderPath, final String fileName) throws IOException {
		
		Collections.sort(values, Comparators.smartClassParameterComparator());	
		super.write(values, HEADER, folderPath, fileName);
	}
	
}
