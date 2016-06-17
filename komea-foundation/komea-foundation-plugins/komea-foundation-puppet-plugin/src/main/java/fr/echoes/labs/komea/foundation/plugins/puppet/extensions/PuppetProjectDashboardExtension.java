package fr.echoes.labs.komea.foundation.plugins.puppet.extensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardWidgets;
import fr.echoes.labs.pluginfwk.api.plugin.Extension;

@Order(value = 7)
@Extension
public class PuppetProjectDashboardExtension implements IProjectDashboardExtension {

	@Autowired
	public PuppetProjectDashboardWidget projectDashboardWidget;

	@Override
	public void reclaimProjectDashboardWidget(final IProjectDashboardWidgets widgets) {
		widgets.addWidget(this.projectDashboardWidget);
	}

	@Override
	public String toString() {
		return "Puppet Dashboard Widget";
	}
}
