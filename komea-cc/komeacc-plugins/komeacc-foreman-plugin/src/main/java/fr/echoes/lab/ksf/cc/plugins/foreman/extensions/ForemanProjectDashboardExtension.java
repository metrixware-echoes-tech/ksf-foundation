package fr.echoes.lab.ksf.cc.plugins.foreman.extensions;

import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardWidgets;
import fr.echoes.lab.ksf.extensions.annotations.Extension;

@Extension
public class ForemanProjectDashboardExtension implements IProjectDashboardExtension {
	
	@Override
	public void reclaimProjectDashboardWidget(final IProjectDashboardWidgets _widgets) {
		_widgets.addWidget(new ForemanProjectDashboardWidget());
	}
	
	@Override
	public String toString() {
		return "Foreman Dashboad Widget";
	}
}
