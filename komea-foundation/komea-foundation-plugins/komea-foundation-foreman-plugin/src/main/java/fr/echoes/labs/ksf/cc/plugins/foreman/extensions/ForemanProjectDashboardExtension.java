package fr.echoes.labs.ksf.cc.plugins.foreman.extensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardWidgets;
import fr.echoes.labs.pluginfwk.api.plugin.Plugin;

@Order(value=6)
@Plugin
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
