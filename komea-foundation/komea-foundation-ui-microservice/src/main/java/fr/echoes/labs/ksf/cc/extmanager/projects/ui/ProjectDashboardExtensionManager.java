package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;

/**
 * This service manages the plugins that alter the poject dashboard page;
 *
 * @author sleroy
 *
 */
@Service("projectDashboardWidgets")
public class ProjectDashboardExtensionManager implements IProjectDashboardExtensionManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDashboardExtensionManager.class);

	@Autowired(required = false)
	private IProjectDashboardExtension[] extensions;

	@Override
	public List<ProjectDashboardWidget> getDashboardWidgets() {

		if (extensions == null) {
			LOGGER.info("GetDashboardWidgets : no extensions");
			return Lists.newArrayList();
		}
		LOGGER.info("GetDashboardWidgets : extensions {}", extensions.length);
		final ProjectDashboardWidgets projectDashboardWidgets = new ProjectDashboardWidgets();
		for (final IProjectDashboardExtension extension : extensions) {
			LOGGER.info("GetDashboardWidgets : extension {}", extension);
			extension.reclaimProjectDashboardWidget(projectDashboardWidgets);
		}
		return projectDashboardWidgets.getWidgets();
	}

}
