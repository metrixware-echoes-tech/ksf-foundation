package fr.echoes.labs.ksf.foreman.backup;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.foreman.api.model.ForemanHost;
import fr.echoes.labs.ksf.foundation.utils.URLUtils;

public class BackupStorage {
	
	private String backupFolderPath;
	
	public BackupStorage(final String backupFolderPath) {
		
		this.backupFolderPath = URLUtils.removeLastSlash(backupFolderPath);
	}
	
	public String getRoot() {
		
		return this.backupFolderPath;
	}
	
	public String getHostRootFolder() {
		
		return this.backupFolderPath+"/hosts/";
	}

	public String getHostFolder(final ForemanHost host) {
		
		return getHostRootFolder()+host.getName()+"/";
	}
	
	public String getHostGroupsFolder() {
		
		return backupFolderPath+"/hostgroups/";
	}
	
	public String getOsFolder() {
		
		return backupFolderPath+"/os/";
	}
	
	public String getDomainFolder() {
		
		return backupFolderPath+"/domains/";
	}
	
	public List<String> readHosts() {
		
		final String dirPath = this.getHostRootFolder();
		final List<String> hostNames = Lists.newArrayList();
		final File dirHosts = new File(dirPath);
		
		if (dirHosts.exists()) {
			for(final File file : dirHosts.listFiles()) {
				if (file.isDirectory()) {
					hostNames.add(file.getName());
				}
			}
		}
		
		return hostNames;
	}
	
}
