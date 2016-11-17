package fr.echoes.labs.ksf.cc.plugins.dashboard.extensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardWidgets;
import fr.echoes.labs.ksf.extensions.annotations.Extension;

/**
 * @author dcollard
 *
 */
@Order(value=10)
@Extension
public class DashboardProjectDashboardExtension implements IProjectDashboardExtension {

	@Autowired
	public DashboardProjectDashboardWidget projectDashboardWidget;

	@Override
	public void reclaimProjectDashboardWidget(final IProjectDashboardWidgets widgets) {
		widgets.addWidget(this.projectDashboardWidget);
	}

	@Override
	public String toString() {
		return "Dashboard Widget";
	}
}
