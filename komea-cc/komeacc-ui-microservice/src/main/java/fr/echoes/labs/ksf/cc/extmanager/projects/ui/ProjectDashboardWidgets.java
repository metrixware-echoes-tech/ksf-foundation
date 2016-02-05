package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardWidgets;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;

public class ProjectDashboardWidgets implements IProjectDashboardWidgets {

	private final List<ProjectDashboardWidget> widgets = new ArrayList<>();

	@Override
	public void addWidget(final ProjectDashboardWidget _widget) {
		if (_widget == null) {
			return;
		}

		widgets.add(_widget);
	}

	public List<ProjectDashboardWidget> getWidgets() {
		return Collections.unmodifiableList(widgets);
	}

}
