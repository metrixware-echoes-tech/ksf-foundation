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
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;

public class PuppetModulesBackupService extends CsvBackupService<PuppetClass> {

	private final static Logger LOGGER = LoggerFactory.getLogger(PuppetModulesBackupService.class);

	private final static String[] HEADER = new String[] {
			"moduleName",
			"name"
	};
	
	private final BackupStorage storage;
	
	public PuppetModulesBackupService(final BackupStorage storage) {
		
		this.storage = storage;
	}
	
	public String getFileName(final ForemanHost host) {
		
		return host.getName()+"-classes.csv";
	}
	
	public void writeHostClasses(final ForemanHost host, final List<PuppetClass> values) throws IOException {
		
		final String dirPath = this.storage.getHostFolder(host);
		
		if (values.isEmpty()) {
			LOGGER.info("No puppet classes found for host {}", host.getName());
		}else{
			LOGGER.info("Writing {} Puppet classes of host {} into {}", values.size(), host.getName(), dirPath);
			super.write(values, HEADER, dirPath, getFileName(host));
		}
	}
	
	public void writeHostGroupClasses(final String hostGroup, final List<PuppetClass> values) throws IOException {
		
		final String dirPath = this.storage.getHostGroupsFolder();
		
		if (values.isEmpty()) {
			LOGGER.info("No puppet classes found for host {}", hostGroup);
		}else{
			LOGGER.info("Writing {} Puppet classes of host {} into {}", values.size(), hostGroup, dirPath);
			super.write(values, HEADER, dirPath, hostGroup+"-classes.csv");
		}
	}
	
	public Map<String, List<PuppetClass>> readHostGroups() throws IOException {
		
		final String dirPath = this.storage.getHostGroupsFolder();		
		final Map<String, List<PuppetClass>> results = Maps.newHashMap();
		
		final Pattern pattern = Pattern.compile("(.*)-classes.csv");
		
		for(final File file : new File(dirPath).listFiles()) {
			final Matcher matcher = pattern.matcher(file.getName());
			if (matcher.find()) {
				final String hostGroup = matcher.group(1);
				final List<PuppetClass> values = super.read(file.getPath(), PuppetClass.class);
				results.put(hostGroup, values);
			}
		}
		
		return results;
	}
	
	public List<PuppetClass> readHostClasses(final ForemanHost host) throws IOException {
		
		final String dirPath = this.storage.getHostFolder(host);
		final String filePath = dirPath+host.getName()+"-classes.csv";
		
		if (new File(filePath).exists()) {
			return super.read(filePath, PuppetClass.class);
		}
		
		return Lists.newArrayList();
	}
	
}
