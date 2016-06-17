package fr.echoes.labs.komea.foundation.plugins.git.extensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardWidgets;
import fr.echoes.labs.ksf.extensions.annotations.Extension;

@Order(value=2)
@Extension
public class GitProjectDashboardExtension implements IProjectDashboardExtension {

	@Autowired
	public GitProjectDashboardWidget projectDashboardWidget;

	@Override
	public void reclaimProjectDashboardWidget(final IProjectDashboardWidgets widgets) {
		widgets.addWidget(this.projectDashboardWidget);
	}

	@Override
	public String toString() {
		return "Git Dashboard Widget";
	}
}
