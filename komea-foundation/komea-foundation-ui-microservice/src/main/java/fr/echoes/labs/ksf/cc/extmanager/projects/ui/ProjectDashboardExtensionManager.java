package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import com.google.common.collect.Lists;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service manages the plugins that alter the poject dashboard page;
 *
 * @author sleroy
 *
 */
@Service("projectDashboardWidgets")
public class ProjectDashboardExtensionManager implements IProjectDashboardExtensionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDashboardExtensionManager.class);

    @Autowired(required = false)
    private IProjectDashboardExtension[] extensions;

    @Override
    public List<ProjectDashboardWidget> getDashboardWidgets() {

        if (this.extensions == null) {
            return Lists.newArrayList();
        }
        final ProjectDashboardWidgets projectDashboardWidgets = new ProjectDashboardWidgets();
        for (final IProjectDashboardExtension extension : this.extensions) {
            extension.reclaimProjectDashboardWidget(projectDashboardWidgets);
        }
        return projectDashboardWidgets.getWidgets();
    }

    @Override
    public List<IProjectTabPanel> getDashboardPanels(String projectKey) {
        final List<ProjectDashboardWidget> widgets = getDashboardWidgets();
        final List<IProjectTabPanel> panels = Lists.newArrayList();

        for (final ProjectDashboardWidget widget : widgets) {
            panels.addAll(widget.getTabPanels(projectKey));
        }

        return panels;
    }

}
