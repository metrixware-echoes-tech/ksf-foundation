package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import java.util.List;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;

/**
 * This interface defines the widgets provided by plugins to fill the project
 * dashboard page.
 *
 * @author sleroy
 *
 */
public interface IProjectDashboardExtensionManager {

	/**
	 * Get the widget dashboards
	 *
	 * @return the list of widget dashboards.
	 */
	List<ProjectDashboardWidget> getDashboardWidgets();

	/**
	 * Get the modules panels.
	 *
	 * @return the list of panels.
	 */
	List<IProjectTabPanel> getDashboardPanels(String projectKey);
}
