package fr.echoes.labs.komea.foundation.plugins.nexus.extensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardWidgets;
import fr.echoes.labs.ksf.extensions.annotations.Extension;

/**
 * @author dcollard
 *
 */
@Order(value=5)
@Extension
public class NexusProjectDashboardExtension implements IProjectDashboardExtension {

	@Autowired
	public NexusProjectDashboardWidget projectDashboardWidget;

	@Override
	public void reclaimProjectDashboardWidget(final IProjectDashboardWidgets widgets) {
		widgets.addWidget(this.projectDashboardWidget);
	}

	@Override
	public String toString() {
		return "Nexus Dashboard Widget";
	}
}
