package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import java.util.List;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;

/**
 * This interface defines the widgets provided by springplugins to fill the project
 * dashboard page.
 *
 * @author sleroy
 *
 */
public interface IProjectDashboardExtensionManager {

	/**
	 * Get the modules panels.
	 *
	 * @return the list of panels.
	 */
	List<IProjectTabPanel> getDashboardPanels(String projectKey);

	/**
	 * Gets the dashboard widget.
	 *
	 * @param widgetID
	 *            the widget JENKINS_PLUGIN
	 * @return the dashboard widget
	 */
	ProjectDashboardWidget getDashboardWidget(String widgetID) throws ProjectDashboardWidgetNotFoundException;

	/**
	 * Get the widget dashboards
	 *
	 * @return the list of widget dashboards.
	 */
	List<ProjectDashboardWidget> getDashboardWidgets();
}
