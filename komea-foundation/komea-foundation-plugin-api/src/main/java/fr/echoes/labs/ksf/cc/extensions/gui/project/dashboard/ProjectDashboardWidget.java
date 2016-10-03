package fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard;

import java.util.List;

public interface ProjectDashboardWidget {

    /**
     * Returns the dropdown actions.
     *
     * @return the dropdown actions.
     */
    List<MenuAction> getDropdownActions();

    String getHtmlPanelBody(String projectId);

    boolean hasHtmlPanelBody();

    String getIconUrl();

    String getTitle();

    List<IProjectTabPanel> getTabPanels(String projectKey);

    String getId();

}
