package fr.echoes.labs.ksf.cc.plugins.foreman.extensions;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardWidgets;
import fr.echoes.labs.ksf.extensions.annotations.Extension;

@Extension
public class ForemanProjectDashboardExtension implements IProjectDashboardExtension {

	@Autowired
	public ForemanProjectDashboardWidget projectDashboardWidget;

	@Override
	public void reclaimProjectDashboardWidget(final IProjectDashboardWidgets _widgets) {
		_widgets.addWidget(projectDashboardWidget);
	}

	@Override
	public String toString() {
		return "Foreman Dashboard Widget";
	}
}
