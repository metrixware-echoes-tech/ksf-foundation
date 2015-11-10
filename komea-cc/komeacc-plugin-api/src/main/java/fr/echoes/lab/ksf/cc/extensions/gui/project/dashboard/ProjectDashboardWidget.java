package fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard;

import java.util.List;

public interface ProjectDashboardWidget {
	/**
	 * Returns the dropdown actions.
	 *
	 * @return the dropdown actions.
	 */
	List<MenuAction> getDropdownActions();

	String getHtmlPanelBody();

	String getIconUrl();

	String getTitle();

}