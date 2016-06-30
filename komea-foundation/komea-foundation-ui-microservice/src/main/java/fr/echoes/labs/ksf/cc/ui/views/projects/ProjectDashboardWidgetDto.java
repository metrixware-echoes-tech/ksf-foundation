package fr.echoes.labs.ksf.cc.ui.views.projects;

import java.util.List;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;

public class ProjectDashboardWidgetDto {

	private String				title;
	private String				iconURL;
	private String				htmlPanelBody;
	private List<MenuAction>	dropdownActions;
	private String				widgetID;

	/**
	 * Returns the dropdown actions.
	 *
	 * @return the dropdown actions.
	 */
	public List<MenuAction> getDropdownActions() {
		return this.dropdownActions;
	}

	/**
	 * Gets the html panel body.
	 *
	 * @param projectId
	 *            the project id
	 * @return the html panel body
	 */
	public String getHtmlPanelBody() {
		return this.htmlPanelBody;
	}

	public String getIconUrl() {
		return this.iconURL;
	}

	public String getIconURL() {
		return this.iconURL;
	}

	public String getTitle() {
		return this.title;
	}

	/**
	 * Gets the widget id to request the content by ajax.
	 *
	 * @return the widget id
	 */
	public String getWidgetId() {
		return this.widgetID;
	}

	public String getWidgetID() {
		return this.widgetID;
	}

	public void setDropdownActions(final List<MenuAction> dropdownActions) {
		this.dropdownActions = dropdownActions;
	}

	public void setHtmlPanelBody(final String htmlPanelBody) {
		this.htmlPanelBody = htmlPanelBody;
	}

	public void setIconURL(final String iconURL) {
		this.iconURL = iconURL;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setWidgetID(final String widgetID) {
		this.widgetID = widgetID;
	}

}
