package fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard;

/**
 * This extension points allows a plugin to add a new widget to the project
 * dashboard.
 *
 * @author sleroy
 *
 */
public interface IProjectDashboardExtension {
	
	void reclaimProjectDashboardWidget(IProjectDashboardWidgets _widgets);
}
