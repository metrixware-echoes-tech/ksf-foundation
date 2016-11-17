package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.extensions.api.ExtensionResolver;
import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;

/**
 * This service manages the plugins that alter the poject dashboard page;
 *
 * @author sleroy
 *
 */
@Service("projectDashboardWidgets")
public class ProjectDashboardExtensionManager implements IProjectDashboardExtensionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDashboardExtensionManager.class);
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private ExtensionManager extensionManager;
    
    private List<IProjectDashboardExtension> extensions;

    public Collection<IProjectDashboardExtension> getExtensions() {
    	
    	if (this.extensions == null) {
    		final ExtensionResolver<IProjectDashboardExtension> extensionResolver = new ExtensionResolver<IProjectDashboardExtension>(this.applicationContext, this.extensionManager);
        	this.extensions = extensionResolver.findExtensions(IProjectDashboardExtension.class);
    	}
    	
    	return extensions;
    }
    
    @Override
    public List<ProjectDashboardWidget> getDashboardWidgets() {

    	final Collection<IProjectDashboardExtension> extensions = getExtensions();
    			
        final ProjectDashboardWidgets projectDashboardWidgets = new ProjectDashboardWidgets();
        for (final IProjectDashboardExtension extension : extensions) {
            extension.reclaimProjectDashboardWidget(projectDashboardWidgets);
        }
        return projectDashboardWidgets.getWidgets();
    }

    @Override
    public List<IProjectTabPanel> getDashboardPanels(final String projectKey) {
        final List<ProjectDashboardWidget> widgets = this.getDashboardWidgets();
        final List<IProjectTabPanel> panels = Lists.newArrayList();

        for (final ProjectDashboardWidget widget : widgets) {
            panels.addAll(widget.getTabPanels(projectKey));
        }

        return panels;
    }

}
