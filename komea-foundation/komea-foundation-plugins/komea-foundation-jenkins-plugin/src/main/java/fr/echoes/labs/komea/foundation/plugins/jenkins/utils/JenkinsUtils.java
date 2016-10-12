package fr.echoes.labs.komea.foundation.plugins.jenkins.utils;

import java.util.List;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

public final class JenkinsUtils {

	private JenkinsUtils() {
		// static class
	}
	
	public static void registerJob(final ProjectDto project, String jobName) {
		List<String> jobs;
		if (project.getOtherAttributes().containsKey(ProjectExtensionConstants.CI_JOBS_KEY)) {
			jobs = (List<String>) project.getOtherAttributes().get(ProjectExtensionConstants.CI_JOBS_KEY);
		}else{
			jobs = Lists.newArrayList();
		}
		jobs.add(jobName);
		registerJobs(project, jobs);
	}
	
	public static void registerJobs(final ProjectDto project, final List<String> jobs) {
		project.getOtherAttributes().put(ProjectExtensionConstants.CI_JOBS_KEY, jobs);
	}
	
}
