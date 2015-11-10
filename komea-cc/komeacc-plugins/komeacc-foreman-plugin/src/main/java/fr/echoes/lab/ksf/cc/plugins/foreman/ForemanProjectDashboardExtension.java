package fr.echoes.lab.ksf.cc.plugins.foreman;

import com.echoeslab.ksf.extensions.annotations.Extension;

import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardWidgets;

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
