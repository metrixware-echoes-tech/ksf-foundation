package fr.echoes.lab.ksf.cc.projects.extmanager.ui;

import java.util.List;

import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;

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
}
