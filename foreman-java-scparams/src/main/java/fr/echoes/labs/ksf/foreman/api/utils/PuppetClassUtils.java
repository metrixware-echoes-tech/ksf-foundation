package fr.echoes.labs.ksf.foreman.api.utils;

import java.util.Collection;
import java.util.List;

import fr.echoes.labs.ksf.foreman.api.model.ForemanHostGroup;
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;

public final class PuppetClassUtils {
	
	private PuppetClassUtils() {
		// static class
	}
	
	public static PuppetClass findPuppetClassById(final List<PuppetClass> puppetClasses, Integer id) {
		
		for(final PuppetClass puppetClass : puppetClasses) {
			if (id.equals(puppetClass.getId())) {
				return puppetClass;
			}
		}
		
		return null;
	}
	
	public static boolean hasHostGroup(final PuppetClass puppetClass, final ForemanHostGroup hostGroup) {
		
		final List<ForemanHostGroup> hostGroups = puppetClass.getHostGroups();
		
		if (hostGroups != null && !hostGroups.isEmpty()) {	
			for(final ForemanHostGroup group : hostGroups) {
				if (hostGroup.getId().equals(group.getId())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static PuppetClass findPuppetClass(final Collection<PuppetClass> puppetClasses, final String className, final String moduleName) {
		
		for(final PuppetClass puppetClass : puppetClasses) {
			if (className.equals(puppetClass.getName()) && moduleName.equals(puppetClass.getModuleName())) {
				return puppetClass;
			}
		}
		
		return null;
	}
	
}
