package fr.echoes.labs.ksf.cc.ui.views.projects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Function;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;

public class ProjectDashboardWidgetDTOFunction implements Function<ProjectDashboardWidget, ProjectDashboardWidgetDto> {

	private final HttpServletRequest	request;
	private final HttpServletResponse	response;
	private final String				projectID;

	public ProjectDashboardWidgetDTOFunction(final String _projectID, final HttpServletRequest _request, final HttpServletResponse _response) {
		super();
		this.projectID = _projectID;
		this.request = _request;
		this.response = _response;
	}

	@Override
	public ProjectDashboardWidgetDto apply(final ProjectDashboardWidget input) {
		final ProjectDashboardWidgetDto projectDashboardWidgetDto = new ProjectDashboardWidgetDto();
		projectDashboardWidgetDto.setDropdownActions(input.getDropdownActions());
		projectDashboardWidgetDto.setHtmlPanelBody(input.getHtmlPanelBody(this.projectID, this.request, this.response));
		projectDashboardWidgetDto.setIconURL(input.getIconUrl());
		projectDashboardWidgetDto.setTitle(input.getTitle());
		projectDashboardWidgetDto.setWidgetID(input.getWidgetId());
		return projectDashboardWidgetDto;
	}

}
