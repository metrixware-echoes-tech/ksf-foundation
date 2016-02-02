package fr.echoes.lab.ksf.cc.plugins.redmine.services;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.ProjectFactory;

import fr.echoes.lab.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.lab.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.lab.ksf.cc.plugins.redmine.RedmineQuery;



/**
 * Spring Service for working with the foreman API.
 *
 * @author DCD
 *
 */
@Service
public class RedmineService implements IRedmineService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineService.class);

	@Autowired
	private RedmineConfigurationService configurationService;

	private RedmineManager createRedmineManager() {
		final String apiAccessKey = this.configurationService.getApiAccessKey();
		final String url = this.configurationService.getUrl();
		final RedmineManager mgr = RedmineManagerFactory.createWithApiKey(url, apiAccessKey);
		return mgr;
	}

	@Override
	public void createProject(String projectName) throws RedmineExtensionException {
		Objects.requireNonNull(projectName);

		final RedmineManager mgr = createRedmineManager();
		final ProjectManager projectManager = mgr.getProjectManager();
		final Project project = ProjectFactory.create(projectName, projectName);

		try {
			projectManager.createProject(project);
		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to create projet " + projectName, e);
		}
	}


	@Override
	public void deleteProject(String projectName) throws RedmineExtensionException {
		Objects.requireNonNull(projectName);

		final RedmineManager mgr = createRedmineManager();
		final ProjectManager projectManager = mgr.getProjectManager();
		try {
			projectManager.deleteProject(projectName);
		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to delete projet " + projectName, e);
		}

	}

	@Override
	public List<RedmineIssue> queryIssues(RedmineQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

}
