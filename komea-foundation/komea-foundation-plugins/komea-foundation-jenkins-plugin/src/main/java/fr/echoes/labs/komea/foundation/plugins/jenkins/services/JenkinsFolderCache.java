package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.offbytwo.jenkins.model.FolderJob;

enum JenkinsFolderCache {

	INSTANCE;

	private static final long MAX_SIZE = 200;
	private final Cache<String, FolderJob> cache;

	private JenkinsFolderCache() {
		this.cache = CacheBuilder.newBuilder()
		.maximumSize(MAX_SIZE)
	    .expireAfterWrite(30, TimeUnit.HOURS)
	    .build();
	}


	/**
	 * Returns the {@link FolderJob} for the given project name.
	 * @param projectname the project name.
	 * @return the job folder or {@code null} if there is no cached value for the project name.
	 */
	public FolderJob get(String projectname) {
		return this.cache.getIfPresent(projectname);
	}

	/**
	 * Stores the {@link FolderJob} for the given projectname.
	 * @param username the projectname.
	 * @param folder the job folder.
	 */
	public void put(String projectname, FolderJob folder) {
		this.cache.put(projectname, folder);
	}
}
