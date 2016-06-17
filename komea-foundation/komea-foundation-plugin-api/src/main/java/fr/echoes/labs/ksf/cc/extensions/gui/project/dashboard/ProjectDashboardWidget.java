package fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard;

import java.util.List;

public interface ProjectDashboardWidget {

	/**
	 * Returns the dropdown actions.
	 *
	 * @return the dropdown actions.
	 */
	List<MenuAction> getDropdownActions();

	String getHtmlPanelBody(String projectId);

	String getIconUrl();

	List<IProjectTabPanel> getTabPanels(String projectKey);

	String getTitle();

	/**
	 * Gets the widget id to request the content by ajax.
	 *
	 * @return the widget id
	 */
	String getWidgetId();

}