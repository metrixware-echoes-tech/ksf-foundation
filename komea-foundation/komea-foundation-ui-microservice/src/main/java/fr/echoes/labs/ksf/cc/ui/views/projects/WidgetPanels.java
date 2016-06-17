package fr.echoes.labs.ksf.cc.ui.views.projects;

import java.util.List;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;

public class WidgetPanels {

	private final List<IProjectTabPanel> tabPanels;

	/**
	 * Instantiates a new widget panels.
	 *
	 * @param tabPanels
	 *            the tab panels
	 */
	public WidgetPanels(final List<IProjectTabPanel> tabPanels) {
		this.tabPanels = tabPanels;
	}

	/**
	 * Gets the tab panels.
	 *
	 * @return the tab panels
	 */
	public List<IProjectTabPanel> getTabPanels() {
		return this.tabPanels;
	}

}
