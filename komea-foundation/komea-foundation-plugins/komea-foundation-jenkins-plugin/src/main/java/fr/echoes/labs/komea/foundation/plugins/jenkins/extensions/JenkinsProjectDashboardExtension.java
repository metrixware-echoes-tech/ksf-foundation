package fr.echoes.labs.komea.foundation.plugins.jenkins.extensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardWidgets;
import fr.echoes.labs.pluginfwk.api.plugin.Plugin;

@Order(value=3)
@Plugin
public class JenkinsProjectDashboardExtension implements IProjectDashboardExtension {

	@Autowired
	public JenkinsProjectDashboardWidget projectDashboardWidget;

	@Override
	public void reclaimProjectDashboardWidget(final IProjectDashboardWidgets widgets) {
		widgets.addWidget(this.projectDashboardWidget);
	}

	@Override
	public String toString() {
		return "Jenkins Dashboard Widget";
	}
}
