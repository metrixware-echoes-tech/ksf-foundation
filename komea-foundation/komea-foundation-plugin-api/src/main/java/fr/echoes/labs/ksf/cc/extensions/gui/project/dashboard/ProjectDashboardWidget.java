package fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ProjectDashboardWidget {

	/**
	 * Returns the dropdown actions.
	 *
	 * @return the dropdown actions.
	 */
	List<MenuAction> getDropdownActions();

	/**
	 * Gets the html panel body.
	 *
	 * @param projectId
	 *            the project id
	 * @param _request
	 *            the request
	 * @param _response
	 *            the response
	 * @return the html panel body
	 */
	String getHtmlPanelBody(String projectId, HttpServletRequest _request, HttpServletResponse _response);

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